package com.amirmoulavi.watchthatpage.plugins;

import org.qi4j.api.mixin.Mixins;
import org.qi4j.library.constraints.annotation.NotEmpty;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 * @since 0.0.1
 *
 */

@Mixins(PageTrackerStateMixin.class)
public interface PageTrackerState {

	void logError(@NotEmpty String error);

	void logInfo(@NotEmpty String info);
	
	
	
}
