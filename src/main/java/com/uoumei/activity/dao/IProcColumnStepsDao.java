package com.uoumei.activity.dao;

import java.util.List;

import com.uoumei.activity.entity.ProcColumnStepsEntity;
import com.uoumei.base.dao.IBaseDao;

public interface IProcColumnStepsDao extends IBaseDao{
	
	List<ProcColumnStepsEntity> queryByProcColumnId(int procColumnId);
	
	void save(ProcColumnStepsEntity procColumnSteps);
	void delete(int procColumnId);
	
	List<ProcColumnStepsEntity> queryManName(int procColumnId);

}
