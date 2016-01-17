package org.goko.autoleveler.bean;

import org.goko.core.common.measure.quantity.Length;

public abstract class AbstractOffsetMapProbeBuilder implements IHeightMapProbeBuilder {
	/** The clearance height */
	private Length zClearance;
	/** The probe start height */
	private Length zProbeStart;
	/** The probe lower height */
	private Length zProbeLower;
	/** The probe feed rate */
	private Length probeFeedrate;

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getClearanceHeight()
	 */
	@Override
	public Length getClearanceHeight() {
		return zClearance;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeStartHeight()
	 */
	@Override
	public Length getProbeStartHeight() {
		return zProbeStart;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeLowerHeight()
	 */
	@Override
	public Length getProbeLowerHeight() {
		return zProbeLower;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeFeedrate()
	 */
	@Override
	public Length getProbeFeedrate() {
		return probeFeedrate;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setClearanceHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setClearanceHeight(Length height) {
		this.zClearance = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeStartHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeStartHeight(Length height) {
		this.zProbeStart = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeLowerHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeLowerHeight(Length height) {
		this.zProbeLower = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeFeedrate(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeFeedrate(Length feedrate) {
		this.probeFeedrate = feedrate;
	}

}
