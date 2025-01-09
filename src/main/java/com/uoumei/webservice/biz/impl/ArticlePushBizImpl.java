package com.uoumei.webservice.biz.impl;


import org.springframework.beans.factory.annotation.Autowired;
import com.uoumei.cms.dao.IArticlePushDao;
import com.uoumei.cms.entity.ArticlePushEntity;
import com.uoumei.webservice.biz.IArticlePushBiz;

/**
 * webservice 文章推送
 */
public class ArticlePushBizImpl implements IArticlePushBiz {
	@Autowired
	IArticlePushDao articlePushDao;

	@Override
	public boolean add(ArticlePushEntity entity) {
		int id = articlePushDao.saveEntity(entity);
		if(id !=0 ){
			return true;
		}else{
			return false;
		}
		
	}
	
	
}
