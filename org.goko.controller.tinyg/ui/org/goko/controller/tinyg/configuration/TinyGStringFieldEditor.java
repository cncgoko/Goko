package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.StringFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGStringFieldEditor extends StringFieldEditor implements ITinyGFieldEditor<Text> {
	String groupIdentifier;	
	TinyGConfiguration cfg;
	
	public TinyGStringFieldEditor(Composite parent, int style) {
		super(parent, style | SWT.READ_ONLY);		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String value = String.valueOf(cfg.findSetting(groupIdentifier, preferenceName, String.class));
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
	public void setConfiguration(TinyGConfiguration cfg) {
		this.cfg = cfg;
	}

}
