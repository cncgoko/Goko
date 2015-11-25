package org.goko.core.execution.monitor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.execution.monitor.executionpart.ExecutionPartController;
import org.goko.core.execution.monitor.executionpart.ExecutionPartModel;
import org.goko.core.log.GkLog;

/**
 * The part for execution control
 * 
 * @author Psyko
 *
 */
public class ExecutionPart extends GkUiComponent<ExecutionPartController, ExecutionPartModel>{
	/** Logger */
	private static final GkLog LOG = GkLog.getLogger(ExecutionPart.class);
	
	/**
	 * Constructor
	 */
	@Inject
	public ExecutionPart(IEclipseContext context) {
		super(new ExecutionPartController());
		ContextInjectionFactory.inject(getController(), context);
		try {
			getController().initialize();
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
	/**
	 * Construct the part
	 * @param parent the parent composite
	 */
	@PostConstruct
	public void postConstruct(Composite parent) {
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
				}
			}
		});
		GridData gd_btnStart = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnStart.heightHint = 32;
		btnStart.setLayoutData(gd_btnStart);
		btnStart.setText("Start queue");
		
		Button btnPause = new Button(composite_1, SWT.NONE);
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
		btnPause.setText("Pause/Resume queue");
		
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
		composite_2.setLayout(new GridLayout(3, false));
		
		Label lblTotal = new Label(composite_2, SWT.NONE);
		GridData gd_lblTotal = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTotal.widthHint = 100;
		lblTotal.setLayoutData(gd_lblTotal);
		lblTotal.setText("Total");
		
		ProgressBar tokenProgressBar = new ProgressBar(composite_2, SWT.NONE);
		tokenProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTokenProgress = new Label(composite_2, SWT.NONE);
		lblTokenProgress.setAlignment(SWT.RIGHT);
		GridData gd_lblTokenProgress = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblTokenProgress.widthHint = 100;
		lblTokenProgress.setLayoutData(gd_lblTokenProgress);
		lblTokenProgress.setText("1/5");
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_3.setLayout(new GridLayout(3, false));
		
		Label lblCurrent = new Label(composite_3, SWT.NONE);
		GridData gd_lblCurrent = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCurrent.widthHint = 100;
		lblCurrent.setLayoutData(gd_lblCurrent);
		lblCurrent.setText("Current token");
		
		ProgressBar lineProgressBar = new ProgressBar(composite_3, SWT.NONE);
		lineProgressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLineProgress = new Label(composite_3, SWT.NONE);
		lblLineProgress.setAlignment(SWT.RIGHT);
		GridData gd_lblLineProgress = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblLineProgress.widthHint = 100;
		lblLineProgress.setLayoutData(gd_lblLineProgress);
		lblLineProgress.setText("262123/305263");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_label.heightHint = 10;
		label.setLayoutData(gd_label);
		label.setBounds(0, 0, 64, 2);
		
		Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		composite_4.setLayout(new GridLayout(3, true));
		
		Label lblElapsedTime = new Label(composite_4, SWT.NONE);
		lblElapsedTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblElapsedTime.setAlignment(SWT.RIGHT);
		lblElapsedTime.setText("Elapsed time :");
		new Label(composite_4, SWT.NONE);
		
		Label lblRemainingTime = new Label(composite_4, SWT.NONE);
		lblRemainingTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblRemainingTime.setText("Remaining time :");
		try {
			this.getController().addTextDisplayBinding(lblLineProgress, ExecutionPartModel.PROPERTY_COMPLETED_LINE_COUNT);
			this.getController().addTextDisplayBinding(lblTokenProgress, ExecutionPartModel.PROPERTY_COMPLETED_TOKEN_COUNT);
			
			this.getController().addEnableBinding(btnStart, ExecutionPartModel.PROPERTY_START_BUTTON_ENABLED);
			this.getController().addEnableBinding(btnPause, ExecutionPartModel.PROPERTY_PAUSE_BUTTON_ENABLED);
			this.getController().addEnableBinding(btnStop , ExecutionPartModel.PROPERTY_STOP_BUTTON_ENABLED);
		} catch (GkException e1) {
			LOG.error(e1);
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( lineProgressBar, "maximum");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOTAL_LINE_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( lineProgressBar, "selection");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_COMPLETED_LINE_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( tokenProgressBar, "maximum");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_TOTAL_TOKEN_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
		{
			IObservableValue widgetObserver = PojoObservables.observeValue( tokenProgressBar, "selection");
			IObservableValue modelObserver = BeanProperties.value(ExecutionPartModel.PROPERTY_COMPLETED_TOKEN_COUNT).observe(getDataModel());
	
			getController().getBindingContext().bindValue( widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		}
	}
}