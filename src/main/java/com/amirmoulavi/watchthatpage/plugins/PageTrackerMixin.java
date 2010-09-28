package com.amirmoulavi.watchthatpage.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.amirmoulavi.watchthatpage.mail.MailService;
import com.amirmoulavi.watchthatpage.mongo.MongoDBHanlder;
import com.amirmoulavi.watchthatpage.resource.ClassResourceLocator;
import com.amirmoulavi.watchthatpage.security.MessageDigesterBehavior;
import com.amirmoulavi.watchthatpage.security.MessageDigesterMixin;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class PageTrackerMixin implements PageTracker {

	PageTrackerState state;
	
	private static Logger log = Logger.getLogger(PageTrackerMixin.class);
	private MongoDBHanlder mongo = MongoDBHanlder.getInstance();

	private MailService mailService = MailService.getInstance();
	private List<String> list = new ArrayList<String>();
	private MessageDigesterBehavior messageDigester = new MessageDigesterMixin();

	public PageTrackerMixin() {
		initialize();
	}
	
	
	private void initialize() {
		Properties property = new Properties();
		InputStream is = ClassResourceLocator.getResourceAsStream("pagetracker.properties");
		try {
			property.load(is);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		String pages = property.getProperty("pages");
		String[] page = pages.split(",");
		for (String s : page) {
			s = s.trim();
			list.add(s);
			mongo.update(s, "");
		}
		
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<String> changedPages = new ArrayList<String>();
		
		for (String page : list ) {
			retrievePage(changedPages, page);
		}
		
		if (changedPages.size() > 0) {
			try {
				mailService.mail(changedPages);
			} catch (MessagingException e) {
				log.error(e.getMessage());
			}
		}
		
	}


	private void retrievePage(List<String> changedPages, String page) {
		HttpClient client = new HttpClient();
		client.getParams().setParameter("http.socket.timeout", new Integer(10000));
		HttpMethod get = new GetMethod(page);
		log.info("Checking page: " + page);
		try {
			int result = client.executeMethod(get);
			if (result == 200) {
				String returned_page = get.getResponseBodyAsString();
				if (mongo.changed(page, messageDigester.MD5(returned_page))) {
					changedPages.add(page);
					log.info("******** Page has changed ********");
				}
			}
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			get.releaseConnection();
		}
	}
	
	/* INJECTION METHODS */
	public void setMongo(MongoDBHanlder mongo) {
		this.mongo = mongo;
	}

}
