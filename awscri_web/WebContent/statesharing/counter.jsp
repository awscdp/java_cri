<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>AWS CRI - State Sharing Pattern : Counter</title>
<link rel="stylesheet" href="../styles/styles.css" type="text/css" media="screen">
</head>

<body>
<%@ include file="../env.jsp" %>
<% 
int counter = (session.getAttribute("counter") == null ? 1 : ((Integer)session.getAttribute("counter")).intValue()+1);
session.setAttribute("counter", new Integer(counter));
%>
Counter = <%= counter %> for sessionId = <%= session.getId() %> 
</body>

</html>