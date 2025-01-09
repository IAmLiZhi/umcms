package com.uoumei.basic.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.biz.IRoleBiz;
import com.uoumei.basic.biz.IRoleColumnBiz;
import com.uoumei.basic.biz.IRoleModelBiz;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.entity.RoleColumnEntity;
import com.uoumei.basic.entity.RoleEntity;
import com.uoumei.basic.entity.RoleModelEntity;
import com.uoumei.base.util.JSONObject;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.bean.ListBean;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.base.util.bean.EUListBean;
	
/**
 * 角色管理控制层
 * 
 */
@Controller
@RequestMapping("/${managerPath}/basic/role")
public class RoleAction extends com.uoumei.basic.action.BaseAction{
	
	/**
	 * 注入角色业务层
	 */	
	@Autowired
	private IRoleBiz roleBiz;
	/**
	 * 模块业务层
	 */
	@Autowired
	private IModelBiz modelBiz;
	/**
	 * 角色模块关联业务层
	 */
	@Autowired
	private IRoleModelBiz roleModelBiz;
	
	/**
	 * 角色模块关联业务层
	 */
	@Autowired
	private IRoleColumnBiz roleColumnBiz;
	
	@Autowired
	private IColumnBiz columnBiz;
	
	/**
	 * 返回主界面index
	 */
	@RequestMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return view ("/basic/role/index");
	}
	
	/**
	 * 查询角色列表
	 * @param role 角色实体
	 * <i>role参数包含字段信息参考：</i><br/>
	 * roleId 角色ID，自增长<br/>
	 * roleName 角色名<br/>
	 * roleManagerid 角色管理员编号<br/>
	 * appId 角色APPID<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * roleId: 角色ID，自增长<br/>
	 * roleName: 角色名<br/>
	 * roleManagerid: 角色管理员编号<br/>
	 * appId: 角色APPID<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute RoleEntity role,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		ManagerSessionEntity managerSession = getManagerBySession(request);
		role.setRoleManagerId(managerSession.getManagerId());
		//--zzq
		role.setAppId(managerSession.getBasicId());
		BasicUtil.startPage();
		List roleList = roleBiz.query(role);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(new EUListBean(roleList,(int)BasicUtil.endPage(roleList).getTotal()),new DoubleValueFilter(),new DateValueFilter()));
	}
	
	@RequestMapping("/again/list")
	@ResponseBody
	public void againList(@ModelAttribute RoleEntity role,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		ManagerSessionEntity managerSession = getManagerBySession(request);
		role.setRoleManagerId(managerSession.getManagerId());
		//--zzq
		role.setAppId(managerSession.getBasicId());
		List roleList = roleBiz.query(role);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(roleList));
	}
	
	@RequestMapping("/{roleId}/queryByRole")
	@ResponseBody
	public void queryByRole(@PathVariable int roleId, HttpServletResponse response){
		List models = modelBiz.queryModelByRoleId(roleId);
		this.outJson(response, JSONObject.toJSONString(models));
	}
	@RequestMapping("/{roleId}/queryColumnByRole")
	@ResponseBody
	public void queryColumnByRole(@PathVariable int roleId, HttpServletResponse response){
		List<RoleColumnEntity> rcList = roleColumnBiz.queryByRoleId(roleId);
		List<Integer> columnIds = new ArrayList<Integer> ();
		for(RoleColumnEntity rc : rcList){
			columnIds.add(rc.getColumnId());
		}
		this.outJson(response, JSONObject.toJSONString(columnIds));
	}
	/**
	 * 返回编辑界面role_form
	 */
	@RequestMapping("/form")
	public String form(@ModelAttribute RoleEntity role,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);
		int appId = managerSession.getBasicId();

		List<ColumnEntity> list = columnBiz.queryAll(appId, this.getModelCodeId(request));
		model.addAttribute("listColumn", JSONArray.toJSONString(list));
		
		if(role.getRoleId() > 0){
			BaseEntity roleEntity = roleBiz.getEntity(role.getRoleId());			
			model.addAttribute("roleEntity",roleEntity);
		}

		return view ("/basic/role/form");
	}
	
	/**
	 * 获取角色
	 * @param role 角色实体
	 * <i>role参数包含字段信息参考：</i><br/>
	 * roleId 角色ID，自增长<br/>
	 * roleName 角色名<br/>
	 * roleManagerid 角色管理员编号<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * roleId: 角色ID，自增长<br/>
	 * roleName: 角色名<br/>
	 * roleManagerid: 角色管理员编号<br/>
	 * appId: 角色APPID<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute RoleEntity role,HttpServletResponse response, HttpServletRequest request,ModelMap model){
		if(role.getRoleId()<=0) {
			this.outJson(response, null, false, getResString("err.error", this.getResString("role.id")));
			return;
		}
		RoleEntity _role = (RoleEntity)roleBiz.getEntity(role.getRoleId());
		this.outJson(response, _role);
	}
	
	/**
	 * 保存角色实体
	 * @param role 角色实体
	 * <i>role参数包含字段信息参考：</i><br/>
	 * roleId 角色ID，自增长<br/>
	 * roleName 角色名<br/>
	 * roleManagerid 角色管理员编号<br/>
	 * appId 角色APPID<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * roleId: 角色ID，自增长<br/>
	 * roleName: 角色名<br/>
	 * roleManagerid: 角色管理员编号<br/>
	 * appId: 角色APPID<br/>
	 * }</dd><br/>
	 */
	@PostMapping("/saveOrUpdateRole")
	@ResponseBody
	@RequiresPermissions("role:save")
	public void saveOrUpdateRole(@ModelAttribute RoleEntity role,
			@RequestParam(value="ids[]",required=false) List<Integer> ids, 
			@RequestParam(value="columnIds[]",required=false) List<Integer> columnIds, 
			HttpServletResponse response, HttpServletRequest request) {
		
		if(StringUtils.isEmpty(role.getRoleName())){
			this.outJson(response, ModelCode.ROLE, false, getResString("err.empty", this.getResString("rolrName")));	
			return;
		}
		//给角色添加APPID
		//获取管理员id --zzq
		ManagerSessionEntity managerSession = getManagerBySession(request);
		role.setAppId(managerSession.getBasicId());
		role.setRoleManagerId(managerSession.getManagerId());
		
		//组织角色属性
		RoleEntity _role = new RoleEntity();
		_role.setRoleName(role.getRoleName());
		_role.setAppId(managerSession.getBasicId());
		RoleEntity _roleEntity = (RoleEntity) roleBiz.getEntity(_role);

		//通过角色id判断是保存还是修改
		if(role.getRoleId()>0){
			//若为更新角色，数据库中存在该角色名称且当前名称不为更改前的名称，则属于重名
			if(_roleEntity != null && !role.getRoleName().equals(BasicUtil.getString("oldRoleName"))){
				this.outJson(response, ModelCode.ROLE, false, getResString("roleName.exist"));	
				return;
			}
			roleBiz.updateEntity(role);
		}else{
			//判断角色名是否重复
			if(_roleEntity != null){
				this.outJson(response, ModelCode.ROLE, false, getResString("roleName.exist"));	
				return;
			}
			//获取管理员id
			roleBiz.saveEntity(role);
		}
		//开始保存相应的关联数据。组织角色模块的列表。
		List<RoleModelEntity> roleModelList = new ArrayList<>();
		if(ids != null){
			for(Integer id : ids){
				RoleModelEntity roleModel = new RoleModelEntity();
				roleModel.setRoleId(role.getRoleId());
				roleModel.setModelId(id);
				roleModelList.add(roleModel);
			}
			//先删除当前的角色关联菜单，然后重新添加。
			roleModelBiz.deleteEntity(role.getRoleId());
			roleModelBiz.saveEntity(roleModelList);
		}else{
			roleModelBiz.deleteEntity(role.getRoleId());
		}
		
		//开始保存相应的关联数据。组织角色栏目的列表。
		List<RoleColumnEntity> roleColumnList = new ArrayList<>();
		if(columnIds != null){
			for(Integer columnId : columnIds){
				RoleColumnEntity roleColumn = new RoleColumnEntity();
				roleColumn.setRoleId(role.getRoleId());
				roleColumn.setColumnId(columnId);
				roleColumnList.add(roleColumn);
			}
			//先删除当前的角色关联菜单，然后重新添加。
			roleColumnBiz.deleteEntity(role.getRoleId());
			roleColumnBiz.saveEntity(roleColumnList);
		}else{
			roleColumnBiz.deleteEntity(role.getRoleId());
		}
		
		this.outJson(response, JSONObject.toJSONString(role));
	}
	
	/**
	 * @param role 角色实体
	 * <i>role参数包含字段信息参考：</i><br/>
	 * roleId:多个roleId直接用逗号隔开,例如roleId=1,2,3,4
	 * 批量删除角色
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("role:del")
	public void delete(@RequestBody List<RoleEntity> roles,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[roles.size()];
		ManagerSessionEntity managerSession = this.getManagerBySession(request);
		int currentRoleId = managerSession.getManagerRoleID();
		for(int i = 0;i<roles.size();i++){
			if(currentRoleId == roles.get(i).getRoleId()){
				//当前角色不能删除
				continue ;
			}
			ids[i] = roles.get(i).getRoleId();
		}
		roleBiz.delete(ids);		
		this.outJson(response, true);
	}
}