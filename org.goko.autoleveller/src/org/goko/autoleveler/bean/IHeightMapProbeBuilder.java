package org.goko.autoleveler.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.math.Tuple6b;

/**
 * Interface defining a builder that uses a probe mechanism to build the map
 *
 * @author Psyko
 *
 */
public interface IHeightMapProbeBuilder extends IHeightMapBuilder{

	List<Tuple6b> getProbePositions() throws GkException;

	BigDecimalQuantity<Length> getClearanceHeight() throws GkException;

	void setClearanceHeight(BigDecimalQuantity<Length> height) throws GkException;

	BigDecimalQuantity<Length> getProbeStartHeight() throws GkException;

	void setProbeStartHeight(BigDecimalQuantity<Length> height) throws GkException;

	BigDecimalQuantity<Length> getProbeLowerHeight() throws GkException;

	void setProbeLowerHeight(BigDecimalQuantity<Length> height) throws GkException;

	BigDecimalQuantity<Length> getProbeFeedrate() throws GkException;

	void setProbeFeedrate(BigDecimalQuantity<Length> feedrate ) throws GkException;

	void registerProbePosition(Tuple6b probedPosition) throws GkException;
}
