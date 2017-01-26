package org.goko.controller.tinyg.commons.configuration.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.StringFieldEditor;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;

/**
 * Field editor for a String TinyG setting 
 * 
 * @author Psyko
 * @date 21 janv. 2017
 */
public class TinyGStringFieldEditor extends StringFieldEditor implements ITinyGFieldEditor<Text> {
	/** The identifier of the group this setting belongs to */
	private String groupIdentifier;
	/** The target configuration */
	private AbstractTinyGConfiguration<?> config;
	
	/**
	 * Constructor 
	 * @param parent
	 * @param style
	 */
	public TinyGStringFieldEditor(Composite parent, int style) {
		super(parent, style | SWT.READ_ONLY);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String value = String.valueOf(config.findSetting(groupIdentifier, preferenceName, String.class));
		getControl().setText(value);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		// read only
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {		
		// read only
	}

	/**
	 * @return the groupIdentifier
	 */
	public String getGroupIdentifier() {
		return groupIdentifier;
	}

	/**
	 * @param groupIdentifier the groupIdentifier to set
	 */
	public void setGroupIdentifier(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;
	}

	@Override
	public void setConfiguration(AbstractTinyGConfiguration<?>  cfg) {
		this.config = cfg;
	}

}
