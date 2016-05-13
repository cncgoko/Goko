/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.scale;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiBigDecimalFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.scale.ScaleModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class ScalePropertiesPanel extends AbstractModifierPropertiesPanel<ScaleModifier, ScaleModifierPropertiesController, ScaleModifierPropertiesModel> implements IPropertiesPanel{

	/**
	 * @param context
	 * @param abstractController
	 */
	public ScalePropertiesPanel(IEclipseContext context) {
		super(context, new ScaleModifierPropertiesController());
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
		
		UiBigDecimalFieldEditor lengthFieldEditor = new UiBigDecimalFieldEditor(composite, SWT.NONE);
		lengthFieldEditor.setEmptyStringAllowed(false);
		lengthFieldEditor.setWidthInChars(5);		
		lengthFieldEditor.setPropertyName(ScaleModifierPropertiesModel.SCALE_FACTOR);
		lengthFieldEditor.setLabel("Scale");
		
		getController().addFieldEditor(lengthFieldEditor);
	}
}
