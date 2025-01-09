package com.uoumei.cms.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.uoumei.cms.entity.InteractionReplyEntity;

/**
 * 互动交流回复持久化层
 */
public interface IInteractionReplyDao {
	void save(InteractionReplyEntity interactionReply);

	void deleteEntity(@Param("id") int id);

	void update(@Param("replyContent") String replyContent, @Param("replyTime") Date replyTime,@Param("publishId") int publishId,@Param("isOpen") int isOpen);
}
