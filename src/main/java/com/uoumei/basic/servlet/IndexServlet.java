package com.uoumei.basic.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uoumei.base.constant.Const;
import com.uoumei.mwebsite.biz.IWebsiteLogBiz;
import com.uoumei.mwebsite.biz.IWebsiteBiz;
import com.uoumei.mwebsite.constant.e.WebsiteEnum;
import com.uoumei.mwebsite.constant.e.WebsiteLogEnum;
import com.uoumei.mwebsite.entity.WebsiteLogEntity;
import com.uoumei.mwebsite.entity.WebsiteEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.StringUtil;


@WebServlet("/index")
public class IndexServlet extends BaseServlet {

	private static final long serialVersionUID = -7580260477467138079L;

	private static String  INDEX = "index.html", DEFAULT = "default.html";
	
	private IWebsiteLogBiz logBiz;
	
	private IWebsiteBiz websiteBiz;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取用户所请求的域名地址

		websiteBiz = (IWebsiteBiz) getBean(request.getServletContext(), "websiteBiz");
		logBiz = (IWebsiteLogBiz) getBean(request.getServletContext(), "logBiz");
		WebsiteLogEntity log = new WebsiteLogEntity();
		log.setLogIsMobile(WebsiteLogEnum.PC);
		// 查询数据库获取域名对应Id
		int websiteId = 0;
		WebsiteEntity website = websiteBiz.getByUrl(getDomain(request));
		if (website != null) {
			websiteId = website.getWebsiteId();
		} else {
			this.outString(response, this.getResString("err.not.exist",com.uoumei.mwebsite.constant.Const.RESOURCES,getDomain(request)));
			return;
		}
		String path = "";
		String url = IParserRegexConstant.HTML_SAVE_PATH + Const.SEPARATOR + websiteId + Const.SEPARATOR;

		if (!StringUtil.isBlank(website.getWebsiteMobileStyle())) {
			path = isMobileDevice(request) ? IParserRegexConstant.MOBILE : ""; // 如果是手机访问就跳转到相应页面
			log.setLogIsMobile(WebsiteLogEnum.MOBILE);
		}
		
		if (website.getWebsiteState() == WebsiteEnum.STOP.toInt()) {
			request.setAttribute("err", this.getResString("err.stop",com.uoumei.mwebsite.constant.Const.RESOURCES));
			request.getRequestDispatcher("/errors/stop.jsp").forward(request, response);
			return;
		}
		
		if (this.isMobileDevice(request) && website.getWebsiteMobileState() == WebsiteEnum.MOBILE_OFF.toInt()) {
			request.setAttribute("err", this.getResString("err.stop.mobile",com.uoumei.mwebsite.constant.Const.RESOURCES,url+INDEX));
			request.getRequestDispatcher("/errors/stop.jsp").forward(request, response);
			return;
		}
		

		String defaultHtmlPath = this.getRealPath(request, IParserRegexConstant.HTML_SAVE_PATH + File.separator
				+ websiteId + File.separator + path + File.separator + DEFAULT);
		File file = new File(defaultHtmlPath);
		String indexPosition = url + Const.SEPARATOR + INDEX;
		if (file.exists()) {
			indexPosition = url + Const.SEPARATOR + DEFAULT;
		}
		
		
		log.setLogAppId(website.getWebsiteId());
		log.setLogDatetime(new Date());
		log.setLogIp(this.getIpAddr(request));
		logBiz.saveEntity(log);
		
		// 转发到网站首页
		request.getRequestDispatcher(indexPosition).forward(request, response);

	}

}
