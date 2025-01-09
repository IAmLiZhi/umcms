
package com.uoumei.cms.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;



import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hjh.file.sync.starter.SyncStarter;
import com.uoumei.base.constant.Const;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;


/**
 * 路径工具类
 */
public class PathUtil {
	//--zzq
	public static final String FOLDER_NAME_STATIC = "static";
	public static final String FOLDER_NAME_CSS = "css";
	public static final String FOLDER_NAME_JS = "js";
	public static final String FOLDER_NAME_IMAGE = "image";
	public static final String FOLDER_NAME_FLASH = "flash";
	public static final String FOLDER_NAME_FONT = "font";
	
	public static String getGeneratePath(AppEntity app){
		return Const.PROJECT_PATH + IParserRegexConstant.HTML_SAVE_PATH + File.separator + app.getAppId();
	}
	public static String getGeneratePath(int appId){
		return Const.PROJECT_PATH + IParserRegexConstant.HTML_SAVE_PATH + File.separator + appId;
	}
	
	public static String getTmpPath(AppEntity app){
		//return Const.PROJECT_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator + app.getAppStyle();
		return Const.WEB_INF_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator + app.getAppStyle();
	}
	public static String getTmpPath(int appId, String appStyle){
		//return Const.PROJECT_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator + app.getAppStyle();
		return Const.WEB_INF_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + appId + File.separator + appStyle;
	}
	public static String getTmpRootPath(int appId){
		//return Const.PROJECT_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator + app.getAppStyle();
		return Const.WEB_INF_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + appId ;
	}
	public static String getTmpFullPath(String path){
		//return Const.PROJECT_PATH + IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator + app.getAppStyle();
		return Const.WEB_INF_PATH + path ;
	}
	public static boolean syncFolder(int appId, String appStyle){
		try {
		
			//生成保存htm页面地址
			String generatePath = PathUtil.getGeneratePath(appId);	
			FileUtil.createFolder(generatePath);
			//网站风格路径
			String tmpPath = PathUtil.getTmpPath(appId, appStyle);
			
			//sync here
			String staticFolder = generatePath+ File.separator + PathUtil.FOLDER_NAME_STATIC;
			FileUtil.createFolder(staticFolder);
			
			String srcFolder = tmpPath+ File.separator + PathUtil.FOLDER_NAME_CSS;
			String destFolder = staticFolder+ File.separator + PathUtil.FOLDER_NAME_CSS;
			if(FileUtil.folderExists(srcFolder)){
				if(FileUtil.folderExists(destFolder)){
					FileUtil.delFolders(destFolder);
				}
				copy(srcFolder, destFolder);
			}
			srcFolder = tmpPath+ File.separator + PathUtil.FOLDER_NAME_JS;
			destFolder = staticFolder+ File.separator + PathUtil.FOLDER_NAME_JS;
			if(FileUtil.folderExists(srcFolder)){
				if(FileUtil.folderExists(destFolder)){
					FileUtil.delFolders(destFolder);
				}
				copy(srcFolder, destFolder);
			}	
			srcFolder = tmpPath+ File.separator + PathUtil.FOLDER_NAME_IMAGE;
			destFolder = staticFolder+ File.separator + PathUtil.FOLDER_NAME_IMAGE;
			if(FileUtil.folderExists(srcFolder)){
				if(FileUtil.folderExists(destFolder)){
					FileUtil.delFolders(destFolder);
				}
				copy(srcFolder, destFolder);
			}
			srcFolder = tmpPath+ File.separator + PathUtil.FOLDER_NAME_FLASH;
			destFolder = staticFolder+ File.separator + PathUtil.FOLDER_NAME_FLASH;
			if(FileUtil.folderExists(srcFolder)){
				if(FileUtil.folderExists(destFolder)){
					FileUtil.delFolders(destFolder);
				}
				copy(srcFolder, destFolder);
			}	
			srcFolder = tmpPath+ File.separator + PathUtil.FOLDER_NAME_FONT;
			destFolder = staticFolder+ File.separator + PathUtil.FOLDER_NAME_FONT;
			if(FileUtil.folderExists(srcFolder)){
				if(FileUtil.folderExists(destFolder)){
					FileUtil.delFolders(destFolder);
				}
				copy(srcFolder, destFolder);
			}			

			return true;
		}catch (Exception e){
			System.out.println(e.getMessage());
			return false;
		}
	}
    //复制方法
    public static void copy(String src, String des) throws Exception {
        //初始化文件复制
        File file1=new File(src);
        //把文件里面内容放进数组
        File[] fs=file1.listFiles();
        //初始化文件粘贴
        File file2=new File(des);
        //判断是否有这个文件有不管没有创建
        if(!file2.exists()){
            file2.mkdirs();
        }
        //遍历文件及文件夹
        for (File f : fs) {
            if(f.isFile()){
                //文件
                fileCopy(f.getPath(),des+ File.separator +f.getName()); //调用文件拷贝的方法
            }else if(f.isDirectory()){
                //文件夹
                copy(f.getPath(),des+ File.separator +f.getName());//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
            }
        }
        
    }

    /**
     * 文件复制的具体方法
     */
    private static void fileCopy(String src, String des) throws Exception {
        //io流固定格式
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(des));
        int i = -1;//记录获取长度
        byte[] bt = new byte[2014];//缓冲区
        while ((i = bis.read(bt))!=-1) {
            bos.write(bt, 0, i);
        }
        bis.close();
        bos.close();
        //关闭流
    }

}
