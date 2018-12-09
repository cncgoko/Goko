/**
 * 
 */
package org.goko.common.dialog;

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
import org.goko.core.common.exception.GkException;

/**
 * @author PsyKo
 *
 */
public class GkErrorDialog extends Dialog {
	private GkException exception;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	GkErrorDialog(Shell parentShell, GkException e) {
		super(parentShell);
		setShellStyle(SWT.TITLE);
		this.exception = e;
	}


	public static int openDialog(Shell parentShell, GkException e){
		return new GkErrorDialog(parentShell,e).open();
	}	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		gridLayout.numColumns = 2;
		
		Label lblQsdqsd = new Label(container, SWT.NONE);
		lblQsdqsd.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblQsdqsd.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/errorCross.png"));
		
		Label lblWarningMessage = new Label(container, SWT.WRAP);
		lblWarningMessage.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		lblWarningMessage.setText(exception.getLocalizedMessage());
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
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(850, 275);
	}

}
