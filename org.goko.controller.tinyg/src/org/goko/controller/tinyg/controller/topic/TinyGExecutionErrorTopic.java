/**
 * 
 */
package org.goko.controller.tinyg.controller.topic;

import org.goko.controller.tinyg.commons.bean.TinyGExecutionError;
import org.goko.controller.tinyg.controller.TinyGv097;
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
		super(TinyGv097.Topic.TinyGExecutionError.TOPIC, TinyGExecutionError.class);
	}

}
