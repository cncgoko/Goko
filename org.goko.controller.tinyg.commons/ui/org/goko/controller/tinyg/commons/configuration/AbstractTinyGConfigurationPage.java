package org.goko.controller.tinyg.commons.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.NullPreferenceStore;
import org.goko.controller.tinyg.commons.configuration.fields.ITinyGFieldEditor;

/**
 * Abstract page used to handle the TinyG configuration
 * 
 * @author PsyKo
 *
 */
public abstract class AbstractTinyGConfigurationPage extends GkFieldEditorPreferencesPage{
	/** The target configuration */
	private AbstractTinyGConfiguration<?> configuration;	
	
	/**
	 * Constructor
	 * @param configuration the target configuration 
	 */
	public AbstractTinyGConfigurationPage(AbstractTinyGConfiguration<?> configuration) {		
		this.configuration = configuration;
		setPreferenceStore(new NullPreferenceStore());
		noDefaultAndApplyButton();
	}
	
	/**
	 * Adds a field editor 
	 * @param field the field to add
	 */
	public void addField(ITinyGFieldEditor<?> field) {
		super.addField(field);
		field.setConfiguration(configuration);
	}

	/**
	 * @return the configuration
	 */
	public AbstractTinyGConfiguration<?> getConfiguration() {
		return configuration;
	}
	
}
