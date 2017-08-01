/**
 * 
 */
package org.goko.core.gcode.element.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.gcode.element.validation.IValidationElement.ValidationSeverity;

/**
 * @author Psyko
 * @date 2 juil. 2017
 */
public class ValidationResult {
	/** The list of elements in this result */
	private List<IValidationElement> elements;
	private boolean error;
	private boolean warning;
	/**
	 * Constructor
	 */
	public ValidationResult() {
		this.elements = new ArrayList<>();
	}
	
	protected ValidationResult(boolean error){
		this();
		this.error = error;
	}
	
	public static ValidationResult error(Throwable e){
		ValidationResult result = new ValidationResult();
		result.addElement(new ValidationElement(ValidationSeverity.ERROR, null, e.getMessage()));
		return result;
	}
	/**
	 * @return the elements
	 */
	public List<IValidationElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<IValidationElement> elements) {
		this.elements = elements;
	}
	
	/**
	 * @param elements the elements to add
	 */
	public void addElements(List<IValidationElement> elements) {
		if(CollectionUtils.isNotEmpty(elements)){
			for (IValidationElement iValidationElement : elements) {
				addElement(iValidationElement);;
			}
		}
	}
	
	/**
	 * @param element the element to add
	 */
	public void addElement(IValidationElement element) {
		this.elements.add(element);
		error |= element.getSeverity() == ValidationSeverity.ERROR; 
		warning |= element.getSeverity() == ValidationSeverity.WARNING; 
	}

	/**
	 * @return
	 */
	public boolean hasErrors() {
		return error;
	}
	
	/**
	 * @return
	 */
	public boolean hasWarnings() {
		return warning;
	}
	
}
