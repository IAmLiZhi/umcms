package com.uoumei.mdiy.biz;

import java.util.List;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.mdiy.entity.FormFieldEntity;

/**
 * 自定义表单接口
 */
public interface IFormFieldBiz extends IBaseBiz{
	
	/**
	 * 通过from的id获取实体
	 * @param diyFormId　自定义表单id 
	 * @return　返回实体
	 */
	List<FormFieldEntity> queryByDiyFormId( int diyFormId);
	
	/**
	 * 获取自定义表单字段
	 * @param diyFormFieldFormId　自定义表单id 
	 * @param diyFormFieldFieldName 　自定义表单字段名
	 * @return 返回自定义表单实体
	 */
	FormFieldEntity  getByFieldName(Integer diyFormFieldFormId,String diyFormFieldFieldName);
	
}
