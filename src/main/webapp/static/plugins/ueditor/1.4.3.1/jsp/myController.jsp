<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
	
    pageEncoding="UTF-8"%>
      <%@ page import="com.uoumei.ueditor.MsUeditorActionEnter" %>
<%@ page trimDirectiveWhitespaces="true" %>


<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	String jsonConfig = request.getParameter("jsonConfig");
	String rootPath = application.getRealPath( "/" );
	System.out.println(jsonConfig+"========================================"+rootPath);
	out.write( new MsUeditorActionEnter( request, rootPath,jsonConfig).exec() );
%>