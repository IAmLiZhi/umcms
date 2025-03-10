
package com.uoumei.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * json格式通用处理类
 */
public class JsonUtil {

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString JSON 对象字符
	 * @param pojoCalss 对象class * 
	 * @return 返回java对象
	 */
	public static Object getObject4JsonString(String jsonString, Class<?> pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		pojo = JSONObject.toJavaObject(jsonObject, pojoCalss);
		return pojo;
	}

	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString json HASH表达式
	 * @return 返回map数组
	 */
	public static Map<String,Object> getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		Iterator<String> keyIter = jsonObject.keySet().iterator();
		String key;
		Object value;
		Map<String,Object> valueMap = new HashMap<String,Object>();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}
		return valueMap;
	}

	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString json字符串
	 * @return 返回java数组
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		return jsonArray.toArray();
	}

	/**
	 * 将一个对象转换为JSON字符串
	 * @param obj 对象
	 * @return 返回json字符串
	 */
	public static String getObjectToJsonObject(Object obj){
		return JSON.toJSON(obj).toString();
	}	
	

	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString json字符串
	 * @return 返回java字符串数字
	 */
	public static String[] getStringArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.parseArray(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.get(i).toString();
		}

		return stringArray;
	}

	/**
	 * 将一个JSON数据转换为对应的JAVA对象</br>
	 * JSON数据中键的名称必须和对应JAVA对象中bean字段的名称一致</br>
	 * @param <T> java对象值
	 * @param jsonString JSON 对象字符
	 * @param cls 对象class * 
	 * @return 返回java对象
	 */
	public static <T> T getJsonToObject(String jsonString, Class<T> cls) {
		return JSONObject.parseObject(jsonString,cls);
	}
	
	/**
	 * 从json对象集合表达式中得到一个java对象集合</br>
	 * JSON数据中键的名称必须和对应JAVA对象中bean字段的名称一致</br>
	 * 
	 * @param <T> 对象值
	 * @param jsonString json字符串
	 * @param cls  JAVA Bean对象
	 * @return JAVA bean对象集合list
	 */
	public static <T> List<T> queryJsonToList(String jsonString,Class<T> cls) {
		List<T> list = JSONArray.parseArray(jsonString,cls);
		return list;
	}
	
}