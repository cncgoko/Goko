/*******************************************************************************
 * Goko - Copyright (C) 2016  PsyKo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
/**
 *
 */
package org.goko.controller.grbl.v11;

import java.util.ArrayList;
import java.util.List;

import org.goko.controller.grbl.commons.IGrblControllerService;
import org.goko.controller.grbl.commons.IGrblOverrideService;
import org.goko.controller.grbl.v11.configuration.GrblConfiguration;
import org.goko.core.common.applicative.logging.IApplicativeLogService;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionService;
import org.goko.core.controller.IControllerConfigurationFileExporter;
import org.goko.core.controller.IControllerConfigurationFileImporter;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.ICoordinateSystemAdapter;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.IJogService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.execution.IGCodeExecutionTimeService;
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.service.IExecutionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * @author PsyKo
 *
 */
public class Grblv11eatureSet implements IFeatureSet {
	/** Target board definition for this feature set */
	public static final TargetBoard GRBL_TARGET_BOARD = new TargetBoard("grbl.v11", "Grbl v1.1 (beta)");
	public List<ServiceRegistration> lstServiceregistration;

	public Grblv11eatureSet() {
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
		GrblState grblState = new GrblState();
		GrblConfiguration cfg = new GrblConfiguration();
		GrblControllerService grblControllerService = new GrblControllerService(grblState, cfg);
		GrblCommunicator communicator = new GrblCommunicator(grblControllerService);
		grblControllerService.setCommunicator(communicator);

		context.registerService(IControllerService.class,	 	grblControllerService, null);
		lstServiceregistration.add(context.registerService(IGrblControllerService.class, 	grblControllerService, null));
		lstServiceregistration.add(context.registerService(IJogService.class, 				grblControllerService, null));		
		lstServiceregistration.add(context.registerService(IGrblOverrideService.class, 		grblControllerService, null));
		lstServiceregistration.add(context.registerService(IThreeAxisControllerAdapter.class, grblControllerService, null));
		lstServiceregistration.add(context.registerService(ICoordinateSystemAdapter.class, grblControllerService, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileExporter.class, grblControllerService, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileImporter.class, grblControllerService, null));
		lstServiceregistration.add(context.registerService(IGCodeContextProvider.class, grblControllerService, null));
		lstServiceregistration.add(context.registerService(IProbingService.class, grblControllerService, null));
		
		grblControllerService.setGCodeService(findService(context, IRS274NGCService.class));
		grblControllerService.setGcodeExecutionTimeService(findService(context, IGCodeExecutionTimeService.class));		
		communicator.setConnectionService(findService(context, IConnectionService.class));
		grblControllerService.setApplicativeLogService(findService(context, IApplicativeLogService.class));
		communicator.setApplicativeLogService(findService(context, IApplicativeLogService.class));
		grblControllerService.setExecutionService(findService(context, IExecutionService.class));

		grblControllerService.start();

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
