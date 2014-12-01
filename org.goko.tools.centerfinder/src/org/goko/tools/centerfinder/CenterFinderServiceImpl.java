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
package org.goko.tools.centerfinder;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;
import org.goko.core.viewer.renderer.IRendererProxy;
import org.goko.core.viewer.renderer.IViewer3DRenderer;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;
import org.goko.tools.centerfinder.bean.Segment;


public class CenterFinderServiceImpl implements ICenterFinderService, IViewer3DRenderer{
	private static final String SERVICE_ID = "org.goko.tools.centerfinder";
	private static final Point3f POINT_COLOR = new Point3f(1f,0.82f,0.16f);
	private List<Point3d> memorizedPoints;
	/** The renderer state */
	private boolean renderEnabled;
	private CircleCenterFinderResult centerResult;

	/**
	 * Constructor
	 */
	public CenterFinderServiceImpl() {
		memorizedPoints = new ArrayList<Point3d>();
		renderEnabled = true;
	}

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
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#getCapturedPoint()
	 */
	@Override
	public List<Point3d> getCapturedPoint() throws GkException{
		return memorizedPoints;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#capturePoint(javax.vecmath.Point3d)
	 */
	@Override
	public void capturePoint(Point3d point) throws GkException {
		memorizedPoints.add(point);

	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#clearCapturedPoint()
	 */
	@Override
	public void clearCapturedPoint() throws GkException {
		memorizedPoints.clear();
		centerResult = null;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#clearCapturedPoint()
	 */
	@Override
	public void removeCapturedPoint(Point3d point) throws GkException {
		memorizedPoints.remove(point);
		centerResult = null;
	}

	@Override
	public CircleCenterFinderResult getCenter(List<Point3d> lstPoints){
		centerResult = new CircleCenterFinderResult();
		Point3d center = new Point3d();
		List<Segment> lstSegment = new ArrayList<Segment>();

		Point3d p1 = lstPoints.get(0);
		Point3d p2 = lstPoints.get(1);
		Point3d p3 = lstPoints.get(2);

//		int maxScale  = Math.max(t1.getX().scale(), Math.max(t1.getY().scale(), t1.getZ().scale()));
//		maxScale  = Math.max(maxScale, Math.max(t2.getX().scale(), Math.max(t2.getY().scale(), t2.getZ().scale())));
//		maxScale  = Math.max(maxScale, Math.max(t3.getX().scale(), Math.max(t3.getY().scale(), t3.getZ().scale())));

		lstSegment.add(new Segment(p1, p2));
		lstSegment.add(new Segment(p2, p3));
		lstSegment.add(new Segment(p3, p1));

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
			center.x = (s1.getStart().x + s1.getEnd().x)/2;
			center.y = (s2.getStart().y + s2.getEnd().y)/2;
			centerResult.setCenter(center);
			centerResult.setRadius(p1.distance(center));
			return centerResult;
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

//		if(!Double.isNaN(center.x)){
//			BigDecimal centerX = new BigDecimal(String.valueOf(center.x));
//			centerX = centerX.setScale(3, BigDecimal.ROUND_HALF_DOWN);
//
//			getDataModel().setCenterXPosition(centerX.toString());
//		}else{
//			getDataModel().setCenterXPosition("Invalid");
//		}
		centerResult.setCenter(center);
//		if(!Double.isNaN(center.y)){
//			BigDecimal centerY = new BigDecimal(String.valueOf(center.y));
//			centerY = centerY.setScale(3, BigDecimal.ROUND_HALF_DOWN);
//
//			getDataModel().setCenterYPosition(centerY.toString());
//		}else{
//			getDataModel().setCenterYPosition("Invalid");
//		}
		centerResult.setRadius(p1.distance(center));
		return centerResult;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return SERVICE_ID;
	}

	@Override
	public void render(IRendererProxy proxy) throws GkException {
		if(CollectionUtils.isNotEmpty(memorizedPoints)){
			List<Point3d> localList = new ArrayList<Point3d>(memorizedPoints);
			for (Point3d p : localList) {
				proxy.drawPoint(new Tuple6b(p.x, p.y, p.z), POINT_COLOR);
			}
		}
		if(centerResult != null){
			if(centerResult.getCenter() != null){
				Tuple6b center = new Tuple6b(centerResult.getCenter().x, centerResult.getCenter().y, centerResult.getCenter().z);
				proxy.drawPoint(center, POINT_COLOR);
				Tuple6b end = new Tuple6b( centerResult.getCenter().x + (centerResult.getRadius()+2)*Math.cos(Math.PI/4), centerResult.getCenter().y + (centerResult.getRadius()+2)*Math.sin(Math.PI/4), centerResult.getCenter().z );
				Tuple6b endAnnotation = new Tuple6b( end.getX().doubleValue() + 2, end.getY().doubleValue(), end.getZ().doubleValue());
				proxy.drawSegment(center, end, POINT_COLOR);
				proxy.drawSegment(end, endAnnotation, POINT_COLOR);
				proxy.drawCircle(center, centerResult.getRadius(), new Vector3f(0,0,1), POINT_COLOR);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return renderEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		renderEnabled = enabled;
	}

}
