package com.uoumei.activity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProcColumnStepsEntity implements Serializable{
    private Integer id;

    private Integer procColumnId;

    private Integer step;
    
    private String roleName;

    private String member;
    
    private String memberName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProcColumnId() {
		return procColumnId;
	}

	public void setProcColumnId(Integer procColumnId) {
		this.procColumnId = procColumnId;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}


	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	

	

    

    
}