
package com.uoumei.basic.entity;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.base.entity.SessionEntity;

/**
 * 管理员实体类
 */
public class ManagerEntity extends SessionEntity {

    /**
     * 自增长编号
     */
    private int managerId;

    /**
     * 帐号
     */
    private String managerName;

    /**
     * 昵称
     */
    private String managerNickName;
    
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 密码
     */
    private String managerPassword;

    /**
     * 角色ID
     */
    private int managerRoleID;

    /**
     * 用户ID
     */
    private int managerPeopleID;
    
    /**
     * 管理员主界面样式
     */
    private int managerSystemSkinId;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date managerTime;
    
    private Date lastLoginTime;
    private int loginErrCount;
    private int lockFlag;

    public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getLoginErrCount() {
		return loginErrCount;
	}
	public void setLoginErrCount(int loginErrCount) {
		this.loginErrCount = loginErrCount;
	}
	public int getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(int lockFlag) {
		this.lockFlag = lockFlag;
	}

	/**
     * 获取角色名
     * @return
     */
    public String getRoleName() {
		return roleName;
	}

    /**
     * 设置角色名
     * @param roleName
     */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
     * 获取managerTime
     * @return managerTime
     */
    public Date getManagerTime() {
        return managerTime;
    }

    /**
     * 设置managerTime
     * @param managerTime
     */
    public void setManagerTime(Date managerTime) {
        this.managerTime = managerTime;
    }

    /**
     * 获取managerId
     * @return managerId
     */
    public int getManagerId() {
        return managerId;
    }

    /**
     * 设置managerId
     * @param managerId
     */
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    /**
     * 获取managerName
     * @return managerName
     */
    public String getManagerName() {
        return managerName;
    }

    /**
     * 设置managerName
     * @param managerName
     */
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    /**
     * 获取managerPassword
     * @return managerPassword
     */
    public String getManagerPassword() {
        return managerPassword;
    }

    /**
     * 设置managerPassword
     * @param managerPassword
     */
    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    /**
     * 获取managerPeopleID
     * @return managerPeopleID
     */
    public int getManagerPeopleID() {
        return managerPeopleID;
    }

    /**
     * 设置managerPeopleID
     * @param managerPeopleID
     */
    public void setManagerPeopleID(int managerPeopleID) {
        this.managerPeopleID = managerPeopleID;
    }

    /**
     * 获取managerNickName
     * @return managerNickName
     */
    public String getManagerNickName() {
        return managerNickName;
    }

    /**
     * 设置managerNickName
     * @param managerNickName
     */
    public void setManagerNickName(String managerNickName) {
        this.managerNickName = managerNickName;
    }

    /**
     * 获取managerRoleID
     * @return managerRoleID
     */
	public int getManagerRoleID() {
		return managerRoleID;
	}

	/**
	 * 设置managerRoleID
	 * @param managerRoleID
	 */
	public void setManagerRoleID(int managerRoleID) {
		this.managerRoleID = managerRoleID;
	}

	public int getManagerSystemSkinId() {
		return managerSystemSkinId;
	}

	public void setManagerSystemSkinId(int managerSystemSkinId) {
		this.managerSystemSkinId = managerSystemSkinId;
	}	
	
	
	
}