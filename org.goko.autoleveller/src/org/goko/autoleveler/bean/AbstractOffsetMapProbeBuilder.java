package org.goko.autoleveler.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;

public abstract class AbstractOffsetMapProbeBuilder implements IHeightMapProbeBuilder {
	/** The clearance height */
	private BigDecimalQuantity<Length> zClearance;
	/** The probe start height */
	private BigDecimalQuantity<Length> zProbeStart;
	/** The probe lower height */
	private BigDecimalQuantity<Length> zProbeLower;
	/** The probe feed rate */
	private BigDecimalQuantity<Length> probeFeedrate;

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getClearanceHeight()
	 */
	@Override
	public BigDecimalQuantity<Length> getClearanceHeight() throws GkException {
		return zClearance;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeStartHeight()
	 */
	@Override
	public BigDecimalQuantity<Length> getProbeStartHeight() throws GkException {
		return zProbeStart;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeLowerHeight()
	 */
	@Override
	public BigDecimalQuantity<Length> getProbeLowerHeight() throws GkException {
		return zProbeLower;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#getProbeFeedrate()
	 */
	@Override
	public BigDecimalQuantity<Length> getProbeFeedrate() throws GkException {
		return probeFeedrate;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setClearanceHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setClearanceHeight(BigDecimalQuantity<Length> height) throws GkException {
		this.zClearance = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeStartHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeStartHeight(BigDecimalQuantity<Length> height) throws GkException {
		this.zProbeStart = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeLowerHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeLowerHeight(BigDecimalQuantity<Length> height) throws GkException {
		this.zProbeLower = height;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMapProbeBuilder#setProbeFeedrate(org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public void setProbeFeedrate(BigDecimalQuantity<Length> feedrate) throws GkException {
		this.probeFeedrate = feedrate;
	}

}
