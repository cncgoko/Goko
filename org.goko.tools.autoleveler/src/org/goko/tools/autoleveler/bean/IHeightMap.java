package org.goko.tools.autoleveler.bean;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.math.Tuple6b;

/**
 * Describes a probed height map for the auto leveler modifier.
 * By convention, the map is always expressed in machine coordinates system
 * 
 * @author Psyko
 * @date 17 janv. 2019
 */
public interface IHeightMap extends IIdBean{
	
	/**
	 * Indicates if this map has already been probed against real surface
	 * @return <code>true</code> if it was probed, <code>false</code> otherwise
	 */
	boolean isProbed();
	
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
