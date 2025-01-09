package com.uoumei.cms.biz;

import com.alibaba.fastjson.JSONObject;

/**
 * 互动交流(发布)业务处理层
 */
public interface IInteractionReplyBiz {
	void save(JSONObject inJson);
	void update(JSONObject inJson);
}
