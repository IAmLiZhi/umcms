package com.uoumei.activity.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uoumei.activity.entity.ProcColumnEntity;
import com.uoumei.base.dao.IBaseDao;

public interface IProcColumnDao extends IBaseDao{
	void save(ProcColumnEntity procColumn);
	void update(ProcColumnEntity procColumn);
	List<ProcColumnEntity> queryAll(int appId);
	int queryByColumnId(int columnId);
	ProcColumnEntity queryEntityByColumnId(int columnId);
	void delete(int id);
	ProcColumnEntity queryById(int id);
	

}
