package org.goko.core.common.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A generic implementation of an Observable object as defined by the <a
 * href="http://en.wikipedia.org/wiki/Observer_pattern">Observer pattern</a>.
 * As a type parameter the interface for the Observer needs to be specified.
 * 
 * @author Steven Jeuris
 * @param <T>
 *            The interface which should be implemented by the observers.
 */
public abstract class AbstractObservable<T> implements IObservable<T> {
	private Class<T> m_class;
	
    private T m_eventDispatcher = null;
    /**
     * The list of observers to which the event dispatcher forwards it calls.
     */
    private final ObserverPool<T> m_observers = new ObserverPool<T>();

	/**
	 * @param m_class
	 */
	public AbstractObservable(Class<T> m_class) {
		super();
		this.m_class = m_class;
	}

	/**
     * Get the event dispatcher through which you can notify the observers.
     * 
     * @return The event dispatcher through which you can notify the observers.
     */
    protected T getEventDispatcher() {
        // Only create one instance of the dispatcher.
        if ( m_eventDispatcher == null ) {
//            // Use reflection to get the generic parameter type.
//            // Find the super class after 'AbstractObservable'.
//            Class<?> superClass = this.getClass();
//            while ( superClass.getSuperclass() != AbstractObservable.class ) {                  
//                superClass = superClass.getSuperclass();
//            }            
//            
//            // Get the generic class for AbstractObservable, so that parameter types
//            // can be extracted.
//            Type genericClass = superClass.getGenericSuperclass();
//            if ( genericClass instanceof Class<?> ) {                
//                new RuntimeException( "Observable requires a parameter type!" );
//            }
//            else {
//                // Get the parameter type.
//                ParameterizedType genericType = (ParameterizedType)genericClass;                
//                Type[] typeArguments = genericType.getActualTypeArguments();

                m_eventDispatcher = m_observers.createEventDispatcher( m_class );
          //  }

        }

        return m_eventDispatcher;
    }

    /*
     * (non-Javadoc)
     * 
     * @see be.hyp3.patterns.observer.IObservable#addObserver(T)
     */
    public void addObserver( T observer ) {
        m_observers.addObserver( observer );
    }

    /*
     * (non-Javadoc)
     * 
     * @see be.hyp3.patterns.observer.IObservable#removeObserver(T)
     */
    public boolean removeObserver( T observer ) {
        return m_observers.removeObserver( observer );
    }

}

/**
 * The ObserverPool is a proxy which allows calls to an interface to be forwarded to a set of listeners.
 * 
 * @author Steven Jeuris
 * 
 * @param <T>
 *            The interface which defines which calls can be made to the listeners.
 */
class ObserverPool<T> implements InvocationHandler {

    /**
     * The list of listeners. Additions and removals greatly outnumber traversals,
     * so a CopyOnWriteArrayList is the most efficient solution.
     */
    private List<T> m_pool = new CopyOnWriteArrayList<T>();

    /**
     * Add an observer to which the calls will be made.
     * 
     * @param observer
     *            The observer to add.
     */
    public void addObserver( T observer ) {
        m_pool.add( observer );
    }

    /**
     * Remove an observer to which calls where being made.
     * 
     * @param observer
     *            The observer to remove.
     * 
     * @return True, when the observer was found and removed, false otherwise.
     */
    public boolean removeObserver( T observer ) {
        return m_pool.remove( observer );
    }

    /**
     * Create the proxy which allows to dispatch all calls to the observers.
     * 
     * @param observerClass
     *            The interface class of the observers.
     * 
     * @return The dispatcher which can be used to make calls to all added observers.
     */
    @SuppressWarnings( "unchecked" )
    public T createEventDispatcher( Class observerClass ) {    	
        Object dispatcher = Proxy.newProxyInstance(
            observerClass.getClassLoader(),
            new Class[] { observerClass },
            this );

        return (T)dispatcher;
    }

    /**
     * invoke() implementation of InvocationHandler.
     * This is called whenever a call is made to an event dispatcher.
     */
    @Override
    public Object invoke( Object object, Method method, Object[] args ) throws Throwable {
        // Forward the call to all observers.
        for ( T observer : m_pool ) {
            method.invoke( observer, args );
        }

        // No return object available.
        return null;
    }

}

