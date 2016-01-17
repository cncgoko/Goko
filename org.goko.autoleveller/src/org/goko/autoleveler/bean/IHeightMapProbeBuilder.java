package org.goko.autoleveler.bean;

import java.util.List;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;

/**
 * Interface defining a builder that uses a probe mechanism to build the map
 *
 * @author Psyko
 *
 */
public interface IHeightMapProbeBuilder extends IHeightMapBuilder{

	List<Tuple6b> getProbePositions();

	Length getClearanceHeight();

	void setClearanceHeight(Length height);

	Length getProbeStartHeight();

	void setProbeStartHeight(Length height);

	Length getProbeLowerHeight();

	void setProbeLowerHeight(Length height);

	Length getProbeFeedrate();

	void setProbeFeedrate(Length feedrate );

	void registerProbePosition(Tuple6b probedPosition);
}
