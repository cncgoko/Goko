/**
 * 
 */
package org.goko.core.controller.bean;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Psyko
 * @date 19 avr. 2017
 */
public class OnOffBoolean {
	private Boolean state;

	/**
	 * @param state
	 */
	public OnOffBoolean(Boolean state) {
		super();
		this.state = state;
	}

	/**
	 * @return the state
	 */
	public Boolean get() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void set(Boolean state) {
		this.state = state;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {		
		if(state == null){
			return StringUtils.EMPTY;
		}else if(state){
			return "On";
		}else{
			return "Off";
		}
	}
}
