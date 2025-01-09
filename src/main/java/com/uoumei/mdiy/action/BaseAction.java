package com.uoumei.mdiy.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.parser.IGeneralParser;
import com.uoumei.mdiy.biz.IPageBiz;
import com.uoumei.mdiy.entity.PageEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;

import com.uoumei.base.util.SpringUtil;

/**
 * 
 * 自定义模块
 */
public abstract class BaseAction extends com.uoumei.basic.action.BaseAction {

	protected static String MODEL = "/mdiy";

	/**
	 * 动态生成页面，根据model_template表
	 * 
	 * @param key
	 *            对应表中的key值
	 * @param req
	 *            HttpServletRequest对象
	 * @return 如果没有读到模版返回的是null，没读到的情况有两种原因一种是找不到相应的应用，一种是模版文件不存在
	 */
	/* --zzq
	protected String generaterPage(String key, IGeneralParser parser, HttpServletRequest req) {
		AppEntity app = this.getApp(req);
		if (app == null) {
			return null;
		}

		IPageBiz pageBiz = (IPageBiz) SpringUtil.getBean(IPageBiz.class);
		PageEntity pageEntity =new PageEntity();
		pageEntity.setPageAppId(app.getAppId());
		pageEntity.setPageKey(key);
		pageEntity  = (PageEntity) pageBiz.getEntity(pageEntity);
		if (pageEntity == null) {
			return null;
		}
		String templatePath = pageEntity.getPagePath();
		String path = getRealPath(req, IParserRegexConstant.REGEX_SAVE_TEMPLATE) + File.separator + app.getAppId()
				+ File.separator + app.getAppStyle() + File.separator;
		String content = "";
		if (isMobileDevice(req) && !StringUtil.isBlank(app.getAppMobileStyle())) { // 移动端
			String htmlContent = FileUtil.readFile(path + app.getAppMobileStyle() + File.separator + templatePath); // 读取模版文件内容
			Map map = new HashMap();
			map.put(IGeneralParser.MOBILE, app.getAppMobileStyle());
			content = parser.parse(htmlContent, app, map);
		} else {
			String htmlContent = FileUtil.readFile(path + templatePath);
			content = parser.parse(htmlContent, app);
		}

		Map<String, String[]> param = req.getParameterMap();
		// 将get或post提交过来的参数映射到界面上去
		for (Entry<String, String[]> entry : param.entrySet()) {
			if (entry.getValue()==null){
				content = content.replace("{" + entry.getKey() + "/}", "");
				continue;
			}

			String value = entry.getValue()[0]; // 处理由get方法请求中文乱码问题
			if (StringUtil.isBlank(value)) {
				continue;
			}
			if (req.getMethod().equals(RequestMethod.GET)) { // 如果是get方法需要将请求地址参数转吗
				value = StringUtil.isoToUTF8(value);
			}
			content = content.replace("{" + entry.getKey() + "/}", value);
		}
		return content;
	}
*/
	/**
	 * 读取模板文件内容
	 * 
	 * @param fileName
	 *            文件名称
	 * @param request
	 *            HttpServletRequest对象
	 * @return 返回模板文件内容
	 */
	/* --zzq
	protected String readTemplate(String fileName, HttpServletRequest request) {
		AppEntity app = this.getApp(request);
		if (app == null) {
			return null;
		}
		String path = this.getRealPath(request, File.separator) + File.separator
				+ IParserRegexConstant.REGEX_SAVE_TEMPLATE + File.separator + app.getAppId() + File.separator
				+ app.getAppStyle();
		path += File.separator + fileName;
		return FileUtil.readFile(path);
	}
	*/
	/**
	 * 读取国际化资源文件
	 * 
	 * @param key
	 *            键值
	 * @return 返回获取到的字符串
	 */
	@Override
	protected String getResString(String key) {
		// TODO Auto-generated method stub
		String str = "";
		try {
			str = super.getResString(key);
		} catch (MissingResourceException e) {
			str = com.uoumei.mdiy.constant.Const.RESOURCES.getString(key);
		}

		return str;
	}
}
