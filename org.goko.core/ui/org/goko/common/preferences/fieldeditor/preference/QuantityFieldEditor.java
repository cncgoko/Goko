package org.goko.common.preferences.fieldeditor.preference;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.units.Unit;

public class QuantityFieldEditor<Q extends Quantity<Q>> extends BigDecimalFieldEditor {
	private Label labelUnit;
	private Unit<Q> unit;
	
	public QuantityFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected void createLayout(Composite parent) {
    	GridLayout layout = new GridLayout(3, false);
    	layout.marginHeight = 2;
    	layout.marginWidth = 2;
    	setLayout(layout);  
	}
	
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		labelUnit = new Label(this, style);    	
    	labelUnit.setText("mm");
    	labelUnit.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
	}

	/**
	 * @return the unit
	 */
	public Unit<Q> getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit<Q> unit) {
		this.unit = unit;
		labelUnit.setText( unit.getSymbol() );
		labelUnit.pack();
	}
	
}
