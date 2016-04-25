package org.goko.controller.grbl.v09.configuration.editors;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.controller.grbl.v09.configuration.GrblConfiguration;

public interface IGrblFieldEditor<C extends Control> extends IPreferenceFieldEditor<C> {

	void setConfiguration(GrblConfiguration cfg);
}