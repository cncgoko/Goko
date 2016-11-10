/*******************************************************************************
 * Goko - Copyright (C) 2016  PsyKo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.controller.grbl.v09;

import java.util.List;

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

public class GrblExecutor extends AbstractStreamingExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> implements IExecutor<ExecutionTokenState, ExecutionToken<ExecutionTokenState>> {
	/** The underlying grbl service */
	private IGrblControllerService grblService;
	/** RS274 GCode service */
	private IRS274NGCService gcodeService;

	public GrblExecutor(IGrblControllerService grblService, IRS274NGCService gcodeService) {
		this.grblService = grblService;
		this.gcodeService = gcodeService;
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
		grblService.send(line);
		getToken().setLineState(line.getId(), ExecutionTokenState.SENT);
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isReadyForNextLine()
	 */
	@Override
	protected boolean isReadyForNextLine() throws GkException {
		if(getToken().hasMoreLine()){
			GCodeLine nextLine = getToken().getNextLine();
			String lineStr = gcodeService.render(nextLine);
			return grblService.getUsedGrblBuffer() + StringUtils.length(lineStr) < Grbl.GRBL_BUFFER_SIZE;
		}
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.execution.IExecutor#isReadyForQueueExecution()
	 */
	@Override
	public boolean isReadyForQueueExecution() throws GkException {
		return true;
	}
	/**
	 * Notification method called when a line is throwing error by Grbl
	 * @throws GkException
	 */
	protected GCodeLine markNextLineAsError() throws GkException{
		List<GCodeLine> lstLines = getToken().getLineByState(ExecutionTokenState.SENT);
		GCodeLine line = null;
		if(CollectionUtils.isNotEmpty(lstLines)){
			line = lstLines.get(0);
			getToken().setLineState(line.getId(), ExecutionTokenState.ERROR);
			getExecutionService().notifyCommandStateChanged(getToken(), line.getId());
		}
		return line;
	}

	/**
	 * Notification method called when a line is executed
	 * @throws GkException
	 */
	protected void confirmNextLineExecution() throws GkException{
		List<GCodeLine> lstLines = getToken().getLineByState(ExecutionTokenState.SENT);
		if(CollectionUtils.isNotEmpty(lstLines)){
			GCodeLine line = null;
			if(CollectionUtils.isNotEmpty(lstLines)){
				line = lstLines.get(0);
				getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
				getExecutionService().notifyCommandStateChanged(getToken(), line.getId());
			}
			notifyReadyForNextLineIfRequired();
			notifyTokenCompleteIfRequired();		
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
				getToken().setLineState(line.getId(), ExecutionTokenState.EXECUTED);
				getExecutionService().notifyCommandStateChanged(getToken(), line.getId());				
				System.err.println("Confirming execution of error command");
			}
			notifyReadyForNextLineIfRequired();
			notifyTokenCompleteIfRequired();
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

	/**
	 * Notify the parent executor if the conditions are met
	 * @throws GkException GkException
	 */
	private void notifyTokenCompleteIfRequired() throws GkException {
		if(getToken().getLineCountByState(ExecutionTokenState.SENT) == 0){
			notifyTokenComplete();
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#isTokenComplete()
	 */
	@Override
	public boolean isTokenComplete() throws GkException {		
		return super.isTokenComplete() && grblService.getUsedGrblPlannerBuffer() == 0;
	}
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#pause()
	 */
	@Override
	public void pause() throws GkException {		
		super.pause();
		grblService.pauseMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#stop()
	 */
	@Override
	public void stop() throws GkException {		
		super.stop();
		grblService.stopMotion();
	}

	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#start()
	 */
	@Override
	public void start() throws GkException {		
		super.start();
		grblService.startMotion();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.execution.monitor.executor.AbstractStreamingExecutor#resume()
	 */
	@Override
	public void resume() throws GkException {
		super.resume();
		confirmErrorCommands();
		grblService.resumeMotion();
	}
}
