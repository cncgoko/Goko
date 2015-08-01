package org.goko.common.preferences.fieldeditor.ui;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.preferences.fieldeditor.IFieldEditor;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

public abstract class UiFieldEditor<C extends Control> extends Composite implements IFieldEditor<C>, IValidator{
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
    /** The name of the preference displayed in this field editor. */
    protected String propertyName;
    /** The label's text. */
    private String label;
    /**  The associated control */
    protected C control;
	/** Listener, or <code>null</code> if none */
    private IPropertyChangeListener propertyChangeListener;

    /**
     * Constructor
     * @param parent
     * @param style
     */
    public UiFieldEditor(Composite parent, int style){
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
    
    @Override
    public void setEnabled(boolean enabled) {
    	boolean wasEnabled = isEnabled();
    	super.setEnabled(enabled);
    	if(!wasEnabled && enabled){
//    		try {
//				loadValue();
//			} catch (GkException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	}
    }
    
       
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
	
	public Binding getBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException{
		verifyGetter(modelObject,propertyName);
		verifySetter(modelObject,propertyName);

		return getFieldEditorBinding(bindingContext, modelObject);		
	}
	
	protected abstract Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException;
	
	
	/**
	 * Make sure the getter for the given property exists
	 * @param source the source object
	 * @param property the property to search for
	 * @return the {@link Method}
	 * @throws GkException GkException
	 */
	private Method verifyGetter(Object source, String property) throws GkException{
		String firstLetter = StringUtils.substring(property, 0,1);
		String otherLetters = StringUtils.substring(property, 1);

		String getGetterName = "^get"+ StringUtils.upperCase(firstLetter)+otherLetters+"$";
		String isGetterName = "^is"+ StringUtils.upperCase(firstLetter)+otherLetters+"$";

		Method[] methodArray = source.getClass().getMethods();
		for (Method method : methodArray) {
			//if(StringUtils.equals(getterName, method.getName())){
			if(method.getName().matches(getGetterName) || method.getName().matches(isGetterName)){
				return method;
			}
		}

		String getterNameDisplay = "get"+ StringUtils.upperCase(firstLetter)+otherLetters+"/is"+ StringUtils.upperCase(firstLetter)+otherLetters;
		throw new GkTechnicalException("Cannot find getter (looking for '"+getterNameDisplay+"') for property '"+property+"' on object "+source.getClass()+". Make sure it's public and correctly spelled");

	}
	/**
	 * Make sure the setter for the given property exists
	 * @param source the source object
	 * @param property the property to search for
	 * @throws GkException GkException
	 */
	private void verifySetter(Object source, String property) throws GkException{
		String firstLetter = StringUtils.substring(property, 0,1);
		String otherLetters = StringUtils.substring(property, 1);
		String setterName = "set"+ StringUtils.upperCase(firstLetter)+otherLetters;
		boolean found = false;

		Method getMethod = verifyGetter(source, property);
		Method[] methodArray = source.getClass().getMethods();
		for (Method method : methodArray) {
			if(StringUtils.equals(setterName, method.getName())){
				Class<?>[] paramsArray = method.getParameterTypes();
				if(paramsArray != null && paramsArray.length == 1 && paramsArray[0] == getMethod.getReturnType()){
					found = true;
					break;
				}

			}
		}
		//source.getClass().getDeclaredMethod(setterName, getMethod.getReturnType());

		if(!found){
			throw new GkTechnicalException("Cannot find setter (looking for '"+setterName+"') for property '"+property+"' on object "+source.getClass()+". Make sure it's public and correctly spelled");
		}
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
}
