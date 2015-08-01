package org.goko.common.preferences.fieldeditor.preference;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.widgets.Control;
import org.goko.common.preferences.fieldeditor.IFieldEditor;
import org.goko.core.common.exception.GkException;

public interface IPreferenceFieldEditor<C extends Control> extends IFieldEditor<C>{

	void setPage(DialogPage page);

	void setPreferenceStore(IPreferenceStore preferenceStore);

	void setPropertyChangeListener(IPropertyChangeListener listener);

	void load() throws GkException;

	void setDefault() throws GkException;

	void store() throws GkException;
}
