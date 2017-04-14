/**
 * 
 */
package org.goko.controller.grbl.commons;

/**
 * @author Psyko
 * @date 7 janv. 2017
 */
public interface IGrblStatus {

	/**
	 * @return the value
	 */
	int getValue();

	/**
	 * @return the label
	 */
	String getLabel();

	/**
	 * Test for this status against warning severity
	 * @return <code>true</code> if it's a warning, <code>false</code> otherwise
	 */
	boolean isWarning();

	/**
	 * Test for this status against error severity
	 * @return <code>true</code> if it's a error, <code>false</code> otherwise
	 */
	boolean isError();

}