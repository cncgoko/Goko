package org.goko.autoleveler.modifier.ui;

import org.goko.autoleveler.bean.grid.GridHeightMapBuilder;
import org.goko.autoleveler.modifier.AutoLevelerModifier;
import org.goko.core.common.exception.GkException;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController;

public class AutoLevelerModifierConfigurationController extends AbstractModifierPanelController<AutoLevelerModifierConfigurationModel, AutoLevelerModifier>{
	private GridHeightMapBuilder builder;

	public AutoLevelerModifierConfigurationController() {
		super(new AutoLevelerModifierConfigurationModel());
		builder = new GridHeightMapBuilder();
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		getDataModel().setStartCoordinateX(builder.getStart().getX());
		getDataModel().setStartCoordinateY(builder.getStart().getY());
		getDataModel().setEndCoordinateX(builder.getEnd().getX());
		getDataModel().setEndCoordinateY(builder.getEnd().getY());
		getDataModel().setStepSizeX(builder.getStepSizeX());
		getDataModel().setStepSizeY(builder.getStepSizeY());
		getDataModel().setClearanceHeight(builder.getClearanceHeight());
		getDataModel().setProbeStartHeight(builder.getProbeStartHeight());
		getDataModel().setProbeLowerHeight(builder.getProbeLowerHeight());
		getDataModel().setProbeFeedrate(builder.getProbeFeedrate());
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		getDataModel().setExpectedHeight(getModifier().getTheoricHeight());
	}
	
	/**
	 * @return the builder
	 */
	public GridHeightMapBuilder getBuilder() {
		return builder;
	}

	/**
	 * @param builder the builder to set
	 */
	public void setBuilder(GridHeightMapBuilder builder) {
		this.builder = builder;
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPanelController#updateModifier()
	 */
	@Override
	protected AutoLevelerModifier updateModifier() throws GkException {
		AutoLevelerModifier modifier = getModifier();
		modifier.setHeightMap(builder.getMap());
		modifier.setTheoricHeight(getDataModel().getExpectedHeight());
		return modifier;
	}

}
