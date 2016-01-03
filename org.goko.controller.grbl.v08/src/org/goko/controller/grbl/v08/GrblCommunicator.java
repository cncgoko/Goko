/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v08;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.goko.controller.grbl.v08.bean.StatusReport;
import org.goko.core.common.GkUtils;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.gcode.execution.IExecutor;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

public class GrblCommunicator implements IConnectionDataListener, IConnectionListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GrblCommunicator.class);
	/** The target Grbl service */
	private GrblControllerService grbl;
	/** Grbl end line delimiter */
	private char endLineCharDelimiter;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** The connection service */
	private IConnectionService connectionService;
	/** Executor used by grbl*/
	private IExecutor grblExecutor;
	/**
	 * Constructor
	 */
	protected GrblCommunicator(GrblControllerService grbl) {
		this.grbl = grbl;
		endLineCharDelimiter = '\n';
		incomingBuffer 		 = new ByteCommandBuffer((byte) endLineCharDelimiter);
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
		incomingBuffer.addAll(data);
		while(incomingBuffer.hasNext()){
			handleIncomingData(GkUtils.toString(incomingBuffer.unstackNextCommand()));
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataSent(java.util.List)
	 */
	@Override
	public void onDataSent(List<Byte> data) throws GkException {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionEvent(EnumConnectionEvent event) throws GkException {
		if(event == EnumConnectionEvent.CONNECTED){
			incomingBuffer.clear();
			getConnectionService().addInputDataListener(this);
			grbl.startStatusPolling();
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			getConnectionService().removeInputDataListener(this);
			grbl.stopStatusPolling();
			grbl.setState(GrblMachineState.UNDEFINED);
			incomingBuffer.clear();
		}
	}
	/**
	 * Handling of incoming data
	 * @param data the received data
	 * @throws GkException GkException
	 */
	protected void handleIncomingData(String data) throws GkException{
		String trimmedData = StringUtils.trim(data);
		if(StringUtils.isNotEmpty(trimmedData)){
			/* Received OK response */
			if(StringUtils.equals(trimmedData, Grbl.OK_RESPONSE)){
				grbl.handleOkResponse();

			/* Received error  */
			}else if(StringUtils.startsWith(trimmedData, "error:")){
				grbl.handleError(trimmedData);

			/* Received status report  */
			}else if(StringUtils.startsWith(trimmedData, "<") && StringUtils.endsWith(trimmedData, ">")){
				grbl.handleStatusReport(parseStatusReport(trimmedData));

			/* Received Grbl header */
			}else if(StringUtils.startsWith(trimmedData, "Grbl")){
				handleHeader(trimmedData);
				grbl.initialiseConnectedState();
			//	refreshStatus();

			/* Received a configuration confirmation */
			}else if(StringUtils.defaultString(trimmedData).matches("\\$[0-9]*=.*")){
				grbl.handleConfigurationReading(trimmedData);

			/* Received an offset position report */
//			}else if(StringUtils.defaultString(trimmedData).matches("\\[(G5|G28|G30|G92).*\\]")){
			}else if(StringUtils.defaultString(trimmedData).matches("\\[(G5).*\\]")){
				Tuple6b targetPoint = new Tuple6b().setNull();
				String offsetName = parseCoordinateSystem(trimmedData, targetPoint);
				grbl.setOffsetCoordinate(offsetName, targetPoint);

			/* Parser state report */
			}else if(StringUtils.defaultString(trimmedData).matches("\\[(G0|G1|G2|G3).*\\]")){
				grbl.receiveParserState(StringUtils.substringBetween(trimmedData, "[","]"));
			/* Unkown format received */
			}else{
				LOG.error("Ignoring received data "+ trimmedData);
				grbl.getApplicativeLogService().warning("Ignoring received data "+ trimmedData, GrblControllerService.SERVICE_ID);
			}
		}
	}

	/**
	 * Parse Grbl header
	 * @param grblHeader the received header
	 */
	private void handleHeader(String grblHeader) {
		String[] tokens = StringUtils.split(grblHeader, " ");
		if(tokens != null && tokens.length >= 2){
			LOG.info("Grbl version is "+tokens[1]);
		}
	}
	/**
	 * Create a status report from the given string
	 * @param strStatusReport the String representing the status report
	 * @return {@link StatusReport}
	 * @throws GkException
	 */
	private StatusReport parseStatusReport(String strStatusReport) throws GkException{
		StatusReport result = new StatusReport();
		int comma = StringUtils.indexOf(strStatusReport, ",");
		String state = StringUtils.substring(strStatusReport, 1, comma);
		GrblMachineState grblState = grbl.getGrblStateFromString (state);
		result.setState(grblState);

		// Looking for MPosition
		String mpos = StringUtils.substringBetween(strStatusReport, "MPos:", ",WPos");
		String wpos = StringUtils.substringBetween(strStatusReport, "WPos:",">");
		Tuple6b machinePosition = new Tuple6b().setNull();
		Tuple6b workPosition = new Tuple6b().setNull();
		String[] machineCoordinates = StringUtils.split(mpos,",");

		parseTuple(machineCoordinates, machinePosition);
		result.setMachinePosition(machinePosition);

		String[] workCoordinates = StringUtils.split(wpos,",");
		parseTuple(workCoordinates, workPosition);
		result.setWorkPosition(workPosition);

		return result;
	}

	private void parseTuple(String[] values, Tuple6b target) throws GkException{
		if(values != null && values.length >= 3){
			Unit<Length> unit = grbl.getConfiguration().getReportUnit();
			if(NumberUtils.isNumber(values[0])){
				target.setX(NumberQuantity.of(new BigDecimal(values[0]), unit));
			}
			if(NumberUtils.isNumber(values[1])){
				target.setY(NumberQuantity.of(new BigDecimal(values[1]), unit));
			}
			if(NumberUtils.isNumber(values[2])){
				target.setZ(NumberQuantity.of(new BigDecimal(values[2]), unit));
			}
		}
	}
	private String parseCoordinateSystem(String strOrigin, Tuple6b targetPoint) throws GkException{
		String identifier = StringUtils.substringBetween(strOrigin, "[", ":");
		String valuesGroup = StringUtils.substringBetween(strOrigin, ":", "]");
		String[] values = StringUtils.split(valuesGroup, ",");
		Unit<Length> unit = grbl.getConfiguration().getReportUnit();
		if(values == null || values.length < 3){
			throw new GkFunctionalException("Received incomplete offset report "+strOrigin+". Ignoring...");
		}

		BigDecimal x = new BigDecimal(values[0]);
		BigDecimal y = new BigDecimal(values[1]);
		BigDecimal z = new BigDecimal(values[2]);
		targetPoint.setX(NumberQuantity.of(x, unit));
		targetPoint.setY(NumberQuantity.of(y, unit));
		targetPoint.setZ(NumberQuantity.of(z, unit));

		return identifier;
	}
	/**
	 * Add the end line character at the end of the given list
	 * @param list the list
	 */
	private void addEndLineCharacter(List<Byte> list){
		//command.add(new Byte((byte) endLineCharDelimiter));
		list.add(new Byte((byte) endLineCharDelimiter));
	}
	/**
	 * @return the connectionService
	 */
	protected IConnectionService getConnectionService() {
		return connectionService;
	}
	/**
	 * @param connectionService the connectionService to set
	 * @throws GkException GkException
	 */
	protected void setConnectionService(IConnectionService connectionService) throws GkException {
		this.connectionService = connectionService;
		this.connectionService.addConnectionListener(this);
	}

	protected void send(List<Byte> lstByte) throws GkException{
		addEndLineCharacter(lstByte);
		getConnectionService().send(lstByte);
	}

	protected void sendWithoutEndLineCharacter(List<Byte> lstByte) throws GkException{
		getConnectionService().send(lstByte);
	}

	protected void sendImmediately(List<Byte> lstByte) throws GkException{
		getConnectionService().send(lstByte, DataPriority.IMPORTANT);
	}
}
