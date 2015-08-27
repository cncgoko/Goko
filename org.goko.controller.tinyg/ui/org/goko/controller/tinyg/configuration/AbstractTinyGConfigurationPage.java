package org.goko.controller.tinyg.configuration;

import org.goko.common.preferences.GkFieldEditorPreferencesPage;
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
	
//	quand on modifie plusieurs parametres d'un seul coup, il y a comme un soucis de concurrences d'accès à la pile série.
//	voir en pas à pas (ex modif du si à 150 + ex à 2 donne {si:150} {ex:150})
//	+ certains parametres pas au bon format 
	
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
