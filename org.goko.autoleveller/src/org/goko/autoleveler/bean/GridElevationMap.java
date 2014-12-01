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
package org.goko.autoleveler.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.Tuple6b;

public class GridElevationMap implements IAxisElevationMap, IAxisElevationPattern {
	private List<Tuple6b> probedPositions;
	private List<Tuple6b> positions;
	private Tuple6b start;
	private Tuple6b end;
	private Vector3d Z_AXIS = new Vector3d(0,0,1);
	private double xAxisStep;
	private double yAxisStep;
	private double xAxisDivisionCount;
	private double yAxisDivisionCount;
	private double zSafeHeight;
	private double startProbeZ;
	private double expectedZ;
	private double maximalProbeZ;
	private int[][] vertices;
	private int xVerts;
	private int yVerts;

	public GridElevationMap(Tuple6b tStart, Tuple6b tEnd, int xAxisDivisionCount, int yAxisDivisionCount, double safeHeight, double expectedHeight) {
		super();
		this.start = new Tuple6b();
		start.setX(tStart.getX().min(tEnd.getX()));
		start.setY(tStart.getY().min(tEnd.getY()));
		start.setZ(tStart.getZ().min(tEnd.getZ()));
		this.end = new Tuple6b();
		end.setX(tStart.getX().max(tEnd.getX()));
		end.setY(tStart.getY().max(tEnd.getY()));
		end.setZ(tStart.getZ().max(tEnd.getZ()));
		this.xAxisDivisionCount = xAxisDivisionCount;
		this.yAxisDivisionCount = yAxisDivisionCount;
		double dx = end.getX().subtract(start.getX()).doubleValue();
		double dy = end.getY().subtract(start.getY()).doubleValue();
		this.xAxisStep = dx / Math.abs(xAxisDivisionCount);
		this.yAxisStep = dy / Math.abs(yAxisDivisionCount);
		this.xVerts = xAxisDivisionCount + 1;//(int) Math.floor((Math.abs(dx)/Math.abs(xAxisStep))) + 1;
		this.yVerts = yAxisDivisionCount + 1;//(int) Math.floor((Math.abs(dy)/Math.abs(yAxisStep))) + 1;
		this.vertices = new int[xVerts][yVerts];

		this.zSafeHeight = safeHeight;
		this.expectedZ = expectedHeight;
		this.probedPositions = new ArrayList<Tuple6b>();
		generatePatternPositions();
	}

