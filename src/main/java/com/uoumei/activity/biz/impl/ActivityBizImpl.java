package com.uoumei.activity.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.uoumei.activity.biz.IActivityBiz;
import com.uoumei.activity.dao.IProcColumnDao;
import com.uoumei.activity.dao.IProcColumnStepsDao;
import com.uoumei.activity.entity.ProcColumnEntity;
import com.uoumei.activity.entity.ProcColumnStepsEntity;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.dao.IManagerDao;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.cms.dao.IArticleDao;
import com.uoumei.cms.entity.ArticleEntity;

@Service
public class ActivityBizImpl implements IActivityBiz {

	@Autowired
	IProcColumnDao procColumnDao;

	@Autowired
	IProcColumnStepsDao procColumnStepsDao;

	@Autowired
	IArticleDao articleDao;

	@Autowired
	IColumnBiz columnBiz;

	@Autowired
	IdentityService identityService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	IManagerDao managerDao;

	@Override
	public int saveProc(JSONObject inJson) {
		String name = inJson.getString("name");
		String description = inJson.getString("description");
		Integer sort = inJson.getInteger("sort");
		Integer steps = inJson.getInteger("steps");
		Integer columnId = inJson.getInteger("columnId");
		String columnName = inJson.getString("columnName");
		Integer appId = inJson.getInteger("appId");
		JSONArray members = inJson.getJSONArray("members");
		JSONArray memberName = inJson.getJSONArray("memberName");

		// 流程定义表保存
		ProcColumnEntity procColumn = new ProcColumnEntity();
		procColumn.setName(name);
		procColumn.setDescription(description);
		procColumn.setSort(sort);
		procColumn.setAppId(appId);
		procColumn.setSteps(steps);
		procColumn.setColumnId(columnId);
		procColumn.setColumnName(columnName);
		procColumn.setProcKey("audit" + steps);
		procColumnDao.save(procColumn);

		// 流程定义步骤表保存
		for (int index = 0; index < steps; index++) {
			int step = index + 1;
			ProcColumnStepsEntity procColumnSteps = new ProcColumnStepsEntity();
			procColumnSteps.setProcColumnId(procColumn.getId());
			procColumnSteps.setStep(step);
			procColumnSteps.setRoleName("audit" + step);
			JSONArray member = members.getJSONArray(index);
			procColumnSteps.setMember(member.toJSONString());
			JSONArray memberNames = memberName.getJSONArray(index);
			procColumnSteps.setMemberName(memberNames.toJSONString());
			procColumnStepsDao.save(procColumnSteps);
		}

		return procColumn.getId();
	}

	@Override
	public ProcColumnEntity queryProcByColumnId(int columnId) {
		ProcColumnEntity pcEntity = procColumnDao.queryEntityByColumnId(columnId);
		if (null != pcEntity)
			return pcEntity;

		List<ColumnEntity> list = columnBiz.queryParentColumnByColumnId(columnId);
		if (null == list) {
			return null;
		}
		for (ColumnEntity columnEntity : list) {
			pcEntity = procColumnDao.queryEntityByColumnId(columnEntity.getCategoryId());
			if (null != pcEntity)
				return pcEntity;
		}
		return null;
	}

	@Override
	public ProcessInstance startWorkflow(int articleId, int managerId, ProcColumnEntity pcEntity) {

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applyuserid", managerId);

		List<ProcColumnStepsEntity> list = procColumnStepsDao.queryByProcColumnId(pcEntity.getId());
		for (ProcColumnStepsEntity pcs : list) {
			String member = pcs.getMember();
			JSONArray jarray = JSONArray.parseArray(member);
			List<String> userList = new ArrayList<String>();
			for (int index = 0; index < jarray.size(); index++) {
				userList.add(jarray.getString(index));
			}
			variables.put(pcs.getRoleName(), userList);
		}
		String businesskey = String.valueOf(articleId);
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(pcEntity.getProcKey(), businesskey,
				variables);

		return instance;
	}

