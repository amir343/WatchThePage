package com.amirmoulavi.watchthatpage.mongo;


/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class MongoDAOImpl implements MongoDAO {
	
	private static MongoDAOImpl instance = new MongoDAOImpl();
	private MongoCommand command = MongoCommandImpl.getInstance();
	private MongoQuery query = MongoQueryImpl.getInstance();
	
	public static MongoDAOImpl getInstance() {
		return instance;
	}
	
	@Override
	public void update(String page, String value) {
		command.update(page, value);
	}


	@Override
	public boolean changed(String page, String newValue) {
		return query.changed(page, newValue);		
	}
	
	
}
