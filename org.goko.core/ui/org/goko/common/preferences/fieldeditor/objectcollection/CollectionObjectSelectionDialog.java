package org.goko.common.preferences.fieldeditor.objectcollection;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CollectionObjectSelectionDialog extends Dialog implements ISelectionChangedListener {
	private Text textDescription;
	private java.util.List<CollectionObject> availableObjects;
	private java.util.List<CollectionObject> selectedObjects;
	private ListViewer valueListViewer;
	
	/**
	 * @wbp.parser.constructor
	 */
	public CollectionObjectSelectionDialog(Shell parentShell) {
		super(parentShell);		
		availableObjects = new ArrayList<CollectionObject>();
	}

	public CollectionObjectSelectionDialog(IShellProvider parentShell) {
		super(parentShell);
		availableObjects = new ArrayList<CollectionObject>();
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		valueListViewer = new ListViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		List list = valueListViewer.getList();
		valueListViewer.setContentProvider(new ArrayContentProvider());
		valueListViewer.setLabelProvider(new CollectionObjectLabelProvider());
		valueListViewer.addSelectionChangedListener(this);
		valueListViewer.setInput(availableObjects);
		GridData gd_list = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_list.minimumHeight = 250;
		gd_list.minimumWidth = 250;
		list.setLayoutData(gd_list);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_scrolledComposite.heightHint = 40;
		scrolledComposite.setLayoutData(gd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		textDescription = new Text(scrolledComposite, SWT.READ_ONLY);
		scrolledComposite.setContent(textDescription);
		scrolledComposite.setMinSize(textDescription.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		return composite;
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
		if(valueListViewer != null){
			valueListViewer.setInput(availableObjects);
		}
	}	

	

	protected Point getInitialSize() {
		return new Point(281, 424);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		IStructuredSelection selection = (IStructuredSelection) this.valueListViewer.getSelection();
		selectedObjects = new ArrayList<CollectionObject>();
		if(!selection.isEmpty()){
			Iterator iterator = selection.iterator();
			while (iterator.hasNext()) {
				selectedObjects.add((CollectionObject) iterator.next());				
			}
		}
		super.okPressed();
	}
	/**
	 * @param event
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		if(ObjectUtils.equals(selection.size(), 1)){
			CollectionObject object = (CollectionObject) selection.getFirstElement();
			textDescription.setText(object.getDescription());
		}else{
			textDescription.setText(StringUtils.EMPTY);
		}
	}

	/**
	 * @return the selectedObjects
	 */
	public java.util.List<CollectionObject> getSelectedObjects() {
		return selectedObjects;
	}

	/**
	 * @param selectedObjects the selectedObjects to set
	 */
	public void setSelectedObjects(java.util.List<CollectionObject> selectedObjects) {
		this.selectedObjects = selectedObjects;
	}
}
