
package com.uoumei.basic.action.web;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.uoumei.basic.action.BaseAction;
import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IManagerBiz;
import com.uoumei.basic.biz.IRoleBiz;
import com.uoumei.basic.constant.Const;
import com.uoumei.basic.constant.ModelCode;
import com.uoumei.basic.constant.e.SessionConstEnum;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ManagerEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.basic.entity.RoleEntity;
import com.uoumei.util.FileUtil;
import com.uoumei.util.StringUtil;
import com.uoumei.util.UseTimeUtil;
import com.uoumei.base.util.BaseUtil;
import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.SpringUtil;

@Controller
@RequestMapping("/${managerPath}")
public class LoginAction extends BaseAction {
	
	public static int LOCK_TIME_MINUTES = 30;
	public static int LOCK_COUNT = 6;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Value("${managerPath}")
	private String managerPath;
	/**
	 * 管理员业务层
	 */
	@Autowired
	private IManagerBiz managerBiz;

	/**
	 * 角色业务request层
	 */
	@Autowired
	private IRoleBiz roleBiz;

	/**
	 * 站点业务层
	 */
	@Autowired
	private IAppBiz appBiz;


	/**
	 * 加载管理员登录界面
	 * 
	 * @param request
	 *            请求对象
	 * @return 管理员登录界面地址
	 */
	@SuppressWarnings("resource")
	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		if (BasicUtil.getSession(SessionConstEnum.MANAGER_SESSION)!=null) {
			return "redirect:"+managerPath+"/index.do";
		}
		/*
		// 根据请求地址来显示标题
		AppEntity app = BasicUtil.getApp();
		// 判断应用实体是否存在
		if (app != null) {
			// 检测应用是否有自定义界面b
			if (!StringUtil.isBlank(app.getAppLoginPage())) {
				LOG.debug("跳转自定义登录界面");
				return "redirect:" + app.getAppLoginPage();
			}
			
		} else {
			File file = new File(this.getRealPath(request, "WEB-INF/ms.install"));
			//存在安装文件
			if (file.exists()) {
				String defaultId = FileUtil.readFile(this.getRealPath(request, "WEB-INF/ms.install")).trim();
				if (!StringUtil.isBlank(defaultId)) {
					app = (AppEntity) appBiz.getEntity(Integer.parseInt(defaultId));
					app.setAppUrl(this.getUrl(request));
					appBiz.updateEntity(app);
					FileUtil.writeFile(defaultId, this.getRealPath(request, "WEB-INF/ms.install.bak"), com.uoumei.base.constant.Const.UTF8);
					file.delete();
				}
			} 

		}
		request.setAttribute("app", app);
		*/
		return view("/login");
	}

	/**
	 * 验证登录
	 * 
	 * @param manager
	 *            管理员实体
	 * @param request
	 *            请求
	 * @param response
	 *            响应
	 */
	@RequestMapping(value="/checkLogin",method=RequestMethod.POST)
	public void checkLogin(@ModelAttribute ManagerEntity manager, HttpServletRequest request,
			HttpServletResponse response) {
		
		//		if (!(checkRandCode(request))) {
		boolean isRightRandCode = checkRandCode(request);
		request.getSession().removeAttribute(SessionConstEnum.CODE_SESSION.toString());
		if (!isRightRandCode) {
			 outJson(response, null, false,
					getResString("err.error", new String[] { getResString("rand.code") }));
			 return;
		}

		// 根据账号获取当前管理员信息
		ManagerEntity newManager = new ManagerEntity();
		newManager.setManagerName(manager.getManagerName());
		ManagerEntity _manager = (ManagerEntity) managerBiz.getEntity(newManager);
		if (_manager == null || StringUtil.isBlank(manager.getManagerName())) {
			// 系统不存在此用户
			this.outJson(response, ModelCode.ADMIN_LOGIN, false, this.getResString("err.nameEmpty"));
			return;
		}
		
		// 判断当前用户输入的密码是否正确
//		if (StringUtil.Md5(manager.getManagerPassword()).equals(_manager.getManagerPassword())) {
		if (manager.getManagerPassword().equals(_manager.getManagerPassword())) {
			
			//20190610
			String missResult = passIsOK(_manager);
			if(!StringUtil.isBlank(missResult)){
				// 
				this.outJson(response, ModelCode.ADMIN_LOGIN, false, missResult);
				return;
			}
			//20190610 end
			
			// 创建管理员session对象
			ManagerSessionEntity managerSession = new ManagerSessionEntity();
			AppEntity website = new AppEntity();
			// 获取管理员所在的角色
			RoleEntity role = (RoleEntity) roleBiz.getEntity(_manager.getManagerRoleID());
			if(role == null){
				// 角色不存在
				this.outJson(response, ModelCode.ADMIN_LOGIN, false, this.getResString("err.not.exist"));
				return;
			}
			website = (AppEntity) appBiz.getByManagerId(_manager.getManagerId());
			if(website != null){
				List childManagerList = managerBiz.queryAllChildManager(managerSession.getManagerId());
				managerSession.setBasicId(website.getAppId());    
				managerSession.setManagerParentID(role.getRoleManagerId());
				managerSession.setManagerChildIDs(childManagerList);
				managerSession.setStyle(website.getAppStyle());
			}else {
				if(_manager.getManagerRoleID() != Const.DEFAULT_SYSTEM_MANGER_ROLE_ID){
					website = (AppEntity) appBiz.getByManagerId(role.getRoleManagerId());
					if(website != null){
						managerSession.setBasicId(website.getAppId());    
						managerSession.setStyle(website.getAppStyle());
					}else{
						// 子站不存在
						this.outJson(response, ModelCode.ADMIN_LOGIN, false, this.getResString("err.not.exist"));
						return;
					}
				}
			}

			BaseUtil.setSession(SessionConstEnum.MANAGER_SESSION, managerSession);
			BeanUtils.copyProperties(_manager, managerSession);
			

			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken upt = new UsernamePasswordToken(managerSession.getManagerName(),
					managerSession.getManagerPassword());
			subject.login(upt);

			this.outJson(response, ModelCode.ADMIN_LOGIN, true, null);
		} else {  // 密码错误
			//20190610
			String missResult = passIsError(_manager);
			if(!StringUtil.isBlank(missResult)){
				// 角色不存在
				this.outJson(response, ModelCode.ADMIN_LOGIN, false, missResult);
				return;
			}
			//20190610 end
			this.outJson(response, ModelCode.ADMIN_LOGIN, false, this.getResString("err.password"));
		}
	}

	private String passIsError(ManagerEntity manager){
		int lockFlag = manager.getLockFlag();
		int loginErrCount = manager.getLoginErrCount();
		Date lastLoginTime = manager.getLastLoginTime();
		
		//current time
//		Date date = new Date();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String datestr = format.format(date);
//		Date curLoginTime = null;
//		try {
//			curLoginTime = format.parse(datestr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Date curLoginTime = new Date();
		if ( lastLoginTime == null) {
			lastLoginTime = curLoginTime;
		}
		
		if (lockFlag == 1) {	// 账户被锁定 // 被锁定是登陆错误次数一定是5，所以只判断一次
			Long timeSlot = 0L;
			timeSlot = curLoginTime.getTime() - lastLoginTime.getTime();
 
			if (timeSlot < LOCK_TIME_MINUTES *60*60) {	// 判断最后锁定时间,30分钟之内继续锁定
				return "您的账户已被锁定，请" + (LOCK_TIME_MINUTES-Math.ceil((double)timeSlot/60000)) + "分钟之后再次尝试";
			} else {								// 判断最后锁定时间,30分钟之后仍是错误，继续锁定30分钟
				manager.setLastLoginTime(curLoginTime);
				manager.setLoginErrCount(-1);
				manager.setLockFlag(-1);
				//update
				managerBiz.updateLogin(manager);
				
				return "账户或密码错误,您的账户已被锁定，请30分钟之后再次尝试登陆";
			}	
		} else if (loginErrCount == LOCK_COUNT-1) {	// 账户第五次登陆失败  ，此时登陆错误次数增加至5，以后错误仍是5，不再递增
			manager.setLastLoginTime(curLoginTime);
			manager.setLoginErrCount(LOCK_COUNT);
			manager.setLockFlag(1);
			//update 
			managerBiz.updateLogin(manager);
			
			return "您的账户已被锁定，请30分钟之后再次尝试登陆";
		} else {										// 账户前四次登陆失败
			manager.setLastLoginTime(curLoginTime);
			manager.setLoginErrCount(loginErrCount+1);
			manager.setLockFlag(-1);
			//update
			managerBiz.updateLogin(manager);
			
			return "账户或密码错误,您还有" + (LOCK_COUNT - manager.getLoginErrCount()) +"次登陆机会";
		}
	}
	private String passIsOK(ManagerEntity manager){
		int lockFlag = manager.getLockFlag();
		int loginErrCount = manager.getLoginErrCount();
		Date lastLoginTime = manager.getLastLoginTime();
		
		Date curLoginTime = new Date();
		if ( lastLoginTime == null) {
			lastLoginTime = curLoginTime;
		}
		
		if (lockFlag == 1) {
			Long timeSlot = 0L;
			timeSlot = curLoginTime.getTime() - lastLoginTime.getTime();
			 
			if (timeSlot < LOCK_TIME_MINUTES *60*60) {	// 判断最后锁定时间,30分钟之内继续锁定
				return "您的账户已被锁定，请" + (LOCK_TIME_MINUTES-Math.ceil((double)timeSlot/60000)) + "分钟之后再次尝试";
			}else {							// 判断最后锁定时间,30分钟之后登陆账户
				manager.setLoginErrCount(0);
				manager.setLastLoginTime(curLoginTime);
				manager.setLockFlag(0);
				//update 
				managerBiz.updateLogin(manager);
				
				return "";
			}	
		} else {
			manager.setLoginErrCount(0);
			manager.setLastLoginTime(curLoginTime);
			manager.setLockFlag(0);
			//update 
			managerBiz.updateLogin(manager);
			
			return "";
		}
	}
}