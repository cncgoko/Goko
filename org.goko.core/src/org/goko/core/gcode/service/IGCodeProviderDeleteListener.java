/**
 * 
 */
package org.goko.core.gcode.service;

/**
 * @author Psyko
 * @date 29 mai 2016
 */
public interface IGCodeProviderDeleteListener {

	/**
	 * Event listener called before deleting a GCodeProvider, allowing to veto the delete
	 * @param event the event
	 */
	void beforeDelete(GCodeProviderDeleteEvent event);
}
