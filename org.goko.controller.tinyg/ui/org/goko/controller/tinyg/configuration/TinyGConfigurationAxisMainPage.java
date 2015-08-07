package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.widgets.Composite;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGConfigurationAxisMainPage extends AbstractTinyGConfigurationPage{

	public TinyGConfigurationAxisMainPage(TinyGConfiguration configuration) {
		super(configuration);		
		setTitle("Configure axis");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		
	}
}
