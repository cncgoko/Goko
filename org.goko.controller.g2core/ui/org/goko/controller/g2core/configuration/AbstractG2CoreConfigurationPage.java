package org.goko.controller.g2core.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.NullPreferenceStore;
import org.goko.controller.tinyg.commons.configuration.fields.ITinyGFieldEditor;

/**
 * Abstract page used to handle the TinyG configuration
 * 
 * @author PsyKo
 *
 */
public abstract class AbstractG2CoreConfigurationPage extends GkFieldEditorPreferencesPage{
	private G2CoreConfiguration configuration;	
	
	public AbstractG2CoreConfigurationPage(G2CoreConfiguration configuration) {		
		this.configuration = configuration;
		setPreferenceStore(new NullPreferenceStore());
		noDefaultAndApplyButton();
	}
	
	public void addField(ITinyGFieldEditor<?> field) {
		super.addField(field);
		field.setConfiguration(configuration);
	}

	/**
	 * @return the configuration
	 */
	public G2CoreConfiguration getConfiguration() {
		return configuration;
	}
	
}
