
package com.uoumei.basic.biz.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.dao.IManagerDao;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.util.PageUtil;

/**
 * 管理员业务层实现类，继承BaseBizImpl，实现IManagerBiz
 * 
 */
@Service("managerBiz")
public class ManagerBizImpl extends BaseBizImpl implements IManagerBiz {

	/**
	 * 注入管理员持久化层
	 */
    private IManagerDao managerDao;
    
    /**
	 * 获取管理员持久化层
	 * @return managerDao 返回管理员持久化层
	 */
    public IManagerDao getManagerDao() {
        return managerDao;
    }

    /**
	 * 设置managerDao
	 * @param managerDao 管理员持久化层
	 */
    @Autowired
    public void setManagerDao(IManagerDao managerDao) {
    	// TODO Auto-generated method stub
       this.managerDao = managerDao;
    }

	/**
	 * 获取管理员持久化层
	 * @return managerDao 返回管理员持久化层
	 */
    @Override
    public IBaseDao getDao() {
    	// TODO Auto-generated method stub
       return managerDao;
    }

	@Override
	public void updateUserPasswordByUserName(ManagerEntity manager) {
		// TODO Auto-generated method stub
		managerDao.updateUserPasswordByUserName(manager);
	}

	@Override
	public void updateLogin(ManagerEntity manager) {
		// TODO Auto-generated method stub
		managerDao.updateLogin(manager);
	}
	
	@Override
	public List<ManagerEntity> queryAllChildManager(int managerId){
		// TODO Auto-generated method stub
		return managerDao.queryAllChildManager(managerId);
	}
	
}