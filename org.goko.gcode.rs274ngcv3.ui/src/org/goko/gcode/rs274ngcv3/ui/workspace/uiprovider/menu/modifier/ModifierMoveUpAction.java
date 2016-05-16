package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;

public class ModifierMoveUpAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ModifierMoveUpAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** Target modifier */
	private Integer idModifier;

	public ModifierMoveUpAction(IRS274NGCService rs274Service, Integer idModifier) {
		super("Move up");
		this.idModifier = idModifier;
		this.rs274Service = rs274Service;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(idModifier);
			return modifier.getOrder() > 0;
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	
	/**
	 * (inheritDoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			IModifier<GCodeProvider> modifier = rs274Service.getModifier(idModifier);
			rs274Service.setModifierOrder(modifier, modifier.getOrder() - 1);			
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
    /** (inheritDoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
    	Image image = ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/menu/move-modifier-up.png");	        	
    	return ImageDescriptor.createFromImage(image);
    }
}
