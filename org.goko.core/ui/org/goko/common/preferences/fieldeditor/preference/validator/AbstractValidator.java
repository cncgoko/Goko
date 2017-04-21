/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.validator;

import org.goko.common.preferences.fieldeditor.preference.IStringValidator;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public abstract class AbstractValidator implements IStringValidator{
	private String errorMessage = "Wrong format";
	
	/**
	 * @param errorMessage
	 */
	public AbstractValidator(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	/**
	 * Sets the error message
	 * @param message the error message to set
	 */
	public void setErrorMessage(String message){
		this.errorMessage = message;
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.IStringValidator#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {		
		return errorMessage;
	}
}
