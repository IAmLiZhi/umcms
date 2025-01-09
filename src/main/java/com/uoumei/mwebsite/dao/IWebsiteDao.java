package com.uoumei.mwebsite.dao;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.entity.RoleEntity;

/**
 * 网站基本信息持久化层
 */
public interface IWebsiteDao extends IBaseDao{
	
	/**
	 * 根据域名查找相同域名的个数
	 * @param websiteUrl 域名
	 * @return 返回相同域名的个数
	 */
	int countByUrl(String websiteUrl);
	
	/**
	 * 根据域名查找站点实体
	 * @param websiteUrl 域名
	 * @return 返回站点实体
	 */
	BaseEntity getByUrl(String websiteUrl);
	
	/**
	 * 更据站点管理员id查找站点
	 * @param managerId 管理员id
	 * @return 返回站点实体
	 */
	BaseEntity getByManagerId(int managerId);
	
	/**
	 * 插入站点超级管理员角色
	 * @param role 角色实体
	 * @return 角色id
	 */
	int saveRole(RoleEntity role);
}
