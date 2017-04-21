package org.goko.controller.grbl.commons.configuration.editors;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.preferences.fieldeditor.preference.IntegerFieldEditor;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.exception.GkException;

public class GrblIntegerSettingFieldEditor<C extends AbstractGrblConfiguration<C>> extends IntegerFieldEditor implements IGrblFieldEditor<Text, C>{
	private C cfg;
	
	public GrblIntegerSettingFieldEditor(Composite parent, int style) {
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
		Integer value = cfg.findSetting(preferenceName , Integer.class);		
		if(value != null){
			getControl().setText( String.valueOf(value));
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
		Integer integerValue = Integer.valueOf(getControl().getText());
		cfg.setSetting(getPreferenceName(), integerValue);		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.commons.configuration.editors.IGrblFieldEditor#setConfiguration(org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration)
	 */
	@Override
	public void setConfiguration(C cfg) {
		this.cfg = cfg;
	}
	
}

