/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.common.bindings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.internal.databinding.BindingStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.goko.common.bindings.converters.NegateBooleanConverter;
import org.goko.common.bindings.validator.StringRealNumberValidator;
import org.goko.common.elements.combo.GkCombo;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.event.EventDispatcher;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;

public abstract class AbstractController<T extends AbstractModelObject> extends EventDispatcher{
	private DataBindingContext bindingContext;
	private T dataModel;
	private List<Binding> bindings;

	public AbstractController(T binding) {
		bindingContext = new DataBindingContext();
		this.dataModel = binding;
		this.bindings = new ArrayList<Binding>();
	}

	/**
	 * Initialisation method
	 * @throws GkException GkException
	 */
	public abstract void initialize() throws GkException;

	public void addBigDecimalModifyBinding(Object source, String property) throws GkException{
		addTextModifyBinding(source, property,new StringRealNumberValidator());
	}

	/**
	 * Binding between an object and a property, based on text modification
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addTextModifyBinding(Object source, String property) throws GkException{
		addTextModifyBinding(source, property,null);
	}

	public void addTextModifyBinding(Object source, String property, IValidator validator) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue widgetObserver = WidgetProperties.text(SWT.Modify).observe(source);
		IObservableValue modelObserver  = BeanProperties.value(property).observe(dataModel);
		UpdateValueStrategy updateStrategy = new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		if(validator != null){
			updateStrategy.setBeforeSetValidator(validator);

		}
		Binding binding = bindingContext.bindValue(widgetObserver, modelObserver, updateStrategy, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		bindings.add(binding);
		if(validator != null){
			ControlDecorationSupport.create(binding, SWT.RIGHT | SWT.TOP);
		}
	}
	/**
	 * Binding between an source and a property to change the text of the source
	 * @param source the widget use to display the text
	 * @param property the property name
	 * @throws GkException GkException
	 */
	public void addTextDisplayBinding(Object source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue widgetObserver = WidgetProperties.text().observe(source);
		IObservableValue modelObserver  = BeanProperties.value(property).observe(dataModel);

		Binding binding = bindingContext.bindValue(widgetObserver, modelObserver, null, new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
		bindings.add(binding);
	}
	/**
	 * Binding between an object and a property, based on items list
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addItemsBinding(Object source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);
		IObservableList itemsComboSerialPortObserveWidget = WidgetProperties.items().observe(source);
		IObservableList comPortListBindingsObserveList = BeanProperties.list(property).observe(dataModel);
		Binding binding = bindingContext.bindList(itemsComboSerialPortObserveWidget, comPortListBindingsObserveList, null, null);
		bindings.add(binding);
	}

	/**
	 * Binding between an GkCombo and a property, based on items list
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addItemsBinding(GkCombo<?> source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		ObservableListContentProvider listContentProvider = new ObservableListContentProvider();
		IObservableMap observeMap = PojoObservables.observeMap(listContentProvider.getKnownElements(), LabeledValue.class, "label");
		source.setLabelProvider(new ObservableMapLabelProvider(observeMap));
		source.setContentProvider(listContentProvider);

		source.setInput(BeanProperties.list(property).observe(dataModel));

	}

	/**
	 * Binding between an object and a property, based on item selection
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addItemSelectionBinding(ComboViewer source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue target = ViewersObservables.observeSingleSelection(source);
		IObservableValue model = BeansObservables.observeValue(dataModel, property);

		Binding binding = bindingContext.bindValue(target, model,null,null);
		bindings.add(binding);
	}


	public void addSelectionBinding(Object target, String property) throws GkException {
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue observeSelectionBtnCheckButtonObserveWidget = WidgetProperties.selection().observe(target);
		IObservableValue enabledBindingsObserveValue = BeanProperties.value(property).observe(dataModel);
		bindingContext.bindValue(observeSelectionBtnCheckButtonObserveWidget, enabledBindingsObserveValue, null, null);

	}

	public void addPropertyBinding(Object target, String targetProperty, String modelProperty) throws GkException {
		verifyGetter(dataModel,modelProperty);
		verifySetter(dataModel,modelProperty);

		verifyGetter(target,targetProperty);
		verifySetter(target,targetProperty);

		IObservableValue observeSelectionBtnCheckButtonObserveWidget = PojoObservables.observeValue(target,targetProperty);
		IObservableValue enabledBindingsObserveValue = BeanProperties.value(modelProperty).observe(dataModel);
		bindingContext.bindValue(observeSelectionBtnCheckButtonObserveWidget, enabledBindingsObserveValue);

	}
	/**
	 * Enables/disable the source when the property is respectively true/false
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addEnableBinding(Widget source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue target = WidgetProperties.enabled().observe(source);
		IObservableValue model = BeansObservables.observeValue(dataModel, property);

		Binding binding = bindingContext.bindValue(target, model);
		bindings.add(binding);
	}

	/**
	 * Enables/disable the source when the property is respectively true/false
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addEnableBinding(ComboViewer source, String property) throws GkException{
		addEnableBinding(source.getCombo(),  property);
	}

	/**
	 * Enables/disable the source when the property is respectively false/true
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addEnableReverseBinding(Widget source, String property) throws GkException{
		verifyGetter(dataModel,property);
		verifySetter(dataModel,property);

		IObservableValue target = WidgetProperties.enabled().observe(source);
		IObservableValue model = BeanProperties.value(property).observe(dataModel);

		UpdateValueStrategy strategy = new UpdateValueStrategy();
		strategy.setConverter(new NegateBooleanConverter());

		Binding binding = bindingContext.bindValue(target, model,new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER), strategy);
		bindings.add(binding);
	}
	/**
	 * Enables/disable the source when the property is respectively false/true
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addEnableReverseBinding(ComboViewer source, String property) throws GkException{
		addEnableReverseBinding(source.getCombo(),  property);
	}
	/**
	 * Binding between a button and a property, based on selection
	 * @param source the UI object
	 * @param property the name of the property
	 * @throws GkException GkException
	 */
	public void addCheckboxSelectionBinding(Button button, String property) {
		IObservableValue widgetValue = WidgetProperties.selection().observe(button);
		IObservableValue modelValue = BeansObservables.observeValue(dataModel, property);
		Binding binding = bindingContext.bindValue(widgetValue, modelValue);
		bindings.add(binding);
	}
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
		String getterName = "get"+ StringUtils.upperCase(firstLetter)+otherLetters;
		try {
			return source.getClass().getDeclaredMethod(getterName);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new GkTechnicalException("Cannot find getter (looking for '"+getterName+"') for property '"+property+"' on object "+source.getClass());
		}
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
		try {
			Method getMethod = verifyGetter(source, property);
			source.getClass().getDeclaredMethod(setterName, getMethod.getReturnType());
		} catch (NoSuchMethodException | SecurityException e) {
			throw new GkTechnicalException("Cannot find setter (looking for '"+setterName+"') for property '"+property+"' on object "+source.getClass());
		}
	}

