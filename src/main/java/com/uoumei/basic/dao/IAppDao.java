
package com.uoumei.basic.dao;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;

/**
 * 网站基本信息持久化层
 */
public interface IAppDao extends IBaseDao{
	
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
}