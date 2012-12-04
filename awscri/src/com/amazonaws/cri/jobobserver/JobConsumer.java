package com.amazonaws.cri.jobobserver;

import com.amazonaws.cri.AwsCriContext;
import com.amazonaws.cri.SQSManager;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class JobConsumer {

	private SQSManager manager;
	private double elapsed;
	
	public JobConsumer() {
		manager = (SQSManager)AwsCriContext.get("sqsmanager");
		elapsed = 0;
	}
	
	public void processMessage() throws InterruptedException {
		int time = Integer.parseInt(manager.receiveMessage());
		Thread.sleep(time);
		elapsed += time;
	}
	
	public double getElapsed() {
		return elapsed;
	}
		
	public static void main(String[] args) throws Exception {
		int counter = 10000;
		if (args.length > 1) counter = Integer.parseInt(args[0]);
		
		JobConsumer consumer = new JobConsumer();
		for(int i = 0; i < counter; i++) {
			consumer.processMessage();
			if (i % 1000 == 0) System.out.println("Elapsed time : " + consumer.getElapsed());
		}
	}
}
