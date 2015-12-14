package org.goko.common.elements.combo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.goko.common.elements.combo.v2.GkComboContentProvider;

public class GkCombo3<T> extends ComboViewer {
   
    public GkCombo3(Composite parent, int style, String labelField) {
        super(parent, style);
       
        setContentProvider(new GkComboContentProvider());
        setLabelProvider(new GkIntrospectionLabelProvider<T>(labelField));
    }
   
    public List<T> getValues(){       
        return (List<T>) getInput();
    }
   
    public final void setInput(List<T> values) {
        super.setInput(values);
    }
   
    public void setSelectedValue(T value){
        setSelection(new StructuredSelection(value));
    }
   
    public T getSelectedValue(){
        IStructuredSelection selection = (IStructuredSelection) super.getSelection();
        return (T) selection.getFirstElement();
    }
}


class GkIntrospectionLabelProvider<T> extends LabelProvider {
    private String labelMethod;
   
    public GkIntrospectionLabelProvider(String labelMethod) {
        this.labelMethod = labelMethod;
    }
   
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {       
        String label = StringUtils.EMPTY;
        try {
            Method method = element.getClass().getMethod(labelMethod);
            label = String.valueOf( method.invoke(element) );
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return label;
    }
}