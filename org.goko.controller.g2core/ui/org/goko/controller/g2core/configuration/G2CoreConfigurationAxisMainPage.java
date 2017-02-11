package org.goko.controller.g2core.configuration;

import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;

public class G2CoreConfigurationAxisMainPage extends AbstractG2CoreConfigurationPage{

	public G2CoreConfigurationAxisMainPage(G2CoreConfiguration configuration) {
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
