package org.goko.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * Implementation of a generic cache for IIdBean objects
 *
 * @author Psyko
 *
 * @param <T> the type of objects to store
 */
public class CacheByCode<T extends ICodeBean> {
	/** The actual content */
	private Map<String, T> cacheById;

	/**
	 * Constructor
	 * @param idGenerator the internal id generator to use
	 */
	public CacheByCode() {
		this.cacheById = new HashMap<String, T>();
	}

	/**
	 * Returns the full content of this cache. Order is not guaranted
	 * @return a list of element T
	 * @throws GkException GkException
	 */
	public List<T> get() throws GkException{
		return new ArrayList<T>(cacheById.values());
	}

	/**
	 * Return the element with the given id in this cache if it exists
	 * @param id the id of the element to retrieve
	 * @return an element T if any exists for the given id
	 * @throws GkException if the requested element does not exist
	 */
	public T get(String code) throws GkException{
		T result = find(code);
		if(result == null){
			 throw new GkTechnicalException("Unable to find object with the code ["+String.valueOf(code)+"]");
		}
		return result;
	}

	/**
	 * Return the list of element for the given list of id
	 * @param lstCodes the list of code of the element to retrieve
	 * @return a list of elements
	 * @throws GkException GkException
	 */
	public List<T> get(List<String> lstCodes) throws GkException{
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(lstCodes)){
			for (String code : lstCodes) {
				result.add(get(code));
			}
		}
		return result;
	}

	/**
	 * Return the element with the given id in this cache if it exists, or <code>null</code> otherwise
	 * @param code the code of the element to retrieve
	 * @return an element T if any exists for the given id, or <code>null</code>
	 * @throws GkException GkException
	 */
	public T find(String code) throws GkException{
		return cacheById.get(code);
	}

	/**
	 * Check the existence of the element with the given id in this cache
	 * @param code the code of the element to retrieve
	 * @return <code>true</code> if the element exists, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean exist(String code) throws GkException{
		return find(code) != null;
	}

	/**
	 * Add the given element to this cache
	 * @param element the element to add
	 * @throws GkException GkException
	 */
	public void add(T element) throws GkException{		
		if(exist(element.getCode())){
			throw new GkTechnicalException("Duplicate entry : an object with the code ["+String.valueOf(element.getCode())+"] already exists");
		}
		cacheById.put(element.getCode(), element);
	}

	/**
	 * Removes all the element from this cache
	 */
	public void removeAll(){
		cacheById.clear();
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
	 * @param code the code of the element to remove
	 */
	public void remove(String code){
		cacheById.remove(code);
	}

	/**
	 * Removes the given element from this cache
	 * @param element the element to remove
	 */
	public void remove(T element){
		remove(element.getCode());
	}

	/**
	 * Returns the number of element in this cache
	 * @return the number of element
	 */
	public int size(){
		return cacheById.size();
	}
}
