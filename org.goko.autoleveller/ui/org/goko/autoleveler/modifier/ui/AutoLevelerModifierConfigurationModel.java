package org.goko.autoleveler.modifier.ui;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;

public class AutoLevelerModifierConfigurationModel extends AbstractModelObject{
	protected static final String X_START_COORDINATE = "startCoordinateX";
	protected static final String Y_START_COORDINATE = "startCoordinateY";
	protected static final String X_END_COORDINATE   = "endCoordinateX";
	protected static final String Y_END_COORDINATE   = "endCoordinateY";
	protected static final String X_STEP   			 = "stepSizeX";
	protected static final String Y_STEP			 = "stepSizeY";
	protected static final String Z_CLEARANCE		 = "clearanceHeight";
	protected static final String Z_EXPECTED		 = "expectedHeight";
	protected static final String Z_PROBE_START		 = "probeStartHeight";
	protected static final String Z_PROBE_LOWER		 = "probeLowerHeight";
	protected static final String PROBE_FEEDRATE	 = "probeFeedrate";

	private BigDecimalQuantity<Length> startCoordinateX;
	private BigDecimalQuantity<Length> startCoordinateY;
	private BigDecimalQuantity<Length> endCoordinateX;
	private BigDecimalQuantity<Length> endCoordinateY;
	private BigDecimalQuantity<Length> stepSizeX;
	private BigDecimalQuantity<Length> stepSizeY;
	private BigDecimalQuantity<Length> clearanceHeight;
	private BigDecimalQuantity<Length> expectedHeight;
	private BigDecimalQuantity<Length> probeStartHeight;
	private BigDecimalQuantity<Length> probeLowerHeight;
	private BigDecimalQuantity<Length> probeFeedrate;
	/**
	 * @return the startCoordinateX
	 */
	public BigDecimalQuantity<Length> getStartCoordinateX() {
		return startCoordinateX;
	}
	/**
	 * @param startCoordinateX the startCoordinateX to set
	 */
	public void setStartCoordinateX(BigDecimalQuantity<Length> startCoordinateX) {
		firePropertyChange(X_START_COORDINATE, startCoordinateX, this.startCoordinateX = startCoordinateX);
	}
	/**
	 * @return the startCoordinateY
	 */
	public BigDecimalQuantity<Length> getStartCoordinateY() {
		return startCoordinateY;
	}
	/**
	 * @param startCoordinateY the startCoordinateY to set
	 */
	public void setStartCoordinateY(BigDecimalQuantity<Length> startCoordinateY) {
		firePropertyChange(Y_START_COORDINATE, startCoordinateY, this.startCoordinateY = startCoordinateY);
	}
	/**
	 * @return the endCoordinateX
	 */
	public BigDecimalQuantity<Length> getEndCoordinateX() {
		return endCoordinateX;
	}
	/**
	 * @param endCoordinateX the endCoordinateX to set
	 */
	public void setEndCoordinateX(BigDecimalQuantity<Length> endCoordinateX) {
		firePropertyChange(X_END_COORDINATE, endCoordinateX, this.endCoordinateX = endCoordinateX);
	}
	/**
	 * @return the endCoordinateY
	 */
	public BigDecimalQuantity<Length> getEndCoordinateY() {
		return endCoordinateY;
	}
	/**
	 * @param endCoordinateY the endCoordinateY to set
	 */
	public void setEndCoordinateY(BigDecimalQuantity<Length> endCoordinateY) {
		firePropertyChange(Y_END_COORDINATE, endCoordinateY, this.endCoordinateY = endCoordinateY);
	}
	/**
	 * @return the stepSizeX
	 */
	public BigDecimalQuantity<Length> getStepSizeX() {
		return stepSizeX;
	}
	/**
	 * @param stepSizeX the stepSizeX to set
	 */
	public void setStepSizeX(BigDecimalQuantity<Length> stepSizeX) {
		firePropertyChange(X_STEP, stepSizeX, this.stepSizeX = stepSizeX);
	}
	/**
	 * @return the stepSizeY
	 */
	public BigDecimalQuantity<Length> getStepSizeY() {
		return stepSizeY;
	}
	/**
	 * @param stepSizeY the stepSizeY to set
	 */
	public void setStepSizeY(BigDecimalQuantity<Length> stepSizeY) {
		firePropertyChange(Y_STEP, stepSizeY, this.stepSizeY = stepSizeY);
	}
	/**
	 * @return the clearanceHeight
	 */
	public BigDecimalQuantity<Length> getClearanceHeight() {
		return clearanceHeight;
	}
	/**
	 * @param clearanceHeight the clearanceHeight to set
	 */
	public void setClearanceHeight(BigDecimalQuantity<Length> clearanceHeight) {
		firePropertyChange(Z_CLEARANCE, clearanceHeight, this.clearanceHeight = clearanceHeight);
	}
	/**
	 * @return the expectedHeight
	 */
	public BigDecimalQuantity<Length> getExpectedHeight() {
		return expectedHeight;
	}
	/**
	 * @param expectedHeight the expectedHeight to set
	 */
	public void setExpectedHeight(BigDecimalQuantity<Length> expectedHeight) {
		firePropertyChange(Z_EXPECTED, expectedHeight, this.expectedHeight = expectedHeight);
	}
	/**
	 * @return the probeStartHeight
	 */
	public BigDecimalQuantity<Length> getProbeStartHeight() {
		return probeStartHeight;
	}
	/**
	 * @param probeStartHeight the probeStartHeight to set
	 */
	public void setProbeStartHeight(BigDecimalQuantity<Length> probeStartHeight) {
		firePropertyChange(Z_PROBE_START, probeStartHeight, this.probeStartHeight = probeStartHeight);
	}
	/**
	 * @return the probeLowerHeight
	 */
	public BigDecimalQuantity<Length> getProbeLowerHeight() {
		return probeLowerHeight;
	}
	/**
	 * @param probeLowerHeight the probeLowerHeight to set
	 */
	public void setProbeLowerHeight(BigDecimalQuantity<Length> probeLowerHeight) {
		firePropertyChange(Z_PROBE_LOWER, probeLowerHeight, this.probeLowerHeight = probeLowerHeight);
	}
	/**
	 * @return the probeFeedrate
	 */
	public BigDecimalQuantity<Length> getProbeFeedrate() {
		return probeFeedrate;
	}
	/**
	 * @param probeFeedrate the probeFeedrate to set
	 */
	public void setProbeFeedrate(BigDecimalQuantity<Length> probeFeedrate) {
		firePropertyChange(PROBE_FEEDRATE, probeFeedrate, this.probeFeedrate = probeFeedrate);
	}

}
