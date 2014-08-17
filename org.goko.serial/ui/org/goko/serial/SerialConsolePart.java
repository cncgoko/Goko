/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.serial;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.serial.bindings.SerialConsoleBindings;
import org.goko.serial.bindings.SerialConsoleController;

public class SerialConsolePart extends GkUiComponent<SerialConsoleController, SerialConsoleBindings> {
	private Text currentCommandTxt;
	private final FormToolkit formToolkit = new FormToolkit( Display.getDefault());

	private StyledText styledText;
	private Label lblLineEnd;
	private Composite composite_2;
	private Combo combo;
	private GkCombo<LabeledValue<String>> endLineTokenCombo;
	private Button btnCheckButton;
	private Button eraseConsoleBtn;
	private Button lockScrollButton;
	private Composite composite_1;
	private Composite composite_3;

	private static final String CONSOLE_ENABLED = "org.goko.serial.consoleEnabled";
	private static final String CONSOLE_SCROLL_LOCKED = "org.goko.serial.consoleScrollLocked";

	@Inject
	public SerialConsolePart(IEclipseContext context) {
		super(new SerialConsoleController(new SerialConsoleBindings()));
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the view part.
	 *
	 * @throws GkException
	 *             GkException
	 */
	@PostConstruct
	public void createControls(final Composite parent, MPart part) throws GkException {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		Composite composite = new Composite(parent, SWT.NONE);
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);

		composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new GridLayout(2, false));

		currentCommandTxt = new Text(composite_1, SWT.BORDER);
		currentCommandTxt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		currentCommandTxt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.Selection) {
					getController().sendCurrentCommand();
				} else {
					if (e.keyCode == SWT.ARROW_UP) {
						getController().selectPreviousCommandInHistory();
					}else{
						getController().resetCommandHistoryIndex();
					}
				}
			}
		});

		Button btnSend = new Button(composite_1, SWT.NONE);
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().sendCurrentCommand();
			}
		});
		btnSend.setText("Send");

		composite_2 = formToolkit.createComposite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		GridLayout gl_composite_2 = new GridLayout(6, false);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		composite_2.setLayout(gl_composite_2);
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);

		lblLineEnd = formToolkit.createLabel(composite_2, "Line end :",
				SWT.NONE);
		lblLineEnd.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		endLineTokenCombo = new GkCombo<LabeledValue<String>>(composite_2,
				SWT.NONE);

		combo = endLineTokenCombo.getCombo();
		formToolkit.paintBordersFor(combo);
		combo.select(2);
		new Label(composite_2, SWT.NONE);

		btnCheckButton = new Button(composite_2, SWT.CHECK);
		btnCheckButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		btnCheckButton.setSelection(true);
		formToolkit.adapt(btnCheckButton, true, true);
		btnCheckButton.setText("Enable console");

		lockScrollButton = new Button(composite_2, SWT.TOGGLE);
		lockScrollButton.setImage(ResourceManager.getPluginImage("org.goko.serial", "icons/lock.png"));
		formToolkit.adapt(lockScrollButton, true, true);

		eraseConsoleBtn = formToolkit.createButton(composite_2, "", SWT.NONE);
		eraseConsoleBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,false, false, 1, 1));
		eraseConsoleBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().clearConsole();
			}
		});
		eraseConsoleBtn.setImage(ResourceManager.getPluginImage(
				"org.goko.serial", "icons/eraser.png"));

		composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(1, false));
		int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
		styledText = new StyledText(composite_3, style);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		styledText.setWordWrap(false);
		styledText.setFont(SWTResourceManager
				.getFont("Consolas", 9, SWT.NORMAL));
		styledText.setCapture(true);
		styledText.setEditable(false);
		styledText.setIndent(5);
		formToolkit.adapt(styledText);
		formToolkit.paintBordersFor(styledText);

		initDataBindings(part);
		getController().setConsoleWidget(styledText);
	}

	protected DataBindingContext initDataBindings(MPart part) throws GkException {
		DataBindingContext bindingContext = new DataBindingContext();

		getController().addTextDisplayBinding(styledText, "console");
		getController().addSelectionBinding(btnCheckButton, "enabled");
		getController().addSelectionBinding(lockScrollButton, "lockScroll");
		getController().addEnableBinding(styledText, "enabled");
		getController().addItemsBinding(endLineTokenCombo, "choiceEndLineToken");
		getController().addItemSelectionBinding(endLineTokenCombo, "endLineToken");
		getController().addTextModifyBinding(currentCommandTxt, "currentCommand");

		IObservableValue observeBackgroundStyledTextObserveWidget = WidgetProperties .background().observe(styledText);
		IObservableValue backgroundBindingsObserveValue = BeanProperties.value( "background").observe(getDataModel());
		bindingContext.bindValue(observeBackgroundStyledTextObserveWidget, backgroundBindingsObserveValue, new UpdateValueStrategy( UpdateValueStrategy.POLICY_NEVER), null);
		//

		Map<String, String> state = part.getPersistedState();
		String consoleEnabledStr = state.get(CONSOLE_ENABLED);
		if(StringUtils.isNotEmpty(consoleEnabledStr)){
			getDataModel().setEnabled(BooleanUtils.toBoolean(consoleEnabledStr));
		}
		String consoleScrollStr = state.get(CONSOLE_SCROLL_LOCKED);
		if(StringUtils.isNotEmpty(consoleScrollStr)){
			getDataModel().setLockScroll(BooleanUtils.toBoolean(consoleScrollStr));
		}
		return bindingContext;
	}

	@PersistState
	public void persist(MPart part) {
		if(getDataModel() != null){
			part.getPersistedState().put(CONSOLE_ENABLED, String.valueOf(getDataModel().isEnabled()));
			part.getPersistedState().put(CONSOLE_SCROLL_LOCKED, String.valueOf(getDataModel().isLockScroll()));
		}
	}
}