	/**
	 * 查询待审核
	 */
	@Override
	public List<ArticleEntity> queryAudit(int managerId, int appId) {
		List<ArticleEntity> results = new ArrayList<ArticleEntity>();
		List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(String.valueOf(managerId)).list();
		for (Task task : tasks) {
			String instanceid = task.getProcessInstanceId();
			ProcessInstance ins = runtimeService.createProcessInstanceQuery().processInstanceId(instanceid)
					.singleResult();
			String businesskey = ins.getBusinessKey();
			ArticleEntity a = (ArticleEntity) articleDao.getEntity(Integer.parseInt(businesskey));
			a.setId(task.getId());
			results.add(a);
		}
		return results;
	}

	/**
	 * 完成审核
	 */
	@Override
	public boolean updateAudit(int managerId, JSONObject inJson) {

		Map<String, Object> variables = new HashMap<String, Object>();

		boolean approve = inJson.getBoolean("state");
		String note = inJson.getString("note");
		if (approve) {
			variables.put("approve", true);
			if(StringUtils.isBlank(note)){
				note = "同意";
			}
		} else {
			variables.put("approve", false);
			if(StringUtils.isBlank(note)){
				note = "不同意";
			}
		}
		variables.put("note", note);
		
		JSONArray taskIds = inJson.getJSONArray("taskIds");
		for (int index = 0; index < taskIds.size(); index++) {
			String taskId = taskIds.getString(index);
			taskService.claim(taskId, String.valueOf(managerId));
			taskService.complete(taskId, variables);
		}

		return true;
	}

	/**
	 * 查询所有流程
	 */
	@Override
	public List<ProcColumnEntity> queryByAppId(int appId) {
		return procColumnDao.queryAll(appId);
	}

	/**
	 * 删除流程
	 */
	@Override
	public void deleteById(JSONObject inJson) {
		JSONArray ids = inJson.getJSONArray("ids");
		for (int i = 0; i < ids.size(); i++) {
			procColumnDao.delete(Integer.valueOf(ids.get(i).toString()));
			procColumnStepsDao.delete(Integer.valueOf(ids.get(i).toString()));
		}
		
	}

	@Override
	public int qyeryByColumnId(int columnId) {
		return procColumnDao.queryByColumnId(columnId);
	}

	/**
	 * 根据id查询信息
	 */
	@Override
	public ProcColumnEntity queryById(JSONObject inJson) {
		int id = inJson.getIntValue("id");
		ProcColumnEntity procColumn = procColumnDao.queryById(id);
		List<ProcColumnStepsEntity> member = procColumnStepsDao.queryByProcColumnId(id);
		procColumn.setMember(member);
		return procColumn;
	}

	/**
	 * 修改工作流
	 */
	@Override
	public void update(JSONObject inJson) {
		Integer id = inJson.getInteger("id");
//		int id = inJson.getIntValue("id");
		String name = inJson.getString("name");
		String description = inJson.getString("description");
		Integer sort = inJson.getInteger("sort");
		Integer steps = inJson.getInteger("steps");
		Integer columnId = inJson.getInteger("columnId");
		String columnName = inJson.getString("columnName");
		Integer appId = inJson.getInteger("appId");
		JSONArray members = inJson.getJSONArray("members");
		JSONArray memberName = inJson.getJSONArray("memberName");
		
		ProcColumnEntity procColumn = new ProcColumnEntity();
		procColumn.setId(id);
		procColumn.setName(name);
		procColumn.setDescription(description);
		procColumn.setSort(sort);
		procColumn.setAppId(appId);
		procColumn.setSteps(steps);
		procColumn.setColumnId(columnId);
		procColumn.setColumnName(columnName);
		procColumn.setProcKey("audit" + steps);

		procColumnDao.update(procColumn);
		procColumnStepsDao.delete(id);
		// 流程定义步骤表保存
		for (int index = 0; index < steps; index++) {
			int step = index + 1;
			ProcColumnStepsEntity procColumnSteps = new ProcColumnStepsEntity();
			procColumnSteps.setProcColumnId(id);
			procColumnSteps.setStep(step);
			procColumnSteps.setRoleName("audit" + step);
			JSONArray member = members.getJSONArray(index);
			procColumnSteps.setMember(member.toJSONString());
			JSONArray memberNames = memberName.getJSONArray(index);
			procColumnSteps.setMemberName(memberNames.toJSONString());
			procColumnStepsDao.save(procColumnSteps);
		}

	}

}
