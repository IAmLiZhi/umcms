
package com.uoumei.mdiy.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.util.*;
import java.util.*;
import com.uoumei.mdiy.entity.PageEntity;
import com.uoumei.mdiy.biz.IPageBiz;
import com.uoumei.mdiy.dao.IPageDao;

/**
 * 自定义页面表管理持久化层
 */
 @Service("pageBizImpl")
public class PageBizImpl extends BaseBizImpl implements IPageBiz {

	
	@Autowired
	private IPageDao pageDao;
	
	
		@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return pageDao;
	} 
}