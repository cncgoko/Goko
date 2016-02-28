/**
 * 
 */
package org.goko.core.gcode.element;

/**
 * DEfinition of a coordinate system
 * @author PsyKo
 * @date 17 oct. 2015
 */
public interface ICoordinateSystem {

	String getCode();
	
	boolean equals(ICoordinateSystem coordinateSystem);
}
