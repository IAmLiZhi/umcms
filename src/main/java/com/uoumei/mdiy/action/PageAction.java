package com.uoumei.mdiy.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uoumei.mdiy.biz.IPageBiz;
import com.uoumei.mdiy.entity.PageEntity;

import com.uoumei.base.util.JSONObject;

import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.base.entity.BaseEntity;

import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.bean.ListBean;

import com.uoumei.base.filter.DateValueFilter;
import com.uoumei.base.filter.DoubleValueFilter;
import com.uoumei.basic.entity.ManagerSessionEntity;

import com.uoumei.base.util.bean.EUListBean;
	
/**
 * 自定义页面表管理控制层
 */
@Controller
@RequestMapping("/${managerPath}/mdiy/page")
public class PageAction extends com.uoumei.mdiy.action.BaseAction{
	
	/**
	 * 注入自定义页面表业务层
	 */	
	@Autowired
	private IPageBiz pageBiz;
	
	/**
	 * 返回主界面index
	 */
	@RequestMapping("/index")
	public String index(HttpServletResponse response,HttpServletRequest request){
		return view ("/mdiy/page/index");
	}
	
	/**
	 * 查询自定义页面表列表
	 * @param page 自定义页面表实体
	 * <i>page参数包含字段信息参考：</i><br/>
	 * pageId 自增长id<br/>
	 * pageModelId 模块id<br/>
	 * pageAppId 应用id<br/>
	 * pagePath 自定义页面绑定模板的路径<br/>
	 * pageTitle 自定义页面标题<br/>
	 * pageKey 自定义页面访问路径<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>[<br/>
	 * { <br/>
	 * pageId: 自增长id<br/>
	 * pageModelId: 模块id<br/>
	 * pageAppId: 应用id<br/>
	 * pagePath: 自定义页面绑定模板的路径<br/>
	 * pageTitle: 自定义页面标题<br/>
	 * pageKey: 自定义页面访问路径<br/>
	 * }<br/>
	 * ]</dd><br/>	 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public void list(@ModelAttribute PageEntity page,HttpServletResponse response, HttpServletRequest request,ModelMap model) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		page.setPageAppId(managerSession.getBasicId());
		BasicUtil.startPage();
		List pageList = pageBiz.query(page);
		this.outJson(response, com.uoumei.base.util.JSONArray.toJSONString(new EUListBean(pageList,(int)BasicUtil.endPage(pageList).getTotal()),new DoubleValueFilter(),new DateValueFilter()));
	}
	
	/**
	 * 返回编辑界面page_form
	 */
	@RequestMapping("/form")
	public String form(@ModelAttribute PageEntity page,HttpServletResponse response,HttpServletRequest request,ModelMap model){
		if(page.getPageId() != null){
			BaseEntity pageEntity = pageBiz.getEntity(page.getPageId());			
			model.addAttribute("pageEntity",pageEntity);
		}
		
		return view ("/mdiy/page/form");
	}
	
