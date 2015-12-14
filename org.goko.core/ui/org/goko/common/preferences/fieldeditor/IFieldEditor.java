package org.goko.common.preferences.fieldeditor;

import org.eclipse.swt.widgets.Control;

public interface IFieldEditor<C extends Control> {

	boolean isValid();

	C getControl();
}
