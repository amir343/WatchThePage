package com.amirmoulavi.watchthatpage.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.amirmoulavi.watchthatpage.resource.ClassResourceLocator;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class MongoDBHanlder {
	
	private static MongoDBHanlder instance = new MongoDBHanlder();
	private Logger log = Logger.getLogger(MongoDBHanlder.class);
	
	public static MongoDBHanlder getInstance() {
		return instance;
	}
	
	private Mongo mongo;
	private DB db_wtp;
	private DBCollection pages;
	
	private MongoDBHanlder() {
		try {
			Properties property = new Properties();
			InputStream is = ClassResourceLocator.getResourceAsStream("mongo.properties");
			property.load(is);
			String server = property.getProperty("server");
			String port = property.getProperty("port");
			String db = property.getProperty("db");
			String coll = property.getProperty("wtp_collection");
			mongo = new Mongo(server, Integer.parseInt(port));
			db_wtp = mongo.getDB(db);
			pages = db_wtp.getCollection(coll);
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
		} catch (MongoException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	

	public void update(String page, String value) {
		DBObject doc = get(page);
		if (StringUtils.isNotBlank(value)) {
			doc.put("value", value);
		} 
		
		pages.save(doc);	
	}


	public boolean changed(String page, String newValue) {
		DBObject doc = get(page);
		String value = (String) doc.get("value");
		if (!StringUtils.equals(value, newValue)) {
			doc.put("value", newValue);
			doc.put("last_changed", Calendar.getInstance().getTime());
			pages.save(doc);
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
