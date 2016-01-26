/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.segmentize;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiLengthFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.config.GokoPreference;
import org.goko.core.gcode.rs274ngcv3.modifier.segmentize.SegmentizeModifier;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class SegmentizePropertiesPanel extends AbstractModifierPropertiesPanel<SegmentizeModifier, SegmentizeModifierPropertiesController, SegmentizeModifierPropertiesModel> implements IPropertiesPanel{

	/**
	 * @param context
	 * @param abstractController
	 */
	public SegmentizePropertiesPanel(IEclipseContext context) {
		super(context, new SegmentizeModifierPropertiesController());
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createContent(Composite parent) throws GkException {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(1, false));
		Group composite = new Group(rootComposite, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		composite.setText("Properties");
		composite.setLayout(new GridLayout(1, false));
		new Label(composite, SWT.NONE);
		
		UiLengthFieldEditor lengthFieldEditor = new UiLengthFieldEditor(composite, SWT.NONE);
		lengthFieldEditor.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lengthFieldEditor.setWidthInChars(5);
		lengthFieldEditor.setUnit(GokoPreference.getInstance().getLengthUnit());
		lengthFieldEditor.setPropertyName(SegmentizeModifierPropertiesModel.CHORDAL_TOLERANCE);
		lengthFieldEditor.setLabel("Chordal tolerance");
		
		getController().addFieldEditor(lengthFieldEditor);
	}
}
