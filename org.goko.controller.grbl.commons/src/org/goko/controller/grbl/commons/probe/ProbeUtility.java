/**
 * 
 */
package org.goko.controller.grbl.commons.probe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.context.EnumDistanceMode;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.SetDistanceModeInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.SetFeedRateInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightFeedInstruction;
import org.goko.core.gcode.rs274ngcv3.instruction.StraightProbeInstruction;
import org.goko.core.math.Tuple6b;

/**
 * GRBL Probe utility 
 * 
 * @author Psyko
 * @date 18 avr. 2017
 */
public class ProbeUtility {
	private List<ProbeCallable> lstProbeCallable;
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	private ExecutorCompletionService<ProbeResult> completionService;
	private IGCodeProvider probeGCodeProvider;
	private IRS274NGCService gcodeService;
	
	/**
	 * Constructor
	 * @param gcodeContextProvider the gcode context provider
	 * @param gcodeService the gcode service
	 */
	public ProbeUtility(IGCodeContextProvider<GCodeContext> gcodeContextProvider, IRS274NGCService gcodeService) {
		super();
		this.gcodeContextProvider = gcodeContextProvider;	
		this.gcodeService = gcodeService;
	}
		
	/**
	 * Prepares the list of probe request
	 * @param lstProbeRequest the list of request to perform
	 * @throws GkException GkException
	 */
	public void prepare(List<ProbeRequest> lstProbeRequest) throws GkException {
		Executor executor = Executors.newSingleThreadExecutor();
		completionService = new ExecutorCompletionService<ProbeResult>(executor);		
		this.lstProbeCallable = new ArrayList<>();
		
		for (int i = 0; i < lstProbeRequest.size(); i++) {
			ProbeCallable probeCallable = new ProbeCallable();
			this.lstProbeCallable.add(probeCallable);
			this.completionService.submit(probeCallable);
		}
		
		probeGCodeProvider = getProbingCode(lstProbeRequest, gcodeContextProvider.getGCodeContext());
		probeGCodeProvider.setCode("Grbl probing");	
		gcodeService.addGCodeProvider(probeGCodeProvider);
		probeGCodeProvider = gcodeService.getGCodeProvider(probeGCodeProvider.getId());// Required since internally the provider is a new one		
	}
	
	public ExecutorCompletionService<ProbeResult> getExecutorCompletionService(){
		return completionService;
	}
	
	/**
	 * Checks if a probe sequence is running
	 * @return <code>true</code> if a probe is running, <code>false</code> otherwise
	 */
	public boolean isProbingInProgress(){
		return CollectionUtils.isNotEmpty(lstProbeCallable);
	}
	
	/**
	 * Handle the probe result 
	 * @param probed <code>true</code> if the probe succeed, <code>false</code> otherwise
	 * @param position the probed position
	 * @throws GkException GkException
	 */
	public void handleProbeResult(boolean probed, Tuple6b position) throws GkException{
		if(isProbingInProgress()){
			ProbeResult probeResult = new ProbeResult();
			probeResult.setProbed(probed);
			probeResult.setProbedPosition(position);
			
			ProbeCallable callable = lstProbeCallable.remove(0);
			callable.setProbeResult(probeResult);
		}
	}
	
	public void cancelActiveProbing() throws GkException{
		if(CollectionUtils.isNotEmpty(lstProbeCallable)){
			for (ProbeCallable probeCallable: lstProbeCallable) {
				probeCallable.setProbeResult(null);
			}
			lstProbeCallable.clear();			
		}
	}
	
	public void clearProbingGCode() throws GkException{
		if(probeGCodeProvider != null){
			gcodeService.deleteGCodeProvider(probeGCodeProvider.getId());
			probeGCodeProvider = null;
		}
	}
	
