AWS Cloud Reference Implementation for Java (java_cri)
========

java_cri is targeting to cover Java implementations for AWS Cloud Design Pattern [(cdp)](http://en.clouddesignpattern.org).
It covers following design patterns.
- Session replication

If you are interested only running, please follow steps below.

## Prerequisites
- ami-128e3613

## Deploying

```javascript
1. Launch two ec2 instances from AMI.
	a. Logon as ec2-user and specify credentials in "/home/ec2-user/env.sh".
	b. Configure AwsCri.properties and specify DynamoDB endpoint.
		...
		dynamodbEndPoint=https://dynamodb.ap-northeast-1.amazonaws.com
		...
	c. Restart each ec2 instance.

2. Create an ELB and register ec2 instances with 
	a. HTTP port	: 80
	b. ping URI	: /index.jsp
	c. CookieName	: JSESSIONID
 
3. Access http://<elb-host>/awscri_web/statesharing/counter.jsp

4. You will see "_sessions" table gets created and session objects are stored as binary data.
```

If you are interested building from scratch, please follow steps below.

## Prerequisites

```javascript
- Java Platform, Standard Edition 6 (Java SE 6)
- AWS SDK 1.3.26 
- Apache Ant 1.8.x
- Apache Tomcat 7.0.x
```

## Building

```javascript
Define environment variables :
% set %AWS_SDK%=c:\aws-java-sdk-1.3.x
% set %CATALINA_HOME%=c:\apache-tomcat-7.0.x

Compile :
% cd <awscri>
% ant
% cd <awscri_web>
% ant

Artifacts :
<awscri>/dist/awscri.jar
<awscri_web>/dist/awscri_web.war
```

## License

(The MIT License)

Copyright (c) 2011-2012 Teppei Yagihashi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to
deal in the Software without restriction, including without limitation the
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
IN THE SOFTWARE.
