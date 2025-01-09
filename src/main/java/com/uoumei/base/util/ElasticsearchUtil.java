package com.uoumei.base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.uoumei.base.util.elasticsearch.bean.BaseMapping;
import com.uoumei.base.util.elasticsearch.bean.SearchBean;
import com.uoumei.base.util.elasticsearch.search.IBaseSearch;

/**
 * 
 * 搜索引擎通用工具类
 */
public class ElasticsearchUtil {

	/**
	 * 新增&更新搜索引擎数据
	 * 
	 * @param elasticsearchTemplate
	 *            搜索引擎模板对象，通常由spring提供
	 * @param id
	 *            id值
	 * @param base
	 *            mapping实体
	 */
	public static void saveOrUpdate(String id, BaseMapping base) {
		ElasticsearchTemplate elasticsearchTemplate = (ElasticsearchTemplate) SpringUtil
				.getBean(ElasticsearchTemplate.class);
		IndexQuery indexQuery = new IndexQueryBuilder().withId(id).withObject(base).build();
		elasticsearchTemplate.index(indexQuery);
	}

	/**
	 * 新增&更新搜索引擎数据
	 * 
	 * @param elasticsearchTemplate
	 *            搜索引擎模板对象，通常由spring提供
	 * @param bases
	 *            BaseMapping 对象集合
	 */
	public static void saveOrUpdate(List<BaseMapping> bases) {
		ElasticsearchTemplate elasticsearchTemplate = (ElasticsearchTemplate) SpringUtil
				.getBean(ElasticsearchTemplate.class);
		List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
		for (int i = 0; i < bases.size(); i++) {
			IndexQuery indexQuery = new IndexQueryBuilder().withId(bases.get(i).getId()).withObject(bases.get(i))
					.build();
			indexQueries.add(indexQuery);
		}
		if (indexQueries.size() > 0) {
			elasticsearchTemplate.bulkIndex(indexQueries);
		}
	}

	/**
	 * 搜索
	 * 
	 * @param baseSearch
	 *            搜索search对象
	 * @param field
	 *            搜索字段
	 * @param search
	 *            搜索bean,包含分页、关键字等搜索信息
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map search(IBaseSearch baseSearch, String field, SearchBean search) {
		MatchQueryBuilder mqb = QueryBuilders.matchQuery(field, search.getKeyword());
		Pageable pageable = new PageRequest(search.getPageNo()-1, search.getPageSize());
		SearchQuery sq = new NativeSearchQueryBuilder().withPageable(pageable).withSort(SortBuilders.fieldSort(search.getOrderBy()).order(search.getOrder().equalsIgnoreCase("asc")?SortOrder.ASC:SortOrder.DESC)).withQuery(mqb).build();
		Page p = baseSearch.search(sq);
		ElasticsearchUtil.Pager pager = new ElasticsearchUtil.Pager();
		pager.setCurrentPage(p.getNumber());
		pager.setPageSize(p.getSize());
		pager.setTotalCount(p.getTotalElements());
		pager.setTotalPage(p.getTotalPages());
		Map map = new HashMap();
		map.put("data", p.getContent());
		map.put("page", pager);
		//System.out.println(net.uoumei.base.util.JSONObject.toJSONString(p));
		return map;
	}

	/**
	 * 组织SearchQuery
	 * 
	 * @param keyword
	 *            关键字
	 * @param field
	 *            过滤字段与比重
	 * @param orderBy
	 *            排序字段
	 * @param order
	 *            排序方式
	 * @param pageNumber
	 *            当前页码
	 * @param pageSize
	 *            一页显示数量
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public static SearchQuery buildSearchQuery(String keyword, Map<String, Float> field, String orderBy,
			SortOrder order, Integer pageNumber, Integer pageSize) {
		FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery();
		Iterator keys = field.keySet().iterator();
		while (keys.hasNext()) {
			String fieldName = String.valueOf(keys.next());
			functionScoreQueryBuilder.add(
					QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery(fieldName, keyword)),
					ScoreFunctionBuilders.weightFactorFunction(1000));
		}
		// functionScoreQueryBuilder.scoreMode("sum").setMinScore(1.2F);
		// 分页参数

		Pageable pageable = new PageRequest(pageNumber, pageSize);
		SearchQuery sq = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(functionScoreQueryBuilder)
				.withSort(SortBuilders.fieldSort(orderBy).order(order.DESC)).build();
		
		return sq;
	}
	
	public static class Pager {
		
		private int currentPage = 1;
		
		private int pageSize;
		
		private int totalPage;
		
		private long totalCount;

		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public int getTotalPage() {
			return totalPage;
		}

		public void setTotalPage(int totalPage) {
			this.totalPage = totalPage;
		}

		public long getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(long totalCount) {
			this.totalCount = totalCount;
		}
		
		
		
	}


}

