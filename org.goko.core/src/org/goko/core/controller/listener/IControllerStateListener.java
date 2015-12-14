package org.goko.core.controller.listener;

import org.goko.core.common.exception.GkException;

public interface IControllerStateListener {
	
	void onControllerStateChange() throws GkException;
}
