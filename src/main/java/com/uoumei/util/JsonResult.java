package com.uoumei.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonResult implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public static final String Name_Code = "code";
	public static final String Name_Msg = "msg";
	public static final String Name_Data = "data";
	
	public static final Integer Code_Succ = 0;
	public static final Integer Code_LoginErr = 1;
	public static final Integer Code_ShopErr = 2;
	public static final Integer Code_PermErr = 3;
	public static final Integer Code_ParamErr = 5;
	public static final Integer Code_SvcErr = 8;
	public static final Integer Code_Err = 9;

	public static JSONObject getParamErrResult(String msg){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, Code_ParamErr);
		rs.put(Name_Msg, msg);
		return rs;
	}
	public static JSONObject getSvcErrResult(String msg){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, Code_SvcErr);
		rs.put(Name_Msg, msg);
		return rs;
	}
	public static JSONObject getErrResult(String msg){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, Code_Err);
		rs.put(Name_Msg, msg);
		return rs;
	}
	public static JSONObject getErrResult(int code, String msg){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, code);
		rs.put(Name_Msg, msg);
		return rs;
	}
	public static JSONObject getSuccResult(){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, Code_Succ);
		return rs;
	}
	public static JSONObject getSuccResult(Object obj){
		JSONObject rs = new JSONObject();
		rs.put(Name_Code, Code_Succ);
		rs.put(Name_Data, obj);
		return rs;
	}
	public static Boolean isError(JSONObject jobj){
		if(null == jobj)
			return true;
		Integer code = jobj.getInteger(Name_Code);
		if(null == code)
			return true;
		if(0 != code)
			return true;
		return false;
	}
	public static Integer getCode(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getInteger(Name_Code);
	}
	public static String getMsg(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getString(Name_Msg);
	}
	public static Boolean getBooleanData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getBoolean(Name_Data);
	}
	public static String getStrData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getString(Name_Data);
	}
	public static Integer getIntegerData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getInteger(Name_Data);
	}
	public static Long getLongData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getLong(Name_Data);
	}
	public static Object getObjData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.get(Name_Data);
	}
	public static JSONObject getJSONObjData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getJSONObject(Name_Data);
	}
	public static JSONArray getJSONArrayData(JSONObject jobj){
		if(null == jobj)
			return null;
		return jobj.getJSONArray(Name_Data);
	}
}
