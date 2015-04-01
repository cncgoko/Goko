/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.common.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.log.GkLog;

/**
 * An event dispatcher using annotation
 *
 * @author PsyKo
 *
 */
public class EventDispatcher {
	private static final GkLog LOG = GkLog.getLogger(EventDispatcher.class);


	/**
	 * The list of listener
	 */
	private List<Object> listenerList;

	/**
	 * Constructor
	 */
	public EventDispatcher() {
		listenerList = Collections.synchronizedList(new ArrayList<Object>());
	}
	/**
	 * Add a listener
	 * @param listener the listener to add
	 */
	public void addListener(Object listener){
		if(listener != null){
			if(listener == this){
				System.err.println("Cannot add myself as listener");
			}else{
				this.listenerList.add(listener);
			}
		}
	}

	/**
	 * Remove the listener
	 * @param listener the listener to remove
	 */
	public void removeListener(Object listener){
		if(listener != null){
			this.listenerList.remove(listener);
		}
	}

	/**
	 * Notify the listeners of the given events
	 * @param event
	 */
	public <T extends Event> void notifyListeners(T event){
		for(Object obj : listenerList){
			notifyListener(obj, event);
		}
	}

	/**
	 * Notify the given listener
	 * @param obj the listener to notify
	 */
	private <T extends Event> void notifyListener(Object obj, T event) {
		List<Method> methods = getListenerMethods(obj, event);

		if(CollectionUtils.isNotEmpty(methods)){
			for(Method method : methods){
				try {
					method.invoke(obj, event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOG.error(e);
				}
			}
		}

	}
	/**
	 * Return the methods capable of handling the event
	 * @param obj the target object
	 * @param event the event
	 * @return a list of Method
	 */
	private <T extends Event> List<Method> getListenerMethods(Object obj, T event) {
		Method[] methodsArray = obj.getClass().getMethods();
		List<Method> selected = new ArrayList<Method>();

		for(Method method : methodsArray){
			if(canHandleEvent(method, event)){
				selected.add(method);
			}
		}
		return selected;
	}

	/**
	 * Check if the given method is registered as a listener for this event
	 * @param method the method
	 * @param event the event
	 * @return a boolean value
	 */
	private <T extends Event> boolean canHandleEvent(Method method, T event){
		EventListener listenerAnnotation = method.getAnnotation(EventListener.class);
		if(listenerAnnotation != null){
			return event.getClass() == listenerAnnotation.value();
		}
		return false;
	}
}
