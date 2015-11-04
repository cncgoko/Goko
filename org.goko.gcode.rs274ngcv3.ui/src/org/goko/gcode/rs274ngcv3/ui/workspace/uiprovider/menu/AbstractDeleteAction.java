package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

public abstract class AbstractDeleteAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(AbstractDeleteAction.class);
	/** Id of the target element */
	private Integer idTarget;
	
	public AbstractDeleteAction(Integer idTarget) {
		super("Delete");
		this.idTarget = idTarget;
	}
	
    /** (inheritDoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
    	Image image = ResourceManager.getPluginImage("org.goko.core.workspace", "icons/menu/cross.png");	        	
    	return ImageDescriptor.createFromImage(image);
    }

    /** (inheritDoc)
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
    	try {
			deleteById(idTarget);
		} catch (GkException e) {
			LOG.error(e);
		}
    }
    	
    protected abstract void deleteById(Integer id) throws GkException;

    
}
