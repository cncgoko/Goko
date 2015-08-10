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
package org.goko.autoleveler.model;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.autoleveler.bean.GridElevationMap;
import org.goko.autoleveler.bean.IAxisElevationPattern;
import org.goko.autoleveler.service.GkAutoLevelerService;
import org.goko.autoleveler.service.IAutoLevelerService;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.workspace.service.IWorkspaceService;

public class AutoLevelerController extends AbstractController<AutoLevelerModel>{
	@Inject
	@Optional
	IAutoLevelerService service;
	@Inject
	IControllerService controllerService;
	@Inject
	IWorkspaceService workspaceService;

	public AutoLevelerController() {
		super(new AutoLevelerModel());
	}

	@Override
	public void initialize() throws GkException {
		// TODO Auto-generated method stub

	}

	public void probe() throws GkException {
		GridElevationMap map = getPattern();		
		service.probe(map);
		getDataModel().setMap(map);
	}

	private GridElevationMap testMap(GridElevationMap map) throws GkException{
		for (Tuple6b probePosition : map.getPatternPositions()) {
			Tuple6b realPosition = new Tuple6b(probePosition);
			realPosition.setZ(NumberQuantity.of( new BigDecimal(Math.random()*10), probePosition.getX().getUnit()));
			map.addProbedPosition(probePosition, realPosition);
		}
		return map;
	}
	public void previewPattern() throws GkException{
		GridElevationMap map = getPattern();
		//service.probe(map);
		getDataModel().setMap(map);
		service.previewPattern(map);

	}
	/**
	 * Build the pattern using user inputs
	 * @return {@link IAxisElevationPattern} the generated pattern
	 */
	private GridElevationMap getPattern(){
		Tuple6b start = new Tuple6b(SI.MILLIMETRE, getDataModel().getStartx(), getDataModel().getStarty(), getDataModel().getSafeZ());
		Tuple6b end = new Tuple6b(SI.MILLIMETRE, getDataModel().getEndx(),getDataModel().getEndy(),getDataModel().getSafeZ());
		int stepX = getDataModel().getStepX().intValue();
		int stepY = getDataModel().getStepY().intValue();		
		GridElevationMap pattern = new GridElevationMap(start, end, stepX,stepY, getDataModel().getSafeZ(), getDataModel().getExpectedZ());
		pattern.setStartProbePosition(getDataModel().getStartProbe());
		pattern.setEndProbePosition(getDataModel().getMaxZProbe());

		return pattern;
	}
	/**
	 * Grab the current position of the machine, and use it as start point
	 * @throws GkException Exception
	 */
	public void grabStartPointFromPosition() throws GkException{
		Tuple6b position = controllerService.getPosition();
		this.getDataModel().setStartx(position.getX());
		this.getDataModel().setStarty(position.getY());
	}
	/**
	 * Grab the current position of the machine, and use it as end point
	 * @throws GkException Exception
	 */
	public void grabEndPointFromPosition() throws GkException{
		Tuple6b position = controllerService.getPosition();
		this.getDataModel().setEndx(position.getX());
		this.getDataModel().setEndy(position.getY());
	}

	public void debugCurrentPosition() throws GkException {
//		Tuple6b position = controllerService.getPosition();
		((GkAutoLevelerService)service).setMap(testMap(getPattern()));		
		IGCodeProvider provider = service.apply(getDataModel().getGcodeProvider());				
		workspaceService.setCurrentGCodeProvider(provider.getId());
	}

	public void setGCodeFile(IGCodeProvider file) {
		getDataModel().setGcodeProvider(file);
	}

	public void setBoundsFromGCodeFile() throws GkException {
		if(workspaceService != null){
			IGCodeProvider currentGCode = workspaceService.getCurrentGCodeProvider();
			if(currentGCode != null){
				BoundingTuple6b bounds = currentGCode.getBounds();
				this.getDataModel().setStartx(bounds.getMin().getX());
				this.getDataModel().setStarty(bounds.getMin().getY());
				this.getDataModel().setEndx(bounds.getMax().getX());
				this.getDataModel().setEndy(bounds.getMax().getY());
			}
		}
	}

	public boolean isAutolevelerService() {
		return service != null;
	}

	public void saveValues() {
		// TODO Auto-generated method stub

	}


}
