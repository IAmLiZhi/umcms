
package com.uoumei.basic.biz.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.ICategoryBiz;
import com.uoumei.basic.dao.ICategoryDao;
import com.uoumei.basic.entity.CategoryEntity;
import com.uoumei.parser.IParserRegexConstant;
import com.uoumei.util.FileUtil;
import com.uoumei.util.PageUtil;
import com.uoumei.util.StringUtil;

import com.uoumei.base.util.BaseUtil;
import com.uoumei.base.util.BasicUtil;

/**
 * 类别业务层实现类，继承IBaseBiz，实现ICategoryBiz接口
 * 
 */
@Service("categoryBiz")
public class CategoryBizImpl extends BaseBizImpl implements ICategoryBiz {

	/**
	 * 注入类别持久化层
	 */
	private ICategoryDao categoryDao;

	@Override
	public int count(CategoryEntity category) {
		// TODO Auto-generated method stub
		return categoryDao.count(category);
	}

	/**
	 * 递归返回生成静态页面的路径
	 * @param categoryId
	 * @return
	 */
    private String getGenerateFilePath(int categoryId,String categoryIds,int appId){
    	CategoryEntity category = (CategoryEntity) categoryDao.getEntity(categoryId);
    	int parentId = category.getCategoryCategoryId();
    	if (parentId != 0) {
    		categoryIds = parentId+File.separator + categoryIds;
    		return getGenerateFilePath(parentId, categoryIds, appId);
    	}else{	
    		//--zzq
    	    String path = IParserRegexConstant.HTML_SAVE_PATH+File.separator+appId+File.separator+categoryIds;
    	    return path;
    	}
    }
    
	@Override
	public void deleteCategory(int categoryId,int appId) {
		// TODO Auto-generated method stub
		CategoryEntity category = (CategoryEntity) categoryDao.getEntity(categoryId);
		//删除父类
		if(category != null){
			//删除生成的html文件（递归方法获得文件路径）
			FileUtil.delFolders(BaseUtil.getRealPath(getGenerateFilePath(categoryId, categoryId+"", appId)));
			category.setCategoryParentId(null);
			List<CategoryEntity> childrenList = categoryDao.queryChildren(category);
			int[] ids = new int[childrenList.size()];
			for(int i = 0; i < childrenList.size(); i++){
				//删除子类
				ids[i] = childrenList.get(i).getCategoryId();
			}
			categoryDao.delete(ids);
			categoryDao.deleteEntity(categoryId);
			deleteEntity(categoryId);
		}
	}
	/**
	 * 获取类别持久化层
	 * 
	 * @return categoryDao 返回类别持久化层
	 */
	public ICategoryDao getCategoryDao() {
		return categoryDao;
	}

	/**
	 * 获取类别持久化层
	 * 
	 * @return categoryDao 返回类别持久话层
	 */
	@Override
	protected IBaseDao getDao() {
		// TODO Auto-generated method stub
		return categoryDao;
	}


	@Override
	public BaseEntity getEntity(BaseEntity entity) {
		// TODO Auto-generated method stub
		CategoryEntity category = (CategoryEntity) super.getEntity(entity);
		if(category == null) {
			return null;
		}
		// 查询所有的子分类
		List<CategoryEntity> childs = categoryDao.query(category);
		resetChildren(category, childs);
		return category;

	}


	@Override
	public BaseEntity getEntity(int id) {
		// TODO Auto-generated method stub
		CategoryEntity category = (CategoryEntity) super.getEntity(id);
		if (category != null) {
			// 查询所有的子分类
			List<CategoryEntity> childs = categoryDao.queryChildren(category);
			resetChildren(category, childs);
		}
		return category;
	}


	@Override
	public List query(BaseEntity entity) {
		List list = super.query(entity);
		List childList = new ArrayList();
		childList.addAll(list);
		for (int i=0;i<list.size();i++) {
			CategoryEntity c = (CategoryEntity)list.get(i); 
			resetChildren(c, childList);
		}
		return list;
	}

