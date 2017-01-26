package org.goko.controller.tinyg.commons.configuration.fields;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;

/**
 * Field editor for a Combo based String TinyG setting 
 * 
 * @author Psyko
 * @date 21 janv. 2017
 */
public class TinyGComboFieldEditor extends ComboFieldEditor implements ITinyGFieldEditor<Combo> {
	/** The identifier of the group this setting belongs to */
	private String groupIdentifier;
	/** The target configuration */
	private AbstractTinyGConfiguration<?> config;
	
	public TinyGComboFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String value = String.valueOf(config.findSetting(groupIdentifier, preferenceName, BigDecimal.class));
		updateComboForValue(value);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {		
		config.setSetting(groupIdentifier, preferenceName, new BigDecimal(getValue()));
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
	public void setConfiguration(AbstractTinyGConfiguration<?> cfg) {
		this.config = cfg;
	}

}
