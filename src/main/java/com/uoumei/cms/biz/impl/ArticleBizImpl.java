
package com.uoumei.cms.biz.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.ICategoryBiz;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.basic.biz.impl.BasicBizImpl;
import com.uoumei.basic.entity.CategoryEntity;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.biz.IArticleBiz;
import com.uoumei.cms.constant.ModelCode;
import com.uoumei.cms.dao.IArticleDao;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.mdiy.biz.IContentModelBiz;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.util.JsonResult;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;

import cn.hutool.core.util.ArrayUtil;

/**
 * 文章管理业务层实现类
 */
@Service("ArticleBizImpl")
public class ArticleBizImpl extends BasicBizImpl implements IArticleBiz {

	/**
	 * 文章持久化处理
	 */
	private IArticleDao articleDao;

	/**
	 * 栏目业务处理
	 */
	@Autowired
	private ICategoryBiz categoryBiz;

	/**
	 * 自定类型义业务处理
	 */
	@Autowired
	private IColumnBiz columnBiz;

	/**
	 * 自定义模型
	 */
	@Autowired
	private IContentModelBiz contentModelBiz;

	/**
	 * 模块管理业务层
	 */
	@Autowired
	private IModelBiz modelBiz;

	@Override
	public int count(int webId, int[] basicCategoryId, String[] flags, String[] noFlags, ArticleEntity article) {
		return articleDao.count(webId, basicCategoryId, flags, noFlags, article);
	}