	@Override
	public List<CategoryEntity> queryBatchCategoryById(List<Integer> listId) {
		// TODO Auto-generated method stub
		return categoryDao.queryBatchCategoryById(listId);
	}

	@Override
	public List<CategoryEntity> queryByAppIdOrModelId(Integer appId, Integer modelId) {
		// TODO Auto-generated method stub
		return categoryDao.queryByAppIdOrModelId(appId, modelId);
	}

	
	
	/**
	 * 根据字典查询机构
	 * @param category
	 * @return
	 */
	public List queryByDictId(CategoryEntity category) {
		return categoryDao.queryByDictId(category);
	}



	@Override
	public List queryByPageList(CategoryEntity category, PageUtil page, String orderBy, boolean order) {
		// TODO Auto-generated method stub
		return categoryDao.queryByPageList(category, page, orderBy, order);
	}

	@Override
	public List<CategoryEntity> queryChildrenCategory(int categoryId, int appId, int modelId) {
		// TODO Auto-generated method stub
		CategoryEntity category = new CategoryEntity();
		category.setCategoryAppId(appId);
		category.setCategoryModelId(modelId);
		category.setCategoryId(categoryId);
		
		return categoryDao.queryChildren(category);
	}

	@Override
	public synchronized int[] queryChildrenCategoryIds(int categoryId, int appId, int modelId) {
		// TODO Auto-generated method stub
		CategoryEntity category = new CategoryEntity();
		category.setCategoryAppId(appId);
		category.setCategoryModelId(modelId);
		category.setCategoryId(categoryId);
		List<CategoryEntity> list = categoryDao.queryChildren(category);
		int[] ids = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			CategoryEntity _category = list.get(i);
			ids[i] = _category.getCategoryId();
		}
		return ids;
	}

	@Override
	public List<CategoryEntity> queryChilds(CategoryEntity category) {
		// TODO Auto-generated method stub
		return categoryDao.queryChilds(category);
	}
	
	private void resetChildren(CategoryEntity category, List<CategoryEntity> chrildrenCategoryList) {
		for (CategoryEntity c : chrildrenCategoryList) {
			if (c.getCategoryCategoryId() == category.getCategoryId() && !category.getChildrenCategoryList().contains(c)) {
				category.getChildrenCategoryList().add(c);
				resetChildren(c, chrildrenCategoryList);
			}
		}
	}
	@Override
	public int saveCategory(CategoryEntity categoryEntity) {
		// TODO Auto-generated method stub
		if(categoryEntity.getCategoryCategoryId()>0) {
			CategoryEntity category = (CategoryEntity)this.getEntity(categoryEntity.getCategoryCategoryId());
			if(StringUtil.isBlank(category.getCategoryParentId())) {
				categoryEntity.setCategoryParentId(categoryEntity.getCategoryCategoryId()+"");
			} else {
				categoryEntity.setCategoryParentId(category.getCategoryParentId()+","+categoryEntity.getCategoryCategoryId());
			}
		}
		categoryDao.saveEntity(categoryEntity);
		return saveEntity(categoryEntity);
	}
	
	@Autowired
	public void setCategoryDao(ICategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}
	

	@Override
	public void updateCategory(CategoryEntity categoryEntity) {
		// TODO Auto-generated method stub
		if(categoryEntity.getCategoryCategoryId()>0) {
			CategoryEntity category = (CategoryEntity)this.getEntity(categoryEntity.getCategoryCategoryId());
			if(StringUtil.isBlank(category.getCategoryParentId())) {
				categoryEntity.setCategoryParentId(categoryEntity.getCategoryCategoryId()+"");
			} else {
				categoryEntity.setCategoryParentId(category.getCategoryParentId()+","+categoryEntity.getCategoryCategoryId());
			}
		}
		categoryDao.updateEntity(categoryEntity);
		updateEntity(categoryEntity);
	}
}