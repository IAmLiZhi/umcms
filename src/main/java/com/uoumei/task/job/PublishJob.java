package com.uoumei.task.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.uoumei.basic.biz.IAppBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.entity.AppEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.constant.Const;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.cms.parser.GeneraterCore;
import com.uoumei.cms.util.PathUtil;
import com.uoumei.util.FileUtil;
import com.uoumei.util.UseTimeUtil;

public class PublishJob {
	@Autowired
	private IArticleBiz articleBiz;
	
	@Autowired
	private IColumnBiz columnBiz;
	
	@Autowired
	private IAppBiz appBiz;
	
	@Autowired
	private GeneraterCore generaterCore;
	
	private Logger log = LoggerFactory.getLogger(PublishJob.class);
	
	public void execute() {
		log.debug("PublishJob enter");
		long begin = System.currentTimeMillis();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateTime = sdf.format(new Date());
		
		List<ArticleEntity> list = articleBiz.queryListByState(dateTime, 0, Const.ARTICLE_STATE_WAIT);
		for(ArticleEntity article : list){
			AppEntity app = (AppEntity) appBiz.getEntity(article.getArticleWebId());
			if(null == app)
				continue;
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(article.getBasicCategoryId());
			if(null == column)
				continue;
			article.setArticleState(Const.ARTICLE_STATE_PUB);
			articleBiz.updateBasic(article);
			
			//生成保存htm页面地址
			String generatePath = PathUtil.getGeneratePath(app);	
			FileUtil.createFolder(generatePath);
			//网站风格路径
			String tmpPath = PathUtil.getTmpPath(app);

			List<ColumnEntity> columnList = columnBiz.queryParentColumnByColumnId(column.getCategoryId());
			columnList.add(column);
			for(ColumnEntity tempColumn : columnList){
				generaterCore.generateOneColumn(tempColumn, app, generatePath, tmpPath, app.getAppHostUrl());
			}
			generaterCore.generateOneIndex(app, generatePath, tmpPath, "index.html", "index.htm");
			generaterCore.generateOneArticle(article, column, app, generatePath, tmpPath, app.getAppHostUrl());
		}
		log.debug("PublishJob USE TOTAL TIME= " + UseTimeUtil.getTimeMillis(begin));
	}

}
