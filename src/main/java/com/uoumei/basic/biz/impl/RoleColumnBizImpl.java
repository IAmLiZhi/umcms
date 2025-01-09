
package com.uoumei.basic.biz.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.biz.IRoleColumnBiz;
import com.uoumei.basic.dao.IRoleColumnDao;
import com.uoumei.basic.entity.RoleColumnEntity;


/**
 * 角色栏目关联业务层接口实现类
 */
@Service("roleColumnBiz")
public class RoleColumnBizImpl extends BaseBizImpl implements IRoleColumnBiz {
	
	/**
	 * 角色栏目关联持久化层
	 */
	@Autowired
	private IRoleColumnDao roleColumnDao;

	/**
	 * 获取角色栏目持久化层
	 * @return roleColumnDao 返回角色栏目持久化层
	 */
	@Override
	public IBaseDao getDao() {
		// TODO Auto-generated method stub
		return roleColumnDao;
	}
	
	@Override
	public void saveEntity(List<RoleColumnEntity> roleColumnList){
		// TODO Auto-generated method stub
		roleColumnDao.saveEntity(roleColumnList);
	}
	
	@Override
	public void updateEntity(List<RoleColumnEntity> roleColumnList){
		// TODO Auto-generated method stub
		roleColumnDao.updateEntity(roleColumnList);
	}
	
	@Override
	public List<RoleColumnEntity> queryByRoleId(int roleId) {
		// TODO Auto-generated method stub
		return roleColumnDao.queryByRoleId(roleId);
	}
	
}