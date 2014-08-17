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
package org.goko.gcode.filesender;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.bindings.validator.FilepathValidator;
import org.goko.core.common.exception.GkException;
import org.goko.gcode.filesender.controller.GCodeFileSenderBindings;
import org.goko.gcode.filesender.controller.GCodeFileSenderController;
import org.goko.gcode.filesender.editor.GCodeEditor;

/**
 * A part for GCode file streaming
 *
 * @author PsyKo
 *
 */
public class FileSenderPart extends GkUiComponent<GCodeFileSenderController, GCodeFileSenderBindings>{
	// s
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Text txtFilepath;
	private Label lblFilesize;
	private Label lblName;
	private Label lblLastupdate;
	private GCodeEditor gCodeTextDisplay;

	private Label sentCommandsCountTxt;

	private ProgressBar progressSentCommand;

	private Label totalCommandCountTxt;

	private Button btnSendFile;

	private Button btnCancel;

	private Label remainingTimeLbl;

	private Label elapsedTimeLbl;


	@Inject
	public FileSenderPart(IEclipseContext context) {
		super(new GCodeFileSenderController(new GCodeFileSenderBindings()));
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create contents of the view part.
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(final Composite parent) throws GkException {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		formToolkit.adapt(composite);
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));

		Group grpFile = new Group(composite, SWT.NONE);
		grpFile.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));

		grpFile.setLayout(new GridLayout(1, false));
		grpFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpFile.setText("File");
		formToolkit.adapt(grpFile);
		formToolkit.paintBordersFor(grpFile);

		Composite composite_2 = new Composite(grpFile, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.adapt(composite_2);
		formToolkit.paintBordersFor(composite_2);
		composite_2.setLayout(new GridLayout(3, false));

				Label lblPath = new Label(composite_2, SWT.NONE);
				lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				formToolkit.adapt(lblPath, true, true);
				lblPath.setText("Path :");

						txtFilepath = new Text(composite_2, SWT.BORDER);
						txtFilepath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
						formToolkit.adapt(txtFilepath, true, true);

								Button btnBrowse = new Button(composite_2, SWT.NONE);
								btnBrowse.addMouseListener(new MouseAdapter() {
									@Override
									public void mouseUp(MouseEvent e) {
										FileDialog dialog = new FileDialog(parent.getShell());
										dialog.setText("Open GCode file...");

										String filePath = dialog.open();
										if(StringUtils.isNotBlank(filePath)){
											try {
												if(getController().validate()){
													getController().setGCodeFilepath(filePath);
												}
											} catch (GkException e1) {
												getController().notifyException(e1);
											}
										}

									}
								});
								formToolkit.adapt(btnBrowse, true, true);
								btnBrowse.setText("Browse");

		Label label = formToolkit.createSeparator(grpFile, SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite_5 = formToolkit.createComposite(grpFile, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.paintBordersFor(composite_5);
		composite_5.setLayout(new GridLayout(2, false));

				Label lblFileName = new Label(composite_5, SWT.NONE);
				lblFileName.setAlignment(SWT.RIGHT);
				GridData gd_lblFileName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblFileName.widthHint = 100;
				lblFileName.setLayoutData(gd_lblFileName);
				lblFileName.setSize(57, 15);
				formToolkit.adapt(lblFileName, true, true);
				lblFileName.setText("File name :");

								lblName = new Label(composite_5, SWT.NONE);
								lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
								formToolkit.adapt(lblName, true, true);
								lblName.setText("--");

		Composite composite_1 = new Composite(grpFile, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(composite_1);
		formToolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new GridLayout(4, false));

		Label lblLastModification = formToolkit.createLabel(composite_1, "Last modification :", SWT.NONE);
		GridData gd_lblLastModification = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblLastModification.widthHint = 100;
		lblLastModification.setLayoutData(gd_lblLastModification);

		lblLastupdate = formToolkit.createLabel(composite_1, "--", SWT.NONE);
		GridData gd_lblLastupdate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblLastupdate.widthHint = 150;
		lblLastupdate.setLayoutData(gd_lblLastupdate);

				Label lblSize = new Label(composite_1, SWT.NONE);
				formToolkit.adapt(lblSize, true, true);
				lblSize.setText("Size :");

				lblFilesize = new Label(composite_1, SWT.NONE);
				GridData gd_lblFilesize = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_lblFilesize.widthHint = 80;
				lblFilesize.setLayoutData(gd_lblFilesize);
				formToolkit.adapt(lblFilesize, true, true);
				lblFilesize.setText("--");

		Group grpControls = new Group(composite, SWT.NONE);
		grpControls.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		grpControls.setLayout(new GridLayout(1, false));
		grpControls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpControls.setText("Controls");
		formToolkit.adapt(grpControls);
		formToolkit.paintBordersFor(grpControls);

		Composite composite_3 = new Composite(grpControls, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		formToolkit.adapt(composite_3);
		formToolkit.paintBordersFor(composite_3);
		composite_3.setLayout(new GridLayout(4, false));

		btnSendFile = new Button(composite_3, SWT.NONE);
		btnSendFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().startFileStreaming();
			}
		});
		btnSendFile.setBounds(0, 0, 75, 25);
		formToolkit.adapt(btnSendFile, true, true);
		btnSendFile.setText("Send file");

		btnCancel = new Button(composite_3, SWT.NONE);

		formToolkit.adapt(btnCancel, true, true);
		btnCancel.setText("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().stopFileStreaming();
			}
		});
		Label lblProgress = new Label(composite_3, SWT.NONE);
		lblProgress.setAlignment(SWT.RIGHT);
		GridData gd_lblProgress = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblProgress.widthHint = 100;
		lblProgress.setLayoutData(gd_lblProgress);
		formToolkit.adapt(lblProgress, true, true);
		lblProgress.setText("Line count :");

		totalCommandCountTxt = new Label(composite_3, SWT.NONE);
		GridData gd_totalCommandCountTxt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_totalCommandCountTxt.widthHint = 80;
		totalCommandCountTxt.setLayoutData(gd_totalCommandCountTxt);
		formToolkit.adapt(totalCommandCountTxt, true, true);
		totalCommandCountTxt.setText("--");

		Composite composite_4 = new Composite(grpControls, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_4.setBounds(0, 0, 64, 64);
		formToolkit.adapt(composite_4);
		formToolkit.paintBordersFor(composite_4);

		Label lblSentCommands = new Label(composite_4, SWT.NONE);
		lblSentCommands.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(lblSentCommands, true, true);
		lblSentCommands.setText("Progress :");

				progressSentCommand = new ProgressBar(composite_4, SWT.NONE);
				progressSentCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				formToolkit.adapt(progressSentCommand, true, true);
		new Label(composite_4, SWT.NONE);

		sentCommandsCountTxt = new Label(composite_4, SWT.NONE);
		GridData gd_sentCommandsCountTxt = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sentCommandsCountTxt.widthHint = 80;
		sentCommandsCountTxt.setLayoutData(gd_sentCommandsCountTxt);
		formToolkit.adapt(sentCommandsCountTxt, true, true);
		sentCommandsCountTxt.setText("--");

				Composite composite_6 = new Composite(grpControls, SWT.NONE);
				formToolkit.adapt(composite_6);
				formToolkit.paintBordersFor(composite_6);
				composite_6.setLayout(new GridLayout(5, false));

				Label lblElapsedTime = new Label(composite_6, SWT.NONE);
				formToolkit.adapt(lblElapsedTime, true, true);
				lblElapsedTime.setText("Elapsed time :");

				elapsedTimeLbl = new Label(composite_6, SWT.NONE);
				GridData gd_elapsedTimeLbl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_elapsedTimeLbl.widthHint = 80;
				elapsedTimeLbl.setLayoutData(gd_elapsedTimeLbl);
				formToolkit.adapt(elapsedTimeLbl, true, true);
				elapsedTimeLbl.setText("New Label");
				new Label(composite_6, SWT.NONE);

				Label lblRemainingTime = new Label(composite_6, SWT.NONE);
				formToolkit.adapt(lblRemainingTime, true, true);
				lblRemainingTime.setText("Estimated time :");

				remainingTimeLbl = new Label(composite_6, SWT.NONE);
				GridData gd_remainingTimeLbl = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
				gd_remainingTimeLbl.widthHint = 80;
				remainingTimeLbl.setLayoutData(gd_remainingTimeLbl);
				formToolkit.adapt(remainingTimeLbl, true, true);
				remainingTimeLbl.setText("New Label");

		Group grpGcodeEditor = new Group(composite, SWT.NONE);
		grpGcodeEditor.setLayout(new GridLayout(1, false));
		grpGcodeEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpGcodeEditor.setText("GCode viewer");
		formToolkit.adapt(grpGcodeEditor);
		formToolkit.paintBordersFor(grpGcodeEditor);

		gCodeTextDisplay = new GCodeEditor(grpGcodeEditor, null, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		final StyledText styledText = gCodeTextDisplay.getTextWidget();
		styledText.setEditable(false);
		styledText.setIndent(5);
		styledText.setMarginColor(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		styledText.setLeftMargin(20);
		styledText.setFont(SWTResourceManager.getFont("Consolas", 10, SWT.NORMAL));
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		initCustomBindings();
	}

	private void initCustomBindings() throws GkException {
		this.getController().addListener(this);
		this.getController().addTextModifyBinding(txtFilepath, "filePath", new FilepathValidator());

		this.getController().addTextDisplayBinding(elapsedTimeLbl, "elapsedTime");
		this.getController().addTextDisplayBinding(remainingTimeLbl, "remainingTime");

		this.getController().addTextDisplayBinding(lblFilesize, "fileSize");
		this.getController().addTextDisplayBinding(lblLastupdate, "fileLastUpdate");
		this.getController().addTextDisplayBinding(lblName, "fileName");

		this.getController().addGCodeViewerBinding(gCodeTextDisplay);
		this.getController().addTextDisplayBinding(sentCommandsCountTxt, "sentCommandCount");
		this.getController().addTextDisplayBinding(totalCommandCountTxt, "totalCommandCount");


		this.getController().addEnableBinding(btnSendFile, "streamingAllowed");
		// this.getController().addEnableBinding(btnCancel, "streamingInProgress");
		// Progress bar bindings
		{
			IObservableValue widgetObserver = PojoObservables.observeValue(progressSentCommand, "maximum");
			IObservableValue modelObserver  = BeanProperties.value("totalCommandCount").observe(getDataModel());

			Binding binding = getController().getBindingContext().bindValue(widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}


		{
			IObservableValue widgetObserver = PojoObservables.observeValue(progressSentCommand, "selection");
			IObservableValue modelObserver  = BeanProperties.value("sentCommandCount").observe(getDataModel());

			Binding binding = getController().getBindingContext().bindValue(widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}

	}



	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}
