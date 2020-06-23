<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath(); // 컨텍스트 경로 (ex./study)
%>
&nbsp;<a href="<%=cp%>/main.action">메인</a>&nbsp;|
<a href="<%=cp%>/bbs/list.action">게시판</a>