/**
 * 
 */
package org.goko.core.gcode.element;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Psyko
 * @date 29 mai 2016
 */
public abstract class AbstractGCodeProviderSource implements IGCodeProviderSource {
	/** Registered listeners */
	private List<IGCodeProviderSourceListener> listeners;
		
	/**
	 * Constructor
	 */
	protected AbstractGCodeProviderSource() {
		super();
		this.listeners = new CopyOnWriteArrayList<IGCodeProviderSourceListener>();
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#addListener(org.goko.core.gcode.element.IGCodeProviderSourceListener)
	 */
	@Override
	public void addListener(IGCodeProviderSourceListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	/**
	 * Notifies registered listener that this source has changed
	 */
	protected void notifyChange(){
		for (IGCodeProviderSourceListener listener : listeners) {
			listener.onSourceChanged(this);
		}
	}
}
