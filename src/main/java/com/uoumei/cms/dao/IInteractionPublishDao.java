package com.uoumei.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.cms.entity.InteractionPublishEntity;

public interface IInteractionPublishDao extends IBaseDao {
	void save(InteractionPublishEntity interactionPublish);
	void update(@Param("id") int id);
	List<InteractionPublishEntity> queryByState(@Param("type") int type, @Param("appId") int appId);
	List<InteractionPublishEntity> queryByType(@Param("appId") int appId, @Param("type") int type, @Param("state") int state, @Param("content") String content);
	InteractionPublishEntity getById(@Param("id") int id);
	void deleteEntity(@Param("id") int id);
	
}
