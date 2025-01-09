package com.uoumei.mdiy.biz;
import java.util.List;
import java.util.Map;

import com.uoumei.basic.biz.IBasicBiz;
import com.uoumei.mdiy.entity.SearchEntity;
import com.uoumei.util.PageUtil;

/**
 * 搜索业务层，继承IBasicBiz
 */
public interface ISearchBiz extends IBasicBiz{
	
	/**
	 * 根据id查询对应站点的搜索
	 * @param searchId 搜索记录编号
	 * @return
	 */
	//SearchEntity getById(int searchId);
}
