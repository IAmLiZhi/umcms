
package com.uoumei.cms.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IBasicBiz;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.cms.entity.ArticleEntity;
import com.uoumei.mdiy.entity.ContentModelEntity;
import com.uoumei.util.PageUtil;

/**
 * 
 * 文章管理业务处理层 || 继承IBasicBiz业务处理层
 */
public interface IArticleBiz extends IBasicBiz {

	/**
	 * @para webId 网站编号
	 * @param basicCategoryId
	 *            栏目编号
	 * @param flag
	 *            文章属性
	 * @param noFlag
	 *            文章不存在的属性
	 * @param article
	 *            文件实体
	 * @return
	 */
	int count(int webId, int[] basicCategoryId, String[] flags, String[] noFlags, ArticleEntity article);

	/**
	 * 通过视图表来查询文章总数
	 * 
	 * @param categoryId
	 *            分类编号
	 * @return 总数
	 * @see IArticleBiz.count
	 */
	@Deprecated
	public int countByCategoryId(int categoryId);


	/**
	 * 通过分类id获取文章内容
	 * 
	 * @param categoryId
	 *            分类编号
	 * @return
	 */
	@Deprecated
	public ArticleEntity getByCategoryId(int categoryId);

	/**
	 * 通过视图表来查询文章总数
	 * 
	 * @param basicId
	 *            文章编号
	 */
	public ArticleEntity getById(int basicId);


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
	 * @see IArticleBiz.count
	 */
	@Deprecated
	public int getCountByColumnId(int webId, int[] basicCategoryIds, String[] flags, String[] noFlags, ArticleEntity article);


	/**
	 * 查找basicId下一篇文章
	 * 
	 * @param appId
	 *            应用编号
	 * @param basicId
	 *            文章编号
	 * @return
	 */
	public ArticleEntity getNext(int appId, int basicId, Integer categoryId);

	/**
	 * 查找basicId上一篇文章
	 * 
	 * @param appId
	 *            应用编号
	 * @param basicId
	 *            文章编号
	 * @return
	 */
	public ArticleEntity getPrevious(int appId, int basicId, Integer categoryId);

	/**
	 * 高级查询接口，主要提供给有自定义模型的栏目，返回總數
	 * 
	 * @param contentModel
	 *            自定义模型
	 * @param whereMap
	 *            條件
	 * @param appId
	 *            appId 應用編號
	 * @param ids
	 *            子类id
	 * @return 记录数量
	 * @see IArticleBiz.count
	 */
	@Deprecated
	public int getSearchCount(ContentModelEntity contentModel, Map whereMap, int appId, List ids);

	/**
	 * 文章查询
	 * 
	 * @para webId 网站编号
	 * @param basicCategoryIds
	 *            栏目编号集合
	 * @param flag
	 *            文章属性
	 * @param noFlag
	 *            文章不存在的属性
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            true 升序 false 降序 排序方式
	 * @param article
	 *            文章实体，便于扩展查询
	 * @return 文章集合
	 */
	List<ArticleEntity> query(int webId, int[] basicCategoryIds, String[] flags, String[] noFlags, String orderBy,
			boolean order, ArticleEntity article);

	/**
	 * 根据分类与时间查询文章列表
	 * 
	 * @param categoryId
	 *            分类编号
	 * @param dateTime
	 *            时间
	 * @param appId
	 *            应用编号
	 * @return 文章
	 */
	@Deprecated
	public List<ArticleEntity> query(int categoryId, String dateTime, int appId, ArticleEntity article);
	
	public List<ArticleEntity> queryListByState(String dateTime, int appId, int articleState);
	public List<ArticleEntity> queryTopHit(int appId, int count);
	public int countToday(int appId,String dateTime, String endDateTime, ArticleEntity article);


	/**
	 * 根据页面栏目的id获取与其绑定的文章实体
	 * 
	 * @param basicCategoryId
	 * @return 文章实体
	 */
	@Deprecated
	public List<ArticleEntity> queryListByColumnId(int basicCategoryId, ArticleEntity article);




	/**
	 * 高级查询接口，主要提供给有自定义模型的栏目，
	 * 
	 * @param conntentModel
	 *            自定义模型
	 * @param whereMap
	 *            條件
	 * @param page
	 *            分頁
	 * @param appId
	 *            應用編號
	 * @param ids
	 *            子类id
	 * @return 记录集合
	 */
	@Deprecated
	public List<ArticleEntity> queryListForSearch(ContentModelEntity conntentModel, Map whereMap, PageUtil page,
			int appId, List ids, Map orders);

	/**
	 * 通过视图表来查询文章列表
	 * 
	 * @param categoryId
	 *            分类编号
	 * @param page
	 *            分頁
	 * @param _isHasChilds
	 *            ture:取categoryId下面的子栏目 false:只取categoryId栏目
	 * @return 文章列表
	 */
	@Deprecated
	public List<BaseEntity> queryPageByCategoryId(int categoryId, int appId, PageUtil page, boolean _isHasChilds);


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
	@Deprecated
	public List<ArticleEntity> queryPageListByWebsiteId(int webId, PageUtil page, String orderBy, boolean order);
	/**
	 * 查询本网站下文章列表数目
	 * 
	 * @param webId网站id
	 * @return 文章条数
	 */
	@Deprecated
	public int getCountByWebsiteId(int webId);
	
	/**
	 * 查询所有文章，支持模糊查询
	 * @param pageNo
	 * @param pageSize
	 * @param articlecontent	文章内容
	 * @param basicDateTime	发布时间
	 * @param order	是否排序,true 升序,false 降序
	 * @return
	 */
	public JSONObject queryByKey(JSONObject inJson);

}