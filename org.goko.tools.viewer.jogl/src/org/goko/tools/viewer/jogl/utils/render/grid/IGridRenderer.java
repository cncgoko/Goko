/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.grid;

import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Vector3f;

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
	 * @return the normal
	 */
	Vector3f getNormal();

	/**
	 * @param normal the normal to set
	 */
	void setNormal(Vector3f normal);

	/**
	 * @return the horizontalVector
	 */
	public Vector3f getHorizontalVector();

	/**
	 * @param horizontalVector the horizontalVector to set
	 */
	public void setHorizontalVector(Vector3f horizontalVector);

	/**
	 * @return the verticalVector
	 */
	public Vector3f getVerticalVector();

	/**
	 * @param verticalVector the verticalVector to set
	 */
	public void setVerticalVector(Vector3f verticalVector);
	
	/**
	 * @return the horizontalMinimal
	 */
	public Length getHorizontalMinimal();

	/**
	 * @param horizontalMinimal the horizontalMinimal to set
	 */
	public void setHorizontalMinimal(Length horizontalMinimal);

	/**
	 * @return the horizontalMaximal
	 */
	public Length getHorizontalMaximal();

	/**
	 * @param horizontalMaximal the horizontalMaximal to set
	 */
	public void setHorizontalMaximal(Length horizontalMaximal);

	/**
	 * @return the verticalMinimal
	 */
	public Length getVerticalMinimal();

	/**
	 * @param verticalMinimal the verticalMinimal to set
	 */
	public void setVerticalMinimal(Length verticalMinimal);

	/**
	 * @return the verticalMaximal
	 */
	public Length getVerticalMaximal();

	/**
	 * @param verticalMaximal the verticalMaximal to set
	 */
	public void setVerticalMaximal(Length verticalMaximal);
	
	/**
	 * @return the horizontalColor
	 */
	public Color4f getHorizontalColor();

	/**
	 * @param horizontalColor the horizontalColor to set
	 */
	public void setHorizontalColor(Color4f horizontalColor);

	/**
	 * @return the verticalColor
	 */
	public Color4f getVerticalColor();

	/**
	 * @param verticalColor the verticalColor to set
	 */
	public void setVerticalColor(Color4f verticalColor);
	
	/**
	 * Set the bounds of this grid renderer using the given world coordinates
	 * @param min minimal value
	 * @param max maximal value
	 */
	public void setWorldBounds(Tuple6b min, Tuple6b max);
}