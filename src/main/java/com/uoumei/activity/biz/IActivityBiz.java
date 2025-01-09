package com.uoumei.activity.biz;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.bind.annotation.PathVariable;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.activity.entity.ProcColumnEntity;
import com.uoumei.activity.entity.ProcColumnStepsEntity;
import com.uoumei.cms.entity.ArticleEntity;

public interface IActivityBiz {
	
	
	int saveProc(JSONObject inJson);
	
	List<ArticleEntity> queryAudit(int managerId, int appId);
	
	boolean updateAudit(int managerId, JSONObject inJson);
	
	ProcColumnEntity queryProcByColumnId(int columnId);
	
	/**
	 * 保存流程
	 * @param inJson
	 * @param managerId
	 * @param variables
	 * @return
	 */
	ProcessInstance startWorkflow(int articleId, int managerId, ProcColumnEntity pcEntity);
	
	/**
	 * 根据站点ID查询该站点下的流程集合
	 * @param appId	站点id
	 * @return
	 */
	List<ProcColumnEntity> queryByAppId(int appId);
	
	/**
	 * 根据id删除流程
	 */
	void deleteById(JSONObject inJson);
	
	int qyeryByColumnId(int columnId);
	
	/**
	 * 根据id查询信息
	 * @param inJson
	 * @return
	 */
	ProcColumnEntity queryById(JSONObject inJson);
	
	/**
	 * 修改工作流
	 * @param procColumn
	 */
	void update(JSONObject inJson);
	
}
