/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.common.elements.list;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.log.GkLog;

public class GkList<T> extends ListViewer {
	public static final GkLog LOG = GkLog.getLogger(GkList.class);

	public GkList(Composite parent, IObservableList list, Class<T> clazz, String labelField) {
		super(parent);
		buildListViewer(list, clazz, labelField);
	}

	protected void buildListViewer(IObservableList list, Class<T>  clazz, String labelField){
		setContentProvider(new ObservableListContentProvider());
		setInput( list );
		setLabelProvider(new BeanLabelProvider(labelField, clazz));
	}

	public GkList(Composite parent, int style) {
		super(parent, style);
	}


	public List<T> getSelectedElements() {
		List<T> selection = new ArrayList<T>();
		IStructuredSelection structuredSelection = (IStructuredSelection) getSelection();
		for (Iterator iterator = structuredSelection.iterator(); iterator.hasNext();) {
			T element = (T) iterator.next();
			selection.add(element);
		}
		return selection;
	}

	class BeanLabelProvider<T> extends LabelProvider{
		String fieldName;
		Class<T> clazz;
		Method getMethod;

		BeanLabelProvider(String fieldName, Class<T> clazz){
			this.fieldName = fieldName;
			this.clazz = clazz;
		}
		/** (inheritDoc)
		 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
		 */
		@Override
		public String getText(Object element) {
			if(element == null){
				return "";
			}
			if(element.getClass() != clazz){
				throw new IllegalArgumentException("Class does not match. Got '"+element.getClass()+"', expected '"+clazz.toString()+"'");
			}
			if(getMethod == null){
				try {
					findGetMethod();
				} catch (NoSuchFieldException e) {
					LOG.error(e);
					return null;
				} catch (SecurityException e) {
					LOG.error(e);
					return null;
				} catch (NoSuchMethodException e) {
					LOG.error(e);
					return null;
				}
			}
			String label = StringUtils.EMPTY;
			try {
				Object obj = getMethod.invoke(element);
				label = String.valueOf(obj);
			} catch (IllegalAccessException e) {
				LOG.error(e);
				return null;
			} catch (IllegalArgumentException e) {
				LOG.error(e);
				return null;
			} catch (InvocationTargetException e) {
				LOG.error(e);
				return null;
			}
			return label;
		}
		private void findGetMethod() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
			String expectedName = "get"+StringUtils.upperCase(StringUtils.substring(fieldName, 0,1))+StringUtils.substring(fieldName, 1);
			getMethod = clazz.getDeclaredMethod(expectedName);
		}

	}

}
