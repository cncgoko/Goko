/**
 * 
 */
package org.goko.controller.tinyg.commons;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.controller.tinyg.commons.configuration.TinyGGroupSettings;
import org.goko.core.common.GkUtils;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.connection.DataPriority;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.serial.ISerialConnectionService;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumCoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.EnumUnit;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 * @author Psyko
 * @date 6 janv. 2017
 */
public abstract class AbstractTinyGCommunicator<C extends AbstractTinyGConfiguration<C>, S extends ITinyGControllerService<C>> implements IConnectionDataListener, IConnectionListener{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractTinyGCommunicator.class);
	/** The connection service */
	private ISerialConnectionService connectionService;
	/** Buffer for incoming data	 */
	private ByteCommandBuffer incomingBuffer;
	/** End line characters */
	private List<Character> endLineCharacters;
	/** Connected flag */
	private boolean connected;
	/** The running TinyG service*/
	private S controllerService;
	/** UI Log service */	
	private IApplicativeLogService applicativeLogService;
	// TODO : remove queue field
	protected ConcurrentLinkedQueue<String> queue;
	
	/**
	 * Constructor 
	 */
	public AbstractTinyGCommunicator() {
		this.incomingBuffer    = new ByteCommandBuffer((byte) '\n');
		this.endLineCharacters = new ArrayList<Character>();
		setEndLineCharacters('\n');
		queue = new ConcurrentLinkedQueue<>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionListener#onConnectionEvent(org.goko.core.connection.EnumConnectionEvent)
	 */
	@Override
	public final void onConnectionEvent(EnumConnectionEvent event) throws GkException {
		if(event == EnumConnectionEvent.CONNECTED){
			System.err.println("connected");
			connected = true;
			onConnected();			
		}else if(event == EnumConnectionEvent.DISCONNECTED){
			connected = false;
			onDisconnected();
		}	
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public final void onDataReceived(List<Byte> data) throws GkException {		
		incomingBuffer.addAll(data);

		while(incomingBuffer.hasNext()){
			try {
				String next = GkUtils.toString(incomingBuffer.unstackNextCommand());				
				handleIncomingData(next);
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataSent(java.util.List)
	 */
	@Override
	public final void onDataSent(List<Byte> data) throws GkException { }
	
	/**
	 * @return the connectionService
	 */
	public ISerialConnectionService getConnectionService() {
		return connectionService;
	}
	
	/**
	 * @param connectionService the connectionService to set
	 * @throws GkException
	 */
	public void setConnectionService(ISerialConnectionService connectionService) throws GkException {
		if(this.connectionService != null){
			this.connectionService.removeConnectionListener(this);
			this.connectionService.removeInputDataListener(this);
			this.connectionService.removeOutputDataListener(this);
		}
		this.connectionService = connectionService;
		connectionService.addConnectionListener(this);
		System.err.println("Adding listener "+this);
	}
	
	/**
	 * Set the end line characters 
	 * @param chars the end line characters
	 */
	public void setEndLineCharacters(char... chars){
		this.endLineCharacters = Arrays.asList(ArrayUtils.toObject(chars));
		this.incomingBuffer.setCommandDelimiter(chars[chars.length - 1]);
	}
	
	/**
	 * Add the end line character at the end of the given list
	 * @param list the list
	 */
	protected final  void addEndLineCharacter(List<Byte> list){
		for (char endChar : endLineCharacters) {
			list.add(new Byte((byte) endChar));	
		}		
	}
		
	/**
	 * Sends the given JsonValue over the connection service
	 * @param data the JsonValue to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void send(JsonValue data, boolean useEndLineCharacter) throws GkException{
		send(data.toString(), useEndLineCharacter);
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
	 * Sends the given GCode over the connection service
	 * @param gcode the gcode to send
	 * @throws GkException GkException
	 */
	public final void sendGCode(String gcode) throws GkException{
		queue.add(gcode+" "); // Add " " to simulate end line char
		send(gcode, true);
	}
	
	/**
	 * Sends the given byte over the connection service
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void send(byte byteCommand, boolean useEndLineCharacter) throws GkException{
		List<Byte> lstByte = Arrays.asList(byteCommand);
		send(lstByte, useEndLineCharacter);
	}
	
	/**
	 * Sends the given JSonValue over the connection service with high priority
	 * @param data the JsonValue to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void sendImmediately(JsonValue data, boolean useEndLineCharacter) throws GkException{
		sendImmediately(data.toString(), useEndLineCharacter);
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
	 * Sends the given list of byte over the connection service with high priority
	 * @param lstByte the list of byte to send
	 * @param useEndLineCharacter <code>true</code> to append end line characters before sending, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public final void sendImmediately(byte byteCommand, boolean useEndLineCharacter) throws GkException{
		List<Byte> lstByte = Arrays.asList(byteCommand);
		sendImmediately(lstByte, useEndLineCharacter);
	}
	/**
	 * Handdle incoming datas 
	 * @param data the received data
	 * @throws GkException GkException
	 */
	protected void handleIncomingData(String data) throws GkException{		
		String trimmedData = StringUtils.trim(data);
		if(StringUtils.isNotEmpty(trimmedData)){
			if(TinyGJsonUtils.isJsonFormat(trimmedData)){
				JsonObject response = null;
				try{
					response = JsonObject.readFrom(trimmedData);
				}catch(Exception e){
					String msg = "Error while parsing JSon for string '"+trimmedData+"'"+System.lineSeparator()+e.getMessage();					
					LOG.error(msg);					
					handleMalformedJson(trimmedData);
					return;
				}
				
				JsonValue footerBody = response.get(TinyG.FOOTER);
				ITinyGStatus status = null;				
				
				if(footerBody != null){
					status = getResponseFooter(footerBody);
				}

				JsonValue responseBody = response.get(TinyG.RESPONSE_ENVELOPE);
				if(responseBody != null){
					handleResponse((JsonObject) responseBody, status);
				}
				JsonValue statusReport = response.get(TinyG.STATUS_REPORT);
				if(statusReport != null){
					handleStatusReport((JsonObject) statusReport);
				}
				JsonValue queueReport = response.get(TinyG.QUEUE_REPORT);
				if(queueReport != null){
					handleQueueReport((JsonValue)queueReport);
				}
				JsonValue errorReport = response.get(TinyG.ERROR_REPORT);
				if(errorReport != null){
					handleErrorReport((JsonObject)errorReport);
				}
				
			}else{
				handleNonJsonData(trimmedData);
			}
		}
	}
		
	
	protected abstract ITinyGStatus getResponseFooter(JsonValue footerBody) throws GkException;

	protected abstract void handleResponse(JsonObject responseBody, ITinyGStatus status) throws GkException;
	
	protected abstract void handleStatusReport(JsonObject statusReportBody) throws GkException;
	
	protected abstract void handleQueueReport(JsonValue queueReportBody) throws GkException;
	
	protected abstract void handleErrorReport(JsonObject errorReportBody) throws GkException;
	
	protected abstract void handleNonJsonData(String data) throws GkException;
	
	protected abstract void handleMalformedJson(String data);
	
	protected abstract void onConnected() throws GkException;
	
	protected abstract void onDisconnected() throws GkException;

	/**
	 * @return the incomingBuffer
	 */
	public ByteCommandBuffer getIncomingBuffer() {
		return incomingBuffer;
	}

	/**
	 * @return the connected
	 */
	public boolean isConnected() {
		return connected;
	}

	// Defaults implementations
	
	/**
	 * Finds the units declaration in the status report
	 * @param statusReport the status report
	 * @return {@link EnumGCodeCommandUnit}
	 * @throws GkException GkException 
	 */
	protected EnumUnit findUnits(JsonObject statusReport) throws GkException{
		JsonValue unitReport = statusReport.get(TinyG.STATUS_REPORT_UNITS);
		if(unitReport != null){
			int units = unitReport.asInt();
			if(units == 1){
				return EnumUnit.MILLIMETERS;
			}else{
				return EnumUnit.INCHES;
			}
		}
		return controllerService.getGCodeContext().getUnit();
	}
	
	/**
	 * Update the current work position using the given status report
	 * @param statusReport the status report
	 * @param unit the unit in use
	 * @return Tuple6b
	 * @throws GkException
	 */
	protected Tuple6b findWorkPosition(JsonObject statusReport, EnumUnit unit) throws GkException{
		Tuple6b 	workPosition = new Tuple6b(controllerService.getGCodeContext().getPosition());
		JsonValue newPositionX = statusReport.get(TinyG.STATUS_REPORT_WORK_POSITION_X);
		JsonValue newPositionY = statusReport.get(TinyG.STATUS_REPORT_WORK_POSITION_Y);
		JsonValue newPositionZ = statusReport.get(TinyG.STATUS_REPORT_WORK_POSITION_Z);
		JsonValue newPositionA = statusReport.get(TinyG.STATUS_REPORT_WORK_POSITION_A);
		if(newPositionX != null){
			workPosition.setX( Length.valueOf(newPositionX.asBigDecimal(), unit.getUnit()));
		}
		if(newPositionY != null){
			workPosition.setY( Length.valueOf(newPositionY.asBigDecimal() , unit.getUnit()));
		}
		if(newPositionZ != null){
			workPosition.setZ( Length.valueOf(newPositionZ.asBigDecimal() , unit.getUnit()));
		}
		if(newPositionA != null){
			workPosition.setA( Angle.valueOf(newPositionA.asBigDecimal() , AngleUnit.DEGREE_ANGLE));
		}
		
		return workPosition;
	}
	
	/**
	 * Update the current work position using the given status report
	 * @param statusReport the status report
	 * @param unit the unit in use
	 * @return Tuple6b
	 * @throws GkException
	 */
	protected Tuple6b findMachinePosition(JsonObject statusReport, EnumUnit unit) throws GkException{
		Tuple6b 	machinePosition = new Tuple6b(controllerService.getGCodeContext().getMachinePosition());
		JsonValue newPositionX = statusReport.get(TinyG.STATUS_REPORT_MACHINE_POSITION_X);
		JsonValue newPositionY = statusReport.get(TinyG.STATUS_REPORT_MACHINE_POSITION_Y);
		JsonValue newPositionZ = statusReport.get(TinyG.STATUS_REPORT_MACHINE_POSITION_Z);
		JsonValue newPositionA = statusReport.get(TinyG.STATUS_REPORT_MACHINE_POSITION_A);
		if(newPositionX != null){
			machinePosition.setX( Length.valueOf(newPositionX.asBigDecimal(), unit.getUnit()));
		}
		if(newPositionY != null){
			machinePosition.setY( Length.valueOf(newPositionY.asBigDecimal() , unit.getUnit()));
		}
		if(newPositionZ != null){
			machinePosition.setZ( Length.valueOf(newPositionZ.asBigDecimal() , unit.getUnit()));
		}
		if(newPositionA != null){
			machinePosition.setA( Angle.valueOf(newPositionA.asBigDecimal() , AngleUnit.DEGREE_ANGLE));
		}
		
		return machinePosition;
	}
	
	/**
	 * Finds the current distance mode from the given status report 
	 * @param statusReport the status report 
	 * @return EnumDistanceMode
	 * @throws GkException GkException 
	 */
	protected EnumDistanceMode findDistanceMode(JsonObject statusReport) throws GkException{
		JsonValue distReport = statusReport.get(TinyG.STATUS_REPORT_DISTANCE_MODE);
		if(distReport != null){
			int dist = distReport.asInt();
			if(dist == 0){
				return EnumDistanceMode.ABSOLUTE;
			}else{
				return EnumDistanceMode.RELATIVE;
			}
		}
		return controllerService.getGCodeContext().getDistanceMode();
	}
	
	/**
	 * Finds the current velocity mode from the given status report 
	 * @param statusReport the status report 
 	 * @param unit the unit in use
	 * @return Speed
	 * @throws GkException GkException 
	 */
	protected Speed findVelocity(JsonObject statusReport, EnumUnit unit){
		JsonValue velocityReport = statusReport.get(TinyG.STATUS_REPORT_VELOCITY);
		if(velocityReport != null){			
			Unit<Speed> speedUnit = SpeedUnit.MILLIMETRE_PER_MINUTE;
			if(EnumUnit.INCHES.equals(unit)){
				speedUnit = SpeedUnit.INCH_PER_MINUTE;
			}
			return Speed.valueOf(velocityReport.asBigDecimal(), speedUnit);
		}
		return null;
	}
	
	/**
	 * Finds the current feedrate mode from the given status report 
	 * @param statusReport the status report 
 	 * @param unit the unit in use
	 * @return Speed
	 * @throws GkException GkException 
	 */
	protected Speed findFeedrate(JsonObject feedrate,  EnumUnit unit){
		JsonValue feedrateReport = feedrate.get(TinyG.STATUS_REPORT_FEEDRATE);
		if(feedrateReport != null){
			Unit<Speed> speedUnit = SpeedUnit.MILLIMETRE_PER_MINUTE;
			if(EnumUnit.INCHES.equals(unit)){
				speedUnit = SpeedUnit.INCH_PER_MINUTE;	
			}
			return Speed.valueOf(feedrateReport.asBigDecimal().setScale(3, BigDecimal.ROUND_HALF_EVEN), speedUnit);
		}
		return null;
	}
	
	/**
	 * Finds the current coordinate system mode from the given status report
	 * 0=g53, 1=g54, 2=g55, 3=g56, 4=g57, 5=g58, 6=g59 
	 * @param statusReport the status report 
	 * @return EnumCoordinateSystem
	 * @throws GkException GkException 
	 */	
	protected EnumCoordinateSystem findCoordinateSystem(JsonObject statusReport){
		EnumCoordinateSystem coordinateSystem = null;
		JsonValue coordReport = statusReport.get(TinyG.STATUS_REPORT_COORDINATES);
		if(coordReport != null){
			int units = coordReport.asInt();
			switch(units){
			case 0: coordinateSystem = EnumCoordinateSystem.G53;
			break;
			case 1: coordinateSystem = EnumCoordinateSystem.G54;
			break;
			case 2: coordinateSystem = EnumCoordinateSystem.G55;
			break;
			case 3: coordinateSystem = EnumCoordinateSystem.G56;
			break;
			case 4: coordinateSystem = EnumCoordinateSystem.G57;
			break;
			case 5: coordinateSystem = EnumCoordinateSystem.G58;
			break;
			case 6: coordinateSystem = EnumCoordinateSystem.G59;
			break;
			}
		}
		return coordinateSystem;
	}
	
	/**
	 * Update the configuration by sending it through the communicator
	 * @throws GkException GkException 
	 */
	public void sendConfigurationUpdate(AbstractTinyGConfiguration<?> configuration) throws GkException {		
		for(TinyGGroupSettings group: configuration.getGroups()){
			JsonObject jsonGroup = TinyGJsonUtils.toCompleteJson(group);
			if(jsonGroup != null){
				send( jsonGroup.toString(), true );
			}
		}
	}
	
	/**
	 * Build a JSon object using the gioven header
	 * @param header the header
	 * @return a JsonValue
	 */
	protected JsonValue buildJsonQuery(String header){
		return buildJsonQuery(header, StringUtils.EMPTY);
	}
	
	/**
	 * Build a JSon object using the given header and value
	 * @param header the header
	 * @return a JsonValue
	 */
	protected JsonValue buildJsonQuery(String header, String value){
		JsonObject query = new JsonObject();
		query.add(header, value);
		return query;
	}
	
	/**
	 * Forces TinyG to use JSon mode
	 * @throws GkException GkException
	 */
	public void forceJsonMode() throws GkException{		
		send(new JsonObject().add(TinyG.JSON_SYNTAX, TinyG.JSON_SYNTAX_STRICT), true);
	}
	
	/**
	 * Sends a status report query
	 * @throws GkException GkException
	 */
	public void requestStatusReport() throws GkException{
		if(isConnected()){
			send(buildJsonQuery(TinyG.STATUS_REPORT), true);
		}
	}
	
	/**
	 * Sends a queue report query
	 * @throws GkException GkException
	 */
	public void requestQueueReport() throws GkException{
		if(isConnected()){
			send(buildJsonQuery(TinyG.QUEUE_REPORT), true);
		}
	}

	/**
	 * Sends a coordinate system update query
	 * @param coordinateSystem the coordinate system to update
	 * @throws GkException GkException
	 */
	public void requestCoordinateSystemUpdate(ICoordinateSystem coordinateSystem)throws GkException{
		if(isConnected()){
			send(buildJsonQuery(coordinateSystem.getCode()), true);
		}
	}
	
	/**
	 * Sends the request for a configuration update
	 * @throws GkException GkException
	 */
	public void requestConfigurationUpdate() throws GkException{
		if(isConnected()){			
			C cfg = getControllerService().getConfiguration();
			List<TinyGGroupSettings> lstGroups = cfg.getGroups();
			if(CollectionUtils.isNotEmpty(lstGroups)){
				for (TinyGGroupSettings groupSettings : lstGroups) {
					send(buildJsonQuery(groupSettings.getGroupIdentifier()), true);
				}
			}
		}
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
