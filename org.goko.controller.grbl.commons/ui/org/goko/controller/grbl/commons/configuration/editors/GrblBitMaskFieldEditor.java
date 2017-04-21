package org.goko.controller.grbl.commons.configuration.editors;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.exception.GkException;

public class GrblBitMaskFieldEditor<C extends AbstractGrblConfiguration<C>> extends BooleanFieldEditor implements IGrblFieldEditor<Button, C> {		
	private C cfg;
	private int bitPosition;
	
	public GrblBitMaskFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {				
		boolean wasSelected = getControl().getSelection();		
		getControl().setSelection( getValue() );
		fireStateChanged(VALUE, wasSelected, getControl().getSelection());
	}

	protected boolean getValue() throws GkException{
		Integer value = cfg.findSetting(preferenceName, Integer.class);
		return (value & (1 << bitPosition)) == (1 << bitPosition);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		boolean wasSelected = getControl().getSelection();
		getControl().setSelection( false );
		fireStateChanged(VALUE, wasSelected, getControl().getSelection());
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {
		Integer value = cfg.findSetting(preferenceName, Integer.class);
		if(getControl().getSelection()){
			value |= 1 << bitPosition;
		}else{
			value &= ~(1 << bitPosition);
		}
		cfg.setSetting(preferenceName, value);			
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.configuration.editors.IGrblFieldEditor#setConfiguration(org.goko.controller.grbl.v09.configuration.GrblConfiguration)
	 */
	@Override
	public void setConfiguration(C cfg) {
		this.cfg = cfg;
	}

	/**
	 * @return the bitPosition
	 */
	public int getBitPosition() {
		return bitPosition;
	}

	/**
	 * @param bitPosition the bitPosition to set
	 */
	public void setBitPosition(int bitPosition) {
		this.bitPosition = bitPosition;
	}

}
