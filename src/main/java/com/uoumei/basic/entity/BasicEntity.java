
package com.uoumei.basic.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uoumei.base.entity.BaseEntity;
import com.uoumei.util.StringUtil;

/**
 * 基本应用实体绑定
 * 
 */
public class BasicEntity extends BaseEntity {
	/**
	 * 基本实体自增长编号
	 */
	private int basicId;

	/**
	 * 标题 长度:200
	 */
	private String basicTitle;

	/**
	 * 描述 长度:1500
	 */
	private String basicDescription;

	/**
	 * 缩略图 长度:200
	 */
	private String basicThumbnails;
	
	/**
	 * 缩略图主图，basicThumbnails可能存在很多张图片，自动取第一张为主缩略图
	 * 不参加表结构
	 */
	private String basicPic;

	
	/**
	 * 通用属性
	 */
	private String basicType;
	
	/**
	 * 不参加表结构
	 * 
	 */
	private int[] basicTypeIds;
	
	
	/**
	 * 点击次数
	 */
	private int basicHit;

	/**
	 * 发布时间
	 */
	private Timestamp basicDateTime;

	/**
	 * 更新时间
	 */
	private Date basicUpdateTime;

	/**
	 * 用户编号
	 */
	private int basicPeopleId;

	/**
	 * 排序
	 */
	private int basicSort;

	/**
	 * 所属分类编号
	 */
	private int basicCategoryId = 0;

	/**
	 * 文章所属应用
	 */
	private int basicAppId;

	/**
	 * 模块编号
	 */
	private int basicModelId;
	
	/**
	 * 评论次数
	 */
	private int basicComment;
	/**
	 * 收藏次数
	 */
	private int basicCollect;
	/**
	 * 分享次数
	 */
	private int basicShare;
	
	/**
	 * 显示属性
	 */
	private int basicDisplay;
	
	/**
	 * 删除状态
	 */
	private int basicDel;
	
	/**
	 * 扩展字段信息,不参加表结构
	 */
	private Map<Object,Object> extendsFields;

	public int getBasicModelId() {
		return basicModelId;
	}

	public void setBasicModelId(int basicModelId) {
		this.basicModelId = basicModelId;
	}

	public int getBasicAppId() {
		return basicAppId;
	}

	/**
	 * 获取 basicCategoryId
	 * 
	 * @return basicCategoryId
	 */
	public int getBasicCategoryId() {
		return basicCategoryId;
	}

	/**
	 * 获取 basicDateTime
	 * 
	 * @return basicDateTime
	 */
	public Timestamp getBasicDateTime() {
		return basicDateTime;
	}

	/**
	 * 获取 basicDescription
	 * 
	 * @return basicDescription
	 */
	public String getBasicDescription() {
		return basicDescription;
	}

	/**
	 * 获取 basicHit
	 * 
	 * @return basicHit
	 */
	public int getBasicHit() {
		return basicHit;
	}

	/**
	 * 获取 basicId
	 * 
	 * @return basicId
	 */
	public int getBasicId() {
		return basicId;
	}

	/**
	 * 获取 basicPeopleId
	 * 
	 * @return basicPeopleId
	 */
	public int getBasicPeopleId() {
		return basicPeopleId;
	}

	/**
	 * 获取 basicThumbnails
	 * 
	 * @return basicThumbnails
	 */
	public String getBasicThumbnails() {
		return basicThumbnails;
	}

	/**
	 * 获取 basicTitle
	 * 
	 * @return basicTitle
	 */
	public String getBasicTitle() {
		return basicTitle;
	}

	/**
	 * 获取 basicUpdateTime
	 * 
	 * @return basicUpdateTime
	 */
	public Date getBasicUpdateTime() {
		return basicUpdateTime;
	}

	public void setBasicAppId(int basicAppId) {
		this.basicAppId = basicAppId;
	}

	/**
	 * 设置basicCategoryId
	 * 
	 * @param basicCategoryId
	 */
	public void setBasicCategoryId(int basicCategoryId) {
		this.basicCategoryId = basicCategoryId;
	}

	/**
	 * 设置basicDateTime
	 * 
	 * @param basicDateTime
	 */
	public void setBasicDateTime(Timestamp basicDateTime) {
		this.basicDateTime = basicDateTime;
	}

	/**
	 * 设置basicDescription
	 * 
	 * @param basicDescription
	 */
	public void setBasicDescription(String basicDescription) {
		this.basicDescription = basicDescription;
	}

	/**
	 * 设置basicHit
	 * 
	 * @param basicHit
	 */
	public void setBasicHit(int basicHit) {
		this.basicHit = basicHit;
	}

	/**
	 * 设置basicId
	 * 
	 * @param basicId
	 */
	public void setBasicId(int basicId) {
		this.basicId = basicId;
	}

	/**
	 * 设置basicPeopleId
	 * 
	 * @param basicPeopleId
	 */
	public void setBasicPeopleId(int basicPeopleId) {
		this.basicPeopleId = basicPeopleId;
	}

	/**
	 * 设置basicThumbnails
	 * 
	 * @param basicThumbnails
	 */
	public void setBasicThumbnails(String basicThumbnails) {
		this.basicThumbnails = basicThumbnails;
	}

	/**
	 * 设置basicTitle
	 * 
	 * @param basicTitle
	 */
	public void setBasicTitle(String basicTitle) {
		this.basicTitle = basicTitle;
	}

	/**
	 * 设置basicUpdateTime
	 * 
	 * @param basicUpdateTime
	 */
	public void setBasicUpdateTime(Date basicUpdateTime) {
		this.basicUpdateTime = basicUpdateTime;
	}

	public int getBasicSort() {
		return basicSort;
	}

	public void setBasicSort(int basicSort) {
		this.basicSort = basicSort;
	}

	public Map<Object, Object> getExtendsFields() {
		return extendsFields;
	}

	public void setExtendsFields(Map<Object, Object> extendsFields) {
		this.extendsFields = extendsFields;
	}

	public String getBasicPic() {
		if (!StringUtil.isBlank(basicThumbnails)) {
			String[] pics = basicThumbnails.split("\\|");
			return pics[0];
		}
		return "";
	}

	public void setBasicPic(String basicPic) {
		this.basicPic = basicPic;
	}

	public int getBasicComment() {
		return basicComment;
	}

	public void setBasicComment(int basicComment) {
		this.basicComment = basicComment;
	}

	public int getBasicCollect() {
		return basicCollect;
	}

	public void setBasicCollect(int basicCollect) {
		this.basicCollect = basicCollect;
	}

	public int getBasicShare() {
		return basicShare;
	}

	public void setBasicShare(int basicShare) {
		this.basicShare = basicShare;
	}

	public String getBasicType() {
		return basicType;
	}

	public void setBasicType(String basicType) {
		this.basicType = basicType;
	}

	public int[] getBasicTypeIds() {
		if(StringUtil.isBlank(basicType)) {
			return new int[0];
		}
		return StringUtil.stringsToInts(basicType.split(","));
	}

	public void setBasicTypeIds(int[] basicTypeIds) {
		this.basicTypeIds = basicTypeIds;
	}

	public int getBasicDisplay() {
		return basicDisplay;
	}

	public void setBasicDisplay(int basicDisplay) {
		this.basicDisplay = basicDisplay;
	}

	public int getBasicDel() {
		return basicDel;
	}

	public void setBasicDel(int basicDel) {
		this.basicDel = basicDel;
	}
	
	

}