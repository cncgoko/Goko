/**
 * 
 */
package IValidationElement;

import org.goko.core.common.utils.Location;
import org.goko.core.gcode.element.validation.IValidationElement;

/**
 * @author Psyko
 * @date 12 juin 2016
 */
public class ValidationElement implements IValidationElement {
	private ValidationSeverity severity;
	private Location location;
	private int length;
	private String description;
	
	/**
	 * @param severity
	 * @param location
	 * @param description
	 */
	public ValidationElement(ValidationSeverity severity, Location location, String description) {
		this(severity, location, 1, description);
	}
	/**
	 * @param severity
	 * @param location
	 * @param length
	 * @param description
	 */
	public ValidationElement(ValidationSeverity severity, Location location, int length, String description) {
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
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	

}
