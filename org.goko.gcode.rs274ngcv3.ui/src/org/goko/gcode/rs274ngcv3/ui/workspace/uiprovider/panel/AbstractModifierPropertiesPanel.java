package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.goko.common.GkUiComponent;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;

public class AbstractModifierPropertiesPanel<M extends IModifier<GCodeProvider>,C extends AbstractModifierPanelController<D, M>, D extends AbstractModifierModelObject> extends GkUiComponent<C, D> implements IModifierPropertiesPanel<M>{
	
	/**
	 * Constructor
	 * @param context 
	 * @param abstractController
	 */
	public AbstractModifierPropertiesPanel(IEclipseContext context, C abstractController) {
		super(context, abstractController);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IPropertiesPanel#beforeDiscard()
	 */
	@Override
	public void beforeDiscard() throws GkException {
		getController().performUpdateModifier();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.bean.IPropertiesPanel#discard()
	 */
	@Override
	public void discard() throws GkException {
		
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel#setModifier(org.goko.core.gcode.rs274ngcv3.element.IModifier)
	 */
	@Override
	public void setModifier(M modifier) {
		getController().setModifier(modifier);
	}

	/** (inheritDoc)
	 * @see org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.panel.IModifierPropertiesPanel#initializeFromModifier()
	 */
	@Override
	public void initializeFromModifier() throws GkException {
		getController().setLockModifierUpdateOnPropertyChange(true);
		getController().initializeFromModifier(); 
		getController().setLockModifierUpdateOnPropertyChange(false);
	}

}

