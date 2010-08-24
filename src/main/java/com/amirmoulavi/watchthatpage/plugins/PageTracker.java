package com.amirmoulavi.watchthatpage.plugins;

import org.qi4j.api.mixin.Mixins;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 * @since 0.0.1
 *
 */

@Mixins(PageTrackerMixin.class)
public interface PageTracker extends PageTrackerBehavior {

}
