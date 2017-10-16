package org.goko.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * Implementation of a generic cache for 
 *
 * @author Psyko
 *
 * @param <T> the type of objects to store
 * @param <K> the type of the key used to store objects
 */
public class CacheByKey<K, T> {
	/** The actual content */
	private Map<K, T> cacheByKey;

	/**
	 * Constructor
	 */
	public CacheByKey() {
		this(null);
	}

	/**
	 * Constructor
	 * @param idGenerator the internal id generator to use
	 */
	public CacheByKey(IIdGenerator idGenerator) {
		this.cacheByKey = new HashMap<K, T>();
	}

	/**
	 * Returns the full content of this cache. Order is not guaranteed
	 * @return a list of element T
	 * @throws GkException GkException
	 */
	public List<T> get() throws GkException{
		return new ArrayList<T>(cacheByKey.values());
	}

	/**
	 * Returns the full list of keys of this cache. Order is not guaranteed
	 * @return a list of element K
	 * @throws GkException GkException
	 */
	public List<K> getKeys(){
		return new ArrayList<>(cacheByKey.keySet());
	}
	
	/**
	 * Return the element with the given id in this cache if it exists
	 * @param id the id of the element to retrieve
	 * @return an element T if any exists for the given id
	 * @throws GkException if the requested element does not exist
	 */
	public T get(K id) throws GkException{
		T result = find(id);
		if(result == null){
			 throw new GkTechnicalException("Unable to find object with key ["+String.valueOf(id)+"]");
		}
		return result;
	}

	/**
	 * Return the list of element for the given list of id
	 * @param lstId the list of id of the element to retrieve
	 * @return a list of elements
	 * @throws GkException GkException
	 */
	public List<T> get(List<K> lstId) throws GkException{
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(lstId)){
			for (K id : lstId) {
				result.add(get(id));
			}
		}
		return result;
	}

	/**
	 * Return the element with the given id in this cache if it exists, or <code>null</code> otherwise
	 * @param id the id of the element to retrieve
	 * @return an element T if any exists for the given id, or <code>null</code>
	 * @throws GkException GkException
	 */
	public T find(K id) throws GkException{
		return cacheByKey.get(id);
	}

	/**
	 * Check the existence of the element with the given id in this cache
	 * @param id the id of the element to check
	 * @return <code>true</code> if the element exists, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean exist(K id) throws GkException{
		return find(id) != null;
	}

	/**
	 * Add the given element to this cache
	 * @param key the sey to store object
	 * @param element the element to add
	 * @throws GkException GkException
	 */
	public void add(K key, T element) throws GkException{		
		if(exist(key)){
			throw new GkTechnicalException("Duplicate entry : an object already exists for the key ["+String.valueOf(key)+"]");
		}
		cacheByKey.put(key, element);
	}

	/**
	 * Removes all the element from this cache
	 */
	public void removeAll(){
		cacheByKey.clear();
	}

	/**
	 * Removes the given element from this cache
	 * @param element the id of the element to remove
	 */
	public void remove(K id){
		cacheByKey.remove(id);
	}

	/**
	 * Returns the number of element in this cache
	 * @return the number of element
	 */
	public int size(){
		return cacheByKey.size();
	}
}
