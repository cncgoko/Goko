package org.goko.test.recorder.ui.test;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.goko.common.elements.combo.LabeledValue;

public class TestEditingSupport extends EditingSupport {

	private final TableViewer viewer;
	private int columnIndex;

	public TestEditingSupport(TableViewer viewer, int columnIndex) {
		super(viewer);
		this.viewer = viewer;
		this.columnIndex = columnIndex;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		Tuple<?> tuple = (Tuple<?>) element;
		CellEditor editor = tuple.getEditor();
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return getTupleValue((Tuple<?>) element);
	}

	protected <T> Object getTupleValue(Tuple<T> element) {
		if(element.get(columnIndex) != null){
			if(element instanceof TupleLabeledValue<?>){
				return ((TupleLabeledValue<?>)element).get(columnIndex).getValue();
			}else{
				return String.valueOf(element.get(columnIndex));
			}
		}
		return StringUtils.EMPTY;
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		if (userInputValue != null) {
			((TupleLabeledValue<LabeledValue<?>>) element).putObject(
					columnIndex, ((LabeledValue<?>) userInputValue).getValue());
			viewer.update(element, null);
		}
	}

}