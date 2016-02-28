/**
 * 
 */
package org.goko.core.controller;

import org.goko.core.common.event.IObservable;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.event.IGCodeContextListener;
import org.goko.core.gcode.element.IGCodeContext;

/**
 * @author PsyKo
 * @date 24 févr. 2016
 */
public interface IGCodeContextProvider<T extends IGCodeContext> extends IObservable<IGCodeContextListener<T>>{
	
	T getGCodeContext() throws GkException;
	
	
}
