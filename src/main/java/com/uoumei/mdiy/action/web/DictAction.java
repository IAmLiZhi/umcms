package com.uoumei.mdiy.action.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.mdiy.biz.IDictBiz;
import com.uoumei.mdiy.biz.IFormBiz;
import com.uoumei.mdiy.entity.DictEntity;
import com.uoumei.base.entity.ListJson;
import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;

import com.uoumei.base.util.bean.EUListBean;
import com.uoumei.base.util.BasicUtil;

/**
 * 通用自定义字典
 */
@Controller("webDictAction")
@RequestMapping("/mdiy/dict")
public class DictAction extends BaseAction{
	
	/**
	 * 注入字典表业务层
	 */	
	@Autowired
	private IDictBiz dictBiz;
	
	/**
	 * 查询字典表列表
	 * @param dict 字典表实体
	 * <i>dict参数包含字段信息参考：</i><br/>
	 * dictId 编号<br/>
	 * dictAppId 应用编号<br/>
	 * dictValue 数据值<br/>
	 * dictLabel 标签名<br/>
	 * dictType 类型<br/>
	 * dictDescription 描述<br/>
	 * dictSort 排序（升序）<br/>
	 * dictParentId 父级编号<br/>
	 * createBy 创建者<br/>
	 * createDate 创建时间<br/>
	 * updateBy 更新者<br/>
	 * updateDate 更新时间<br/>
	 * dictRemarks 备注信息<br/>
	 * del 删除标记<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * dictId: 编号<br/>
	 * dictAppId: 应用编号<br/>
	 * dictValue: 数据值<br/>
	 * dictLabel: 标签名<br/>
	 * dictType: 类型<br/>
	 * dictDescription: 描述<br/>
	 * dictSort: 排序（升序）<br/>
	 * dictParentId: 父级编号<br/>
	 * createBy: 创建者<br/>
	 * createDate: 创建时间<br/>
	 * updateBy: 更新者<br/>
	 * updateDate: 更新时间<br/>
	 * dictRemarks: 备注信息<br/>
	 * del: 删除标记<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute DictEntity dict,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		dict.setAppId(BasicUtil.getAppId());
		BasicUtil.startPage();
		List dictList = dictBiz.query(dict);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(new EUListBean(dictList,(int)BasicUtil.endPage(dictList).getTotal()),new DoubleValueFilter(),new DateValueFilter()));
	}
	
}
