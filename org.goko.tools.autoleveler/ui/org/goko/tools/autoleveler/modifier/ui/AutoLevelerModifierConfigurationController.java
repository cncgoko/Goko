package org.goko.tools.autoleveler.modifier.ui;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.gcode.element.ICoordinateSystem;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.rs274ngcv3.context.CoordinateSystem;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;
import org.goko.tools.autoleveler.modifier.GridAutoLevelerModifier;

public class AutoLevelerModifierConfigurationController extends AbstractModifierPanelController<AutoLevelerModifierConfigurationModel, GridAutoLevelerModifier>{
	private static final GkLog LOG = GkLog.getLogger(AutoLevelerModifierConfigurationController.class);
	@Inject
	@Optional
	private IProbingService probingService;
	@Inject	
	private IExecutionService executionService;
	@Inject	
	private ICoordinateSystemAdapter<ICoordinateSystem> coordinateSystemAdapter;
	
	public AutoLevelerModifierConfigurationController() {
		super(new AutoLevelerModifierConfigurationModel());	
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {		
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
		
		getDataModel().setExpectedHeight(getModifier().getTheoricHeight());
		getDataModel().setStartCoordinateX(getModifier().getHeightMap().getStart().getX());
		getDataModel().setStartCoordinateY(getModifier().getHeightMap().getStart().getY());
		getDataModel().setEndCoordinateX(getModifier().getHeightMap().getEnd().getX());
		getDataModel().setEndCoordinateY(getModifier().getHeightMap().getEnd().getY());
		getDataModel().setStepSizeX(new BigDecimal(getModifier().getHeightMap().getxDivisionCount()));
		getDataModel().setStepSizeY(new BigDecimal(getModifier().getHeightMap().getyDivisionCount()));
		getDataModel().setClearanceHeight(getModifier().getHeightMap().getClearanceHeight());
		getDataModel().setProbeStartHeight(getModifier().getHeightMap().getProbeStartHeight());
		getDataModel().setProbeLowerHeight(getModifier().getHeightMap().getProbeLowerHeight());
		getDataModel().setProbeFeedrate(getModifier().getHeightMap().getProbeFeedrate());
		getDataModel().setMoveFeedrate(getModifier().getHeightMap().getMoveFeedrate());
		getDataModel().setModificationAllowed(!getModifier().getHeightMap().isProbed());
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected GridAutoLevelerModifier updateModifier() throws GkException {
		GridAutoLevelerModifier modifier = getModifier();
		if(!modifier.getHeightMap().isProbed()){
			modifier.setTheoricHeight(getDataModel().getExpectedHeight());
			modifier.getHeightMap().setStart(new Tuple6b(getDataModel().getStartCoordinateX(), getDataModel().getStartCoordinateY(), Length.ZERO));
			modifier.getHeightMap().setEnd(new Tuple6b(getDataModel().getEndCoordinateX(), getDataModel().getEndCoordinateY(), Length.ZERO));
			modifier.getHeightMap().setxDivisionCount(getDataModel().getStepSizeX().intValue());
			modifier.getHeightMap().setyDivisionCount(getDataModel().getStepSizeY().intValue());
			modifier.getHeightMap().setProbeStartHeight(getDataModel().getProbeStartHeight());
			modifier.getHeightMap().setProbeLowerHeight(getDataModel().getProbeLowerHeight());
			modifier.getHeightMap().setProbeFeedrate(getDataModel().getProbeFeedrate());
			modifier.getHeightMap().setClearanceHeight(getDataModel().getClearanceHeight());
			modifier.getHeightMap().build();
		}
		return modifier;
	}

	/**
	 * Clear the probed datas and allows modifications 
	 */
	public void clearProbedData() {
		GridAutoLevelerModifier modifier = getModifier();
		modifier.getHeightMap().setProbed(false);
		getDataModel().setModificationAllowed(true);
	}
	public boolean isProbingService(){
		return probingService != null;
	}
	private ProbeRequest getProbeRequest(Tuple6b tuple6b){
		ProbeRequest request = new ProbeRequest();
		request.setAxis(EnumControllerAxis.Z_POSITIVE);
		request.setClearance(getDataModel().getClearanceHeight());
		request.setProbeStart(getDataModel().getProbeStartHeight());
		request.setProbeEnd(getDataModel().getProbeLowerHeight());
		request.setProbeFeedrate(getDataModel().getProbeFeedrate());
		request.setMotionFeedrate(getDataModel().getMoveFeedrate());
		request.setProbeCoordinate(tuple6b);
		return request;
	}
	
	public void startMapProbing() throws GkException{
		getModifier().getHeightMap().build();
		probingService.checkReadyToProbe();
		if(executionService.getExecutionState() ==  ExecutionState.PAUSED
		|| executionService.getExecutionState() ==  ExecutionState.RUNNING){
			throw new GkFunctionalException("Some GCode is being executed. Please wait for it to complete before probing.");
		}
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				GridHeightMap map = getModifier().getHeightMap();
				List<Tuple6b> mapPoints = map.getOffsets();
				List<ProbeRequest> requests = new ArrayList<ProbeRequest>();
				
				for(int x = 0; x <= map.getxDivisionCount(); x++){
					if(x%2==0){
						for(int y = 0; y <= map.getyDivisionCount(); y++){
							requests.add(getProbeRequest(map.getPoint(x, y)));
						}
					}else{
						for(int y = map.getyDivisionCount(); y >= 0 ; y--){
							requests.add(getProbeRequest(map.getPoint(x, y)));
						}
					}
				}
				
				try {
					CompletionService<ProbeResult> result = probingService.probe(requests);
					int i = 0;
					while(i < mapPoints.size()){
						Future<ProbeResult> futureProbeResult = result.take();
						ProbeResult probeResult = futureProbeResult.get();
						if(probeResult == null){
							LOG.info("Probe result is null. Probably cancelled.");
							continue;
						}
						
							int x = probeResult.getProbedPosition().getX().subtract(map.getStart().getX()).divide(map.getStepSizeX()).intValue();
							int y = probeResult.getProbedPosition().getY().subtract(map.getStart().getY()).divide(map.getStepSizeY()).intValue();
						if(probeResult.isProbed()){
							LOG.info("Probed "+probeResult.getProbedPosition().getZ());
							map.getPoint(x, y).setZ(probeResult.getProbedPosition().getZ());
						}else{
							LOG.info("Could not find the probed position within range");
							map.setInvalidProbe(x, y);
							map.getPoint(x, y).setZ(Length.valueOf(0, LengthUnit.MILLIMETRE));
						}
						i++;
					}
					map.setProbed(true);
					performUpdateModifier();
					initializeFromModifier();
				} catch (GkException e) {
					LOG.error(e);
				} catch (InterruptedException e) {
					LOG.error(e);
				} catch (ExecutionException e) {
					LOG.error(e);
				}	
			}
		});
		
	}
}
