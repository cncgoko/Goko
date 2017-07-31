/**
 * 
 */
package PriorizedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.goko.core.common.utils.PriorizedListener;

/**
 * @author Psyko
 * @date 9 juil. 2017
 */
public class PriorizedListenerList<T> {
	CopyOnWriteArrayList<PriorizedListener<T>> listeners ;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2320893488984093068L;

	/**
	 * 
	 */
	public PriorizedListenerList() {
		listeners = new CopyOnWriteArrayList<PriorizedListener<T>>();
	}
	
	public void addListener(T listener){
		addListener(PriorizedListener.PRIORITY_STANDARD, listener);		
	}
	
	public void addListener(int priority, T listener){
		listeners.add(new PriorizedListener<T>(priority, listener));
		Collections.sort(listeners);
	}
	
	public void remove(T listener){
		listeners.remove(new PriorizedListener<T>(PriorizedListener.PRIORITY_STANDARD, listener));
	}
	
	public List<T> getListeners(){
		List<T> result = new ArrayList<>();
		for (PriorizedListener<T> pListener : listeners) {
			result.add(pListener.get());
		}
		return result;
	}
}
