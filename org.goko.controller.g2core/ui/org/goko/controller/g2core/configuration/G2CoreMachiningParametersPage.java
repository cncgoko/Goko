/**
 * 
 */
package org.goko.controller.g2core.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 30 janv. 2017
 */
public class G2CoreMachiningParametersPage extends AbstractG2CoreConfigurationPage{

	/**
	 * @param configuration
	 */
	public G2CoreMachiningParametersPage(G2CoreConfiguration configuration) {
		super(configuration);
		setTitle("Machining");
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 8;
		
		TinyGBigDecimalSettingFieldEditor junctionIntegrationTimeFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		junctionIntegrationTimeFieldEditor.setWidthInChars(8);
		junctionIntegrationTimeFieldEditor.setLabelWidthInChar(20);
		junctionIntegrationTimeFieldEditor.setLabel("Junction integration time");
		junctionIntegrationTimeFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		junctionIntegrationTimeFieldEditor.setPreferenceName(G2Core.Configuration.System.JUNCTION_INTEGRATION_TIME);
		
		TinyGBigDecimalSettingFieldEditor chordalToleranceFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		chordalToleranceFieldEditor.setWidthInChars(8);
		chordalToleranceFieldEditor.setLabelWidthInChar(20);
		chordalToleranceFieldEditor.setLabel("Chordal tolerance");
		chordalToleranceFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		chordalToleranceFieldEditor.setPreferenceName(G2Core.Configuration.System.CHORDAL_TOLERANCE);
		
		TinyGBigDecimalSettingFieldEditor motorTimeoutFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		motorTimeoutFieldEditor.setWidthInChars(8);
		motorTimeoutFieldEditor.setLabelWidthInChar(20);
		motorTimeoutFieldEditor.setLabel("Motor disable timeout");
		motorTimeoutFieldEditor.setGroupIdentifier(G2Core.Configuration.Groups.SYSTEM);
		motorTimeoutFieldEditor.setPreferenceName(G2Core.Configuration.System.MOTOR_DISABLE_TIMEOUT);
		
		addField(junctionIntegrationTimeFieldEditor);
		addField(chordalToleranceFieldEditor);
		addField(motorTimeoutFieldEditor);
		
	}
}
