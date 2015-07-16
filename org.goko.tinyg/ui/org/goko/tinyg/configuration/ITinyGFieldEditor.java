package org.goko.tinyg.configuration;

import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.IFieldEditor;
import org.goko.tinyg.controller.configuration.TinyGConfiguration;

public interface ITinyGFieldEditor<C extends Control> extends IFieldEditor<C> {

	void setConfiguration(TinyGConfiguration cfg);
}
