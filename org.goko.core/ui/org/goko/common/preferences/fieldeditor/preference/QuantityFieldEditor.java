package org.goko.common.preferences.fieldeditor.preference;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.dimension.Dimension;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.common.utils.BigDecimalUtils;
import org.goko.core.config.GokoPreference;

public class QuantityFieldEditor<Q extends Quantity<Q>> extends BigDecimalFieldEditor {
	private Label labelUnit;
	private Unit<Q> unit;
	private Quantity<Q> quantity;
	private Dimension<Q> dimension;
	
	public QuantityFieldEditor(Composite parent, int style, Dimension<Q> dimension) {
		super(parent, style);
		this.dimension = dimension;
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#createLayout(org.eclipse.swt.widgets.Composite)
	 */
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
	 * @throws GkException GkException 
	 */
	public void setUnit(Unit<Q> unit) throws GkException {
		this.unit = unit;
		if(labelUnit != null && !labelUnit.isDisposed()){
			labelUnit.setText( unit.getSymbol() );
			labelUnit.pack();
		}
		// If the value was already loaded, update it
		if(getControl() != null && !getControl().isDisposed() && quantity != null){
			loadValue();
		}
	}	
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {		
		BigDecimal decimalValue = BigDecimalUtils.parse(getControl().getText());
		getPreferenceStore().setValue(getPreferenceName(), decimalValue.toString()+getUnit().getSymbol());
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		quantity =  NumberQuantity.of(getPreferenceStore().getString(getPreferenceName()), dimension);
		getControl().setText( GokoPreference.getInstance().format(quantity.to(getUnit()), false, false));
		refreshValidState();
	}
}
