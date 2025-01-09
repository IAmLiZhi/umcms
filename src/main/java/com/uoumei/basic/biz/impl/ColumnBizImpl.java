
package com.uoumei.basic.biz.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uoumei.base.dao.IBaseDao;
import com.uoumei.basic.biz.IColumnBiz;
import com.uoumei.basic.dao.IColumnDao;
import com.uoumei.basic.entity.ColumnEntity;
import com.uoumei.basic.entity.ManagerSessionEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.StringUtil;
import com.sun.swing.internal.plaf.basic.resources.basic;

import com.uoumei.base.util.BasicUtil;
import com.uoumei.base.util.SpringUtil;

/**
 * 栏目业务层实现类，继承CategoryBizImpl，实现接口IColumnBiz
 * 
 */
@Service("columnBiz")
public class ColumnBizImpl extends CategoryBizImpl implements IColumnBiz {

	/**
	 * 栏目持久化层注入
	 */
	private IColumnDao columnDao;

	/**
	 * 获取 columnDao
	 * 
	 * @return columnDao
	 */
	public IColumnDao getColumnDao() {
		return columnDao;
	}

	/**
	 * 设置 columnDao
	 * 
	 * @param columnDao
	 */
	@Autowired
	public void setColumnDao(IColumnDao columnDao) {
		this.columnDao = columnDao;
	}

	@Override
	protected IBaseDao getDao() {
		return columnDao;
	}

	/**
	 * 根据站点ID查询该站点下的栏目集合
	 * 
	 * @param columnWebsiteId
	 *            站点Id
	 * @return 栏目集合
	 */
	public List<ColumnEntity> queryColumnListByWebsiteId(int columnWebsiteId) {
		return columnDao.queryColumnListByWebsiteId(columnWebsiteId);
	}

	public List<ColumnEntity> queryChild(int categoryCategoryId, int columnWebsiteId, Integer modelId, Integer size) {
		return columnDao.queryColumnByCategoryIdAndWebsiteIdAndModelId(categoryCategoryId, columnWebsiteId, modelId,
				size);
	}

	public List<ColumnEntity> queryAll(int appId, int modelId) {
		return columnDao.queryByAppIdAndModelId(appId, modelId);
	}
	
	//use this
	public List<ColumnEntity> queryByColumnIds(int appId,int[] columnIds){
		return columnDao.queryByAppIdAndColumnIds(appId, columnIds);
	}


	/**
	 * 通过栏目的站点ID查询该站点下的栏目的父栏目Id为categoryCategoryId子栏目
	 * 通过递归查询将父栏目ID为categoryCategoryId的子栏目集合和他对应在同一节点树的父级栏目的集合全部查询装入List中
	 * 
	 * @param categoryCategoryId
	 *            栏目ID
	 * @param list
	 *            栏目集合
	 * @param columnWebsiteId
	 *            站点ID
	 */
	private void queryExpansionColumnListByWebsiteId(int categoryCategoryId, List<ColumnEntity> list,
			int columnWebsiteId) {
		List<ColumnEntity> queryChildList = new ArrayList<ColumnEntity>();
		queryChildList = columnDao.queryColumnByCategoryIdAndWebsiteIdAndModelId(categoryCategoryId, columnWebsiteId,
				null, null);
		for (int i = 0; i < queryChildList.size(); i++) {
			list.add(queryChildList.get(i));
		}

		if (categoryCategoryId != 0) {
			ColumnEntity columnEntity = (ColumnEntity) (columnDao.getEntity(categoryCategoryId));
			queryExpansionColumnListByWebsiteId(columnEntity.getCategoryCategoryId(), list, columnWebsiteId);
		}
	}

	/**
	 * 通过栏目ID查询该栏目同级栏目
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return 同级栏目集合
	 */
	public List<ColumnEntity> querySibling(int columnId, Integer size) {
		ColumnEntity columnEntity = (ColumnEntity) columnDao.getEntity(columnId);
		List<ColumnEntity> list = new ArrayList<ColumnEntity>();
		if (columnEntity != null) {

			list = columnDao.queryColumnByCategoryIdAndWebsiteIdAndModelId(columnEntity.getCategoryCategoryId(),
					columnEntity.getAppId(), null, size);
		}
		return list;
	}

	/**
	 * 通过栏目ID查询顶级栏目的同级栏目
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return 顶级同级栏目集合
	 */
	public List<ColumnEntity> queryTopSiblingListByColumnId(int columnId, Integer size) {
		ColumnEntity columnEntity = (ColumnEntity) columnDao.getEntity(columnId);
		List<ColumnEntity> list = null;
		if (columnEntity != null) {
			list = querySibling(columnEntity.getCategoryCategoryId(), size);
		}
		return list;
	}

	/**
	 * 根据栏目Id查询栏目的子栏目集
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return 子栏目集合
	 */
	public List<ColumnEntity> queryChildListByColumnId(int columnId, Integer size) {
		ColumnEntity columnEntity = (ColumnEntity) columnDao.getEntity(columnId);
		List<ColumnEntity> list = null;
		if (columnEntity != null) {
			list = columnDao.queryColumnByCategoryIdAndWebsiteIdAndModelId(columnEntity.getCategoryId(),
					columnEntity.getAppId(), null, size);
		}
		return list;
	}

