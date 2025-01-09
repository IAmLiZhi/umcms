package com.uoumei.webservice.biz;

import javax.jws.WebService;

import com.uoumei.cms.entity.ArticlePushEntity;


@WebService
public interface IArticlePushBiz {
	public boolean add(ArticlePushEntity entity);
}
