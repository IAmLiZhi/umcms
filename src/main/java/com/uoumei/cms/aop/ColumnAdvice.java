package com.uoumei.cms.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uoumei.cms.biz.IArticleBiz;

/**
 * 
 * 栏目管理切面
 */
// @Aspect : 标记为切面类
// @Pointcut : 指定匹配切点集合
// @Before : 指定前置通知，value中指定切入点匹配
// @AfterReturning ：后置通知，具有可以指定返回值
// @AfterThrowing ：异常通知
// 注意：前置/后置/异常通知的函数都没有返回值，只有环绕通知有返回值
@Component
// 首先初始化切面类
@Aspect
// 声明为切面类，底层使用动态代理实现AOP
public class ColumnAdvice {
	
	@Autowired
	private IArticleBiz articleBiz;
	
	// 指定切入点匹配表达式，注意它是以方法的形式进行声明的。
		// 即切点集合是：aop.annotation包下所有类所有方法
		// 第一个* 代表返回值类型
		// 如果要设置多个切点可以使用 || 拼接
		// and args(com.uoumei.order.entity.OrderEntity
		@Pointcut(" execution(*  com.uoumei.basic.biz.impl.ColumnBizImpl.deleteCategory(..) ) ")
		public void deleteCategory() {
		}
		
		/**
		 * 
		 * @param jp
		 * @throws Throwable
		 */
		@After("deleteCategory()")
		public void deleteArticle(JoinPoint jp)throws Throwable{
			Object[] obj = jp.getArgs();
			if (obj[0] instanceof Integer) {
				//删除该栏目下的所有文章
				//
			}
		}
}