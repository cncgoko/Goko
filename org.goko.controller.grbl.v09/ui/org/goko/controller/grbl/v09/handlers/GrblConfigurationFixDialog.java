/**
 * 
 */
package org.goko.controller.grbl.v09.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Psyko
 * @date 7 juin 2016
 */
public class GrblConfigurationFixDialog extends Dialog {
	/** The description of the fixes */
	private String[] fixProposal;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public GrblConfigurationFixDialog(Shell parentShell, String[] fixProposal) {
		super(parentShell);
		this.fixProposal = fixProposal;		
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Configuration fixes");
	}
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblNewLabel = new Label(container, SWT.WRAP);
		lblNewLabel.setLayoutData(new GridData( GridData.FILL_HORIZONTAL ));
		lblNewLabel.setText("For correct communication between Goko and Grbl v0.9, the following parameters should be updated in Grbl Configuration");
		
		List list = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		Label lblWouldYouLike = new Label(container, SWT.NONE);
		lblWouldYouLike.setText("Would you like to apply this settings now ? ");
		
		Label lblWarningNot = new Label(container, SWT.NONE);
		lblWarningNot.setText("Warning : not using the proposed settings may result in erratic behavior");
		
		list.setItems(fixProposal);
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

}