	/**
	 * 根据栏目ID查询其子栏目ID集合
	 * 
	 * @param categoryId
	 *            栏目ID
	 * @return 子栏目ID集合
	 */
	public int[] queryChildIdsByColumnId(int categoryId, int appId) {
		List<Integer> ids = columnDao.queryColumnChildIdList(categoryId, appId);
		int[] ret = new int[ids.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = ids.get(i).intValue();
		return ret;
	}


	/**
	 * 用递归通过栏目ID查询栏目的父级栏目,将查询结果装入List集合中
	 * 
	 * @param columnId
	 *            栏目ID
	 * @param list
	 *            父级栏目集合
	 */
	private void queryColumnParent(ColumnEntity column, List<ColumnEntity> list) {
		if (column.getCategoryCategoryId() != 0) {
			ColumnEntity columnEntity = (ColumnEntity) columnDao.getEntity(column.getCategoryCategoryId());
			list.add(columnEntity);
			queryColumnParent(columnEntity, list);
		}
	}

	/**
	 * 通过栏目ID查询栏目对应节点路径上的父级栏目集合
	 * 
	 * @param columnId
	 *            栏目ID
	 * @return 栏目及其父级栏目集合
	 */
	public List<ColumnEntity> queryParentColumnByColumnId(int columnId) {
		List<ColumnEntity> list = null;
		ColumnEntity columnEntity = (ColumnEntity) columnDao.getEntity(columnId);
		if (columnEntity != null) {
			list = new ArrayList<ColumnEntity>();
			// 递归的查询所有父节点
			queryColumnParent(columnEntity, list);
		}
		return list;
	}

	/**
	 * 根据站点Id查询该站点下的栏目的父栏目Id为categoryCategoryId的子栏目集合数目统计
	 * 
	 * @param categoryCategoryId
	 *            父栏目ID
	 * @param columnWebsiteId
	 *            站点ID
	 * @return 子栏目统计数目
	 */
	public int queryColumnChildListCountByWebsiteId(int categoryCategoryId, int columnWebsiteId) {
		return columnDao.queryColumnChildListCountByWebsiteId(categoryCategoryId, columnWebsiteId);
	}
	
	/**
	 * 组织栏目链接地址
	 * @param request
	 * @param column 栏目实体
	 */
	public static void columnPath(ColumnEntity column,String file) {
		IColumnBiz columnBiz=  SpringUtil.getBean(IColumnBiz.class);
		String columnPath = "";
		String delFile = "";
		//修改栏目路径时，删除已存在的文件夹
		column = (ColumnEntity) columnBiz.getEntity(column.getCategoryId());
		delFile = file + column.getColumnPath();
		if(!StringUtil.isBlank(delFile)){
			File delFileName = new File(delFile);
			delFileName.delete();
		}
		//若为顶级栏目，则路径为：/+栏目ID
		if(column.getCategoryCategoryId() == 0){
			column.setColumnPath(File.separator+column.getCategoryId());
			file = file + File.separator + column.getCategoryId();
		} else {
			List<ColumnEntity> list = columnBiz.queryParentColumnByColumnId(column.getCategoryId());
			if(!StringUtil.isBlank(list)){
				String temp = "";
				for(int i = list.size()-1; i>=0; i--){
					ColumnEntity entity = list.get(i);
					columnPath = columnPath + File.separator + entity.getCategoryId();
					temp = temp + File.separator + entity.getCategoryId();
				}
				column.setColumnPath(columnPath + File.separator + column.getCategoryId());
				file = file + temp + File.separator + column.getCategoryId();
			}
		}
		columnBiz.updateEntity(column);
		//生成文件夹
		File fileName = new File(file);
        fileName.mkdir();
	}

	@Override
	public void save(ColumnEntity column, int modelCode,int CategoryManagerId,String file, int appId) {
		//--zzq
		column.setCategoryAppId(appId);
		column.setAppId(appId);
		column.setCategoryManagerId(CategoryManagerId);
		column.setCategoryDateTime(new Timestamp(System.currentTimeMillis()));
		column.setCategoryModelId(modelCode);
		if(column.getColumnType()==ColumnEntity.ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()){
			column.setColumnListUrl(null);
		}
		super.saveCategory(column);
		ColumnBizImpl.columnPath(column,file);
	}

	@Override
	public void delete(int[] columns, int appId) {
		for(int i=0;i<columns.length;i++){
			if(this.getEntity(columns[i]) != null){
				super.deleteCategory(columns[i], appId);
			}
		}
		
	}

	@Override
	public void update(ColumnEntity column, int modelCode, int managerId, String file, int appId) {
		// 获取站点ID --zzq
		int websiteId = appId;
		// 检测栏目信息是否合法

		// 若栏目管理属性为单页，则栏目的列表模板地址设为Null
		if (column.getColumnType() == ColumnEntity.ColumnTypeEnum.COLUMN_TYPE_COVER.toInt()) {
			column.setColumnListUrl(null);
		}
		column.setCategoryManagerId(managerId);
		column.setAppId(websiteId);
		super.updateCategory(column);
		ColumnBizImpl.columnPath(column, file);
		// 查询当前栏目是否有子栏目，
		List<ColumnEntity> childList = this.queryChild(column.getCategoryId(), websiteId, modelCode, null);
		if (childList != null && childList.size() > 0) {
			// 改变子栏目的顶级栏目ID为当前栏目的父级栏目ID
			for (int i = 0; i < childList.size(); i++) {
				childList.get(i).setCategoryCategoryId(column.getCategoryId());
				childList.get(i).setCategoryManagerId(managerId);
				childList.get(i).setAppId(websiteId);
				super.updateCategory(childList.get(i));
				// 组织子栏目链接地址
				ColumnBizImpl.columnPath(childList.get(i), file);
			}
		}

	}

}