package org.goko.base.dro;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.base.dro.controller.DisplayReadOutController;
import org.goko.base.dro.controller.DisplayReadOutModel;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.MachineValueDefinition;

/**
 * DRO part
 *
 * @author PsyKo
 *
 */
public class DisplayReadOut extends GkUiComponent<DisplayReadOutController, DisplayReadOutModel>{
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());


	@Inject
	public DisplayReadOut(IEclipseContext context) {
		super(new DisplayReadOutController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the view part.
	 * @throws GkException GkException
	 */
	@PostConstruct
	public void createControls(Composite parent) {

		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayout(new GridLayout(2, false));

		for(MachineValueDefinition definition : getDataModel().getObservedValuesDefinition()){
			Label label = new Label(composite, SWT.NONE);
			label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			label.setFont(SWTResourceManager.getFont("Segoe UI", 15, SWT.BOLD));
			label.setText(definition.getName());

			Text valueTxt = new Text(composite, SWT.BORDER | SWT.RIGHT | SWT.READ_ONLY);
			valueTxt.setText("--");
			valueTxt.setFont(SWTResourceManager.getFont("Consolas", 16, SWT.BOLD));

			GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
			gridData.widthHint = 120;
			valueTxt.setLayoutData(gridData );
			getController().enableTextBindingOnValue(valueTxt, definition.getId());
			formToolkit.adapt(valueTxt, true, true);
		}
	}

	@Focus
	public void onFocus() {
		//TODO Your code here
	}


}