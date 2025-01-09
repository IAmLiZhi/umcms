
package com.uoumei.base.util.configurer;

import java.io.IOException;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;


public class ShiroTagFreeMarkderConfigurer
		extends org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer {
	@Override
	public void afterPropertiesSet() throws IOException, TemplateException {
		super.afterPropertiesSet();
		this.getConfiguration().setSharedVariable("shiro", new ShiroTags());
	}
}