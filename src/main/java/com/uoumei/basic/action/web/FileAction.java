package com.uoumei.basic.action.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uoumei.base.constant.Const;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.cms.util.PathUtil;

/**
 * 上传文件
 */
@Controller
@RequestMapping("/fileuploader")
public class FileAction extends BaseAction {

	private static final int BUFFER_SIZE = 100 * 1024;
	private static final String TEMP = "/temp";

	/**
	 * 上传路径
	 */
	@Value("${upload.floder.path}")
	private String uploadFloderPath;

	@Value("${upload.max.size}")
	private int uploadMaxSize;

	@Value("${upload.file.allowed}")
	private String uploadFileAllowed;

	@Value("${upload.file.denied}")
	private String uploadFileDenied;

	@Value("${upload.max.in.memory.size}")
	private int uploadMaxInMemorySize;

	/**
	 * 处理post请求上传文件
	 * 
	 * @param req
	 *            HttpServletRequest对象
	 * @param res
	 *            HttpServletResponse 对象
	 * @throws ServletException
	 *             异常处理
	 * @throws IOException
	 *             异常处理
	 */
	@PostMapping("/upload")
	@ResponseBody
	public void upload(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		Integer chunk = 0, chunks = 0; // 分块上传
		try {
			PrintWriter out = res.getWriter();
			String uploadPath = "";
			String floderName = com.uoumei.basic.constant.Const.UPLOAD_PATH;

			// --zzq
//			String floderName = com.uoumei.basic.constant.Const.UPLOAD_PATH + Const.SEPARATOR + BasicUtil.getAppId();
			ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(req);
			if(null != managerSession  && managerSession.getBasicId()>0){
				 floderName += Const.SEPARATOR + managerSession.getBasicId();
			}
			
			String uploadFolder = BasicUtil.getRealPath(floderName) + Const.SEPARATOR; // 上传的文件路径
			String isRename = req.getParameter("isRename");// 是否重命名 true:重命名
			String _tempPath = req.getServletContext().getRealPath(TEMP);// 存放文件的临时目录路径
			FileUtil.createFolder(_tempPath);
			File tempPath = new File(_tempPath); // 用于存放临时文件的目录
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// maximum size that will be stored in memory
			// 允许设置内存中存储数据的门限，单位：字节
			factory.setSizeThreshold(uploadMaxInMemorySize);
			// the location for saving data that is larger than
			// getSizeThreshold()
			// 如果文件大小大于SizeThreshold，则保存到临时目录
			factory.setRepository(tempPath);

			ServletFileUpload upload = new ServletFileUpload(factory);
			// maximum size before a FileUploadException will be thrown
			try {
				List fileItems = upload.parseRequest(req);

				Iterator iter = fileItems.iterator();

				// 正则匹配，过滤路径取文件名
				String regExp = ".+\\\\(.+)$";

				// 过滤掉的文件类型
				String[] errorType = uploadFileDenied.split(",");
				String fileName = "";
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					InputStream input = item.getInputStream();
					String fieldName = item.getFieldName();
					if (fieldName.equals("uploadFloderPath")) {
						uploadFloderPath = Streams.asString(input);
						uploadFolder = BasicUtil.getRealPath(uploadFloderPath); // 上传的文件路径
						if (!StringUtils.isEmpty(uploadPath)) {
							uploadFolder += uploadPath;
						}
						floderName = uploadFloderPath;
						LOG.info("uploadPath:" + uploadFloderPath);
					}
					if (fieldName.equals("uploadPath")) {
						uploadPath = Streams.asString(input);
						uploadFolder += uploadPath;
						LOG.info("uploadPath:" + uploadFolder);
					} else if (fieldName.equals("isRename")) {
						isRename = Streams.asString(input);
						LOG.info("isRename:" + isRename);
					} else if (fieldName.equals("maxSize")) {
						uploadMaxSize = Integer.parseInt(item.getString()) * 1024;
						LOG.info("maxSize:" + uploadMaxSize);
					} else if (fieldName.equals("allowedFile")) {
						uploadFileAllowed = item.getString();
						LOG.info("uploadFileAllowed:" + uploadFileAllowed);

					} else if (fieldName.equals("deniedFile")) {
						LOG.info("uploadFileDenied:" + uploadFileDenied);
						uploadFileDenied = item.getString();
					} else if ("chunk".equals(fieldName)) {
						chunk = Integer.valueOf(Streams.asString(input));
						LOG.info("chunk:" + chunk);
					} else if ("chunks".equals(fieldName)) {
						chunks = Integer.valueOf(Streams.asString(input));
						LOG.info("chunks:" + chunks);
					} else if ("name".equals(fieldName)) {
						fileName = new String(item.get(), "UTF-8");
						LOG.info("name:" + fileName);
					} else if (!item.isFormField()) { // 忽略其他不是文件域的所有表单信息
						if (StringUtil.isBlank(fileName)) {
							fileName = item.getName();
						}
						long size = item.getSize();
						if ((fileName == null || fileName.equals("")) && size == 0)
							continue;
						try {
							// 最大上传文件，单位：字节 1000000/1024=0.9M
							upload.setSizeMax(uploadMaxSize);

							// 保存上传的文件到指定的目录
							// 在下文中上传文件至数据库时，将对这里改写
							String folder = uploadFolder + Const.SEPARATOR + TEMP;
							FileUtil.createFolder(folder);

							File destFile = new File(folder, fileName);
							// //文件已存在删除旧文件（上传了同名的文件）
							if (chunk == 0 && destFile.exists()) {
								destFile.delete();
								destFile = new File(folder, fileName);
							}
							// 合成文件
							appendFile(input, destFile);
							if (chunk == chunks - 1) {
								String _fileName = fileName;
								// // 重命名
								if (StringUtil.isBlank(isRename) || Boolean.parseBoolean(isRename)) {
									_fileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("."));
									destFile.renameTo(new File(uploadFolder, _fileName));
								} else {
									// savePath += fileName;
									// outPath += fileName;
									destFile.renameTo(new File(uploadFolder, _fileName));
								}
								LOG.info("上传完成");
								if (uploadPath.equals("/") || StringUtil.isBlank(uploadPath)) {
									out.print(floderName + _fileName);
								} else {
									out.print(floderName + uploadPath + Const.SEPARATOR + _fileName);
								}
								new File(folder).delete();
							} else if (chunks == 0) {
								String _fileName = fileName;
								if (StringUtil.isBlank(isRename) || Boolean.parseBoolean(isRename)) {
									_fileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("."));
								}
								destFile.renameTo(new File(uploadFolder, _fileName));
								new File(folder).delete();
								if (uploadPath.equals("/") || StringUtil.isBlank(uploadPath)) {
									out.print(floderName + _fileName);
								} else {
									out.print(floderName + Const.SEPARATOR + uploadPath + Const.SEPARATOR + _fileName);
								}
								LOG.info("上传完成");
							} else {
								LOG.info("还剩[" + (chunks - 1 - chunk) + "]个块文件");

							}

							LOG.debug("upload file ok return path " + uploadFolder + fileName);

							try {
								if (null != input) {
									input.close();
								}
								if (null != out) {
									out.flush();
									out.close();
								}
							} catch (IOException e) {
								LOG.error(e.getMessage());
							}
						} catch (Exception e) {
							this.LOG.debug(e);
						}

					}
				}
			} catch (FileUploadException e) {
				this.LOG.debug(e);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@PostMapping("/tmpupload")
	@ResponseBody
	public void tmpUpload(HttpServletRequest req, HttpServletResponse res) {
		res.setContentType("text/html;charset=utf-8");
		Integer chunk = 0, chunks = 0; // 分块上传
		try {
			PrintWriter out = res.getWriter();
			String uploadPath = "";
			String floderName = com.uoumei.basic.constant.Const.UPLOAD_PATH;
			String uploadFolder = BasicUtil.getRealPath(floderName) + Const.SEPARATOR; // 上传的文件路径
			
			String isRename = req.getParameter("isRename");// 是否重命名 true:重命名

			DiskFileItemFactory factory = new DiskFileItemFactory();
			// maximum size that will be stored in memory
			// 允许设置内存中存储数据的门限，单位：字节
			factory.setSizeThreshold(uploadMaxInMemorySize);
			// the location for saving data that is larger than
			// getSizeThreshold()
			
			// 如果文件大小大于SizeThreshold，则保存到临时目录
//			String _tempPath = req.getServletContext().getRealPath(TEMP);// 存放文件的临时目录路径
//			FileUtil.createFolder(_tempPath);
//			File tempPath = new File(_tempPath); // 用于存放临时文件的目录
			//factory.setRepository(tempPath);

			ServletFileUpload upload = new ServletFileUpload(factory);
			// maximum size before a FileUploadException will be thrown
			try {
				List fileItems = upload.parseRequest(req);

				Iterator iter = fileItems.iterator();

				// 正则匹配，过滤路径取文件名
				String regExp = ".+\\\\(.+)$";

				// 过滤掉的文件类型
				String[] errorType = uploadFileDenied.split(",");
				String fileName = "";
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					InputStream input = item.getInputStream();
					String fieldName = item.getFieldName();
					if (fieldName.equals("uploadFloderPath")) {
						uploadFloderPath = Streams.asString(input);
						//--zzq
						//uploadFolder = BasicUtil.getRealPath(uploadFloderPath); // 上传的文件路径
						uploadFolder = PathUtil.getTmpFullPath(uploadFloderPath); // 上传的文件路径
						if (!StringUtils.isEmpty(uploadPath)) {
							uploadFolder += uploadPath;
						}
						floderName = uploadFloderPath;
						LOG.info("uploadPath:" + uploadFloderPath);
					}
					if (fieldName.equals("uploadPath")) {
						uploadPath = Streams.asString(input);
						uploadFolder += uploadPath;
						LOG.info("uploadPath:" + uploadFolder);
					} else if (fieldName.equals("isRename")) {
						isRename = Streams.asString(input);
						LOG.info("isRename:" + isRename);
					} else if (fieldName.equals("maxSize")) {
						uploadMaxSize = Integer.parseInt(item.getString()) * 1024;
						LOG.info("maxSize:" + uploadMaxSize);
					} else if (fieldName.equals("allowedFile")) {
						uploadFileAllowed = item.getString();
						LOG.info("uploadFileAllowed:" + uploadFileAllowed);

					} else if (fieldName.equals("deniedFile")) {
						LOG.info("uploadFileDenied:" + uploadFileDenied);
						uploadFileDenied = item.getString();
					} else if ("chunk".equals(fieldName)) {
						chunk = Integer.valueOf(Streams.asString(input));
						LOG.info("chunk:" + chunk);
					} else if ("chunks".equals(fieldName)) {
						chunks = Integer.valueOf(Streams.asString(input));
						LOG.info("chunks:" + chunks);
					} else if ("name".equals(fieldName)) {
						fileName = new String(item.get(), "UTF-8");
						LOG.info("name:" + fileName);
					} else if (!item.isFormField()) { // 忽略其他不是文件域的所有表单信息
						if (StringUtil.isBlank(fileName)) {
							fileName = item.getName();
						}
						long size = item.getSize();
						if ((fileName == null || fileName.equals("")) && size == 0)
							continue;
						try {
							// 最大上传文件，单位：字节 1000000/1024=0.9M
							upload.setSizeMax(uploadMaxSize);

							// 保存上传的文件到指定的目录
							// 在下文中上传文件至数据库时，将对这里改写
							String folder = uploadFolder + Const.SEPARATOR + TEMP;
							FileUtil.createFolder(folder);

							File destFile = new File(folder, fileName);
							// //文件已存在删除旧文件（上传了同名的文件）
							if (chunk == 0 && destFile.exists()) {
								destFile.delete();
								destFile = new File(folder, fileName);
							}
							// 合成文件
							appendFile(input, destFile);
							if (chunk == chunks - 1) {
								String _fileName = fileName;
								// // 重命名
								if (StringUtil.isBlank(isRename) || Boolean.parseBoolean(isRename)) {
									_fileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("."));
									destFile.renameTo(new File(uploadFolder, _fileName));
								} else {
									// savePath += fileName;
									// outPath += fileName;
									destFile.renameTo(new File(uploadFolder, _fileName));
								}
								LOG.info("上传完成");
								if (uploadPath.equals("/") || StringUtil.isBlank(uploadPath)) {
									out.print(floderName + _fileName);
								} else {
									out.print(floderName + uploadPath + Const.SEPARATOR + _fileName);
								}
								new File(folder).delete();
							} else if (chunks == 0) {
								String _fileName = fileName;
								if (StringUtil.isBlank(isRename) || Boolean.parseBoolean(isRename)) {
									_fileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("."));
								}
								destFile.renameTo(new File(uploadFolder, _fileName));
								new File(folder).delete();
								if (uploadPath.equals("/") || StringUtil.isBlank(uploadPath)) {
									out.print(floderName + _fileName);
								} else {
									out.print(floderName + Const.SEPARATOR + uploadPath + Const.SEPARATOR + _fileName);
								}
								LOG.info("上传完成");
							} else {
								LOG.info("还剩[" + (chunks - 1 - chunk) + "]个块文件");

							}

							LOG.debug("upload file ok return path " + uploadFolder + fileName);

							try {
								if (null != input) {
									input.close();
								}
								if (null != out) {
									out.flush();
									out.close();
								}
							} catch (IOException e) {
								LOG.error(e.getMessage());
							}
						} catch (Exception e) {
							this.LOG.debug(e);
						}

					}
				}
			} catch (FileUploadException e) {
				this.LOG.debug(e);
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void appendFile(InputStream in, File destFile) {
		OutputStream out = null;
		try {
			// plupload 配置了chunk的时候新上传的文件append到文件末尾
			if (destFile.exists()) {
				out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE);
			} else {
				out = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
			}
			in = new BufferedInputStream(in, BUFFER_SIZE);

			int len = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		} finally {
			try {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}
	}
}
