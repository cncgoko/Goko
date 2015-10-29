package org.goko.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class CacheById<T extends IIdBean> {
	private Map<Integer, T> cacheById;
	private IIdGenerator idGenerator;
	
	/**
	 * Constructor
	 */
	public CacheById() {
		this(null);
	}
	
	public CacheById(IIdGenerator idGenerator) {
		this.cacheById = new HashMap<Integer, T>();
		this.idGenerator = idGenerator;
	}
	
	public List<T> get() throws GkException{
		return new ArrayList<T>(cacheById.values());
	}
	
	public T get(Integer id) throws GkException{
		T result = find(id);
		if(result == null){
			 throw new GkTechnicalException("Unable to find object with id ["+String.valueOf(id)+"]");
		}
		return result;
	}
	
	public List<T> get(List<Integer> lstId) throws GkException{
		List<T> result = new ArrayList<T>();
		if(CollectionUtils.isNotEmpty(lstId)){
			for (Integer id : lstId) {
				result.add(get(id));
			}
		}
		return result;
	}
	public T find(Integer id) throws GkException{
		return cacheById.get(id);
	}

	public boolean exist(Integer id) throws GkException{
		return find(id) != null;
	}
	
	public void add(T element) throws GkException{	
		if(idGenerator != null){
			element.setId(idGenerator.getNextValue());
		}
		if(exist(element.getId())){
			throw new GkTechnicalException("Duplicate entry : an object with id ["+String.valueOf(element.getId())+"] already exists");
		}
		cacheById.put(element.getId(), element);
	}
	
	public void add(List<T> lstElement) throws GkException{	
		for (T element : lstElement) {
			add(element);
		}
	}
	
	public void remove(Integer id){
		cacheById.remove(id);
	}

	public void remove(T element){
		remove(element.getId());
	}
}
