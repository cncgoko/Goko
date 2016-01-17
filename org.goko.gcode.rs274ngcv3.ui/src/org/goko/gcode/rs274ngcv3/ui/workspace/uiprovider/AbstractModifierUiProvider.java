/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider;

import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Composite;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel;
import org.osgi.framework.FrameworkUtil;

/**
 * @author PsyKo
 * @date 16 janv. 2016
 */
public abstract class AbstractModifierUiProvider<T extends IModifier<GCodeProvider>> implements IModifierUiProvider<T>{
	private Class<T> modifierClass;
	
	/**
	 * @param modifierClass
	 */
	public AbstractModifierUiProvider(Class<T> modifierClass) {
		super();
		this.modifierClass = modifierClass;
	}
	
	/**
	 * Return the context for the UI component
	 * @return IEclipseContext
	 */
	protected IEclipseContext getContext(){		
		return EclipseContextFactory.getServiceContext(FrameworkUtil.getBundle(getClass()).getBundleContext());
	}
	
	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.IModifierUiProvider#createConfigurationPanelFor(org.eclipse.swt.widgets.Composite, org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final IModifierPropertiesPanel<T> createConfigurationPanelFor(Composite parent, IModifier<?> modifier) throws GkException {	
		if(modifier.getClass().equals(modifierClass)){
			IModifierPropertiesPanel<T> panel = createPropertiesPanelForModifier(parent, (T) modifier);
			panel.setModifier((T)modifier);
			panel.initializeFromModifier();
			return panel;
		}
		return null;
	}
	
	protected abstract IModifierPropertiesPanel<T> createPropertiesPanelForModifier(Composite parent, T modifier) throws GkException;
}
