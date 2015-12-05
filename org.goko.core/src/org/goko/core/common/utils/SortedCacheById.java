package org.goko.core.common.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
public class SortedCacheById<T extends IIdBean> {
	/** The actual content */
	private SortedMap<Integer, T> cacheById;
	/** The id generator (may be <code>null</code>)*/
	private IIdGenerator idGenerator;

	/**
	 * Constructor
	 */
	public SortedCacheById(Comparator<Integer> comparator) {
		this(null, comparator);
	}

	/**
	 * Constructor
	 * @param idGenerator the internal id generator to use
	 */
	public SortedCacheById(IIdGenerator idGenerator, Comparator<Integer> comparator) {
		this.cacheById = new TreeMap<Integer, T>(comparator);
		this.idGenerator = idGenerator;
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
	public T get(Integer id) throws GkException{
		T result = find(id);
		if(result == null){
			 throw new GkTechnicalException("Unable to find object with id ["+String.valueOf(id)+"]");
		}
		return result;
	}

	/**
	 * Return the list of element for the given list of id
	 * @param lstId the list of id of the element to retrieve
	 * @return a list of elements
	 * @throws GkException GkException
	 */
	public List<T> get(List<Integer> lstId) throws GkException{
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(lstId)){
			for (Integer id : lstId) {
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
	public T find(Integer id) throws GkException{
		return cacheById.get(id);
	}

	/**
	 * Check the existence of the element with the given id in this cache
	 * @param id the id of the element to check
	 * @return <code>true</code> if the element exists, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public boolean exist(Integer id) throws GkException{
		return find(id) != null;
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
		if(exist(element.getId())){
			throw new GkTechnicalException("Duplicate entry : an object with id ["+String.valueOf(element.getId())+"] already exists");
		}
		cacheById.put(element.getId(), element);
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
	 * @param element the id of the element to remove
	 */
	public void remove(Integer id){
		cacheById.remove(id);
	}

	/**
	 * Removes the given element from this cache
	 * @param element the element to remove
	 */
	public void remove(T element){
		remove(element.getId());
	}

	/**
	 * Returns the number of element in this cache
	 * @return the number of element
	 */
	public int size(){
		return cacheById.size();
	}
}
