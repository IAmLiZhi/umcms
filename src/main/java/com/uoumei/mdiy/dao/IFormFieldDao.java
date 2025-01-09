package com.uoumei.mdiy.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.mdiy.entity.FormFieldEntity;

/**
 * 自定义表单字段
 */
public interface IFormFieldDao extends IBaseDao{

	/**
	 * 通过from的id获取实体
	 * @param diyFormId　自定义表单id 
	 * @return　返回实体
	 */
	List<FormFieldEntity> queryByDiyFormId(@Param("diyFormFieldFormId") int diyFormId);
	
	/**
	 * 获取自定义表单字段
	 * @param diyFormId　自定义表单id 
	 * @param diyFormFieldFieldName 　自定义表单字段名
	 * @return 返回自定义表单实体
	 */
	FormFieldEntity getByFieldName(@Param("diyFormFieldFormId") Integer diyFormId,@Param("diyFormFieldFieldName") String  diyFormFieldFieldName);
}
