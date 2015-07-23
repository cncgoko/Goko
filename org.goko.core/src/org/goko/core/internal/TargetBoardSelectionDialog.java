/**
 * 
 */
package org.goko.core.internal;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.goko.common.preferences.fieldeditor.ComboFieldEditor;
import org.goko.core.feature.TargetBoard;

/**
 * @author PsyKo
 *
 */
public class TargetBoardSelectionDialog extends Dialog {
	private ComboFieldEditor targetBoardCombo;
	private List<TargetBoard> lstTargetBoard;
	private String targetBoard;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TargetBoardSelectionDialog(Shell parentShell) {
		super(parentShell);		
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.verticalSpacing = 0;
		gl_container.marginWidth = 0;
		gl_container.marginHeight = 0;
		gl_container.horizontalSpacing = 0;
		container.setLayout(gl_container);
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setImage(ResourceManager.getPluginImage("org.goko.core", "icons/boardDialogImage.png"));
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		
		Composite composite_1 = new Composite(container, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.marginHeight = 8;
		gl_composite_1.marginWidth = 8;
		composite_1.setLayout(gl_composite_1);
		
		Label lblSelectYourOperating = new Label(composite_1, SWT.NONE);
		lblSelectYourOperating.setText("Select your machine controller :");
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		gd_composite.heightHint = 60;
		composite.setLayoutData(gd_composite);
		
		targetBoardCombo = new ComboFieldEditor(composite, SWT.READ_ONLY);
		targetBoardCombo.setLabel("Controller");
		targetBoardCombo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		initComboContent();
		return container;
	}

	private void initComboContent(){
		String[][] entry = new String[lstTargetBoard.size()][];
		int i = 0;
		for (TargetBoard board : lstTargetBoard) {
			entry[i] = new String[]{board.getLabel(), board.getId()};
			i = i + 1;
		}
		targetBoardCombo.setEntry(entry);		
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if(StringUtils.isNotBlank(targetBoardCombo.getValue())){
			targetBoard = targetBoardCombo.getValue();
			super.okPressed();
		}
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {
		targetBoard = StringUtils.EMPTY;
		super.cancelPressed();
	}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(356, 287);
	}

	/**
	 * @return the lstTargetBoard
	 */
	public List<TargetBoard> getLstTargetBoard() {
		return lstTargetBoard;
	}

	/**
	 * @param lstTargetBoard the lstTargetBoard to set
	 */
	public void setLstTargetBoard(List<TargetBoard> lstTargetBoard) {
		this.lstTargetBoard = lstTargetBoard;
	}

	/**
	 * @return the targetBoard
	 */
	public String getTargetBoard() {
		return targetBoard;
	}

	/**
	 * @param targetBoard the targetBoard to set
	 */
	public void setTargetBoard(String targetBoard) {
		this.targetBoard = targetBoard;
	}

}
