
package com.uoumei.cms.action.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.action.BaseAction;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.biz.IContentModelFieldBiz;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.mdiy.entity.ContentModelFieldEntity;

/**
 * 
 * 供前端页面获取自定义模型中字段实体信息
 */
@Controller("webField")
@RequestMapping("/field")
public class FieldAction extends BaseAction{
	
	/**
	 * 栏目业务层
	 */
	@Autowired
	private IColumnBiz columnBiz;
	
	/**
	 * 内容模型业务层
	 */
	@Autowired
	private IContentModelBiz contentModelBiz;
	
	/**
	 * 字段管理业务层
	 */
	@Autowired
	private IContentModelFieldBiz fieldBiz;
	
	/**
	 * 
	 * 根据当前栏目id和字段名称获取自定义模型中的字段实体信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/{columId}/getEntity")
	@ResponseBody
	public void getEntity(@PathVariable int columId,HttpServletRequest request, HttpServletResponse response) {
		//获取字段名称
		String fieldFieldName = request.getParameter("fieldFieldName");
		//根据栏目id获取栏目实体
		ColumnEntity column = (ColumnEntity) this.columnBiz.getEntity(columId);
		if(column==null){
			this.outJson(response, this.getResString("err"));
			return;
		}else{
			//判断该栏目下是存在内容模型
			if(column.getColumnContentModelId()>0){
				//获取当前栏目对应的内容模型
				ContentModelEntity contentModel = (ContentModelEntity) this.contentModelBiz.getEntity(column.getColumnContentModelId());
				if(contentModel==null){
					this.outJson(response, this.getResString("err"));
					return;
				}
				//获取字段实体
				ContentModelFieldEntity field = fieldBiz.getEntityByCmId(column.getColumnContentModelId(), fieldFieldName);
				//返回字段实体
				this.outJson(response, JSONObject.toJSONString(field));
			}
		}
		
	}
}