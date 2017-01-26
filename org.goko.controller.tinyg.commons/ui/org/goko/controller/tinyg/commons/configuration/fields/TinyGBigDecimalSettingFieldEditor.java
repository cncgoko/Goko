package org.goko.controller.tinyg.commons.configuration.fields;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;

/**
 * Field editor for a BigDecimal TinyG setting 
 * 
 * @author Psyko
 * @date 21 janv. 2017
 */
public class TinyGBigDecimalSettingFieldEditor extends BigDecimalFieldEditor implements ITinyGFieldEditor<Text>{
	/** The identifier of the group this setting belongs to */
	private String groupIdentifier;
	/** The target configuration */
	private AbstractTinyGConfiguration<?> config;
	
	/**
	 * Constructor 
	 * @param parent
	 * @param style
	 */
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
		BigDecimal value = config.findSetting(groupIdentifier, preferenceName , BigDecimal.class);		
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
		config.setSetting(groupIdentifier, getPreferenceName(), decimalValue);		
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

