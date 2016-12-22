/**
 * 
 */
package org.goko.tools.serial.jssc.preferences.console;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.goko.common.preferences.GkPreferencesPage;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilter;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilterType;
import org.goko.tools.serial.jssc.preferences.connection.SerialConnectionPreference;

/**
 * @author Psyko
 * @date 18 déc. 2016
 */
public class JsscConsolePreferencesPage extends GkPreferencesPage {
	private Table filterTable;
	private List<JsscConsoleFilter> lstFilter;
	private TableViewer filterTableViewer;
	private JsscConsoleFilter lastSelection;
	
	public JsscConsolePreferencesPage() {
		setTitle("Serial console");
		lstFilter = new ArrayList<JsscConsoleFilter>();
	}

	
	@Override
	protected Control createContents(final Composite parentComposite) {
		Composite parent = new Composite(parentComposite, SWT.NONE);
		parent.setLayout(new GridLayout(2, false));
		
		Label lblApplyFiltersTo = new Label(parent, SWT.NONE);
		lblApplyFiltersTo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		lblApplyFiltersTo.setText("Filter list :");
		new Label(parent, SWT.NONE);
		
		filterTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);		
		filterTable = filterTableViewer.getTable();
		filterTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(filterTableViewer, SWT.NONE);
		TableColumn columnViewerDescription = tableViewerColumn_2.getColumn();
		columnViewerDescription.setWidth(100);
		columnViewerDescription.setText("Description");
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {				
				return ((JsscConsoleFilter)element).getDescription();
			}
		});
		
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(filterTableViewer, SWT.NONE);
		TableColumn columnViewerPattern = tableViewerColumn.getColumn();
		columnViewerPattern.setWidth(94);
		columnViewerPattern.setText("Pattern");
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {				
				return ((JsscConsoleFilter)element).getRegex();
			}
		});
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(filterTableViewer, SWT.NONE);
		TableColumn columnViewerEnable = tableViewerColumn_1.getColumn();
		columnViewerEnable.setWidth(60);
		columnViewerEnable.setText("Enabled");
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {	
				if(((JsscConsoleFilter)element).isEnabled()){
					return "Enabled";
				}
				return "Disabled";
			}
		});
		// set the content provider
		filterTableViewer.setContentProvider(ArrayContentProvider.getInstance());

		// provide the input to the viewer
		filterTableViewer.setInput(lstFilter);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
		
		Button btnAdd = new Button(composite, SWT.NONE);
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				JsscConsoleFilter filter = new JsscConsoleFilter(true, StringUtils.EMPTY, StringUtils.EMPTY, JsscConsoleFilterType.INPUT);
				JsscConsoleFilterEditDialog dialog = new JsscConsoleFilterEditDialog(parentComposite.getShell());
				dialog.setFilter(filter);
				int result = dialog.open();
				if(result == Window.OK){
					lstFilter.add(filter);
					lastSelection = filter;
					filterTableViewer.refresh();
					updateSelection();				
					
				}
			}
		});
		GridData gd_btnAdd = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 80;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText("Add...");
		
		final Button btnEdit = new Button(composite, SWT.NONE);
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				JsscConsoleFilter filter = (JsscConsoleFilter) filterTableViewer.getStructuredSelection().getFirstElement();
				JsscConsoleFilterEditDialog dialog = new JsscConsoleFilterEditDialog(parentComposite.getShell());
				dialog.setFilter(filter);
				int result = dialog.open();
				if(result == Window.OK){
					lastSelection = filter;
					filterTableViewer.refresh();
					updateSelection();
				}
			}
		});
		btnEdit.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEdit.setText("Edit...");
		
		final Button btnRemove = new Button(composite, SWT.NONE);
		btnRemove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(!filterTableViewer.getStructuredSelection().isEmpty()){
					JsscConsoleFilter filter = (JsscConsoleFilter) filterTableViewer.getStructuredSelection().getFirstElement();				
					lstFilter.remove(filter);	
					filterTableViewer.refresh();
					updateSelection();
				}
			}
		});
		btnRemove.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnRemove.setText("Remove");
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		final Button btnEnable = new Button(composite, SWT.NONE);
		btnEnable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(!filterTableViewer.getStructuredSelection().isEmpty()){
					JsscConsoleFilter filter = (JsscConsoleFilter)filterTableViewer.getStructuredSelection().getFirstElement();
					filter.setEnabled(true);
					filterTableViewer.refresh();
					updateSelection();
				}
			}
		});
		btnEnable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnEnable.setText("Enable");
		
		final Button btnDisable = new Button(composite, SWT.NONE);
		btnDisable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				if(!filterTableViewer.getStructuredSelection().isEmpty()){
					JsscConsoleFilter filter = (JsscConsoleFilter)filterTableViewer.getStructuredSelection().getFirstElement();
					filter.setEnabled(false);
					filterTableViewer.refresh();
					updateSelection();
				}
			}
		});
		btnDisable.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnDisable.setText("Disable");

		filterTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				if(filterTableViewer.getStructuredSelection().isEmpty()){
					btnEdit.setEnabled(false);
					btnRemove.setEnabled(false);
					btnEnable.setEnabled(false);
					btnDisable.setEnabled(false);
					lastSelection = null;
				}else{
					btnEdit.setEnabled(true);
					btnRemove.setEnabled(true);
					lastSelection = (JsscConsoleFilter)filterTableViewer.getStructuredSelection().getFirstElement();
					btnEnable.setEnabled(!lastSelection.isEnabled());
					btnDisable.setEnabled(lastSelection.isEnabled());
				}
			}
		});
		
		lstFilter.clear();
		lstFilter.addAll(SerialConnectionPreference.getInstance().getFilters());
		filterTableViewer.refresh();
		updateSelection();
		return parent;
	}	
	
	void updateSelection(){
		if(CollectionUtils.isNotEmpty(lstFilter) ){
			if(lastSelection != null && lstFilter.contains(lastSelection)){
				filterTableViewer.setSelection(new StructuredSelection(lastSelection));
			}else{
				filterTableViewer.setSelection(new StructuredSelection(lstFilter.get(0)));
			}
		}else{
			filterTableViewer.setSelection(new StructuredSelection());
		}
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		SerialConnectionPreference.getInstance().setFilters(lstFilter);
		return super.performOk();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
