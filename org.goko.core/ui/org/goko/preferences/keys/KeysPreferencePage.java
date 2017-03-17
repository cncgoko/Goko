/**
 * 
 */
package org.goko.preferences.keys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeySequenceText;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheByCode;
import org.goko.core.log.GkLog;

/**
 * 
 * @author Psyko
 * @date 4 mars 2017
 */
public class KeysPreferencePage extends GkPreferencesPage {
	private static final GkLog LOG = GkLog.getLogger(KeysPreferencePage.class);
	private static final Object SYSTEM_COMMAND_TAG = "system";
	@Inject
	private MApplication app;	
	private MBindingTable rootTable;
	private Table table;
	private KeySequenceText keyInput;
	private Label commandNameLabel;
	private Label commandDescriptionLabel;
	private KeyBinding selectedKeybinding;
	private ComboViewer bindingTableViewer;
	private String e4KeySequence;
	private TableViewer commandTableViewer;
	private Button btnUnbind;
	private Table conflictTable;
	private TableViewer conflictTableViewer;
	private Map<String, KeyBinding> mapOverrideBindings;
	private KeySequenceText text;
	
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
		List<MBindingContext> lstCtx = app.getBindingContexts();
		for (MBindingContext mBindingContext : lstCtx) {
			System.err.println(mBindingContext.getName());
		}
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
		
		TableViewerColumn commandNameViewerColumn = new TableViewerColumn(commandTableViewer, SWT.NONE);
		TableColumn commandNameColumn = commandNameViewerColumn.getColumn();
		commandNameColumn.setWidth(127);
		commandNameColumn.setText("Command");
		commandNameViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        		KeyBinding k = (KeyBinding) element;
	                return k.getCommand().getCommandName();
	        }
		});	
		
		TableViewerColumn commandKeyViewerColumn = new TableViewerColumn(commandTableViewer, SWT.NONE);
		TableColumn commandKeyColumn = commandKeyViewerColumn.getColumn();
		commandKeyColumn.setWidth(100);
		commandKeyColumn.setText("Key");
		commandKeyViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        		KeyBinding k = (KeyBinding) element;
	        		if(k != null &&  k.getKeyBinding() != null){	        			
	        			return convertKeySequence(k.getKeyBinding().getKeySequence());
	        		}
	                return StringUtils.EMPTY;
	        }


		});	
		
		TableViewerColumn categoryViewerColumn = new TableViewerColumn(commandTableViewer, SWT.NONE);
		TableColumn categoryColumn = categoryViewerColumn.getColumn();
		categoryColumn.setWidth(100);
		categoryColumn.setText("Category");
		categoryViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        		KeyBinding k = (KeyBinding) element;	
	        		if(k.getCommand().getCategory() != null){
	        			return k.getCommand().getCategory().getName();
	        		}
	        		return StringUtils.EMPTY;
	        }
		});	
		
		commandTableViewer.setComparator(new KeyBindingViewerComparator());
		
		TableViewerColumn whenViewerColumn = new TableViewerColumn(commandTableViewer, SWT.NONE);
		TableColumn whenColumn = whenViewerColumn.getColumn();
		whenColumn.setWidth(100);
		whenColumn.setText("When");
		
		whenViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        		KeyBinding k = (KeyBinding) element;	
	        		if(k.getBindingTable() != null){
	        			return k.getBindingTable().getBindingContext().getName();
	        		}
	        		return StringUtils.EMPTY;
	        }
		});	
		
		commandTableViewer.setContentProvider(new ArrayContentProvider());
		try {
			commandTableViewer.setInput(getTableContent());
		} catch (GkException e) {
			LOG.error(e);
		}
		
		Composite composite_2 = new Composite(main, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginHeight = 0;
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		
		btnUnbind = new Button(composite_2, SWT.NONE);
		btnUnbind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				unbindCommand();
			}
		});
		btnUnbind.setText("Unbind ");
		List<MBindingTable> bindings = getBindingTable();
		
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				selectKeyBinding((KeyBinding) commandTableViewer.getStructuredSelection().getFirstElement());
			}
		});
		commandTableViewer.setSelection( new StructuredSelection(bindings.get(0)));
		
		Composite composite_3 = new Composite(main, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.marginHeight = 0;
		gl_composite_3.marginWidth = 0;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		Composite composite = new Composite(composite_3, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginHeight = 0;
		gl_composite.marginWidth = 0;
		composite.setLayout(gl_composite);
		
		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("Name:");
		
		commandNameLabel = new Label(composite, SWT.NONE);
		commandNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		lblNewLabel_1.setText("Description:");
		
		commandDescriptionLabel = new Label(composite, SWT.BORDER | SWT.WRAP);
		commandDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setText("Key:");
		
		keyInput = new KeySequenceText(new Text(composite, SWT.BORDER));
		keyInput.addPropertyChangeListener(new IPropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				updateBinding();
			}
		});
