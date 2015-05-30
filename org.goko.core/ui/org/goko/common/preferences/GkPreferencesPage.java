package org.goko.common.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.goko.core.log.GkLog;

public abstract class GkPreferencesPage extends PreferencePage implements IPropertyChangeListener {
	private static final GkLog LOG = GkLog.getLogger(GkPreferencesPage.class);
	
}
