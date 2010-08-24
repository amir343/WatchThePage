package com.amirmoulavi.watchthatpage.security;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 *
 */

public class MessageDigesterMixinTest {
	
	private MessageDigesterMixin mDig;
	private String plain;
	private String result;

	@Before
	public void setUp() {
		mDig = new MessageDigesterMixin();
	}
	
	@Test
	public void md5_digester_is_working_correctly() {
		given_the_content_as("<html><title>Amir Moulavi</title></html>");
		
		when_the_content_digested();
		
		then_the_result_is("9e6f3bbb1110d0b802be4b499c1d3ac8");
	}

	private void given_the_content_as(String plain) {
		this.plain = plain;
	}

	private void when_the_content_digested() {
		result = mDig.MD5(plain);
	}

	private void then_the_result_is(String digestedMessage) {
		Assert.assertEquals("", digestedMessage, result);		
	}
}
