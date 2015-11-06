package org.goko.common.preferences.fieldeditor.ui;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;

/**
 * A field editor for a string type preference.
 * <p>
 * This class may be used as is, or subclassed as required.
 * </p>
 */
public class UiStringFieldEditor extends UiLabeledFieldEditor<Text> {
    /**
     * Text limit constant (value <code>-1</code>) indicating unlimited
     * text limit and width.
     */
    public static int UNLIMITED = -1;
    /**
     * Width of text field in characters; initially unlimited.
     */
    private int widthInChars = UNLIMITED;

    /**
     * Text limit of text field in characters; initially unlimited.
     */
    private int textLimit = UNLIMITED;

    /**
     * The error message, or <code>null</code> if none.
     */
    private String errorMessage = "Error";
    /** 
     * The valid state of this control
     */
    private boolean isValid;
    /**
     * The old valid value
     */
    private String oldValue;
    /**
     * Indicates whether the empty string is legal;
     * <code>true</code> by default.
     */
    private boolean emptyStringAllowed = true;
	
	public UiStringFieldEditor(Composite parent, int style) {
		super(parent, style);
		//createControls(parent, style);
		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		control = new Text(this, SWT.BORDER |  style);    	
    	control.setText("Text");
    	control.setLayoutData(new GridData(SWT.LEFT, SWT.RIGHT, true, false, 1, 1));
    	control.addKeyListener(new KeyAdapter() {
    		@Override
    		public void keyReleased(KeyEvent e) {    			
    			valueChanged();
    		}
		});
    	control.pack();
    	this.pack();
	}
    /**
     * Informs this field editor's listener, if it has one, about a change
     * to the value (<code>VALUE</code> property) provided that the old and
     * new values are different.
     * <p>
     * This hook is <em>not</em> called when the text is initialized 
     * (or reset to the default value) from the preference store.
     * </p>
     */
	protected void valueChanged() {		
        boolean oldState = isValid;
        refreshValidState();

        if (isValid != oldState) {
			fireStateChanged(IS_VALID, oldState, isValid);
		}

        String newValue = getText();
        if (!StringUtils.equals(newValue, oldValue)) {
            fireValueChanged(VALUE, oldValue, newValue);
            oldValue = newValue;
        }
	}
	
	/**
	 * Update the valid state of this input
	 */
	protected void refreshValidState() {
		isValid = true;
		if(textLimit != UNLIMITED && StringUtils.length(getText()) > textLimit){
			isValid = false;
		}
		
		if( !emptyStringAllowed && StringUtils.isBlank(getText())){
			isValid = false;
		}
		
		isValid = isValid && isValidValue();
	}

	/**
	 * Allow verification in subclass
	 * @return <code>true</code> if the input is valid, <code>false</code> otherwise
	 */
	protected boolean isValidValue() {
		return true;
	}

	public String getValue(){
		return oldValue;
	}
	
	protected String getText(){
		return getControl().getText();
	}

	/**
	 * @return the widthInChars
	 */
	public int getWidthInChars() {
		return widthInChars;
	}

	/**
	 * @param widthInChars the widthInChars to set
	 */
	public void setWidthInChars(int widthInChars) {
		this.widthInChars = widthInChars;
		updateControlLayout();
	}

	private void updateControlLayout(){
		GridData gd = new GridData(SWT.LEFT, SWT.RIGHT, true, false, 1, 1);		
		if (widthInChars != UNLIMITED) {
            GC gc = new GC(control);
            try {
                Point extent = gc.textExtent("X");//$NON-NLS-1$
                gd.widthHint = widthInChars * extent.x;
            } finally {
                gc.dispose();
            }
        } 
		control.setLayoutData(gd);
		pack();
	}
	/**
	 * @return the textLimit
	 */
	public int getTextLimit() {
		return textLimit;
	}

	/**
	 * @param textLimit the textLimit to set
	 */
	public void setTextLimit(int textLimit) {
		this.textLimit = textLimit;
	}

	/**
	 * @return the emptyStringAllowed
	 */
	public boolean isEmptyStringAllowed() {
		return emptyStringAllowed;
	}

	/**
	 * @param emptyStringAllowed the emptyStringAllowed to set
	 */
	public void setEmptyStringAllowed(boolean emptyStringAllowed) {
		this.emptyStringAllowed = emptyStringAllowed;
        boolean oldState = isValid;
		refreshValidState();

        if (isValid != oldState) {
			fireStateChanged(IS_VALID, oldState, isValid);
		}
	}

	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		IObservableValue widgetObserver = WidgetProperties.text(SWT.Modify).observe(getControl());
		IObservableValue modelObserver  = BeanProperties.value(propertyName).observe(modelObject);
		
		UpdateValueStrategy targetToModelStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		targetToModelStrategy.setBeforeSetValidator(this);
		
		UpdateValueStrategy modelToTargetStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		//modelToTargetStrategy.setBeforeSetValidator(this);
		
		if(getConverter() != null){
			if(getConverter().getTargetToModelConverter() != null){
				targetToModelStrategy.setConverter(getConverter().getTargetToModelConverter());				
			}
			if(getConverter().getModelToTargetConverter() != null){
				modelToTargetStrategy.setConverter(getConverter().getModelToTargetConverter());				
			}
		}
		Binding bindValue = bindingContext.bindValue(widgetObserver, modelObserver, targetToModelStrategy, modelToTargetStrategy);
		
		// add some decorations to the control
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT); 
		
		return bindValue;
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#isValid()
	 */
	@Override
	public boolean isValid() {
		return isValid;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		refreshValidState();
		if(isValid){
			return ValidationStatus.OK_STATUS;
		}
		return ValidationStatus.error(errorMessage);
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public IBindingConverter getConverter(){
		return null;
	}
}
