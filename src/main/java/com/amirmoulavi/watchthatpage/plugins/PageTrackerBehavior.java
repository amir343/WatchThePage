package com.amirmoulavi.watchthatpage.plugins;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 * @since 0.0.1
 *
 */

public interface PageTrackerBehavior extends StatefulJob {
	
	void execute(JobExecutionContext arg0) throws JobExecutionException;
}
