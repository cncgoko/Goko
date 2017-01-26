package org.goko.controller.tinyg.commons.configuration.fields;

import java.math.BigDecimal;

import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;

/**
 * Field editor for a BigDecimal TinyG setting 
 * 
 * @author Psyko
 * @date 21 janv. 2017
 */
public class TinyGBooleanFieldEditor extends BooleanFieldEditor implements ITinyGFieldEditor<Button> {
	/** The identifier of the group this setting belongs to */
	private String groupIdentifier;
	/** The target configuration */
	private AbstractTinyGConfiguration<?> config;
	/** Value for true */
	private BigDecimal trueValue = BigDecimal.ONE;
	/** Value for false */
	private BigDecimal falseValue = BigDecimal.ZERO;
	
	/**
	 * Constructor 
	 * @param parent
	 * @param style
	 */
	public TinyGBooleanFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {				
		boolean wasSelected = getControl().getSelection();
		BigDecimal value = config.findSetting(groupIdentifier, preferenceName, BigDecimal.class);
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
			config.setSetting(groupIdentifier, preferenceName, trueValue);
		}else{
			config.setSetting(groupIdentifier, preferenceName, falseValue);	
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
	public void setConfiguration(AbstractTinyGConfiguration<?> cfg) {
		this.config = cfg;
	}

}
