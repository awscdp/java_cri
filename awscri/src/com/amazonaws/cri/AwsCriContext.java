package com.amazonaws.cri;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AwsCriContext {
	private final static String version = "0.1";
	private static Properties props;
	
	static {
		InputStream in = AwsCriContext.class.getClassLoader().getResourceAsStream("AwsCri.properties");
		props = new Properties();
		try {
			props.load(in);
			props.put("sqsmanager", new SQSManager());
			props.put("dynamodbmanager", new DynamoDBManager());			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key) {
		return props.getProperty(key);
	}
	
	public static Object get(String key) {
		return props.get(key);
	}
	
	public static String getVersion() {
		return version;
	}
	
}
