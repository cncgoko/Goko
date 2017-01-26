/**
 * 
 */
package org.goko.junit.tools.connection;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.EnumConnectionEvent;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionListener;
import org.goko.core.connection.IConnectionService;


public abstract class ConnectionServiceAdapter implements IConnectionService {
	/** Incoming data listeners */
	private List<WeakReference<IConnectionDataListener>> inputListeners;
	/** Outgoing data listeners */
	private List<WeakReference<IConnectionDataListener>> outputListeners;
	/** Connection event listener */
	private List<WeakReference<IConnectionListener>> connectionListeners;
	
	/**
	 * Constructor
	 */
	protected ConnectionServiceAdapter() {
		this.inputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.outputListeners 	 = new ArrayList<WeakReference<IConnectionDataListener>>();
		this.connectionListeners = new ArrayList<WeakReference<IConnectionListener>>();
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addInputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addInputDataListener(IConnectionDataListener listener) throws GkException {
		inputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#removeInputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void removeInputDataListener(IConnectionDataListener listener) throws GkException {
		WeakReference<IConnectionDataListener> reference = findListener(inputListeners, listener);
		if(reference != null){
			inputListeners.remove(reference);
		}

	}
	protected <T> WeakReference<T> findListener(List<WeakReference<T>> listenerList, T listener) throws GkException{
		WeakReference<T> reference = null;
		if(CollectionUtils.isNotEmpty(inputListeners)){
			for (WeakReference<T> weakReference : listenerList) {
				if(weakReference.get() == listener){
					reference = weakReference;
				}
			}
		}
		return reference;
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addOutputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void addOutputDataListener(IConnectionDataListener listener) throws GkException {
		outputListeners.add(new WeakReference<IConnectionDataListener>(listener));
	}

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#removeOutputDataListener(org.goko.core.connection.IConnectionDataListener)
	 */
	@Override
	public void removeOutputDataListener(IConnectionDataListener listener) throws GkException {
		WeakReference<IConnectionDataListener> reference = findListener(outputListeners, listener);
		if(reference != null){
			outputListeners.remove(reference);
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#addConnectionListener(org.goko.core.connection.IConnectionListener)
	 */
	@Override
	public void addConnectionListener(IConnectionListener listener) throws GkException {
		connectionListeners.add(new WeakReference<IConnectionListener>(listener));
	}
	

	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionService#removeConnectionListener(org.goko.core.connection.IConnectionListener)
	 */
	@Override
	public void removeConnectionListener(IConnectionListener listener) throws GkException {
		WeakReference<IConnectionListener> reference = findListener(connectionListeners, listener);
		if(reference != null){
			outputListeners.remove(reference);
		}
	}
	/**
	 * Notifies the registered IConnectionListener that the given event happened
	 * @param event the event
	 * @throws GkException GkException
	 */
	protected void notifyConnectionListeners(EnumConnectionEvent event) throws GkException{
		if(CollectionUtils.isNotEmpty(connectionListeners)){
			for(WeakReference<IConnectionListener> reference : connectionListeners){
				if(reference.get() != null){
					reference.get().onConnectionEvent(event);
				}
			}
		}
	}

	/**
	 * Notifies the registered IConnectionDataListener that the given data has been received
	 * @param data the received data
	 * @throws GkException GkException
	 */
	protected void notifyInputListeners(List<Byte> data) throws GkException{
		if(CollectionUtils.isNotEmpty(inputListeners)){
			for(WeakReference<IConnectionDataListener> reference : inputListeners){
				if(reference.get() != null){
					reference.get().onDataReceived(data);
				}
			}
		}
	}

	/**
	 * Notifies the registered IConnectionDataListener that the given data has been sent
	 * @param data the received data
	 * @throws GkException GkException
	 */
	protected void notifyOutputListeners(List<Byte> data) throws GkException{
		if(CollectionUtils.isNotEmpty(outputListeners)){
			for(WeakReference<IConnectionDataListener> reference : outputListeners){
				if(reference.get() != null){
					reference.get().onDataSent(data);
				}
			}
		}
	}

}
