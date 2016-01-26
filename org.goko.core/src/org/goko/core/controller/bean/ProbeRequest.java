package org.goko.core.controller.bean;

import java.math.BigDecimal;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;

/**
 * Describes a probing request 
 * 
 * @author PsyKo
 * @date 24 janv. 2016
 */
public class ProbeRequest {
	/** The probing axis */
	private EnumControllerAxis axis;
	/** The clearance position on the safe axis */
	private Length clearance;
	/** The probe start position */
	private Length probeStart;
	/** The probe end position */
	private Length probeEnd;
	/** The probe feedrate */
	private BigDecimal probeFeedrate;
	/** The motion feedrate */
	private BigDecimal motionFeedrate;
	/** The position where the probe should happens */
	private Tuple6b probeCoordinate;
	
	
	/**
	 * @return the axis
	 */
	public EnumControllerAxis getAxis() {
		return axis;
	}
	/**
	 * @param axis the axis to set
	 */
	public void setAxis(EnumControllerAxis axis) {
		this.axis = axis;
	}
	/**
	 * @return the clearance
	 */
	public Length getClearance() {
		return clearance;
	}
	/**
	 * @param clearance the clearance to set
	 */
	public void setClearance(Length clearance) {
		this.clearance = clearance;
	}
	/**
	 * @return the probeStart
	 */
	public Length getProbeStart() {
		return probeStart;
	}
	/**
	 * @param probeStart the probeStart to set
	 */
	public void setProbeStart(Length probeStart) {
		this.probeStart = probeStart;
	}
	/**
	 * @return the probeEnd
	 */
	public Length getProbeEnd() {
		return probeEnd;
	}
	/**
	 * @param probeEnd the probeEnd to set
	 */
	public void setProbeEnd(Length probeEnd) {
		this.probeEnd = probeEnd;
	}
	/**
	 * @return the probeFeedrate
	 */
	public BigDecimal getProbeFeedrate() {
		return probeFeedrate;
	}
	/**
	 * @param probeFeedrate the probeFeedrate to set
	 */
	public void setProbeFeedrate(BigDecimal probeFeedrate) {
		this.probeFeedrate = probeFeedrate;
	}
	/**
	 * @return the probeCoordinate
	 */
	public Tuple6b getProbeCoordinate() {
		return probeCoordinate;
	}
	/**
	 * @param probeCoordinate the probeCoordinate to set
	 */
	public void setProbeCoordinate(Tuple6b probeCoordinate) {
		this.probeCoordinate = probeCoordinate;
	}
	/**
	 * @return the motionFeedrate
	 */
	public BigDecimal getMotionFeedrate() {
		return motionFeedrate;
	}
	/**
	 * @param motionFeedrate the motionFeedrate to set
	 */
	public void setMotionFeedrate(BigDecimal motionFeedrate) {
		this.motionFeedrate = motionFeedrate;
	}
	
}
