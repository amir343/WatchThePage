package com.amirmoulavi.watchthatpage.security;

import org.qi4j.api.mixin.Mixins;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-23
 * @since 0.0.1
 *
 */
@Mixins(MessageDigesterMixin.class)
public interface MessageDigesterBehavior {

	String MD5(String message);

}
