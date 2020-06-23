<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath(); // 컨텍스트 경로 (ex./study)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="<%=cp%>/data/css/style.css" type="text/css"/>

</head>
<body>
<br/><br/>
<table width="700" align="center" border="1">
<tr height="50">
	<td><tiles:insertAttribute name="header"/></td> <!-- 외부에있는 파일을 불러올때 사용 -->
</tr>

<tr height="350" valign="top">
	<td><tiles:insertAttribute name="body"/></td>
</tr>

<tr height="50">
	<td><tiles:insertAttribute name="footer"/></td>
</tr>

</table>

</body>
</html>