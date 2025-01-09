
package com.uoumei.mdiy.biz.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.util.*;
import java.util.*;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.dao.IContentModelDao;

/**
 * 自定义模型表管理持久化层
 */
 @Service("contentModelBizImpl")
public class ContentModelBizImpl extends BaseBizImpl implements IContentModelBiz {

	
	@Autowired
	private IContentModelDao contentModelDao;
	
	
		@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return contentModelDao;
	} 
}