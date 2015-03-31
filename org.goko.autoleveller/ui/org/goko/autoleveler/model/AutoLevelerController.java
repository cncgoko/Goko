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
import javax.vecmath.Point3d;

import org.eclipse.e4.core.di.annotations.Optional;
import org.goko.autoleveler.bean.GridElevationMap;
import org.goko.autoleveler.bean.IAxisElevationPattern;
import org.goko.autoleveler.service.IAutoLevelerService;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
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
		Tuple6b start = new Tuple6b(getDataModel().getStartx(),getDataModel().getStarty(),getDataModel().getSafeZ());
		Tuple6b end = new Tuple6b(getDataModel().getEndx(),getDataModel().getEndy(),getDataModel().getSafeZ());
		int stepX = getDataModel().getStepX().intValue();
		int stepY = getDataModel().getStepY().intValue();
		double safeZ = getDataModel().getSafeZ().doubleValue();
		GridElevationMap pattern = new GridElevationMap(start, end, stepX,stepY, safeZ, getDataModel().getExpectedZ().doubleValue());
		pattern.setStartProbePosition(getDataModel().getStartProbe());
		pattern.setEndProbePosition(getDataModel().getMaxZProbe());

		return pattern;
	}
	/**
	 * Grab the current position of the machine, and use it as start point
	 * @throws GkException Exception
	 */
	public void grabStartPointFromPosition() throws GkException{
		Point3d position = controllerService.getPosition();
		this.getDataModel().setStartx(BigDecimal.valueOf(position.x));
		this.getDataModel().setStarty(BigDecimal.valueOf(position.y));
	}
	/**
	 * Grab the current position of the machine, and use it as end point
	 * @throws GkException Exception
	 */
	public void grabEndPointFromPosition() throws GkException{
		Point3d position = controllerService.getPosition();
		this.getDataModel().setEndx(BigDecimal.valueOf(position.x));
		this.getDataModel().setEndy(BigDecimal.valueOf(position.y));
	}

	public void debugCurrentPosition() throws GkException {
		Point3d position = controllerService.getPosition();
		getDataModel().getMap().getCorrectedElevation(new Tuple6b(position.x,position.y,position.z));
		service.apply(getDataModel().getGcodeProvider());
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
