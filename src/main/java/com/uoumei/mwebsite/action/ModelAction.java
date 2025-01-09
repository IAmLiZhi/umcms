package com.uoumei.mwebsite.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.uoumei.base.util.JSONArray;
import com.uoumei.base.util.JSONObject;

import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.entity.BaseEntity;

import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.bean.ListBean;

import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.biz.IRoleModelBiz;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.entity.ModelEntity;
import com.uoumei.basic.entity.RoleModelEntity;

import com.uoumei.base.util.bean.EUListBean;
	
/**
 * 模块表管理控制层
 */
@Controller("mweModelAction")
@RequestMapping("/${managerPath}/mwebsite/model")
public class ModelAction extends com.uoumei.mwebsite.action.BaseAction{
	
	/**
	 * 注入模块表业务层
	 */	
	@Autowired
	private IModelBiz modelBiz;
	/**
	 * 角色模块关联业务层
	 */
	@Autowired
	private IRoleModelBiz roleModelBiz;
	
	/**
	 * 返回主界面index
	 */
	@RequestMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request,ModelMap mode){
		List<BaseEntity> parentModelList = modelBiz.queryAll();
		mode.addAttribute("parentModelList", JSONArray.toJSONString(parentModelList));
		return view ("/mwebsite/model/index");
	}
	
	/**
	 * 查询所有模块，以json格式输出
	 * @param response 响应对象
	 */
	@RequestMapping("/query")
	public void query(HttpServletResponse response) {
		ModelEntity model = new ModelEntity();
		List<BaseEntity> modelList = modelBiz.query(model);
		this.outJson(response, JSONObject.toJSONString(modelList));
	}
	/**
	 * 查询模块表列表
	 * @param model 模块表实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelId 模块自增长id<br/>
	 * modelTitle 模块标题<br/>
	 * modelCode 模块编码<br/>
	 * modelModelid 模块的父模块id<br/>
	 * modelUrl 模块连接地址<br/>
	 * modelDatetime <br/>
	 * modelIcon 模块图标<br/>
	 * modelModelmanagerid 模块关联的关联员id<br/>
	 * modelSort 模块的排序<br/>
	 * modelIsmenu 模块是否是菜单<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * modelId: 模块自增长id<br/>
	 * modelTitle: 模块标题<br/>
	 * modelCode: 模块编码<br/>
	 * modelModelid: 模块的父模块id<br/>
	 * modelUrl: 模块连接地址<br/>
	 * modelDatetime: <br/>
	 * modelIcon: 模块图标<br/>
	 * modelModelmanagerid: 模块关联的关联员id<br/>
	 * modelSort: 模块的排序<br/>
	 * modelIsmenu: 模块是否是菜单<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		List modelList = modelBiz.query(modelEntity);
		EUListBean _list = new EUListBean(modelList, modelList.size());
		this.outJson(response,com.uoumei.base.util.JSONArray.toJSONString(_list));
	}
	
	/**
	 * 获取模块表
	 * @param model 模块表实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelId 模块自增长id<br/>
	 * modelTitle 模块标题<br/>
	 * modelCode 模块编码<br/>
	 * modelModelid 模块的父模块id<br/>
	 * modelUrl 模块连接地址<br/>
	 * modelDatetime <br/>
	 * modelIcon 模块图标<br/>
	 * modelModelmanagerid 模块关联的关联员id<br/>
	 * modelSort 模块的排序<br/>
	 * modelIsmenu 模块是否是菜单<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * modelId: 模块自增长id<br/>
	 * modelTitle: 模块标题<br/>
	 * modelCode: 模块编码<br/>
	 * modelModelid: 模块的父模块id<br/>
	 * modelUrl: 模块连接地址<br/>
	 * modelDatetime: <br/>
	 * modelIcon: 模块图标<br/>
	 * modelModelmanagerid: 模块关联的关联员id<br/>
	 * modelSort: 模块的排序<br/>
	 * modelIsmenu: 模块是否是菜单<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute ModelEntity modelEntity,HttpServletResponse response, HttpServletRequest request,ModelMap model){
		if(modelEntity.getModelId()<=0) {
			this.outJson(response, null, false, getResString("err.error", this.getResString("model.id")));
			return;
		}
		//根据父模块id查寻模块
		ModelEntity _model = (ModelEntity)modelBiz.getEntity(modelEntity.getModelId());
		if(_model != null){
			Map<String, ModelEntity> mode = new HashMap<String, ModelEntity>();
			ModelEntity parentModel = (ModelEntity) modelBiz.getEntity(_model.getModelModelId());
			mode.put("parentModel", parentModel);
			mode.put("model", _model);
			this.outJson(response, JSONObject.toJSONString(mode));
			return;
		}
		this.outJson(response, _model);
	}
	
	/**
	 * 保存模块表实体
	 * @param model 模块表实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelId 模块自增长id<br/>
	 * modelTitle 模块标题<br/>
	 * modelCode 模块编码<br/>
	 * modelModelid 模块的父模块id<br/>
	 * modelUrl 模块连接地址<br/>
	 * modelDatetime <br/>
	 * modelIcon 模块图标<br/>
	 * modelModelmanagerid 模块关联的关联员id<br/>
	 * modelSort 模块的排序<br/>
	 * modelIsmenu 模块是否是菜单<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * modelId: 模块自增长id<br/>
	 * modelTitle: 模块标题<br/>
	 * modelCode: 模块编码<br/>
	 * modelModelid: 模块的父模块id<br/>
	 * modelUrl: 模块连接地址<br/>
	 * modelDatetime: <br/>
	 * modelIcon: 模块图标<br/>
	 * modelModelmanagerid: 模块关联的关联员id<br/>
	 * modelSort: 模块的排序<br/>
	 * modelIsmenu: 模块是否是菜单<br/>
	 * }</dd><br/>
	 */
	@PostMapping("/save")
	@ResponseBody
	public void save(@ModelAttribute ModelEntity model, HttpServletResponse response, HttpServletRequest request) {
//		//获取管理员id并赋值给模块的id
//		model.setModelId(getManagerId(request));
//		// 获取模块保存时间
//		model.setModelDatetime(new Timestamp(System.currentTimeMillis()));
//		if (!StringUtil.checkLength(model.getModelTitle(), 1, 20) ) {
//			this.outJson(response,getResString("err.length",this.getResString("modelTitle"),"2","20"));
//			return;
//		}
//		if(!StringUtil.checkLength(model.getModelCode(), 1, 20)){
//			this.outJson(response,getResString("err.length",this.getResString("modelCode"),"2","20"));
//			return;
//		}
//		//判断图标是否为空，不为空去掉,图标地址中含有的“|”
//		//空值判断
//		if(!StringUtil.isBlank(model.getModelIcon())) {
//			model.setModelIcon( model.getModelIcon().replace("|", ""));
//		}
//		//添加父级id集合
//		ModelEntity parent = (ModelEntity) modelBiz.getEntity(model.getModelModelId());
//		String parentIds="";
//		if(parent.getModelParentIds() != null){
//			parentIds = parent.getModelParentIds()+","+model.getModelModelId();
//		}else{
//			parentIds = model.getModelModelId()+"";
//		}
//		model.setModelParentIds(parentIds);
//		modelBiz.saveEntity(model);
//		//返回模块id到页面
//		this.outJson(response, ModelCode.ROLE, true,String.valueOf(model.getModelId()));
		
		
		//模块标题验证
		if(StringUtil.isBlank(model.getModelTitle())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("model.title")));
			return;			
		}
		if(!StringUtil.checkLength(model.getModelTitle()+"", 1, 10)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("model.title"), "1", "10"));
			return;			
		}
		//模块编码验证
		if(StringUtil.isBlank(model.getModelCode())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("model.code")));
			return;			
		}
		if(!StringUtil.checkLength(model.getModelCode()+"", 7, 9)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("model.code"), "7", "9"));
			return;			
		}
		//获取管理员id并赋值给模块的id
		model.setModelId(getManagerId(request));
		// 获取模块保存时间
		model.setModelDatetime(new Timestamp(System.currentTimeMillis()));
		//判断图标是否为空，不为空去掉,图标地址中含有的“|”
		//空值判断
		if(!StringUtil.isBlank(model.getModelIcon())) {
			model.setModelIcon( model.getModelIcon().replace("|", ""));
		}
		//添加父级id集合
		ModelEntity parent = (ModelEntity) modelBiz.getEntity(model.getModelModelId());
		String parentIds="";
		if(parent != null){
			if(parent.getModelParentIds() != null){
				parentIds = parent.getModelParentIds()+","+model.getModelModelId();
			}else{
				parentIds = model.getModelModelId()+"";
			}
		}
		//三级栏目不能为菜单
		if(parentIds.split(",").length>1 && model.getModelIsMenu() == 1){
			this.outJson(response, null,false,this.getResString("model.is.not.menu"));
			return;
		}
		model.setModelParentIds(parentIds);
		modelBiz.saveEntity(model);
		//保存成功后给当前管理就就加上对应的权限
		if(model.getModelId() > 0){
			ManagerSessionEntity managerSession = this.getManagerBySession(request);
			List<RoleModelEntity> roleModels = new ArrayList<>(); 
			RoleModelEntity rolemodel = new RoleModelEntity();
			rolemodel.setModelId(model.getModelId());
			rolemodel.setRoleId(managerSession.getManagerRoleID());
			roleModels.add(rolemodel);
			roleModelBiz.saveEntity(roleModels);
		}
		//返回模块id到页面
		this.outJson(response, ModelCode.ROLE, true,String.valueOf(model.getModelId()));
	}
	
	/**
	 * @param model 模块表实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelId:多个modelId直接用逗号隔开,例如modelId=1,2,3,4
	 * 批量删除模块表
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(HttpServletResponse response, HttpServletRequest request) {
		int[] ids = BasicUtil.getInts("ids", ",");
		modelBiz.delete(ids);		
		this.outJson(response, true);
	}
	
	/** 
	 * 更新模块表信息模块表
	 * @param model 模块表实体
	 * <i>model参数包含字段信息参考：</i><br/>
	 * modelId 模块自增长id<br/>
	 * modelTitle 模块标题<br/>
	 * modelCode 模块编码<br/>
	 * modelModelid 模块的父模块id<br/>
	 * modelUrl 模块连接地址<br/>
	 * modelDatetime <br/>
	 * modelIcon 模块图标<br/>
	 * modelModelmanagerid 模块关联的关联员id<br/>
	 * modelSort 模块的排序<br/>
	 * modelIsmenu 模块是否是菜单<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * modelId: 模块自增长id<br/>
	 * modelTitle: 模块标题<br/>
	 * modelCode: 模块编码<br/>
	 * modelModelid: 模块的父模块id<br/>
	 * modelUrl: 模块连接地址<br/>
	 * modelDatetime: <br/>
	 * modelIcon: 模块图标<br/>
	 * modelModelmanagerid: 模块关联的关联员id<br/>
	 * modelSort: 模块的排序<br/>
	 * modelIsmenu: 模块是否是菜单<br/>
	 * }</dd><br/>
	 */
	@PostMapping("/update")
	@ResponseBody	 
	public void update(@ModelAttribute ModelEntity model, HttpServletResponse response,
			HttpServletRequest request) {
		
		//模块标题验证
		if(StringUtil.isBlank(model.getModelTitle())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("model.title")));
			return;			
		}
		if(!StringUtil.checkLength(model.getModelTitle()+"", 1, 10)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("model.title"), "1", "10"));
			return;			
		}
		//模块编码验证
		if(StringUtil.isBlank(model.getModelCode())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("model.code")));
			return;			
		}
		if(!StringUtil.checkLength(model.getModelCode()+"", 7, 9)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("model.code"), "7", "9"));
			return;			
		}
		//判断当前修改的菜单是否是三级菜单
		ModelEntity _model = (ModelEntity) modelBiz.getEntity(model.getModelId());
		if(_model.getModelIsMenu() == 1 && model.getModelIsMenu() == 0){
			this.outJson(response, null,false,this.getResString("model.is.menu"));
			return;	
		}
		//判断图标是否为空，不为空去掉,图标地址中含有的“|”
		//空值判断
		if(!StringUtil.isBlank(model.getModelIcon())) {
			model.setModelIcon( model.getModelIcon().replace("|", ""));
		}
		//添加父级id集合
		ModelEntity parent = (ModelEntity) modelBiz.getEntity(model.getModelModelId());
		
		
		String parentIds="";
		if(parent != null){
			if(parent.getModelParentIds() != null){
				parentIds = parent.getModelParentIds()+","+model.getModelModelId();
			}else{
				parentIds = model.getModelModelId()+"";
			}
		}
		//三级栏目不能为菜单
		if(parentIds.split(",").length>1 && model.getModelIsMenu() == 1){
			this.outJson(response, null,false,this.getResString("model.is.not.menu"));
			return;
		}
		model.setModelParentIds(parentIds);
		modelBiz.updateEntity(model);		
		this.outJson(response, ModelCode.ROLE, true,String.valueOf(model.getModelId()));

		
		
		
		
/*		
		if (!StringUtil.checkLength(model.getModelTitle(), 2, 20)
				|| !StringUtil.checkLength(model.getModelCode(), 2, 20)) {
			return;
		} 
		//判断图标是否为空，不为空去掉,图标地址中含有的“|”
		//空值判断
		if(!StringUtil.isBlank(model.getModelIcon())) {
			model.setModelIcon( model.getModelIcon().replace("|", ""));
		}
		//添加父级id集合
		ModelEntity parent = (ModelEntity) modelBiz.getEntity(model.getModelModelId());
		String parentIds="";
		if(parent.getModelParentIds() != null){
			parentIds = parent.getModelParentIds()+","+model.getModelModelId();
		}else{
			parentIds = model.getModelModelId()+"";
		}
		model.setModelParentIds(parentIds);
		modelBiz.updateEntity(model);		
		this.outJson(response, ModelCode.ROLE, true,String.valueOf(model.getModelId()));
	*/
	}
		
}