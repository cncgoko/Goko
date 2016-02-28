/**
 * 
 */
package org.goko.core.controller.event;

import org.goko.core.gcode.element.IGCodeContext;

/**
 * @author PsyKo
 * @date 24 févr. 2016
 */
public interface IGCodeContextListener<T extends IGCodeContext> {

	void onGCodeContextEvent(final T context);
}
