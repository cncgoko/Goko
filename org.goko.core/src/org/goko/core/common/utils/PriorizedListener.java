/**
 * 
 */
package org.goko.core.common.utils;

/**
 * @author Psyko
 * @date 8 juil. 2017
 */
public class PriorizedListener<T> implements Comparable<PriorizedListener<T>> {
	public static int PRIORITY_HIGHEST = 1;
	public static int PRIORITY_STANDARD = 10;
	public static int PRIORITY_LEAST = 20;
	
	private int priority;
	private T listener;
	/**
	 * @param priority
	 * @param listener
	 */
	public PriorizedListener(int priority, T listener) {
		super();
		this.priority = priority;
		this.listener = listener;
	}
	
	public T get(){
		return listener;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PriorizedListener<T> o) {		
		return priority - o.priority;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listener == null) ? 0 : listener.hashCode());
		return result;
	}
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriorizedListener other = (PriorizedListener) obj;
		if (listener == null) {
			if (other.listener != null)
				return false;
		} else if (!listener.equals(other.listener))
			return false;
		return true;
	}
	
	
}
