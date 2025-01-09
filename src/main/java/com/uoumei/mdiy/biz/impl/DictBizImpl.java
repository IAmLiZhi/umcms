
package com.uoumei.mdiy.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.mdiy.biz.IDictBiz;
import com.uoumei.mdiy.dao.IDictDao;
import com.uoumei.mdiy.entity.DictEntity;

import com.uoumei.base.util.BasicUtil;


/**
 * 字典表管理持久化层
 */
 @Service("dictBizImpl")
public class DictBizImpl extends BaseBizImpl implements IDictBiz {

	
	@Autowired
	private IDictDao dictDao;
	
	
		@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return dictDao;
	}

		@Override
		public DictEntity getByTypeAndLabel(String dictType, String dictLabel, int appId) {
			DictEntity dict = new DictEntity();
			dict.setDictType(dictType);
			dict.setDictLabel(dictLabel);
			//--zzq
			dict.setAppId(appId);
			return (DictEntity) dictDao.getByEntity(dict);
		} 
}