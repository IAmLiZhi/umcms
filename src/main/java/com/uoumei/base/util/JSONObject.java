package com.uoumei.base.util;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

/**
 * 
 * 重写fastjson JSONObject类
 */
public class JSONObject extends com.alibaba.fastjson.JSONObject{

	 /**
	  * 对象转json字符串,允许输出值为null的字段
	  * @param object 对象
	  * @param filters fastjson 的SerializerFeature对象
	  * @return 字符串
	  */
	 public static final String toJSONString(Object object, SerializeFilter... filters) {
		return com.alibaba.fastjson.JSONObject.toJSONString(object, filters,SerializerFeature.WriteMapNullValue);
	 }
}
