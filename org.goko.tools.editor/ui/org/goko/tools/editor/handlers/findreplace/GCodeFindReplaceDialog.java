/**
 * 
 */
package org.goko.tools.editor.handlers.findreplace;

import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;

/**
 * @author Psyko
 * @date 30 mai 2016
 */
public class GCodeFindReplaceDialog extends Dialog {
	/** The string to find */
	private Text findString;
	/** The replacement string*/
	private Text replaceString;
	/** The find replace target */
	private Supplier<IFindReplaceTarget> findReplaceTargetSupplier;
	private Button btnRadioForward;
	private Button btnCaseSensitive;
	private Button btnWholeWord;	
	/** Position of the last match. <code>null</code> if none found */
	private Point lastFoundMatch;
	private Button findButton;
	private Button replaceFindButton;
	private Button replaceButton;
	private Button replaceAllButton;
	private Label lblResult;
	private Button btnWrapSearch;
	private Composite composite_3;
		
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public GCodeFindReplaceDialog(Shell parentShell, Supplier<IFindReplaceTarget> findReplaceTargetSupplier) {
		super(parentShell);
		setBlockOnOpen(false);
		setShellStyle(SWT.CLOSE);		
		this.findReplaceTargetSupplier = findReplaceTargetSupplier;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Find/replace");
		newShell.setImage(ResourceManager.getPluginImage("org.goko.tools.editor", "resources/icons/clipboard-search-result.png"));
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.marginHeight = 0;
		gl_container.horizontalSpacing = 0;
		container.setLayout(gl_container);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblFind = new Label(composite, SWT.NONE);
		lblFind.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFind.setText("Find: ");
		
		findString = new Text(composite, SWT.BORDER);		
		findString.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		if(findReplaceTargetSupplier != null && findReplaceTargetSupplier.get() != null){
			findString.setText(findReplaceTargetSupplier.get().getSelectionText());
		}
		Label lblReplace = new Label(composite, SWT.NONE);
		lblReplace.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReplace.setText("Replace:");
		
		replaceString = new Text(composite, SWT.BORDER);
		replaceString.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_2 = new Composite(container, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(2, true);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		
		Group grpOptions = new Group(composite_2, SWT.NONE);
		grpOptions.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		grpOptions.setLayout(new GridLayout(2, false));
		grpOptions.setText("Options");
		
		btnCaseSensitive = new Button(grpOptions, SWT.CHECK);
		btnCaseSensitive.setText("Case sensitive");
		
		btnWrapSearch = new Button(grpOptions, SWT.CHECK);
		btnWrapSearch.setText("Wrap search");
		
		btnWholeWord = new Button(grpOptions, SWT.CHECK);
		btnWholeWord.setText("Whole word");
		new Label(grpOptions, SWT.NONE);
		
		Group grpDirection = new Group(composite_2, SWT.NONE);
		grpDirection.setLayout(new GridLayout(1, false));
		grpDirection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpDirection.setText("Direction");
		
		btnRadioForward = new Button(grpDirection, SWT.RADIO);
		btnRadioForward.setSelection(true);
		btnRadioForward.setText("Forward");
		
		Button btnRadioBackward = new Button(grpDirection, SWT.RADIO);
		btnRadioBackward.setText("Backward");
		
		composite_3 = new Composite(container, SWT.NONE);
		composite_3.setLayout(new GridLayout(2, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lblResult = new Label(composite_3, SWT.NONE);
		lblResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblResult.setText(StringUtils.EMPTY);
		
		Composite composite_1 = new Composite(composite_3, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(2, false);
		gl_composite_1.numColumns = -2;		
		gl_composite_1.marginRight = 5;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
		findButton 			= createButton(composite_1, 600, "Find", true);
		replaceFindButton 	= createButton(composite_1, 601, "Replace/Find", false);
		replaceButton 		= createButton(composite_1, 602, "Replace", false);
		replaceAllButton 	= createButton(composite_1, 603, "Replace all", false);
		
		replaceButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {				
					performReplace();
			}
		});
		
		replaceAllButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {				
				performReplaceAll();
			}
		});
		
		findButton.addSelectionListener(new SelectionAdapter() {
			/** (inheritDoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {				
				performFind();
			}
		});
		
		container.addTraverseListener(new TraverseListener() {			
			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN) {					
					if (!Util.isMac()) {
						Control controlWithFocus= getShell().getDisplay().getFocusControl();
						if (controlWithFocus != null && (controlWithFocus.getStyle() & SWT.PUSH) == SWT.PUSH)
							return;
					}
					
					Event event= new Event();
					event.type= SWT.Selection;
					event.stateMask = e.stateMask;					
					findButton.notifyListeners(SWT.Selection, event);
					e.doit= false;
				}
			}
		});
		replaceFindButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {				
				performReplaceFind();
			}
		});

		findString.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setLastFoundMatch(null);
				updateButtonState();
			}
		});
		 
		updateButtonState();
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button closeButton = createButton(parent, IDialogConstants.CLOSE_ID, IDialogConstants.CLOSE_LABEL, false);
		closeButton.addSelectionListener(new SelectionAdapter() {
			/** (inheritDoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {				
				close();
			}
		});
	}

	protected void updateButtonState(){
		findButton.setEnabled(!StringUtils.isEmpty(getSearchString()));
		replaceAllButton.setEnabled(!StringUtils.isEmpty(getSearchString()));		
		// Manage button state
		replaceButton.setEnabled(isReplacePossible());				
		replaceFindButton.setEnabled(isReplacePossible());
	}
	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	private void findIndex(int startIndex, boolean wrapped){
		int foundPosition = getFindRplaceTarget().findAndSelect(startIndex, getSearchString(), isForwardSearch(), isCaseSensitiveSearch(), isWholeWordSearch());
		
		// Memorize the found match
		if(foundPosition != -1){	
			setLastFoundMatch(new Point(foundPosition, StringUtils.length(getSearchString())));
			lblResult.setText(StringUtils.EMPTY);
		}else{
			if(getLastFoundMatch() == null){ // It means the string was not found so far
				lblResult.setText("String not found");
			}else{
				lblResult.setText("End of document reached");
			}
			setLastFoundMatch(null);
			getShell().getDisplay().beep();
			if(isWrapSearch()){
				findIndex(-1, getLastFoundMatch() != null);
			}
		}
	}
	/**
	 * Perform the actual search for the given search string
	 */
	private void performFind(){
		int position = getFindRplaceTarget().getSelection().x;
		if(isForwardSearch()){
			position += getFindRplaceTarget().getSelection().y;
		}
		findIndex(position, false);
		updateButtonState();
	}	
	
	/**
	 * Perform the actual replace of the found String
	 */
	private void performReplace(){
		if(isReplacePossible()){
			getFindRplaceTarget().replaceSelection(getReplaceString());			
			setLastFoundMatch(null);
		}
		updateButtonState();
	}
	
	/**
	 * Perform the actual replace and find
	 */
	private void performReplaceFind(){
		if(isReplacePossible()){
			getFindRplaceTarget().replaceSelection(getReplaceString());			
			setLastFoundMatch(null);
			performFind();
		}
		updateButtonState();
	}
	
	/**
	 * Perform the actual replace all
	 */
	private void performReplaceAll(){
		while (isReplacePossible()) {
			performReplaceFind();
		}
	}
	
	private IFindReplaceTarget getFindRplaceTarget(){
		return findReplaceTargetSupplier.get();
	}

	private boolean isForwardSearch(){
		return btnRadioForward.getSelection();
	}
	
	private boolean isWholeWordSearch(){
		return btnWholeWord.getSelection();
	}
	
	private boolean isCaseSensitiveSearch(){
		return btnCaseSensitive.getSelection();
	}
	
	private boolean isWrapSearch(){
		return btnWrapSearch.getSelection();
	}
	
	
	private String getSearchString(){
		return findString.getText();
	}
	
	private String getReplaceString(){
		return replaceString.getText();
	}
	
	/**
	 * @return the replacePossible
	 */
	public boolean isReplacePossible() {
		return lastFoundMatch != null;
	}

	/**
	 * @return the lastFoundMatch
	 */
	public Point getLastFoundMatch() {
		return lastFoundMatch;
	}

	/**
	 * @param lastFoundMatch the lastFoundMatch to set
	 */
	public void setLastFoundMatch(Point lastFoundMatch) {
		this.lastFoundMatch = lastFoundMatch;
	}
}
