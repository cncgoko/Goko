/**
 * 
 */
package org.goko.controller.grbl.v11.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.GkUiComponent;
import org.goko.controller.grbl.v11.parts.override.GrblOverrideController;
import org.goko.controller.grbl.v11.parts.override.GrblOverrideModel;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 10 avr. 2017
 */
public class GrblOverridePart extends GkUiComponent<GrblOverrideController, GrblOverrideModel> {

	/**
	 * @param context
	 * @param abstractController
	 */
	@Inject
	public GrblOverridePart(IEclipseContext context) {
		super(context, new GrblOverrideController());
		ContextInjectionFactory.inject(getController(), context);
	}

	@PostConstruct
	public void createControls(final Composite parent) throws GkException {
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setBounds(0, 0, 448, 298);
		composite_1.setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(composite_1, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(7, true);
		gl_composite.marginWidth = 0;
		gl_composite.verticalSpacing = 10;
		composite.setLayout(gl_composite);
		
		Label lblFeed = new Label(composite, SWT.NONE);
		lblFeed.setText("Feed:");
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("100%");
		
		Button btnFeedMinus10 = new Button(composite, SWT.NONE);
		btnFeedMinus10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().decreaseFeedSpeed10();
			}
		});
		GridData gd_btnFeedMinus10 = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btnFeedMinus10.heightHint = 30;
		gd_btnFeedMinus10.widthHint = 50;
		btnFeedMinus10.setLayoutData(gd_btnFeedMinus10);
		btnFeedMinus10.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnFeedMinus10.setText("-10%");
		
		Button btnFeedMinus1 = new Button(composite, SWT.NONE);
		btnFeedMinus1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().decreaseFeedSpeed1();
			}
		});
		GridData gd_btnFeedMinus1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFeedMinus1.heightHint = 30;
		gd_btnFeedMinus1.widthHint = 50;
		btnFeedMinus1.setLayoutData(gd_btnFeedMinus1);
		btnFeedMinus1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnFeedMinus1.setText("-1%");
		
		Button btnFeedReset = new Button(composite, SWT.NONE);
		btnFeedReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().resetFeedSpeed();
			}
		});
		GridData gd_btnFeedReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFeedReset.heightHint = 30;
		gd_btnFeedReset.widthHint = 50;
		btnFeedReset.setLayoutData(gd_btnFeedReset);
		btnFeedReset.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnFeedReset.setText("100%");
		
		Button btnFeedPlus1 = new Button(composite, SWT.NONE);
		btnFeedPlus1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().increaseFeedSpeed1();
			}
		});
		GridData gd_btnFeedPlus1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFeedPlus1.heightHint = 30;
		gd_btnFeedPlus1.widthHint = 50;
		btnFeedPlus1.setLayoutData(gd_btnFeedPlus1);
		btnFeedPlus1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnFeedPlus1.setText("+1%");
		
		Button btnFeedPlus10 = new Button(composite, SWT.NONE);
		btnFeedPlus10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().increaseFeedSpeed10();
			}
		});
		GridData gd_btnFeedPlus10 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnFeedPlus10.heightHint = 30;
		gd_btnFeedPlus10.widthHint = 50;
		btnFeedPlus10.setLayoutData(gd_btnFeedPlus10);
		btnFeedPlus10.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnFeedPlus10.setText("+10%");
		
		Label lblSpindle = new Label(composite, SWT.NONE);
		lblSpindle.setText("Spindle:");
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setText("90%");
		
		Button btnSpindleMinus10 = new Button(composite, SWT.NONE);
		btnSpindleMinus10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().decreaseSpindleSpeed10();
			}
		});
		GridData gd_btnSpindleMinus10 = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btnSpindleMinus10.heightHint = 30;
		gd_btnSpindleMinus10.widthHint = 50;
		btnSpindleMinus10.setLayoutData(gd_btnSpindleMinus10);
		btnSpindleMinus10.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnSpindleMinus10.setText("-10%");
		
		Button btnSpindleMinus1 = new Button(composite, SWT.NONE);
		btnSpindleMinus1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().decreaseSpindleSpeed1();
			}
		});
		GridData gd_btnSpindleMinus1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSpindleMinus1.heightHint = 30;
		gd_btnSpindleMinus1.widthHint = 50;
		btnSpindleMinus1.setLayoutData(gd_btnSpindleMinus1);
		btnSpindleMinus1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnSpindleMinus1.setText("-1%");
		
		Button btnSpindleReset = new Button(composite, SWT.NONE);
		btnSpindleReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().resetSpindleSpeed();
			}
		});
		GridData gd_btnSpindleReset = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSpindleReset.heightHint = 30;
		gd_btnSpindleReset.widthHint = 50;
		btnSpindleReset.setLayoutData(gd_btnSpindleReset);
		btnSpindleReset.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnSpindleReset.setText("100%");
		
		Button btnSpindlePlus1 = new Button(composite, SWT.NONE);
		btnSpindlePlus1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().increaseSpindleSpeed1();
			}
		});
		GridData gd_btnSpindlePlus1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSpindlePlus1.heightHint = 30;
		gd_btnSpindlePlus1.widthHint = 50;
		btnSpindlePlus1.setLayoutData(gd_btnSpindlePlus1);
		btnSpindlePlus1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnSpindlePlus1.setText("+1%");
		
		Button btnSpindlePlus10 = new Button(composite, SWT.NONE);
		btnSpindlePlus10.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().increaseSpindleSpeed10();
			}
		});
		GridData gd_btnSpindlePlus10 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSpindlePlus10.heightHint = 30;
		gd_btnSpindlePlus10.widthHint = 50;
		btnSpindlePlus10.setLayoutData(gd_btnSpindlePlus10);
		btnSpindlePlus10.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnSpindlePlus10.setText("+10%");
		
		Label lblRapid = new Label(composite, SWT.NONE);
		lblRapid.setText("Rapid:");
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setText("15%");
		
		Button btnRapid25 = new Button(composite, SWT.NONE);
		btnRapid25.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().setRapidSpeed25();
			}
		});
		GridData gd_btnRapid25 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnRapid25.heightHint = 30;
		gd_btnRapid25.widthHint = 50;
		btnRapid25.setLayoutData(gd_btnRapid25);
		btnRapid25.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnRapid25.setText("25%");
		
		Button btnRapid50 = new Button(composite, SWT.NONE);
		btnRapid50.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().setRapidSpeed50();
			}
		});
		GridData gd_btnRapid50 = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btnRapid50.heightHint = 30;
		gd_btnRapid50.widthHint = 50;
		btnRapid50.setLayoutData(gd_btnRapid50);
		btnRapid50.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnRapid50.setText("50%");
		
		Button btnRapid100 = new Button(composite, SWT.NONE);
		btnRapid100.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				getController().resetRapidSpeed();
			}
		});
		GridData gd_btnRapid100 = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1);
		gd_btnRapid100.heightHint = 30;
		gd_btnRapid100.widthHint = 50;
		btnRapid100.setLayoutData(gd_btnRapid100);
		btnRapid100.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		btnRapid100.setText("100%");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_2 = new Composite(composite_1, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(3, true);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnNewButton_7 = new Button(composite_2, SWT.NONE);
		GridData gd_btnNewButton_7 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton_7.heightHint = 35;
		btnNewButton_7.setLayoutData(gd_btnNewButton_7);
		btnNewButton_7.setText("Toggle spindle");
		
		Button btnNewButton_8 = new Button(composite_2, SWT.NONE);
		btnNewButton_8.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnNewButton_8.setText("Toggle flood coolant");
		
		Button btnNewButton_9 = new Button(composite_2, SWT.NONE);
		btnNewButton_9.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		btnNewButton_9.setText("Toggle mist coolant");
		
	}
}
