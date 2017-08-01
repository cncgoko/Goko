/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author Psyko
 * @date 30 avr. 2016
 */
public class UiRadioGroupFieldEditor<T> extends UiLabeledFieldEditor<Composite> {
	private List<UiRadioGroupFieldEditorOption<T>> options;
	
	/**
	 * @param parent
	 * @param style
	 */
	public UiRadioGroupFieldEditor(Composite parent, int style) {
		super(parent, style);
		this.options = new ArrayList<UiRadioGroupFieldEditorOption<T>>();
		setNbColumns(1);
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return null;
	}

//	/** (inheritDoc)
//	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#createLayout(org.eclipse.swt.widgets.Composite)
//	 */
//	@Override
//	protected void createLayout(Composite parent) {
//    	GridLayout layout = new GridLayout(1, false);
//    	layout.marginHeight = 2;
//    	layout.marginWidth = 2;
//    	setLayout(layout);    
//	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiLabeledFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);    		
		control = new Composite(this, SWT.NONE);
		control.setLayout(new GridLayout(1, false));				
		control.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout layout = new GridLayout(2, false);
    	layout.marginHeight = 2;
    	layout.marginWidth = 2;
    	layout.numColumns = 1;
		control.setLayout(layout);		
		control.pack();
		this.pack();
	}
	
	private void createOptions(){
		if(CollectionUtils.isNotEmpty(options)){
			for (UiRadioGroupFieldEditorOption<T> option : options) {
				Button button = new Button(control, SWT.RADIO);
				button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
				button.setText(option.getLabel());
				option.setControl(button);
			}			
		}
		GridData gd = (GridData) labelControl.getLayoutData();
		gd.verticalAlignment = SWT.TOP;
		//labelControl.pack();
		control.pack();
		this.pack();
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getFieldEditorBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		SelectObservableValue selectedRadioButtonObservable = new SelectObservableValue();
		if(CollectionUtils.isEmpty(options)){
			throw new GkTechnicalException("No option available for UiRadioGroup ["+getPropertyName()+"]");
		}
		createOptions();
		
		for (UiRadioGroupFieldEditorOption<T> option : options) {
			selectedRadioButtonObservable.addOption(option.getValue(), WidgetProperties.selection().observe(option.getControl()));
		}
		IObservableValue modelObserver  = BeanProperties.value(propertyName).observe(modelObject);
		UpdateValueStrategy targetToModelStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);		
		UpdateValueStrategy modelToTargetStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		// bind label text to currently selected option
		return bindingContext.bindValue(selectedRadioButtonObservable, modelObserver, targetToModelStrategy, modelToTargetStrategy); 
	}

	public void addOption(String label, T value){
		this.options.add(new UiRadioGroupFieldEditorOption<T>(label, value));
	}

	/**
	 * @param nbColumns the nbColumns to set
	 */
	public void setNbColumns(int nbColumns) {
		((GridLayout)control.getLayout()).numColumns = nbColumns;
		control.layout();
	}
	
}

/**
 * Class describing a radio group option
 * @author Psyko
 */
class UiRadioGroupFieldEditorOption<T>{
	private String label;
	private T value;
	private Button control;
	
	/**
	 * @param label
	 * @param value
	 */
	public UiRadioGroupFieldEditorOption(String label, T value) {
		super();
		this.label = label;
		this.value = value;
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

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @return the control
	 */
	public Button getControl() {
		return control;
	}

	/**
	 * @param control the control to set
	 */
	public void setControl(Button control) {
		this.control = control;
	}
	
}
