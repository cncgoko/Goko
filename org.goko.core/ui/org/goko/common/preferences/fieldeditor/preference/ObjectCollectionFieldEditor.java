package org.goko.common.preferences.fieldeditor.preference;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.goko.common.preferences.fieldeditor.objectcollection.CollectionObject;
import org.goko.common.preferences.fieldeditor.objectcollection.CollectionObjectLabelProvider;
import org.goko.common.preferences.fieldeditor.objectcollection.CollectionObjectSelectionDialog;
import org.goko.core.common.exception.GkException;

/**
 * Object collection editor
 * @author PsyKo
 *
 */
public class ObjectCollectionFieldEditor extends PreferenceFieldEditor<Composite> {
	private static final String SEPARATOR = ";";
	java.util.List<CollectionObject> availableObjects;
	java.util.List<CollectionObject> selectedObjects;
	private ListViewer listViewer;
	private CollectionObjectSelectionDialog selectionDialog;
	private Button removeButton;
	
	public ObjectCollectionFieldEditor(Composite parent, int style) {
		super(parent, style);
		availableObjects = new ArrayList<CollectionObject>();
		selectedObjects = new ArrayList<CollectionObject>();		
		createControls(parent, style);
		setLayout(new GridLayout(2, false));		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);

		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listViewer.setContentProvider(new ArrayContentProvider());
		listViewer.setLabelProvider(new CollectionObjectLabelProvider());
		listViewer.setInput(selectedObjects);
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateButton();			
			}
		});
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		
		Button addButton = new Button(composite, SWT.NONE);
		addButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				selectionDialog = new CollectionObjectSelectionDialog(getShell());
				java.util.List<CollectionObject> unselectedObjects = new ArrayList<CollectionObject>(availableObjects);
				unselectedObjects.removeAll(selectedObjects);
				selectionDialog.setAvailableObjects(unselectedObjects);
				int result = selectionDialog.open();
				
				if(result == Window.OK){
					ArrayList<CollectionObject> localSelectedObjects = new ArrayList<CollectionObject>(selectedObjects);
					localSelectedObjects.addAll(selectionDialog.getSelectedObjects());
					ObjectCollectionFieldEditor.this.setSelectedObjects(localSelectedObjects);
				}
			}
		});
		GridData gd_addButton = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_addButton.widthHint = 80;
		addButton.setLayoutData(gd_addButton);		
		addButton.setBounds(0, 0, 75, 25);
		addButton.setText("Add");
		
		removeButton = new Button(composite, SWT.NONE);
		removeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				ArrayList<CollectionObject> localSelectedObjects = new ArrayList<CollectionObject>(selectedObjects);
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				if(!selection.isEmpty()){
					Iterator iterator = selection.iterator();
					while(iterator.hasNext()){
						localSelectedObjects.remove(iterator.next());
					}
					ObjectCollectionFieldEditor.this.setSelectedObjects(localSelectedObjects);
				}
				updateButton();
			}
		});
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		removeButton.setText("Remove");		
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));		
		btnNewButton.setBounds(0, 0, 75, 25);
		btnNewButton.setText("Up");
		
		Button btnMoveDown = new Button(composite, SWT.NONE);
		btnMoveDown.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));		
		btnMoveDown.setBounds(0, 0, 75, 25);
		btnMoveDown.setText("Down");
		
		updateButton();
	}

	protected void updateButton(){
		IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();				
		removeButton.setEnabled(!selection.isEmpty());	
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		setSelectedObjects(new ArrayList<CollectionObject>());
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#loadValue()
	 */
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String listString = getPreferenceStore().getString(getPreferenceName());
		String[] tokens = StringUtils.split(listString, SEPARATOR);
		java.util.List<CollectionObject> lstObject = new ArrayList<CollectionObject>();
		if(tokens != null && tokens.length > 0){
			for (String value : tokens) {
				CollectionObject obj = findObject(value);
				if(obj != null){
					lstObject.add(obj);
				}
			}
		}
		setSelectedObjects(lstObject);
	}

	private CollectionObject findObject(String value){
		if(CollectionUtils.isNotEmpty(availableObjects)){
			for (CollectionObject collectionObject : availableObjects) {
				if(StringUtils.equals(value, collectionObject.getValue())){
					return collectionObject;
				}
			}
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		String listString = StringUtils.EMPTY;
		if(CollectionUtils.isNotEmpty(selectedObjects)){
			for (CollectionObject collectionObject : selectedObjects) {
				listString += collectionObject.getValue();
				listString += SEPARATOR;
			}
		}
		getPreferenceStore().setValue(getPreferenceName(), listString);
	}

	
	/**
	 * @return the availableObjects
	 */
	public java.util.List<CollectionObject> getAvailableObjects() {
		return availableObjects;
	}

	/**
	 * @param availableObjects the availableObjects to set
	 */
	public void setAvailableObjects(java.util.List<CollectionObject> availableObjects) {
		this.availableObjects = availableObjects;		
	}

	/**
	 * @return the selectObjects
	 */
	public java.util.List<CollectionObject> getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * @param selectObjects the selectObjects to set
	 */
	public void setSelectedObjects(java.util.List<CollectionObject> selectObjects) {
		this.selectedObjects = selectObjects;	
		if(listViewer != null){
			listViewer.setInput(selectObjects);
		}
	}

	
	
}
