/**
 * 
 */
package org.goko.controller.grbl.v08.featureset;

import java.util.ArrayList;
import java.util.List;

import org.goko.controller.grbl.v08.GrblControllerService;
import org.goko.controller.grbl.v08.IGrblControllerService;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IStepJogService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.core.gcode.service.IGCodeService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author PsyKo
 *
 */
public class GrblFeatureSet implements IFeatureSet {
	/** Target board definition for this feature set */
	public static final TargetBoard GRBL_TARGET_BOARD = new TargetBoard("grbl.v08", "Grbl v0.8");
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
		lstServiceregistration.add(context.registerService(IStepJogService.class, 			service, null));
		lstServiceregistration.add(context.registerService(IThreeAxisControllerAdapter.class, service, null));
		lstServiceregistration.add(context.registerService(ICoordinateSystemAdapter.class, service, null));
	
		service.setConnectionService(findService(context, IConnectionService.class));
		service.setGCodeService(findService(context, IGCodeService.class));
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