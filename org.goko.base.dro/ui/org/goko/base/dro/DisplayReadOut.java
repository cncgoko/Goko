package org.goko.base.dro;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.base.dro.controller.DisplayReadOutController;
import org.goko.base.dro.controller.DisplayReadOutModel;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
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
	private ECommandService commandService;
	@Inject
	private EHandlerService handlerService;

	@Inject
	public DisplayReadOut(IEclipseContext context) {
		super(new DisplayReadOutController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			displayMessage(e);
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
		if(CollectionUtils.isEmpty(getDataModel().getObservedValuesDefinition())){
			createEmptyPanel(parentComposite);
		}else{
			createFields(parentComposite);
		}
	}

	private void createEmptyPanel(Composite parent){
		parent.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(parent, SWT.WRAP);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);

		Label lblNoValueHere = new Label(composite, SWT.WRAP | SWT.CENTER);
		lblNoValueHere.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(lblNoValueHere, true, true);
		lblNoValueHere.setText("No value here ? You can configure the displayed values in the preferences.");

		Button btnPreferences = new Button(composite, SWT.NONE);
		btnPreferences.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		btnPreferences.setImage(ResourceManager.getPluginImage("org.goko.base.dro", "icons/gear.png"));
		formToolkit.adapt(btnPreferences, true, true);
		btnPreferences.setText("Preferences");
		btnPreferences.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					Command cmd = commandService.getCommand("org.goko.base.dro.command.dropreferences");
					Parameterization param = new Parameterization(cmd.getParameter("goko.org.ui.page.id"), "org.goko.dro.displayPreferences");
					ParameterizedCommand pCmd = new ParameterizedCommand(cmd, new Parameterization[]{param});
					if (handlerService.canExecute(pCmd)) {
						handlerService.executeHandler(pCmd);
					}
				} catch (NotDefinedException e1) {
					displayError(new GkTechnicalException(e1));
				}
			}
		});
	}
	private void createFields(Composite composite){
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
		composite.setLayout(new GridLayout(2, false));

		for(MachineValueDefinition definition : getDataModel().getObservedValuesDefinition()){
			Label label = new Label(composite, SWT.NONE);
			label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
			label.setFont(SWTResourceManager.getFont("Segoe UI", 13, SWT.BOLD));
			label.setText(definition.getName());

			Text valueTxt = new Text(composite, SWT.BORDER | SWT.RIGHT | SWT.READ_ONLY);
			valueTxt.setText("--");
			valueTxt.setFont(SWTResourceManager.getFont("Consolas", 14, SWT.BOLD));

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
			createControls(parentComposite);
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