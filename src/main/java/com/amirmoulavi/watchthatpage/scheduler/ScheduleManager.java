package com.amirmoulavi.watchthatpage.scheduler;

import org.qi4j.api.mixin.Mixins;

/**
 * 
 * @author Amir Moulavi
 * @date 2010-08-24
 * @since 0.0.1
 *
 */
@Mixins(ScheduleManagerMixins.class)
public interface ScheduleManager extends ScheduleBehavior {

}
