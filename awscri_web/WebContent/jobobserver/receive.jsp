<%@ include file="../env.jsp" %>
<html>
<head>
<title>AWS CRI - Job Observer Pattern : Receiving a message</title>
</head>

<body>
Received a message : <%= sqsmanager.receiveMessage() %> 
</body>

</html>