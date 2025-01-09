
package com.uoumei.basic.biz;

import com.uoumei.basic.entity.AppEntity;

/**
 * 网站基本信息业务层接口
 * 
 */
public interface IAppBiz extends IBasicBiz{
	
	/**
	 * 查找域名相同站点的个数
	 * @param websiteUrl 域名
	 * @return 返回站点个数
	 */
	int countByUrl(String websiteUrl);
	
	/**
	 * 根据站点管理员id查找站点
	 * @param managerId 站点管理员id
	 * @return 返回站点实体
	 */
	AppEntity getByManagerId(int managerId);
	/**
	 * 根据站点域名获取站点实体
	 * @param websiteUrl 域名
	 * @return 返回站点实体
	 */
	AppEntity getByUrl(String websiteUrl);
}