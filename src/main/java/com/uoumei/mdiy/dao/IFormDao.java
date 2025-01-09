package com.uoumei.mdiy.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.mdiy.entity.FormEntity;

/**
 * 自定义表单
 */
public interface IFormDao extends IBaseDao{
	/**
	 * 为自定义表单创建表
	 * @param table 表名
	 * @param fileds 字段集合
	 */
	void createDiyFormTable(@Param("table")String table,@Param("fileds")Map<Object,List> fileds);
	
}
