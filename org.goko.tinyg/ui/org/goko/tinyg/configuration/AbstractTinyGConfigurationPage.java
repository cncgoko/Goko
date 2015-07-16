package org.goko.tinyg.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

public abstract class AbstractTinyGConfigurationPage extends GkFieldEditorPreferencesPage{
	private TinyGConfiguration configuration;
	
	public AbstractTinyGConfigurationPage(TinyGConfiguration configuration) {		
		this.configuration = configuration;
		setPreferenceStore(new NullPreferenceStore());
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
