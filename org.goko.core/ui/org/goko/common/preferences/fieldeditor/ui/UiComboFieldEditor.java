package org.goko.common.preferences.fieldeditor.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * Field editor using a combo control
 * @author PsyKo
 */
public class UiComboFieldEditor<T> extends UiLabeledFieldEditor<Combo> {	
    /**
     * Text limit constant (value <code>-1</code>) indicating unlimited
     * text limit and width.
     */
    public static int UNLIMITED = -1;
    /**
     * Width of text field in characters; initially unlimited.
     */
    private int widthInChars = UNLIMITED;
	private String inputPropertyName;
	private GkCombo<LabeledValue<T>> gkCombo;

	public UiComboFieldEditor(Composite parent, int style) {
		super(parent, style);
	}
	
	public void setEntry(List<LabeledValue<T>> entries){
		gkCombo.setInput(entries);
	}
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.preference.PreferenceFieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {
		super.createControls(parent, style); 
		gkCombo = new GkCombo<>(this, style | SWT.READ_ONLY);
		control = gkCombo.getCombo();//new Combo(this, style & SWT.READ_ONLY);    	
		control.setLayoutData(new GridData(SWT.LEFT, SWT.RIGHT, true, false, 1, 1));
    	control.pack();
    	this.pack();
	}

	/** (inheritDoc)
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Object value) {
		return ValidationStatus.OK_STATUS;		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getFieldEditorBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	protected Binding getFieldEditorBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		throw new GkTechnicalException("Should not be called");
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.ui.UiFieldEditor#getBinding(org.eclipse.core.databinding.DataBindingContext, org.goko.common.bindings.AbstractModelObject)
	 */
	@Override
	public List<Binding> getBinding(DataBindingContext bindingContext, AbstractModelObject modelObject) throws GkException {
		List<Binding> bindings = new ArrayList<>();
		// Selection binding
		IObservableValue target = ViewersObservables.observeSingleSelection(gkCombo);
		IObservableValue model = BeanProperties.value(propertyName).observe(modelObject);
		
		UpdateValueStrategy targetToModelStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		UpdateValueStrategy modelToTargetStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		
		bindings.add(bindingContext.bindValue(target, model, targetToModelStrategy, modelToTargetStrategy));
		
		// AvailableItems binding
		if(StringUtils.isNotEmpty(inputPropertyName)){
			IViewerObservableValue itemsObserveWidget = ViewerProperties.input().observe(gkCombo);
			IObservableValue bindingsObserveList = BeanProperties.value(inputPropertyName).observe(modelObject);
			Binding binding = bindingContext.bindValue(itemsObserveWidget, bindingsObserveList, targetToModelStrategy, modelToTargetStrategy );
			bindings.add(binding);
		}		
		return bindings;
	}

	/**
	 * @return the inputPropertyName
	 */
	public String getInputPropertyName() {
		return inputPropertyName;
	}

	/**
	 * @param inputPropertyName the inputPropertyName to set
	 */
	public void setInputPropertyName(String inputPropertyName) {
		this.inputPropertyName = inputPropertyName;
	}
	
	private void updateControlLayout(){
		GridData gd = (GridData) control.getLayoutData();		
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
	
}
