package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiLengthFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiQuantityFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.translate.TranslateModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate.TranslateModifierConfigurationController;
import org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.translate.TranslateModifierConfigurationModel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

public class TranslateModifierConfigurationPanel extends AbstractModifierPropertiesPanel<TranslateModifier, TranslateModifierConfigurationController, TranslateModifierConfigurationModel> implements IPropertiesPanel{

	public TranslateModifierConfigurationPanel(IEclipseContext context) {
		super(context, new TranslateModifierConfigurationController(new TranslateModifierConfigurationModel()));
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent(Composite parent, IModifier<?> modifier) throws GkException {		
		Group composite = new Group(parent, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		composite.setText("Properties");
		composite.setLayout(new GridLayout(1, false));
		GridData gl_data = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);		 
		composite.setLayoutData(gl_data);
		
		Label lblTranslation = new Label(composite, SWT.NONE);
		lblTranslation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblTranslation.setText("Translation");
		
		UiQuantityFieldEditor<Length> translationX = new UiLengthFieldEditor(composite, SWT.NONE);
		translationX.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		translationX.setWidthInChars(8);
		translationX.setPropertyName("translationX");
		translationX.setLabel("X");
		translationX.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationX.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationX);	
		
		UiQuantityFieldEditor<Length> translationY = new UiLengthFieldEditor(composite, SWT.NONE);
		translationY.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		translationY.setWidthInChars(8);
		translationY.setPropertyName("translationY");
		translationY.setLabel("Y");
		translationY.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationY.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationY);	
		
		UiQuantityFieldEditor<Length> translationZ = new UiLengthFieldEditor(composite, SWT.NONE);
		translationZ.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		translationZ.setWidthInChars(8);
		translationZ.setPropertyName("translationZ");
		translationZ.setLabel("Z");
		translationZ.setUnit(GokoPreference.getInstance().getLengthUnit());
		translationZ.setEmptyStringAllowed(false);
		
		getController().addFieldEditor(translationZ);			
	}
}
