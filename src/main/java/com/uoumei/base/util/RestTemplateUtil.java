package com.uoumei.base.util;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

/**
 * spring RestTemplate工具类
 */
public class RestTemplateUtil {
    
    private static RestTemplate restTemplate = new RestTemplate();
    
    /**
     * post请求，保存
     * @param req ServletRequest对象
     * @param url 请求地址
     * @param params 参数
     * @return 内容
     */
    public static String post(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.POST, params);
        return rss.getBody();
    }

    /**
     * get请求,查询
     * @param req ServletRequest对象
     * @param url 请求地址
     * @param params 参数
     * @return
     */
    public static String get(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.GET, params);
        return rss.getBody();
    }
    
    /**
     * 删除
     * @param req
     * @param url
     * @param params
     * @return
     */
    public static String delete(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.DELETE, params);
        return rss.getBody();
    }
    
    /**
     * 保存
     * @param req
     * @param url
     * @param params
     * @return
     */
    public static String put(ServletRequest req, String url, Map<String, ?> params) {
        ResponseEntity<String> rss = request(req, url, HttpMethod.PUT, params);
        return rss.getBody();
    }
    
    /**
     * 请求方法
     * @param req ServletRequest对象
     * @param url 请求地址
     * @param method 方法
     * @param params 参数
     * @return
     */
    private static ResponseEntity<String> request(ServletRequest req, String url, HttpMethod method, Map<String, ?> params) {
        HttpServletRequest request = (HttpServletRequest) req;
        //获取header信息
        HttpHeaders requestHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
          String key = (String) headerNames.nextElement();
          String value = request.getHeader(key);
          requestHeaders.add(key, value);
        }
        HttpEntity<String> requestEntity = new HttpEntity<String>(params != null ? JSONObject.toJSONString(params) : null, requestHeaders);
        ResponseEntity<String> rss = restTemplate.exchange(url, method, requestEntity, String.class);
        return rss;
    }
}