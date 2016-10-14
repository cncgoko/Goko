/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.grid;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector4f;

import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.ICoreJoglRenderer;

/**
 * @author Psyko
 * @date 12 oct. 2016
 */
public interface IGridRenderer extends ICoreJoglRenderer{

	/**
	 * @return the opacity
	 */
	float getMinorOpacity();

	/**
	 * @param opacity the opacity to set
	 */
	void setMinorOpacity(float opacity);

	/**
	 * @return the opacity
	 */
	float getMajorOpacity();

	/**
	 * @param opacity the opacity to set
	 */
	void setMajorOpacity(float opacity);

	/**
	 * @return the opacity
	 */
	float getAxisOpacity();

	/**
	 * @param opacity the opacity to set
	 */
	void setAxisOpacity(float opacity);

	/**
	 * @return the majorIncrement
	 */
	Length getMajorIncrement();

	/**
	 * @param majorIncrement the majorIncrement to set
	 */
	void setMajorIncrement(Length majorIncrement);

	/**
	 * @return the minorIncrement
	 */
	Length getMinorIncrement();

	/**
	 * @param minorIncrement the minorIncrement to set
	 */
	void setMinorIncrement(Length minorIncrement);

	/**
	 * @param majorUnitColor the majorUnitColor to set
	 */
	void setMajorUnitColor(Color3f majorUnitColor);

	/**
	 * @param minorUnitColor the minorUnitColor to set
	 */
	void setMinorUnitColor(Color3f minorUnitColor);

	/**
	 * @return the originColor
	 */
	Color4f getOriginColor();

	/**
	 * @param originColor the originColor to set
	 */
	void setOriginColor(Color4f originColor);

	/**
	 * @return the start
	 */
	Tuple6b getStart();

	/**
	 * @param start the start to set
	 */
	void setStart(Tuple6b start);

	/**
	 * @return the end
	 */
	Tuple6b getEnd();

	/**
	 * @param end the end to set
	 */
	void setEnd(Tuple6b end);

	/**
	 * @return the normal
	 */
	Vector4f getNormal();

	/**
	 * @param normal the normal to set
	 */
	void setNormal(Vector4f normal);

}