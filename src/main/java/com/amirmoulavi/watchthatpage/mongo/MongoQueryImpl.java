package com.amirmoulavi.watchthatpage.mongo;

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

public class MongoQueryImpl implements MongoQuery {

	private static MongoQueryImpl instance = new MongoQueryImpl();
	
	public static MongoQueryImpl getInstance() {
		return instance;
	}

	private DBCollection pages;
	
	private MongoQueryImpl() {
		MongoConnection mongo = MongoConnectionFactory.getSlaveConnection();
		pages = mongo.getPages();
	}
	
	@Override
	public boolean changed(String page, String newValue) {
		DBObject doc = get(page);
		String value = (String) doc.get("value");
		if (!StringUtils.equals(value, newValue)) {
			return true;
		}
		return false;

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
