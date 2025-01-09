
package com.uoumei.basic.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.entity.BasicEntity;

/**
 * 基本信息的数据层(接口)
 */
public interface IBasicDao extends IBaseDao {

	/**
	 * 更新点击次数
	 * 
	 * @param basicId 文章编号
	 * @param num null:为递增
	 */
	void updateHit(@Param("basicId") int basicId, @Param("num") Integer num);


	/**
	 * 根据分类与关键子统计总数
	 * @param appId 站点ID
	 * @param categoryId
	 *            　分类编号
	 * @param keyWord
	 *            　关键字
	 * @param begin 开始
	 * @param end　结束
	 * @param orderField　排序字段
	 * @param ad　排序方式true:升序 false:倒序
	 * @param modelId 模块ID
	 * @param where 查询条件
	 * @return　返回列表实体
	 */
	List<BasicEntity> query(@Param("appId") Integer appId,@Param("categoryId") Integer categoryId, @Param("keyWord") String keyWord, @Param("begin") Integer begin, @Param("end") Integer end, @Param("orderField")String orderField, @Param("ad")Boolean ad, @Param("modelId")Integer modelId,@Param("where")Map where);

	
	

}