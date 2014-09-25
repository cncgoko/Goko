
package org.goko.test.recorder.ui.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.goko.common.elements.combo.LabeledValue;

public class testPart {
	private Table table;
	private Table table_1;
	@Inject
	public testPart() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FormLayout());

		TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(0, 98);
		fd_table.right = new FormAttachment(0, 507);
		fd_table.top = new FormAttachment(0);
		fd_table.left = new FormAttachment(0);
		table.setLayoutData(fd_table);

		tableViewer.setContentProvider(new ArrayContentProvider());

		List<Tuple<?>> lstTuple = new ArrayList<Tuple<?>>();
		TupleLabeledValue<Integer> tupleMotorMapping = new TupleLabeledValue<Integer>(tableViewer, "Mapping", new LabeledValue<Integer>(1, "X axis"),new LabeledValue<Integer>(2, "Y axis"),new LabeledValue<Integer>(4, "Z axis"),new LabeledValue<Integer>(8, "A axis"));
		lstTuple.add( new TupleDouble("Step angle", 1.5, 1.6, 1.7, 1.8));
		lstTuple.add( new TupleDouble("Travel/rev", 31.5, 21.6, 41.7, 51.8));

		TupleLabeledValue<Integer> tupleMicroStep = new TupleLabeledValue<Integer>(tableViewer, "Microsteps", new LabeledValue<Integer>(1, "1 Microstep"),new LabeledValue<Integer>(2, "1/2 Microstep"),new LabeledValue<Integer>(4, "1/4 Microstep"),new LabeledValue<Integer>(8, "1/8 Microstep"));
		TupleLabeledValue<Integer> tuplePolarity = new TupleLabeledValue<Integer>(tableViewer, "Polarity", new LabeledValue<Integer>(0, "Normal"),new LabeledValue<Integer>(1, "Reversed"));
		TupleLabeledValue<Integer> tuplePowerMode = new TupleLabeledValue<Integer>(tableViewer, "Power mode", new LabeledValue<Integer>(0, "Disabled"),new LabeledValue<Integer>(1, "Always on"),new LabeledValue<Integer>(2, "On when in cycle"),new LabeledValue<Integer>(3, "On when moving"));


		lstTuple.add( tupleMotorMapping );
		lstTuple.add( tupleMicroStep );
		lstTuple.add( tuplePolarity );
		lstTuple.add( tuplePowerMode );
		/*lstTuple.add( new TupleInteger("Polarity", 0, 1, 2, 3));
		lstTuple.add( new TupleInteger("Power mode", 0, 1, 2, 3));
		 */
		//lstTuple.add( new Tuple<Boolean>("Power management", true, false, false , true));

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return ((Tuple<?>)element).getName();
			}
		});
		TableColumn tableColumn = tableViewerColumn_1.getColumn();
		tableColumn.setWidth(100);

		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return String.valueOf(((Tuple<?>)element).get(0));
			}
		});
		tableViewerColumn.setEditingSupport(new TestEditingSupport(tableViewer, 0));
		TableColumn tblclmnMotor = tableViewerColumn.getColumn();
		tblclmnMotor.setWidth(100);
		tblclmnMotor.setText("Motor 1");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return String.valueOf(((Tuple<?>)element).get(1));
			}
		});
		tableViewerColumn_2.setEditingSupport(new TestEditingSupport(tableViewer, 1));

		TableColumn tblclmnMotor_1 = tableViewerColumn_2.getColumn();
		tblclmnMotor_1.setWidth(100);
		tblclmnMotor_1.setText("Motor 2");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnMotor_3 = tableViewerColumn_3.getColumn();
		tblclmnMotor_3.setWidth(100);
		tblclmnMotor_3.setText("Motor 3");
		tableViewerColumn_3.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return String.valueOf(((Tuple<?>)element).get(2));
			}
		});
		tableViewerColumn_3.setEditingSupport(new TestEditingSupport(tableViewer, 2));

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnMotor_2 = tableViewerColumn_4.getColumn();
		tblclmnMotor_2.setWidth(100);
		tblclmnMotor_2.setText("Motor 4");

		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				return String.valueOf(((Tuple<?>)element).get(3));
			}
		});
		tableViewerColumn_4.setEditingSupport(new TestEditingSupport(tableViewer, 3));

		TableViewer tableViewer_1 = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table_1 = tableViewer_1.getTable();
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		FormData fd_table_1 = new FormData();
		fd_table_1.bottom = new FormAttachment(100, -10);
		fd_table_1.right = new FormAttachment(table, 0, SWT.RIGHT);
		fd_table_1.top = new FormAttachment(table, 16);
		fd_table_1.left = new FormAttachment(0, 10);
		table_1.setLayoutData(fd_table_1);

