package org.goko.controller.tinyg.configuration;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
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
		BigDecimal value = cfg.findSetting(groupIdentifier, preferenceName , BigDecimal.class);		
		if(value != null){
			getControl().setText( BigDecimalUtils.toString(value));
		}else{
			getControl().setText( StringUtils.EMPTY );
		}
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

