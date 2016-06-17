/**
 * 
 */
package org.goko.core.gcode.element.validation;

import java.util.List;

/**
 * @author Psyko
 * @date 12 juin 2016
 */
public interface IValidationTarget {
	
	/**
	 * Check if this target has errors
	 * @return <code>true</code> if errors were detected, <code>false</code> otherwise
	 */
	boolean hasErrors();

	/**
	 * Check if this target has warnings
	 * @return <code>true</code> if warnings were detected, <code>false</code> otherwise
	 */
	boolean hasWarnings();
	
	/**
	 * Returns the list of validation elements for this target
	 * @return a list of {@link IValidationElement}
	 */
	List<IValidationElement> getValidationElements();
	
	/**
	 * Clear the validation elements of this target
	 */
	void clearValidationElements();
	
	/**
	 * Add the given {@link IValidationElement} to this provider
	 * @param element the {@link IValidationElement}
	 */
	void addValidationElement(IValidationElement element); 
}