//		keyInput.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				e4KeySequence = StringUtils.EMPTY;
//				if(rootTable != null && bindingTableViewer.getSelection().isEmpty()){
//					bindingTableViewer.setSelection(new StructuredSelection(rootTable));					
//				}				
//				
//				if((e.stateMask & SWT.CTRL) == SWT.CTRL){
//					e4KeySequence +="M1+";
//				}
//				if((e.stateMask & SWT.SHIFT) == SWT.SHIFT){
//					e4KeySequence +="M2+";
//				}
//				if((e.stateMask & SWT.ALT) == SWT.ALT){
//					e4KeySequence +="M3+";
//				}
//				if((e.stateMask & SWT.ALT) == SWT.COMMAND){
//					e4KeySequence +="M4+";
//				}
//				e4KeySequence += getKeyBindingString(e);
//				
//				keyInput.setText(convertKeySequence(e4KeySequence));
//				updateBinding();
//				bindingTableViewer.refresh();
//				e.doit = false;
//			}
//		});
//		keyInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblWhen = new Label(composite, SWT.NONE);
		lblWhen.setText("When :");
		
		bindingTableViewer = new ComboViewer(composite, SWT.READ_ONLY);
		Combo bindingTableCombo = bindingTableViewer.getCombo();
		bindingTableCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(selectedKeybinding != null){
					updateBinding();
				}
			}
		});
		bindingTableCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.verticalSpacing = 0;
		gl_composite_4.marginHeight = 0;
		composite_4.setLayout(gl_composite_4);
		
		Label lblCoinflicts = new Label(composite_4, SWT.NONE);
		lblCoinflicts.setText("Conflicts:");
		
		conflictTableViewer = new TableViewer(composite_4, SWT.BORDER | SWT.FULL_SELECTION);
		conflictTable = conflictTableViewer.getTable();
		conflictTable.setHeaderVisible(true);
		GridData gd_table_1 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_table_1.heightHint = 70;
		conflictTable.setLayoutData(gd_table_1);
		conflictTableViewer.setContentProvider(new ArrayContentProvider());
		TableViewerColumn conflictCommandViewerColumn = new TableViewerColumn(conflictTableViewer, SWT.NONE);
		TableColumn conflictCommandColumn = conflictCommandViewerColumn.getColumn();
		conflictCommandColumn.setWidth(100);
		conflictCommandColumn.setText("Command");
		
		conflictCommandViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        	return ((KeyBinding)element).getCommand().getCommandName();
	        }
		});
		
		TableViewerColumn conflictWhenViewerColumn = new TableViewerColumn(conflictTableViewer, SWT.NONE);
		TableColumn conflictWhenColumn = conflictWhenViewerColumn.getColumn();
		conflictWhenColumn.setWidth(115);
		conflictWhenColumn.setText("When");
		
		conflictWhenViewerColumn.setLabelProvider(new ColumnLabelProvider() {
	        @Override
	        public String getText(Object element) {
	        	return ((KeyBinding)element).getBindingTable().getBindingContext().getName();
	        }
		});
		
		bindingTableViewer.setContentProvider( new ArrayContentProvider() );
		bindingTableViewer.setLabelProvider(new LabelProvider(){
			/** (inheritDoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {				
				return ((MBindingTable)element).getBindingContext().getName();
			}
		});
		bindingTableViewer.setInput(bindings);
		return main;
	}
	
	private void selectKeyBinding(KeyBinding target){
		selectedKeybinding = target;
		commandNameLabel.setText(selectedKeybinding.getCommand().getCommandName());
		 
		if(selectedKeybinding.getCommand().getDescription() != null){
			commandDescriptionLabel.setText(selectedKeybinding.getCommand().getDescription());
		}else{
			commandDescriptionLabel.setText(StringUtils.EMPTY);
		}
		if(selectedKeybinding.getKeyBinding() != null){
			e4KeySequence = selectedKeybinding.getKeyBinding().getKeySequence();
			try {
				keyInput.setKeySequence(KeySequence.getInstance(e4KeySequence));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
			bindingTableViewer.setSelection(new StructuredSelection(selectedKeybinding.getBindingTable()));
			btnUnbind.setEnabled(true);
			conflictTableViewer.setInput(computeConflicts(selectedKeybinding));
		}else{
			keyInput.clear();
			bindingTableViewer.setSelection(new StructuredSelection());
			btnUnbind.setEnabled(false);
			conflictTableViewer.setInput(new ArrayList<MKeyBinding>());
		}			
	}
	
	private List<KeyBinding> computeConflicts(KeyBinding keybinding){
		List<KeyBinding> conflicts = new ArrayList<KeyBinding>();
		if(keybinding.getBindingTable() != null && keybinding.getKeyBinding() != null){
			String newKeySequence = keybinding.getKeyBinding().getKeySequence();
			List<MKeyBinding> bindings = keybinding.getBindingTable().getBindings();
			for (MKeyBinding mKeyBinding : bindings) {
				if(StringUtils.equals(newKeySequence, mKeyBinding.getKeySequence())){
					conflicts.add(getKeyBinding(mKeyBinding, keybinding.getBindingTable()));
				}
			}
		}
		return conflicts;
	}
	
	private KeyBinding getKeyBinding(MKeyBinding mKeyBinding, MBindingTable mBindingTable){
		KeyBinding result = new KeyBinding(mKeyBinding.getCommand(), mKeyBinding);
		result.setKeyBinding(mKeyBinding);
		result.setBindingTable(mBindingTable);
		return result;
	}
	private void unbindCommand(){
		if(selectedKeybinding != null){
			selectedKeybinding.getBindingTable().getBindings().remove(selectedKeybinding.getKeyBinding());
			selectedKeybinding.setBindingTable(null);
			selectedKeybinding.setKeyBinding(null);
			commandTableViewer.update(selectedKeybinding, null);
			selectKeyBinding(selectedKeybinding);
		}
	}
	private void updateBinding(){
		if(StringUtils.isNotBlank(e4KeySequence)){
			MBindingTable targetTable = (MBindingTable) bindingTableViewer.getStructuredSelection().getFirstElement();
			if(selectedKeybinding.getKeyBinding() == null){
				MKeyBinding binding = MCommandsFactory.INSTANCE.createKeyBinding();				
				binding.setCommand(selectedKeybinding.getCommand());					
				binding.setElementId("kb." + selectedKeybinding.getCommand().getElementId());
				binding.setContributorURI(selectedKeybinding.getCommand().getContributorURI());
				selectedKeybinding.setKeyBinding(binding);
				selectedKeybinding.getKeyBinding().setKeySequence(e4KeySequence);					
				selectedKeybinding.setBindingTable(targetTable);
				selectedKeybinding.getBindingTable().getBindings().add(binding);					
			}else{
				selectedKeybinding.getKeyBinding().setKeySequence(e4KeySequence);
			}
			
			if(table != selectedKeybinding.getBindingTable()){ 
				// Remove from current table
				selectedKeybinding.getBindingTable().getBindings().remove(selectedKeybinding.getKeyBinding());
				selectedKeybinding.setKeyBinding(duplicateBinding(selectedKeybinding.getKeyBinding(), targetTable));			
			}
			selectedKeybinding.setBindingTable(targetTable);
			commandTableViewer.update(selectedKeybinding, null);
		}
	}
	
	//voir la gestion des conflits
	
	private MKeyBinding duplicateBinding(MKeyBinding source, MBindingTable targetTable){
		MKeyBinding binding = MCommandsFactory.INSTANCE.createKeyBinding();
		binding.setCommand(source.getCommand());					
		binding.setElementId(source.getElementId());
		binding.setContributorURI(source.getContributorURI());
		binding.setKeySequence(source.getKeySequence());
		targetTable.getBindings().add(binding);
		return binding;
	}
	
	private String convertKeySequence(String keySequence) {
		String result = StringUtils.replace(keySequence, "M1", "Ctrl");
		result = StringUtils.replace(result, "M2", "Shift");
		result = StringUtils.replace(result, "M3", "Alt");
		return result;
	}
	
	private List<MBindingTable> getBindingTable(){
		List<MBindingTable> lst = app.getBindingTables();
		if(CollectionUtils.isNotEmpty(lst)){
			rootTable = lst.get(0);
		}
		return lst;
	}

	
	private List<KeyBinding> getTableContent() throws GkException{
		List<MBindingTable> table = app.getBindingTables();
		CacheByCode<KeyBinding> cache = new CacheByCode<>();
				
		for (MCommand command : app.getCommands()) {
			if(!isSystemCommand(command)){
				cache.add(new KeyBinding(command, null));
			}
		}
		for (MBindingTable mBindingTable : table) {
			List<MKeyBinding> bindings = mBindingTable.getBindings();
			for (MKeyBinding mKeyBinding : bindings) {
				if(mKeyBinding.getCommand() != null){
					MCommand command = mKeyBinding.getCommand();
					if(isSystemCommand(command)){
						// We skip command tagged as System						
						continue;
					}
					
					KeyBinding keyBinding = cache.find(mKeyBinding.getCommand().getElementId());
					if(keyBinding != null){
						keyBinding.setKeyBinding(mKeyBinding);
						keyBinding.setBindingTable(mBindingTable);
					}
				}
			}
		}
		
		return cache.get();
	}
	
	protected boolean isSystemCommand(MCommand command){
		if(CollectionUtils.isNotEmpty(command.getTags()) && command.getTags().contains(SYSTEM_COMMAND_TAG)){
			return true;
		}
		return false;
	}
	
	protected String getKeyBindingString(KeyEvent e){
		String result = StringUtils.upperCase(String.valueOf((char)e.keyCode));
		
		switch(e.keyCode){
		case SWT.F1: result = "F1";
		break;
		case SWT.F2: result = "F2";
		break;
		case SWT.F3: result = "F3";
		break;
		case SWT.F4: result = "F4";
		break;
		case SWT.F5: result = "F5";
		break;
		case SWT.F6: result = "F6";
		break;
		case SWT.F7: result = "F7";
		break;
		case SWT.F8: result = "F8";
		break;
		case SWT.F9: result = "F9";
		break;
		case SWT.F10: result = "F10";
		break;
		case SWT.F11: result = "F11";
		break;
		case SWT.F12: result = "F12";
		break;
		
		}
		return result;
	}
}
