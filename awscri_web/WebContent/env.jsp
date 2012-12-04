<%@ page import="com.amazonaws.cri.*" %>
<jsp:useBean id="sqsmanager" type="com.amazonaws.cri.SQSManager" scope="application"/>
<jsp:useBean id="dynamodbmanager" type="com.amazonaws.cri.DynamoDBManager" scope="application"/>

<div id="content" class="container">
	<div class="section grid grid5 dynamodb">
		<h2>Amazon DynamoDB</h2>
		<ul>
			<li><jsp:getProperty name="dynamodbmanager" property="endPoint"/></li>
		</ul>
	</div>

	<div class="section grid grid5 sqs">
		<h2>Amazon SQS</h2>
		<ul>
			<li><jsp:getProperty name="sqsmanager" property="endPoint"/></li>
		</ul>
	</div>

	<div class="section grid grid5 gridlast ec2">
		<h2>Amazon EC2 Instances:</h2>
		<ul><li></li></ul>
	</div>
</div>
