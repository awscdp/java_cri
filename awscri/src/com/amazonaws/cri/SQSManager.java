package com.amazonaws.cri;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sqs.*;
import com.amazonaws.services.sqs.model.*;

public class SQSManager {
	private AmazonSQSClient client;
	
	private String endPoint;
	private String accountId;
	private String queue;
	private String url;
	
	private SendMessageRequest sreq;
	private ReceiveMessageRequest rreq;
	private DeleteMessageRequest dreq;
	
	public SQSManager() throws IOException {
		if (AwsCriContext.getProperty("sqs").equals("true")) {
			endPoint = AwsCriContext.getProperty("sqsEndPoint");
			accountId = AwsCriContext.getProperty("accountId");
			queue = AwsCriContext.getProperty("queue");

			client = new AmazonSQSClient();
			client.setEndpoint(endPoint);

			CreateQueueRequest request = new CreateQueueRequest();
			request.setQueueName(queue);
			client.createQueue(request);

			url = endPoint + "/" + accountId + "/" + queue;

			sreq = new SendMessageRequest(url, "hello world!");
			rreq = new ReceiveMessageRequest(url);
			// Only retrieve one message at a time
			rreq.setMaxNumberOfMessages(new Integer(1));
			dreq = new DeleteMessageRequest();
			dreq.setQueueUrl(url);
		}
	}
	
	public String getEndPoint() {
		return AwsCriContext.getProperty("sqsEndPoint");
	}
	
	public void sendMessage(String msg) {
		if (msg == null) return;
		client.sendMessage(sreq.withMessageBody(msg));
	}
	
	public String receiveMessage() throws InterruptedException {
		ReceiveMessageResult rresult;
		for(;;) {
			rresult = client.receiveMessage(rreq);
			if (rresult.getMessages().size() != 0) {
				dreq.setReceiptHandle(rresult.getMessages().get(0).getReceiptHandle());
				client.deleteMessage(dreq);
				break;
			}
			// Need to fix this when true event handling is available with WebSocket.
			Thread.sleep(1000);
		}
		return rresult.getMessages().get(0).getBody();
	}	
}
