package org.goko.autoleveler.modifier.ui;

import org.goko.autoleveler.bean.grid.GridHeightMapBuilder;
import org.goko.autoleveler.modifier.AutoLevelerModifier;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;

public class AutoLevelerModifierConfigurationController extends AbstractController<AutoLevelerModifierConfigurationModel>{
	private AutoLevelerModifier modifier;
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
		getDataModel().setExpectedHeight(modifier.getTheoricHeight());
	}



	public void updateHeightMap(){

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

	/**
	 * @return the modifier
	 */
	public AutoLevelerModifier getModifier() {
		return modifier;
	}

	/**
	 * @param modifier the modifier to set
	 */
	public void setModifier(AutoLevelerModifier modifier) {
		this.modifier = modifier;
	}

}
