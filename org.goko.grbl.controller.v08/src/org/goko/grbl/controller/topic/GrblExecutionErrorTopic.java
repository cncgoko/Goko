package org.goko.grbl.controller.topic;

import org.goko.core.common.event.GkTopic;
import org.goko.grbl.controller.Grbl;
import org.goko.grbl.controller.bean.GrblExecutionError;

public class GrblExecutionErrorTopic extends GkTopic<GrblExecutionError> {

	public GrblExecutionErrorTopic() {
		super(Grbl.Topic.GrblExecutionError.TOPIC, GrblExecutionError.class);
	}

}
