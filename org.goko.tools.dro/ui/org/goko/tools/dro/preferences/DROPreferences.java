/**
 * 
 */
package org.goko.tools.dro.preferences;

import org.goko.core.config.GkPreference;

/**
 * @author PsyKo
 *
 */
public class DROPreferences extends GkPreference{
	/** ID of this configuration */
	public static final String NODE_ID = "org.goko.tools.droservice";
	private static DROPreferences instance;
	
	/**
	 * Constructor
	 */
	private DROPreferences() {
		super(NODE_ID);
	}
	
	/**
	 * Singleton access
	 * @return DROPreferences
	 */
	public static DROPreferences getInstance(){
		if(instance == null){
			instance = new DROPreferences();
		}
		return instance;
	}

}
