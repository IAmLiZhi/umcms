package com.uoumei.cms.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hazelcast.com.eclipsesource.json.JsonObject;
import com.uoumei.cms.biz.IInteractionReplyBiz;
import com.uoumei.cms.dao.IInteractionPublishDao;
import com.uoumei.cms.dao.IInteractionReplyDao;
import com.uoumei.cms.entity.InteractionReplyEntity;

/**
 * 互动交流(回复)业务层实现类
 */
@Service
public class InteractionReplyBizImpl implements IInteractionReplyBiz {
	
	/**
	 * 互动交流(发布)持久化层
	 */
	@Autowired
	IInteractionPublishDao interactionPublishDao;
	
	/**
	 * 互动交流(回复)持久化层
	 */
	@Autowired
	IInteractionReplyDao interactionReplyDao;
	
	/**
	 * 互动交流(回复)
	 */
	@Override
	public void save(JSONObject inJson) {
		int publishId = inJson.getIntValue("publishId");
		String replyContent = inJson.getString("replyContent");
		int isOpen = inJson.getIntValue("isOpen");
		
		InteractionReplyEntity interactionReply = new InteractionReplyEntity();
		interactionReply.setPublishId(publishId);
		interactionReply.setReplyTime(new Date());
		interactionReply.setReplyContent(replyContent);
		interactionReply.setIsOpen(isOpen);
		//保存回复
		interactionReplyDao.save(interactionReply);
		//更新发布状态为已回复
		interactionPublishDao.update(publishId);
		
	}

	/**
	 * 互动交流(修改回复)
	 */
	@Override
	public void update(JSONObject inJson) {
		String replyContent = inJson.getString("replyContent");
		Date replyTime = new Date();
		int publishId = inJson.getIntValue("publishId");
		int isOpen = inJson.getIntValue("isOpen");
		interactionReplyDao.update(replyContent, replyTime, publishId,isOpen);
		
	}

}
