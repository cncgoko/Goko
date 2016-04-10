/**
 *
 */
package org.goko.controller.tinyg.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executor.AbstractStreamingExecutor;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;

/**
 * TinyG executor implementation
 *
 * @author PsyKo
 * @date 20 nov. 2015
 */
public class TinyGExecutor extends AbstractStreamingExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> implements IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>, IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(TinyGExecutor.class);
	/** The number of command sent but not confirmed */
	private AtomicInteger pendingCommandCount;
	/** The underlying service */
	private ITinygControllerService tinygService;
	/** The underlying gcode service */
	private IRS274NGCService rs274Service;
	/** Required space in TinyG planner buffer to send a new command */
	private int requiredBufferSpace = 6;

	/**
	 * Constructor
	 * @param tinygService the underlying TinyG service
	 */
	public TinyGExecutor(ITinygControllerService tinygService, IRS274NGCService gcodeService) {
		super();
		this.tinygService = tinygService;
		this.rs274Service = gcodeService;
		this.pendingCommandCount = new AtomicInteger(0);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#createToken(org.goko.core.gcode.element.IGCodeProvider)
	 */
	@Override
	public ExecutionToken<ExecutionTokenState> createToken(IGCodeProvider provider) throws GkException {
		return new ExecutionToken<ExecutionTokenState>(rs274Service, provider, ExecutionTokenState.NONE);
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#send(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	protected void send(GCodeLine line) throws GkException {
		String gcodeString = rs274Service.render(line);
		if(StringUtils.isNotBlank(gcodeString)){
			pendingCommandCount.incrementAndGet();
			tinygService.send(line);
			getToken().setLineState(line.getId(), ExecutionTokenState.SENT);
		}else{
			getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isReadyForNextLine()
	 */
	@Override
	protected boolean isReadyForNextLine() throws GkException {
		int actuallyAvailableBuffer = tinygService.getAvailableBuffer() - pendingCommandCount.intValue();
		return  ( !tinygService.isPlannerBufferSpaceCheck() || actuallyAvailableBuffer >= requiredBufferSpace);		
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
	protected void confirmNextLineExecution() throws GkException{
		pendingCommandCount.decrementAndGet();
		List<GCodeLine> lstLines = getToken().getLineByState(ExecutionTokenState.SENT);

		if(CollectionUtils.isNotEmpty(lstLines)){
			GCodeLine line = lstLines.get(0);
			getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
			getExecutionService().notifyCommandStateChanged(getToken(), line.getId());
			notifyReadyForNextLineIfRequired();
			notifyTokenCompleteIfRequired();
		}
	}

	/**
	 * Notification method called when a line is throwing error by TinyG
	 * @throws GkException
	 */
	protected void markNextLineAsError() throws GkException{
		pendingCommandCount.decrementAndGet();
		List<GCodeLine> lstLines = getToken().getLineByState(ExecutionTokenState.SENT);
		GCodeLine line = lstLines.get(0);
		getToken().setLineState(line.getId(), ExecutionTokenState.ERROR);
		getExecutionService().notifyCommandStateChanged(getToken(), line.getId());
	}

	/**
	 * Notify the parent executor if the conditions are met
	 * @throws GkException GkException
	 */
	private void notifyTokenCompleteIfRequired() throws GkException {
		if(getToken().getLineCountByState(ExecutionTokenState.SENT) == 0 && pendingCommandCount.get() <= 0){			
			notifyTokenComplete();
		}
	}

	/**
	 * Handles any TinyG Status that is not TG_OK
	 * @param status the received status or <code>null</code> if unknown
	 * @throws GkException GkException
	 */
	protected void handleNonOkStatus(TinyGStatusCode status) throws GkException {
		if(status == null || status.isError()){
			LOG.warn("Pausing execution queue from TinyG Executor due to received status ["+status.getValue()+"]");
			getExecutionService().pauseQueueExecution();
			markNextLineAsError();
		}else{
			confirmNextLineExecution();
		}
	}

	/**
	 * Method used to confirm all command being marked as error.
	 * This is the result of the user continuing the execution after an error was reported.
	 * If the user continues, it meas the error is ignored.
	 * @throws GkException GkException
	 */
	protected void confirmErrorCommands() throws GkException{
		List<GCodeLine> lstErrorToken = getToken().getLineByState(ExecutionTokenState.ERROR);
		if(CollectionUtils.isNotEmpty(lstErrorToken)){
			for (GCodeLine line : lstErrorToken) {
				pendingCommandCount.decrementAndGet();
				getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
				getExecutionService().notifyCommandStateChanged(getToken(), line.getId());
				notifyReadyForNextLineIfRequired();
				notifyTokenCompleteIfRequired();
			}
		}
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
		tinygService.verifyReadyForExecution();
		return tinygService.isReadyForFileStreaming();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		this.pendingCommandCount.set(0);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(ExecutionToken<ExecutionTokenState> token) throws GkException {
		// We confirm commands in error state:
		//   - if it's a non blocking warning, it's fine
		//   - if it's an error and the user continue the execution, it assumes the error can be ignored
		// 	 - if it's an error and the user stops the execution, this confirm never happens
		confirmErrorCommands();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		this.pendingCommandCount.set(0);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the rs274Service
	 */
	public IRS274NGCService getRs274Service() {
		return rs274Service;
	}

	/**
	 * @param rs274Service the rs274Service to set
	 */
	public void setRs274Service(IRS274NGCService rs274Service) {
		this.rs274Service = rs274Service;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#stop()
	 */
	@Override
	public void stop() throws GkException {		
		super.stop();	
		tinygService.stopMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#pause()
	 */
	@Override
	public void pause() throws GkException {		
		super.pause();
		tinygService.pauseMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#start()
	 */
	@Override
	public void start() throws GkException {		
		super.start();
		tinygService.start();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#resume()
	 */
	@Override
	public void resume() throws GkException {		
		super.resume();
		tinygService.resumeMotion();
	}
}
