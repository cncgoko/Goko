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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Point3d;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.math.Segment;
import org.goko.core.math.Tuple6b;
import org.goko.tools.centerfinder.bean.CircleCenterFinderResult;
import org.goko.tools.viewer.jogl.service.IJoglViewerService;
import org.goko.tools.viewer.jogl.utils.render.basic.PointRenderer;
import org.goko.tools.viewer.jogl.utils.render.coordinate.measurement.DiameterRenderer;


public class CenterFinderServiceImpl extends AbstractGokoService implements ICenterFinderService{
	private static final String SERVICE_ID = "org.goko.tools.centerfinder";
	private static final Color4f POINT_COLOR = new Color4f(1f,0.82f,0.16f,1f);
	private static final Color4f CENTER_COLOR = new Color4f(0f,0.47f,0.62f,1f);
	private static final Color4f CIRCLE_COLOR = new Color4f(0.42f,0.81f,0.94f,1f);
	private List<Tuple6b> memorizedPoints;
	private List<PointRenderer> pointsRenderer;
	private CircleCenterFinderResult centerResult;
	private IJoglViewerService rendererService;
	private DiameterRenderer renderer;
	/**
	 * Constructor
	 */
	public CenterFinderServiceImpl() {
		memorizedPoints = new ArrayList<Tuple6b>();
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
	public void startService() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stopService() throws GkException {

	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#getCapturedPoint()
	 */
	@Override
	public List<Tuple6b> getCapturedPoint() throws GkException{
		return memorizedPoints;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#capturePoint(javax.vecmath.Point3d)
	 */
	@Override
	public void capturePoint(Tuple6b point) throws GkException {
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
	public void removeCapturedPoint(Tuple6b point) throws GkException {
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

	/** (inheritDoc)
	 * @see org.goko.tools.centerfinder.ICenterFinderService#getCenter(java.util.List)
	 */
	@Override
	public CircleCenterFinderResult getCenter(List<Tuple6b> lstPoints, EnumPlane plane) throws GkException{
		centerResult = new CircleCenterFinderResult();
		Tuple6b center = new Tuple6b();
		List<Segment> lstSegment = new ArrayList<Segment>();
		// Get the result unit (arbitrary)
		Unit<Length> resultUnit = lstPoints.get(0).getX().getUnit();
		Tuple6b t1 = transformToXYPlane(plane, lstPoints.get(0).to(resultUnit));
		Tuple6b t2 = transformToXYPlane(plane, lstPoints.get(1).to(resultUnit));
		Tuple6b t3 = transformToXYPlane(plane, lstPoints.get(2).to(resultUnit));
		Point3d p1 = t1.toPoint3d(resultUnit);
		Point3d p2 = t2.toPoint3d(resultUnit);
		Point3d p3 = t3.toPoint3d(resultUnit);
		
		lstSegment.add(new Segment(t1, t2));
		lstSegment.add(new Segment(t2, t3));
		lstSegment.add(new Segment(t3, t1));

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
			Length centerX = s1.getStart().getX().add(s1.getEnd().getX()).divide(2);
			Length centerY = s2.getStart().getY().add(s2.getEnd().getY()).divide(2);
			center.setX(centerX);
			center.setY(centerY);
			centerResult.setCenter(untransformFromXYPlane(plane, center));
			centerResult.setRadius(t1.distance(center));	
			centerResult.setPlane(plane);
			updateRenderer();
			return centerResult;
		}else if(xParallelSegment >= 0){ // Avoid x parallel axis to avoid division by zero
			s1 = lstSegment.get((xParallelSegment+1)%3);
			s2 = lstSegment.get((xParallelSegment+2)%3);
		}

		BigDecimal x1 = s1.getStart().getX().value(resultUnit);
		BigDecimal x2 = s1.getEnd().getX().value(resultUnit);
		BigDecimal x3 = s2.getEnd().getX().value(resultUnit);

		BigDecimal y1 = s1.getStart().getY().value(resultUnit);
		BigDecimal y2 = s1.getEnd().getY().value(resultUnit);
		BigDecimal y3 = s2.getEnd().getY().value(resultUnit);
		if(s2.getEnd().equals(s1.getStart()) || s2.getEnd().equals(s1.getEnd())){
			x3 = s2.getStart().getX().value(resultUnit);
			y3 = s2.getStart().getY().value(resultUnit);
		}

		if( y3.subtract(y2).multiply(BigDecimal.valueOf(2)).abs().doubleValue() < 0.0001 ){
			throw new GkFunctionalException("Invalid points. Cannot compute center"); // Use translatable messages
		}
		if( y2.subtract(y1).multiply(BigDecimal.valueOf(2)).abs().doubleValue() < 0.0001 ){
			throw new GkFunctionalException("Invalid points. Cannot compute center");
		}
		BigDecimal a = ( x3.pow(2).subtract(x2.pow(2)).add(y3.pow(2)).subtract(y2.pow(2)).divide( y3.subtract(y2).multiply(BigDecimal.valueOf(2)), RoundingMode.HALF_UP  ));
		BigDecimal b = ( x2.pow(2).subtract(x1.pow(2)).add(y2.pow(2)).subtract(y1.pow(2)).divide( y2.subtract(y1).multiply(BigDecimal.valueOf(2)), RoundingMode.HALF_UP  ));
		BigDecimal c = ( x3.subtract(x2).divide(y3.subtract(y2), RoundingMode.HALF_UP ));
		BigDecimal d = ( x2.subtract(x1).divide(y2.subtract(y1), RoundingMode.HALF_UP ));
		
		if( c.equals(d) ){
			throw new GkFunctionalException("Invalid points. Cannot compute center");
		}
		
		BigDecimal centerX = (a.subtract(b).divide(c.subtract(d), RoundingMode.HALF_UP));
		BigDecimal centerY = b.subtract(d.multiply(centerX));
		center.setX( Length.valueOf(centerX, resultUnit));
		center.setY( Length.valueOf(centerY, resultUnit));		
		centerResult.setCenter(untransformFromXYPlane(plane, center));
		centerResult.setRadius(t1.distance(center));
		centerResult.setPlane(plane);
		updateRenderer();
		return centerResult;
	}

	/**
	 * Change the given tuple so it is expressed in the XY plane
	 * @param enumPlane the origin plane
	 * @param tuple the tuple to transform
	 * @return the transformed tuple in the XY plane
	 * @throws GkException GkException
	 */
	private static Tuple6b transformToXYPlane(EnumPlane enumPlane, Tuple6b tuple) throws GkException{		
		if(EnumPlane.XY_PLANE == enumPlane){			
			return new Tuple6b(tuple.getX(), tuple.getY(), Length.ZERO, tuple.getA(), tuple.getB(), tuple.getC());
			
		}else if(EnumPlane.XZ_PLANE == enumPlane){
			return new Tuple6b(tuple.getX(), tuple.getZ(), Length.ZERO, tuple.getA(), tuple.getB(), tuple.getC());
			
		}else if(EnumPlane.YZ_PLANE == enumPlane){
			return new Tuple6b(tuple.getY(), tuple.getZ(), Length.ZERO, tuple.getA(), tuple.getB(), tuple.getC());
		}	
		throw new GkTechnicalException("Unsupported plane "+ enumPlane);
	}
	
	/**
	 * Change the given tuple in the XY plane to the target plane 
	 * @param enumPlane the target plane 
	 * @param tuple the tuple in the XY plane
	 * @return the tuple in the given plane 
	 * @throws GkException GkException
	 */
	private static Tuple6b untransformFromXYPlane(EnumPlane enumPlane, Tuple6b tuple) throws GkException{		
		if(EnumPlane.XY_PLANE == enumPlane){			
			return new Tuple6b(tuple.getX(), tuple.getY(), Length.ZERO, tuple.getA(), tuple.getB(), tuple.getC());
						
		}else if(EnumPlane.XZ_PLANE == enumPlane){
			return new Tuple6b(tuple.getX(), Length.ZERO, tuple.getY(), tuple.getA(), tuple.getB(), tuple.getC());
			
		}else if(EnumPlane.YZ_PLANE == enumPlane){
			return new Tuple6b(Length.ZERO, tuple.getX(), tuple.getY(), tuple.getA(), tuple.getB(), tuple.getC());
		}		
		throw new GkTechnicalException("Unsupported plane "+ enumPlane);
	}
	
	/**
	 * Update the renderer for the circle center finder 
	 * @throws GkException GkException
	 */
	protected void updateRenderer() throws GkException{
		if(getRendererService() != null){
			if(renderer != null){
				renderer.destroy();
				renderer = null;
			}
			if(centerResult != null && renderer == null){
				
				renderer = new DiameterRenderer(centerResult.getCenter(), centerResult.getDiameter(), CIRCLE_COLOR, centerResult.getNormal(), CENTER_COLOR );
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
