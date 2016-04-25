package org.goko.controller.grbl.v09.configuration.editors;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.controller.grbl.v09.configuration.GrblConfiguration;
import org.goko.core.common.exception.GkException;

public class GrblComboFieldEditor extends ComboFieldEditor implements IGrblFieldEditor<Combo> {
	GrblConfiguration cfg;
	
	public GrblComboFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {
		String value = String.valueOf(cfg.findSetting(preferenceName, BigDecimal.class));
		updateComboForValue(value);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#setDefaultValue()
	 */
	@Override
	protected void setDefaultValue() throws GkException {
		updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#storeValue()
	 */
	@Override
	protected void storeValue() throws GkException {		
		cfg.setSetting(preferenceName, new BigDecimal(getValue()));
	}

	@Override
	public void setConfiguration(GrblConfiguration cfg) {
		this.cfg = cfg;
	}

}
