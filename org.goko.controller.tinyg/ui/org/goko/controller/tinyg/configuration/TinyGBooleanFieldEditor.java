package org.goko.controller.tinyg.configuration;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;

public class TinyGBooleanFieldEditor extends BooleanFieldEditor implements ITinyGFieldEditor<Button> {
	private String groupIdentifier;	
	private TinyGConfiguration cfg;
	private BigDecimal trueValue = BigDecimal.ONE;
	private BigDecimal falseValue = BigDecimal.ZERO;
	
	public TinyGBooleanFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {				
		boolean wasSelected = getControl().getSelection();
		BigDecimal value = cfg.findSetting(groupIdentifier, preferenceName, BigDecimal.class);
		getControl().setSelection( ObjectUtils.equals(value, trueValue) );
		fireStateChanged(VALUE, wasSelected, getControl().getSelection());
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
		if(getControl().getSelection()){
			cfg.setSetting(groupIdentifier, preferenceName, trueValue);
		}else{
			cfg.setSetting(groupIdentifier, preferenceName, falseValue);	
		}
		
	}

	/**
	 * @return the groupIdentifier
	 */
	public String getGroupIdentifier() {
		return groupIdentifier;
	}

	/**
	 * @param groupIdentifier the groupIdentifier to set
	 */
	public void setGroupIdentifier(String groupIdentifier) {
		this.groupIdentifier = groupIdentifier;
	}

	@Override
	public void setConfiguration(TinyGConfiguration cfg) {
		this.cfg = cfg;
	}

}
