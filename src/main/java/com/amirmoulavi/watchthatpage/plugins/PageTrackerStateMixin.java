package com.amirmoulavi.watchthatpage.plugins;

import org.apache.log4j.Logger;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 * @since 0.0.1
 *
 */

public class PageTrackerStateMixin implements PageTrackerState {

	private Logger log = Logger.getLogger(PageTrackerStateMixin.class);
	
	@Override
	public void logError(String error) {
		log.error(error);
	}
	
	@Override
	public void logInfo(String info) {
		log.info(info);
	}
	
}
