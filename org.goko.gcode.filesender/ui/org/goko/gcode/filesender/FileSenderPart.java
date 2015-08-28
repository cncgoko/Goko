/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.gcode.filesender;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.bindings.validator.FilepathValidator;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.execution.IGCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.log.GkLog;
import org.goko.gcode.filesender.controller.GCodeFileSenderBindings;
import org.goko.gcode.filesender.controller.GCodeFileSenderController;

/**
 * A part for GCode file streaming
 *
 * @author PsyKo
 *
 */
public class FileSenderPart extends GkUiComponent<GCodeFileSenderController, GCodeFileSenderBindings> implements IGCodeExecutionListener, PropertyChangeListener {
	private static final GkLog LOG = GkLog.getLogger(FileSenderPart.class);
	@Inject
	private IApplicativeLogService applicativeLogService;
	@Inject
	private IGCodeExecutionMonitorService monitorService;
	private Text txtFilepath;
	private Label lblFilesize;
	private Label lblName;
	private Label lblLastupdate;

	private Label sentCommandsCountTxt;

	private ProgressBar progressSentCommand;

	private Label totalCommandCountTxt;

	private Button btnSendFile;

	private Button btnCancel;

	private Label remainingTimeLbl;

	private Label elapsedTimeLbl;
	private Shell shell;
	private Button btnBrowse;

	//private NatTable natTable;
	private Table table;
	private TableViewer tableViewer;

