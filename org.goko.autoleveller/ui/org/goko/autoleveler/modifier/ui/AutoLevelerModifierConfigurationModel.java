package org.goko.autoleveler.modifier.ui;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;

public class AutoLevelerModifierConfigurationModel extends AbstractModelObject{
	protected static final String X_START_COORDINATE = "xStartCoordinate";
	protected static final String Y_START_COORDINATE = "yStartCoordinate";
	protected static final String X_END_COORDINATE   = "xEndCoordinate";
	protected static final String Y_END_COORDINATE   = "yEndCoordinate";
	protected static final String X_STEP   			 = "xStep";
	protected static final String Y_STEP			 = "yStep";
	protected static final String Z_CLEARANCE		 = "zClearance";
	protected static final String Z_EXPECTED		 = "zExpected";
	protected static final String Z_PROBE_START		 = "zProbeStart";
	protected static final String Z_PROBE_LOWER		 = "zProbeLower";
	protected static final String PROBE_FEEDRATE	 = "probeFeedrate";

	private BigDecimalQuantity<Length> xStartCoordinate;
	private BigDecimalQuantity<Length> yStartCoordinate;
	private BigDecimalQuantity<Length> xEndCoordinate;
	private BigDecimalQuantity<Length> yEndCoordinate;
	private BigDecimalQuantity<Length> xStep;
	private BigDecimalQuantity<Length> yStep;
	private BigDecimalQuantity<Length> zClearance;
	private BigDecimalQuantity<Length> zExpected;
	private BigDecimalQuantity<Length> zProbeStart;
	private BigDecimalQuantity<Length> zProbeLower;
	private BigDecimalQuantity<Length> probeFeedrate;
	/**
	 * @return the xStartCoordinate
	 */
	public BigDecimalQuantity<Length> getxStartCoordinate() {
		return xStartCoordinate;
	}
	/**
	 * @param xStartCoordinate the xStartCoordinate to set
	 */
	public void setxStartCoordinate(BigDecimalQuantity<Length> xStartCoordinate) {
		firePropertyChange(X_START_COORDINATE, xStartCoordinate, this.xStartCoordinate = xStartCoordinate);
	}
	/**
	 * @return the yStartCoordinate
	 */
	public BigDecimalQuantity<Length> getyStartCoordinate() {
		return yStartCoordinate;
	}
	/**
	 * @param yStartCoordinate the yStartCoordinate to set
	 */
	public void setyStartCoordinate(BigDecimalQuantity<Length> yStartCoordinate) {
		firePropertyChange(Y_START_COORDINATE, yStartCoordinate, this.yStartCoordinate = yStartCoordinate);
	}
	/**
	 * @return the xEndCoordinate
	 */
	public BigDecimalQuantity<Length> getxEndCoordinate() {
		return xEndCoordinate;
	}
	/**
	 * @param xEndCoordinate the xEndCoordinate to set
	 */
	public void setxEndCoordinate(BigDecimalQuantity<Length> xEndCoordinate) {
		firePropertyChange(X_END_COORDINATE, xEndCoordinate, this.xEndCoordinate = xEndCoordinate);
	}
	/**
	 * @return the yEndCoordinate
	 */
	public BigDecimalQuantity<Length> getyEndCoordinate() {
		return yEndCoordinate;
	}
	/**
	 * @param yEndCoordinate the yEndCoordinate to set
	 */
	public void setyEndCoordinate(BigDecimalQuantity<Length> yEndCoordinate) {
		firePropertyChange(Y_END_COORDINATE, yEndCoordinate, this.yEndCoordinate = yEndCoordinate);
	}
	/**
	 * @return the xStep
	 */
	public BigDecimalQuantity<Length> getxStep() {
		return xStep;
	}
	/**
	 * @param xStep the xStep to set
	 */
	public void setxStep(BigDecimalQuantity<Length> xStep) {
		firePropertyChange(X_STEP, xStep, this.xStep = xStep);
	}
	/**
	 * @return the yStep
	 */
	public BigDecimalQuantity<Length> getyStep() {
		return yStep;
	}
	/**
	 * @param yStep the yStep to set
	 */
	public void setyStep(BigDecimalQuantity<Length> yStep) {
		firePropertyChange(Y_STEP, yStep, this.yStep = yStep);
	}
	/**
	 * @return the zClearance
	 */
	public BigDecimalQuantity<Length> getzClearance() {
		return zClearance;
	}
	/**
	 * @param zClearance the zClearance to set
	 */
	public void setzClearance(BigDecimalQuantity<Length> zClearance) {
		firePropertyChange(Z_CLEARANCE, zClearance, this.zClearance = zClearance);
	}
	/**
	 * @return the zExpected
	 */
	public BigDecimalQuantity<Length> getzExpected() {
		return zExpected;
	}
	/**
	 * @param zExpected the zExpected to set
	 */
	public void setzExpected(BigDecimalQuantity<Length> zExpected) {
		firePropertyChange(Z_EXPECTED, zExpected, this.zExpected = zExpected);
	}
	/**
	 * @return the zProbeStart
	 */
	public BigDecimalQuantity<Length> getzProbeStart() {
		return zProbeStart;
	}
	/**
	 * @param zProbeStart the zProbeStart to set
	 */
	public void setzProbeStart(BigDecimalQuantity<Length> zProbeStart) {
		firePropertyChange(Z_PROBE_START, zProbeStart, this.zProbeStart = zProbeStart);
	}
	/**
	 * @return the zProbeLower
	 */
	public BigDecimalQuantity<Length> getzProbeLower() {
		return zProbeLower;
	}
	/**
	 * @param zProbeLower the zProbeLower to set
	 */
	public void setzProbeLower(BigDecimalQuantity<Length> zProbeLower) {
		firePropertyChange(Z_PROBE_LOWER, zProbeLower, this.zProbeLower = zProbeLower);
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