	/**
	 * 获取自定义页面表
	 * @param page 自定义页面表实体
	 * <i>page参数包含字段信息参考：</i><br/>
	 * pageId 自增长id<br/>
	 * pageModelId 模块id<br/>
	 * pageAppId 应用id<br/>
	 * pagePath 自定义页面绑定模板的路径<br/>
	 * pageTitle 自定义页面标题<br/>
	 * pageKey 自定义页面访问路径<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * pageId: 自增长id<br/>
	 * pageModelId: 模块id<br/>
	 * pageAppId: 应用id<br/>
	 * pagePath: 自定义页面绑定模板的路径<br/>
	 * pageTitle: 自定义页面标题<br/>
	 * pageKey: 自定义页面访问路径<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/get")
	@ResponseBody
	public void get(@ModelAttribute PageEntity page,HttpServletResponse response, HttpServletRequest request,ModelMap model){
		if(page.getPageId()<=0) {
			this.outJson(response, null, false, getResString("err.error", this.getResString("page.id")));
			return;
		}
		PageEntity _page = (PageEntity)pageBiz.getEntity(page.getPageId());
		this.outJson(response, _page);
	}
	
	/**
	 * 保存自定义页面表实体
	 * @param page 自定义页面表实体
	 * <i>page参数包含字段信息参考：</i><br/>
	 * pageId 自增长id<br/>
	 * pageModelId 模块id<br/>
	 * pageAppId 应用id<br/>
	 * pagePath 自定义页面绑定模板的路径<br/>
	 * pageTitle 自定义页面标题<br/>
	 * pageKey 自定义页面访问路径<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * pageId: 自增长id<br/>
	 * pageModelId: 模块id<br/>
	 * pageAppId: 应用id<br/>
	 * pagePath: 自定义页面绑定模板的路径<br/>
	 * pageTitle: 自定义页面标题<br/>
	 * pageKey: 自定义页面访问路径<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/save")
	@ResponseBody
	@RequiresPermissions("mdiy:page:save")
	public void save(@ModelAttribute PageEntity page, HttpServletResponse response, HttpServletRequest request) {
		// 获取登录的session --zzq
		ManagerSessionEntity managerSession = (ManagerSessionEntity) getManagerBySession(request);

		page.setPageAppId(managerSession.getBasicId());
		//验证应用id的值是否合法			
		if(StringUtil.isBlank(page.getPageAppId())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.app.id")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPageAppId()+"", 1, 10)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.app.id"), "1", "10"));
			return;			
		}
		//验证自定义页面绑定模板的路径的值是否合法			
		if(StringUtil.isBlank(page.getPagePath())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.path")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPagePath()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.path"), "1", "255"));
			return;			
		}
		//验证自定义页面标题的值是否合法			
		if(StringUtil.isBlank(page.getPageTitle())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.title")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPageTitle()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.title"), "1", "255"));
			return;			
		}
		//验证自定义页面访问路径的值是否合法			
		if(StringUtil.isBlank(page.getPageKey())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.key")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPageKey()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.key"), "1", "255"));
			return;			
		}
		pageBiz.saveEntity(page);
		this.outJson(response, JSONObject.toJSONString(page));
	}
	
	/**
	 * @param page 自定义页面表实体
	 * <i>page参数包含字段信息参考：</i><br/>
	 * pageId:多个pageId直接用逗号隔开,例如pageId=1,2,3,4
	 * 批量删除自定义页面表
	 *            <dt><span class="strong">返回</span></dt><br/>
	 *            <dd>{code:"错误编码",<br/>
	 *            result:"true｜false",<br/>
	 *            resultMsg:"错误信息"<br/>
	 *            }</dd>
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@RequiresPermissions("mdiy:page:del")
	public void delete(@RequestBody List<PageEntity> pages,HttpServletResponse response, HttpServletRequest request) {
		int[] ids = new int[pages.size()];
		for(int i = 0;i<pages.size();i++){
			ids[i] = pages.get(i).getPageId();
		}
		pageBiz.delete(ids);		
		this.outJson(response, true);
	}
	
	/** 
	 * 更新自定义页面表信息自定义页面表
	 * @param page 自定义页面表实体
	 * <i>page参数包含字段信息参考：</i><br/>
	 * pageId 自增长id<br/>
	 * pageModelId 模块id<br/>
	 * pageAppId 应用id<br/>
	 * pagePath 自定义页面绑定模板的路径<br/>
	 * pageTitle 自定义页面标题<br/>
	 * pageKey 自定义页面访问路径<br/>
	 * <dt><span class="strong">返回</span></dt><br/>
	 * <dd>{ <br/>
	 * pageId: 自增长id<br/>
	 * pageModelId: 模块id<br/>
	 * pageAppId: 应用id<br/>
	 * pagePath: 自定义页面绑定模板的路径<br/>
	 * pageTitle: 自定义页面标题<br/>
	 * pageKey: 自定义页面访问路径<br/>
	 * }</dd><br/>
	 */
	@RequestMapping("/update")
	@ResponseBody	
	@RequiresPermissions("mdiy:page:update")
	public void update(@ModelAttribute PageEntity page, HttpServletResponse response,
			HttpServletRequest request) {
		//验证自定义页面绑定模板的路径的值是否合法			
		if(StringUtil.isBlank(page.getPagePath())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.path")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPagePath()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.path"), "1", "255"));
			return;			
		}
		//验证自定义页面标题的值是否合法			
		if(StringUtil.isBlank(page.getPageTitle())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.title")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPageTitle()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.title"), "1", "255"));
			return;			
		}
		//验证自定义页面访问路径的值是否合法			
		if(StringUtil.isBlank(page.getPageKey())){
			this.outJson(response, null,false,getResString("err.empty", this.getResString("page.key")));
			return;			
		}
		if(!StringUtil.checkLength(page.getPageKey()+"", 1, 255)){
			this.outJson(response, null, false, getResString("err.length", this.getResString("page.key"), "1", "255"));
			return;			
		}
		pageBiz.updateEntity(page);
		this.outJson(response, JSONObject.toJSONString(page));
	}
		
}