
package com.uoumei.basic.biz.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IRoleBiz;
import com.uoumei.basic.dao.IRoleDao;
import com.uoumei.basic.entity.RoleEntity;
import com.uoumei.util.PageUtil;

/**
 * 角色业务层接口实现类
 */
@Service("roleBiz")
public class RoleBizImpl extends BaseBizImpl implements IRoleBiz {
	
	/**
	 * 注入角色持久化层
	 */
	@Autowired
	private IRoleDao roleDao;

	/**
	 * 获取角色持久化层
	 * @return roleDao 返回角色持久化层
	 */
	@Override
	public IBaseDao getDao() {
		return roleDao;
	}
	
}