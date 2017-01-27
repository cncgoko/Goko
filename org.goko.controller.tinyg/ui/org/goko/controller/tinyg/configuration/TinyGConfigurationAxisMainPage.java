package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBooleanFieldEditor;
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
		
		TinyGBooleanFieldEditor softLimitFieldEditor = new TinyGBooleanFieldEditor(parent, SWT.NONE);
		softLimitFieldEditor.setLabel("Enable soft limits");
		softLimitFieldEditor.setGroupIdentifier(TinyGConfiguration.SYSTEM_SETTINGS);
		softLimitFieldEditor.setPreferenceName(TinyGConfiguration.ENABLE_SOFT_LIMIT);		
		
		addField(softLimitFieldEditor);
	}
}
