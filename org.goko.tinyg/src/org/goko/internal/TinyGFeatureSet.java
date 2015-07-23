package org.goko.internal;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IContinuousJogService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IProbingService;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.tinyg.controller.ITinygControllerService;
import org.goko.tinyg.controller.TinyGControllerService;
import org.osgi.framework.BundleContext;

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
		TinyGControllerService service = new TinyGControllerService();
		// ITinygControllerService extends IControllerService, IProbingService, IFourAxisControllerAdapter, ICoordinateSystemAdapter, IContinuousJogService
		context.registerService(IControllerService.class, service, null);
		context.registerService(ITinygControllerService.class, service, null);		
		context.registerService(IProbingService.class, service, null);
		context.registerService(IFourAxisControllerAdapter.class, service, null);
		context.registerService(ICoordinateSystemAdapter.class, service, null);
		context.registerService(IContinuousJogService.class, service, null);
		
		service.start();
	}

	/** (inheritDoc)
	 * @see org.goko.core.feature.IFeatureSet#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub
		
	}	
}
