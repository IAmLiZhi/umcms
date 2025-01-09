
package com.uoumei.cms.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.activity.biz.impl.ActivityBizImpl;
import com.uoumei.activity.entity.ProcColumnEntity;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.biz.IArticlePushBiz;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.biz.IRoleColumnBiz;
import com.uoumei.cms.constant.e.ColumnTypeEnum;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.biz.IContentModelFieldBiz;
import com.uoumei.mdiy.biz.IDictBiz;
import com.uoumei.cms.constant.Const;
import com.uoumei.cms.constant.ModelCode;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.entity.ArticlePushEntity;
import com.uoumei.cms.parser.GeneraterCore;
import com.uoumei.cms.util.ArrysUtil;
import com.uoumei.cms.util.PathUtil;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.entity.RoleColumnEntity;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.mdiy.entity.ContentModelFieldEntity;
import com.uoumei.mdiy.entity.DictEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.RegexUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.FileExtUtil;
import com.uoumei.util.FileUtil;

/**
 * 文章管理
 */
@Controller
@RequestMapping("/${managerPath}/cms/article")
public class ArticleAction extends BaseAction {
	
	// --zzq
	@Autowired
	private IRoleColumnBiz roleColumnBiz;
	@Autowired
	private IArticlePushBiz articlePushBiz;
	
	/**
	 * 站点管理业务层
	 */
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
	
	@Autowired
	private ActivityBizImpl activityBizImpl;
	
	@Autowired
	private GeneraterCore generaterCore;
	
	/**
	 * 判断是否为checkbox类型
	 */
	private static final int checkBox = 11;

	private List<DictEntity> dictList(String dictType, int appId) {
		DictEntity dict = new DictEntity();
		dict.setDictType(dictType);
		dict.setAppId(appId);
		return (List<DictEntity>) dictBiz.query(dict);
	}
	
	// --zzq
	private int [] getColumnId(int categoryId, int roleId, int appId) {
		int[] allColumnIds = null;
		int[] roleColumnIds = null;
		
		if (categoryId > 0) {
			allColumnIds = columnBiz.queryChildrenCategoryIds(categoryId, appId,
					BasicUtil.getModelCodeId(ModelCode.CMS_COLUMN));
		}
		
		List<RoleColumnEntity> rcList = roleColumnBiz.queryByRoleId(roleId);
		if(null != rcList && rcList.size()>0){
			roleColumnIds = new int[rcList.size()];
			for (int i = 0; i < rcList.size(); i++) {
				roleColumnIds[i] = rcList.get(i).getColumnId();
			}
		}
		
		if(null == allColumnIds && null == roleColumnIds){
			return null;
		}else if(null != allColumnIds && null == roleColumnIds){
			return allColumnIds;
		}else if(null == allColumnIds && null != roleColumnIds){
			return roleColumnIds;
		}else if(null != allColumnIds && null != roleColumnIds){
			List<Integer> idList = new ArrayList<Integer>();
			for(int outside=0; outside< allColumnIds.length; outside++){
				int id = allColumnIds[outside];
				for(int inside=0; inside< roleColumnIds.length; inside++){
					if(id == roleColumnIds[inside]){
						idList.add(id);
						break;
					}
				}
			}
			if(idList.size() == 0){
				return null;
			}
			int [] idArray = new int [idList.size()];
			for(int index=0; index< idList.size(); index++){
				idArray[index] = idList.get(index);
			}
			return idArray;
		}
		return null;
	}

	private List<ColumnEntity> getColumnList(int roleId, int appId) {
		List<RoleColumnEntity> rcList = roleColumnBiz.queryByRoleId(roleId);
		int [] columnIds = null;
		if(null != rcList && rcList.size()>0){
			columnIds = new int[rcList.size()];
			for (int i = 0; i < rcList.size(); i++) {
				columnIds[i] = rcList.get(i).getColumnId();
			}
		}
		List<ColumnEntity> list = columnBiz.queryByColumnIds(appId, columnIds);
		return list;
	}
	/**
	 * 获取文章属性
	 * 
	 * @return
	 */
	public List<Map.Entry<String, String>> articleType() {
		Map<String, String> map = getMapByProperties(com.uoumei.cms.constant.Const.ARTICLE_ATTRIBUTE_RESOURCE);
		Set<Entry<String, String>> set = map.entrySet();
		List<Map.Entry<String, String>> articleType = new ArrayList<Map.Entry<String, String>>();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			articleType.add(entry);
		}
		return articleType;
	}

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
		
		// role column --zzq
