/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.modifierbuilder.wrap;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.preferences.fieldeditor.ui.UiBigDecimalFieldEditor;
import org.goko.common.preferences.fieldeditor.ui.UiRadioGroupFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifier;
import org.goko.core.gcode.rs274ngcv3.modifier.wrap.WrapModifierAxis;
import org.goko.core.workspace.bean.IPropertiesPanel;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.AbstractModifierPropertiesPanel;

/**
 * @author PsyKo
 * @date 17 janv. 2016
 */
public class WrapPropertiesPanel extends AbstractModifierPropertiesPanel<WrapModifier, WrapModifierPropertiesController, WrapModifierPropertiesModel> implements IPropertiesPanel{

	/**
	 * @param context
	 * @param abstractController
	 */
	public WrapPropertiesPanel(IEclipseContext context) {
		super(context, new WrapModifierPropertiesController());
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
//		voir :
//			http://eclipsercpdev.blogspot.fr/2013/04/databinding-with-radio-buttons-and.html
//			http://www.vogella.com/tutorials/EclipseDataBinding/article.html#SelectObservableValue
		
		UiRadioGroupFieldEditor<WrapModifierAxis> radioGroupFieldEditor = new UiRadioGroupFieldEditor<WrapModifierAxis>(composite, SWT.NONE);
		radioGroupFieldEditor.setLabel("Axis transformation");
		radioGroupFieldEditor.addOption(WrapModifierAxis.X_TO_A_AXIS.getLabel(), WrapModifierAxis.X_TO_A_AXIS);
		radioGroupFieldEditor.addOption(WrapModifierAxis.Y_TO_A_AXIS.getLabel(), WrapModifierAxis.Y_TO_A_AXIS);
		// Default value
		getDataModel().setAxis(WrapModifierAxis.X_TO_A_AXIS);
		
		radioGroupFieldEditor.setPropertyName(WrapModifierPropertiesModel.AXIS);		
		
		UiBigDecimalFieldEditor ratio = new UiBigDecimalFieldEditor(composite, SWT.NONE);
		ratio.setEmptyStringAllowed(false);
		ratio.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		ratio.setWidthInChars(8);		
		ratio.setPropertyName(WrapModifierPropertiesModel.RATIO);
		ratio.setLabel("Unit ratio");
		
		getController().addFieldEditor(radioGroupFieldEditor);
		getController().addFieldEditor(ratio);
	}
}
