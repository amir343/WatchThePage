package com.amirmoulavi.watchthatpage.mongo;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-09-28
 * @since 0.0.1
 *
 */

public class MongoCommandImpl implements MongoCommand {

	private static MongoCommandImpl instance = new MongoCommandImpl();
	
	public static MongoCommand getInstance() {
		return instance;
	}

	private DBCollection pages;

	private MongoCommandImpl() {
		MongoConnection connection = MongoConnectionFactory.getMasterConnection();
		pages = connection.getPages();
	}

	@Override
	public void update(String page, String value) {
		DBObject doc = get(page);
		if (StringUtils.isNotBlank(value)) {
			doc.put("value", value);
		} 
		doc.put("last_changed", Calendar.getInstance().getTime());		
		pages.save(doc);	
	}
	
	@SuppressWarnings("static-access")
	private DBObject get(String page) {
		DBObject q = new BasicDBObjectBuilder().start().add("url", page).get();
		DBCursor res = pages.find(q);
		DBObject doc;
		if (res.hasNext()) {
			doc = res.next();
		} else {
			doc = new BasicDBObject();
			doc.put("url", page);
		}
		return doc;
	}
	

}
