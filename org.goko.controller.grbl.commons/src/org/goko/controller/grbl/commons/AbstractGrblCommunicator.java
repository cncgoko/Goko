/*
 *
 *   Goko
 *   Copyright (C) 2013, 2016  PsyKo
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
package org.goko.controller.grbl.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystemFactory;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

public abstract class AbstractGrblCommunicator<C extends AbstractGrblConfiguration<C>, M extends MachineState, S extends IGrblControllerService<C, M>> implements IConnectionDataListener, IConnectionListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractGrblCommunicator.class);
	/** The target Grbl service */
	private S controllerService;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** End line characters */
	private List<Character> endLineCharacters;
	/** Connected flag */
	private boolean connected;
	/** The connection service */
	private IConnectionService connectionService;
	/** UI Log service */	
	private IApplicativeLogService applicativeLogService;

	
	/**
	 * Constructor
	 */
	protected AbstractGrblCommunicator(S grblControllerService) {
		this.controllerService = grblControllerService;
		this.incomingBuffer    = new ByteCommandBuffer((byte) '\n');
		this.endLineCharacters = new ArrayList<Character>();
		setEndLineCharacters('\n');
	}
	
	/**
	 * Set the end line characters 
	 * @param chars the end line characters
	 */
	public void setEndLineCharacters(char... chars){
		this.endLineCharacters = Arrays.asList(ArrayUtils.toObject(chars));
		this.incomingBuffer.setCommandDelimiter(chars[chars.length - 1]);
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
			connected = true;
			connectionService.addInputDataListener(this);
			onConnected();		
			getControllerService().onConnected();
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			connected = false;
			connectionService.removeInputDataListener(this);
			onDisconnected();
			getControllerService().onDisconnected();
		}		
	}
	
	protected abstract void onConnected() throws GkException;
	
	protected abstract void onDisconnected() throws GkException;
	/**
	 * Handling of incoming data
	 * @param data the received data
	 * @throws GkException GkException
	 */
	protected void handleIncomingData(String data) throws GkException{
		String trimmedData = StringUtils.trim(StringUtils.defaultString(data));
		if(StringUtils.isNotEmpty(trimmedData)){
			/* Received OK response */
			if(StringUtils.equals(trimmedData, Grbl.OK_RESPONSE)){
				handleOkResponse();

			/* Received error  */
			}else if(StringUtils.startsWith(trimmedData, "error:")){
				handleError(trimmedData);

			/* Received status report  */
			}else if(StringUtils.startsWith(trimmedData, "<") && StringUtils.endsWith(trimmedData, ">")){
				handleStatusReport(trimmedData);

			/* Received Grbl header */
			}else if(StringUtils.startsWith(trimmedData, "Grbl")){
				handleHeader(trimmedData);
			/* Received a configuration confirmation */
			}else if(trimmedData.matches("\\$[0-9]*=.*")){
				handleConfigurationReading(trimmedData);
				
			/* Received a probe result */
			}else if(trimmedData.matches("\\[PRB.*]")){
				//[PRB:0.000,0.000,0.000:0]
				handleProbeResult(trimmedData);
			/* Received a work position report */
			}else if(trimmedData.matches("\\[TLO.*]")){
				//[PRB:0.000,0.000,0.000:0]
				handleToolLengthOffset(trimmedData);
			/* Received a work position report */
			}else if(trimmedData.matches("\\[G5.*\\]")){
				handleCoordinateSystemOffset(trimmedData);
			
			}else if(trimmedData.matches("\\[MSG:.*\\]")){
				handleMessage(trimmedData);
			/* Received an offset position report */
			}else if(trimmedData.matches("\\[(G92|G28|G30).*\\]")){					
//				Tuple6b targetPoint = new Tuple6b().setNull();
//				String coordinateSystemName = parseCoordinateSystem(trimmedData, targetPoint);
//				grbl.setOffsetCoordinate(coordinateSystemName, targetPoint);
				// TODO Handle G92
			/* Parser state report */
			}else if(trimmedData.matches("\\[GC:.*\\]")){
				handleParserState(trimmedData);
			/* Unknown format received */
			}else if(trimmedData.startsWith("ALARM")){
				handleAlarm(trimmedData);
			}else{
				if(!handleCustomIncomingData(trimmedData)){
					LOG.error("Ignoring received data "+ trimmedData);
					applicativeLogService.warning("Ignoring received data "+ trimmedData, "Grbl Communicator");
				}
			}
		}
	}

	/**
	 * @param trimmedData
	 */
	protected abstract void handleMessage(String trimmedData) throws GkException;
	

	/**
	 * Allow subclasses to handle custom data
	 * @param trimmedData the raw data
	 * @return <code>true</code> if data were processed, <code>false</code> otherwise
	 */
	protected boolean handleCustomIncomingData(String trimmedData) {
		return false;
	}

	/**
	 * @param trimmedData
	 */
	protected abstract void handleProbeResult(String trimmedData) throws GkException;

	/**
	 * @param trimmedData
	 */
	protected abstract void handleParserState(String trimmedData) throws GkException;

	/**
	 * @param trimmedData
	 */
	protected abstract void handleToolLengthOffset(String trimmedData) throws GkException;

	/**
	 * @param trimmedData
	 */
	protected abstract void handleAlarm(String trimmedData) throws GkException;

	/**
	 * @param trimmedData
	 * @throws GkException 
	 */
	protected void handleCoordinateSystemOffset(String trimmedData) throws GkException{
		Tuple6b targetPoint = new Tuple6b().setNull();
		String offsetName = parseCoordinateSystem(trimmedData, targetPoint);
		controllerService.setCoordinateSystemOffset(new CoordinateSystemFactory().get(offsetName), targetPoint);
	}
	
	/**
	 * @param trimmedData
	 */
	protected void handleConfigurationReading(String configurationMessage) throws GkException{
		String identifier = StringUtils.substringBefore(configurationMessage, "=").trim();
		String value 	  = StringUtils.substringAfter(configurationMessage, "=").trim();
		getControllerService().setConfigurationSetting(identifier, value);		
		LOG.info("Updating setting '"+identifier+"' with value '"+value+"'");
	}

	/**
	 * @param parseStatusReport
	 */
	protected abstract void handleStatusReport(String trimmedData) throws GkException ;

	/**
	 * @param trimmedData
	 */
	protected abstract void handleError(String trimmedData) throws GkException ;

	/**
	 * 
	 */
	protected abstract void handleOkResponse() throws GkException ;

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
	 * Create a probe resultfrom the given string
	 * @param strProbeReport the String representing the probe report
	 * @return {@link ProbeResult}
	 * @throws GkException GkException
	 */
	private ProbeResult parseProbeResult(String strProbeReport) throws GkException{
		String[] tokens = StringUtils.split(strProbeReport, ":");
		int probeSuccess = Integer.valueOf(tokens[2]);
		String[] pos = StringUtils.split(tokens[1], ",");
		Length x = Length.valueOf(pos[0], controllerService.getReportUnit());
		Length y = Length.valueOf(pos[0], controllerService.getReportUnit());
		Length z = Length.valueOf(pos[0], controllerService.getReportUnit());		
		ProbeResult result = new ProbeResult();
		result.setProbed(probeSuccess == 1);
		result.setProbedPosition(new Tuple6b(x, y,z));
		return result;
	}
	/**
	 * Return the Grbl state matching the given String
	 * @param state the desired state 
	 * @return GrblMachineState
	 */
	protected abstract M getGrblStateFromString(String state);

	protected void parseTuple(Tuple6b target, String... values) throws GkException{	
		if(values != null && values.length >= 3){
			Unit<Length> unit = controllerService.getReportUnit();
			if(NumberUtils.isNumber(values[0])){
				target.setX(Length.valueOf(new BigDecimal(values[0]), unit));
			}
			if(NumberUtils.isNumber(values[1])){
				target.setY(Length.valueOf(new BigDecimal(values[1]), unit));
			}
			if(NumberUtils.isNumber(values[2])){
				target.setZ(Length.valueOf(new BigDecimal(values[2]), unit));
			}
		}
	}
	private String parseCoordinateSystem(String strOrigin, Tuple6b targetPoint) throws GkException{
		String identifier = StringUtils.substringBetween(strOrigin, "[", ":");
		String valuesGroup = StringUtils.substringBetween(strOrigin, ":", "]");
		String[] values = StringUtils.split(valuesGroup, ",");
		Unit<Length> unit = controllerService.getReportUnit();
		if(values == null || values.length < 3){
			throw new GkFunctionalException("Received incomplete offset report "+strOrigin+". Ignoring...");
		}

		BigDecimal x = new BigDecimal(values[0]);
		BigDecimal y = new BigDecimal(values[1]);
		BigDecimal z = new BigDecimal(values[2]);
		targetPoint.setX(Length.valueOf(x, unit));
		targetPoint.setY(Length.valueOf(y, unit));
		targetPoint.setZ(Length.valueOf(z, unit));

		return identifier;
	}
	
	/**
	 * Add the end line character at the end of the given list
	 * @param list the list
	 */
	private void addEndLineCharacter(List<Byte> list){
		for (char endChar : endLineCharacters) {
			list.add(new Byte((byte) endChar));	
		}	
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
	public void setConnectionService(IConnectionService connectionService) throws GkException {
		this.connectionService = connectionService;
		this.connectionService.addConnectionListener(this);		
	}

	/**
	 * Sends the given string as a list of byte over the connection service
	 * @param data the String to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void send(String data, boolean useEndLineCharacter) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList(data);
		if(useEndLineCharacter){
			addEndLineCharacter(lstBytes);
		}
		getConnectionService().send(lstBytes);
	}
	
	/**
	 * Sends the given list of byte over the connection service
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void send(List<Byte> lstByte, boolean useEndLineCharacter) throws GkException{
		if(useEndLineCharacter){
			addEndLineCharacter(lstByte);
		}
		getConnectionService().send(lstByte);
	}

	/**
	 * Sends the given list of byte over the connection service with high priority
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void sendImmediately(String data, boolean useEndLineCharacter) throws GkException{
		List<Byte> lstBytes = GkUtils.toBytesList(data);
		if(useEndLineCharacter){
			addEndLineCharacter(lstBytes);
		}
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}
	
	/**
	 * Sends the given byte over the connection service with high priority
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void sendImmediately(byte data, boolean useEndLineCharacter) throws GkException{
		List<Byte> lstBytes = new ArrayList<>();
		lstBytes.add(data);
		if(useEndLineCharacter){
			addEndLineCharacter(lstBytes);
		}
		getConnectionService().send(lstBytes, DataPriority.IMPORTANT);
	}
	
	/**
	 * Sends the given list of byte over the connection service with high priority
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void sendImmediately(List<Byte> lstByte, boolean useEndLineCharacter) throws GkException{
		if(useEndLineCharacter){
			addEndLineCharacter(lstByte);
		}
		getConnectionService().send(lstByte, DataPriority.IMPORTANT);
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * @return the controllerService
	 */
	public S getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(S controllerService) {
		this.controllerService = controllerService;
	}

	/**
	 * @return the applicativeLogService
	 */
	public IApplicativeLogService getApplicativeLogService() {
		return applicativeLogService;
	}

	/**
	 * @param applicativeLogService the applicativeLogService to set
	 */
	public void setApplicativeLogService(IApplicativeLogService applicativeLogService) {
		this.applicativeLogService = applicativeLogService;
	}
}
