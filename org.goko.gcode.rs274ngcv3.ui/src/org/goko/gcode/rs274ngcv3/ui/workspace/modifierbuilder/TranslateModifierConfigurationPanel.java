package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate.TranslateModifierConfigurationController;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate.TranslateModifierConfigurationModel;

public class TranslateModifierConfigurationPanel extends GkUiComponent<TranslateModifierConfigurationController, TranslateModifierConfigurationModel> {

	public TranslateModifierConfigurationPanel() {
		super(new TranslateModifierConfigurationController(new TranslateModifierConfigurationModel()));
	}
	
	public void createContent(Composite parent, IModifier<?> modifier) throws GkException {
		getDataModel().setModifier((TranslateModifier) modifier);
		Group composite = new Group(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		composite.setText("Properties");
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblTranslation = new Label(composite, SWT.NONE);
		lblTranslation.setText("Translation");
		
		UiQuantityFieldEditor<Length> translationX = new UiQuantityFieldEditor<Length>(composite, SWT.NONE);
		translationX.setPropertyName("translationX");
		translationX.setLabel("X");
		translationX.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationX.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationX);	
		
		UiQuantityFieldEditor<Length> translationY = new UiQuantityFieldEditor<Length>(composite, SWT.NONE);
		translationY.setPropertyName("translationY");
		translationY.setLabel("Y");
		translationY.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationY.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationY);	
		
		UiQuantityFieldEditor<Length> translationZ = new UiQuantityFieldEditor<Length>(composite, SWT.NONE);
		translationZ.setPropertyName("translationZ");
		translationZ.setLabel("Z");
		translationZ.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationZ.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationZ);			
	}
}
