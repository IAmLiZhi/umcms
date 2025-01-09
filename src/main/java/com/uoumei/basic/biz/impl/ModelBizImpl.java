
package com.uoumei.basic.biz.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uoumei.base.biz.impl.BaseBizImpl;
import com.uoumei.base.dao.IBaseDao;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.biz.IModelBiz;
import com.uoumei.base.constant.e.BaseEnum;
import com.uoumei.basic.constant.e.ModelEnum;
import com.uoumei.basic.dao.IModelDao;
import com.uoumei.basic.entity.ModelEntity;

/**
 * 模块业务接口实现类
 */
@Service("modelBiz")
public class ModelBizImpl extends BaseBizImpl implements IModelBiz{

	

	@Override
	public ModelEntity getEntityByModelCode(BaseEnum modelCode){
		// TODO Auto-generated method stub
		return modelDao.getEntityByModelCode(modelCode.toString());
	}	
	
	@Override
	public ModelEntity getEntityByModelCode(String modelCode) {
		// TODO Auto-generated method stub
		return modelDao.getEntityByModelCode(modelCode);
	}

	/**
	 * 模块持久化层
	 */
    private IModelDao modelDao;
    

	/**
	 * 获取模块持久化层
	 * @return modelDao 返回模块持久化层
	 */
    public IModelDao getModelDao() {
        return modelDao;
    }

    @Autowired
    public void setModelDao(IModelDao modelDao) {
        this.modelDao = modelDao;
    }

    @Override
    protected IBaseDao getDao() {
        // TODO Auto-generated method stub
        return modelDao;
    }

	@Override
	public ModelEntity getModel(String modelType,int modelId) {
		// TODO Auto-generated method stub
		return modelDao.getModel(modelType,modelId);
	}

	@Override
	public List<BaseEntity> queryModelByRoleId(int roleId) {
		return modelDao.queryModelByRoleId(roleId);
	}

}