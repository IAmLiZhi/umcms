package com.uoumei.mdiy.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uoumei.mdiy.biz.IDictBiz;
import com.uoumei.mdiy.entity.DictEntity;

import com.uoumei.base.util.JSONObject;

import com.uoumei.base.entity.BaseEntity;

import com.uoumei.base.util.BasicUtil;

import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.basic.entity.ManagerSessionEntity;

import com.uoumei.base.util.bean.EUListBean;
	
/**
 * 字典表管理控制层
 */
@Controller
@RequestMapping("/${managerPath}/mdiy/dict")
public class DictAction extends com.uoumei.mdiy.action.BaseAction{
	
	/**
	 * 注入字典表业务层
	 */	
	@Autowired
	private IDictBiz dictBiz;
	
	/**
	 * 返回主界面index
	 */
	@RequestMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return view ("/mdiy/dict/index");
	}
	
	/**
	 * 查询字典表列表
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId 编号<br/>
	 * dictAppId 应用编号<br/>
	 * dictValue 数据值<br/>
	 * dictLabel 标签名<br/>
	 * dictType 类型<br/>
	 * dictDescription 描述<br/>
	 * dictSort 排序（升序）<br/>
	 * dictParentId 父级编号<br/>
	 * createBy 创建者<br/>
	 * createDate 创建时间<br/>
	 * updateBy 更新者<br/>
	 * updateDate 更新时间<br/>
	 * dictRemarks 备注信息<br/>
	 * del 删除标记<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * dictId: 编号<br/>
	 * dictAppId: 应用编号<br/>
	 * dictValue: 数据值<br/>
	 * dictLabel: 标签名<br/>
	 * dictType: 类型<br/>
	 * dictDescription: 描述<br/>
	 * dictSort: 排序（升序）<br/>
	 * dictParentId: 父级编号<br/>
	 * createBy: 创建者<br/>
	 * createDate: 创建时间<br/>
	 * updateBy: 更新者<br/>
	 * updateDate: 更新时间<br/>
	 * dictRemarks: 备注信息<br/>
	 * del: 删除标记<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute DictEntity dict,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		dict.setAppId(managerSession.getBasicId());
		BasicUtil.startPage();
		List dictList = dictBiz.query(dict);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(new EUListBean(dictList,(int)BasicUtil.endPage(dictList).getTotal()),new DoubleValueFilter(),new DateValueFilter()));
	}
	
	/**
	 * 返回编辑界面dict_form
	 */
	@RequestMapping("/form")
	public String form(@ModelAttribute DictEntity dict,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(dict.getDictId() != null){
			BaseEntity dictEntity = dictBiz.getEntity(dict.getDictId());			
			model.addAttribute("dictEntity",dictEntity);
		}
		
		return view ("/mdiy/dict/form");
	}
	
	/**
	 * 获取字典表
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId 编号<br/>
	 * dictAppId 应用编号<br/>
	 * dictValue 数据值<br/>
	 * dictLabel 标签名<br/>
	 * dictType 类型<br/>
	 * dictDescription 描述<br/>
	 * dictSort 排序（升序）<br/>
	 * dictParentId 父级编号<br/>
	 * createBy 创建者<br/>
	 * createDate 创建时间<br/>
	 * updateBy 更新者<br/>
	 * updateDate 更新时间<br/>
	 * dictRemarks 备注信息<br/>
	 * del 删除标记<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * dictId: 编号<br/>
	 * dictAppId: 应用编号<br/>
	 * dictValue: 数据值<br/>
	 * dictLabel: 标签名<br/>
	 * dictType: 类型<br/>
	 * dictDescription: 描述<br/>
	 * dictSort: 排序（升序）<br/>
	 * dictParentId: 父级编号<br/>
	 * createBy: 创建者<br/>
	 * createDate: 创建时间<br/>
	 * updateBy: 更新者<br/>
	 * updateDate: 更新时间<br/>
	 * dictRemarks: 备注信息<br/>
	 * del: 删除标记<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute DictEntity dict,HttpServletResponse response, HttpServletRequest request,ModelMap model){
		if(dict.getDictId()<=0) {
			this.outJson(response, null, false, getResString("err.error", this.getResString("dict.id")));
			return;
		}
		DictEntity _dict = (DictEntity)dictBiz.getEntity(dict.getDictId());
		this.outJson(response, _dict);
	}
	
	/**
	 * 保存字典表实体
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId 编号<br/>
	 * dictAppId 应用编号<br/>
	 * dictValue 数据值<br/>
	 * dictLabel 标签名<br/>
	 * dictType 类型<br/>
	 * dictDescription 描述<br/>
	 * dictSort 排序（升序）<br/>
	 * dictParentId 父级编号<br/>
	 * createBy 创建者<br/>
	 * createDate 创建时间<br/>
	 * updateBy 更新者<br/>
	 * updateDate 更新时间<br/>
	 * dictRemarks 备注信息<br/>
	 * del 删除标记<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * dictId: 编号<br/>
	 * dictAppId: 应用编号<br/>
	 * dictValue: 数据值<br/>
	 * dictLabel: 标签名<br/>
	 * dictType: 类型<br/>
	 * dictDescription: 描述<br/>
	 * dictSort: 排序（升序）<br/>
	 * dictParentId: 父级编号<br/>
	 * createBy: 创建者<br/>
	 * createDate: 创建时间<br/>
	 * updateBy: 更新者<br/>
	 * updateDate: 更新时间<br/>
	 * dictRemarks: 备注信息<br/>
	 * del: 删除标记<br/>
	 * }</dd><br/>
	 */
	@PostMapping("/save")
	@ResponseBody
	@RequiresPermissions("mdiy:dict:save")
	public void save(@ModelAttribute DictEntity dict, HttpServletResponse response, HttpServletRequest request) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		dict.setAppId(managerSession.getBasicId());
		if(dictBiz.getByTypeAndLabel(dict.getDictType(),dict.getDictLabel(),managerSession.getBasicId())!=null){
			this.outJson(response, null, false, getResString("diy.dict.type.and.label.repeat"));
			return;
		}
		dictBiz.saveEntity(dict);
		this.outJson(response, true);
	}
	
	/**
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId:多个dictId直接用逗号隔开,例如dictId=1,2,3,4
	 * 批量删除字典表
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mdiy:dict:del")
	public void delete(@RequestBody List<DictEntity> dicts,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[dicts.size()];
		for(int i = 0;i<dicts.size();i++){
			ids[i] = dicts.get(i).getDictId();
		}
		dictBiz.delete(ids);		
		this.outJson(response, true);
	}
	
	/** 
	 * 更新字典表信息字典表
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId 编号<br/>
	 * dictAppId 应用编号<br/>
	 * dictValue 数据值<br/>
	 * dictLabel 标签名<br/>
	 * dictType 类型<br/>
	 * dictDescription 描述<br/>
	 * dictSort 排序（升序）<br/>
	 * dictParentId 父级编号<br/>
	 * createBy 创建者<br/>
	 * createDate 创建时间<br/>
	 * updateBy 更新者<br/>
	 * updateDate 更新时间<br/>
	 * dictRemarks 备注信息<br/>
	 * del 删除标记<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * dictId: 编号<br/>
	 * dictAppId: 应用编号<br/>
	 * dictValue: 数据值<br/>
	 * dictLabel: 标签名<br/>
	 * dictType: 类型<br/>
	 * dictDescription: 描述<br/>
	 * dictSort: 排序（升序）<br/>
	 * dictParentId: 父级编号<br/>
	 * createBy: 创建者<br/>
	 * createDate: 创建时间<br/>
	 * updateBy: 更新者<br/>
	 * updateDate: 更新时间<br/>
	 * dictRemarks: 备注信息<br/>
	 * del: 删除标记<br/>
	 * }</dd><br/>
	 */
	@PostMapping("/update")
	@ResponseBody
	@RequiresPermissions("mdiy:dict:update")
	public void update(@ModelAttribute DictEntity dict, HttpServletResponse response,
			HttpServletRequest request) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		DictEntity _dict = dictBiz.getByTypeAndLabel(dict.getDictType(),dict.getDictLabel(),managerSession.getBasicId());
		if(_dict!=null){
			if(!_dict.getDictId().equals(dict.getDictId())){
				this.outJson(response, null, false, getResString("diy.dict.type.and.label.repeat"));
				return;
			}
		}
		dictBiz.updateEntity(dict);
		this.outJson(response, true);
	}
		
}