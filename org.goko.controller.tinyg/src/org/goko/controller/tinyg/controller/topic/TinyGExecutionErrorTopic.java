/**
 * 
 */
package org.goko.controller.tinyg.controller.topic;

import org.goko.controller.tinyg.controller.TinyG;
import org.goko.controller.tinyg.controller.bean.TinyGExecutionError;
import org.goko.core.common.event.GkTopic;

/**
 * @author PsyKo
 *
 */
public class TinyGExecutionErrorTopic extends GkTopic<TinyGExecutionError>{

	/**
	 * Constructor 
	 */
	public TinyGExecutionErrorTopic() {
		super(TinyG.Topic.TinyGExecutionError.TOPIC, TinyGExecutionError.class);
	}

}