//	/*	TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer_1, SWT.NONE);
//		TableColumn tblclmnProperty = tableViewerColumn_5.getColumn();
//		tblclmnProperty.setWidth(100);
//		tblclmnProperty.setText("Property");
//		tableViewerColumn_5.setLabelProvider(new PropertyNameColumnLabelProvider(0));
//
//		/*TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer_1, SWT.NONE);
//		TableColumn tblclmnMotor_4 = tableViewerColumn_6.getColumn();
//		tblclmnMotor_4.setWidth(100);
//		tblclmnMotor_4.setText("Motor 1");
//		tableViewerColumn_6.setLabelProvider(new PropertyValueColumnLabelProvider(0));
//		tableViewerColumn_6.setEditingSupport( new PropertyTableEditingSupport(tableViewer_1, 0));
//*/
//		createColumn(tableViewer_1, "Motor 1", 0);
//		createColumn(tableViewer_1, "Motor 2", 1);
//		createColumn(tableViewer_1, "Motor 3", 2);
//		createColumn(tableViewer_1, "Motor 4", 3);
//		tableViewer_1.setContentProvider(new PropertyTableContentProvider());
//		tableViewer_1.setInput(initTableProperty());
//
//
//		//tableViewer_1.setInput(lstTuple);
//
//	}
//
//	protected void createColumn(TableViewer tableViewer, String title, int columnIndex){
//		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
//		TableColumn tblclmnMotor = tableViewerColumn.getColumn();
//		tblclmnMotor.setWidth(100);
//		tblclmnMotor.setText(title);
//		tableViewerColumn.setLabelProvider(new PropertyValueColumnLabelProvider(columnIndex));
//		tableViewerColumn.setEditingSupport( new PropertyTableEditingSupport(tableViewer, columnIndex));
//
//	}
//	protected PropertyTable initTableProperty(){
//		PropertyTable pTable = new PropertyTable(4);
//
//
//		ComboboxPropertyDescriptor<Integer> axisMapping = new ComboboxPropertyDescriptor<Integer>("axismapping","Axis mapping");
//		axisMapping.addChoice("X axis", 1);
//		axisMapping.addChoice("Y axis", 2);
//		axisMapping.addChoice("Z axis", 3);
//		axisMapping.addChoice("A axis", 4);
//
//		DoublePropertyDescriptor stepangle = new DoublePropertyDescriptor("stepangle", "Step angle");
//		DoublePropertyDescriptor travelPerRev = new DoublePropertyDescriptor("travelPerRevolution", "Travel/revolution");
//
//		ComboboxPropertyDescriptor<Integer> microsteps = new ComboboxPropertyDescriptor<Integer>("microsteps","Microsteps");
//		microsteps.addChoice("1", 1);
//		microsteps.addChoice("1/2", 2);
//		microsteps.addChoice("1/4", 4);
//		microsteps.addChoice("1/8", 8);
//
//		ComboboxPropertyDescriptor<Integer> polarity = new ComboboxPropertyDescriptor<Integer>("polarity","Polarity");
//		polarity.addChoice("Normal", 0);
//		polarity.addChoice("Reversed", 1);
//
//		ComboboxPropertyDescriptor<Integer> powerMode = new ComboboxPropertyDescriptor<Integer>("powerMode","Power mode");
//		powerMode.addChoice("Powered when idle", 0);
//		powerMode.addChoice("Off when idle", 1);
//
//		pTable.addProperty(axisMapping);
//		pTable.addProperty(stepangle);
//		pTable.addProperty(travelPerRev);
//		pTable.addProperty(microsteps);
//		pTable.addProperty(polarity);
//		pTable.addProperty(powerMode);
//
//		return pTable;
//	}
	}
}