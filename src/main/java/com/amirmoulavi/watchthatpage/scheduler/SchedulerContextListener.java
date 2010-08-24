package com.amirmoulavi.watchthatpage.scheduler;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class SchedulerContextListener implements ServletContextListener {

	public SchedulerContextListener() {
		ScheduleManagerMixins.getInstance().initialize();
	}
	
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
