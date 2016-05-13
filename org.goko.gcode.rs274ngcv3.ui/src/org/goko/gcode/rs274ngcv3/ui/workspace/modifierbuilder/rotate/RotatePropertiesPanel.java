/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.rotate;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiAngleFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiRadioGroupFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.gcode.rs274ngcv3.modifier.rotate.RotateModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class RotatePropertiesPanel extends AbstractModifierPropertiesPanel<RotateModifier, RotateModifierPropertiesController, RotateModifierPropertiesModel> implements IPropertiesPanel{

	/**
	 * @param context
	 * @param abstractController
	 */
	public RotatePropertiesPanel(IEclipseContext context) {
		super(context, new RotateModifierPropertiesController());
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
				
		UiRadioGroupFieldEditor<EnumControllerAxis> radioGroupFieldEditor = new UiRadioGroupFieldEditor<EnumControllerAxis>(composite, SWT.NONE);
		radioGroupFieldEditor.setLabel("Rotation axis");
		radioGroupFieldEditor.addOption("Around X", EnumControllerAxis.X_POSITIVE);		
		radioGroupFieldEditor.addOption("Around Y", EnumControllerAxis.Y_POSITIVE);		
		radioGroupFieldEditor.addOption("Around Z", EnumControllerAxis.Z_POSITIVE);		
		radioGroupFieldEditor.setPropertyName(RotateModifierPropertiesModel.ROTATION_AXIS);
		
		UiAngleFieldEditor angleFieldEditor = new UiAngleFieldEditor(composite, SWT.NONE);
		angleFieldEditor.setEmptyStringAllowed(false);
		angleFieldEditor.setWidthInChars(5);
		angleFieldEditor.setUnit(AngleUnit.DEGREE_ANGLE);
		angleFieldEditor.setPropertyName(RotateModifierPropertiesModel.ROTATION_ANGLE);
		angleFieldEditor.setLabel("Angle");
		
		getController().addFieldEditor(radioGroupFieldEditor);
		getController().addFieldEditor(angleFieldEditor);
	}
}
