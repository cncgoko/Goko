package org.goko.core.common.utils;

import java.util.List;

import org.goko.core.common.exception.GkException;

/**
 * Implementation of a generic cache for IIdBean objects
 *
 * @author Psyko
 *
 * @param <T> the type of objects to store
 */
public class CacheById<T extends IIdBean> extends CacheByKey<Integer, T>{
	/** The id generator (may be <code>null</code>)*/
	private IIdGenerator idGenerator;

	/**
	 * Constructor
	 */
	public CacheById() {
		this(null);
	}

	/**
	 * Constructor
	 * @param idGenerator the internal id generator to use
	 */
	public CacheById(IIdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	/**
	 * Add the given element to this cache
	 * @param element the element to add
	 * @throws GkException GkException
	 */
	public void add(T element) throws GkException{
		if(idGenerator != null && element.getId() == null){
			element.setId(idGenerator.getNextValue());
		}		
		super.add(element.getId(), element);
	}
	
	/**
	 * Add the given list of element to this cache
	 * @param lstElement the list of elements
	 * @throws GkException GkException
	 */
	public void add(List<T> lstElement) throws GkException{
		for (T element : lstElement) {
			add(element);
		}
	}
	
	/**
	 * Removes the given element from this cache
	 * @param element the element to remove
	 */
	public void remove(T element){
		remove(element.getId());
	}
}
