/**
 * 
 */
package org.goko.controller.g2core.controller.topic;

import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.bean.TinyGExecutionError;
import org.goko.core.common.event.GkTopic;

/**
 * @author PsyKo
 *
 */
public class G2CoreExecutionErrorTopic extends GkTopic<TinyGExecutionError>{

	/**
	 * Constructor 
	 */
	public G2CoreExecutionErrorTopic() {
		super(G2Core.Topic.Error.TOPIC, TinyGExecutionError.class);
	}

}
