package org.goko.controller.tinyg.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.log.GkLog;

public abstract class AbstractTinyGConfigurationPage extends GkFieldEditorPreferencesPage{
	private static GkLog LOG = GkLog.getLogger(AbstractTinyGConfigurationPage.class);
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
