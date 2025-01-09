package com.uoumei.base.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONObject;
import com.uoumei.base.biz.IBaseBiz;
import com.uoumei.base.entity.BaseEntity;

import com.uoumei.base.util.BaseUtil;

/**
 * 清理文件
 *
 */
public class FileExtUtil {
	
	/**
	 * 文件后缀
	 */
	private static String fileSuffix = "[/|\\\\]upload.*?\\.(rmvb|mpga|mpg4|mpeg|docx|xlsx|pptx|jpeg|[a-z]{3})";

	/**
	 * @Title: del
	 * @Description: TODO(查找出json数据里面的附件路径，并执行删除)
	 * @param json
	 *            通常是业务实体转换之后的json字符串
	 */
	public static void del(String json) {
		Pattern pattern = Pattern.compile(fileSuffix);
		Matcher matcher = pattern.matcher(json);
		while (matcher.find()) {
			try {
				FileUtils.forceDelete(new File(BasicUtil.getRealPath(matcher.group())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: del
	 * @Description: TODO(查找出list数据里面的附件路径，并执行删除)
	 * @param list
	 *            对象集合
	 */
	public static void del(List<?> list) {
		String json = "";
		for (Object entity : list) {
			json = JSONObject.toJSONString(entity);
			FileExtUtil.del(json);
		}
	}

	/**
	 * @Title: del
	 * @Description: TODO(查找出实体数据里面的附件路径，并执行删除)
	 * @param entity
	 *            实体对象
	 */
	public static void del(BaseEntity entity) {
		String json = JSONObject.toJSONString(entity);
		FileExtUtil.del(json);
	}

}
