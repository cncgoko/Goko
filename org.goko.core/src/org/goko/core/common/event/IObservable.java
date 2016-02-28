package org.goko.core.common.event;
/**
 * An interface to the abstract Observable class.
 * 
 * @author Steven Jeuris
 * @param <T>
 */
public interface IObservable<T> {

    /**
     * Add an observer which will listen to the actions of this object.
     * 
     * @param observer
     *            The observer which should listen to this observable.
     */
    public abstract void addObserver( T observer );

    /**
     * Remove an observer which was listening to this object.
     * 
     * @param observer
     *            The observer to remove.
     * 
     * @return True, when observer was found and removed, false otherwise.
     */
    public abstract boolean removeObserver( T observer );

}