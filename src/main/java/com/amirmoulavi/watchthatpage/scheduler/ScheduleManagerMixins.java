package com.amirmoulavi.watchthatpage.scheduler;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.qi4j.api.entity.EntityBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.amirmoulavi.watchthatpage.resource.ClassResourceLocator;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class ScheduleManagerMixins implements ScheduleManager {

	private static ScheduleManagerMixins instance = new ScheduleManagerMixins();
	private static Logger log = Logger.getLogger(ScheduleManagerMixins.class);
	private List<JobDetail> jobList = new ArrayList<JobDetail>();
	private Properties properties;
	private static Scheduler scheduler;
	
	static {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
		} catch (SchedulerException e) {
			log.error(e.getMessage());
		}
	}
	
	public static ScheduleManagerMixins getInstance() {
		return instance;
	}
	
	public ScheduleManagerMixins() {
		
	}

	@Override
	public void initialize() {
		InputStream is = ClassResourceLocator.getResourceAsStream("scheduler.properties");
		properties = new Properties();
		
		try {
			properties.load(is);
			for (Iterator it = properties.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				if(log.isDebugEnabled()){
					log.debug("Read properties file value: \n" + (String)entry.getKey());
				}
				try {
					if (((String)entry.getValue()).indexOf(" ") > -1 ) {
						initializeJob((String)entry.getKey(), (String)entry.getValue() );
					} else {
						initializeJob((String)entry.getKey(), Integer.parseInt((String)entry.getValue() ));
					}

				}catch (NumberFormatException e){
					log.error("Unable to convert the interval time to a integer. " + entry.getValue());
				}catch (SchedulerException e){
					log.error("Unable to read properties file.");
				}catch(ArrayIndexOutOfBoundsException e){
					log.error("Probable error reading properties file for Scheduled jobs", e);
				}
			}
			is.close();
			is = null;
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void initializeJob(String jobClassName, int timerInterval) throws SchedulerException{
		Class<?> clazz = null;
		try {
			clazz = (Class) Class.forName( jobClassName );
		} catch (ClassNotFoundException e) {
			log.error("Cache class " + jobClassName + " could not be found");
		}
		
		JobDetail newJob = constructJobDetails(jobClassName, clazz);
		
		if (null == newJob) {
			return;
		}
		
		SimpleTrigger trigger = new SimpleTrigger(clazz.getSimpleName(), null, Calendar.getInstance().getTime(),
				null, SimpleTrigger.REPEAT_INDEFINITELY, timerInterval * 60L * 1000L);

		scheduler.scheduleJob(newJob, trigger);
		jobList.add(newJob);
	}
	
	private void initializeJob(String jobClassName, String cronTrigger) throws SchedulerException{
		Class<?> clazz = null;
		try {
			clazz = (Class) Class.forName( jobClassName );
		} catch (ClassNotFoundException e) {
			log.error("Cache class " + jobClassName + " could not be found");
		}
		
		JobDetail newJob = constructJobDetails(jobClassName, clazz);
		
		if (null == newJob) {
			return;
		}
		
		CronTrigger trigger = null;
		try {
			trigger = new CronTrigger(clazz.getSimpleName(), null, cronTrigger);
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		
		if (null == trigger) {
			return;
		}
		
		scheduler.scheduleJob(newJob, trigger);
		jobList.add(newJob);
	}

	private JobDetail constructJobDetails(String jobClassName, Class<?> clazz) {
		Job job = null;
		try {
			try {
				job = (Job)clazz.newInstance();;
			} catch (IllegalArgumentException e) {
				log.error("Unable to create new instance of class " + jobClassName );
			} catch(ClassCastException e) {
				log.error("Class " + jobClassName + " does not implement the requiered interface.");
			}
		} catch (InstantiationException e) {
			log.error("Cache class " + jobClassName + " could not be instantiated.");
		} catch (IllegalAccessException e) {
			log.error("Cache class " + jobClassName + " is not permitted to access");
		} catch (SecurityException e) {
			log.error("Cache class " + jobClassName + " could not be created due to security issues.");
		}
		
		if(null == job) {
			return null;
		}
		
		if(log.isInfoEnabled()) {
			log.info("Job " + job.getClass().getSimpleName() + " initialized.");
		}


		JobDetail newJob = new JobDetail(clazz.getSimpleName(), null, job.getClass());
		return newJob;
	}


}
