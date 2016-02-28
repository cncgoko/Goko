/**
 * 
 */
package org.goko.core.common.event;

/**
 * @author PsyKo
 * @date 25 févr. 2016
 */
public interface IObservableDelegate<T> extends IObservable<T>{

	T getEventDispatcher();
}
