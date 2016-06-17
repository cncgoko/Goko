/**
 * 
 */
package org.goko.core.gcode.element.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.goko.core.gcode.element.validation.IValidationElement.ValidationSeverity;

/**
 * @author Psyko
 * @date 12 juin 2016
 */
public class ValidationTargetAdapter implements IValidationTarget{
	/** The list of validation elements */
	private List<IValidationElement> validationElements;
	/** Boolean indicating that errors were detected */
	private boolean hasErrors;
	/** Boolean indicating that warnings were detected */
	private boolean hasWarnings;
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return hasErrors;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasWarnings()
	 */
	@Override
	public boolean hasWarnings() {
		return hasWarnings;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#getValidationElements()
	 */
	@Override
	public List<IValidationElement> getValidationElements() {	
		if(validationElements == null){
			return null;
		}
		return Collections.unmodifiableList(validationElements);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#clearValidationElements()
	 */
	@Override
	public void clearValidationElements() {
		// TODO Auto-generated method stub
		
	}
		
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#addValidationElement(org.goko.core.gcode.element.validation.IValidationElement)
	 */
	@Override
	public void addValidationElement(IValidationElement element){
		if(validationElements == null){
			validationElements = new ArrayList<IValidationElement>();
		}
		validationElements.add(element);
		hasErrors 	|= element.getSeverity() == ValidationSeverity.ERROR;
		hasWarnings |= element.getSeverity() == ValidationSeverity.WARNING;
	}
}
