package com.amirmoulavi.watchthatpage.mongo;

import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-09-28
 * @since 0.0.1
 *
 */

public class MongoConnection {

	private Mongo mongo;
	private DBCollection pages;
	
	public Mongo getMongo() {
		return mongo;
	}
	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}
	public DBCollection getPages() {
		return pages;
	}
	public void setPages(DBCollection pages) {
		this.pages = pages;
	}
	
}
