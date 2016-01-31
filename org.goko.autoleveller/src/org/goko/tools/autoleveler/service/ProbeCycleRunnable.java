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
package org.goko.autoleveler.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.goko.autoleveler.bean.IAxisElevationMap;
import org.goko.autoleveler.bean.IAxisElevationPattern;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.IProbingService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.core.controller.bean.ProbeResult;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;

public class ProbeCycleRunnable implements Runnable {
	private static final GkLog LOG = GkLog.getLogger(ProbeCycleRunnable.class);
	private IAxisElevationPattern pattern;
	private IProbingService probingService;
	private IControllerService controllerService;
	private IAxisElevationMap elevationMap;

	@Override
	public void run() {
		try{
			if(elevationMap == null){
				throw new GkFunctionalException("No elevation map set for probing");
			}
			if(CollectionUtils.isNotEmpty(pattern.getPatternPositions())){
				List<Tuple6b> lst = pattern.getPatternPositions();
				for (Tuple6b probePositionSafe : lst) {
					LOG.info("Probing at "+probePositionSafe.getX()+", "+probePositionSafe.getY());
					// Moving to X/Y probing position 
					controllerService.moveToAbsolutePosition(probePositionSafe);
					
					// Moving to Z Safe low position 
					Tuple6b probePositionLow = new Tuple6b();
					probePositionLow.setZ(pattern.getStartProbePosition());
					controllerService.moveToAbsolutePosition(probePositionLow);
					
					// Perform probe
					Future<ProbeResult> result = probingService.probe(EnumControllerAxis.Z_POSITIVE, 10, pattern.getEndProbePosition().doubleValue());
					ProbeResult probeResult = result.get();
					
					if(probeResult.isProbed()){
						LOG.info("    Result "+probeResult.getProbedPosition().getZ());
						elevationMap.addProbedPosition(probePositionSafe, probeResult.getProbedPosition());
					}
					controllerService.moveToAbsolutePosition(probePositionSafe);
				}
			}
		}catch(GkException e){
			LOG.error(e);
		} catch (InterruptedException e) {
			LOG.error(e);
		} catch (ExecutionException e) {
			LOG.error(e);
		}
	}

	/**
	 * @return the pattern
	 */
	public IAxisElevationPattern getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(IAxisElevationPattern pattern) {
		this.pattern = pattern;
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
	 * @return the elevationMap
	 */
	public IAxisElevationMap getElevationMap() {
		return elevationMap;
	}

	/**
	 * @param elevationMap the elevationMap to set
	 */
	public void setElevationMap(IAxisElevationMap elevationMap) {
		this.elevationMap = elevationMap;
	}

}
