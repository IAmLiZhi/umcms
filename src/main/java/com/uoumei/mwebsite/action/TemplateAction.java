
package com.uoumei.mwebsite.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hjh.file.sync.starter.SyncStarter;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.constant.Const;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.constant.e.CookieConstEnum;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.cms.util.PathUtil;

/**
 * 获取有关模版文件夹或模版文件信息
 * 
 */

@Controller("mweTemplateAction")
@RequestMapping("/${managerPath}/mwebsite/template")
public class TemplateAction extends BaseAction {

	/**
	 * 站点业务层
	 */
	@Autowired
	private IAppBiz appBiz;

	/**
	 * 模版列表分页路径
	 */
	private final static String SKINLIST_PAGE_URL = "/mwebsite/template/queryTemplateSkin.do";

	
	/**
	 * 点击模版管理，获取所有的模版文件名
	 * 
	 * @param response
	 *            响应
	 * @param model
	 * @param request
	 *            请求
	 * @return 返回模版文件名集合
	 */
	@RequestMapping("/queryTemplateSkin")
	protected String queryTemplateSkin(HttpServletResponse response, ModelMap model, HttpServletRequest request) {
		
		String pageNo = request.getParameter("pageNo");
		if (!StringUtil.isInteger(pageNo)) {
			pageNo = "1";
		}
		String websiteIdParam = request.getParameter("websiteId");
		
		List<AppEntity> websites = appBiz.queryAll();
		model.addAttribute("websites", websites);
		
		int websiteId = 0;
		if (!StringUtil.isBlank(websiteIdParam)) {
			websiteId = Integer.parseInt(request.getParameter("websiteId"));
		}else{
			if(websites != null && websites.size()>0){
				AppEntity app = websites.get(0);
				websiteId = app.getAppId();
			}
		}
		model.addAttribute("websiteId", websiteId);
		
		List<String> folderNameList = new ArrayList<String>();
		if (websiteId > 0) {
			folderNameList = this.queryTemplateFile(request, websiteId);
		}
		model.addAttribute("folderNameList", folderNameList);
		
		int recordCount = 0;
		if (!StringUtil.isBlank(folderNameList)) {
			recordCount = folderNameList.size();
		}
		//--zzq
		//PageUtil page = new PageUtil(StringUtil.string2Int(pageNo), recordCount, getUrl(request) + SKINLIST_PAGE_URL);
		PageUtil page = new PageUtil(StringUtil.string2Int(pageNo), recordCount, request.getAttribute("managerPath") + SKINLIST_PAGE_URL);
		this.setCookie(request, response, CookieConstEnum.PAGENO_COOKIE, pageNo);
		model.addAttribute("page", page);
		model.addAttribute("model", "mwebsite");
		
		return view("/mwebsite/template/template_list");
	}
	
	/**
	 * 显示子文件和子文件夹
	 * 
	 * @param response
	 *            响应
	 * @param model
	 * @param request
	 *            请求
	 * @return 返回文件名集合
	 */
	@RequestMapping("/showChildFileAndFolder")
	public String showChildFileAndFolder(HttpServletResponse response, ModelMap model, HttpServletRequest request) {
		
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return "";
		}
		
		List<String> folderNameList = null;
		String skinFolderName = request.getParameter("skinFolderName");
		//--zzq
		//File files[] = new File(this.getRealPath(request, skinFolderName)).listFiles();
		File files[] = new File(PathUtil.getTmpFullPath(skinFolderName)).listFiles();
		
		if (!StringUtil.isBlank(files)) {
			folderNameList = new ArrayList<String>();
			List<String> fileNameList = new ArrayList<String>();
			for (int i = 0; i < files.length; i++) {
				File currFile = files[i];
				//--zzq
//				String filter = BasicUtil.getRealPath(
//						IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + websiteIdParam);
				String filter = PathUtil.getTmpRootPath(Integer.parseInt(websiteIdParam)) + File.separator;
				LOG.debug("过滤路径" + filter);
				String temp = currFile.getPath().replace(filter, "");
				if (currFile.isDirectory()) {
					folderNameList.add(temp);
				} else {
					fileNameList.add(temp);
				}
			}
			folderNameList.addAll(fileNameList);
			model.addAttribute("fileNameList", folderNameList);
		}
		
		String uploadFileUrl = skinFolderName;
		model.addAttribute("uploadFileUrl", uploadFileUrl);
		model.addAttribute("websiteId", websiteIdParam);
		List<AppEntity> websites = appBiz.queryAll();
		model.addAttribute("websites", websites);
		
