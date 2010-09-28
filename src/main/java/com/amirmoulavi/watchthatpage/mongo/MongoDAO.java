package com.amirmoulavi.watchthatpage.mongo;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-09-28
 * @since 0.0.1
 *
 */

public interface MongoDAO {

	void update(String page, String value);

	boolean changed(String page, String newValue);

}
