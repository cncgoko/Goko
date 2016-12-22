/**
 * 
 */
package org.goko.tools.serial.jssc.console.internal;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * @author Psyko
 * @date 18 déc. 2016
 */
public class JsscConsoleFilter implements Serializable{
	/** State of the filtre */
	private boolean enabled;
	/** The regex of the filter */
	private String regex;
	/** Description */
	private String description;
	/** The compiled pattern */
	private Pattern pattern;
	/** The type of the filter */
	private JsscConsoleFilterType type;

	/**
	 * @param enabled
	 * @param regex
	 * @param description
	 */
	public JsscConsoleFilter(boolean enabled, String regex, String description, JsscConsoleFilterType type) {
		super();
		this.enabled = enabled;
		this.regex = regex;
		this.description = description;
		this.type = type;
	}
	/**
	 * @return the enable
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enable the enable to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}
	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
		this.pattern = null;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((regex == null) ? 0 : regex.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsscConsoleFilter other = (JsscConsoleFilter) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled != other.enabled)
			return false;
		if (regex == null) {
			if (other.regex != null)
				return false;
		} else if (!regex.equals(other.regex))
			return false;
		return true;
	}
	/**
	 * @return the pattern
	 */
	public Pattern getPattern() {
		if(pattern == null){
			pattern = Pattern.compile(regex);
		}
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return the type
	 */
	public JsscConsoleFilterType getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(JsscConsoleFilterType type) {
		this.type = type;
	}
}
