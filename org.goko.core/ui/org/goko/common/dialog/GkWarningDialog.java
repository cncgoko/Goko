/**
 * 
 */
package org.goko.common.dialog;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkFunctionalException;

/**
 * @author PsyKo
 *
 */
public class GkWarningDialog extends Dialog {
	private String message;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	GkWarningDialog(Shell parentShell, String message) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
		this.message = message;
	}


	static int openDialog(Shell parentShell, GkFunctionalException e){
		return new GkWarningDialog(parentShell,e.getLocalizedMessage()).open();
	}
	
	static int openDialog(Shell parentShell, IStatus status){
		return new GkWarningDialog(parentShell,status.getMessage()).open();
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		
		Label label = new Label(container, SWT.NONE);
		label.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/Warning.png"));
		
		Label lblWarningMessage = new Label(container, SWT.WRAP);
		lblWarningMessage.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		lblWarningMessage.setText(message);
		
		label.pack();
		lblWarningMessage.pack();
		container.pack();
		
		getShell().setText("Warning");
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	//	createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(858, 263);
	}

}
