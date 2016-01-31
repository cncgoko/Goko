package org.goko.tools.autoleveler.modifier.ui;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Length;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierModelObject;

public class AutoLevelerModifierConfigurationModel extends AbstractModifierModelObject{
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
	protected static final String MODIFICATION_ALLOWED	 = "modificationAllowed";

	private Length startCoordinateX;
	private Length startCoordinateY;
	private Length endCoordinateX;
	private Length endCoordinateY;
	private BigDecimal stepSizeX;
	private BigDecimal stepSizeY;
	private Length clearanceHeight;
	private Length expectedHeight;
	private Length probeStartHeight;
	private Length probeLowerHeight;
	private Length probeFeedrate;
	private boolean modificationAllowed;
	/**
	 * @return the startCoordinateX
	 */
	public Length getStartCoordinateX() {
		return startCoordinateX;
	}
	/**
	 * @param startCoordinateX the startCoordinateX to set
	 */
	public void setStartCoordinateX(Length startCoordinateX) {
		firePropertyChange(X_START_COORDINATE, this.startCoordinateX, this.startCoordinateX = startCoordinateX);
	}
	/**
	 * @return the startCoordinateY
	 */
	public Length getStartCoordinateY() {
		return startCoordinateY;
	}
	/**
	 * @param startCoordinateY the startCoordinateY to set
	 */
	public void setStartCoordinateY(Length startCoordinateY) {
		firePropertyChange(Y_START_COORDINATE, this.startCoordinateY, this.startCoordinateY = startCoordinateY);
	}
	/**
	 * @return the endCoordinateX
	 */
	public Length getEndCoordinateX() {
		return endCoordinateX;
	}
	/**
	 * @param endCoordinateX the endCoordinateX to set
	 */
	public void setEndCoordinateX(Length endCoordinateX) {
		firePropertyChange(X_END_COORDINATE, this.endCoordinateX, this.endCoordinateX = endCoordinateX);
	}
	/**
	 * @return the endCoordinateY
	 */
	public Length getEndCoordinateY() {
		return endCoordinateY;
	}
	/**
	 * @param endCoordinateY the endCoordinateY to set
	 */
	public void setEndCoordinateY(Length endCoordinateY) {
		firePropertyChange(Y_END_COORDINATE, this.endCoordinateY, this.endCoordinateY = endCoordinateY);
	}
	/**
	 * @return the stepSizeX
	 */
	public BigDecimal getStepSizeX() {
		return stepSizeX;
	}
	/**
	 * @param stepSizeX the stepSizeX to set
	 */
	public void setStepSizeX(BigDecimal stepSizeX) {
		firePropertyChange(X_STEP, this.stepSizeX, this.stepSizeX = stepSizeX);
	}
	/**
	 * @return the stepSizeY
	 */
	public BigDecimal getStepSizeY() {
		return stepSizeY;
	}
	/**
	 * @param stepSizeY the stepSizeY to set
	 */
	public void setStepSizeY(BigDecimal stepSizeY) {
		firePropertyChange(Y_STEP, this.stepSizeY, this.stepSizeY = stepSizeY);
	}
	/**
	 * @return the clearanceHeight
	 */
	public Length getClearanceHeight() {
		return clearanceHeight;
	}
	/**
	 * @param clearanceHeight the clearanceHeight to set
	 */
	public void setClearanceHeight(Length clearanceHeight) {
		firePropertyChange(Z_CLEARANCE, this.clearanceHeight, this.clearanceHeight = clearanceHeight);
	}
	/**
	 * @return the expectedHeight
	 */
	public Length getExpectedHeight() {
		return expectedHeight;
	}
	/**
	 * @param expectedHeight the expectedHeight to set
	 */
	public void setExpectedHeight(Length expectedHeight) {
		firePropertyChange(Z_EXPECTED, this.expectedHeight, this.expectedHeight = expectedHeight);
	}
	/**
	 * @return the probeStartHeight
	 */
	public Length getProbeStartHeight() {
		return probeStartHeight;
	}
	/**
	 * @param probeStartHeight the probeStartHeight to set
	 */
	public void setProbeStartHeight(Length probeStartHeight) {
		firePropertyChange(Z_PROBE_START, this.probeStartHeight, this.probeStartHeight = probeStartHeight);
	}
	/**
	 * @return the probeLowerHeight
	 */
	public Length getProbeLowerHeight() {
		return probeLowerHeight;
	}
	/**
	 * @param probeLowerHeight the probeLowerHeight to set
	 */
	public void setProbeLowerHeight(Length probeLowerHeight) {
		firePropertyChange(Z_PROBE_LOWER, this.probeLowerHeight, this.probeLowerHeight = probeLowerHeight);
	}
	/**
	 * @return the probeFeedrate
	 */
	public Length getProbeFeedrate() {
		return probeFeedrate;
	}
	/**
	 * @param probeFeedrate the probeFeedrate to set
	 */
	public void setProbeFeedrate(Length probeFeedrate) {
		firePropertyChange(PROBE_FEEDRATE, this.probeFeedrate, this.probeFeedrate = probeFeedrate);
	}
	/**
	 * @return the modificationAllowed
	 */
	public boolean isModificationAllowed() {
		return modificationAllowed;
	}
	/**
	 * @param modificationAllowed the modificationAllowed to set
	 */
	public void setModificationAllowed(boolean modificationAllowed) {
		firePropertyChange(MODIFICATION_ALLOWED, this.modificationAllowed, this.modificationAllowed = modificationAllowed);
	}

}
