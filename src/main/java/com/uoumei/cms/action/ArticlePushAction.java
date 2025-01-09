
package com.uoumei.cms.action;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.biz.IRoleColumnBiz;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.biz.IArticlePushBiz;
import com.uoumei.cms.constant.Const;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.entity.ArticlePushEntity;
import com.uoumei.cms.parser.GeneraterCore;
import com.uoumei.cms.util.PathUtil;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.BasicUtil;

/**
 * 文章管理
 */
@Controller
@RequestMapping("/${managerPath}/cms/push")
public class ArticlePushAction extends BaseAction {
	
	
	@Autowired
	private IArticlePushBiz articlePushBiz;
	@Autowired
	private IAppBiz appBiz;
	@Autowired
	private IColumnBiz columnBiz;
	@Autowired
	private IArticleBiz articleBiz;
	@Autowired
	private GeneraterCore generaterCore;
	@Autowired
	private IRoleColumnBiz roleColumnBiz;

	
	/**
	 * 加载页面显示所有文章信息
	 * 
	 * @param request
	 * @return 返回文章页面显示地址
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/index")
	public String index(HttpServletRequest request, ModelMap mode, HttpServletResponse response) {
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		
		List<AppEntity> listApp = appBiz.queryAll();
		// role column --zzq
//		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request, ModelCode.CMS_COLUMN));
		request.setAttribute("websites", listApp);
		return view("/cms/push/index"); 
	}
	
	/**
	 * 加载页面显示所有文章信息
	 * 
	 * @param request
	 * @return 返回文章页面显示数据
	 */
	@RequestMapping("/list")
	public void list(@ModelAttribute ArticlePushEntity articlePush, 
			HttpServletRequest request, HttpServletResponse response) {
		
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();

		// 查询文章列表
//		ArticlePushEntity apEntity = new ArticlePushEntity();
		articlePush.setAppId(appId);
		
		int srcAppId = 0;
		if(articlePush.getSrcAppId() == null) {
			String srcAppIdStr = request.getParameter("srcAppIdStr");
			if(!StringUtil.isBlank(srcAppIdStr)){
				srcAppId = Integer.parseInt(srcAppIdStr);
			}
		}else{
			srcAppId = articlePush.getSrcAppId();
		}
		if(srcAppId != 0){
			articlePush.setSrcAppId(srcAppId);
		}else {
			articlePush.setSrcAppId(null);
		}
		BasicUtil.startPage();
		List articlePushList = articlePushBiz.query(articlePush);
		EUListBean _list = new EUListBean(articlePushList, (int) BasicUtil.endPage(articlePushList).getTotal());
		
		// 将数据以json数据的形式返回
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd")));

	}
	@RequestMapping("/delete")
	public void delete(@RequestBody List<ArticlePushEntity> articlePushs, 
			HttpServletRequest request,HttpServletResponse response) {
		for(ArticlePushEntity articlePush:articlePushs){
			articlePushBiz.deleteEntity(articlePush.getPushId());
		}
		this.outJson(response, true);
	}
	
	@RequestMapping("/pub")
	public void pub(@RequestBody List<ArticlePushEntity> articlePushs, 
			HttpServletRequest request,HttpServletResponse response) {
		
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		AppEntity app = (AppEntity) appBiz.getEntity(appId);
		
		for(ArticlePushEntity articlePush:articlePushs){
			ArticleEntity article = new ArticleEntity();

			//保存文章
			article.setArticleType("h");	// 文章类型=链接
			article.setBasicTitle(articlePush.getLinkTitle());
			article.setArticleUrl(articlePush.getLinkUrl());
			article.setBasicThumbnails(articlePush.getLinkThumbnails());
			
			//--zzq 20190328
			Timestamp currTimeStamp = new Timestamp(System.currentTimeMillis());
			ArticlePushEntity articlePushEntity = (ArticlePushEntity)articlePushBiz.getEntity(articlePush.getPushId());
			if(null != articlePushEntity.getPubTime()){
				article.setBasicDateTime(new Timestamp(articlePushEntity.getPubTime().getTime()));
			}else{
				article.setBasicDateTime(currTimeStamp);
			}
			
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(articlePush.getDestColumnId());
			article.setColumn(column);
			article.setBasicCategoryId(column.getCategoryId());
			article.setArticleWebId(appId);
			article.setBasicAppId(appId);
			article.setArticleManagerId(managerSession.getManagerId());
			article.setBasicUpdateTime(currTimeStamp);
			article.setArticleState(Const.ARTICLE_STATE_PUB);
			articleBiz.saveBasic(article);
			
			//生成保存htm页面地址
			String generatePath = PathUtil.getGeneratePath(app);	
			FileUtil.createFolder(generatePath);
			String tmpPath = PathUtil.getTmpPath(app);

			List<ColumnEntity> list = columnBiz.queryParentColumnByColumnId(column.getCategoryId());
			list.add(column);
			for(ColumnEntity tempColumn : list){
				generaterCore.generateOneColumn(tempColumn, app, generatePath, tmpPath, app.getAppHostUrl());
			}
			generaterCore.generateOneIndex(app, generatePath, tmpPath, "index.html", "index.htm");
			
			//更新状态和发布时间
			ArticlePushEntity apEntity = new ArticlePushEntity();
			apEntity.setPushId(articlePush.getPushId());
			apEntity.setDestColumnId(column.getCategoryId());
			apEntity.setDestColumnName(column.getCategoryTitle());
			//apEntity.setPubTime(new Date());
			apEntity.setState(1);
			articlePushBiz.updateEntity(apEntity);
		}
		this.outJson(response, true);
	}

}