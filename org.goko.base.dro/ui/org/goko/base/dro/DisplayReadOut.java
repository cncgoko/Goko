package org.goko.base.dro;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
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
	private Composite parentComposite;

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

		this.parentComposite = new Composite(parent, SWT.NONE);
		createFields(parentComposite);
	}

	private void createFields(Composite composite){
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
	/**
	 * Recreate fields for data when settings changes
	 * @param list the changed setting (not used, only for notification)
	 * @throws GkException GkException
	 */
	@Inject
	private void update(@Preference(nodePath = DROServiceImpl.SERVICE_ID, value=IDROPreferencesConstants.KEY_VALUES_ID_LIST) String list ) throws GkException{
		if(parentComposite != null){
			getController().updateDisplayedValues();
			Composite parent = parentComposite.getParent();
			parentComposite.dispose();
			parentComposite = new Composite(parent, SWT.NONE);
			createFields(parentComposite);
			parent.setSize(parent.getSize());
//			parent.redraw();
//			parent.update();
			parent.layout();
		}
	}

	@Focus
	public void onFocus() {
		//TODO Your code here
	}


}