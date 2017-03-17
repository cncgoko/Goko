/**
 * 
 */
package org.goko.preferences.keys;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.goko.core.common.utils.ICodeBean;

/**
 * @author Psyko
 * @date 5 mars 2017
 */
public class KeyBinding implements ICodeBean{
	private MCommand command;
	private MKeyBinding keyBinding;
	private MBindingTable mBindingTable;
	/**
	 * @param command
	 * @param keyBinding
	 */
	public KeyBinding(MCommand command, MKeyBinding keyBinding) {
		super();
		this.command = command;
		this.keyBinding = keyBinding;
	}
	/**
	 * @return the command
	 */
	public MCommand getCommand() {
		return command;
	}
	/**
	 * @param command the command to set
	 */
	public void setCommand(MCommand command) {
		this.command = command;
	}
	/**
	 * @return the keyBinding
	 */
	public MKeyBinding getKeyBinding() {
		return keyBinding;
	}
	/**
	 * @param keyBinding the keyBinding to set
	 */
	public void setKeyBinding(MKeyBinding keyBinding) {
		this.keyBinding = keyBinding;
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#getCode()
	 */
	@Override
	public String getCode() {		
		return getCommand().getElementId();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		
	}
	/**
	 * @return the mBindingTable
	 */
	public MBindingTable getBindingTable() {
		return mBindingTable;
	}
	/**
	 * @param mBindingTable the mBindingTable to set
	 */
	public void setBindingTable(MBindingTable mBindingTable) {
		this.mBindingTable = mBindingTable;
	}

	
	
}
