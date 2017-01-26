package org.goko.controller.tinyg.commons.configuration.fields;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.controller.tinyg.commons.configuration.AbstractTinyGConfiguration;

/**
 * Field editor for a TinyG based configuration
 * 
 * @author Psyko
 * @date 21 janv. 2017
 */
public interface ITinyGFieldEditor<C extends Control> extends IPreferenceFieldEditor<C> {

	/**
	 * Set the target configuration of this field 
	 * @param cfg the configuration to set
	 */
	void setConfiguration(AbstractTinyGConfiguration<?> cfg);
}
