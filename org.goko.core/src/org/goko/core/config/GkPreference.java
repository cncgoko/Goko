/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.config;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.goko.common.preferences.ScopedPreferenceStore;

public abstract class GkPreference implements IPreferenceStore, IPersistentPreferenceStore{
	private ScopedPreferenceStore store;

	public GkPreference(String id) {
		this(id, InstanceScope.INSTANCE);	
	}
	
	public GkPreference(String id, IScopeContext scope) {
		store = new ScopedPreferenceStore(scope, id);	
	}
	
	public ScopedPreferenceStore getPreferenceStore(){
		return store;
	}
	/**
	 * @param listener
	 * @see org.goko.common.preferences.ScopedPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		store.addPropertyChangeListener(listener);
	}

	/**
	 * @param includeDefault
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getPreferenceNodes(boolean)
	 */
	public IEclipsePreferences[] getPreferenceNodes(boolean includeDefault) {
		return store.getPreferenceNodes(includeDefault);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#contains(java.lang.String)
	 */
	public boolean contains(String name) {
		return store.contains(name);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return store.equals(obj);
	}

	/**
	 * @param scopes
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setSearchContexts(org.eclipse.core.runtime.preferences.IScopeContext[])
	 */
	public void setSearchContexts(IScopeContext[] scopes) {
		store.setSearchContexts(scopes);
	}

	/**
	 * @param name
	 * @param oldValue
	 * @param newValue
	 * @see org.goko.common.preferences.ScopedPreferenceStore#firePropertyChangeEvent(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		store.firePropertyChangeEvent(name, oldValue, newValue);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String name) {
		return store.getBoolean(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultBoolean(java.lang.String)
	 */
	public boolean getDefaultBoolean(String name) {		
		return store.getDefaultBoolean(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultDouble(java.lang.String)
	 */
	public double getDefaultDouble(String name) {
		return store.getDefaultDouble(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultFloat(java.lang.String)
	 */
	public float getDefaultFloat(String name) {
		return store.getDefaultFloat(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultInt(java.lang.String)
	 */
	public int getDefaultInt(String name) {
		return store.getDefaultInt(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultLong(java.lang.String)
	 */
	public long getDefaultLong(String name) {
		return store.getDefaultLong(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDefaultString(java.lang.String)
	 */
	public String getDefaultString(String name) {
		return store.getDefaultString(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getDouble(java.lang.String)
	 */
	public double getDouble(String name) {
		return store.getDouble(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getFloat(java.lang.String)
	 */
	public float getFloat(String name) {
		return store.getFloat(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getInt(java.lang.String)
	 */
	public int getInt(String name) {
		return store.getInt(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getLong(java.lang.String)
	 */
	public long getLong(String name) {
		return store.getLong(name);
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#getString(java.lang.String)
	 */
	public String getString(String name) {
		return store.getString(name);
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return store.hashCode();
	}

	/**
	 * @param name
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#isDefault(java.lang.String)
	 */
	public boolean isDefault(String name) {
		return store.isDefault(name);
	}

	/**
	 * @return
	 * @see org.goko.common.preferences.ScopedPreferenceStore#needsSaving()
	 */
	public boolean needsSaving() {
		return store.needsSaving();
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#putValue(java.lang.String, java.lang.String)
	 */
	public void putValue(String name, String value) {
		store.putValue(name, value);
	}

	/**
	 * @param listener
	 * @see org.goko.common.preferences.ScopedPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		store.removePropertyChangeListener(listener);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, double)
	 */
	public void setDefault(String name, double value) {
		store.setDefault(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, float)
	 */
	public void setDefault(String name, float value) {
		store.setDefault(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, int)
	 */
	public void setDefault(String name, int value) {
		store.setDefault(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, long)
	 */
	public void setDefault(String name, long value) {
		store.setDefault(name, value);
	}

	/**
	 * @param name
	 * @param defaultObject
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, java.lang.String)
	 */
	public void setDefault(String name, String defaultObject) {
		store.setDefault(name, defaultObject);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setDefault(java.lang.String, boolean)
	 */
	public void setDefault(String name, boolean value) {
		store.setDefault(name, value);
	}

	/**
	 * @param name
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setToDefault(java.lang.String)
	 */
	public void setToDefault(String name) {
		store.setToDefault(name);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, double)
	 */
	public void setValue(String name, double value) {
		store.setValue(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, float)
	 */
	public void setValue(String name, float value) {
		store.setValue(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, int)
	 */
	public void setValue(String name, int value) {
		store.setValue(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, long)
	 */
	public void setValue(String name, long value) {
		store.setValue(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, java.lang.String)
	 */
	public void setValue(String name, String value) {
		store.setValue(name, value);
	}

	/**
	 * @param name
	 * @param value
	 * @see org.goko.common.preferences.ScopedPreferenceStore#setValue(java.lang.String, boolean)
	 */
	public void setValue(String name, boolean value) {
		store.setValue(name, value);
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPersistentPreferenceStore#save()
	 */
	@Override
	public void save() throws IOException {
		store.save();
	}
	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return store.toString();
	}
	
//	
//	/**
//	 * Constructor
//	 */
//	protected GkPreference(String id) {
//		preferences = InstanceScope.INSTANCE.getNode(id);		
//		preferences = InstanceScope.INSTANCE.getNode(id+".defaultValues");		
//	}
//
//	/**
//	 * @return the preferences
//	 */
//	public IEclipsePreferences getPreferences() {
//		return preferences;
//	}
//
//	/**
//	 * @param preferences the preferences to set
//	 */
//	protected void setPreferences(IEclipsePreferences preferences) {
//		this.preferences = preferences;
//	}
//
//	public void save() throws GkException{
//		try {
//			getPreferences().flush();
//		} catch (BackingStoreException e) {
//			throw new GkTechnicalException(e);
//		}
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
//	 */
//	@Override
//	public void addPropertyChangeListener(IPropertyChangeListener listener) {
//		
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
//	 */
//	@Override
//	public boolean contains(String name) {
//		try {
//			return preferences.nodeExists(name);
//		} catch (BackingStoreException e) {
//			return false;
//		}
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent(java.lang.String, java.lang.Object, java.lang.Object)
//	 */
//	@Override
//	public void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String)
//	 */
//	@Override
//	public boolean getBoolean(String name) {		
//		return preferences.getBoolean(name, IPreferenceStore.BOOLEAN_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.lang.String)
//	 */
//	@Override
//	public boolean getDefaultBoolean(String name) {		
//		return IPreferenceStore.BOOLEAN_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang.String)
//	 */
//	@Override
//	public double getDefaultDouble(String name) {		
//		return IPreferenceStore.DOUBLE_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang.String)
//	 */
//	@Override
//	public float getDefaultFloat(String name) {
//		return IPreferenceStore.FLOAT_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang.String)
//	 */
//	@Override
//	public int getDefaultInt(String name) {
//		return IPreferenceStore.INT_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang.String)
//	 */
//	@Override
//	public long getDefaultLong(String name) {
//		return IPreferenceStore.LONG_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang.String)
//	 */
//	@Override
//	public String getDefaultString(String name) {
//		return IPreferenceStore.STRING_DEFAULT_DEFAULT;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
//	 */
//	@Override
//	public double getDouble(String name) {
//		return preferences.getDouble(name, IPreferenceStore.DOUBLE_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
//	 */
//	@Override
//	public float getFloat(String name) {
//		return preferences.getFloat(name, IPreferenceStore.FLOAT_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
//	 */
//	@Override
//	public int getInt(String name) {		
//		return preferences.getInt(name, IPreferenceStore.INT_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
//	 */
//	@Override
//	public long getLong(String name) {
//		return preferences.getLong(name, IPreferenceStore.LONG_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
//	 */
//	@Override
//	public String getString(String name) {
//		return preferences.get(name, IPreferenceStore.STRING_DEFAULT_DEFAULT);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
//	 */
//	@Override
//	public boolean isDefault(String name) {		
//		return false;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
//	 */
//	@Override
//	public boolean needsSaving() {
//		return true;
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void putValue(String name, String value) {
//		preferences.put(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
//	 */
//	@Override
//	public void removePropertyChangeListener(IPropertyChangeListener listener) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, double)
//	 */
//	@Override
//	public void setDefault(String name, double value) {
//		defaultPreferences.putDouble(name, value);		
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, float)
//	 */
//	@Override
//	public void setDefault(String name, float value) {
//		defaultPreferences.putFloat(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, int)
//	 */
//	@Override
//	public void setDefault(String name, int value) {
//		defaultPreferences.putInt(name, value);		
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, long)
//	 */
//	@Override
//	public void setDefault(String name, long value) {
//		defaultPreferences.putLong(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void setDefault(String name, String value) {
//		defaultPreferences.put(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, boolean)
//	 */
//	@Override
//	public void setDefault(String name, boolean value) {
//		defaultPreferences.putBoolean(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.String)
//	 */
//	@Override
//	public void setToDefault(String name) {
//		preferences.put(name, defaultPreferences.get(name, STRING_DEFAULT_DEFAULT));
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, double)
//	 */
//	@Override
//	public void setValue(String name, double value) {
//		preferences.putDouble(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, float)
//	 */
//	@Override
//	public void setValue(String name, float value) {
//		preferences.putFloat(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, int)
//	 */
//	@Override
//	public void setValue(String name, int value) {
//		preferences.putInt(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, long)
//	 */
//	@Override
//	public void setValue(String name, long value) {
//		preferences.putLong(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void setValue(String name, String value) {
//		preferences.put(name, value);
//	}
//
//	/** (inheritDoc)
//	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, boolean)
//	 */
//	@Override
//	public void setValue(String name, boolean value) {
//		preferences.putBoolean(name, value);
//	}
}
