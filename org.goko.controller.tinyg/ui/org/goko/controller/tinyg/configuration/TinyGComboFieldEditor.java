package org.goko.controller.tinyg.configuration;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGComboFieldEditor extends ComboFieldEditor implements ITinyGFieldEditor<Combo> {
	String groupIdentifier;	
	TinyGConfiguration cfg;
	
	public TinyGComboFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String value = String.valueOf(cfg.getSetting(groupIdentifier, preferenceName, BigDecimal.class));
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
		cfg.setSetting(groupIdentifier, preferenceName, new BigDecimal(getValue()));
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
	public void setConfiguration(TinyGConfiguration cfg) {
		this.cfg = cfg;
	}

}
