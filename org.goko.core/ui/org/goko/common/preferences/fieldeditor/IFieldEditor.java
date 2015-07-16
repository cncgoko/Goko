package org.goko.common.preferences.fieldeditor;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Control;
import org.goko.core.common.exception.GkException;

public interface IFieldEditor<C extends Control> {

	void setPage(DialogPage page);

	void setPropertyChangeListener(IPropertyChangeListener listener);

	void setPreferenceStore(IPreferenceStore preferenceStore);

	void load() throws GkException;

	boolean isValid();

	C getControl();

	void setDefault() throws GkException;

	void store() throws GkException;

}
