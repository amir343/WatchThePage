package com.amirmoulavi.watchthatpage.resource;

import java.io.InputStream;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-19
 * @since 0.0.1
 *
 */

public class ClassResourceLocator {
	
	public static InputStream getSystemResource(String filename) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
	}
	
	public static InputStream getClassSystemResource(String filename) {
		return ClassResourceLocator.class.getClassLoader().getResourceAsStream(filename);
	}
	
	public static InputStream getResourceAsStream(String filename) {
		InputStream resourceStream = null;
		
		resourceStream = getSystemResource(filename);
		if (null != resourceStream) {
			return resourceStream;
		}
		
		resourceStream = getClassSystemResource(filename);
		if (null != resourceStream) {
			return resourceStream;
		}
		
		resourceStream = Thread.currentThread().getClass().getResourceAsStream(filename);
		if (null != resourceStream) {
			return resourceStream;
		}

		return resourceStream;
		
	}
}
