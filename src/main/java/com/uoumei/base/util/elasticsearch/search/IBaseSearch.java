package com.uoumei.base.util.elasticsearch.search;

import java.io.Serializable;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;


@NoRepositoryBean
public interface IBaseSearch<T, ID extends Serializable>
		extends ElasticsearchRepository<T, ID>, PagingAndSortingRepository<T, ID> {
}
