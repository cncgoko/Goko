/**
 * 
 */
package org.goko.tools.zeroprobe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.common.measure.quantity.TimeUnit;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbeController extends AbstractController<ZeroProbeModel>{
	private static final GkLog LOG = GkLog.getLogger(ZeroProbeController.class);
	@Inject
	@Optional
	private IProbingService probingService;
	@Inject	
	private IControllerService<?, ?> controllerService;
	@Inject	
	private ICoordinateSystemAdapter<ICoordinateSystem> coordinateSystemAdapter;
	
	@Inject	
	private IExecutionService executionService;
	
	/**
	 * 
	 */
	public ZeroProbeController() {
		super(new ZeroProbeModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		List<ICoordinateSystem> lstCoordinateSystem = coordinateSystemAdapter.getCoordinateSystem();
		List<LabeledValue<ICoordinateSystem>> values = new ArrayList<>();
		for (ICoordinateSystem iCoordinateSystem : lstCoordinateSystem) {
			if(!StringUtils.equalsIgnoreCase(iCoordinateSystem.getCode(), CoordinateSystem.G53.getCode())){ // Kind of dirty, but...
				LabeledValue<ICoordinateSystem> labeledValue = new LabeledValue<ICoordinateSystem>(iCoordinateSystem, iCoordinateSystem.getCode());
				values.add( labeledValue );
				if(getDataModel().getCoordinateSystem() == null){
					getDataModel().setCoordinateSystem(labeledValue);
				}
			}
		}
		getDataModel().setCoordinateSystemList(values);
	}
	
	public void executeProbing() throws GkException{
		getDataModel().setError(false);
		getDataModel().setInfoMessage("Probing...");
		getDataModel().setErrorMessage(null);
		probingService.checkReadyToProbe();
		
		Tuple6b position = controllerService.getPosition();
		
		ProbeRequest probeRequest = new ProbeRequest();
		probeRequest.setAxis(getDataModel().getAxis());
		probeRequest.setMotionFeedrate(getDataModel().getFeedrate());
		probeRequest.setProbeFeedrate(getDataModel().getFeedrate());
		probeRequest.setProbeCoordinate(position);
		//probeRequest.setClearance(getAxisPosition(position, Length.ZERO));
		probeRequest.setProbeStart(getAxisPosition(position, Length.ZERO));
		probeRequest.setProbeEnd(getAxisPosition(position, getDataModel().getMaxDistance()));
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(new Runnable(){
			/** (inheritDoc)
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				try {
					CompletionService<ProbeResult> probeCompletionResult = probingService.probe(probeRequest);
					Time waitTime = getDataModel().getMaxDistance().multiply(2).divide(getDataModel().getFeedrate());
					int timeout = (int) waitTime.doubleValue(TimeUnit.SECOND);
									
					Future<ProbeResult> result = probeCompletionResult.poll(timeout, java.util.concurrent.TimeUnit.SECONDS);
					if(result != null && result.isDone()){
						ProbeResult probeResult = result.get();
						if(probeResult != null && probeResult.isProbed()){							
							// Compute new position
							Tuple6b offset = getNewCoordinateSystemOffset(probeResult);
							
							coordinateSystemAdapter.updateCoordinateSystemPosition(getDataModel().getCoordinateSystem().getValue(), offset);
							
							getDataModel().setError(false);
							getDataModel().setInfoMessage("Successfully probed");
						}else{
							getDataModel().setError(true);
							getDataModel().setErrorMessage("Probe did not trigger");
						}
					}else{
						getDataModel().setError(true);
						getDataModel().setErrorMessage("Probe canceled");
					}
				} catch (Exception e) {					
					LOG.error(e);
				}
			}
		});

	}

	/**
	 * @return 
	 * @throws GkException 
	 */
	private Tuple6b getNewCoordinateSystemOffset(ProbeResult probeResult) throws GkException {
		Tuple6b offset = coordinateSystemAdapter.getCoordinateSystemOffset(getDataModel().getCoordinateSystem().getValue());
		Length toolRadius = Length.ZERO;
		if(getDataModel().isToolDiameterCompensation() && getDataModel().getToolDiameter() != null){
			toolRadius  = getDataModel().getToolDiameter().divide(2);
		}
		Length expected = getDataModel().getExpected();
		switch (getDataModel().getAxis()) {
		case X_NEGATIVE: offset.setX( probeResult.getProbedPosition().getX().subtract( toolRadius ).subtract(expected));
			break;
		case X_POSITIVE: offset.setX( probeResult.getProbedPosition().getX().add( toolRadius ).subtract(expected));
			break;
		case Y_NEGATIVE: offset.setY( probeResult.getProbedPosition().getY().subtract( toolRadius ).subtract(expected));
			break;
		case Y_POSITIVE: offset.setY( probeResult.getProbedPosition().getY().add( toolRadius ).subtract(expected));
			break;
		case Z_NEGATIVE: offset.setZ( probeResult.getProbedPosition().getZ().subtract( toolRadius ).subtract(expected));
		break;
		case Z_POSITIVE: offset.setZ( probeResult.getProbedPosition().getZ().add( toolRadius ).subtract(expected));
			break;
		default:
			break;
		}
		return offset;
	}

	/**
	 * @param offset
	 * @return
	 * @throws GkException 
	 */
	private Length getAxisPosition(Tuple6b position, Length offset) throws GkException {
		switch (getDataModel().getAxis()) {
		case X_NEGATIVE: return position.getX().subtract(offset);		
		case X_POSITIVE: return position.getX().add(offset);		
		case Y_NEGATIVE: return position.getY().subtract(offset);		
		case Y_POSITIVE: return position.getY().add(offset);		
		case Z_NEGATIVE: return position.getZ().subtract(offset);		
		case Z_POSITIVE: return position.getZ().add(offset);	
		default: return null;
		}
	}
	
}
