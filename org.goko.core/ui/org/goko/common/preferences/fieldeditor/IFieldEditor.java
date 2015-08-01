package org.goko.common.preferences.fieldeditor;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Control;
import org.goko.core.common.exception.GkException;

public interface IFieldEditor<C extends Control> {

	boolean isValid();

	C getControl();
}