		return view("/mwebsite/template/template_file_list");
	}

	/**
	 * 删除模版文件
	 * 
	 * @param styleName
	 *            文件名称
	 * @param request
	 *            请求
	 */
	@RequestMapping("/deleteTemplateFile")
	@ResponseBody
	public int deleteTemplateFile(HttpServletRequest request) {
		int pageNo = 1;
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)) {
			return 0;
		}
		
		String fileName = request.getParameter("fileName");
		//--zzq
//		FileUtil.delFile(this.getRealPath(request, IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator
//				+ websiteIdParam + File.separator + fileName));
		String delFilePath = PathUtil.getTmpRootPath(Integer.parseInt(websiteIdParam)) + File.separator + fileName;
		FileUtil.delFile(delFilePath);
		// 判断当前页码
		this.getHistoryPageNoByCookie(request);
		return pageNo;
	}
	
	/**
	 * 读取模版文件内容
	 * 
	 * @param model
	 * @param request
	 *            请求
	 * @return 返回文件内容
	 */
	@RequestMapping("/readFileContent")
	@RequiresPermissions("mwebsite:template:update")
	public String readFileContent(ModelMap model, HttpServletRequest request) {
		String fileName = request.getParameter("fileName");
		if (!StringUtil.isBlank(fileName)) {
			//--zzq
			//model.addAttribute("fileContent", FileUtil.readFile(this.getRealPath(request, fileName)));
			model.addAttribute("fileContent", FileUtil.readFile(PathUtil.getTmpFullPath(fileName)));
		}
		//--zzq
		//model.addAttribute("name", new File(this.getRealPath(request, fileName)).getName());
		model.addAttribute("name", new File(PathUtil.getTmpFullPath(fileName)).getName());
		model.addAttribute("fileName", fileName);
		model.addAttribute("fileNamePrefix", fileName.substring(0, fileName.lastIndexOf(File.separator) + 1));
		
		return view("/mwebsite/template/template_edit_file");
	}
	
	/**
	 * 写入模版文件内容
	 * 
	 * @param model
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 * @throws IOException
	 */
	@RequestMapping("/writeFileContent")
	public void writeFileContent(ModelMap model, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String fileName = request.getParameter("fileName");
		String oldFileName = request.getParameter("oldFileName");
		String fileContent = request.getParameter("fileContent");
		
		// --zzq
		if (StringUtil.isBlank(fileName)) {
			return;
		}
		String templets = PathUtil.getTmpFullPath(fileName);
		LOG.debug(templets);
		FileUtil.writeFile(fileContent, templets, "utf-8");
		if (!fileName.equals(oldFileName)) {
			// 得到一个待命名文件对象
			File newName = new File(templets);
			// 获取新名称文件的文件对象
			File oldName = new File(PathUtil.getTmpFullPath(oldFileName));
			// 进行重命名
			oldName.renameTo(newName);
			FileUtil.delFile(PathUtil.getTmpFullPath(oldFileName));
		}
		this.outJson(response, ModelCode.ROLE, true, null);
		
	}
	
	/**
	 * 删除模版
	 * 
	 * @param name
	 *            模版名称
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mwebsite:template:del")
	public boolean delete(HttpServletRequest request) {
		
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return false;
		}
		
		String fileName = request.getParameter("fileName");
//		String path = this.getRealPath(request, IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator
//				+ websiteIdParam + File.separator + fileName);
		
		String path = PathUtil.getTmpRootPath(Integer.parseInt(websiteIdParam))+ File.separator + fileName;
		try {
			FileUtils.deleteQuietly(new File(path));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}	
	
	/**
	 * 解压zip模版文件
	 * 
	 * @param fileUrl
	 *            文件路径
	 * @throws ZipException
	 * @throws IOException
	 */
	@RequestMapping("/unZip")
	@ResponseBody
	@RequiresPermissions("mwebsite:template:upload")
	public String unZip(ModelMap model, HttpServletRequest request) throws  IOException {
		boolean hasDic = false;
		String entryName = "";
		String fileUrl = request.getParameter("fileUrl");
		// 创建文件对象
		//--zzq
		//File file = new File(this.getRealPath(request, fileUrl));
		File file = new File(PathUtil.getTmpFullPath(fileUrl));
		// 创建zip文件对象
		ZipFile zipFile = new ZipFile(file);
		// 创建本zip文件解压目录
		File unzipFile = new File(
				PathUtil.getTmpFullPath(fileUrl.substring(0, fileUrl.length() - file.getName().length())));
		// 得到zip文件条目枚举对象
		Enumeration<? extends ZipEntry> zipEnum = zipFile.getEntries();
		// 定义输入输出流对象
		// 循环读取条目
		while (zipEnum.hasMoreElements()) {
			// 得到当前条目
			ZipEntry entry = (ZipEntry) zipEnum.nextElement();
			entryName = new String(entry.getName().getBytes("utf-8"));
			File f = new File(unzipFile.getAbsolutePath() + File.separator + entryName);
			if(f.getName().charAt(0)=='.') {
				continue;
			}
			if(!hasDic) {
				new File(unzipFile.getAbsolutePath() + File.separator + entryName).getParentFile().mkdirs();
				hasDic = true;
			}
			// 若当前条目为目录则创建
			if (entry.isDirectory()) {
				new File(unzipFile.getAbsolutePath() + File.separator + entryName).mkdirs();
			} else {
				// 若当前条目为文件则解压到相应目录
				InputStream input = zipFile.getInputStream(entry);
				OutputStream output = new FileOutputStream(
						new File(unzipFile.getAbsolutePath() + File.separator + entryName));
				byte[] buffer = new byte[1024 * 8];
				int readLen = 0;
				while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
					output.write(buffer, 0, readLen);
				}
				output.flush();
				output.close();
				input.close();
				input = null;
				output = null;
			}
		}
		zipFile.close();
		FileUtils.forceDelete(file);
		
		return entryName;
	}
	
	/**
	 * 查询模版文件供栏目选择
	 * 
	 * @param request
	 *            请求
	 * @return 返回列表集合
	 */
	@RequestMapping("/queryTemplateFileForColumn")
	@ResponseBody
	public List<String> queryTemplateFileForColumn(HttpServletRequest request) {
		List<String> listName = new ArrayList<String>();
		String websiteIdParam = request.getParameter("websiteId");
		if(StringUtil.isBlank(websiteIdParam)){
			return listName;
		}
		
		// --zzq
//		String path = this.getRealPath(request, IParserRegexConstant.REGEX_SAVE_TEMPLATE) + File.separator
//				+ websiteIdParam;
		int websiteId = Integer.parseInt(websiteIdParam);
		AppEntity website = (AppEntity) appBiz.getEntity(websiteId);
		String path = PathUtil.getTmpRootPath(websiteId);
		path = path + File.separator + website.getAppStyle();
		LOG.debug("tempPath:" + path);
		
		files(listName, new File(path), website.getAppStyle());
		return listName;
	}
	
	
	
	/** no use
	 * 查询模版风格供站点选择
	 * 
	 * @param model
	 * @param request
	 *            请求
	 * @return 模版文件集合
	 */
	@RequestMapping("/queryAppTemplateSkin")
	@ResponseBody
	public Map queryAppTemplateSkin(HttpServletRequest request) {
		
		String websiteIdParam = request.getParameter("websiteId");
		List<String> folderNameList = new ArrayList<String>();
		
		if (!StringUtil.isBlank(websiteIdParam)) {
			int websiteId = Integer.parseInt(websiteIdParam);
			folderNameList = (List<String>) this.queryTemplateFile(request, websiteId);
		}
		Map map = new HashMap();
		if (!StringUtil.isBlank(folderNameList)) {
			map.put("fileName", folderNameList);
		}
		return map;
	}

	
	private void files(List list, File fileDir, String style) {
		if (fileDir.isDirectory()) {
			File files[] = fileDir.listFiles();
			for (int i = 0; i < files.length; i++) {
				File currFile = files[i];
				if (currFile.isFile()) {
					String ex = currFile.getName();
					if (ex.endsWith("htm") || ex.endsWith("html")) {
						String _pathName = new String();
						_pathName = files(currFile, style, _pathName);
						list.add(_pathName + currFile.getName());
					}
				} else if (currFile.isDirectory()
						&& !currFile.getName().equalsIgnoreCase(IParserRegexConstant.MOBILE)) {
					files(list, currFile, style);
				}
			}
		}
	}

	private String files(File file, String style, String pathName) {
		if (!file.getParentFile().getName().equals(style)) {
			pathName = file.getParentFile().getName() + "/" + pathName;
			pathName = files(file.getParentFile(), style, pathName);
		}
		return pathName;
	}

	/**
	 * 查询模版文件集合
	 * 
	 * @param request
	 *            请求
	 * @return 模版文件集合
	 */
	private List<String> queryTemplateFile(HttpServletRequest request, int websiteId) {
		
		List<String> folderNameList = null;
		if (isSystemManager(request)) {
			//--zzq
//			String templets = this.getRealPath(request, IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator
//					+ websiteId + File.separator);
			String templets = PathUtil.getTmpRootPath(websiteId)+ File.separator;
			File file = new File(templets);
			String[] str = file.list();
			if (!StringUtil.isBlank(str)) {
				folderNameList = new ArrayList<String>();
				for (int i = 0; i < str.length; i++) {
					// 避免不为文件夹的文件显示
					if (str[i].indexOf(".") < 0) {
						folderNameList.add(str[i]);
					}
				}
			}
		}
		return folderNameList;
	}

}