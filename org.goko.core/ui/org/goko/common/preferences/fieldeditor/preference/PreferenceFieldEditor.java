package org.goko.common.preferences.fieldeditor.preference;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.log.GkLog;

public abstract class PreferenceFieldEditor<C extends Control> extends Composite implements IPreferenceFieldEditor<C>{
    /** LOG */
	private static final GkLog LOG = GkLog.getLogger(PreferenceFieldEditor.class);
	/**
     * Property name constant (value <code>"field_editor_is_valid"</code>)
     * to signal a change in the validity of the value of this field editor.
     */
    public static final String IS_VALID = "field_editor_is_valid";
    /**
     * Property name constant (value <code>"field_editor_value"</code>)
     * to signal a change in the value of this field editor.
     */
    public static final String VALUE = "field_editor_value";
	  /** The preference store, or <code>null</code> if none. */
    private IPreferenceStore preferenceStore = null;
    /** The name of the preference displayed in this field editor. */
    protected String preferenceName;
    /** The label's text. */
    private String label;
    /**  The associated control */
    protected C control;
	/** Listener, or <code>null</code> if none */
    private IPropertyChangeListener propertyChangeListener;
    /**  The page containing this field editor */
    private DialogPage page;
    
    /**
     * Constructor
     * @param parent
     * @param style
     */
    public PreferenceFieldEditor(Composite parent, int style){
    	super(parent, style);		
    	label = "Default";    	
    }
    
    protected void createLayout(Composite parent){
    	GridLayout layout = new GridLayout(2, false);
    	layout.marginHeight = 2;
    	layout.marginWidth = 2;
    	setLayout(layout);    	
    }
    
    protected void createControls(Composite parent, int style){
    	createLayout(parent);    	
    }
    /**
     * Set the value of this field to the default value
     * @throws GkException GkException
     */
    public final void setDefault() throws GkException{
    	checkPreferenceStore();
    	setDefaultValue();
    }
    
    @Override
    public void setEnabled(boolean enabled) {
    	boolean wasEnabled = isEnabled();
    	super.setEnabled(enabled);
    	if(!wasEnabled && enabled){
    		try {
				loadValue();
			} catch (GkException e) {
				LOG.error(e);
			}
    	}
    }
    
    /**
     * Set the value of this field to the default value
     * @throws GkException GkException
     */
    protected abstract void setDefaultValue() throws GkException;
    

    /**
     * Initialize the value of this field 
     * @throws GkException GkException
     */
    public final void load() throws GkException{
    	checkPreferenceStore();
    	loadValue();
    }
    /**
     * Initialize the value of this field 
     * @throws GkException GkException
     */
    protected abstract void loadValue() throws GkException;
    
    /**
     * Store the value of this field 
     * @throws GkException GkException
     */
    public final void store() throws GkException{
    	checkPreferenceStore();
    	storeValue();
    }
    
    /**
     * Store the value of this field 
     * @throws GkException GkException
     */
    protected abstract void storeValue() throws GkException;
    
       
    public boolean isValid(){
    	return true;
    }
    /**
     * Informs this field editor's listener, if it has one, about a change to
     * one of this field editor's boolean-valued properties. Does nothing
     * if the old and new values are the same.
     *
     * @param property the field editor property name, 
     *   such as <code>VALUE</code> or <code>IS_VALID</code>
     * @param oldValue the old value
     * @param newValue the new value
     */
    protected void fireStateChanged(String property, boolean oldValue, boolean newValue) {
        if (oldValue == newValue) {
			return;
		}
        fireValueChanged(property, oldValue ? Boolean.TRUE : Boolean.FALSE, newValue ? Boolean.TRUE : Boolean.FALSE);
    }
    

    /**
     * Informs this field editor's listener, if it has one, about a change to
     * one of this field editor's properties.
     *
     * @param property the field editor property name, 
     *   such as <code>VALUE</code> or <code>IS_VALID</code>
     * @param oldValue the old value object, or <code>null</code>
     * @param newValue the new value, or <code>null</code>
     */
    protected void fireValueChanged(String property, Object oldValue, Object newValue) {
        if (propertyChangeListener == null) {
			return;
		}
        propertyChangeListener.propertyChange(new PropertyChangeEvent(this,
                property, oldValue, newValue));
    }
    
    /**
     * Checks if a preference store is set
     * @throws GkException GkException
     */
    private void checkPreferenceStore() throws GkException{
    	if(preferenceStore == null){
    		throw new GkFunctionalException("No preferences store for FieldEditor");
    	}
    	
    	if(StringUtils.isBlank(preferenceName)){
    		throw new GkFunctionalException("No preference name for FieldEditor");
    	}
    }
    
    /**
     * Clears the error message in the page for this
     * field editor if it has one.
     *
     * @param msg the error message
     */
    protected void clearErrorMessage() {
        if (page != null) {
			page.setErrorMessage(null);
		}
    }
    
    /**
     * Shows the given error message in the page for this
     * field editor if it has one.
     *
     * @param msg the error message
     */
    protected void showErrorMessage(String msg) {
        if (page != null) {
			page.setErrorMessage(msg);
		}
    }

    /**
     * Shows the given message in the page for this
     * field editor if it has one.
     *
     * @param msg the message
     */
    protected void showMessage(String msg) {
        if (page != null) {
			page.setErrorMessage(msg);
		}
    }
    
	/**
	 * @return the preferenceStore
	 */
	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}

	/**
	 * @param preferenceStore the preferenceStore to set
	 */
	public void setPreferenceStore(IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
	}

	/**
	 * @return the preferenceName
	 */
	public String getPreferenceName() {
		return preferenceName;
	}

	/**
	 * @param preferenceName the preferenceName to set
	 */
	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
	}

	/**
	 * @return the propertyChangeListener
	 */
	public IPropertyChangeListener getPropertyChangeListener() {
		return propertyChangeListener;
	}

	/**
	 * @param propertyChangeListener the propertyChangeListener to set
	 */
	public void setPropertyChangeListener(IPropertyChangeListener propertyChangeListener) {
		this.propertyChangeListener = propertyChangeListener;
	}

	/**
	 * @return the page
	 */
	public DialogPage getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(DialogPage page) {
		this.page = page;
	}
	
    /**
	 * @return the control
	 */
	public C getControl() {
		return control;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
