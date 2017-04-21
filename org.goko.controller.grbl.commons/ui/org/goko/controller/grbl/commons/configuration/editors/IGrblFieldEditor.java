package org.goko.controller.grbl.commons.configuration.editors;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.preference.IPreferenceFieldEditor;
import org.goko.controller.grbl.commons.configuration.AbstractGrblConfiguration;

public interface IGrblFieldEditor<C extends Control, F extends AbstractGrblConfiguration<F>> extends IPreferenceFieldEditor<C> {

	void setConfiguration(F cfg);
}