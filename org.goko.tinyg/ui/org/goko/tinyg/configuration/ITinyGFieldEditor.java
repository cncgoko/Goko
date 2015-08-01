package org.goko.tinyg.configuration;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

public interface ITinyGFieldEditor<C extends Control> extends IPreferenceFieldEditor<C> {

	void setConfiguration(TinyGConfiguration cfg);
}
