package com.uoumei.mwebsite.action;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mchange.v1.util.ArrayUtils;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.biz.IRoleBiz;
import com.uoumei.basic.biz.IRoleModelBiz;
import com.uoumei.basic.constant.Const;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.constant.e.CookieConstEnum;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.basic.entity.ModelEntity;
import com.uoumei.basic.entity.RoleEntity;
import com.uoumei.basic.entity.RoleModelEntity;
import com.uoumei.mwebsite.biz.IWebsiteBiz;
import com.uoumei.mwebsite.entity.WebsiteEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.util.SpringUtil;
import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.bean.ListBean;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.cms.util.PathUtil;

/**
 * 网站基本信息控制层
 */
@Controller
@RequestMapping("/${managerPath}/website")
public class WebsiteAction extends BaseAction{
	
	/**
	 * appBiz业务层的注入
	 */
	@Autowired
	private IWebsiteBiz websiteBiz;
	
	/**
	 * managerBiz业务层的注入
	 */
	@Autowired
	private IManagerBiz managerBiz;
	
	/**
	 * 返回站点管理页面
	 * @param request
	 * @param mode
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request,ModelMap mode,HttpServletResponse response){
		return view("/mwebsite/index");
	}
	
	/**
	 * 查询站点列表信息
	 * @param request 请求对象
	 * @param mode ModelMap实体对象
	 * @param response 响应对象
	 * @return 站点列表数据
	 */
	@RequestMapping("/list")
	public void list(@ModelAttribute WebsiteEntity websiteEntity, HttpServletRequest request,HttpServletResponse response){
		BasicUtil.startPage();
		List<BaseEntity> websiteList = websiteBiz.query(websiteEntity);
		EUListBean _list = new EUListBean(websiteList, (int) BasicUtil.endPage(websiteList).getTotal());
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(_list, new DoubleValueFilter(),new DateValueFilter("yyyy-MM-dd")));
	}
	
	/**
	 * 根据id删除站点信息
	 * @param basicId 要删除的站点Id
	 * @param request 请求对象
	 * @return 返回当前页数
	 */
	@RequestMapping("/{basicId}/delete")
	@ResponseBody
	public void delete(@PathVariable int basicId, HttpServletRequest request){
		WebsiteEntity website= (WebsiteEntity) websiteBiz.getEntity(basicId);
		/*
		 * 删除对应的站点管理员
		 */
		if(website!=null){
			int managerId = website.getWebsiteManagerId();
			managerBiz.deleteEntity(managerId);
			websiteBiz.deleteEntity(basicId);
		}
	}
	
	/**
	 * Mstore标识是否显示
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ckeckedStore")
	public void ckeckedStore(HttpServletRequest request,HttpServletResponse response){
		//判断是否是管理员
		if(this.isSystemManager(request)){
			this.outJson(response, true);
		}else{
			this.outJson(response, false);
		}
	}
	
	/**
	 * 批量id删除站点信息
	 * @param basicId 要删除的站点Id
	 * @param request 请求对象
	 * @return 返回当前页数
	 */
	@RequestMapping("/batchDelete")
	@ResponseBody
	public void batchDelete(HttpServletRequest request,HttpServletResponse response){
		String websiteIds = request.getParameter("websiteIds");
		if (StringUtil.isBlank(websiteIds) && StringUtil.isInteger(websiteIds.split(","))) {
			this.outJson(response, null, false);
		} else {
			int[] ids = StringUtil.stringsToInts(websiteIds.split(","));
			/* --zzq
			int appId = this.getAppId(request);
			Arrays.sort(ids);
			int postion = Arrays.binarySearch(ids, appId);
			if(postion>-1) {
				ids[postion] = 0;
			}*/
			websiteBiz.delete(ids);
			this.outJson(response, null, true);
		}
	}
	
	/**
	 * 跳转到站点保存页面
	 * @param request 请求对象
	 * @param mode ModelMap实体对象
	 * @return 站点保存页面
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request,ModelMap mode){
		//超级管理员识别
		mode.addAttribute("SystemManager",true);
		WebsiteEntity website = new WebsiteEntity();
		//传入一个空的app
		mode.addAttribute("website",website);
		return view("/mwebsite/website_form");
	}
	
	/**
	 * 跳转到修改页面
	 * @param mode ModelMap实体对象
	 * @param appId 站点id
	 * @param request 请求对象
	 * @return 站点修改页面
	 */
	@RequestMapping(value="/{websiteId}/edit")
	public String edit(ModelMap mode,@PathVariable int websiteId, HttpServletRequest request) {
		WebsiteEntity website = null;
		if (websiteId<0) {
			website = this.getWebsite(request);
		} else {
			website = (WebsiteEntity) websiteBiz.getEntity(websiteId);
		}

		//判断否是超级管理员,是的话不显示站点风格
		if(this.isSystemManager(request)){
			mode.addAttribute("SystemManager",true);
		}else{
			mode.addAttribute("SystemManager",false);
		}
		mode.addAttribute("website",website);
		return view("/mwebsite/website_edit_form");
	}

	/**
	 * 更新站点信息
	 * @param mode ModelMap实体对象
	 * @param app	站点对象
	 * @param request 请求对象
	 * @param response 相应对象
	 */
	@RequestMapping("/update")
	public void update(ModelMap mode,@ModelAttribute WebsiteEntity website, HttpServletRequest request, HttpServletResponse response){
		mode.clear();
		// 获取Session值
		ManagerEntity managerSession = (ManagerEntity) getManagerBySession(request);
		if(managerSession==null){
			return ;
		}
		mode.addAttribute("managerSession",managerSession);
		/*
		//判断否是超级管理员,不是则不修改应用续费时间和清单
		if(!this.isSystemManager(request)){
			website.setWebsitePayDate(null);
			website.setWebsitePay(null);
			// 设置当前的站点id
			website.setWebsiteId(this.getAppId(request));
		}
		*/
		int managerRoleID = managerSession.getManagerRoleID();
		//判断站点数据的合法性
		// 获取cookie
		String cookie =this.getCookie(request, CookieConstEnum.PAGENO_COOKIE);
		int pageNo = this.getPageNo(request);
		mode.addAttribute("pageNo", pageNo);
		if(!checkForm(website,response)){
			return;
		}
		if(!StringUtil.isBlank(website.getWebsiteLogo())) {
			website.setWebsiteLogo( website.getWebsiteLogo().replace("|", ""));
		}
		//更新站点信息
		websiteBiz.updateEntity(website);
		
		//
		if(!StringUtil.isBlank(website.getWebsiteStyle())){
			PathUtil.syncFolder(website.getWebsiteId(), website.getWebsiteStyle());
		}
		this.outJson(response, ModelCode.APP, true, String.valueOf(pageNo), String.valueOf(managerRoleID));
	}
	
	/**
	 * 保存站点信息
	 * @param app 站点实体对象
	 * @param request 请求对象
	 * @param response 相应对象
	 */
	@RequestMapping("/save")
	public void save(@ModelAttribute WebsiteEntity website, HttpServletRequest request, HttpServletResponse response){
		//验证站点数据的合法性
		if(!checkForm(website,response)){
			return;
		}
		//问题:由于上传的图片路径后面可能带有｜符合。所以要进行将“｜”替换空
		//空值判断
		if(!StringUtil.isBlank(website.getWebsiteLogo())) {
			website.setWebsiteLogo( website.getWebsiteLogo().replace("|", ""));
		}
		//问题:去掉域名后面的"/"
		//String appUrl = website.getWebsiteHostUrl();
		//website.setWebsiteUrl(appUrl);
		
		//--zzq
		if(websiteBiz.countByUrl(website.getWebsiteUrl())>0){
			this.outJson(response, ModelCode.APP, false, getResString("err.exist", this.getResString("appUrl")));
			return;
		}

		websiteBiz.saveEntity(website);
		if(isSystemManager(request)) {
			//--zzq
//			String file = this.getRealPath(request,IParserRegexConstant.REGEX_SAVE_TEMPLATE+File.separator+ website.getWebsiteId());
//			File fileName = new File(file);
//	        fileName.mkdir();
			String tmpRootPath = PathUtil.getTmpRootPath(website.getWebsiteId());
			FileUtil.createFolder(tmpRootPath);
		}
		//--zzq
		if(!StringUtil.isBlank(website.getWebsiteStyle())){
			PathUtil.syncFolder(website.getWebsiteId(), website.getWebsiteStyle());
		}
		this.outJson(response, ModelCode.APP, true,null);
	}
	
	/**
	 * 判断站点域名的合法性
	 * @param app 要验证的站点信息
	 * @param response response对象
	 */
	public boolean checkForm(WebsiteEntity website, HttpServletResponse response){
		/*
		 * 判断数据的合法性
		 */
		if(!(StringUtil.isBlank(website.getWebsiteKeyword())) && !StringUtil.checkLength(website.getWebsiteKeyword(), 0,1000)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.keyword",com.uoumei.mwebsite.constant.Const.RESOURCES),"0","1000"));
			return false;
		}
		if(!(StringUtil.isBlank(website.getWebsiteCopyright())) && !StringUtil.checkLength(website.getWebsiteCopyright(), 0,1000)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.copyright",com.uoumei.mwebsite.constant.Const.RESOURCES),"0","1000"));
			return false;
		}
		if(!(StringUtil.isBlank(website.getWebsiteDescription())) && !StringUtil.checkLength(website.getWebsiteDescription(), 0,1000)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.descrip",com.uoumei.mwebsite.constant.Const.RESOURCES),"0","1000"));
			return false;
		}
		if(!StringUtil.checkLength(website.getWebsiteName(),1,50)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.title",com.uoumei.mwebsite.constant.Const.RESOURCES),"1","50"));
			return false;
		}
		if(!(StringUtil.isBlank(website.getWebsiteStyle()))  && !StringUtil.checkLength(website.getWebsiteStyle(),1,30)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.style",com.uoumei.mwebsite.constant.Const.RESOURCES),"1","30"));
			return false;
		}
		if(!StringUtil.checkLength(website.getWebsiteHostUrl(),10,150)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.url",com.uoumei.mwebsite.constant.Const.RESOURCES),"10","150"));
			return false;
		}
		if(!(StringUtil.isBlank(website.getWebsitePay()))  &&  !StringUtil.checkLength(website.getWebsitePay(),0,500)){
			this.outJson(response, ModelCode.APP, false,getResString("err.length",this.getResString("website.pay",com.uoumei.mwebsite.constant.Const.RESOURCES),"0","500"));
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否有重复的域名
	 * @param request 请求对象
	 * @return true:重复,false:不重复
	 */
	@RequestMapping("/checkUrl")
	@ResponseBody
	public boolean checkUrl(HttpServletRequest request){
		if(request.getParameter("websiteUrl")!=null){
			if(websiteBiz.countByUrl(request.getParameter("websiteUrl"))>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	/**
	 * 安装初始化
	 */
	@RequestMapping("/init")
	public void init(HttpServletRequest request,HttpServletResponse response) {
		IRoleBiz roleBiz = (IRoleBiz) SpringUtil.getBean(IRoleBiz.class);
	    IWebsiteBiz websiteBiz = (IWebsiteBiz) SpringUtil.getBean(IWebsiteBiz.class);
	    RoleEntity role = new RoleEntity();
		if (roleBiz.getEntity(1) == null) {
			role.setRoleId(1);
			role.setRoleName("超级管理员");
			websiteBiz.saveRole(role);
		}
	    //第三步，创建超级管理员。
		IManagerBiz managerBiz = (IManagerBiz) SpringUtil.getBean(IManagerBiz.class);
		ManagerEntity managerEntity = new ManagerEntity();
		// 管理员昵称，默认是adminms
		managerEntity.setManagerName("adminms");
		if(managerBiz.getEntity(managerEntity) == null){
			// 管理员密码，默认是msopen
			managerEntity.setManagerPassword("9d8622060de5f24937b60585c3f4d66b");
			// 管理员角色id
			managerEntity.setManagerRoleID(1);
			managerEntity.setManagerTime(new Date());
			managerBiz.saveEntity(managerEntity);
		}
	    //第四步，载入菜单。
		IModelBiz modelBiz = (IModelBiz) SpringUtil.getBean(IModelBiz.class);
		IRoleModelBiz roleModelBiz = (IRoleModelBiz) SpringUtil.getBean(IRoleModelBiz.class); 
		// 插入角色与模块的关联关系
		List<RoleModelEntity> listRoleModel = new ArrayList<RoleModelEntity>();
		// 插入系统设置模块
		ModelEntity _model = modelBiz.getEntityByModelCode("00000000");
		ModelEntity systemModel = new ModelEntity();
		systemModel.setModelCode("00000000");
		systemModel.setModelIcon("&#xe71f");
		systemModel.setModelTitle("系统设置");
		systemModel.setModelModelId(0);
		if(_model == null){
			modelBiz.saveEntity(systemModel);
			_model = new ModelEntity();
			_model.setModelId(systemModel.getModelId());
		}
		// 插入应用管理模块
		ModelEntity websiteModel = new ModelEntity();
		websiteModel.setModelIcon("");
		websiteModel.setModelCode("00000002");
		websiteModel.setModelModelId(_model.getModelId());
		websiteModel.setModelTitle("应用管理");
		websiteModel.setModelUrl("/website/index.do");
		websiteModel.setModelIsMenu(1);
		websiteModel.setModelParentIds(systemModel.getModelId() + "");
		websiteModel.setModelDatetime(new Timestamp(System.currentTimeMillis()));
		ModelEntity _websiteModel = modelBiz.getEntityByModelCode("00000002");
		if(_websiteModel == null){
			modelBiz.saveEntity(websiteModel);
		}
		// 插入模块管理模块
		ModelEntity modelEntity = new ModelEntity();
		modelEntity.setModelIcon("");
		modelEntity.setModelCode("00000001");
		modelEntity.setModelModelId(_model.getModelId());
		modelEntity.setModelTitle("模块管理");
		modelEntity.setModelIsMenu(1);
		modelEntity.setModelParentIds(systemModel.getModelId() + "");
		modelEntity.setModelDatetime(new Timestamp(System.currentTimeMillis()));
		modelEntity.setModelUrl("model/index.do");
		ModelEntity _modelEntity = modelBiz.getEntityByModelCode("00000001");
		List list = new ArrayList();
		if(_modelEntity == null){
			modelBiz.saveEntity(modelEntity);
			// 角色的权限
			
			RoleModelEntity roleMode = new RoleModelEntity();
			roleMode.setModelId(systemModel.getModelId());
			roleMode.setRoleId(1);
			list.add(roleMode);
			RoleModelEntity _roleMode = new RoleModelEntity();
			_roleMode.setModelId(websiteModel.getModelId());
			_roleMode.setRoleId(1);
			list.add(_roleMode);
			RoleModelEntity temRoleMode = new RoleModelEntity();
			temRoleMode.setModelId(modelEntity.getModelId());
			temRoleMode.setRoleId(1);
			list.add(temRoleMode);
		}
		//应用管理功能添加
		ModelEntity appModelEntity = modelBiz.getEntityByModelCode("00000002");
		int modelId = appModelEntity.getModelId();
		String modelParentIds = modelId+"";
		if(modelParentIds != null){
			modelParentIds = modelId + "," + appModelEntity.getModelParentIds();
		}
		//组织应用管理子功能的sql
		String functionSql = "INSERT INTO model (model_title,model_code,model_modelid,model_url,model_ismenu,model_parent_ids)VALUES('查看','00010002',"+modelId+",'app:view',0,'"+modelParentIds+"'),('新增','00020002',"+modelId+",'app:save',0,'"+modelParentIds+"'),('修改','00040002',"+modelId+",'app:update',0,'"+modelParentIds+"'),('删除','00030002',"+modelId+",'app:update',0,'"+modelParentIds+"')";
		modelBiz.excuteSql(functionSql);
		//应用管理子功能查看权限
		ModelEntity functionModel = modelBiz.getEntityByModelCode("00010002");
		RoleModelEntity roleModeView = new RoleModelEntity();
		roleModeView.setModelId(functionModel.getModelId());
		roleModeView.setRoleId(1);
		list.add(roleModeView);
		//模块管理功能添加
		ModelEntity tempModelEntity = modelBiz.getEntityByModelCode("00000001");
		int tempModelId = tempModelEntity.getModelId();
		String tempModelParentIds = tempModelId+"";
		if(tempModelParentIds != null){
			tempModelParentIds = tempModelId + "," + tempModelEntity.getModelParentIds();
		}
		//组织模块管理子功能的sql
		String tempFunctionSql = "INSERT INTO model (model_title,model_code,model_modelid,model_url,model_ismenu,model_parent_ids)VALUES('查看','00010001',"+tempModelId+",'temp:view',0,'"+tempModelParentIds+"'),('新增','00020001',"+tempModelId+",'temp:save',0,'"+tempModelParentIds+"'),('修改','00040001',"+tempModelId+",'temp:update',0,'"+tempModelParentIds+"'),('删除','00030001',"+tempModelId+",'temp:update',0,'"+tempModelParentIds+"')";
		modelBiz.excuteSql(tempFunctionSql);
		//模块管理子功能查看权限
		ModelEntity tempFunctionModel = modelBiz.getEntityByModelCode("00010001");
		RoleModelEntity tempRoleModeView = new RoleModelEntity();
		tempRoleModeView.setModelId(tempFunctionModel.getModelId());
		tempRoleModeView.setRoleId(1);
		list.add(tempRoleModeView);
		roleModelBiz.saveEntity(list);
		//提示成功
		this.outJson(response, "站群安装成功");
	}
}
