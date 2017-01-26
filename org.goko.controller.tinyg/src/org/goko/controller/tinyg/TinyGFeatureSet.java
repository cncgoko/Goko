package org.goko.controller.tinyg;

import org.goko.controller.tinyg.controller.ITinygControllerService;
import org.goko.controller.tinyg.controller.TinyGCommunicator;
import org.goko.controller.tinyg.controller.TinyGControllerService;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.serial.ISerialConnectionService;
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.IWorkVolumeProvider;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.service.IExecutionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;

/**
 * TinyG V0.97 feature set 
 * 
 * @author PsyKo
 *
 */
public class TinyGFeatureSet implements IFeatureSet {
	/** Target board definition for this feature set */
	private static final TargetBoard TINYG_TARGET_BOARD = new TargetBoard("tinyg.v097", "TinyG v0.97");
				
	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#getTargetBoard()
	 */
	@Override
	public TargetBoard getTargetBoard() {
		return TINYG_TARGET_BOARD;
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws GkException {
		TinyGCommunicator communicator = new TinyGCommunicator();
		communicator.setConnectionService(findService(context, ISerialConnectionService.class));
		
		TinyGControllerService service = new TinyGControllerService(communicator);
		// ITinygControllerService extends IControllerService, IProbingService, IFourAxisControllerAdapter, ICoordinateSystemAdapter, IContinuousJogService
		context.registerService(IControllerService.class, service, null);
		context.registerService(ITinygControllerService.class, service, null);		
		context.registerService(IProbingService.class, service, null);
		context.registerService(IFourAxisControllerAdapter.class, service, null);
		context.registerService(ICoordinateSystemAdapter.class, service, null);
		context.registerService(IJogService.class, service, null);
		context.registerService(IJogService.class, service, null);		
		context.registerService(IWorkVolumeProvider.class, service, null);		
		context.registerService(IGCodeContextProvider.class, service, null);		
		context.registerService(IControllerConfigurationFileExporter.class, service, null);		
		context.registerService(IControllerConfigurationFileImporter.class, service, null);		
				
		service.setGCodeService(findService(context, IRS274NGCService.class));
		service.setEventAdmin(findService(context, EventAdmin.class));		
		service.setExecutionService(findService(context, IExecutionService.class));		
		service.setApplicativeLogService(findService(context, IApplicativeLogService.class));
		
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
		
	}	
}
