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

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;
import org.goko.tools.centerfinder.bean.Segment;
import org.goko.viewer.jogl.service.ICoreJoglRenderer;
import org.goko.viewer.jogl.service.IJoglViewerService;
import org.goko.viewer.jogl.utils.render.basic.PointRenderer;
import org.goko.viewer.jogl.utils.render.coordinate.measurement.DiameterRenderer;


public class CenterFinderServiceImpl implements ICenterFinderService{
	private static final String SERVICE_ID = "org.goko.tools.centerfinder";
	private static final Color4f POINT_COLOR = new Color4f(1f,0.82f,0.16f,1f);
	private List<Point3d> memorizedPoints;
	private List<PointRenderer> pointsRenderer;
	private CircleCenterFinderResult centerResult;
	private IJoglViewerService rendererService;
	private ICoreJoglRenderer renderer;
	/**
	 * Constructor
	 */
	public CenterFinderServiceImpl() {
		memorizedPoints = new ArrayList<Point3d>();
		pointsRenderer = new ArrayList<PointRenderer>();
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
		if(getRendererService() != null){
			PointRenderer pRenderer = new PointRenderer(point, 2, POINT_COLOR);
			pointsRenderer.add(pRenderer);
			getRendererService().addRenderer(pRenderer);
		}
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
		if(getRendererService() != null){
			int pos = memorizedPoints.indexOf(point);
			PointRenderer pRenderer = pointsRenderer.get(pos);
			pRenderer.destroy();
			pointsRenderer.remove(pos);
		}
		memorizedPoints.remove(point);
		centerResult = null;
		updateRenderer();
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

	protected void updateRenderer() throws GkException{
		if(getRendererService() != null){
			if(renderer != null){
				renderer.destroy();
				renderer = null;
			}
			if(centerResult != null){
				renderer = new DiameterRenderer(centerResult.getCenter(), NumberQuantity.of(centerResult.getRadius()*2, SIPrefix.MILLI(SI.METRE)), POINT_COLOR, new Vector3d(0,0,1) );
				getRendererService().addRenderer(renderer);
			}
		}
	}

	/**
	 * @return the rendererService
	 */
	public IJoglViewerService getRendererService() {
		return rendererService;
	}

	/**
	 * @param rendererService the rendererService to set
	 */
	public void setRendererService(IJoglViewerService rendererService) {
		this.rendererService = rendererService;
	}

}
