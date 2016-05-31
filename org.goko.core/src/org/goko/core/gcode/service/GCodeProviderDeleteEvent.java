/**
 * 
 */
package org.goko.core.gcode.service;

/**
 * @author Psyko
 * @date 29 mai 2016
 */
public class GCodeProviderDeleteEvent {
	/** Vetoable boolean */
	private boolean doIt;
	/** Id of the GCodeProvider */
	private Integer idGCodeProvider;	
	/**
	 * @param idGCodeProvider
	 */
	public GCodeProviderDeleteEvent(Integer idGCodeProvider) {
		super();
		this.idGCodeProvider = idGCodeProvider;
		this.doIt = true;
	}	
	/**
	 * @return the doIt
	 */
	public boolean isDoIt() {
		return doIt;
	}
	/**
	 * @param doIt the doIt to set
	 */
	public void setDoIt(boolean doIt) {
		this.doIt = doIt;
	}
	/**
	 * @return the idGCodeProvider
	 */
	public Integer getIdGCodeProvider() {
		return idGCodeProvider;
	}
	/**
	 * @param idGCodeProvider the idGCodeProvider to set
	 */
	public void setIdGCodeProvider(Integer idGCodeProvider) {
		this.idGCodeProvider = idGCodeProvider;
	}
	
}
