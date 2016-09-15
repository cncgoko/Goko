/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.array;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiAngleFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiIntegerFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiLengthFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.rs274ngcv3.modifier.array.ArrayModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class ArrayPropertiesPanel extends AbstractModifierPropertiesPanel<ArrayModifier, ArrayModifierPropertiesController, ArrayModifierPropertiesModel> implements IPropertiesPanel{

	/**
	 * @param context
	 * @param abstractController
	 */
	public ArrayPropertiesPanel(IEclipseContext context) {
		super(context, new ArrayModifierPropertiesController());
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent(Composite parent) throws GkException {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		rootComposite.setLayout(new GridLayout(1, false));
		Group composite = new Group(rootComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		composite.setText("Properties");
		composite.setLayout(new GridLayout(1, false));
		
		UiIntegerFieldEditor copyCountEditor = new UiIntegerFieldEditor(composite, SWT.NONE);
		copyCountEditor.setPropertyName(ArrayModifierPropertiesModel.COUNT);
		copyCountEditor.setWidthInChars(6);
		copyCountEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		copyCountEditor.setLabel("Number of copy");
		
		Label lblOffset = new Label(composite, SWT.HORIZONTAL);
		lblOffset.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblOffset.setText("Offset");
		lblOffset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		UiLengthFieldEditor xOffsetEditor = new UiLengthFieldEditor(composite, SWT.NONE);
		xOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_X);
		xOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		xOffsetEditor.setLabelWidthInChar(2);
		xOffsetEditor.setWidthInChars(8);
		xOffsetEditor.setLabel("X");
		xOffsetEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		UiLengthFieldEditor yOffsetEditor = new UiLengthFieldEditor(composite, SWT.NONE);
		yOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_Y);
		yOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		yOffsetEditor.setLabelWidthInChar(2);
		yOffsetEditor.setWidthInChars(8);
		yOffsetEditor.setLabel("Y");
		yOffsetEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		UiLengthFieldEditor zOffsetEditor = new UiLengthFieldEditor(composite, SWT.NONE);
		zOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_Z);
		zOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		zOffsetEditor.setLabelWidthInChar(2);
		zOffsetEditor.setWidthInChars(8);
		zOffsetEditor.setLabel("Z");
		zOffsetEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		
		UiAngleFieldEditor aOffsetEditor = new UiAngleFieldEditor(composite, SWT.NONE);
		aOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_A);
		aOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		aOffsetEditor.setLabelWidthInChar(2);
		aOffsetEditor.setWidthInChars(8);
		aOffsetEditor.setLabel("A");
		aOffsetEditor.setUnit(AngleUnit.DEGREE_ANGLE);
		
		UiAngleFieldEditor bOffsetEditor = new UiAngleFieldEditor(composite, SWT.NONE);
		bOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_B);
		bOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		bOffsetEditor.setLabelWidthInChar(2);
		bOffsetEditor.setWidthInChars(8);
		bOffsetEditor.setLabel("B");
		bOffsetEditor.setUnit(AngleUnit.DEGREE_ANGLE);
		
		UiAngleFieldEditor cOffsetEditor = new UiAngleFieldEditor(composite, SWT.NONE);
		cOffsetEditor.setPropertyName(ArrayModifierPropertiesModel.TRANSLATION_C);
		cOffsetEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		cOffsetEditor.setLabelWidthInChar(2);
		cOffsetEditor.setWidthInChars(8);
		cOffsetEditor.setLabel("C");
		cOffsetEditor.setUnit(AngleUnit.DEGREE_ANGLE);
		
		getController().addFieldEditor(copyCountEditor);
		getController().addFieldEditor(xOffsetEditor);
		getController().addFieldEditor(yOffsetEditor);
		getController().addFieldEditor(zOffsetEditor);
		getController().addFieldEditor(aOffsetEditor);
		getController().addFieldEditor(bOffsetEditor);
		getController().addFieldEditor(cOffsetEditor);
	}
}