//		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request, ModelCode.CMS_COLUMN));
		List<ColumnEntity> list = this.getColumnList(managerSession.getManagerRoleID(), appId);
		
		request.setAttribute("listColumn", JSONArray.toJSONString(list));
		// 返回路径
		return view("/cms/article/index"); 
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/myindex")
	public String myIndex(HttpServletRequest request, ModelMap mode, HttpServletResponse response) {
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		
		// role column --zzq
//		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request, ModelCode.CMS_COLUMN));
		List<ColumnEntity> list = this.getColumnList(managerSession.getManagerRoleID(), appId);
		
		request.setAttribute("listColumn", JSONArray.toJSONString(list));
		// 返回路径
		return view("/cms/article/myindex"); 
	}

	/**
	 * 返回一个文章列表框架和一些基础数据
	 * 
	 * @param article
	 * @param request
	 * @param mode
	 * @param response
	 * @param categoryId
	 * @return 返回一个文章列表界面
	 */
	@RequestMapping("/{categoryId}/main")
	public String main(@ModelAttribute ArticleEntity article, HttpServletRequest request, ModelMap mode,
			HttpServletResponse response, @PathVariable int categoryId) {
		String articleType = request.getParameter("articleType");
		String isParent = BasicUtil.getString("isParent", "false");
		mode.addAttribute("isParent", isParent);
		// 使用糊涂工具排序使全部属性排在第一个
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		mode.addAttribute("articleTypeList", dictList("文章属性", managerSession.getBasicId()));
		mode.addAttribute("articleType", articleType);
		mode.addAttribute("categoryId", categoryId);
		// 返回文章页面显示地址
		return view("/cms/article/article_main");
	}
	
	@RequestMapping("/{categoryId}/mymain")
	public String myMain(@ModelAttribute ArticleEntity article, HttpServletRequest request, ModelMap mode,
			HttpServletResponse response, @PathVariable int categoryId) {
		String articleType = request.getParameter("articleType");
		String isParent = BasicUtil.getString("isParent", "false");
		mode.addAttribute("isParent", isParent);
		// 使用糊涂工具排序使全部属性排在第一个
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		mode.addAttribute("articleTypeList", dictList("文章属性", managerSession.getBasicId()));
		mode.addAttribute("articleType", articleType);
		mode.addAttribute("categoryId", categoryId);
		// 返回文章页面显示地址
		return view("/cms/article/myarticle_main");
	}

	/**
	 * 加载页面显示所有文章信息
	 * 
	 * @param request
	 * @return 返回文章页面显示数据
	 */
	@RequestMapping("/{categoryId}/list")
	public void list(@ModelAttribute ArticleEntity article, HttpServletRequest request, ModelMap mode,
			HttpServletResponse response, @PathVariable int categoryId) {
		int[] basicCategoryIds = null;
		String articleType = article.getArticleType();
		if (StringUtils.isEmpty(articleType)) {
			articleType = BasicUtil.getString("articleTypeStr");
		}
		String[] flags = null; 
		if (!StringUtils.isEmpty(articleType) ) {
			if(articleType.equals("a")){
				flags = null;
			}else{
				flags = articleType.split(",");
			}
		}
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		int currentRoleId = managerSession.getManagerRoleID();
		
		basicCategoryIds = getColumnId(categoryId, currentRoleId, appId);
		
		BasicUtil.startPage();
		article.setBasicDisplay(-1);
		article.setArticleState(-1);
		// 查询文章列表
		List<ArticleEntity> articleList = articleBiz.query(appId, basicCategoryIds, flags, null, null, true,
				article);
		EUListBean _list = new EUListBean(articleList, (int) BasicUtil.endPage(articleList).getTotal());
		// 将数据以json数据的形式返回
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd")));
	}
	
	@RequestMapping("/{categoryId}/mylist")
	public void myList(@ModelAttribute ArticleEntity article, HttpServletRequest request, ModelMap mode,
			HttpServletResponse response, @PathVariable int categoryId) {
		int[] basicCategoryIds = null;
		String articleType = article.getArticleType();
		if (StringUtils.isEmpty(articleType)) {
			articleType = BasicUtil.getString("articleTypeStr");
		}
		String[] flags = null; 
		if (!StringUtils.isEmpty(articleType) ) {
			if(articleType.equals("a")){
				flags = null;
			}else{
				flags = articleType.split(",");
			}
		}
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		int currentRoleId = managerSession.getManagerRoleID();
		
		basicCategoryIds = getColumnId(categoryId, currentRoleId, appId);
		
		BasicUtil.startPage();
		article.setBasicDisplay(-1);
		article.setArticleState(-1);
		article.setArticleManagerId(managerSession.getManagerId());
		// 查询文章列表
		List<ArticleEntity> articleList = articleBiz.query(appId, basicCategoryIds, flags, null, null, true,
				article);
		EUListBean _list = new EUListBean(articleList, (int) BasicUtil.endPage(articleList).getTotal());
		// 将数据以json数据的形式返回
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),
				new DateValueFilter("yyyy-MM-dd")));
	}

	/**
	 * 添加文章页面
	 * 
	 * @return 保存文章的页面地址
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/add")
	public String add(ModelMap mode, HttpServletRequest request) {
		int categoryId = this.getInt(request, "categoryId", 0);
		String categoryTitle = request.getParameter("categoryTitle");
		// 站点ID
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		// 文章属性
		mode.addAttribute("articleType", dictList("文章属性", managerSession.getBasicId()));
		mode.addAttribute("appId", appId);
		
		// role column --zzq
//		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request, ModelCode.CMS_COLUMN));
		List<ColumnEntity> list = this.getColumnList(managerSession.getManagerRoleID(), appId);
		mode.addAttribute("listColumn", JSONArray.toJSONString(list));
		
		boolean isEditCategory = false; // 新增，不是单篇
		int columnType = 1;// 新增，不是单篇
		if (categoryId != 0) {
			// 获取栏目id
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(categoryId);
			columnType = column.getColumnType();
			// 判断栏目是否为"",如果是"",就重新赋值
			if (StringUtil.isBlank(categoryTitle)) {
				categoryTitle = column.getCategoryTitle();
			}
			// 判断栏目是否是单篇
			if (column != null && column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {
				isEditCategory = true; // 是单页
				columnType = column.getColumnType();
			}
		}
		mode.addAttribute("categoryTitle", categoryTitle);
		mode.addAttribute("isEditCategory", isEditCategory); // 新增状态
		mode.addAttribute("columnType", columnType);
		mode.addAttribute("categoryId", categoryId);
//		mode.addAttribute("articleImagesUrl",
//				com.uoumei.basic.constant.Const.UPLOAD_PATH + com.uoumei.base.constant.Const.SEPARATOR
//						+ managerSession.getBasicId() + com.uoumei.base.constant.Const.SEPARATOR);
		//上传文件服务器
		mode.addAttribute("uploadServerUrl", com.uoumei.basic.constant.Const.UPLOAD_SERVER_URL);
		// 添加一个空的article实体
		ArticleEntity article = new ArticleEntity();
		mode.addAttribute("article", article);
		// 返回路径
		return view("/cms/article/article_form"); // 这里表示显示/manager/cms/article/article_save.ftl
	}

	/**
	 * 获取表单信息进行保存
	 * 
	 * @param article
	 *            文章对象
	 */
	@RequestMapping("/save")
	@RequiresPermissions("article:save")
	public void save(@ModelAttribute ArticleEntity article, HttpServletRequest request, HttpServletResponse response) {
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		AppEntity app = (AppEntity) appBiz.getEntity(appId);
		
		// 验证文章，文章自由排序，栏目id
		if (!validateForm(article, response)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false);
			return;
		}
		
		String articleType = BasicUtil.getString("radio");
		//检查articleUrl
		if(articleType.equals("h")){
			if(!RegexUtil.isRightUrl(article.getArticleUrl())){
				this.outJson(response, false, "无效的链接地址");
				return;
			}
		}
		article.setArticleType(articleType);
		
		// 问题:由于上传的图片路径后面可能带有｜符合。所以要进行将“｜”替换空
		// 空值判断
		if (!StringUtil.isBlank(article.getBasicThumbnails())) {
			article.setBasicThumbnails(article.getBasicThumbnails().replace("|", ""));
		}
		ColumnEntity column = (ColumnEntity) columnBiz.getEntity(article.getBasicCategoryId());
		article.setColumn(column);

		// 添加文章所属的站点id
		article.setArticleWebId(appId);
		article.setBasicAppId(appId);
		article.setBasicModelId(BasicUtil.getInt("modelId"));
		article.setArticleManagerId(managerSession.getManagerId());
		article.setBasicUpdateTime(new Date());
		articleBiz.saveBasic(article);
		
		if(StringUtil.isBlank(article.getArticleUrl())){
			if (column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_LIST.toInt()) {// 列表
				article.setArticleUrl(column.getColumnPath() + File.separator + article.getBasicId() + ".html");
			} else if (column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {// 单篇
				article.setArticleUrl(column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);
			}
		}else{
			article.setArticleUrl(article.getArticleUrl());
		}


		//--zzq 需要审核，启动工作流，否则根据时间判断是否立即静态化
		//--zzq 判断是否需要审核，根据时间设置状态
		int nColumnId = column.getCategoryId();
		ProcColumnEntity pcEntity = activityBizImpl.queryProcByColumnId(nColumnId);
		if(null != pcEntity){     //need audit
			article.setArticleState(Const.ARTICLE_STATE_AUDIT);
			articleBiz.updateBasic(article);
			
			activityBizImpl.startWorkflow(article.getBasicId(), managerSession.getManagerId(), pcEntity);
		}else{
			Timestamp currTime = new Timestamp(System.currentTimeMillis());
			if(currTime.before(article.getBasicDateTime())){
				article.setArticleState(Const.ARTICLE_STATE_WAIT);
				article.setArticleNote("无审核");
				articleBiz.updateBasic(article);
			}else {
				article.setArticleState(Const.ARTICLE_STATE_PUB);
				article.setArticleNote("无审核");
				articleBiz.updateBasic(article);
				
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
				article.setArticleID(article.getBasicId());
				generaterCore.generateOneArticle(article, column, app, generatePath, tmpPath, app.getAppHostUrl());
			}
		}
		
		// 判断栏目是否存在新增字段
		if (column.getColumnContentModelId() != 0) {
			// 保存所有的字段信息
			List<BaseEntity> listField = fieldBiz.queryListByCmid(column.getColumnContentModelId());
			// 获取内容模型实体
			ContentModelEntity contentModel = (ContentModelEntity) contentBiz
					.getEntity(column.getColumnContentModelId());
			if (contentModel != null) {
				// 保存新增字段的信息
				Map param = this.checkField(listField, request, article.getBasicId());
				fieldBiz.insertBySQL(contentModel.getCmTableName(), param);
			}

		}

		//返回
		if (article.getColumn().getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {
			this.outJson(response, ModelCode.CMS_ARTICLE, true, "" + article.getColumn().getCategoryId(),
					article.getBasicId());
		} else {
			this.outJson(response, ModelCode.CMS_ARTICLE, true, article.getColumn().getCategoryId() + "", "");
		}
	}

	/**
	 * 验证表单
	 * 
	 * @param article
	 * @param response
	 * @return 返回Boolean类型 true：通过，false:有错
	 */
	public boolean validateForm(ArticleEntity article, HttpServletResponse response) {
		// 对表单数据进行再次验证
		// 验证文章标题是否为空
		if (StringUtil.isBlank(article.getBasicTitle())) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.empty", this.getResString("basicTitle")));
			return false;
		}
		// 验证文章所属是否为0
		if (article.getBasicCategoryId() == 0) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.empty", this.getResString("basicCategoryId")));
			return false;
		}
		// 验证文章标题长度,若超过定义长度则截取
		if (!StringUtil.checkLength(article.getBasicTitle(), 1, 300)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.length", this.getResString("basicTitle"), "1", "300"));
			return false;
		}
		// 验证文章来源长度,若超过定义长度则截取
		if (!StringUtil.isBlank(article.getArticleSource())
				&& !StringUtil.checkLength(article.getArticleSource(), 1, 300)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.length", this.getResString("articleSource"), "1", "300"));
			return false;
		}
		// 验证文章作者长度,若超过定义长度则截取
		if (!StringUtil.isBlank(article.getArticleAuthor())
				&& !StringUtil.checkLength(article.getArticleAuthor(), 1, 12)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.length", this.getResString("articleAuthor"), "1", "12"));
			return false;
		}
		// 验证文章描述长度,若超过定义长度则截取
		if (!StringUtil.isBlank(article.getBasicDescription())
				&& !StringUtil.checkLength(article.getBasicDescription(), 1, 400)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.length", this.getResString("basicDescription"), "1", "400"));
			return false;
		}
		// 验证文章关键字长度,若超过定义长度则截取
		if (!StringUtil.isBlank(article.getArticleKeyword())
				&& !StringUtil.checkLength(article.getArticleKeyword(), 1, 155)) {
			this.outJson(response, ModelCode.CMS_ARTICLE, false,
					getResString("err.length", this.getResString("articleKeyword"), "1", "155"));
			return false;
		}
		return true;
	}

	/**
	 * 显示更新内容
	 * 
	 * @param request
	 * @return 修改文章的页面地址
	 */
	@RequestMapping("/{id}/edit")
	public String edit(@PathVariable int id, ModelMap model, HttpServletRequest request) {

		// 如果_categoryId大于0表示是编辑封面栏目，应该先查询分类下面的唯一一篇文章
		String categoryTitle = request.getParameter("categoryTitle");
		// 板块id
		int categoryId = this.getInt(request, "categoryId", 0);
		ArticleEntity articleEntity = null;

		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();

		model.addAttribute("appId", appId);
		model.addAttribute("articleImagesUrl", "/upload/" + managerSession.getBasicId() + "/");
		
		//上传文件服务器
		model.addAttribute("uploadServerUrl", com.uoumei.basic.constant.Const.UPLOAD_SERVER_URL);
		
		if (categoryId > 0) { // 分类获取文章
			articleEntity = articleBiz.getByCategoryId(categoryId);
			ColumnEntity column = articleEntity.getColumn();
			int columnType = column.getColumnType();
			model.addAttribute("article", articleEntity);
			// 文章属性
			model.addAttribute("articleType", dictList("文章属性", appId));
			model.addAttribute("categoryTitle", categoryTitle);
			model.addAttribute("categoryId", categoryId);// 编辑封面
			model.addAttribute("isEditCategory", true);// 编辑封面
			model.addAttribute("columnType", columnType);
			return view("/cms/article/article_form");
		} else if (id > 0) { // 文章id获取
			// 允许编辑文章时更改分类
			// role column --zzq
//			List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request, ModelCode.CMS_COLUMN));
			List<ColumnEntity> list = this.getColumnList(managerSession.getManagerRoleID(), appId);
			request.setAttribute("listColumn", JSONArray.toJSONString(list));
			
			// 文章属性
			model.addAttribute("articleType", dictList("文章属性", appId));

			articleEntity = (ArticleEntity) articleBiz.getEntity(id);
			model.addAttribute("article", articleEntity);
			// 判断是否是封面类型的栏目，如果是封面类型的栏目有些信息需要屏蔽，例如分类
			ColumnEntity column = articleEntity.getColumn();
			int columnType = column.getColumnType();
			if (column.getColumnType() == ColumnEntity.COLUMN_TYPE_COVER) {
				model.addAttribute("categoryTitle", categoryTitle);
				model.addAttribute("categoryId", column.getCategoryId());// 编辑封面
				model.addAttribute("isEditCategory", true);// 编辑封面
			} else {
				model.addAttribute("categoryTitle", articleEntity.getColumn().getCategoryTitle());
				model.addAttribute("isEditCategory", false);// 编辑文章
			}
			model.addAttribute("columnType", columnType);
			model.addAttribute("categoryId", column.getCategoryId());// 编辑封面
			return view("/cms/article/article_form");
		} else {// 非法
			// return view("/cms/article/article_form");
			return this.redirectBack(request, true);
		}
	}
	/**
	 * 更新文章
	 * 
	 * @param basicId
	 *            文章id
	 * @param article
	 *            文章实体
	 * @param request
	 * @param response
	 */
	@RequestMapping("/{basicId}/update")
	@RequiresPermissions("article:update")
	public void update(@PathVariable int basicId, @ModelAttribute ArticleEntity article, HttpServletRequest request,
			HttpServletResponse response) {
		//获取文章状态
		int articleState = article.getArticleState();
		if(articleState == Const.ARTICLE_STATE_AUDIT || articleState == Const.ARTICLE_STATE_PUB){
			this.outJson(response, ModelCode.CMS_ARTICLE, false, "不能编辑已发布或审核状态的内容", this.redirectBack(request, false));
			return;
		}
		
		// 获取站点id
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		AppEntity app = (AppEntity) appBiz.getEntity(appId);
		
		String articleType = BasicUtil.getString("radio");
		//检查articleUrl
		if(articleType.equals("h")){
			if(!RegexUtil.isRightUrl(article.getArticleUrl())){
				this.outJson(response, false, "无效的链接地址");
				return;
			}
		}
		article.setArticleType(articleType);
		
		// 问题:由于上传的图片路径后面可能带有｜符合。所以要进行将“｜”替换空
		// 空值判断
		if (!StringUtil.isBlank(article.getBasicThumbnails())) {
			article.setBasicThumbnails(article.getBasicThumbnails().replace("|", ""));
		}
		
		// 获取更改前的文章实体
		ArticleEntity oldArticle = (ArticleEntity) articleBiz.getEntity(basicId);
		// 获取栏目实体
		ColumnEntity column = (ColumnEntity) columnBiz.getEntity(article.getBasicCategoryId());
		if (!StringUtil.isBlank(oldArticle)) {
			// 获取更改前的文章所属栏目实体
			ColumnEntity oldColumn = (ColumnEntity) columnBiz.getEntity(oldArticle.getBasicCategoryId());

			// 通过表单类型id判断是否更改了表单类型,如果更改则先删除记录
			if (oldColumn.getColumnContentModelId() != column.getColumnContentModelId()) {
				// 获取旧的内容模型id
				ContentModelEntity contentModel = (ContentModelEntity) contentBiz
						.getEntity(oldColumn.getColumnContentModelId());
				// 删除旧的内容模型中保存的值
				Map wheres = new HashMap();
				wheres.put("basicId", article.getBasicId());
				if (contentModel != null) {
					fieldBiz.deleteBySQL(contentModel.getCmTableName(), wheres);
				}
				// 判断栏目是否存在新增字段
				if (column.getColumnContentModelId() != 0) {
					// 保存所有的字段信息
					List<BaseEntity> listField = fieldBiz.queryListByCmid(column.getColumnContentModelId());
					ContentModelEntity newContentModel = (ContentModelEntity) contentBiz
							.getEntity(column.getColumnContentModelId());
					if (newContentModel != null) {
						Map param = this.checkField(listField, request, article.getBasicId());
						fieldBiz.insertBySQL(newContentModel.getCmTableName(), param);
					}
				}
			}
		}

		article.setColumn(column);
		article.setArticleWebId(appId);
		article.setArticleManagerId(managerSession.getManagerId());
		article.setBasicUpdateTime(new Date());
		if(!articleType.equals("h")){
			if (column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_LIST.toInt()) {// 列表
				article.setArticleUrl(column.getColumnPath() + File.separator + article.getBasicId() + ".html");
			} else if (column.getColumnType() == ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {// 单篇
				article.setArticleUrl(column.getColumnPath() + File.separator + IParserRegexConstant.HTML_INDEX);
			}
		}
		
		//--zzq 需要审核，启动工作流，否则根据时间判断是否立即静态化
		//--zzq 判断是否需要审核，根据时间设置状态
		int nColumnId = column.getCategoryId();
		ProcColumnEntity pcEntity = activityBizImpl.queryProcByColumnId(nColumnId);
		if(null != pcEntity){     //need audit
			article.setArticleState(Const.ARTICLE_STATE_AUDIT);
			articleBiz.updateBasic(article);
			
			activityBizImpl.startWorkflow(article.getBasicId(), managerSession.getManagerId(), pcEntity);
		}else{
			Timestamp currTime = new Timestamp(System.currentTimeMillis());
			if(currTime.before(article.getBasicDateTime())){
				article.setArticleState(Const.ARTICLE_STATE_WAIT);
				article.setArticleNote("无审核");
				articleBiz.updateBasic(article);
			}else {
				article.setArticleState(Const.ARTICLE_STATE_PUB);
				article.setArticleNote("无审核");
				articleBiz.updateBasic(article);
				
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
				article.setArticleID(article.getBasicId());
				generaterCore.generateOneArticle(article, column, app, generatePath, tmpPath, app.getAppHostUrl());

			}
		}

		// 判断该文章是否存在新增字段
		if (column.getColumnContentModelId() != 0) {
			// 保存所有的字段信息
			List<BaseEntity> listField = fieldBiz.queryListByCmid(column.getColumnContentModelId());
			// // update中的where条件
			Map where = new HashMap();
			// 压入默认的basicId字段
			where.put("basicId", article.getBasicId());
			// 遍历字段的信息
			Map param = this.checkField(listField, request, article.getBasicId());
			ContentModelEntity contentModel = (ContentModelEntity) contentBiz
					.getEntity(column.getColumnContentModelId());
			if (contentModel != null) {
				// 遍历所有的字段实体,得到字段名列表信息
				List<String> listFieldName = new ArrayList<String>();
				listFieldName.add("basicId");
				// 查询新增字段的信息
				List fieldLists = fieldBiz.queryBySQL(contentModel.getCmTableName(), listFieldName, where);

				// 判断新增字段表中是否存在该文章，不存在则保存，否则更新
				if (fieldLists == null || fieldLists.size() == 0) {
					fieldBiz.insertBySQL(contentModel.getCmTableName(), param);
				} else {
					fieldBiz.updateBySQL(contentModel.getCmTableName(), param, where);
				}

			}
		}
		//
		switch (column.getColumnType()) {
		case ColumnEntity.COLUMN_TYPE_COVER:
			this.outJson(response, ModelCode.CMS_ARTICLE, true, column.getCategoryId() + "", "");
			break;
		case ColumnEntity.COLUMN_TYPE_LIST:
			this.outJson(response, ModelCode.CMS_ARTICLE, true, column.getCategoryId() + "",
					this.redirectBack(request, false));
		}

	}
	
	@RequestMapping("/{id}/preview")
	public String preview(@PathVariable int id, ModelMap model, 
			HttpServletRequest request, HttpServletResponse response) {
		
//		PrintWriter out;
//		try {
//			out = response.getWriter();
//			out.print("<html><head></head><body> html内容 </body>");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ArticleEntity articleEntity = (ArticleEntity)articleBiz.getEntity(id);
		model.addAttribute("articleEntity", articleEntity);
		return view("/cms/article/article_preview");
	}


	/**
	 * 删除文章
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("article:del")
	public void delete(@RequestBody List<ArticleEntity> articles, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		AppEntity app = (AppEntity) appBiz.getEntity(appId);
		
		//check article state
		boolean hasAudit = false;
		List<ArticleEntity> articlesPub = new ArrayList<ArticleEntity>();
		List<ArticleEntity> articlesNotPub = new ArrayList<ArticleEntity>();
		for(ArticleEntity article:articles){
			if(article.getArticleState() == Const.ARTICLE_STATE_AUDIT){
				hasAudit = true;
				break;
			}
			if (article.getArticleState() == Const.ARTICLE_STATE_PUB){
				articlesPub.add(article);
			}else{
				articlesNotPub.add(article);
			}
		}
		//有审核中内容，直接返回
		if(hasAudit){
			this.outJson(response, ModelCode.CMS_ARTICLE, false, "不能删除审核中内容", this.redirectBack(request, false));
			return;
		}
		
		//未发布的内容，直接删除
		int[] ids = new int[articlesNotPub.size()];
		for (int i = 0; i < articlesNotPub.size(); i++) {
			ids[i] = articlesNotPub.get(i).getArticleID();
		}
		if (ids.length > 0) {
			articleBiz.deleteBasic(ids);
			FileExtUtil.del(articlesNotPub);
		}
		
		//发布的内容，更新状态，并静态化
		//生成保存htm页面地址
		String generatePath = PathUtil.getGeneratePath(app);	
		FileUtil.createFolder(generatePath);
		//网站风格路径
		String tmpPath = PathUtil.getTmpPath(app);
		for(ArticleEntity article:articlesPub){
			//逻辑删除
			article.setBasicDel(1);
			articleBiz.updateBasic(article);
			
			//静态化
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(article.getBasicCategoryId());
			article.setColumn(column);

			List<ColumnEntity> list = columnBiz.queryParentColumnByColumnId(column.getCategoryId());
			list.add(column);
			for(ColumnEntity tempColumn : list){
				generaterCore.generateOneColumn(tempColumn, app, generatePath, tmpPath, app.getAppHostUrl());
			}
			FileUtil.delFile(generatePath + article.getArticleUrl());
		}
		if(articlesPub.size()>0){
			generaterCore.generateOneIndex(app, generatePath, tmpPath, "index.html", "index.htm");
		}
		
		this.outJson(response, ModelCode.CMS_ARTICLE, true, "", this.redirectBack(request, false));
	}

	/**
	 * 推送文章
	 */
	@RequestMapping("/push")
	public void push(@RequestBody List<ArticleEntity> articles, 
			HttpServletRequest request,HttpServletResponse response) {
		String type = request.getParameter("type");
		if(StringUtil.isBlank(type)){
			this.outJson(response, ModelCode.CMS_ARTICLE, false, "参数类型错误", this.redirectBack(request, false));
			return;
		}
		int nType = Integer.parseInt(type);
		if(nType != 1 && nType != 2){
			this.outJson(response, ModelCode.CMS_ARTICLE, false, "参数类型错误", this.redirectBack(request, false));
			return;
		}
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();
		AppEntity app = (AppEntity) appBiz.getEntity(appId);
		
		//check article state
		boolean hasNotPub = false;
		for(ArticleEntity article:articles){
			if(article.getArticleState() != Const.ARTICLE_STATE_PUB){
				hasNotPub = true;
				break;
			}
		}
		//有审核中内容，直接返回
		if(hasNotPub){
			this.outJson(response, ModelCode.CMS_ARTICLE, false, "只能推送已发布的内容", this.redirectBack(request, false));
			return;
		}
		
		for(ArticleEntity article:articles){
			ArticlePushEntity apEntity = new ArticlePushEntity();
			apEntity.setLinkTitle(article.getBasicTitle());
			
			if(RegexUtil.isRightUrl(article.getArticleUrl())){
				apEntity.setLinkUrl(article.getArticleUrl());
			}else{
				apEntity.setLinkUrl(app.getAppHostUrl()+ article.getArticleUrl());
			}
			if(RegexUtil.isRightUrl(article.getBasicThumbnails())){
				apEntity.setLinkThumbnails(article.getBasicThumbnails());
			}else{
				//--zzq 20190225
				if(!StringUtil.isBlank(article.getBasicThumbnails())){
					apEntity.setLinkThumbnails(app.getAppHostUrl()+ article.getBasicThumbnails());
				}
			}
			//--zzq 20190328
			ArticleEntity articleEntity = (ArticleEntity) articleBiz.getEntity(article.getArticleID());
			apEntity.setPubTime(articleEntity.getBasicDateTime());
			
			apEntity.setCreateTime(new Date());

			if(nType == 1) {
				apEntity.setAppId(1); //主站
			}else{
				apEntity.setAppId(appId); //本站
			}
			apEntity.setSrcAppId(appId);
			apEntity.setSrcAppName(app.getAppName());
			if(article.getColumn() != null){
				apEntity.setSrcColumnId(article.getColumn().getCategoryId());
				apEntity.setSrcColumnName(article.getColumn().getCategoryTitle());
			}

			articlePushBiz.saveEntity(apEntity);
		}

		this.outJson(response, ModelCode.CMS_ARTICLE, true, "", this.redirectBack(request, false));
	}
	
	/**
	 * 查询单页栏目是否绑定了文章
	 * 
	 * @param article
	 *            文章对象
	 */
	@RequestMapping("/{id}/queryColumnArticle")
	public void queryColumnArticle(@PathVariable int id, HttpServletResponse response) {
		List articls = articleBiz.queryListByColumnId(id,null);
		if (articls == null || articls.size() == 0) {
			this.outJson(response, ModelCode.CMS_ARTICLE, true, null);
		} else {
			this.outJson(response, ModelCode.CMS_ARTICLE, false, null);
		}
	}
	@RequestMapping("/{id}/myqueryColumnArticle")
	public void myQueryColumnArticle(@PathVariable int id, 
			HttpServletRequest request, HttpServletResponse response) {
		
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		
		ArticleEntity articleEntityWhere = new ArticleEntity();
		articleEntityWhere.setArticleManagerId(managerSession.getManagerId());
		
		List articls = articleBiz.queryListByColumnId(id, articleEntityWhere);
		if (articls == null || articls.size() == 0) {
			this.outJson(response, ModelCode.CMS_ARTICLE, true, null);
		} else {
			this.outJson(response, ModelCode.CMS_ARTICLE, false, null);
		}
	}
//////////////////////////////	
	/**
	 * 遍历出所有文章新增字段的信息
	 * 
	 * @param listField
	 *            :字段列表
	 * @param request
	 * @param articleId
	 *            文章id
	 * @return 字段信息
	 */
	private Map checkField(List<BaseEntity> listField, HttpServletRequest request, int articleId) {
		Map mapParams = new HashMap();
		// 压入默认的basicId字段
		mapParams.put("basicId", articleId);
		// 遍历字段名
		for (int i = 0; i < listField.size(); i++) {
			ContentModelFieldEntity field = (ContentModelFieldEntity) listField.get(i);
			String fieldName = field.getFieldFieldName();
			// 判断字段类型是否为checkbox类型
			if (field.getFieldType() == checkBox) {
				String langtyp[] = request.getParameterValues(field.getFieldFieldName());
				if (langtyp != null) {
					StringBuffer sb = new StringBuffer();
					for (int j = 0; j < langtyp.length; j++) {
						sb.append(langtyp[j] + ",");
					}
					mapParams.put(field.getFieldFieldName(), sb.toString());
				} else {
					mapParams.put(field.getFieldFieldName(), langtyp);
				}
			} else {
				if (StringUtil.isBlank(request.getParameter(field.getFieldFieldName()))) {
					mapParams.put(field.getFieldFieldName(), null);
				} else {
					mapParams.put(field.getFieldFieldName(), request.getParameter(field.getFieldFieldName()));
				}
			}
		}
		return mapParams;
	}

}