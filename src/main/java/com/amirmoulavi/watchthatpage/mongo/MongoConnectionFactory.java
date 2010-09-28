package com.amirmoulavi.watchthatpage.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.amirmoulavi.watchthatpage.resource.ClassResourceLocator;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-09-28
 * @since 0.0.1
 *
 */

/**
 * TODO I hate using static like this! 
 * This needs to be changed sooner or later
 */

public class MongoConnectionFactory {
	
	private static Logger log = Logger.getLogger(MongoConnectionFactory.class);
	private static String coll;
	private static String db;
	private static String port;
	private static String server;

	static {
		Properties property = new Properties();
		InputStream is = ClassResourceLocator.getResourceAsStream("mongo.properties");
		try {
			property.load(is);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		server = property.getProperty("server");
		port = property.getProperty("port");
		db = property.getProperty("db");
		coll = property.getProperty("wtp_collection");

	}
	
	public static MongoConnection getMasterConnection() {
		MongoConnection conn = new MongoConnection();
		try {
			Mongo mongo = new Mongo(server, Integer.parseInt(port));
			DB db_wtp = mongo.getDB(db);
			DBCollection pages = db_wtp.getCollection(coll);
			conn.setMongo(mongo);
			conn.setPages(pages);
			
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
		} catch (MongoException e) {
			log.error(e.getMessage());
		}
		return conn;
	}

	public static MongoConnection getSlaveConnection() {
		// TODO This method deserves its own implementation. Simply a new field in property file
		return getMasterConnection();
	}

	
}
