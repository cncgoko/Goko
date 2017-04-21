package org.goko.core.execution.monitor;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.common.dialog.GkDialog;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executionpart.ExecutionPartController;
import org.goko.core.execution.monitor.executionpart.ExecutionPartModel;
import org.goko.core.gcode.execution.ExecutionQueueType;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;

/**
 * The part for execution control
 * 
 * @author Psyko
 *
 */
public class ExecutionPart extends GkUiComponent<ExecutionPartController, ExecutionPartModel> implements IGCodeExecutionListener<ExecutionTokenState, ExecutionToken<ExecutionTokenState>>{
	/** Logger */
	private static final GkLog LOG = GkLog.getLogger(ExecutionPart.class);
	/** EXECUTION_TIMER_REFRESH_INTERVAL */
	private static final int EXECUTION_TIMER_REFRESH_INTERVAL_MS = 1000;
	/** Parent composite */
	private Composite parent;
	private Button btnPause;
	/**
	 * Constructor
	 */
	@Inject
	public ExecutionPart(IEclipseContext context) {
		super(context, new ExecutionPartController());		
	}
	
	/**
	 * Construct the part
	 * @param parent the parent composite
	 */
	@PostConstruct
	public void postConstruct(final Composite parent) {
		this.parent = parent;
		addResizeListener(parent);
		parent.setLayout(new GridLayout(1, false));
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.verticalSpacing = 0;
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(3, true);
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnStart = new Button(composite_1, SWT.NONE);
		
		GridData gd_btnStart = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_btnStart.heightHint = 32;
		btnStart.setLayoutData(gd_btnStart);
		btnStart.setText("Start queue");
		
		btnPause = new Button(composite_1, SWT.NONE);
		btnPause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().pauseResumeQueueExecution();
				} catch (GkException e1) {
					LOG.error(e1);
				}
			}
		});
		btnPause.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnPause.setText("Pause queue");
		
		Button btnStop = new Button(composite_1, SWT.NONE);
		btnStop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				try {
					getController().stopQueueExecution();					
				} catch (GkException e1) {
					LOG.error(e1);
				}
			}
		});
		btnStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnStop.setText("Stop queue");
		
		Label label_1 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label_1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_label_1.heightHint = 10;
		label_1.setLayoutData(gd_label_1);
		label_1.setBounds(0, 0, 438, 10);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_2.setLayout(new GridLayout(2, false));
		
		Label lblTotal = new Label(composite_2, SWT.NONE);
		GridData gd_lblTotal = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotal.widthHint = 80;
		lblTotal.setLayoutData(gd_lblTotal);
		lblTotal.setText("Total ");
		
		ProgressBar totalProgressBar = new ProgressBar(composite_2, SWT.SMOOTH);
		GridData gd_totalProgressBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_totalProgressBar.heightHint = 20;
		totalProgressBar.setLayoutData(gd_totalProgressBar);
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_3.setLayout(new GridLayout(2, false));
		
		Label lblCurrent = new Label(composite_3, SWT.NONE);
		GridData gd_lblCurrent = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCurrent.widthHint = 80;
		lblCurrent.setLayoutData(gd_lblCurrent);
		lblCurrent.setText("Current token");
		
		ProgressBar currentTokenProgressBar = new ProgressBar(composite_3, SWT.SMOOTH);
		GridData gd_currentTokenProgressBar = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_currentTokenProgressBar.heightHint = 20;
		currentTokenProgressBar.setLayoutData(gd_currentTokenProgressBar);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_label.heightHint = 10;
		label.setLayoutData(gd_label);
		label.setBounds(0, 0, 64, 2);
		
		Composite lblElapsedLines = new Composite(composite, SWT.NONE);
		lblElapsedLines.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		lblElapsedLines.setLayout(new GridLayout(4, false));
		new Label(lblElapsedLines, SWT.NONE);
		
		Label lblCompleted = new Label(lblElapsedLines, SWT.NONE);
		lblCompleted.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblCompleted.setAlignment(SWT.RIGHT);
		GridData gd_lblCompleted = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCompleted.widthHint = 100;
		lblCompleted.setLayoutData(gd_lblCompleted);
		lblCompleted.setText("Completed");
		new Label(lblElapsedLines, SWT.NONE);
		
		Label lblTotal_1 = new Label(lblElapsedLines, SWT.NONE);
		GridData gd_lblTotal_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotal_1.widthHint = 100;
		lblTotal_1.setLayoutData(gd_lblTotal_1);
		lblTotal_1.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTotal_1.setText("Total");
		
		Label lblTokens = new Label(lblElapsedLines, SWT.NONE);
		lblTokens.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTokens.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTokens.setText("Tokens");
		
		Label lblElapsedTokens = new Label(lblElapsedLines, SWT.NONE);
		lblElapsedTokens.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblElapsedTokens.setAlignment(SWT.RIGHT);
		lblElapsedTokens.setText("1");
				
		Label lblSeparatorTokens = new Label(lblElapsedLines, SWT.NONE);
		lblSeparatorTokens.setText("/");
		
		Label lblTotalTokens = new Label(lblElapsedLines, SWT.NONE);
		lblTotalTokens.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblTotalTokens.setText("5");
		
		Label lblLines = new Label(lblElapsedLines, SWT.NONE);
		lblLines.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblLines.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLines.setText("Lines");
		
		Label lblLineProgress = new Label(lblElapsedLines, SWT.NONE);
		lblLineProgress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblLineProgress.setAlignment(SWT.RIGHT);
		lblLineProgress.setText("262123");
		
		
		Label lblSeparatorLines = new Label(lblElapsedLines, SWT.NONE);
		lblSeparatorLines.setText("/");
		
		Label lblTotalLines = new Label(lblElapsedLines, SWT.NONE);
		lblTotalLines.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblTotalLines.setText("302632");
		
		Label lblTime = new Label(lblElapsedLines, SWT.NONE);
		lblTime.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTime.setAlignment(SWT.RIGHT);
		lblTime.setText("Time");
		
		final Label lblElapsedTime = new Label(lblElapsedLines, SWT.NONE);
		lblElapsedTime.setAlignment(SWT.RIGHT);
		lblElapsedTime.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		new Label(lblElapsedLines, SWT.NONE);
		
		Label lblEstimatedTime = new Label(lblElapsedLines, SWT.NONE);
		lblEstimatedTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		try {
			
			this.getController().addEnableBinding(btnStart, ExecutionPartModel.PROPERTY_START_BUTTON_ENABLED);
			this.getController().addEnableBinding(btnPause, ExecutionPartModel.PROPERTY_PAUSE_BUTTON_ENABLED);
			this.getController().addEnableBinding(btnStop , ExecutionPartModel.PROPERTY_STOP_BUTTON_ENABLED);
			
			this.getController().addTextDisplayBinding(lblElapsedTokens, ExecutionPartModel.PROPERTY_COMPLETED_TOKEN_COUNT);
			this.getController().addTextDisplayBinding(lblTotalTokens, ExecutionPartModel.PROPERTY_TOTAL_TOKEN_COUNT);
			
			this.getController().addTextDisplayBinding(lblLineProgress , ExecutionPartModel.PROPERTY_COMPLETED_LINE_COUNT );
			this.getController().addTextDisplayBinding(lblTotalLines , ExecutionPartModel.PROPERTY_TOTAL_LINE_COUNT );
			
			this.getController().addTextDisplayBinding(lblElapsedTime  , ExecutionPartModel.PROPERTY_ELAPSED_TIME_STRING );			
			this.getController().addTextDisplayBinding(lblEstimatedTime  , ExecutionPartModel.PROPERTY_ESTIMATED_TIME_STRING );			
		} catch (GkException e1) {
			LOG.error(e1);
		}		
		{ // Binding of line progress
			IObservableValue widgetObserver = PojoObservables.observeValue( currentTokenProgressBar, "state");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOTAL_PROGRESS_BAR_STATE).observe(getDataModel());
			
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}		
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( currentTokenProgressBar, "maximum");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOKEN_LINE_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( currentTokenProgressBar, "selection");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_COMPLETED_LINE_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( currentTokenProgressBar, "state");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOKEN_PROGRESS_BAR_STATE).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}		
		{ // Binding of tokenprogress
			IObservableValue widgetObserver = PojoObservables.observeValue( totalProgressBar, "state");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOTAL_PROGRESS_BAR_STATE).observe(getDataModel());
			
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}		
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( totalProgressBar, "maximum");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOTAL_TOKEN_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( totalProgressBar, "selection");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_COMPLETED_TOKEN_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		
		btnStart.addMouseListener(new MouseAdapter() {
			/** (inheritDoc)
			 * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseUp(MouseEvent e) {
				try {					
					getController().beginQueueExecution();	
					
				} catch (GkException e1) {
					LOG.error(e1);
					GkDialog.openDialog(e1);
				}
			}
		});
		
		try {
			getController().getExecutionService().addExecutionListener(ExecutionQueueType.DEFAULT, this);
			getController().getExecutionService().addExecutionListener(ExecutionQueueType.SYSTEM, this);
		} catch (GkException e1) {
			LOG.error(e1);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
				
		parent.getDisplay().asyncExec(new Runnable() {
			/** Runnable used to update execution time measurement */
			@Override
			public void run() {														
				if(parent.isDisposed()){
					return;
				}				
				Display display = parent.getDisplay();
				Date startDate = ExecutionPart.this.getDataModel().getExecutionQueueStartDate();
				if(startDate != null){
					long durationInMilliseconds = new Date().getTime() - startDate.getTime();
					String durationStr = DurationFormatUtils.formatDuration(durationInMilliseconds, "HH:mm:ss");
					ExecutionPart.this.getDataModel().setElapsedTimeString(durationStr);
				}							
				if(ExecutionPart.this.getDataModel().isExecutionTimerActive()){
					// Respawn it only if the queue is running
					display.timerExec(EXECUTION_TIMER_REFRESH_INTERVAL_MS, this);
				}
			}
		});	
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {
		parent.getDisplay().asyncExec(new Runnable() {
			/** Runnable used to update execution time measurement */
			@Override
			public void run() {														
				if(parent.isDisposed()){					
					return;
				}
				btnPause.setText("Resume queue");
			}
		});
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(ExecutionToken<ExecutionTokenState> token) throws GkException {
		parent.getDisplay().asyncExec(new Runnable() {
			/** Runnable used to update execution time measurement */
			@Override
			public void run() {														
				if(parent.isDisposed()){
					return;
				}
				btnPause.setText("Pause queue");
			}
		});
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {
		// TODO Auto-generated method stub
		
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		// TODO Auto-generated method stub
		
	}
}