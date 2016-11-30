/**
 * 
 */
package org.goko.controller.grbl.v09.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.goko.controller.grbl.v09.IGrblControllerService;
import org.goko.controller.grbl.v09.configuration.GrblConfiguration;
import org.goko.controller.grbl.v09.configuration.IGrblConfigurationListener;
import org.goko.controller.grbl.v09.handlers.watcher.BufferReportConfigurationFix;
import org.goko.controller.grbl.v09.handlers.watcher.IGrblConfigurationFix;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;

/**
 * @author Psyko
 * @date 28 nov. 2016
 */
public class GrblConfigurationWatcherHandler implements IGrblConfigurationListener {
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GrblConfigurationWatcherHandler.class);
	/** The fixes to apply */
	private List<IGrblConfigurationFix> lstConfigurationFix;
	/** The target service */
	private IGrblControllerService grblControllerService;
	/** UI Sync object */
	@Inject
	private UISynchronize uiSynchronize;
	/** Parent shell */
	/** Update in progress */
	private AtomicBoolean updateInProgress;
	
	/**
	 * Constructeur
	 */
	@Inject
	public GrblConfigurationWatcherHandler(IGrblControllerService grblControllerService) {
		this.grblControllerService = grblControllerService;
		grblControllerService.addConfigurationListener(this);
		this.lstConfigurationFix = new ArrayList<IGrblConfigurationFix>();
		this.lstConfigurationFix.add(new BufferReportConfigurationFix());		
		this.updateInProgress = new AtomicBoolean(false);
	}

	/** (inheritDoc)
	 * @see org.goko.controller.grbl.v09.configuration.IGrblConfigurationListener#onConfigurationChanged(org.goko.controller.grbl.v09.configuration.GrblConfiguration)
	 */
	@Override
	public void onConfigurationChanged(GrblConfiguration configuration, String identifier) {
		try{
			if(!updateInProgress.get()){		
				
				List<IGrblConfigurationFix> lstFixToApply = new ArrayList<IGrblConfigurationFix>();
				GrblConfiguration tinyGConfiguration = grblControllerService.getConfiguration();
				
				if(CollectionUtils.isNotEmpty(lstConfigurationFix)){
					for (IGrblConfigurationFix fix : lstConfigurationFix) {
						if(fix.shouldApply(identifier, tinyGConfiguration)){
							lstFixToApply.add(fix);							
						}
					}
				}
								
				if(CollectionUtils.isNotEmpty(lstFixToApply)){
					suggestToApply(lstFixToApply);
				}
				
			}
		}catch(GkException e){
			LOG.error(e);
		}
	}
	
	/**
	 * Opens the dialog describing changes that should be made, and asking for user feedback
	 * @param lstFixToApply the list of fix to apply
	 */
	private void suggestToApply(final List<IGrblConfigurationFix> lstFixToApply){
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			uiSynchronize.asyncExec(new Runnable() {
				
				/** (inheritDoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					GrblConfigurationFixDialog dialog = new GrblConfigurationFixDialog(null, getDescription(lstFixToApply));
					int result = dialog.open();
					if(result == IDialogConstants.OK_ID){
						try {
							GrblConfiguration grblConfiguration = grblControllerService.getConfiguration();
							for (IGrblConfigurationFix fix : lstConfigurationFix) {
								if(fix.shouldApply(null, grblConfiguration)){
									fix.apply(grblConfiguration);
								}
							}						
							grblControllerService.updateConfiguration(grblConfiguration);
						} catch (GkException e) {
							LOG.error(e);
						}
					}
				}
			});
		}
	}
	
	private String[] getDescription(List<IGrblConfigurationFix> lstFixToApply){		
		if(CollectionUtils.isNotEmpty(lstFixToApply)){
			String[] arrDescriptions = new String[lstFixToApply.size()];
			int i = 0;
			for (IGrblConfigurationFix fix : lstFixToApply) {				
				arrDescriptions[i] = fix.getDescription();
				i++;
			}
			return arrDescriptions;
		}
		return null;
	}
}
