/**
 * 
 */
package org.goko.tools.commandpanel.preferences;

import org.goko.core.config.GkPreference;

/**
 * @author Psyko
 * @date 8 déc. 2018
 */
public class CommandPanelPreference extends GkPreference{
	/** ID of this configuration */
	public static final String NODE_ID = "org.goko.tools.commandpanel";
	/** Key for A axis mode */
	public static final String A_AXIS_ENABLED = "org.goko.tools.commandpanel.axis.enabled.a";
	/** Key for B axis mode */
	public static final String B_AXIS_ENABLED = "org.goko.tools.commandpanel.axis.enabled.b";
	/** Key for C axis mode */
	public static final String C_AXIS_ENABLED = "org.goko.tools.commandpanel.axis.enabled.c";
	
	public static CommandPanelPreference instance;
	/**
	 * 
	 */
	public CommandPanelPreference() {
		super(NODE_ID);
	}

	public static CommandPanelPreference getInstance(){
		if(instance == null){
			instance = new CommandPanelPreference();
		}
		return instance;
	}
	
	public boolean isAAxisEnabled(){
		return getBoolean(A_AXIS_ENABLED);
	}
	
	public boolean isBAxisEnabled(){
		return getBoolean(B_AXIS_ENABLED);
	}
	
	public boolean isCAxisEnabled(){
		return getBoolean(C_AXIS_ENABLED);
	}
}
