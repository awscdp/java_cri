package com.amazonaws.cri.statesharing;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

import org.apache.catalina.Session;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StoreBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodb.model.*;

import com.amazonaws.cri.AwsCriContext;
import com.amazonaws.cri.DynamoDBManager;

public class DynamoDBStore extends StoreBase {

	private static final String storeName = "DynamoDBStore";
    private static final String info = storeName + "/" + AwsCriContext.getVersion();
    private static final String tableName = (String)AwsCriContext.get("sessiontable");

    private Log logger = LogFactory.getLog(DynamoDBStore.class);;
	private DynamoDBManager ddm = (DynamoDBManager)AwsCriContext.get("dynamodbmanager");
	
	private ByteArrayOutputStream baos;
	private ByteArrayInputStream bais;
	private ByteBuffer buffer;
	
	private QueryRequest keyRequest;
	
	public DynamoDBStore() throws ClassNotFoundException, IOException {
		try {
			ddm.createTable("Id", tableName);
		} catch(ResourceInUseException riue) {
			logger.info(tableName + " table already exists");
		}
		baos = new ByteArrayOutputStream();
	}
		
	@Override
	public void clear() throws IOException {
		logger.debug("clear()");
		ddm.clear();
	}

	@Override
	public int getSize() throws IOException {
		return (int)ddm.getSize(tableName);
	}

	@Override
	public String[] keys() throws IOException {
		HashMap<String, Condition> filter = new HashMap<String, Condition>();
		List<String> attributes = Arrays.asList("Id");
		ScanResult sr = ddm.scan(tableName, null, attributes);

		List<Map<String, AttributeValue>> list = sr.getItems();
		logger.debug("keys() : " + list.size());

		String[] array = new String[list.size()];
		int i = 0;
		for(Iterator<Map<String, AttributeValue>> iterator = list.iterator(); iterator.hasNext(); i++) {
			Map<String, AttributeValue> entry = iterator.next();
			array[i] = entry.get("Id").getS();
			logger.debug("key : " + array[i]);
		}

		return array;
	}

	@Override
	public Session load(String arg0) throws ClassNotFoundException, IOException {
		logger.debug("Loading : " + arg0);
		StandardSession session = (StandardSession)manager.createEmptySession();
		
		GetItemResult gir = ddm.getItem(tableName, arg0);		
		if (gir.getItem() != null) {
			bais = new ByteArrayInputStream(gir.getItem().get("body").getB().array());
			ObjectInputStream ois = new ObjectInputStream(bais);
			session.readObjectData(ois);
		}
		else return null;
		
		session.setManager(manager);
		return session;
	}

	@Override
	public void remove(String arg0) throws IOException {
		logger.debug("Removing : " + arg0);
		ddm.deleteItem(tableName, arg0);
	}

	@Override
	public void save(Session arg0) throws IOException {
		logger.debug("Saving : " + arg0.toString());
		
		baos.reset();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		StandardSession session = (StandardSession)arg0;
		session.writeObjectData(oos);
		
		byte[] b = baos.toByteArray();
		buffer = ByteBuffer.allocate(b.length);
		buffer.put(b);
		buffer.flip();

		Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		item.put("Id", new AttributeValue(arg0.getId()));
		item.put("body", new AttributeValue().withB(buffer));

		ddm.putItem(tableName, item);
	}

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public String getStoreName() {
        return storeName;
    }
    
}
