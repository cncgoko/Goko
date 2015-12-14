package org.goko.controller.tinyg.configuration;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.controller.tinyg.controller.configuration.TinyGConfiguration;

public interface ITinyGFieldEditor<C extends Control> extends IPreferenceFieldEditor<C> {

	void setConfiguration(TinyGConfiguration cfg);
}
