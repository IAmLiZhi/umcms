
package com.uoumei.basic.biz;

import java.util.List;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.util.PageUtil;

/**
 * 管理员业务层，接口，继承IBaseBiz
 * 
 */
public interface IManagerBiz extends IBaseBiz {
	
    
	/**
	 * 根据用户名修改用户密码
	 * @param manager 管理员实体
	 */
	public void updateUserPasswordByUserName(ManagerEntity manager);
	
	//--zzq 20190610
	public void updateLogin(ManagerEntity manager);	
	
	/**
	 * 查询当前登录的管理员的所有子管理员
	 * @return 返回管理员集合
	 */
	public List<ManagerEntity> queryAllChildManager(int managerId);
	
	
}