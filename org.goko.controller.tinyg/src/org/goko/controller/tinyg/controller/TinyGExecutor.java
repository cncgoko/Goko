/**
 * 
 */
package org.goko.controller.tinyg.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executor.AbstractStreamingExecutor;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.IExecutor;

/**
 * TinyG executor implementation 
 * 
 * @author PsyKo
 * @date 20 nov. 2015
 */
public class TinyGExecutor extends AbstractStreamingExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> implements IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	/** The number of command sent but not confirmed */
	private AtomicInteger pendingCommandCount;
	/** The underlying service */
	private ITinygControllerService tinygService;
	/** Required space in TinyG planner buffer to send a new command */
	private int requiredBufferSpace;
		
	/**
	 * Constructor
	 * @param tinygService the underlying TinyG service
	 */
	public TinyGExecutor(ITinygControllerService tinygService) {
		super();
		this.tinygService = tinygService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#createToken(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> createToken(IGCodeProvider provider) throws GkException {
		return new ExecutionToken<ExecutionTokenState>(provider, ExecutionTokenState.NONE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	protected void send(GCodeLine line) throws GkException {
		pendingCommandCount.incrementAndGet();		
		tinygService.send(line);
		getToken().setLineState(line.getId(), ExecutionTokenState.SENT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isReadyForNextLine()
	 */
	@Override
	protected boolean isReadyForNextLine() throws GkException {
		return pendingCommandCount.intValue() <= 0 && tinygService.getAvailableBuffer() >= requiredBufferSpace;
	}

	/**
	 * Notification method when the available buffer space changed
	 * @param availableBufferSpace the available space buffer 
	 * @throws GkException GkException
	 */
	protected void onBufferSpaceAvailableChange(int availableBufferSpace) throws GkException{
		if(availableBufferSpace >= requiredBufferSpace && isReadyForNextLine()){
			notifyReadyForNextLineIfRequired();
		}
	}
	
	/**
	 * Notification method called when a line is confirmed by TinyG
	 * @throws GkException
	 */
	protected void onLineConfirmed() throws GkException{
		pendingCommandCount.decrementAndGet();
		List<GCodeLine> lstLines = getToken().getLineByState(ExecutionTokenState.SENT);
		GCodeLine line = lstLines.get(0);
		getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
		notifyReadyForNextLineIfRequired();
	}
	
	/**
	 * Notify the parent executor if the conditions are met
	 * @throws GkException GkException 
	 */
	protected void notifyReadyForNextLineIfRequired() throws GkException{
		if(isReadyForNextLine()){
			notifyReadyForNextLine();
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#isReadyForQueueExecution()
	 */
	@Override
	public boolean isReadyForQueueExecution() throws GkException {
		// FIXME : bind with TinyG
		/*
		 checkExecutionControl();
		 checkVerbosity(configuration);
		 */
		return true;
	}
}
