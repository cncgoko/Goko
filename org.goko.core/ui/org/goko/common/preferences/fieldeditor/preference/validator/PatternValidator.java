/**
 * 
 */
package org.goko.common.preferences.fieldeditor.preference.validator;

import java.util.regex.Pattern;

import org.goko.common.preferences.fieldeditor.preference.IStringValidator;

/**
 * @author Psyko
 * @date 21 avr. 2017
 */
public class PatternValidator extends AbstractValidator implements IStringValidator{
	/** The actual pattern */
	private String pattern;
	/** The compiled pattern, for internal use only */
	private Pattern compiledPattern;
	
	/**
	 * @param errorMessage
	 */
	public PatternValidator(String pattern, String errorMessage) {
		super(errorMessage);
		this.pattern = pattern;
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.IStringValidator#isValid(java.lang.String)
	 */
	@Override
	public boolean isValid(String value) {
		Pattern localPattern = getCompiledPattern();		
		return localPattern.matcher(value).matches();
	}
	
	/**
	 * Return the compiled pattern
	 * @return Pattern
	 */
	private Pattern getCompiledPattern(){
		if(compiledPattern == null){
			compiledPattern = Pattern.compile(pattern);
		}
		return compiledPattern;
	}
	
}
