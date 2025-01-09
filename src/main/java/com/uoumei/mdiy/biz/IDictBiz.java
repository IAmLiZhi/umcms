package com.uoumei.mdiy.biz;


import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.mdiy.entity.DictEntity;

/**
 * 字典表业务
 */
public interface IDictBiz extends IBaseBiz {
	/**
	 * 
	 * 根据字典类型和标签名或者实体
	 * @param dictType 类型
	 * @param dictLabel 标签名
	 * @return DictEntity 字典实体
	 */
	public DictEntity getByTypeAndLabel(String dictType,String dictLabel,int appId);
}