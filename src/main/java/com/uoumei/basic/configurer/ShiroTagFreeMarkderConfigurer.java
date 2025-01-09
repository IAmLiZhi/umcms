
package com.uoumei.basic.configurer;

import java.io.IOException;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

/**
 * shiro,freemarker标签设置
 * 
 */
public class ShiroTagFreeMarkderConfigurer
		extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {
	@Override
	public void afterPropertiesSet() throws IOException, TemplateException {
		super.afterPropertiesSet();
		this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}
}