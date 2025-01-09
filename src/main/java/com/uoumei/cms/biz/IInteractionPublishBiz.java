package com.uoumei.cms.biz;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.cms.entity.InteractionPublishEntity;

/**
 * 互动交流(发布)业务处理层
 */
public interface IInteractionPublishBiz {
	boolean save(JSONObject inJson);
	void update(JSONObject inJson);
	List<InteractionPublishEntity> queryByType(JSONObject inJson);
	JSONObject queryByState(JSONObject inJson);
	InteractionPublishEntity getById(int id);
	void del(JSONObject inJson);
	InteractionPublishEntity show(JSONObject inJson);
	

}