	@Inject
	public FileSenderPart(IEclipseContext context) {
		super(new GCodeFileSenderController(new GCodeFileSenderBindings()));
		context.set(FileSenderPart.class, this);
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();			
			getController().getDataModel().addPropertyChangeListener(this);
		} catch (GkException e) {
			displayMessage(e);
		}

	}
	@PostConstruct
	protected void postConstruct() throws GkException{
		monitorService.addExecutionListener(this);		
	}
	private void startFileParsingJob(final String filePath) {
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					monitor.beginTask("Opening " + filePath, 100);
					monitor.worked(30);
					try {
						getController().setGCodeFilepath(filePath);						
					} catch (GkException e) {
						displayError(e);
					}
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			displayMessage(new GkTechnicalException(e));
		} catch (InterruptedException e) {
			displayMessage(new GkTechnicalException(e));
		}
	}

	public Shell getShell(){
		return shell;
	}
	/**
	 * Create contents of the view part.
	 *
	 * @throws GkException
	 */
	@PostConstruct
	public void createControls(final Composite parent) throws GkException {

		DropTarget dt = new DropTarget(parent, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				// Set the text field's text to the text being dropped
				startFileParsingJob(((String[]) event.data)[0]);
				refreshCommandTable();
			}
		});		
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell = parent.getShell();

		Composite composite = new Composite(parent, SWT.NONE);		
		composite.setLayout(new GridLayout(1, false));

		Group grpFile = new Group(composite, SWT.NONE);	

		grpFile.setLayout(new GridLayout(1, false));
		grpFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpFile.setText("File");
		
		Composite composite_2 = new Composite(grpFile, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		composite_2.setLayout(new GridLayout(3, false));

		Label lblPath = new Label(composite_2, SWT.NONE);
		lblPath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblPath.setText("Path :");

		txtFilepath = new Text(composite_2, SWT.BORDER);
		txtFilepath.setEditable(false);
		txtFilepath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		btnBrowse = new Button(composite_2, SWT.NONE);
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				FileDialog dialog = new FileDialog(parent.getShell());
				dialog.setText("Open GCode file...");

				String filePath = dialog.open();
				getDataModel().setFilePath(filePath);
				if (StringUtils.isNotBlank(filePath)) {
					startFileParsingJob(filePath);
					refreshCommandTable();
				}
			}


		});
		btnBrowse.setText("Browse");

		Label label = new Label(grpFile, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Composite composite_5 = new Composite(grpFile, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));		
		composite_5.setLayout(new GridLayout(2, false));

		Label lblFileName = new Label(composite_5, SWT.NONE);
		lblFileName.setAlignment(SWT.RIGHT);
		GridData gd_lblFileName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblFileName.widthHint = 100;
		lblFileName.setLayoutData(gd_lblFileName);
		lblFileName.setSize(57, 15);
		
		lblFileName.setText("File name :");

		lblName = new Label(composite_5, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		lblName.setText("--");

		Composite composite_1 = new Composite(grpFile, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		composite_1.setLayout(new GridLayout(4, false));

		Label lblLastModification = new Label(composite_1, SWT.NONE);
		lblLastModification.setText("Last modification :");
		GridData gd_lblLastModification = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblLastModification.widthHint = 100;
		lblLastModification.setLayoutData(gd_lblLastModification);

		lblLastupdate = new Label(composite_1, SWT.NONE);		
		GridData gd_lblLastupdate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_lblLastupdate.widthHint = 150;
		lblLastupdate.setLayoutData(gd_lblLastupdate);

		Label lblSize = new Label(composite_1, SWT.NONE);
		
		lblSize.setText("Size :");

		lblFilesize = new Label(composite_1, SWT.NONE);
		GridData gd_lblFilesize = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblFilesize.widthHint = 80;
		lblFilesize.setLayoutData(gd_lblFilesize);		
		lblFilesize.setText("--");

		Group grpControls = new Group(composite, SWT.NONE);		
		grpControls.setLayout(new GridLayout(1, false));
		grpControls.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		grpControls.setText("Controls");

		Composite composite_3 = new Composite(grpControls, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_3.setLayout(new GridLayout(4, false));

		btnSendFile = new Button(composite_3, SWT.NONE);
		btnSendFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent event) {
				try {					
					progressSentCommand.setState(SWT.NORMAL);				
					getController().startFileStreaming();
				} catch (GkFunctionalException e) {
					LOG.error(e);
					IStatus status = new Status(IStatus.WARNING, "org.goko.gcode.filesender", e.getLocalizedMessage());      
					ErrorDialog.openError(null, "Warning", null, status);
				} catch (GkException e) {
					IStatus status = new Status(IStatus.ERROR, "org.goko.gcode.filesender", e.getLocalizedMessage());      
					ErrorDialog.openError(null, "Error", null, status);
				}
			}
		});
		btnSendFile.setBounds(0, 0, 75, 25);
		btnSendFile.setText("Send file");

		btnCancel = new Button(composite_3, SWT.NONE);
		
		btnCancel.setText("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				progressSentCommand.setState(SWT.PAUSED);
				getController().stopFileStreaming();
			}
		});
		Label lblProgress = new Label(composite_3, SWT.NONE);
		lblProgress.setAlignment(SWT.RIGHT);
		GridData gd_lblProgress = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblProgress.widthHint = 100;
		lblProgress.setLayoutData(gd_lblProgress);
		lblProgress.setText("Line count :");

		totalCommandCountTxt = new Label(composite_3, SWT.NONE);
		GridData gd_totalCommandCountTxt = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_totalCommandCountTxt.widthHint = 80;
		totalCommandCountTxt.setLayoutData(gd_totalCommandCountTxt);		
		totalCommandCountTxt.setText("--");

		Composite composite_4 = new Composite(grpControls, SWT.NONE);
		composite_4.setLayout(new GridLayout(4, false));
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		composite_4.setBounds(0, 0, 64, 64);

		Label lblSentCommands = new Label(composite_4, SWT.NONE);
		lblSentCommands.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblSentCommands.setText("Progress :");

		progressSentCommand = new ProgressBar(composite_4, SWT.NONE);
		progressSentCommand.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_4, SWT.NONE);

		sentCommandsCountTxt = new Label(composite_4, SWT.NONE);
		GridData gd_sentCommandsCountTxt = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_sentCommandsCountTxt.widthHint = 80;
		sentCommandsCountTxt.setLayoutData(gd_sentCommandsCountTxt);
		sentCommandsCountTxt.setText("--");

		Composite composite_6 = new Composite(grpControls, SWT.NONE);
		composite_6.setLayout(new GridLayout(5, false));

		Label lblElapsedTime = new Label(composite_6, SWT.NONE);
		lblElapsedTime.setText("Elapsed time :");

		elapsedTimeLbl = new Label(composite_6, SWT.NONE);
		GridData gd_elapsedTimeLbl = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_elapsedTimeLbl.widthHint = 80;
		elapsedTimeLbl.setLayoutData(gd_elapsedTimeLbl);
		elapsedTimeLbl.setText("New Label");
		new Label(composite_6, SWT.NONE);

		Label lblRemainingTime = new Label(composite_6, SWT.NONE);
		lblRemainingTime.setText("Estimated time :");

		remainingTimeLbl = new Label(composite_6, SWT.NONE);
		GridData gd_remainingTimeLbl = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_remainingTimeLbl.widthHint = 80;
		remainingTimeLbl.setLayoutData(gd_remainingTimeLbl);
		remainingTimeLbl.setText("New Label");

		Group grpGcodeEditor = new Group(composite, SWT.NONE);
		grpGcodeEditor.setLayout(new GridLayout(1, false));
		grpGcodeEditor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		grpGcodeEditor.setText("GCode viewer");

		Composite composite_8 = new Composite(grpGcodeEditor, SWT.NONE);
		GridLayout gl_composite_8 = new GridLayout(1, false);
		gl_composite_8.verticalSpacing = 0;
		gl_composite_8.marginWidth = 0;
		gl_composite_8.marginHeight = 0;
		gl_composite_8.horizontalSpacing = 0;
		composite_8.setLayout(gl_composite_8);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));


		tableViewer = new TableViewer(composite_8, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.VIRTUAL);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE |  SWT.RIGHT);
		TableColumn tblclmnLine = tableViewerColumn.getColumn();
		tblclmnLine.setWidth(50);
		tblclmnLine.setText("Line");
		tableViewerColumn.setLabelProvider(new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {
				GCodeCommand p = (GCodeCommand) cell.getElement();
				cell.setText(String.valueOf(p.getLineNumber()));
				cell.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
				super.update(cell);
			}
		});


		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnCommand = tableViewerColumn_1.getColumn();
		tblclmnCommand.setWidth(300);
		tblclmnCommand.setText("Command");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        	GCodeCommand p = (GCodeCommand) element;
	          return String.valueOf(p.getStringCommand());
	        }
	      });
		tableViewer.setContentProvider(new GCodeTableLazyContentProvider(tableViewer));

		// special settings for the lazy content provider
		tableViewer.setUseHashlookup(true);

		// create the model and set it as input
		tableViewer.setInput(new ArrayList<GCodeCommand>());
		// you must explicitly set the items count
		tableViewer.setItemCount(0);



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
		this.getController().addTextDisplayBinding(sentCommandsCountTxt, "sentCommandCount");
		this.getController().addTextDisplayBinding(totalCommandCountTxt, "totalCommandCount");

		this.getController().addEnableBinding(btnSendFile, "streamingAllowed");
		this.getController().addEnableReverseBinding(btnBrowse, "streamingInProgress");

		{
			IObservableValue widgetObserver = PojoObservables.observeValue( progressSentCommand, "maximum");
			IObservableValue modelObserver = BeanProperties.value( "totalCommandCount").observe(getDataModel());

			Binding binding = getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}

		{
			IObservableValue widgetObserver = PojoObservables.observeValue( progressSentCommand, "selection");
			IObservableValue modelObserver = BeanProperties.value( "sentCommandCount").observe(getDataModel());

			Binding binding = getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}

	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO Set the focus to control
	}
	private void refreshCommandTable() {
		if(getDataModel().getGcodeProvider() != null){
			tableViewer.setInput(getDataModel().getGcodeProvider().getGCodeCommands());
			tableViewer.setItemCount(getDataModel().getGcodeProvider().getGCodeCommands().size());
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionStart(IGCodeExecutionToken token) throws GkException {
		getController().onExecutionStart(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(IGCodeExecutionToken token) throws GkException {
		getController().onExecutionCanceled(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionPause(IGCodeExecutionToken token) throws GkException {		
		getController().onExecutionComplete(token);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.bean.execution.IGCodeExecutionToken)
	 */
	@Override
	public void onExecutionComplete(IGCodeExecutionToken token) throws GkException {	
		System.err.println("FileSenderPart onExecutionComplete");
		if(!shell.isDisposed()){
			final int errorCount 	= token.getErrorCommandCount();
			final int executedCount = token.getExecutedCommandCount();
			final int totalCount 	= token.getCommandCount();
			shell.getDisplay().asyncExec(new Runnable() {
		      @Override
		      public void run() {
		    	  if(executedCount == totalCount){
		    		  MessageDialog.openInformation(shell, "Execution complete", "GCode file successfully executed.");
		    	  }else{
		    		  MessageDialog.openInformation(shell, "Execution finished with errors", "GCode file was completely executed but some error were reported.Please refers to the Problems view.");
		    	  }
		      }
		    });
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeCommandExecutionListener#onCommandStateChanged(org.goko.core.gcode.bean.execution.IGCodeExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onCommandStateChanged(IGCodeExecutionToken token, Integer idCommand) throws GkException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void propertyChange(PropertyChangeEvent changeEvent) {
		if(StringUtils.equals(changeEvent.getPropertyName(), "gcodeProvider")){
			getShell().getDisplay().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					refreshCommandTable();
				}
			});			
		}
	}
}

