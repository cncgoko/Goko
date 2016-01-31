/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.tools.autoleveler.service;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IProbingService;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.core.viewer.service.IViewer3DService;
import org.goko.tools.autoleveler.bean.GridElevationMap;
import org.goko.tools.autoleveler.bean.IAxisElevationMap;
import org.goko.tools.autoleveler.bean.IAxisElevationPattern;
/**
 * Auto leveler service
 *
 * @author PsyKo
 *
 */
public class GkAutoLevelerService implements IAutoLevelerService{
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(GkAutoLevelerService.class);
	/** Service id */
	public static String SERVICE_ID = "org.goko.autoleveller.service.GkAutoLevellerService";
	/** Renderer service */
	private IViewer3DService viewerService;
	/** Renderer */
	private GkAutoLevelerRenderer renderer;
	/** Controller service */
	private IControllerService controllerService;
	/** Probing service */
	private IProbingService probingService;
	/** GCode service */
	private IGCodeService gcodeService;
	private IAxisElevationMap map;

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+SERVICE_ID);
		if(viewerService != null){
			renderer = new GkAutoLevelerRenderer(SERVICE_ID);
			viewerService.addRenderer(renderer);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.service.IAutoLevelerService#probe(org.goko.autoleveler.bean.GridElevationMap)
	 */
	@Override
	public void probe(GridElevationMap pattern) throws GkException{
		if(pattern == null){
			throw new GkTechnicalException("Argument must be non null");
		}
		ProbeCycleRunnable cycle = new ProbeCycleRunnable();
		map = pattern;
		cycle.setControllerService(controllerService);
		cycle.setPattern(pattern);
		cycle.setProbingService(probingService);
		cycle.setElevationMap(pattern);
		renderer.setElevationMap(pattern);
		Thread t = new Thread(cycle);
		t.start();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.service.IAutoLevelerService#apply(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public IGCodeProvider apply(IGCodeProvider gcodeProvider) throws GkException{
		List<GCodeCommand> lstCommands = gcodeProvider.getGCodeCommands();
		for (GCodeCommand gCodeCommand : lstCommands) {
			if(gCodeCommand.getType() == EnumGCodeCommandType.MOTION){
				applyOnCommand((MotionCommand) gCodeCommand);
			}
		}
		return gcodeProvider;
	}

	private void applyOnCommand(MotionCommand motionCommand)throws GkException{
		if(motionCommand.getMotionType() == EnumGCodeCommandMotionType.LINEAR){
			applyOnCommand((LinearMotionCommand)motionCommand);
		}else{
			applyOnCommand((ArcMotionCommand)motionCommand);
		}
	}

	private void applyOnCommand(LinearMotionCommand motionCommand)throws GkException{
		Tuple6b start = map.getCorrectedElevation(motionCommand.getAbsoluteStartCoordinate());
		Tuple6b end   = map.getCorrectedElevation(motionCommand.getAbsoluteEndCoordinate());
		if(start != null){
			motionCommand.setAbsoluteStartCoordinate(start);
		}
		if(end != null){
			if(motionCommand.getDistanceMode() == EnumGCodeCommandDistanceMode.RELATIVE){
				Tuple6b delta = end.subtract(start);
				delta.setX( end.getX());
				motionCommand.setCoordinates(delta);
			}else{
				motionCommand.setCoordinates(end);
			}
		}
	}
	
	private void applyOnCommand(ArcMotionCommand motionCommand)throws GkException{

	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.service.IAutoLevelerService#previewPattern(org.goko.tools.autoleveler.bean.IAxisElevationPattern)
	 */
	@Override
	public void previewPattern(IAxisElevationPattern pattern) throws GkException {
		if(viewerService != null){
			renderer.setPattern(pattern);
		}
	}

	/**
	 * @return the viewerService
	 */
	public IViewer3DService getViewerService() {
		return viewerService;
	}

	/**
	 * @param viewerService the viewerService to set
	 */
	public void setViewerService(IViewer3DService viewerService) {
		this.viewerService = viewerService;
	}

	/**
	 * @return the gcodeService
	 */
	public IGCodeService getGCodeService() {
		return gcodeService;
	}

	/**
	 * @param gcodeService the gcodeService to set
	 */
	public void setGCodeService(IGCodeService gcodeService) {
		this.gcodeService = gcodeService;
	}

	/**
	 * @return the controllerService
	 */
	public IControllerService getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(IControllerService controllerService) {
		this.controllerService = controllerService;
	}

	/**
	 * @return the probingService
	 */
	public IProbingService getProbingService() {
		return probingService;
	}

	/**
	 * @param probingService the probingService to set
	 */
	public void setProbingService(IProbingService probingService) {
		this.probingService = probingService;
	}

	/**
	 * @return the map
	 */
	public IAxisElevationMap getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(IAxisElevationMap map) {
		this.map = map;
	}

}
