
package com.uoumei.cms.action;


import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.activity.biz.impl.ActivityBizImpl;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.biz.IContentModelFieldBiz;
import com.uoumei.mdiy.biz.IDictBiz;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.parser.GeneraterCore;
import com.uoumei.cms.util.PathUtil;
import com.uoumei.cms.constant.Const;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.BasicUtil;

/**
 * 文章管理
 */
@Controller(value="AuditAction")
@RequestMapping("/${managerPath}/cms/audit")
public class AuditAction extends BaseAction implements JavaDelegate {
	
	
	@Autowired
	private ActivityBizImpl activityBizImpl;
	@Autowired
	private GeneraterCore generaterCore;
	@Autowired
	private IAppBiz appBiz;
	
	/**
	 * 业务层的注入
	 */
	@Autowired
	private IColumnBiz columnBiz;

	/**
	 * 文章管理业务处理层
	 */
	@Autowired
	private IArticleBiz articleBiz;

	/**
	 * 字段管理业务层
	 */
	@Autowired
	private IContentModelFieldBiz fieldBiz;

	/**
	 * 内容管理业务层
	 */
	@Autowired
	private IContentModelBiz contentBiz;

	/**
	 * 注入字典表业务层
	 */
	@Autowired
	private IDictBiz dictBiz;

	/**
	 * 判断是否为checkbox类型
	 */
	private static final int checkBox = 11;


	/**
	 * 加载页面显示所有文章信息
	 * 
	 * @param request
	 * @return 返回文章页面显示地址
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/index")
	public String index(HttpServletRequest request, ModelMap mode, HttpServletResponse response) {
		return view("/cms/audit/index"); 
	}
	
	/**
	 * 加载页面显示所有文章信息
	 * 
	 * @param request
	 * @return 返回文章页面显示数据
	 */
	@RequestMapping("/list")
	public void list(HttpServletRequest request, ModelMap mode,
			HttpServletResponse response) {
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		int managerId = managerSession.getManagerId();

		// 查询文章列表
		BasicUtil.startPage();
		List<ArticleEntity> articleList = activityBizImpl.queryAudit(managerId, appId);
		EUListBean _list = new EUListBean(articleList, (int) BasicUtil.endPage(articleList).getTotal());
		// 将数据以json数据的形式返回
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd")));

	}
	
	/**
	 * 完成审核
	 */
	@RequestMapping("/finish")
	public void finish(@RequestBody String inJsonStr,HttpServletResponse response, HttpServletRequest request) {
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		int managerId = managerSession.getManagerId();
		
//		Map<String, Object> inObject
//		JSONObject inJson = new JSONObject(inObject);
		JSONObject inJson = JSONObject.parseObject(inJsonStr);
		
		activityBizImpl.updateAudit(managerId, inJson);

		this.outJson(response, true, null, JSONArray.toJSONString("success"));
	}
	
	/**
	 * 更新状态
	 */
	public void execute(DelegateExecution execution) throws Exception {
		String businessKey = execution.getProcessBusinessKey();
		System.out.println("businessKey:"+businessKey);
		
		boolean approve = (boolean)execution.getVariable("approve");
		String note = (String)execution.getVariable("note");
		
		ArticleEntity article = (ArticleEntity)articleBiz.getEntity(Integer.parseInt(businessKey));
		article.setArticleNote(note);
		if(!approve){
			article.setArticleState(Const.ARTICLE_STATE_NOPASS);
			articleBiz.updateEntity(article);
			return;
		}
		
		ColumnEntity column = (ColumnEntity) columnBiz.getEntity(article.getBasicCategoryId());
		AppEntity app = (AppEntity) appBiz.getEntity(article.getArticleWebId());
		
		//判断发布时间，如果是超过发布时间立即静态化，否则只更新状态
//		boolean isPubTime = true;
//		if(!isPubTime){
//			article.setArticleState(Const.ARTICLE_STATE_WAIT);
//			articleBiz.updateEntity(article);
		Timestamp currTime = new Timestamp(System.currentTimeMillis());
		if(currTime.before(article.getBasicDateTime())){
			article.setArticleState(Const.ARTICLE_STATE_WAIT);
			articleBiz.updateBasic(article);
		}else{
			article.setArticleState(Const.ARTICLE_STATE_PUB);
			articleBiz.updateEntity(article);
			
			//生成保存htm页面地址
			String generatePath = PathUtil.getGeneratePath(app);
			FileUtil.createFolder(generatePath);
			//网站风格路径
			String tmpPath = PathUtil.getTmpPath(app);
			
			List<ColumnEntity> list = columnBiz.queryParentColumnByColumnId(column.getCategoryId());
			list.add(column);
			for(ColumnEntity tempColumn : list){
				generaterCore.generateOneColumn(tempColumn, app, generatePath, tmpPath, app.getAppHostUrl());
			}
			generaterCore.generateOneIndex(app, generatePath, tmpPath, "index.html", "index.htm");
			generaterCore.generateOneArticle(article, column, app, generatePath, tmpPath, app.getAppHostUrl());

		}
    }
}