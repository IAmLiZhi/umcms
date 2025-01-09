
package com.uoumei.basic.entity;

import com.uoumei.base.constant.e.BaseEnum;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.basic.constant.e.ModelEnum;

import java.sql.Timestamp;
import java.util.List;

/**
 * 模块实体
 */
public class ModelEntity extends BaseEntity {

    /**
     * 模块的编号
     */
    private int modelId;

    /**
     * 模块的标题
     */
    private String modelTitle;

    /**
     * 发布时间
     */
    private Timestamp modelDatetime;

    /**
     * 模块父id
     */
    private int modelModelId;

    /**
     * 链接地址
     */
    private String modelUrl;

    /**
     * 模块编码
     */
    private String modelCode;

    /**
     * 模块图标
     */
    private String modelIcon = null;
    
    /**
     *模块管理员Id
     */
    private int modelManagerId;
    /**
     * 模块排序
     * @return
     */
    private int modelSort;
    /**
     * 子功能集合，不参加表结构
     */
    private List<ModelEntity> modelChildList;
    /**
     * 是否是菜单
     */
    private Integer modelIsMenu;
    /**
     * 选中状态，不参加表结构
     */
    private int chick;
    /**
     * 父级编号集合
     * @return
     */
    private String modelParentIds;
    
    public Integer getModelIsMenu() {
		return modelIsMenu;
	}
    
	public void setModelIsMenu(Integer modelIsMenu) {
		this.modelIsMenu = modelIsMenu;
	}
	
    
    /**
     * 获取modelCode
     * @return modelCode
     */
    public String getModelCode() {
        return modelCode;
    }

    /**
     * 设置modelCode
     * @param modelCode
     */
    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    /**
     * 获取modelIcon
     * @return modelIcon
     */
    public String getModelIcon() {
        return modelIcon;
    }

    /**
     * 设置modelIcon
     * @param modelIcon
     */
    public void setModelIcon(String modelIcon) {
        this.modelIcon = modelIcon;
    }

    /**
     * 获取modelModelId
     * @return modelModelId
     */
    public int getModelModelId() {
        return modelModelId;
    }

    /**
     * 设置modelModelId
     * @param modelModelId
     */
    public void setModelModelId(int modelModelId) {
        this.modelModelId = modelModelId;
    }

    /**
     * 获取modelUrl
     * @return modelUrl
     */
    public String getModelUrl() {
        return modelUrl;
    }

    /**
     * 设置modelUrl
     * @param modelUrl
     */
    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    /**
     * 获取modelId
     * @return modelId
     */
    public int getModelId() {
        return modelId;
    }

    /**
     * 设置modelId
     * @param modelId
     */
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取modelDatetime
     * @return modelDatetime
     */
    public Timestamp getModelDatetime() {
        return modelDatetime;
    }

    /**
     * 设置modelDatetime
     * @param modelDatetime
     */
    public void setModelDatetime(Timestamp modelDatetime) {
        this.modelDatetime = modelDatetime;
    }

    /**
     * 获取modelTitle
     * @return modelTitle
     */
    public String getModelTitle() {
        return modelTitle;
    }

    /**
     * 设置modelTitle
     * @param modelTitle 
     */
    public void setModelTitle(String modelTitle) {
        this.modelTitle = modelTitle;
    }
    
    /**
     * 获取管理员id 
     * @return 返回管理员ID
     */
	public int getModelManagerId() {
		return modelManagerId;
	}
	
	/**
	 * 设置管理员id
	 * @param modelManagerId 管理员ID
	 */
	public void setModelManagerId(int modelManagerId) {
		this.modelManagerId = modelManagerId;
	}

	public int getModelSort() {
		return modelSort;
	}

	public void setModelSort(int modelSort) {
		this.modelSort = modelSort;
	}

	public List<ModelEntity> getModelChildList() {
		return modelChildList;
	}

	public void setModelChildList(List<ModelEntity> modelChildList) {
		this.modelChildList = modelChildList;
	}

	public int getChick() {
		return chick;
	}

	public void setChick(int chick) {
		this.chick = chick;
	}
	
	
	public String getModelParentIds() {
		return modelParentIds;
	}

	public void setModelParentIds(String modelParentIds) {
		this.modelParentIds = modelParentIds;
	}


	public enum IsMenu implements BaseEnum{
		NO(0),
		YES(1);
		private int id;
		IsMenu(int id){
			this.id = id;
		}
		@Override
		public int toInt() {
			// TODO Auto-generated method stub
			return this.id;
		}
	}
	
}