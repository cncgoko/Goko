/**
 * 
 */
package org.goko.controller.g2core.configuration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.controller.g2core.controller.G2Core;
import org.goko.controller.tinyg.commons.configuration.fields.TinyGBigDecimalSettingFieldEditor;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 31 janv. 2017
 */
public class G2CorePwmChannelPage extends AbstractG2CoreConfigurationPage{
	private String groupIdentifier;
	/**
	 * @param configuration
	 */
	public G2CorePwmChannelPage(G2CoreConfiguration configuration, String title, String groupIdentifier) {
		super(configuration);
		this.groupIdentifier = groupIdentifier;
		setTitle(title);
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) throws GkException {
		parent.setLayout(new GridLayout(2, false));
		
		TinyGBigDecimalSettingFieldEditor frequencyFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		frequencyFieldEditor.setLabel("Frequency");
		frequencyFieldEditor.setLabelWidthInChar(14);
		frequencyFieldEditor.setWidthInChars(10);
		frequencyFieldEditor.setGroupIdentifier(groupIdentifier);
		frequencyFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.FREQUENCY);
		
		Label lblHz = new Label(parent, SWT.NONE);
		lblHz.setText("Hz");
		
		TinyGBigDecimalSettingFieldEditor clockwiseSpeedLowFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		clockwiseSpeedLowFieldEditor.setLabel("CW Speed low");
		clockwiseSpeedLowFieldEditor.setLabelWidthInChar(14);
		clockwiseSpeedLowFieldEditor.setWidthInChars(10);
		clockwiseSpeedLowFieldEditor.setGroupIdentifier(groupIdentifier);
		clockwiseSpeedLowFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.CLOCKWISE_SPEED_LOW);
		
		Label lblRpm = new Label(parent, SWT.NONE);
		lblRpm.setText("RPM");
		
		TinyGBigDecimalSettingFieldEditor clockwiseSpeedHighFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		clockwiseSpeedHighFieldEditor.setLabel("CW Speed high");
		clockwiseSpeedHighFieldEditor.setLabelWidthInChar(14);
		clockwiseSpeedHighFieldEditor.setWidthInChars(10);
		clockwiseSpeedHighFieldEditor.setGroupIdentifier(groupIdentifier);
		clockwiseSpeedHighFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.CLOCKWISE_SPEED_HIGH);
		
		Label label = new Label(parent, SWT.NONE);
		label.setText("RPM");
		
		TinyGBigDecimalSettingFieldEditor clockwisePhaseLowFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		clockwisePhaseLowFieldEditor.setLabel("CW Phase low");
		clockwisePhaseLowFieldEditor.setLabelWidthInChar(14);
		clockwisePhaseLowFieldEditor.setWidthInChars(10);
		clockwisePhaseLowFieldEditor.setGroupIdentifier(groupIdentifier);
		clockwisePhaseLowFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.CLOCKWISE_PHASE_LOW);
		new Label(parent, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor clockwisePhaseHighFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		clockwisePhaseHighFieldEditor.setLabel("CW Phase high");
		clockwisePhaseHighFieldEditor.setLabelWidthInChar(14);
		clockwisePhaseHighFieldEditor.setWidthInChars(10);
		clockwisePhaseHighFieldEditor.setGroupIdentifier(groupIdentifier);
		clockwisePhaseHighFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.CLOCKWISE_PHASE_HIGH);
		new Label(parent, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor counterclockwiseSpeedLowFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		counterclockwiseSpeedLowFieldEditor.setLabel("CCW Speed low");
		counterclockwiseSpeedLowFieldEditor.setLabelWidthInChar(14);
		counterclockwiseSpeedLowFieldEditor.setWidthInChars(10);
		counterclockwiseSpeedLowFieldEditor.setGroupIdentifier(groupIdentifier);
		counterclockwiseSpeedLowFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_SPEED_LOW);
		
		Label label_1 = new Label(parent, SWT.NONE);
		label_1.setText("RPM");
		
		TinyGBigDecimalSettingFieldEditor counterclockwiseSpeedHighFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		counterclockwiseSpeedHighFieldEditor.setLabel("CCW Speed high");
		counterclockwiseSpeedHighFieldEditor.setLabelWidthInChar(14);
		counterclockwiseSpeedHighFieldEditor.setWidthInChars(10);
		counterclockwiseSpeedHighFieldEditor.setGroupIdentifier(groupIdentifier);
		counterclockwiseSpeedHighFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_SPEED_HIGH);
		
		Label label_2 = new Label(parent, SWT.NONE);
		label_2.setText("RPM");
		
		TinyGBigDecimalSettingFieldEditor counterclockwisePhaseLowFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		counterclockwisePhaseLowFieldEditor.setLabel("CCW Phase low");
		counterclockwisePhaseLowFieldEditor.setLabelWidthInChar(14);
		counterclockwisePhaseLowFieldEditor.setWidthInChars(10);
		counterclockwisePhaseLowFieldEditor.setGroupIdentifier(groupIdentifier);
		counterclockwisePhaseLowFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_PHASE_LOW);
		new Label(parent, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor counterclockwisePhaseHighFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		counterclockwisePhaseHighFieldEditor.setLabel("CCW Phase high");
		counterclockwisePhaseHighFieldEditor.setLabelWidthInChar(14);
		counterclockwisePhaseHighFieldEditor.setWidthInChars(10);
		counterclockwisePhaseHighFieldEditor.setGroupIdentifier(groupIdentifier);
		counterclockwisePhaseHighFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.COUNTERCLOCKWISE_PHASE_HIGH);
		new Label(parent, SWT.NONE);
		
		TinyGBigDecimalSettingFieldEditor phaseOffFieldEditor = new TinyGBigDecimalSettingFieldEditor(parent, SWT.NONE);
		phaseOffFieldEditor.setLabel("Phase off");
		phaseOffFieldEditor.setLabelWidthInChar(14);
		phaseOffFieldEditor.setWidthInChars(10);
		phaseOffFieldEditor.setGroupIdentifier(groupIdentifier);
		phaseOffFieldEditor.setPreferenceName(G2Core.Configuration.PwmChannel.PHASE_OFF);
		
		addField(frequencyFieldEditor);
		addField(clockwiseSpeedLowFieldEditor);
		addField(clockwiseSpeedHighFieldEditor);
		addField(clockwisePhaseLowFieldEditor);
		addField(clockwisePhaseHighFieldEditor);
		addField(counterclockwiseSpeedLowFieldEditor);
		addField(counterclockwiseSpeedHighFieldEditor);
		addField(counterclockwisePhaseLowFieldEditor);
		addField(counterclockwisePhaseHighFieldEditor);
		addField(phaseOffFieldEditor);
		new Label(parent, SWT.NONE);
	}

}
