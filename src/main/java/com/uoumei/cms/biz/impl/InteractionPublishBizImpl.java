package com.uoumei.cms.biz.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uoumei.base.util.StringUtil;
import com.uoumei.cms.biz.IInteractionPublishBiz;
import com.uoumei.cms.dao.IInteractionPublishDao;
import com.uoumei.cms.dao.IInteractionReplyDao;
import com.uoumei.cms.entity.InteractionPublishEntity;
import com.uoumei.util.JsonResult;

/**
 * 互动交流业务层实现类
 */
@Service
public class InteractionPublishBizImpl implements IInteractionPublishBiz {

	@Autowired
	IInteractionPublishDao interactionPublishDao;
	
	@Autowired
	IInteractionReplyDao interactionReplyDao;

	/**
	 * 保存发布
	 */
	@Override
	public boolean save(JSONObject inJson) {
		String name = inJson.getString("name");
		String phone = inJson.getString("phone");
		String cardNo = inJson.getString("cardNo");
		String title = inJson.getString("title");
		if (!StringUtil.checkLength(title, 0, 128)) {
			return false;
		}
		String publishContent = inJson.getString("publishContent");
		if (!StringUtil.checkLength(publishContent, 0, 1024)) {
			return false;
		}
		String fileUrl = inJson.getString("fileUrl");
		int type = inJson.getIntValue("type");
		int appId = inJson.getIntValue("appId");
		InteractionPublishEntity interactionPublish = new InteractionPublishEntity();
		interactionPublish.setName(name);
		interactionPublish.setPhone(phone);
		interactionPublish.setTitle(title);
		interactionPublish.setPublishContent(publishContent);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String createTime = format.format(new Date());
		interactionPublish.setCreateTime(new Date());
		interactionPublish.setType(type);
		interactionPublish.setFileUrl(fileUrl);
		interactionPublish.setState(2);
		interactionPublish.setCardNo(cardNo);
		interactionPublish.setAppId(appId);
		interactionPublishDao.save(interactionPublish);
		return true;
	}

	/**
	 * 修改发布
	 */
	@Override
	public void update(JSONObject inJson) {
		Integer id = inJson.getInteger("id");
		interactionPublishDao.update(id);
	}

	/**
	 * 查询所有
	 */
	@Override
	public List<InteractionPublishEntity> queryByType(JSONObject inJson) {
		int type = inJson.getIntValue("type");
		int appId = inJson.getIntValue("appId");
		String content = inJson.getString("content");
		int state = inJson.getIntValue("state");
		List<InteractionPublishEntity> list = interactionPublishDao.queryByType(appId, type, state, content);
		return list;
	}

	/**
	 * 根据state查询
	 */
	@Override
	public JSONObject queryByState(JSONObject inJson) {
		int type = inJson.getIntValue("type");
		int appId = inJson.getIntValue("appId");
		int pageNum = inJson.getIntValue("pageNum");
		int pageSize = inJson.getIntValue("pageSize");
		PageHelper.startPage(pageNum, pageSize);
		List<InteractionPublishEntity> list = interactionPublishDao.queryByState(type, appId);
		PageInfo<InteractionPublishEntity> page = new PageInfo<>(list);
		JSONArray array = new JSONArray();
		array.add(page);
		return JsonResult.getSuccResult(array);
	}

	/**
	 * 根据id查询互动交流信息
	 */
	@Override
	public InteractionPublishEntity getById(int id) {
		InteractionPublishEntity interactionPublish = interactionPublishDao.getById(id);
		return interactionPublish;
	}

	/**
	 * 删除互动交流
	 */
	@Override
	public void del(JSONObject inJson) {
		JSONArray ids = inJson.getJSONArray("ids");
		for (int i = 0; i < ids.size(); i++) {
			interactionPublishDao.deleteEntity(ids.getIntValue(i));
			interactionReplyDao.deleteEntity(ids.getIntValue(i));
		}
	}

	@Override
	public InteractionPublishEntity show(JSONObject inJson) {
		int id = inJson.getIntValue("id");
		InteractionPublishEntity interactionPublish = interactionPublishDao.getById(id);
		return interactionPublish;
	}
	

}
