/**
 * 
 */
package org.goko.controller.grbl.v08.featureset;

import java.util.ArrayList;
import java.util.List;

import org.goko.controller.grbl.v08.GrblControllerService;
import org.goko.controller.grbl.v08.IGrblControllerService;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IStepJogService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.core.gcode.service.IGCodeExecutionMonitorService;
import org.goko.core.gcode.service.IGCodeService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;

/**
 * @author PsyKo
 *
 */
public class GrblFeatureSet implements IFeatureSet {
	/** Target board definition for this feature set */
	public static final TargetBoard GRBL_TARGET_BOARD = new TargetBoard("grbl.v08", "Grbl v0.8 (beta)");
	public List<ServiceRegistration> lstServiceregistration;
	
	public GrblFeatureSet() {
		lstServiceregistration = new ArrayList<ServiceRegistration>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#getTargetBoard()
	 */
	@Override
	public TargetBoard getTargetBoard() {
		return GRBL_TARGET_BOARD;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws GkException {
		GrblControllerService service = new GrblControllerService();
		
		lstServiceregistration.add( context.registerService(IControllerService.class,	 	service, null));
		lstServiceregistration.add(context.registerService(IGrblControllerService.class, 	service, null));
		lstServiceregistration.add(context.registerService(IJogService.class, 			service, null));
		lstServiceregistration.add(context.registerService(IStepJogService.class, 			service, null));
		lstServiceregistration.add(context.registerService(IThreeAxisControllerAdapter.class, service, null));
		lstServiceregistration.add(context.registerService(ICoordinateSystemAdapter.class, service, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileExporter.class, service, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileImporter.class, service, null));
	
		service.setConnectionService(findService(context, IConnectionService.class));
		service.setGCodeService(findService(context, IGCodeService.class));
		service.setEventAdmin(findService(context, EventAdmin.class));
		service.setApplicativeLogService(findService(context, IApplicativeLogService.class));
		service.setMonitorService(findService(context, IGCodeExecutionMonitorService.class));
		
		service.start();
		
	}

	protected <S> S findService( BundleContext context, Class<S> clazz){
		ServiceReference<S> ref = context.getServiceReference(clazz);
		if(ref != null){
			return context.getService(ref);
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#stop()
	 */
	@Override
	public void stop() throws GkException {
		for (ServiceRegistration serviceRegistration : lstServiceregistration) {
			serviceRegistration.unregister();
		}
	}
}