	private IGCodeProvider getProbingCode(List<ProbeRequest> lstProbeRequest, GCodeContext gcodeContext) throws GkException{		
		InstructionProvider instrProvider = new InstructionProvider();
		// Force distance mode to absolute
		instrProvider.addInstruction( new SetDistanceModeInstruction(EnumDistanceMode.ABSOLUTE) );
		for (ProbeRequest probeRequest : lstProbeRequest) {			
			Tuple6b clearancePoint 	= getProbeAxisPosition(probeRequest, probeRequest.getClearance());
			Tuple6b probeStartPoint = getProbeAxisPosition(probeRequest, probeRequest.getProbeStart());
			Tuple6b probeEndPoint 	= getProbeAxisPosition(probeRequest, probeRequest.getProbeEnd());
			Tuple6b probePoint	   	= getProbePoint(probeRequest);
			// Move to clearance coordinate 
			instrProvider.addInstruction( new SetFeedRateInstruction(probeRequest.getMotionFeedrate()) );
			instrProvider.addInstruction( new StraightFeedInstruction(clearancePoint.getX(), clearancePoint.getY(), clearancePoint.getZ(), null, null, null) );
			// Move to probe position		
			instrProvider.addInstruction( new StraightFeedInstruction(probePoint.getX(), probePoint.getY(), probePoint.getZ(), null, null, null) );
			// Move to probe start position
			instrProvider.addInstruction( new StraightFeedInstruction(probeStartPoint.getX(), probeStartPoint.getY(), probeStartPoint.getZ(), null, null, null) );
			// Actual probe command
			instrProvider.addInstruction( new SetFeedRateInstruction(probeRequest.getProbeFeedrate()) );
			instrProvider.addInstruction( new StraightProbeInstruction(probeEndPoint.getX(), probeEndPoint.getY(), probeEndPoint.getZ(), null, null, null) );
			// Move to clearance coordinate 
			instrProvider.addInstruction( new SetFeedRateInstruction(probeRequest.getMotionFeedrate()) );
			instrProvider.addInstruction( new StraightFeedInstruction(clearancePoint.getX(), clearancePoint.getY(), clearancePoint.getZ(), null, null, null) );
		}		
		
		return gcodeService.getGCodeProvider(gcodeContext, instrProvider);
	}
	
	/**
	 * Generates a position along the probing axis 
	 * @param request the probe request
	 * @param position the position
	 * @return Tuple6b
	 */
	private Tuple6b getProbeAxisPosition(ProbeRequest request, Length position){
		Tuple6b clearance = new Tuple6b().setNull();
		if(request.getAxis() == EnumControllerAxis.X_POSITIVE
		|| request.getAxis() == EnumControllerAxis.X_NEGATIVE){
			clearance.setX(position);
		
		}else if(request.getAxis() == EnumControllerAxis.Y_POSITIVE
		|| request.getAxis() == EnumControllerAxis.Y_NEGATIVE){
			clearance.setY(position);
		
		}else if(request.getAxis() == EnumControllerAxis.Z_POSITIVE
		|| request.getAxis() == EnumControllerAxis.Z_NEGATIVE){
			clearance.setZ(position);
		}
		return clearance;
	}
	
	/**
	 * Generates a position in the plane whose normal is the probe axis 
	 * @param request the probe request
	 * @param position the position
	 * @return Tuple6b
	 */
	private Tuple6b getProbePoint(ProbeRequest request){
		Tuple6b probePoint = new Tuple6b().setNull();
		if(request.getAxis() == EnumControllerAxis.X_POSITIVE
		|| request.getAxis() == EnumControllerAxis.X_NEGATIVE){
			probePoint.setY(request.getProbeCoordinate().getY());
			probePoint.setZ(request.getProbeCoordinate().getZ());
		
		}else if(request.getAxis() == EnumControllerAxis.Y_POSITIVE
		|| request.getAxis() == EnumControllerAxis.Y_NEGATIVE){
			probePoint.setX(request.getProbeCoordinate().getX());
			probePoint.setZ(request.getProbeCoordinate().getZ());
		
		}else if(request.getAxis() == EnumControllerAxis.Z_POSITIVE
		|| request.getAxis() == EnumControllerAxis.Z_NEGATIVE){
			probePoint.setX(request.getProbeCoordinate().getX());
			probePoint.setY(request.getProbeCoordinate().getY());
		}
		return probePoint;
	}

	/**
	 * @return the probeGCodeProvider
	 */
	public IGCodeProvider getProbeGCodeProvider() {
		return probeGCodeProvider;
	}
}
