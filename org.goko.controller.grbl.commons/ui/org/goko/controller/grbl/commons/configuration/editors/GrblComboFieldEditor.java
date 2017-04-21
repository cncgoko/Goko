package org.goko.controller.grbl.commons.configuration.editors;

import java.math.BigDecimal;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.ComboFieldEditor;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.exception.GkException;

public class GrblComboFieldEditor<C extends AbstractGrblConfiguration<C>> extends ComboFieldEditor implements IGrblFieldEditor<Combo, C> {
	private C cfg;
	
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

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.editors.IGrblFieldEditor#setConfiguration(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration)
	 */
	@Override
	public void setConfiguration(C cfg) {
		this.cfg = cfg;
	}

}
