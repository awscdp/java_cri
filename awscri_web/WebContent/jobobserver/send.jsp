<%@ include file="../env.jsp" %>
<html>
<head>
<title>AWS CRI - Job Observer Pattern : Sending a message</title>
</head>

<body>
<% 
String msg = request.getParameter("msg") == null ? "hello world!" : request.getParameter("msg");
sqsmanager.sendMessage(msg);
%>
Sent a message : <%= msg %> 
</body>

</html>