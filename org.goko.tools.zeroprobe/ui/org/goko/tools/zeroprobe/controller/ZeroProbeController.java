/**
 * 
 */
package org.goko.tools.zeroprobe.controller;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 30 juil. 2017
 */
public class ZeroProbeController extends AbstractController<ZeroProbeModel>{
	@Inject
	@Optional
	private IProbingService probingService;
	@Inject	
	private IControllerService<?, ?> controllerService;
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
		// TODO Auto-generated method stub
		
	}
	
	public void executeProbing() throws GkException{
		probingService.checkReadyToProbe();
		
		Tuple6b position = controllerService.getPosition();
		
		ProbeRequest probeRequest = new ProbeRequest();
		probeRequest.setAxis(getDataModel().getAxis());
		probeRequest.setMotionFeedrate(getDataModel().getFeedrate());
		probeRequest.setProbeFeedrate(getDataModel().getFeedrate());
		probeRequest.setProbeCoordinate(position);
		probeRequest.setProbeStart(getAxisPosition(position, Length.ZERO));
		probeRequest.setProbeEnd(getAxisPosition(position, getDataModel().getMaxDistance()));
		
		probingService.probe(probeRequest);
	}

	/**
	 * @param offset
	 * @return
	 */
	private Length getAxisPosition(Tuple6b position, Length offset) {
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
