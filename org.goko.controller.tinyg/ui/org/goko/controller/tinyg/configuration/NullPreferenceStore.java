package org.goko.controller.tinyg.configuration;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * @author PsyKo
 *
 */
public class NullPreferenceStore implements IPreferenceStore {

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String name) {
		// Simply do nothing
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#firePropertyChangeEvent(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String name) {
		// Simply do nothing
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultBoolean(java.lang.String)
	 */
	@Override
	public boolean getDefaultBoolean(String name) {
		// Simply do nothing
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultDouble(java.lang.String)
	 */
	@Override
	public double getDefaultDouble(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultFloat(java.lang.String)
	 */
	@Override
	public float getDefaultFloat(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultInt(java.lang.String)
	 */
	@Override
	public int getDefaultInt(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultLong(java.lang.String)
	 */
	@Override
	public long getDefaultLong(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDefaultString(java.lang.String)
	 */
	@Override
	public String getDefaultString(String name) {
		// Simply do nothing
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getFloat(java.lang.String)
	 */
	@Override
	public float getFloat(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String name) {
		// Simply do nothing
		return 0;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#getString(java.lang.String)
	 */
	@Override
	public String getString(String name) {
		// Simply do nothing
		return null;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#isDefault(java.lang.String)
	 */
	@Override
	public boolean isDefault(String name) {
		// Simply do nothing
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#needsSaving()
	 */
	@Override
	public boolean needsSaving() {
		// Simply do nothing
		return false;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#putValue(java.lang.String, java.lang.String)
	 */
	@Override
	public void putValue(String name, String value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, double)
	 */
	@Override
	public void setDefault(String name, double value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, float)
	 */
	@Override
	public void setDefault(String name, float value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, int)
	 */
	@Override
	public void setDefault(String name, int value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, long)
	 */
	@Override
	public void setDefault(String name, long value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, java.lang.String)
	 */
	@Override
	public void setDefault(String name, String defaultObject) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setDefault(java.lang.String, boolean)
	 */
	@Override
	public void setDefault(String name, boolean value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setToDefault(java.lang.String)
	 */
	@Override
	public void setToDefault(String name) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, double)
	 */
	@Override
	public void setValue(String name, double value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, float)
	 */
	@Override
	public void setValue(String name, float value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, int)
	 */
	@Override
	public void setValue(String name, int value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, long)
	 */
	@Override
	public void setValue(String name, long value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, java.lang.String)
	 */
	@Override
	public void setValue(String name, String value) {
		// Simply do nothing

	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.preference.IPreferenceStore#setValue(java.lang.String, boolean)
	 */
	@Override
	public void setValue(String name, boolean value) {
		// Simply do nothing

	}

}
