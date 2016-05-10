package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;
import org.goko.common.dialog.GkDialog;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.log.GkLog;

public class ReloadGCodeProviderAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ReloadGCodeProviderAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** Target GCode provider */
	private Integer idGCodeProvider;
	
	public ReloadGCodeProviderAction(IRS274NGCService rs274NGCService, Integer idTarget) {
		super("Reload");		
		this.rs274Service = rs274NGCService;
		this.idGCodeProvider = idTarget;
	}

    /** (inheritDoc)
     * @see org.eclipse.jface.action.Action#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor() {
    	Image image = ResourceManager.getPluginImage("org.goko.core.workspace", "icons/menu/arrow_refresh_small.png");	        	
    	return ImageDescriptor.createFromImage(image);
    }
    
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		boolean enabled = false;
		try {
			IGCodeProvider provider = rs274Service.getGCodeProvider(idGCodeProvider);
			if(provider.getSource() != null && !provider.isLocked()){
				enabled = true;
			}
		} catch (GkException e) {
			LOG.error(e);			
		}
		return enabled;
	}
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		try {
			rs274Service.reload(idGCodeProvider, new NullProgressMonitor());
		} catch (GkException e) {
			LOG.error(e);
			GkDialog.openDialog(null, e);			 
		}
	}
}
