/**
 * 
 */
package org.goko.preferences.keys;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.bindings.internal.BindingTableManager;
import org.eclipse.e4.ui.bindings.keys.KeyBindingDispatcher;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeySequenceText;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.GkPreferencesPage;
import org.goko.core.log.GkLog;
import org.goko.preferences.keys.model.BindingElement;
import org.goko.preferences.keys.model.BindingModel;
import org.goko.preferences.keys.model.CommonModel;
import org.goko.preferences.keys.model.ConflictModel;
import org.goko.preferences.keys.model.ContextElement;
import org.goko.preferences.keys.model.KeyController;
import org.goko.preferences.keys.model.ModelElement;

/**
 * 
 * @author Psyko
 * @date 4 mars 2017
 */
public class KeysPreferencePage extends GkPreferencesPage {
	private static final GkLog LOG = GkLog.getLogger(KeysPreferencePage.class);
	@Inject
	private MApplication app;
	@Inject
	private KeyBindingDispatcher keyBindingDispatcher;
	@Inject 
	BindingTableManager btm;
	
	private Table table;
	private TableViewer commandTableViewer;
	private KeySequenceText keySequenceText;
	private Table conflictTable;
	private Collection<String> activeContexts;
	private KeyController keyController;
	
	public KeysPreferencePage() {
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {		
		keyController = new KeyController();
		keyController.init(app);
		List<MBindingContext> lstCtx = app.getBindingContexts();
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
	
		Composite composite_1 = new Composite(main, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_1.heightHint = 235;
		composite_1.setLayoutData(gd_composite_1);
		
		commandTableViewer = new TableViewer(composite_1, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table = commandTableViewer.getTable();

		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn commandNameColumn = new TableColumn(table, SWT.NONE);
		commandNameColumn.setWidth(127);
		commandNameColumn.setText("Command");
		
		TableColumn commandKeyColumn = new TableColumn(table, SWT.NONE);
		commandKeyColumn.setWidth(100);
		commandKeyColumn.setText("Key");
		
		TableColumn whenColumn = new TableColumn(table, SWT.NONE);
		whenColumn.setWidth(100);
		whenColumn.setText("When");
		
		TableColumn categoryColumn = new TableColumn(table, SWT.NONE);
		categoryColumn.setWidth(100);
		categoryColumn.setText("Category");
				
		commandTableViewer.setContentProvider(new BindingModelContentProvider());
		commandTableViewer.setInput(keyController.getBindingModel());
		commandTableViewer.setLabelProvider(new BindingElementLabelProvider());
		commandTableViewer.setComparator(new BindingElementViewerComparator());
		
		Composite composite = new Composite(main, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		Button btnUnbindCommand = new Button(composite, SWT.NONE);
		btnUnbindCommand.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				BindingElement be = keyController.getBindingModel().getSelectedElement();
				be.setTrigger(null);
				be.setContext(null);
			}
		});
		
		btnUnbindCommand.setText("Unbind command");
		
		Button btnRestoreCommand = new Button(composite, SWT.NONE);
		btnRestoreCommand.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				
			}
		});
		btnRestoreCommand.setText("Restore command");
		
		Composite composite_2 = new Composite(main, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(3, false);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		composite_3.setLayout(gl_composite_3);
		GridData gd_composite_3 = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_composite_3.widthHint = 60;
		composite_3.setLayoutData(gd_composite_3);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Name:");
		
		final Label commandNameValueLabel = new Label(composite_3, SWT.NONE);
		commandNameValueLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(composite_3, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblNewLabel_1.setText("Description:");
		
		Composite composite_5 = new Composite(composite_3, SWT.BORDER);
		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_composite_5 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_composite_5.heightHint = 40;
		composite_5.setLayoutData(gd_composite_5);
		
		final Label commandDescriptionValueLabel = new Label(composite_5, SWT.WRAP);
		
		Label lblNewLabel_2 = new Label(composite_3, SWT.NONE);
		lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_2.setText("Binding:");
		
		final Text fBindingText = new Text(composite_3, SWT.BORDER);
		fBindingText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		keySequenceText = new KeySequenceText(fBindingText);
		keySequenceText.addPropertyChangeListener(new IPropertyChangeListener() {
			public final void propertyChange(final PropertyChangeEvent event) {
				if (!event.getOldValue().equals(event.getNewValue())) {
					final KeySequence keySequence = keySequenceText.getKeySequence();
					if (!keySequence.isComplete()) {
						return;
					}

					BindingElement activeBinding = (BindingElement) keyController.getBindingModel().getSelectedElement();
					if (activeBinding != null) {
						activeBinding.setTrigger(keySequence);
					}
					fBindingText.setSelection(fBindingText.getTextLimit());
				}
			}
		});
		
		fBindingText.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				keyBindingDispatcher.getKeyDownFilter().setEnabled(true);
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				keyBindingDispatcher.getKeyDownFilter().setEnabled(false);
			}
		});
		
		Label lblNewLabel_3 = new Label(composite_3, SWT.NONE);
		lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_3.setText("When:");
		
		final ComboViewer whenComboViewer = new ComboViewer(composite_3, SWT.READ_ONLY);
		Combo combo = whenComboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		whenComboViewer.setContentProvider(new ModelContentProvider());
		whenComboViewer.setLabelProvider(new ModelElementLabelProvider());
		whenComboViewer.setInput(keyController.getContextModel());
		
		IPropertyChangeListener whenListener = new IPropertyChangeListener() {
			// Sets the combo selection when a new keybinding is selected?
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getSource() == keyController.getContextModel()
						&& CommonModel.PROP_SELECTED_ELEMENT.equals(event.getProperty())) {
					Object newVal = event.getNewValue();
					StructuredSelection structuredSelection = null; 
					if(newVal != null){
						structuredSelection = new StructuredSelection(newVal);
					}
					whenComboViewer.setSelection(structuredSelection, true);
				}
			}
		};
		whenComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if(keyController.getBindingModel().getSelectedElement() != null){
					ContextElement selectedContext = (ContextElement) ((StructuredSelection)event.getSelection()).getFirstElement();
					keyController.getBindingModel().getSelectedElement().setContext(selectedContext);
				}
			}
		});
		keyController.addPropertyChangeListener(whenListener);
		
		Label label = new Label(composite_2, SWT.NONE);
		label.setText("  ");
		
		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.marginHeight = 0;
		gl_composite_4.marginWidth = 0;
		composite_4.setLayout(gl_composite_4);
		GridData gd_composite_4 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_composite_4.widthHint = 40;
		composite_4.setLayoutData(gd_composite_4);
		
		Label lblConflicts = new Label(composite_4, SWT.NONE);
		lblConflicts.setText("Conflicts:");
		
		final TableViewer conflictTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		conflictTable = conflictTableViewer.getTable();
		conflictTable.setHeaderVisible(true);
		conflictTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		conflictTableViewer.setContentProvider(new ConflictModelContentProvider());
		conflictTableViewer.setInput(keyController.getConflictModel());
		conflictTableViewer.setLabelProvider(new BindingElementLabelProvider() {
			public String getColumnText(Object o, int index) {
				BindingElement element = (BindingElement) o;
				if (index == 0) {
					return element.getName();
				}
				return element.getContext().getName();
			}
		});
		
		TableColumn conflictNameColumn = new TableColumn(conflictTable, SWT.NONE);
		conflictNameColumn.setWidth(100);
		conflictNameColumn.setText("Name");
		
		TableColumn conflictWhenColumn = new TableColumn(conflictTable, SWT.NONE);
		conflictWhenColumn.setWidth(100);
		conflictWhenColumn.setText("When");
		
		commandTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			// When the viewer changes selection, update the model's current selection
			public void selectionChanged(SelectionChangedEvent event) {
				BindingElement binding = (BindingElement) ((IStructuredSelection) event.getSelection()).getFirstElement();
				keyController.getBindingModel().setSelectedElement(binding);
			}
		});
		
		IPropertyChangeListener dataUpdateListener = new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				BindingElement bindingElement = null;
				boolean weCare = false;
				if (event.getSource() == keyController.getBindingModel()
						&& CommonModel.PROP_SELECTED_ELEMENT.equals(event.getProperty())) {
					bindingElement = (BindingElement) event.getNewValue();
					weCare = true;
				} else if (event.getSource() == keyController.getBindingModel().getSelectedElement()
						&& (ModelElement.PROP_MODEL_OBJECT.equals(event.getProperty())
							|| BindingElement.PROP_TRIGGER.equals(event.getProperty())
							|| BindingElement.PROP_CONTEXT.equals(event.getProperty()))) {
					bindingElement = (BindingElement) event.getSource();
					weCare = true;					
				}
				if (bindingElement == null && weCare) {
					commandNameValueLabel.setText(StringUtils.EMPTY); 
					commandDescriptionValueLabel.setText(StringUtils.EMPTY);
					fBindingText.setText(StringUtils.EMPTY); 
					whenComboViewer.setSelection(new StructuredSelection());
				} else if (bindingElement != null) {
					commandNameValueLabel.setText(bindingElement.getName());
					String desc = bindingElement.getDescription();
					commandDescriptionValueLabel.setText(desc == null ? StringUtils.EMPTY : desc); 
					KeySequence trigger = (KeySequence) bindingElement.getTrigger();
					keySequenceText.setKeySequence(trigger);
					whenComboViewer.setSelection(bindingElement.getContext() == null ? null : new StructuredSelection(bindingElement.getContext()));
				}
			}
		};
		keyController.addPropertyChangeListener(dataUpdateListener);
		
		IPropertyChangeListener tableUpdateListener = new IPropertyChangeListener() {

			// When the model changes a property, update the viewer
			public void propertyChange(PropertyChangeEvent event) {
				
				if (event.getSource() == keyController.getBindingModel() && CommonModel.PROP_SELECTED_ELEMENT.equals(event.getProperty())) {
					Object newVal = event.getNewValue();
					StructuredSelection structuredSelection = newVal == null ? null : new StructuredSelection(newVal);
					commandTableViewer.setSelection(structuredSelection, true);					
				} else if (event.getSource() instanceof BindingElement){
					commandTableViewer.update(event.getSource(), null);
				} else if (BindingElement.PROP_CONFLICT.equals(event.getProperty())) {
					commandTableViewer.update(keyController.getConflictModel().getSelectedElement().getConflicts().toArray(), null);
					updateCommandTableViewerConflicts();
					updateButtons();
				} else if (BindingModel.PROP_BINDINGS.equals(event.getProperty())) {
					commandTableViewer.refresh();
				}
			}
		};				
		keyController.addPropertyChangeListener(tableUpdateListener);
		
		IPropertyChangeListener conflictsListener = new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				// Update the list of element in error in the main table
				if (event.getSource() == keyController.getConflictModel() && CommonModel.PROP_SELECTED_ELEMENT.equals(event.getProperty())) {
					if (keyController.getConflictModel().getSelectedElement() != null) {						
						Object newVal = event.getNewValue();						
						StructuredSelection structuredSelection = newVal == null ? null : new StructuredSelection(newVal);
						conflictTableViewer.setSelection(structuredSelection, true);
						commandTableViewer.update(keyController.getConflictModel().getSelectedElement().getConflicts().toArray(), null);
					}
					conflictTableViewer.setInput(keyController.getConflictModel());
					conflictTableViewer.refresh(true);
				} else if (ConflictModel.PROP_CONFLICTS.equals(event.getProperty())) {
					conflictTableViewer.setInput(keyController.getConflictModel());
					updateCommandTableViewerConflicts();
					updateButtons();
				} else if (ConflictModel.PROP_CONFLICTS_ADD.equals(event.getProperty())) {
					conflictTableViewer.add(event.getNewValue());
					updateCommandTableViewerConflicts();
					updateButtons();
				} else if (ConflictModel.PROP_CONFLICTS_REMOVE.equals(event.getProperty())) {
					conflictTableViewer.remove(event.getNewValue());
					updateCommandTableViewerConflicts();
					updateButtons();
				}
			}
			
		};
		keyController.addPropertyChangeListener(conflictsListener);
		commandNameColumn.pack();
		categoryColumn.pack();
		whenColumn.pack();
		commandKeyColumn.pack();
		return main;
	}
	
	private void updateCommandTableViewerConflicts(){
		if(keyController.getConflictModel().getSelectedElement() != null){
			commandTableViewer.update(keyController.getConflictModel().getSelectedElement().getConflicts().toArray(), null);
		}
	}
	
	private void updateButtons(){
		if(keyController.getConflictModel().getConflictsCount() > 0){
			setValid(false);	
			
		}else{
			setValid(true);
		}
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		if(keyController != null){
			keyController.applyChanges(app, btm);
		}
		return super.performOk();
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		keyController.setNotifying(false);				
		String selectedBinding = null;
		if(keyController.getBindingModel().getSelectedElement() != null){
			selectedBinding = keyController.getBindingModel().getSelectedElement().getId();
		}
		keyController.restore(app);				
		keyController.setNotifying(true);				
		commandTableViewer.setInput(keyController.getBindingModel());
		
		// Force reselection
		if(!CollectionUtils.isEmpty(keyController.getBindingModel().getBindings()) && selectedBinding != null){
			keyController.getBindingModel().setSelectedElement(keyController.getBindingModel().getBinding(selectedBinding));					
		}
		super.performDefaults();
	}
}
