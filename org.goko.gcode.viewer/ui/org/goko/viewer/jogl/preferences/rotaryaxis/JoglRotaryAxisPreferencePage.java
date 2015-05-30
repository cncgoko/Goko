package org.goko.viewer.jogl.preferences.rotaryaxis;

import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.goko.common.GkUiUtils;
import org.goko.common.preferences.GkFieldEditorPreferencesPage;
import org.goko.common.preferences.fieldeditor.BooleanFieldEditor;
import org.goko.common.preferences.fieldeditor.ComboFieldEditor;
import org.goko.common.preferences.fieldeditor.QuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.viewer.jogl.service.JoglViewerSettings;
import org.goko.viewer.jogl.service.JoglViewerSettings.EnumRotaryAxisDirection;

public class JoglRotaryAxisPreferencePage extends GkFieldEditorPreferencesPage {
	private BooleanFieldEditor booleanFieldEditor;
	private Group grpSettings;
	
	public JoglRotaryAxisPreferencePage() {
		setTitle("4th axis");
		setDescription("Configure the 4th axis display");
		setPreferenceStore(JoglViewerSettings.getInstance().getPreferences());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#propertyChangle(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		GkUiUtils.setEnabled(grpSettings, booleanFieldEditor.getControl().getSelection());		
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkPreferencesPage#createPreferencePage(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createPreferencePage(Composite parent) {
		
		booleanFieldEditor = new BooleanFieldEditor(parent, SWT.NONE);
		booleanFieldEditor.setPreferenceName("rotaryAxisEnabled");
		booleanFieldEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		booleanFieldEditor.setLabel("Enable rotary axis display");
		booleanFieldEditor.setPropertyChangeListener(this);
		
		grpSettings = new Group(parent, SWT.NONE);
		grpSettings.setText("Settings");
		grpSettings.setLayout(new GridLayout(1, false));
		grpSettings.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		ComboFieldEditor comboFieldEditor = new ComboFieldEditor(grpSettings, SWT.READ_ONLY);
		comboFieldEditor.setPreferenceName("rotaryAxisDirection");
		String[][] axisDirection = new String[][]{{ "X Axis", EnumRotaryAxisDirection.X.toString()},
													{"Y Axis", EnumRotaryAxisDirection.Y.toString()},
													{"Z Axis", EnumRotaryAxisDirection.Z.toString()}};
		comboFieldEditor.setLabelWidthInChar(16);
		comboFieldEditor.setLabel("Rotary axis direction");
		comboFieldEditor.setEntry(axisDirection);
		
		Composite composite = new Composite(grpSettings, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Rotary axis position");
		
		QuantityFieldEditor<Length> xPositionField = new QuantityFieldEditor<Length>(composite, SWT.NONE);
		xPositionField.setPreferenceName("rotaryAxisPositionX");
		xPositionField.setWidthInChars(6);
		xPositionField.setLabel("X");
		new Label(composite, SWT.NONE);
		
		QuantityFieldEditor<Length> yPositionField = new QuantityFieldEditor<Length>(composite, SWT.NONE);
		yPositionField.setPreferenceName("rotaryAxisPositionY");
		yPositionField.setWidthInChars(6);
		yPositionField.setLabel("Y");
		new Label(composite, SWT.NONE);
		
		QuantityFieldEditor<Length> zPositionField = new QuantityFieldEditor<Length>(composite, SWT.NONE);
		zPositionField.setPreferenceName("rotaryAxisPositionZ");
		zPositionField.setWidthInChars(6);
		zPositionField.setLabel("Z");
				
		addField(comboFieldEditor);
		addField(xPositionField);
		addField(yPositionField);
		addField(zPositionField);
		addField(booleanFieldEditor);			
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.GkFieldEditorPreferencesPage#postInitialize()
	 */
	@Override
	protected void postInitialize() throws GkException {
		GkUiUtils.setEnabled(grpSettings, booleanFieldEditor.getControl().getSelection());
	}
}
