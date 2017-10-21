package org.goko.gcode.rs274ngcv3.jogl.action;

import org.eclipse.jface.action.Action;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.rs274ngcv3.jogl.RS274NGCV3JoglService;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.RS274GCodeRenderer;
import org.goko.core.log.GkLog;

public class HideShowGCodeProviderAction extends Action {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(HideShowGCodeProviderAction.class);
	/** GCode service */
	private IRS274NGCService rs274Service;
	/** RS274NGCV3Jogl  service */
	private RS274NGCV3JoglService rs274JoglService;
	/** ID of the target provider */
	private Integer idGcodeProvider;
	
	public HideShowGCodeProviderAction(IRS274NGCService rs274Service, RS274NGCV3JoglService rs274JoglService, Integer idTarget) {
		super("Hide/Show");		
		this.rs274Service = rs274Service;
		this.rs274JoglService = rs274JoglService;
		this.idGcodeProvider = idTarget;
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.action.AbstractDeleteAction#run()
	 */
	@Override
	public void run() {
		try{
			RS274GCodeRenderer renderer = rs274JoglService.getRendererByGCodeProvider(rs274Service.getGCodeProvider(idGcodeProvider));
			if(renderer != null){
				renderer.setEnabled( !renderer.isEnabled() );
			}
		}catch(GkException e){
			LOG.error(e);
		}
	}
}
