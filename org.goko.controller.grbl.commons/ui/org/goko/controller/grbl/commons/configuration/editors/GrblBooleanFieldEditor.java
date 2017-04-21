package org.goko.controller.grbl.commons.configuration.editors;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.preferences.fieldeditor.preference.BooleanFieldEditor;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;
import org.goko.core.common.exception.GkException;

public class GrblBooleanFieldEditor<C extends AbstractGrblConfiguration<C>> extends BooleanFieldEditor implements IGrblFieldEditor<Button, C> {		
	private C cfg;
	
	public GrblBooleanFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#loadValue()
	 */
	@Override
	protected void loadValue() throws GkException {				
		boolean wasSelected = getControl().getSelection();
		Boolean value = cfg.findSetting(preferenceName, Boolean.class);
		getControl().setSelection( value );
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
			cfg.setSetting(preferenceName, true);
		}else{
			cfg.setSetting(preferenceName, false);	
		}
		
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.configuration.editors.IGrblFieldEditor#setConfiguration(org.goko.controller.grbl.v09.configuration.GrblConfiguration)
	 */
	@Override
	public void setConfiguration(C cfg) {
		this.cfg = cfg;
	}

}
