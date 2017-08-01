/**
 * 
 */
package org.goko.core.gcode.element.validation;

import org.goko.core.common.utils.Location;

/**
 * @author Psyko
 * @date 12 juin 2016
 */
public interface IValidationElement {
	enum ValidationSeverity{
		ERROR,
		WARNING		
	}
	
	ValidationSeverity getSeverity();
	
	Location getLocation();
	
	void setLocation(Location location);
	
	String getDescription();
	
	Integer getLength();
}
