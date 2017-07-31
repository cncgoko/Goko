/**
 * 
 */
package org.goko.core.gcode.element.validation;

import org.goko.core.common.utils.Location;

/**
 * @author Psyko
 * @date 12 juin 2016
 */
public class ValidationElement implements IValidationElement {
	private ValidationSeverity severity;
	private Location location;
	private Integer length;
	private String description;
	
	/**
	 * @param severity
	 * @param location
	 * @param description
	 */
	public ValidationElement(ValidationSeverity severity, Location location, String description) {
		this(severity, location, null, description);
	}
	/**
	 * @param severity
	 * @param location
	 * @param length
	 * @param description
	 */
	public ValidationElement(ValidationSeverity severity, Location location, Integer length, String description) {
		super();
		this.severity = severity;
		this.location = location;
		this.length = length;
		this.description = description;
	}
	/**
	 * @return the severity
	 */
	@Override
	public ValidationSeverity getSeverity() {
		return severity;
	}
	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(ValidationSeverity severity) {
		this.severity = severity;
	}
	/**
	 * @return the location
	 */
	@Override
	public Location getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	/**
	 * @return the description
	 */
	@Override
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}
	

}
