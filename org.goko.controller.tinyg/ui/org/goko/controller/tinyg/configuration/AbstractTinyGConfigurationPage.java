package org.goko.controller.tinyg.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.NullPreferenceStore;
import org.goko.controller.tinyg.commons.configuration.fields.ITinyGFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;

/**
 * Abstract page used to handle the TinyG configuration
 * 
 * @author PsyKo
 *
 */
public abstract class AbstractTinyGConfigurationPage extends GkFieldEditorPreferencesPage{
	private TinyGConfiguration configuration;	
	
	public AbstractTinyGConfigurationPage(TinyGConfiguration configuration) {		
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
	public TinyGConfiguration getConfiguration() {
		return configuration;
	}
	
}
