package org.goko.core.gcode.execution;

import org.goko.core.common.exception.GkException;

public interface IExecutionControl {
	ExecutionState getState() throws GkException;
	void start() throws GkException;
	void resume() throws GkException;
	void pause() throws GkException;
	void stop() throws GkException;
}
