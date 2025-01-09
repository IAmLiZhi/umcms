package com.uoumei.mwebsite.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.uoumei.base.entity.BaseEntity;
import com.uoumei.util.StringUtil;

/**
 * 网站基本信息实体类
 */
public class WebsiteEntity extends BaseEntity {

	/**
	 * 应用名称
	 */
	private String websiteName;

	/**
	 * 应用描述
	 */
	private String websiteDescription;

	/**
	 * 应用logo
	 */
	private String websiteLogo;

	/**
	 * 应用编号
	 */
	private int websiteId;

	/**
	 * 网站采用的模板风格
	 */

	private String websiteStyle;
	
	/**
	 * 移动端样式目录
	 */
	private String websiteMobileStyle;


	/**
	 * 网站关键字
	 */
	private String websiteKeyword;

	/**
	 * 网站版权信息
	 */
	private String websiteCopyright;

	/**
	 * 网站域名
	 */
	private String websiteUrl;
	

	/**
	 * 管理站点的管理员id
	 */
	private int websiteManagerId;

	/**
	 * 站点日期
	 */
	private Date websiteDatetime;
	
	/**
	 * 应用续费时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date websitePayDate = new Date();
	
	
	/**
	 * 应用费用清单
	 */
	private String websitePay;
	
	/**
	 * 网站状态 0:运行中 1:停止
	 */
	private int websiteState;
	
	
	/**
	 * 移动站状态 0:启用  1:未启用
	 */
	private int websiteMobileState=1;
	
	

	public int getWebsiteState() {
		return websiteState;
	}

	public void setWebsiteState(int websiteState) {
		this.websiteState = websiteState;
	}

	public int getWebsiteMobileState() {
		return websiteMobileState;
	}

	public void setWebsiteMobileState(int websiteMobileState) {
		this.websiteMobileState = websiteMobileState;
	}

	public Date getWebsitePayDate() {
		return websitePayDate;
	}

	public void setWebsitePayDate(Date websitePayDate) {
		this.websitePayDate = websitePayDate;
	}

	public String getWebsitePay() {
		return websitePay;
	}

	public void setWebsitePay(String websitePay) {
		this.websitePay = websitePay;
	}

	public Date getWebsiteDatetime() {
		return websiteDatetime;
	}

	public void setWebsiteDatetime(Date websiteDatetime) {
		this.websiteDatetime = websiteDatetime;
	}

	/**
	 * 获取网站版权信息
	 * 
	 * @return 返回网站版权信息
	 */
	public String getWebsiteCopyright() {
		return websiteCopyright;
	}

	public String getWebsiteDescription() {
		return websiteDescription;
	}

	public int getWebsiteId() {
		return websiteId;
	}

	/**
	 * 获取网站的关键字
	 * 
	 * @return 返回网站关键字
	 */
	public String getWebsiteKeyword() {
		return websiteKeyword;
	}

	/**
	 * 获取管理员的id
	 * 
	 * @return 返回管理员ID
	 */
	public int getWebsiteManagerId() {
		return websiteManagerId;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	/**
	 * 获取网站使用的模板风格
	 * 
	 * @return 返回网站使用的模板风格
	 */
	public String getWebsiteStyle() {
		return websiteStyle;
	}

	/**
	 * 获取网站域名
	 */
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	
	/**
	 * 获取网站域名
	 */
	public String getWebsiteHostUrl() {
		if (!StringUtil.isBlank(websiteUrl) && websiteUrl.indexOf("\n") > 0) { //存在多个域名绑定
			return websiteUrl.split("\n")[0].trim();
		}
		return websiteUrl;
	}

	public String getWebsiteLogo() {
		return websiteLogo;
	}

	/**
	 * 设置网站版权信息
	 * 
	 * @param appCopyright
	 */
	public void setWebsiteCopyright(String websiteCopyright) {
		this.websiteCopyright = websiteCopyright;
	}

	public void setWebsiteDescription(String websiteDescription) {
		this.websiteDescription = websiteDescription;
	}

	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}

	/**
	 * 设置网站关键字
	 * 
	 * @param appKeyword
	 */
	public void setWebsiteKeyword(String websiteKeyword) {
		this.websiteKeyword = websiteKeyword;
	}

	/**
	 * 设置管理员的id
	 */
	public void setWebsiteManagerId(int websiteManagerId) {
		this.websiteManagerId = websiteManagerId;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	/**
	 * 设置网站使用的模板风格
	 * 
	 * @param appStyle
	 */
	public void setWebsiteStyle(String websiteStyle) {
		this.websiteStyle = websiteStyle;
	}

	/**
	 * 设置网站域名
	 * 
	 * @param appUrl
	 */
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	

	public String getWebsiteMobileStyle() {
		return websiteMobileStyle;
	}

	public void setWebsiteMobileStyle(String websiteMobileStyle) {
		this.websiteMobileStyle = websiteMobileStyle;
	}

	public void setWebsiteLogo(String websiteLogo) {
		this.websiteLogo = websiteLogo;
	}

}
