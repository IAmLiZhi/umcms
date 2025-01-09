package com.uoumei.mwebsite.biz;

import com.uoumei.basic.biz.IBasicBiz;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.basic.entity.RoleEntity;
import com.uoumei.mwebsite.entity.WebsiteEntity;

/**
 * 网站基本信息业务层接口
 */
public interface IWebsiteBiz extends IBasicBiz {

	/**
	 * 查找域名相同站点的个数
	 * 
	 * @param websiteUrl
	 *            域名
	 * @return 返回站点个数
	 */
	int countByUrl(String websiteUrl);

	/**
	 * 根据站点管理员id查找站点
	 * 
	 * @param managerId
	 *            站点管理员id
	 * @return 返回站点实体
	 */
	WebsiteEntity getByManagerId(int managerId);

	/**
	 * 根据站点域名获取站点实体
	 * 
	 * @param websiteUrl
	 *            域名
	 * @return 返回站点实体
	 */
	WebsiteEntity getByUrl(String websiteUrl);

	/**
	 * 插入超级管理员的角色id
	 * 
	 * @param role
	 *            角色实体
	 * @return 角色id
	 */
	int saveRole(RoleEntity role);

	/**
	 * 根据站点创建管理员与角色信息，该方法会进行管理员的创建，站点的角色创建，角色对应的权限创建
	 * 
	 * @param manager
	 *            管理员
	 * @param models
	 *            模块id集合
	 */
	void save(WebsiteEntity website,ManagerEntity manager, int[] models);
	
	/**
	 * 根据站点更新管理员信息，该方法会进行管理员昵称、密码修改，角色对应的权限修改
	 * @param manager
	 * @param models
	 */
	void update(ManagerEntity manager, int[] models);
}
