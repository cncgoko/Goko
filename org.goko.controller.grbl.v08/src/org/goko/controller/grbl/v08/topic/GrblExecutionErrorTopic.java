package org.goko.controller.grbl.v08.topic;

import org.goko.controller.grbl.v08.Grbl;
import org.goko.controller.grbl.v08.bean.GrblExecutionError;
import org.goko.core.common.event.GkTopic;

public class GrblExecutionErrorTopic extends GkTopic<GrblExecutionError> {

	public GrblExecutionErrorTopic() {
		super(Grbl.Topic.GrblExecutionError.TOPIC, GrblExecutionError.class);
	}

}