	@Override
	public int countByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return articleDao.countByCategoryId(categoryId);
	}

	/**
	 * 获取Article的持久化层
	 * 
	 * @return 返回持Article的久化对象
	 */
	public IArticleDao getArticleDao() {
		return articleDao;
	}

	@Override
	public ArticleEntity getByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		List list = articleDao.getByCategoryId(categoryId);
		if (list != null && list.size() > 0) {
			return (ArticleEntity) list.get(0);
		}
		return null;
	}

	@Override
	public ArticleEntity getById(int basicId) {
		// TODO Auto-generated method stub
		ArticleEntity article = (ArticleEntity) articleDao.getEntity(basicId);
		String contentModelTableName = null;
		int ccmi = article.getColumn().getColumnContentModelId(); // 内容模型编号
		if (ccmi > 0) {
			ContentModelEntity contentModel = (ContentModelEntity) contentModelBiz.getEntity(ccmi);
			contentModelTableName = contentModel.getCmTableName();
		}
		List temp = articleDao.getById(basicId, contentModelTableName);
		if (temp != null && temp.size() > 0) {
			return (ArticleEntity) temp.get(0);
		}
		return null;
	}

	/**
	 * 根据站点Id,栏目列表Id，栏目属性，和栏目不推荐属性查找栏目下的文章总数
	 * 
	 * @param webId
	 *            :站点id
	 * @param basicCategoryIds
	 *            :栏目列表id
	 * @param flag
	 *            :文章推荐属性
	 * @param noFlag
	 *            :文章不推荐属性
	 * @return 文章总数
	 */
	@Override
	public int getCountByColumnId(int webId, int[] basicCategoryIds, String[] flags, String[] noFlags,
			ArticleEntity article) {
		if (basicCategoryIds == null || basicCategoryIds.length == 0) {
			return 0;
		}
		return articleDao.getCountByColumnId(webId, basicCategoryIds, flags, noFlags, article);
	}

	/**
	 * 获取IBaseDao的持久化层
	 * 
	 * @return 返回持articleDao的久化对象
	 */
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return articleDao;
	}

	@Override
	public ArticleEntity getNext(int appId, int basicId, Integer categoryId) {
		// TODO Auto-generated method stub
		return articleDao.getNextOrPrevious(appId, basicId, true, categoryId);
	}

	@Override
	public ArticleEntity getPrevious(int appId, int basicId, Integer categoryId) {
		// TODO Auto-generated method stub
		return articleDao.getNextOrPrevious(appId, basicId, false, categoryId);
	}

	public int getSearchCount(ContentModelEntity contentModel, Map wherMap, int websiteId, List ids) {
		if (contentModel != null) {
			return articleDao.getSearchCount(contentModel.getCmTableName(), wherMap, websiteId, ids);
		}
		return articleDao.getSearchCount(null, wherMap, websiteId, ids);
	}

	@Override
	public List<ArticleEntity> query(int webId, int[] basicCategoryIds, String[] flags, String[] noFlags,
			String orderBy, boolean order, ArticleEntity article) {
		// TODO Auto-generated method stub
		if (article == null) {
			article = new ArticleEntity();
		}
		return articleDao.query(webId, basicCategoryIds, flags, noFlags, orderBy, order, article);
	}

	@Override
	public List<ArticleEntity> query(int categoryId, String dateTime, int appId, ArticleEntity article) {
		return articleDao.queryListByTime(categoryId, dateTime, appId, article);
	}

	public List<ArticleEntity> queryListByState(String dateTime, int appId, int articleState){
		return articleDao.queryListByState(dateTime, appId, articleState);
	}
	
	public List<ArticleEntity> queryTopHit(int appId, int count){
		return articleDao.queryTopHit(appId, count);
	}

	@Override
	public int countToday(int appId,String dateTime, String endDateTime, ArticleEntity article){
		return articleDao.countToday(appId, dateTime, endDateTime, article);
	}
	/**
	 * 根据文章标题查询文章
	 * 
	 * @param articleTitle
	 *            文章标题
	 * @param webId
	 *            应用Id
	 * @param modelCode
	 *            模块编号
	 * @return 文章集合
	 */
	public List queryListByArticleTitle(String articleTitle, int webId, ModelCode modelCode) {
		// 将文章标题截断
		String[] _articleTitle = articleTitle.split(",");
		if (_articleTitle.length > 1) {
			articleTitle = _articleTitle[1];
		} else {
			articleTitle = _articleTitle[0];
		}
		// 查询文章集合
		List<ArticleEntity> articleList = this.articleDao.queryByArticleTitle(articleTitle, webId,
				modelCode.toString());
		return articleList;
	}

	/**
	 * 根据页面栏目的id获取与其绑定的文章实体
	 * 
	 * @param basicCategoryId
	 * @return 文章实体
	 */
	@Override
	public List<ArticleEntity> queryListByColumnId(int basicCategoryId, ArticleEntity article) {
		// TODO Auto-generated method stub
		return articleDao.queryListByColumnId(basicCategoryId, article);
	}

	public List<ArticleEntity> queryListForSearch(ContentModelEntity conntentModel, Map whereMap, PageUtil page,
			int websiteId, List ids, Map orders) {
		List<ArticleEntity> articleList = new ArrayList<ArticleEntity>();
		String tableName = null;
		if (conntentModel != null) {
			tableName = conntentModel.getCmTableName();
		}
		// 查找所有符合条件的文章实体
		articleList = articleDao.queryListForSearch(tableName, whereMap, page, websiteId, ids, orders);
		return articleList;
	}

	@Override
	public List<BaseEntity> queryPageByCategoryId(int categoryId, int appId, PageUtil page, boolean _isHasChilds) {
		// TODO Auto-generated method stub
		// return articleDao.queryByView(categoryId, page);
		Integer modelId = modelBiz.getEntityByModelCode(ModelCode.CMS_COLUMN).getModelId(); // 查询当前模块编号
		List list = categoryBiz.queryChildrenCategory(categoryId, appId, modelId);
		// 分类不存在直接返回
		if (list == null || list.size() == 0) {
			return null;
		}

		// 如果是最低级栏目需要查询该栏目是否存在自定义模型，如果存在需要再关联自定义模型查询
		if (list.size() == 1) { // 最低级栏目
			ColumnEntity column = (ColumnEntity) columnBiz.getEntity(categoryId);
			if (column.getColumnContentModelId() != 0) { // 存在自定义模型
				ContentModelEntity contentModel = (ContentModelEntity) contentModelBiz
						.getEntity(column.getColumnContentModelId());
				return articleDao.queryPageByCategoryId(categoryId, null, page, contentModel.getCmTableName());
			} else {
				return articleDao.queryPageByCategoryId(categoryId, null, page, null);
			}
		} else {
			if (_isHasChilds) {
				return articleDao.queryPageByCategoryId(categoryId, list, page, null);
			} else {
				return articleDao.queryPageByCategoryId(categoryId, null, page, null);
			}
		}

	}

	/**
	 * 设置Article的持久化层
	 * 
	 * @param articleDao
	 */
	@Autowired
	public void setArticleDao(IArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	/**
	 * 显示本网站下文章列表
	 * 
	 * @param webId网站id
	 * @param page
	 *            PageUtil对象，主要封装分页的方法 <br/>
	 * @param orderBy
	 *            排序字段 <br/>
	 * @param order
	 *            排序方式true:asc false:desc <br/>
	 * @return 返回所查询的文章集合
	 */
	@Override
	@Deprecated
	public List<ArticleEntity> queryPageListByWebsiteId(int webId, PageUtil page, String orderBy, boolean order) {
		// TODO Auto-generated method stub
		return articleDao.queryPageListByWebsiteId(webId, page.getPageNo(), page.getPageSize(), orderBy, order);
	}

	/**
	 * 查询本网站下文章列表数目
	 * 
	 * @param webId网站id
	 * @return 文章条数
	 */
	@Override
	@Deprecated
	public int getCountByWebsiteId(int webId) {
		// TODO Auto-generated method stub
		return articleDao.getCountByWebsiteId(webId);
	}

	@Override
	public JSONObject queryByKey(JSONObject inJson) {
		int pageNo = inJson.getInteger("pageNo");
		int pageSize = inJson.getInteger("pageSize");
		String articleContent = inJson.getString("articleContent");
		int dateTime = inJson.getInteger("basicDateTime");
		boolean order = inJson.getBoolean("order");
		int appId = inJson.getIntValue("appId");
		
		
		String basicDateTime = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		if (dateTime == 1) { // 全部
			basicDateTime = "";
		} else if (dateTime == 2) { // 前一周
			c.add(Calendar.DATE, -7);
			Date d = c.getTime();
			basicDateTime = format.format(d);
		} else if (dateTime == 3) { // 前一月
			c.add(Calendar.MONTH, -1);
			Date d = c.getTime();
			basicDateTime = format.format(d);
		} else { // 前一年
			c.add(Calendar.YEAR, -1);
			Date d = c.getTime();
			basicDateTime = format.format(d);
		}
		
		PageHelper.startPage(pageNo, pageSize);
		List<ArticleEntity> articleList = articleDao.queryByKey( articleContent, basicDateTime, order, appId);
		PageInfo<ArticleEntity> page = new PageInfo<>(articleList);
		JSONArray array = new JSONArray();
		array.add(page);
		return JsonResult.getSuccResult(array);
	}


}