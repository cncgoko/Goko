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
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.gcode.bean.Tuple6b;

public class GridElevationMap implements IAxisElevationMap, IAxisElevationPattern {
	private List<Tuple6b> probedPositions;
	private List<Tuple6b> positions;
	private Tuple6b start;
	private Tuple6b end;
	private Vector3d Z_AXIS = new Vector3d(0,0,1);
	private BigDecimalQuantity<Length> xAxisStep;
	private BigDecimalQuantity<Length> yAxisStep;
	private double xAxisDivisionCount;
	private double yAxisDivisionCount;
	private BigDecimalQuantity<Length> zSafeHeight;
	private BigDecimalQuantity<Length> startProbeZ;
	private BigDecimalQuantity<Length> expectedZ;
	private BigDecimalQuantity<Length> maximalProbeZ;
	private int[][] vertices;
	private int xVerts;
	private int yVerts;

	public GridElevationMap(Tuple6b tStart, Tuple6b tEnd, int xAxisDivisionCount, int yAxisDivisionCount, BigDecimalQuantity<Length> safeHeight, BigDecimalQuantity<Length> expectedHeight) {
		super();
		this.start = new Tuple6b();
		this.start.min(tStart, tEnd);
		this.end = new Tuple6b();
		this.end.max(tStart, tEnd);
		
		this.xAxisDivisionCount = xAxisDivisionCount;
		this.yAxisDivisionCount = yAxisDivisionCount;
		BigDecimalQuantity<Length> dx = end.getX().subtract(start.getX());
		BigDecimalQuantity<Length> dy = end.getY().subtract(start.getY());
		this.xAxisStep = dx.divide(Math.abs(xAxisDivisionCount));
		this.yAxisStep = dy.divide(Math.abs(yAxisDivisionCount));
		this.xVerts 	= xAxisDivisionCount + 1;
		this.yVerts 	= yAxisDivisionCount + 1;
		this.vertices 	= new int[xVerts][yVerts];

		this.zSafeHeight 	= safeHeight;
		this.expectedZ 		= expectedHeight;
		this.probedPositions = new ArrayList<Tuple6b>();
		generatePatternPositions();
	}

	/**
	 * Generates the positions in this pattern
	 */
	private void generatePatternPositions(){
		positions = new ArrayList<Tuple6b>();
				
		Tuple6b currentPosition = new Tuple6b(start);
		
		for(int xi = 0; xi <= xAxisDivisionCount; xi++){			
			for(int yi = 0; yi <= yAxisDivisionCount; yi++){				
				currentPosition.setZ(zSafeHeight);
				vertices[xi][yi] = CollectionUtils.size(positions);
				positions.add( new Tuple6b(currentPosition) );
				currentPosition.setY(currentPosition.getY().add(yAxisStep));
			}
			currentPosition.setX(currentPosition.getX().add(xAxisStep));
			currentPosition.setY(start.getY());
		}
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
		BigDecimalQuantity<Length> theoricalHeight = position.getZ();
		Tuple6b clippedPosition  = new Tuple6b(position);		
		clippedPosition = clippedPosition.min(end);
		clippedPosition = clippedPosition.max(start);
		clippedPosition.setZ(theoricalHeight); // Z remains unchanged
		
		int[] xs = getXBounds(clippedPosition);
		int[] ys = getYBounds(clippedPosition);

		Tuple6b pA = probedPositions.get( vertices[xs[0]][ys[0]] );
		Tuple6b pB = probedPositions.get( vertices[xs[0]][ys[1]] );
		Tuple6b pC = probedPositions.get( vertices[xs[1]][ys[0]] );
		Tuple6b pD = probedPositions.get( vertices[xs[1]][ys[1]] );

		BigDecimalQuantity<Length> computedHeight = findHeightBilinear(clippedPosition, pA, pB, pC, pD);
		if(computedHeight != null){
			BigDecimalQuantity<Length> delta = computedHeight.subtract(expectedZ);
			Tuple6b fixed = new Tuple6b(clippedPosition);
			fixed.setZ( clippedPosition.getZ().add(delta));
			return fixed;
		}
		System.err.println("height null");
		return null;
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
	private BigDecimalQuantity<Length> findHeightBilinear(Tuple6b position,Tuple6b v1,Tuple6b v2,Tuple6b v3,Tuple6b v4){

		BigDecimalQuantity<Length> x1 =  position.getY().subtract(v1.getY()).abs();
		BigDecimalQuantity<Length> dx1 = v2.getY().subtract(v1.getY()).abs();		
		BigDecimalQuantity<Length> a1z = v2.getZ().multiply(x1.divide(dx1)).add(v1.getZ().multiply( 1 - (x1.divide(dx1).doubleValue())));
		
		BigDecimalQuantity<Length> x2 =  position.getY().subtract(v3.getY()).abs();
		BigDecimalQuantity<Length> dx2 = v4.getY().subtract(v3.getY()).abs();
		BigDecimalQuantity<Length> a2z = v4.getZ().multiply(x2.divide(dx2)).add(v3.getZ().multiply( 1 - (x2.divide(dx2).doubleValue())));


		BigDecimalQuantity<Length> y1  = position.getX().subtract(v1.getX()).abs();
		BigDecimalQuantity<Length> dy1 = v3.getX().subtract(v1.getX()).abs();
		BigDecimalQuantity<Length> zFinal = a2z.multiply(y1.divide(dy1)).add(a1z.multiply(1 - (y1.divide(dy1).doubleValue() )));

		return zFinal;
		

//		double x1 =  Math.abs(position.getY().doubleValue() - v1.getY().doubleValue());
//		double dx1 = Math.abs(v2.getY().doubleValue() - v1.getY().doubleValue());
//		double a1z = v2.getZ().doubleValue() * (x1 / dx1) + v1.getZ().doubleValue() * (1- (x1/dx1));
//
//		double x2 = Math.abs(position.getY().doubleValue() - v3.getY().doubleValue());
//		double dx2 = Math.abs(v4.getY().doubleValue() - v3.getY().doubleValue());
//		double a2z = v4.getZ() * (x2 / dx2) + v3.getZ()  * (1- (x2/dx2));
//
//		double y1 = Math.abs(position.getX().doubleValue() - v1.getX().doubleValue());;
//		double dy1 = Math.abs(v3.getX().doubleValue() - v1.getX().doubleValue());;
//		double zFinal = a2z * (y1/dy1) + a1z * (1 - (y1/dy1));
//
//		return BigDecimal.valueOf(zFinal);
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
	public BigDecimalQuantity<Length> getStartProbePosition() throws GkException {
		return startProbeZ;
	}

	public void setStartProbePosition(BigDecimalQuantity<Length> start) {
		startProbeZ = start;
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationPattern#getEndProbePosition()
	 */
	@Override
	public BigDecimalQuantity<Length> getEndProbePosition() throws GkException {
		return maximalProbeZ;
	}

	public void setEndProbePosition(BigDecimalQuantity<Length> end) {
		maximalProbeZ = end;
	}
	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IAxisElevationMap#getProbedPositions()
	 */
	@Override
	public List<Tuple6b> getProbedPositions() {
		return probedPositions;
	}

}
