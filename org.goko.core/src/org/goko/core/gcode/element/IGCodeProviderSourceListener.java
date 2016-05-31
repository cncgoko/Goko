/**
 * 
 */
package org.goko.core.gcode.element;

/**
 * @author Psyko
 * @date 29 mai 2016
 */
public interface IGCodeProviderSourceListener {

	/**
	 * Receive notification when the source changed
	 * @param target the source that triggered the event
	 */
	void onSourceChanged(IGCodeProviderSource taget);
}
