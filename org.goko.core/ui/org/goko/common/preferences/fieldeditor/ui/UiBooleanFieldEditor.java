/**
 * 
 */
package org.goko.common.preferences.fieldeditor.ui;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.exception.GkException;

/**
 * @author Psyko
 * @date 18 oct. 2016
 */
public class UiBooleanFieldEditor extends UiFieldEditor<Button> {
	
	/**
	 * @param parent
	 * @param style
	 */
	public UiBooleanFieldEditor(Composite parent, int style) {
		super(parent, style);
		createControls(parent, style);
	}

	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		control = new Button(this, SWT.CHECK |  style);    	
    	control.setText(getLabel());
    	control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    	control.addSelectionListener(new SelectionAdapter() {
    		/** (inheritDoc)
    		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
    		 */
    		@Override
    		public void widgetSelected(SelectionEvent e) {    			
    			super.widgetSelected(e);
    			UiBooleanFieldEditor.this.fireStateChanged(getPropertyName(), !getControl().getSelection(), getControl().getSelection());
    		}
		});
    	control.pack();
    	this.pack();
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getFieldEditorBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		IObservableValue observeSelectionBtnCheckButtonObserveWidget = WidgetProperties.selection().observe(getControl());
		IObservableValue enabledBindingsObserveValue = BeanProperties.value(getPropertyName()).observe(modelObject);
		
		UpdateValueStrategy targetToModelStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		UpdateValueStrategy modelToTargetStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		
		return bindingContext.bindValue(observeSelectionBtnCheckButtonObserveWidget, enabledBindingsObserveValue, targetToModelStrategy, modelToTargetStrategy);
	}
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {		
		super.setLabel(label);
		getControl().setText(label);
	}

}
