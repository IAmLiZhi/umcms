package com.uoumei.basic.biz;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.entity.BasicLogEntity;
import com.uoumei.util.PageUtil;

public interface IBasicLogBiz extends IBaseBiz {
	/**
	 * 分页查询
	 * @return 返回list数组
	 */
	List<BaseEntity> query( BasicLogEntity basicLog,PageUtil page,String orderBy,boolean order);
	
	/**
	 * 查询数据表中记录集合总数
	 * @return 返回查询总条数
	 */
	int count( BasicLogEntity basicLog);
}
