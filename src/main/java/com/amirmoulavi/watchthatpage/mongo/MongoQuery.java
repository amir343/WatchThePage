package com.amirmoulavi.watchthatpage.mongo;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-09-28
 * @since 0.0.1
 *
 */

public interface MongoQuery {

	boolean changed(String page, String newValue);

}
