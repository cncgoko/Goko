package org.goko.core.execution.monitor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.service.IExecutionService;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * The part for execution control
 * 
 * @author Psyko
 *
 */
public class ExecutionPart {
	/** The execution service */
	@Inject
	private IExecutionService<ExecutionState, ExecutionToken<ExecutionState>> executionService;
	private IWorkspaceService workspaceService;
	
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
					executionService.beginQueueExecution();					
				} catch (GkException e1) {
					e1.printStackTrace();
				}
			}
		});
		GridData gd_btnStart = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnStart.heightHint = 32;
		btnStart.setLayoutData(gd_btnStart);
		btnStart.setText("Start queue");
		
		Button btnNewButton = new Button(composite_1, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnNewButton.setText("Pause queue");
		
		Button btnNewButton_1 = new Button(composite_1, SWT.NONE);
		btnNewButton_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnNewButton_1.setText("Stop queue");
		
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
		
		ProgressBar progressBar = new ProgressBar(composite_2, SWT.NONE);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_2 = new Label(composite_2, SWT.NONE);
		GridData gd_label_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_2.widthHint = 100;
		label_2.setLayoutData(gd_label_2);
		label_2.setText("1/5");
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite_3.setLayout(new GridLayout(3, false));
		
		Label lblCurrent = new Label(composite_3, SWT.NONE);
		GridData gd_lblCurrent = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblCurrent.widthHint = 100;
		lblCurrent.setLayoutData(gd_lblCurrent);
		lblCurrent.setText("Current token");
		
		ProgressBar progressBar_1 = new ProgressBar(composite_3, SWT.NONE);
		progressBar_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_3 = new Label(composite_3, SWT.NONE);
		GridData gd_label_3 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_label_3.widthHint = 100;
		label_3.setLayoutData(gd_label_3);
		label_3.setText("262123/305263");
		
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
	
	}
}