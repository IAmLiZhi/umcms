package com.uoumei.base.security.session;

import java.util.Collection;

import org.apache.shiro.session.Session;

@Deprecated
public interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO {

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave);
	
	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);
	
}
