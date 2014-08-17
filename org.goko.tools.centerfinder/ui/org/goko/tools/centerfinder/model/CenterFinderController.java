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


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.vecmath.Point3d;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.gcode.service.IGCodeService;

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
		Point3d p = controllerService.getPosition();
		List<Tuple6b> lst = getDataModel().getSamplePoints();
		Tuple6b tuple = new Tuple6b(p.x,p.y,p.z);
		lst.add(tuple );
		getDataModel().setSamplePoints(lst);
		getDataModel().setSelectedPoint(tuple);
		if(getDataModel().getSamplePoints().size() >= 3){
			computeCenter();
		}else{
			getDataModel().setCenterXPosition("--");
			getDataModel().setCenterYPosition("--");
			getDataModel().setCenterZPosition("--");
		}
	}

	private void computeCenter(){
		Point3d center = new Point3d();
		List<Segment> lstSegment = new ArrayList<Segment>();

		Tuple6b t1 = (Tuple6b) getDataModel().getSamplePoints().get(0);
		Tuple6b t2 = (Tuple6b) getDataModel().getSamplePoints().get(1);
		Tuple6b t3 = (Tuple6b) getDataModel().getSamplePoints().get(2);

		int maxScale  = Math.max(t1.getX().scale(), Math.max(t1.getY().scale(), t1.getZ().scale()));
		maxScale  = Math.max(maxScale, Math.max(t2.getX().scale(), Math.max(t2.getY().scale(), t2.getZ().scale())));
		maxScale  = Math.max(maxScale, Math.max(t3.getX().scale(), Math.max(t3.getY().scale(), t3.getZ().scale())));

		lstSegment.add(new Segment(t1.toPoint3d(), t2.toPoint3d()));
		lstSegment.add(new Segment(t2.toPoint3d(), t3.toPoint3d()));
		lstSegment.add(new Segment(t3.toPoint3d(), t1.toPoint3d()));
		Point3d p1 = t1.toPoint3d();
		Point3d p2 = t2.toPoint3d();
		Point3d p3 = t3.toPoint3d();
		int xParallelSegment = -1;
		int yParallelSegment = -1;
		// First determine if the have 3 points defining segments along the x or y axis
		// Segment p1p2
		if(p1.x == p2.x && p1.y != p2.y){
			yParallelSegment = 0;
		}else if(p2.x == p3.x && p2.y != p3.y){  // Segment p2p3
			yParallelSegment = 1;
		}else if(p1.x == p3.x && p1.y != p3.y){  // Segment p3p1
			yParallelSegment = 2;
		}

		// Segment p1p2
		if(p1.y == p2.y && p1.x != p2.x){
			xParallelSegment = 0;
		}else if(p2.y == p3.y && p2.x != p3.x){  // Segment p2p3
			xParallelSegment = 1;
		}else if(p1.y == p3.y && p1.x != p3.x){  // Segment p3p1
			xParallelSegment = 2;
		}
		Segment s1 = lstSegment.get(0);
		Segment s2 = lstSegment.get(1);
		if(xParallelSegment >= 0 && yParallelSegment >= 0){ // 3 Points make a rectangle triangle
			s1 = lstSegment.get(xParallelSegment);
			s2 = lstSegment.get(yParallelSegment);
			center.x = (s2.getStart().x + s2.getEnd().x)/2;
			center.y = (s1.getStart().y + s1.getEnd().y)/2;
			return;
		}else if(xParallelSegment >= 0){ // Avoid x parallel axis to avoid division by zero
			s1 = lstSegment.get((xParallelSegment+1)%3);
			s2 = lstSegment.get((xParallelSegment+2)%3);
		}

		double x1 = s1.getStart().x;
		double x2 = s1.getEnd().x;
		double x3 = s2.getEnd().x;

		double y1 = s1.getStart().y;
		double y2 = s1.getEnd().y;
		double y3 = s2.getEnd().y;
		if(s2.getEnd().equals(s1.getStart()) || s2.getEnd().equals(s1.getEnd())){
			x3 = s2.getStart().x;
			y3 = s2.getStart().y;
		}
		double a = ( x3*x3 - x2*x2 + y3*y3 - y2*y2) / (2*(y3-y2));
		double b = ( x2*x2 - x1*x1 + y2*y2 - y1*y1)/(2*(y2-y1));
		double c = ( x3-x2) / (y3-y2);
		double d = ( x2-x1) / (y2-y1);

		center.x = (a - b)/( c - d);
		center.y = -d*center.x + b;
		if(!Double.isNaN(center.x)){
			BigDecimal centerX = new BigDecimal(String.valueOf(center.x));
			centerX = centerX.setScale(maxScale, BigDecimal.ROUND_HALF_DOWN);

			getDataModel().setCenterXPosition(centerX.toString());
		}else{
			getDataModel().setCenterXPosition("Invalid");
		}
		if(!Double.isNaN(center.y)){
			BigDecimal centerY = new BigDecimal(String.valueOf(center.y));
			centerY = centerY.setScale(maxScale, BigDecimal.ROUND_HALF_DOWN);

			getDataModel().setCenterYPosition(centerY.toString());
		}else{
			getDataModel().setCenterYPosition("Invalid");
		}
	}

	public void clearSelectedSamplePoints(){
		if(getDataModel().getSelectedPoint() != null){
			int index = getDataModel().getSamplePoints().indexOf(getDataModel().getSelectedPoint());
			getDataModel().getSamplePoints().remove(getDataModel().getSelectedPoint());
			if(getDataModel().getSamplePoints().isEmpty()){
				getDataModel().setSelectedPoint(null);
			}else{
				index = Math.max(0, Math.min(index, getDataModel().getSamplePoints().size()-1));
				getDataModel().setSelectedPoint((Tuple6b) getDataModel().getSamplePoints().get(index));
			}
		}
		if(getDataModel().getSamplePoints().size() >= 3){
			computeCenter();
		}else{
			getDataModel().setCenterXPosition("--");
			getDataModel().setCenterYPosition("--");
			getDataModel().setCenterZPosition("--");
		}
	}
}
