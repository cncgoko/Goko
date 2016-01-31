package org.goko.autoleveler.modifier.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.autoleveler.bean.grid.GridHeightMap;
import org.goko.autoleveler.modifier.GridAutoLevelerModifier;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.LengthUnit;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.ProbeRequest;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.math.Tuple6b;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

public class AutoLevelerModifierConfigurationController extends AbstractModifierPanelController<AutoLevelerModifierConfigurationModel, GridAutoLevelerModifier>{
	@Inject
	@Optional
	private IProbingService probingService;
	
	
	public AutoLevelerModifierConfigurationController() {
		super(new AutoLevelerModifierConfigurationModel());	
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
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
		getDataModel().setModificationAllowed(!getModifier().getHeightMap().isProbed());
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected GridAutoLevelerModifier updateModifier() throws GkException {
		GridAutoLevelerModifier modifier = getModifier();		
		modifier.setTheoricHeight(getDataModel().getExpectedHeight());
		modifier.getHeightMap().setStart(new Tuple6b(getDataModel().getStartCoordinateX(), getDataModel().getStartCoordinateY(), Length.ZERO));
		modifier.getHeightMap().setEnd(new Tuple6b(getDataModel().getEndCoordinateX(), getDataModel().getEndCoordinateY(), Length.ZERO));
		modifier.getHeightMap().setxDivisionCount(getDataModel().getStepSizeX().intValue());
		modifier.getHeightMap().setyDivisionCount(getDataModel().getStepSizeY().intValue());
		modifier.getHeightMap().setProbeStartHeight(getDataModel().getProbeStartHeight());
		modifier.getHeightMap().setProbeLowerHeight(getDataModel().getProbeLowerHeight());
		modifier.getHeightMap().setProbeFeedrate(getDataModel().getProbeFeedrate());
		modifier.getHeightMap().setClearanceHeight(getDataModel().getClearanceHeight());
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

	public void startMapProbing() throws GkException{
		getModifier().getHeightMap().build();
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				GridHeightMap map = getModifier().getHeightMap();
				List<Tuple6b> mapPoints = map.getOffsets();
				for (Tuple6b tuple6b : mapPoints) {
					ProbeRequest request = new ProbeRequest();
					request.setAxis(EnumControllerAxis.Z_POSITIVE);
					request.setClearance(getDataModel().getClearanceHeight());
					request.setProbeStart(getDataModel().getProbeStartHeight());
					request.setProbeEnd(getDataModel().getProbeLowerHeight());
					request.setProbeFeedrate(getDataModel().getProbeFeedrate().value(LengthUnit.MILLIMETRE));
					request.setMotionFeedrate(new BigDecimal("1000"));
					request.setProbeCoordinate(tuple6b);
					try {
						Future<ProbeResult> result = probingService.probe(request);
						ProbeResult probeResult = result.get();
						tuple6b.setZ(probeResult.getProbedPosition().getZ());
					} catch (GkException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
								
			}
		});
		
	}
}
