<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	request.setCharacterEncoding("UTF-8");
	String cp = request.getContextPath(); // 컨텍스트 경로 (ex./study)
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<link rel="stylesheet" href="<%=cp%>/imageTest/data/style.css" type="text/css"/>
<link rel="stylesheet" href="<%=cp%>/imageTest/data/list.css" type="text/css"/>

</head>
<body>


<br/><br/>

<table width="560" border="2" cellpadding="0" cellspacing="0" bordercolor="#d6d4a6" align="center">
<tr height="40">
	<td style="padding-left: 20px;">
	<b>이미지 게시판(Struts2)</b>
	</td>
</tr>
</table>


<table width="600" align="center" style="font-family: 돋움; font-size: 10pt;" cellpadding="0" cellspacing="10">
<tr>
	<td align="left" colspan="2" width="400">
		Total : ${dataCount } articles, ${totalPage } pages / Now page is ${currentPage}
	</td>
	<td align="right" width="200">
		<input class="btn1" type="button" value=" 게시물 등록 " onclick="javascript:location.href='<%=cp%>/image/write.do';"/><br><br>
	</td>
</tr>


<tr><td colspan="3" height="3" bgcolor="#dbdbdb"></td></tr>


<%int line = 0;
%>
<c:forEach var="dto" items="${lists }">
<%if(line == 0) {
out.print("<tr>");
}
line++;
%>

	<td align="center" width="180">
	<a href="${imagePath }/${dto.saveFileName}"><img src="${imagePath }/${dto.saveFileName}" width="180" height="180"/></a><br/>
	${dto.subject } <a href="${deletePath }?num=${dto.num}">삭제</a>
	</td>
	
<%if(line==3){
out.print("</tr>");
line=0;
}
%>
</c:forEach>


<% 
while(line > 0 && line < 3){
    out.print("<td width='180'></td>");
	line++;
	out.print("</tr>");
	}
	%>


<tr><td colspan="3" height="3" bgcolor="#dbdbdb"></td></tr>
</table>

<table align="center">

<tr>
	<td>${pageIndexList }</td>
</tr>

</table>




</body>
</html>