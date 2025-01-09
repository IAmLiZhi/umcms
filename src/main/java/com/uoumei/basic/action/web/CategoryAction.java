
package com.uoumei.basic.action.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.ICategoryBiz;
import com.uoumei.basic.constant.e.GlobalModelCodelEnum;
import com.uoumei.basic.entity.CategoryEntity;

import cn.hutool.core.util.ObjectUtil;
import com.uoumei.base.util.BasicUtil;

/**
 * 供前端查询分类信息使用
 * 
 */
@Controller("webCategory")
@RequestMapping("/category")
public class CategoryAction extends BaseAction{
	
	/**
	 * 分类业务处理层注入
	 */
	@Autowired
	private ICategoryBiz categoryBiz;
	
	/**
	 * 根据分类id查找其父分类实体,如果父分类不存在则返回该分类实体
	 * @param categoryId 分类ID
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 */
	@RequestMapping("/{categoryId}/getParentCategory")
	@ResponseBody
	public void getParentCategory(@PathVariable int categoryId,HttpServletRequest request, HttpServletResponse response){
		CategoryEntity category = (CategoryEntity)categoryBiz.getEntity(categoryId);
		if(ObjectUtil.isNotNull(category)){
			CategoryEntity paCategory = (CategoryEntity)categoryBiz.getEntity(category.getCategoryCategoryId());
			if(ObjectUtil.isNull(paCategory)){
				this.outJson(response, JSONObject.toJSONString(category));
			}else{
				this.outJson(response, JSONObject.toJSONString(paCategory));
			}
			
		}
	}
	
	/**
	 * 根据指定分类id查询其子分类
	 * @param categoryId 分类id
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 */
	@RequestMapping("/{categoryId}/queryChildren")
	public void queryChildren(@PathVariable int categoryId,HttpServletRequest request, HttpServletResponse response){
		CategoryEntity category = (CategoryEntity) this.categoryBiz.getEntity(categoryId);
		if(category!=null){
			List<CategoryEntity> list = this.categoryBiz.queryChilds(category);
			this.outJson(response, JSONObject.toJSONStringWithDateFormat(list,"yyyy-MM-dd HH:mm:ss"));
		}
	}
}