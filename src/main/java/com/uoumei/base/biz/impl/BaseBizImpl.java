
package com.uoumei.base.biz.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.base.constant.e.TableEnum;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.util.PageUtil;

import com.uoumei.base.util.elasticsearch.bean.BaseMapping;

public abstract class BaseBizImpl<E extends Serializable> implements IBaseBiz {

	private IBaseDao<E> baseDao;

	protected final Logger LOG = Logger.getLogger(this.getClass());

	@Override
	public int saveEntity(BaseEntity entity) {
		return getDao().saveEntity(entity);
	}

	@Override
	public void deleteEntity(int id) {
		// TODO Auto-generated method stub
		getDao().deleteEntity(id);
	}

	@Override
	public void updateEntity(BaseEntity entity) {

		// TODO Auto-generated method stub
		getDao().updateEntity(entity);
	}

	@Override
	public List<E> queryAll() {
		// TODO Auto-generated method stub
		return getDao().queryAll();
	}

	@Override
	@Deprecated
	public List<E> queryByPage(PageUtil page, String orderBy, boolean order) {
		// TODO Auto-generated method stub
		return getDao().queryByPage(page.getPageNo(), page.getPageSize(), orderBy, order);
	}


	@Override
	@Deprecated
	public int queryCount() {
		return getDao().queryCount();
	}

	@Override
	public BaseEntity getEntity(int id) {
		return getDao().getEntity(id);
	}

	@Override
	public List queryBySQL(String table, List fields, Map wheres, Integer begin, Integer end) {
		// TODO Auto-generated method stub
		return getDao().queryBySQL(table, fields, wheres, begin, end, null);
	}

	@Override
	public int countBySQL(String table, Map wheres) {
		// TODO Auto-generated method stub
		return getDao().countBySQL(table, wheres);
	}

	@Override
	public List queryBySQL(String table, List fields, Map wheres) {
		// TODO Auto-generated method stub
		return getDao().queryBySQL(table, fields, wheres, null, null, null);
	}

	@Override
	public void updateBySQL(String table, Map fields, Map wheres) {
		// TODO Auto-generated method stub
		getDao().updateBySQL(table, fields, wheres);
	}

	@Override
	public void deleteBySQL(String table, Map wheres) {
		// TODO Auto-generated method stub
		getDao().deleteBySQL(table, wheres);
	}

	@Override
	public void insertBySQL(String table, Map fields) {
		// TODO Auto-generated method stub
		getDao().insertBySQL(table, fields);
	}

	@Override
	public void createTable(String table, Map fileds) {
		// TODO Auto-generated method stub
		getDao().createTable(table, fileds);
	}

	@Override
	public void alterTable(String table, Map fileds, String type) {
		// TODO Auto-generated method stub
		getDao().alterTable(table, fileds, type);
	}

	public void alterTable(String table, Map fileds, TableEnum type) {
		// TODO Auto-generated method stub
		getDao().alterTable(table, fileds, type.toString());
	}

	@Override
	public void dropTable(String table) {
		// TODO Auto-generated method stub
		getDao().dropTable(table);
	}

	@Override
	public Object excuteSql(String sql) {
		// TODO Auto-generated method stub
		return getDao().excuteSql(sql);
	}

	/**
	 * 不需要重写此方法，自动会
	 * 
	 * @return
	 */
	protected abstract IBaseDao<E> getDao();

	@Override
	public void saveBatch(List list) {
		getDao().saveBatch(list);
	}

	@Override
	public void delete(int[] ids) {
		getDao().delete(ids);
	}

	@Override
	public void deleteEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		getDao().deleteByEntity(entity);
	}

	@Override
	public E getEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		return getDao().getByEntity(entity);
	}

	@Override
	public List<E> query(BaseEntity entity) {
		// TODO Auto-generated method stub
		return getDao().query(entity);
	}

	@Override
	public List<BaseMapping> queryForSearchMapping(BaseMapping base) {
		// TODO Auto-generated method stub
		return getDao().queryForSearchMapping(base);
	}
	
	
}