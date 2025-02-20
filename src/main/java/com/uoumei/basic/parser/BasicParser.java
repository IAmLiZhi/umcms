package com.uoumei.basic.parser;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.util.StringUtil;

public class BasicParser extends IGeneralParser{
	
	public static final BasicParser basicParser = new BasicParser();
	
	@Override
	public String parse(String html, Object... obj) {
		super.htmlContent = html;
		init(obj);
		htmlContent = parseGeneral();
		return htmlContent;
	}
	
	public static BasicParser getInstance() {
		return basicParser;
	}
}
