package org.goko.common.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

/** This interface can be implemented to provide a PreferenceStore for a given plugin. 
 * This associatino must be done in the e4PreferenceStoreProvider extension point. 
 * @author olivier
 *
 */
public interface IPreferenceStoreProvider
{
	/** Must be implemented to return a preference store */
	public IPreferenceStore getPreferenceStore();

}
