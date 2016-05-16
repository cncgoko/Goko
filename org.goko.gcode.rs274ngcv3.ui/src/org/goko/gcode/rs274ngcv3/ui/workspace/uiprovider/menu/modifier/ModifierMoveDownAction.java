package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.modifier;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.element.GCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.IModifier;
import org.goko.core.log.GkLog;

public class ModifierMoveDownAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ModifierMoveDownAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** Target modifier */
	private Integer idModifier;

	public ModifierMoveDownAction(IRS274NGCService rs274Service, Integer idModifier) {
		super("Move down");
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
			List<IModifier<GCodeProvider>> lstModifier = rs274Service.getModifierByGCodeProvider(modifier.getIdGCodeProvider());
			return modifier.getOrder() < ( CollectionUtils.size(lstModifier) - 1);
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
			rs274Service.setModifierOrder(modifier, modifier.getOrder() + 1);			
		} catch (GkException e) {
			LOG.error(e);
		}
	}
	
    /** (inheritDoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
    	Image image = ResourceManager.getPluginImage("org.goko.gcode.rs274ngcv3.ui", "resources/icons/menu/move-modifier-down.png");	        	
    	return ImageDescriptor.createFromImage(image);
    }
}
