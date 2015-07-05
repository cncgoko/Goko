package org.goko.tools.centerfinder.model;
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


import java.util.List;

import javax.inject.Inject;
import javax.vecmath.Point3d;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.service.IGCodeService;
import org.goko.tools.centerfinder.ICenterFinderService;

/**
 *Center finder controller
 * @author PsyKo
 *
 */
public class CenterFinderController extends AbstractController<CenterFinderModel>{

	@Inject
	IControllerService controllerService;
	@Inject
	IGCodeService gcodeService;
	@Inject
	ICenterFinderService centerFinderService;

	public CenterFinderController() {
		super(new CenterFinderModel());
	}

	@Override
	public void initialize() throws GkException {
		// TODO Auto-generated method stub

	}

	public void goToCalculatedCenter(){

	}

	@SuppressWarnings("unchecked")
	public void grabPoint() throws GkException{		
		List<Tuple6b> lst = getDataModel().getSamplePoints();
		Tuple6b tuple = controllerService.getPosition();
		lst.add(tuple );
		getDataModel().setSamplePoints(lst);
		getDataModel().setSelectedPoint(tuple);
		centerFinderService.capturePoint(tuple);
		if(getDataModel().getSamplePoints().size() >= 3){
			getDataModel().setCircleCenterResult(centerFinderService.getCenter(centerFinderService.getCapturedPoint()));
		}else{
			getDataModel().setCircleCenterResult(null);
		}
	}

	public void clearSelectedSamplePoints() throws GkException{
		if(getDataModel().getSelectedPoint() != null){
			int index = getDataModel().getSamplePoints().indexOf(getDataModel().getSelectedPoint());
			getDataModel().getSamplePoints().remove(getDataModel().getSelectedPoint());
			centerFinderService.removeCapturedPoint(centerFinderService.getCapturedPoint().get(index));
			autoSelectPoint(index);
		}

		if(getDataModel().getSamplePoints().size() >= 3){
			getDataModel().setCircleCenterResult(centerFinderService.getCenter(centerFinderService.getCapturedPoint()));
		}else{
			getDataModel().setCircleCenterResult(null);
		}
	}

	protected void autoSelectPoint(int index){
		if(getDataModel().getSamplePoints().isEmpty()){
			getDataModel().setSelectedPoint(null);
		}else{
			index = Math.max(0, Math.min(index, getDataModel().getSamplePoints().size()-1));
			getDataModel().setSelectedPoint((Tuple6b) getDataModel().getSamplePoints().get(index));
		}
	}
}
