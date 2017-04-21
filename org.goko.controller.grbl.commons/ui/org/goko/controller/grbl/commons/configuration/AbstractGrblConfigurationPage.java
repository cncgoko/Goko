package org.goko.controller.grbl.commons.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.NullPreferenceStore;
import org.goko.controller.grbl.commons.configuration.editors.IGrblFieldEditor;

/**
 * Abstract page used to handle the Grbl configuration
 * 
 * @author PsyKo
 *
 */
public abstract class AbstractGrblConfigurationPage<C extends AbstractGrblConfiguration<C>> extends GkFieldEditorPreferencesPage{
	private C configuration;	
	
	public AbstractGrblConfigurationPage(C configuration) {		
		this.configuration = configuration;
		setPreferenceStore(new NullPreferenceStore());
		noDefaultAndApplyButton();
	}
	
	public void addField(IGrblFieldEditor<?, C> field) {
		super.addField(field);
		field.setConfiguration(configuration);
	}

	/**
	 * @return the configuration
	 */
	public C getConfiguration() {
		return configuration;
	}
	
}
