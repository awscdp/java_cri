package com.amazonaws.cri;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.model.*;

public class DynamoDBManager {
	private AmazonDynamoDBClient client;
	private long readCapacity = 1;
	private long writeCapacity = 1;
	
	public DynamoDBManager() {
		if (AwsCriContext.getProperty("dynamodb").equals("true")) {
			client = new AmazonDynamoDBClient();
			client.setEndpoint(AwsCriContext.getProperty("dynamodbEndPoint"));
			readCapacity = (AwsCriContext.getProperty("readCapacity") == null ? 1 : Long.parseLong(AwsCriContext.getProperty("readCapacity"))); 
			writeCapacity = (AwsCriContext.getProperty("writeCapacity") == null ? 1 : Long.parseLong(AwsCriContext.getProperty("writeCapacity")));
		}
	}
	
	public String getEndPoint() {
		return AwsCriContext.getProperty("dynamodbEndPoint");
	}
	
	public void createTable(String key, String tableName) {
		CreateTableRequest ctr = new CreateTableRequest()
				.withTableName(tableName)
				.withKeySchema(new KeySchema(new KeySchemaElement().withAttributeName(key).withAttributeType("S")))
				.withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(readCapacity).withWriteCapacityUnits(writeCapacity));
		client.createTable(ctr);
		
		// Waiting for a table to be available
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (10 * 60 * 1000);
        while (System.currentTimeMillis() < endTime) {
        	try {Thread.sleep(1000 * 10);} catch (Exception e) {}
        	try {
        		DescribeTableRequest request = new DescribeTableRequest().withTableName(tableName);
        		TableDescription tableDescription = client.describeTable(request).getTable();
        		String tableStatus = tableDescription.getTableStatus();
        		if (tableStatus.equals(TableStatus.ACTIVE.toString())) return;
        	} catch (AmazonServiceException ase) {
        		if (ase.getErrorCode().equalsIgnoreCase("ResourceNotFoundException") == false) throw ase;
        	}
        }

        throw new RuntimeException("Table " + tableName + " never went active");
	}
	
	public GetItemResult getItem(String tableName, String key) {
		GetItemRequest gir = new GetItemRequest()
			.withTableName(tableName)
			.withKey(new Key().withHashKeyElement(new AttributeValue(key)));
		return client.getItem(gir);
	}
	
	public void updateItem(String tableName, Map<String, AttributeValueUpdate> item) {
		UpdateItemRequest uir = new UpdateItemRequest()
			.withTableName(tableName)
			.withAttributeUpdates(item);
		client.updateItem(uir);
	}
	
	public void putItem(String tableName, Map<String, AttributeValue> item) {
		PutItemRequest pir = new PutItemRequest(tableName, item);
		client.putItem(pir);
	}
	
	public void deleteItem(String tableName, String key) {
		DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
			.withTableName(tableName)
			.withKey(new Key().withHashKeyElement(new AttributeValue().withS(key)));
		DeleteItemResult result = client.deleteItem(deleteItemRequest);
	}
	
	public ScanResult scan(String tableName, Map<String, Condition> filter, List<String> attributes) {
		ScanRequest sr = new ScanRequest(tableName);
		if (filter != null)
			sr.setScanFilter(filter);
		if (attributes != null)
			sr.setAttributesToGet(attributes);
		return client.scan(sr);
	}
	
	public void clear() {
		
	}
	
	public long getSize(String tableName) {
		TableDescription desc = client.describeTable(new DescribeTableRequest().withTableName(tableName)).getTable();
		return desc.getItemCount();
	}
}
