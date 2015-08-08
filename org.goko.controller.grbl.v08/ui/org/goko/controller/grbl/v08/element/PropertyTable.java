/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.controller.grbl.v08.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeColumn;
import org.goko.controller.grbl.v08.element.model.MultichoiceProperty;
import org.goko.controller.grbl.v08.element.model.Property;

/**
 * Custom property table
 *
 * @author PsyKo
 *
 */
public class PropertyTable extends TreeViewer{

	/**
	 * Constructor
	 * @param parent
	 * @param style
	 */
	public PropertyTable(Composite parent, int style) {
		super(parent, SWT.FULL_SELECTION | style);
		init();
		fakeInput();
	}

	public void setPropertyInput(){

	}
	private void fakeInput() {
		List<Property> tst = new ArrayList<Property>();

		tst.add(new MultichoiceProperty("$","$a (x, step/mm)","80","","true","false"));
		tst.add(new Property<String>("$0","$0 (x, step/mm)","80",false));
		tst.add(new Property<String>("$1","$1 (y, step/mm)","23",false));
		tst.add(new Property<String>("$2","$2 (z, step/mm)","12",false));
		tst.add(new Property<String>("$3","$3 (step pulse, usec)","25",false));
		tst.add(new Property<String>("$4","$4 (default feed, mm/min)","1200",false));
		tst.add(new Property<String>("$5","$5 (default seek, mm/min)","120",false));
		tst.add(new Property<String>("$6","$6 (step port invert mask)","",false));
		tst.add(new Property<String>("$7","$7 (step idle delay, msec)","",false));
		tst.add(new Property<String>("$8","$8 (acceleration, mm/sec^2)","",false));
		tst.add(new Property<String>("$9","$9 (junction deviation, mm)","",false));
		tst.add(new Property<String>("$10","$10  (arc, mm/segment)","",false));
		tst.add(new Property<String>("$11","$11 (n-arc correction, int)","",false));
		tst.add(new Property<String>("$12","$12 (n-decimals, int)","",false));
		tst.add(new Property<String>("$13","$13 (report inches, bool)","",false));
		tst.add(new Property<String>("$14","$14 (auto start, bool)","",false));
		Property<String> invertGroup = new Property<String>("$15","$15 (invert step enable, bool)","",true);
		invertGroup.addChild(new MultichoiceProperty("$15x","X axis","Normal","Normal","Reversed"));
		invertGroup.addChild(new MultichoiceProperty("$15y","Y axis","Normal","Normal","Reversed"));
		invertGroup.addChild(new MultichoiceProperty("$15z","Z axis","Normal","Normal","Reversed"));
		tst.add(invertGroup);
		tst.add(new MultichoiceProperty("$16","$16 (hard limits, bool)","Enabled","Enabled","Disabled"));
		tst.add(new Property<String>("$17","$17 (homing cycle, bool)","",false));
		tst.add(new Property<String>("$18","$18 (homing dir invert mask, int:00000000)","",false));
		tst.add(new Property<String>("$19","$19 (homing feed, mm/min)","",false));
		tst.add(new Property<String>("$20","$20 (homing seek, mm/min)","",false));
		tst.add(new Property<String>("$21","$21 (homing debounce, msec)","",false));
		tst.add(new Property<String>("$22","$22 (homing pull-off, mm)","",false));

		setInput(tst.toArray(new Property[]{}));

	}


	private void init(){
		setContentProvider(new PropertyTreeContentProvider());

		TreeViewerColumn colLabel = createTableViewerColumn("Property",120,1);
		colLabel.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof Property<?>){
					return ((Property<?>) element).getLabel();
				}
				return super.getText(element);
			}
		});

		TreeViewerColumn colValue = createTableViewerColumn("Value",120,1);
		colValue.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof Property<?>){
					return ((Property<?>) element).getValue();
				}
				return super.getText(element);
			}

		});
		colValue.setEditingSupport(new PropertyCellEditor(this));

	}

	private TreeViewerColumn createTableViewerColumn(String title, int width, final int colNumber) {
	    final TreeViewerColumn viewerColumn = new TreeViewerColumn(this, SWT.NONE);
	    final TreeColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(width);
	    column.setResizable(true);
	    column.setMoveable(true);
	    return viewerColumn;
	}
}


class PropertyCellEditor extends EditingSupport{
	TreeViewer treeViewer;

	public PropertyCellEditor(TreeViewer viewer) {
		super(viewer);
		treeViewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		if(element instanceof MultichoiceProperty){
			return new ComboBoxCellEditor(treeViewer.getTree(),((MultichoiceProperty)element).getChoicesArray(), SWT.READ_ONLY);
		}else{
			return new TextCellEditor(treeViewer.getTree());
		}

	}

	@Override
	protected boolean canEdit(Object element) {
		if(element instanceof Property){
			return !((Property) element).isReadOnly();
		}
		return false;
	}

	@Override
	protected Object getValue(Object element) {
		if(element instanceof MultichoiceProperty){
			return ((MultichoiceProperty) element).getSelectedIndex();
		}else if(element instanceof Property){
			return ((Property) element).getValue();
		}
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		if(element instanceof MultichoiceProperty){
			((MultichoiceProperty) element).setSelectedIndex((Integer) value);

		}else if(element instanceof Property){
			((Property) element).setValue(value);
		}

		treeViewer.update(element, null);
	}

}

class PropertyTreeContentProvider implements ITreeContentProvider{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (Property[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((Property<?>)parentElement).getChildren().toArray();
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return CollectionUtils.isNotEmpty(((Property<?>)element).getChildren());
	}

}