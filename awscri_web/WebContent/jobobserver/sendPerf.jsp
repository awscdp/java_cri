<%@ include file="../env.jsp" %>
<%@ page import="java.util.*" %>
<html>
<head>
<title>AWS CRI - Job Observer Pattern : Sending messages perf</title>
</head>

<body>
<% 
String param = request.getParameter("num");
int num = (param == null) ? 10 : Integer.parseInt(param);

Random rand = new Random();
long start = System.currentTimeMillis();
for(int i = 0; i < num; i++)
	sqsmanager.sendMessage(Integer.toString(rand.nextInt(100)));
long elapsed = System.currentTimeMillis() - start;
%>

Sent <%= num %> messages in <%= elapsed %> msecs

</body>
</html>