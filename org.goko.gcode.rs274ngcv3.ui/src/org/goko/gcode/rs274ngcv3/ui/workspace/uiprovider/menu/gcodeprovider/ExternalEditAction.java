/**
 * 
 */
package org.goko.gcode.rs274ngcv3.ui.workspace.uiprovider.menu.gcodeprovider;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.URIUtil;
import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.element.source.ResourceLocationGCodeSource;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;
import org.goko.core.workspace.io.IResourceLocation;
import org.goko.core.workspace.io.URIResourceLocation;
import org.goko.core.workspace.service.IWorkspaceService;

/**
 * @author PsyKo
 * @date 22 nov. 2015
 */
public class ExternalEditAction extends Action{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ExternalEditAction.class);	
	/** Target GCode provider */
	private Integer idGCodeProvider;
	/** IRS274NGCService */
	private IGCodeProviderRepository gcodeProviderRepository;
	/** IWorkspaceService  */
	private IWorkspaceService workspaceService;
	/**
	 * Constructor
	 * @param gcodeProviderRepository the {@link IGCodeProviderRepository}
	 * @param idGCodeProvider the target GCodeProvider id
	 */
	public ExternalEditAction(IGCodeProviderRepository gcodeProviderRepository, IWorkspaceService workspaceService, Integer idGCodeProvider) {
		super("Edit");
		this.idGCodeProvider = idGCodeProvider;
		this.gcodeProviderRepository = gcodeProviderRepository;
		this.workspaceService = workspaceService; 
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {		
		try {
			IGCodeProvider gcode = gcodeProviderRepository.getGCodeProvider(idGCodeProvider);
			if(gcode.getSource() instanceof ResourceLocationGCodeSource){
				ResourceLocationGCodeSource source = (ResourceLocationGCodeSource) gcode.getSource();
				IResourceLocation resourceLocation = source.getResourceLocation();
				if(resourceLocation instanceof URIResourceLocation){
					URIResourceLocation uriResourceLocation = (URIResourceLocation) resourceLocation;		
					if(URIUtil.isFileURI(uriResourceLocation.getAbsoluteUri())){
						Desktop.getDesktop().edit(new File(uriResourceLocation.getAbsoluteUri()));
					}
				}
			}
		} catch (GkException | IOException e) {
			LOG.error(e);
		}
	}
	
	/** (inheritDoc)
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		try {
			IGCodeProvider gcode = gcodeProviderRepository.getGCodeProvider(idGCodeProvider);
			if(gcode.getSource() instanceof ResourceLocationGCodeSource){
				ResourceLocationGCodeSource source = (ResourceLocationGCodeSource) gcode.getSource();
				IResourceLocation resourceLocation = source.getResourceLocation();
				if(resourceLocation instanceof URIResourceLocation){
					URIResourceLocation uriResourceLocation = (URIResourceLocation) resourceLocation;
					if(URIUtil.isFileURI(uriResourceLocation.getAbsoluteUri())){
						return true;
					}					
				}
			}
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
}