	/**
	 * Generates the positions in this pattern
	 */
	private void generatePatternPositions(){
		positions = new ArrayList<Tuple6b>();

		double x = start.getX().doubleValue();
		for(int xi = 0; xi <= xAxisDivisionCount; xi++){
			double y = start.getY().doubleValue();
			for(int yi = 0; yi <= yAxisDivisionCount; yi++){
				Tuple6b pos = new Tuple6b(start);
				pos.setX(BigDecimal.valueOf(x));
				pos.setY(BigDecimal.valueOf(y));
				pos.setZ(BigDecimal.valueOf(zSafeHeight));
				vertices[xi][yi] = CollectionUtils.size(positions);
				positions.add( pos );
				y += yAxisStep;
			}
			x+= xAxisStep;
		}
//		for(double x = start.getX().doubleValue(); x <= end.getX().doubleValue(); x+= xAxisStep){
//			yi = 0;
//			for(double y = start.getY().doubleValue(); y <= end.getY().doubleValue(); y+= yAxisStep){
//				Tuple6b pos = new Tuple6b(start);
//				pos.setX(BigDecimal.valueOf(x));
//				pos.setY(BigDecimal.valueOf(y));
//				pos.setZ(BigDecimal.valueOf(zSafeHeight));
//				vertices[xi][yi] = CollectionUtils.size(positions);
//				positions.add( pos );
//				yi += 1;
//			}
//			xi += 1;
//		}

	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationMap#addPosition(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public void addProbedPosition(Tuple6b patternPosition, Tuple6b realPosition) throws GkException {
		probedPositions.add(realPosition);
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationMap#getCorrectedElevation(org.goko.core.gcode.bean.Tuple6b)
	 */
	@Override
	public Tuple6b getCorrectedElevation(Tuple6b position) throws GkException {
		Tuple6b clippedPosition  = clipPointInsideArea(position);
		int[] xs = getXBounds(clippedPosition);
		int[] ys = getYBounds(clippedPosition);

		Tuple6b pA = probedPositions.get( vertices[xs[0]][ys[0]] );
		Tuple6b pB = probedPositions.get( vertices[xs[0]][ys[1]] );
		Tuple6b pC = probedPositions.get( vertices[xs[1]][ys[0]] );
		Tuple6b pD = probedPositions.get( vertices[xs[1]][ys[1]] );

		BigDecimal h = findHeightBilinear(clippedPosition, pA, pB, pC, pD);
		if(h != null){
			double delta = h.doubleValue() - expectedZ;
			Tuple6b fixed = new Tuple6b(clippedPosition);
			fixed.setZ( BigDecimal.valueOf(clippedPosition.getZ().doubleValue() + delta));
			return fixed;
		}
		System.err.println("height null");

		return null;
	}

	private Tuple6b clipPointInsideArea(Tuple6b position){
		Tuple6b clippedPosition = new Tuple6b(position);
		if(position.getX().compareTo(end.getX()) > 0){
			clippedPosition.setX( end.getX());
		}else if(position.getX().compareTo(start.getX()) < 0){
			clippedPosition.setX( start.getX());
		}

		if(position.getY().compareTo(end.getY()) > 0){
			clippedPosition.setY( end.getY());
		}else if(position.getY().compareTo(start.getY()) < 0){
			clippedPosition.setY( start.getY());
		}
		return clippedPosition;
	}

	/**
	 * Height finder base on successive linear interpolation.
	 * Points v1 and v2 should be x aligned.
	 * Points v3 and v4 should be x aligned
	 * Points v1 and v3 should be y aligned
	 * Points v2 and v4 should be y aligned
	 * @param position the position of the point to adjust Z
	 * @param v1 area corner 1
	 * @param v2 area corner 1
	 * @param v3 area corner 1
	 * @param v4 area corner 1
	 * @return
	 */
	private BigDecimal findHeightBilinear(Tuple6b position,Tuple6b v1,Tuple6b v2,Tuple6b v3,Tuple6b v4){

		double x1 = Math.abs(position.getY().doubleValue() - v1.getY().doubleValue());
		double dx1 = Math.abs(v2.getY().doubleValue() - v1.getY().doubleValue());
		double a1z = v2.getZ().doubleValue() * (x1 / dx1) + v1.getZ().doubleValue() * (1- (x1/dx1));

		double x2 = Math.abs(position.getY().doubleValue() - v3.getY().doubleValue());
		double dx2 = Math.abs(v4.getY().doubleValue() - v3.getY().doubleValue());
		double a2z = v4.getZ().doubleValue() * (x2 / dx2) + v3.getZ().doubleValue() * (1- (x2/dx2));

		double y1 = Math.abs(position.getX().doubleValue() - v1.getX().doubleValue());;
		double dy1 = Math.abs(v3.getX().doubleValue() - v1.getX().doubleValue());;
		double zFinal = a2z * (y1/dy1) + a1z * (1 - (y1/dy1));

		return BigDecimal.valueOf(zFinal);
	}


	/**
	 * Möller–Trumbore intersection algorithm
	 * @param position
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 */
	private BigDecimal findHeightMollerTrumbore(Tuple6b position,Tuple6b v1,Tuple6b v2,Tuple6b v3){
		Vector3d e1 = new Vector3d( v2.getX().doubleValue() - v1.getX().doubleValue(), v2.getY().doubleValue() - v1.getY().doubleValue(), v2.getZ().doubleValue() - v1.getZ().doubleValue());
		Vector3d e2 = new Vector3d( v3.getX().doubleValue() - v1.getX().doubleValue(), v3.getY().doubleValue() - v1.getY().doubleValue(), v3.getZ().doubleValue() - v1.getZ().doubleValue());
		Vector3d p = new Vector3d();
		p.cross(Z_AXIS, e2);
		Vector3d grandT = new Vector3d(position.getX().doubleValue() - v1.getX().doubleValue(), position.getY().doubleValue() - v1.getY().doubleValue(), position.getZ().doubleValue() - v1.getZ().doubleValue());
		double det = e1.dot(p);
		double inv_det = 1.0f / det;
		double u = grandT.dot(p) * inv_det;
		//The intersection lies outside of the triangle
		if(u < 0 || u > 1){
			return null;
		}

		Vector3d q = new Vector3d();
		q.cross(grandT, e1);
		double v = Z_AXIS.dot(q) * inv_det;
		//The intersection lies outside of the triangle
		if(v < 0 || v > 1){
			return null;
		}
		double t = e2.dot(q) * inv_det;
		if(t > 0.00001){
			return BigDecimal.valueOf(t);
		}
		return null;
	}

	private int[] getXBounds(Tuple6b position){
		int v = 0;
		for(int i = 1; i < xVerts ; i++){
			Tuple6b gridPoint = positions.get(vertices[i][0]);
			if(gridPoint.getX().doubleValue() >= position.getX().doubleValue() ){
				v = i;
				break;
			}
		}
		int[] result = new int[]{v-1, v};
		return result;
	}

	private int[] getYBounds(Tuple6b position){
		int v = 0;
		for(int i = 1; i < xVerts ; i++){
			Tuple6b gridPoint = positions.get(vertices[0][i]);
			if(gridPoint.getY().doubleValue() >= position.getY().doubleValue() ){
				v = i;
				break;
			}
		}
		int[] result = new int[]{v-1, v};
		return result;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationPattern#getPatternPositions()
	 */
	@Override
	public List<Tuple6b> getPatternPositions() {
		return positions;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationPattern#getStartProbePosition()
	 */
	@Override
	public BigDecimal getStartProbePosition() throws GkException {
		return BigDecimal.valueOf(startProbeZ);
	}

	public void setStartProbePosition(BigDecimal start) {
		startProbeZ = start.doubleValue();
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationPattern#getEndProbePosition()
	 */
	@Override
	public BigDecimal getEndProbePosition() throws GkException {
		return BigDecimal.valueOf(maximalProbeZ);
	}

	public void setEndProbePosition(BigDecimal end) {
		maximalProbeZ = end.doubleValue();
	}
	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationMap#getProbedPositions()
	 */
	@Override
	public List<Tuple6b> getProbedPositions() {
		return probedPositions;
	}

}
