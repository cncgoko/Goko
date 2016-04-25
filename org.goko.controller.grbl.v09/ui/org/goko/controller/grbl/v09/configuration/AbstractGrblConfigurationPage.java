package org.goko.controller.grbl.v09.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.NullPreferenceStore;
import org.goko.controller.grbl.v09.configuration.editors.IGrblFieldEditor;

/**
 * Abstract page used to handle the Grbl configuration
 * 
 * @author PsyKo
 *
 */
public abstract class AbstractGrblConfigurationPage extends GkFieldEditorPreferencesPage{
	private GrblConfiguration configuration;	
	
	public AbstractGrblConfigurationPage(GrblConfiguration configuration) {		
		this.configuration = configuration;
		setPreferenceStore(new NullPreferenceStore());
		noDefaultAndApplyButton();
	}
	
	public void addField(IGrblFieldEditor<?> field) {
		super.addField(field);
		field.setConfiguration(configuration);
	}

	/**
	 * @return the configuration
	 */
	public GrblConfiguration getConfiguration() {
		return configuration;
	}
	
}