	/**
	 * Validates
	 * @return
	 * @throws GkException
	 */
	public boolean validate() throws GkException{
		List<BindingStatus> lstStatus = new ArrayList<BindingStatus>();
		for(Binding binding : bindings){
			IObservableValue status = binding.getValidationStatus();
			if(status != null){
				BindingStatus statusValue = (BindingStatus) status.getValue();
				if(statusValue.getSeverity() == BindingStatus.ERROR){
					lstStatus.add(statusValue);
				}
			}
		}
		getDataModel().setValidationMessages(lstStatus);
		return CollectionUtils.isEmpty(lstStatus);
	}

	protected void notifyException(Exception e){
		if(e instanceof GkFunctionalException){
			notifyWarning(((GkFunctionalException)e).getLocalizedMessage());
			return;
		}
		notifyError(e);
	}
	/**
	 * display an error dialog
	 * @param message the exception to display
	 */
	private void notifyError(Exception e){
		notifyListeners(new ErrorEvent(e,"DEFAULT"));
	}
	/**
	 * display a warning dialog
	 * @param message the message to display
	 */
	private void notifyWarning(String message){
		notifyListeners(new WarningEvent(message));
	}
	/**
	 * Adds binding for the header displaying validation messages
	 * @param source the label in which the errors will be displayed
	 * @throws GkException GkException
	 */
	public void addValidationMessagesBinding(final CLabel source) throws GkException {

		AggregateValidationStatus aggValidationStatus = new AggregateValidationStatus(bindingContext.getBindings(), AggregateValidationStatus.MAX_SEVERITY);
		aggValidationStatus.addChangeListener(new IChangeListener() {

			@Override
			public void handleChange(ChangeEvent event) {
				StringBuffer errorBuffer = new StringBuffer();
				source.setText(StringUtils.EMPTY);
				source.setVisible(false);
				 for (Object o : bindingContext.getBindings()) {
				        Binding binding = (Binding) o;
				        IStatus status = (IStatus) binding .getValidationStatus().getValue();
				        Control control = null;
				        if (binding.getTarget() instanceof ISWTObservable) {
				          ISWTObservable swtObservable = (ISWTObservable) binding.getTarget();
				          control = (Control) swtObservable.getWidget();
				        }
				        if (!status.isOK()) {
				        	if(StringUtils.isNotEmpty(status.getMessage())){
					        	errorBuffer.append(status.getMessage());
					        	errorBuffer.append(System.lineSeparator());
					        	source.setText(errorBuffer.toString().trim());
					        	source.setVisible(true);
					        	return;
				        	}
				        }
				 }

			}
		});
		bindingContext.bindValue(SWTObservables.observeText(source),
				aggValidationStatus, null, null);

	}

	/**
	 * @return the bindings
	 */
	public T getDataModel() {
		return dataModel;
	}

	/**
	 * @param bindings the bindings to set
	 */
	protected void setDataModel(T bindings) {
		this.dataModel = bindings;
	}

	public void addBinding(Binding binding){
		bindings.add(binding);
	}

	/**
	 * @return the bindings
	 */
	public List<Binding> getBindings() {
		return bindings;
	}

	/**
	 * @param bindings the bindings to set
	 */
	public void setBindings(List<Binding> bindings) {
		this.bindings = bindings;
	}

	/**
	 * @return the bindingContext
	 */
	public DataBindingContext getBindingContext() {
		return bindingContext;
	}

	/**
	 * @param bindingContext the bindingContext to set
	 */
	public void setBindingContext(DataBindingContext bindingContext) {
		this.bindingContext = bindingContext;
	}
}
