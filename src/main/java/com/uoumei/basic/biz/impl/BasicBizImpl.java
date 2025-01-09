
package com.uoumei.basic.biz.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.biz.IBasicBiz;
import com.uoumei.basic.dao.IBasicDao;
import com.uoumei.basic.entity.BasicEntity;
import com.uoumei.util.PageUtil;

import com.uoumei.base.util.BasicUtil;

/**
 * 基本信息的业务层实现类
 */
@Service("basicBiz")
public  class BasicBizImpl extends BaseBizImpl implements IBasicBiz {
	
	/**
	 * 注入基本信息持久化层
	 */
	@Autowired
	private IBasicDao basicDao;

    
    @Override
    public void deleteBasic(int basicId) {
        basicDao.deleteEntity(basicId);
        deleteEntity(basicId);
    }
    
    @Override
	public void deleteBasic(int[] basicIds) {
		// TODO Auto-generated method stub
    	basicDao.delete(basicIds);
		delete(basicIds);
	}
    
    @Override
    public BasicEntity getBasic(int basicId){
    	return (BasicEntity) basicDao.getEntity(basicId);
    }
    
    /**
	 * 获取基本信息持久化层
	 * @return basicDao 返回基本信息持久化层
	 */
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return basicDao;
	}


	@Override
	public List<BasicEntity> query(int categoryId) {
		// TODO Auto-generated method stub
		return basicDao.query(null,categoryId, null, null, null, null, null,null,null);
	}


	@Override
	public List<BasicEntity> query(Integer appId,Integer categoryId, String keyWord, PageUtil page,Integer modelId,Map where) {
		// TODO Auto-generated method stub
		if (page==null) {
			return basicDao.query(appId,categoryId, keyWord, null, null, null, null,modelId,where);
		}
		return basicDao.query(appId,categoryId, keyWord, page.getPageSize()*page.getPageNo(), page.getPageSize(), null, null,modelId,where);
	}

	@Override
    public int saveBasic(BasicEntity basic) {
		//--zzq
        basicDao.saveEntity(basic);
        return saveEntity(basic);
    }

	@Override
    public void updateBasic(BasicEntity basic) {
		//--zzq
        basicDao.updateEntity(basic);
        updateEntity(basic);
    }

	@Override
	public void updateHit(int basicId, Integer num) {
		// TODO Auto-generated method stub
		  basicDao.updateHit(basicId, num);
	}

    
}