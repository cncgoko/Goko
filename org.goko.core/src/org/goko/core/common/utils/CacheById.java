package org.goko.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public class CacheById<T extends IIdBean> {
	private Map<Integer, T> cacheById;
	
	/**
	 * Constructor
	 */
	public CacheById() {
		this.cacheById = new HashMap<Integer, T>();
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
	
	public T find(Integer id) throws GkException{
		return cacheById.get(id);
	}

	public boolean exist(Integer id) throws GkException{
		return find(id) != null;
	}
	
	public void add(T element) throws GkException{
		if(exist(element.getId())){
			throw new GkTechnicalException("Duplicate entry : an object with id ["+String.valueOf(element.getId())+"] already exists");
		}
		cacheById.put(element.getId(), element);
	}
	
	public void remove(Integer id){
		cacheById.remove(id);
	}

	public void remove(T element){
		remove(element.getId());
	}
}
