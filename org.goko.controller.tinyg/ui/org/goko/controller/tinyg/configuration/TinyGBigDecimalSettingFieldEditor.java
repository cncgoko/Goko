package org.goko.controller.tinyg.configuration;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;

public class TinyGBigDecimalSettingFieldEditor extends BigDecimalFieldEditor implements ITinyGFieldEditor<Text>{
	String groupIdentifier;	
	TinyGConfiguration cfg;
	
	public TinyGBigDecimalSettingFieldEditor(Composite parent, int style) {
		super(parent, style);		
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		getControl().setText( "0" );
		refreshValidState();
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		BigDecimal value = cfg.getSetting(groupIdentifier, preferenceName , BigDecimal.class);		
		getControl().setText( BigDecimalUtils.toString(value));
		refreshValidState();		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {		
		BigDecimal decimalValue = BigDecimalUtils.parse(getControl().getText());
		cfg.setSetting(groupIdentifier, getPreferenceName(), decimalValue);		
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

