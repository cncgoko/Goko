/**
 * 
 */
package org.goko.tools.serial.jssc.preferences.console;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilter;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilterType;

/**
 * @author Psyko
 * @date 20 déc. 2016
 */
public class JsscConsoleFilterEditDialog extends Dialog {
	private Text filterDescriptionText;
	private Text filterPatternText;
	private JsscConsoleFilter filter;
	private Label errorLabel;
	private ComboViewer filterTypeCombo;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public JsscConsoleFilterEditDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.BORDER | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
		parentShell.setText("Edit filter");
	}

	void setFilter(JsscConsoleFilter filter){
		this.filter = filter;
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.verticalSpacing = 3;
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		
		Label lblDescription = new Label(container, SWT.NONE);
		lblDescription.setText("Description");
		
		filterDescriptionText = new Text(container, SWT.BORDER);
		filterDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		
		Label lblPattern = new Label(container, SWT.NONE);
		lblPattern.setText("Pattern");
		
		filterPatternText = new Text(container, SWT.BORDER);
		filterPatternText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				boolean validState = false;
				if(StringUtils.isEmpty(filterPatternText.getText())){
					errorLabel.setText(StringUtils.EMPTY);
					validState = true;
				}else{
					try{
						Pattern.compile(filterPatternText.getText());
						errorLabel.setText(StringUtils.EMPTY);
						validState = true;
					}catch(PatternSyntaxException exception){
						errorLabel.setText("Pattern is not a valid expression");
						validState = false;						
					}	
				}
				if(getButton(IDialogConstants.OK_ID) != null){
					getButton(IDialogConstants.OK_ID).setEnabled(validState);
				}
			}
		});
		filterPatternText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		errorLabel = new Label(container, SWT.NONE);
		errorLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		errorLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		Label lblType = new Label(container, SWT.NONE);
		lblType.setText("Type");
		
		filterTypeCombo = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = filterTypeCombo.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		filterTypeCombo.setContentProvider(ArrayContentProvider.getInstance());
		filterTypeCombo.setLabelProvider(new LabelProvider(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {				
				return ((JsscConsoleFilterType)element).getLabel();
			}
		});
		
		filterTypeCombo.setInput(JsscConsoleFilterType.values());
		if(filter != null){
			filterDescriptionText.setText(filter.getDescription());
			filterPatternText.setText(filter.getRegex());
			filterTypeCombo.setSelection(new StructuredSelection(filter.getType()));
		}
		return container;
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
		return new Point(433, 271);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		if(filter != null){
			filter.setDescription(filterDescriptionText.getText());
			filter.setRegex(filterPatternText.getText());
			filter.setType((JsscConsoleFilterType) filterTypeCombo.getStructuredSelection().getFirstElement());
		}
		super.okPressed();
	}
}
