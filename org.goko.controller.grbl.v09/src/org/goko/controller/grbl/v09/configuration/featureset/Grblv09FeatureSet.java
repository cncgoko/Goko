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
package org.goko.controller.grbl.v09.configuration.featureset;

import java.util.ArrayList;
import java.util.List;

import org.goko.controller.grbl.v09.GrblControllerService;
import org.goko.controller.grbl.v09.IGrblControllerService;
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
import org.goko.core.feature.IFeatureSet;
import org.goko.core.feature.TargetBoard;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.gcode.service.IExecutionService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventAdmin;

/**
 * @author PsyKo
 *
 */
public class Grblv09FeatureSet implements IFeatureSet {
	/** Target board definition for this feature set */
	public static final TargetBoard GRBL_TARGET_BOARD = new TargetBoard("grbl.v09", "Grbl v0.9 (beta)");
	public List<ServiceRegistration> lstServiceregistration;

	public Grblv09FeatureSet() {
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

		context.registerService(IControllerService.class,	 	service, null);
		lstServiceregistration.add(context.registerService(IGrblControllerService.class, 	service, null));
		lstServiceregistration.add(context.registerService(IJogService.class, 			service, null));
		lstServiceregistration.add(context.registerService(IJogService.class, 			service, null));
		lstServiceregistration.add(context.registerService(IThreeAxisControllerAdapter.class, service, null));
		lstServiceregistration.add(context.registerService(ICoordinateSystemAdapter.class, service, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileExporter.class, service, null));
		lstServiceregistration.add(context.registerService(IControllerConfigurationFileImporter.class, service, null));
		lstServiceregistration.add(context.registerService(IGCodeContextProvider.class, service, null));
		lstServiceregistration.add(context.registerService(IProbingService.class, service, null));
		
		service.setGCodeService(findService(context, IRS274NGCService.class));
		service.setConnectionService(findService(context, IConnectionService.class));
		service.setEventAdmin(findService(context, EventAdmin.class));
		service.setApplicativeLogService(findService(context, IApplicativeLogService.class));
		service.setMonitorService(findService(context, IExecutionService.class));

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
