package org.goko.autoleveler.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.math.Tuple6b;

public interface IHeightMap extends IIdBean{

	/**
	 * Returns the offset at the given position
	 * @param x the X position of the location point
	 * @param y the Y position of the location point
	 * @return the height
	 * @throws GkException GkException
	 */
	Length getHeight(Length x, Length y) throws GkException;

	/**
	 * Splits a segment matching this offset map.
	 * The returned point are ordered from start to end.
	 * A point is created every time the given segment requires offset compensation.
	 * @param start the start point of the segment
	 * @param end the end point of the segment
	 * @return a list of Tuple6b
	 * @throws GkException GkException
	 */
	List<Tuple6b> splitSegment(Tuple6b start, Tuple6b end) throws GkException;

}
