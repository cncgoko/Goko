package org.goko.controller.grbl.v09.configuration.editors;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.BigDecimalFieldEditor;
import org.goko.controller.grbl.v09.configuration.GrblConfiguration;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.BigDecimalUtils;
import org.goko.core.config.GokoPreference;

public class GrblBigDecimalSettingFieldEditor extends BigDecimalFieldEditor implements IGrblFieldEditor<Text>{
	GrblConfiguration cfg;
	
	public GrblBigDecimalSettingFieldEditor(Composite parent, int style) {
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
		BigDecimal value = cfg.findSetting(preferenceName , BigDecimal.class);		
		if(value != null){
			getControl().setText( BigDecimalUtils.toString(value, GokoPreference.getInstance().getDigitCount()));
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
		cfg.setSetting(getPreferenceName(), decimalValue);		
	}

	@Override
	public void setConfiguration(GrblConfiguration cfg) {
		this.cfg = cfg;
	}
	
}

