package org.goko.autoleveler.bean.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.autoleveler.bean.IHeightMap;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.core.math.Tuple6b;

public class GridHeightMap extends AbstractIdBean implements IHeightMap {
	/** The spatial repartition of positions */
	private int[][] vertices;
	/** The indexed list of position */
	private List<Tuple6b> offsets;
	/** The start point of this map */
	private Tuple6b start;
	/** The end point of this map */
	private Tuple6b end;
	/** Number of divisions on the X axis*/
	private int xDivisionCount;
	/** Number of divisions on the Y axis*/
	private int yDivisionCount;

	/**
	 * Constructor
	 * @param vertices the grid of offsets indexes
	 * @param offsets the list of indexes
	 */
	public GridHeightMap(int[][] vertices, List<Tuple6b> offsets) {
		super();
		this.vertices = vertices;
		this.offsets = offsets;
		this.xDivisionCount = vertices.length;
		this.yDivisionCount = vertices[0].length;
		this.start = offsets.get( vertices[0][0]);
		this.end   = offsets.get( vertices[xDivisionCount - 1][yDivisionCount - 1]);
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMap#getHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity, org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public BigDecimalQuantity<Length> getHeight(BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y) throws GkException {
		Tuple6b clippedPosition = new Tuple6b(x.getUnit(), x, y, NumberQuantity.of(BigDecimal.ZERO, x.getUnit()));
		// Clamp the target position in the map area
		clippedPosition = clippedPosition.max(start);
		clippedPosition = clippedPosition.min(end);

		int cellXIndex = getCellXIndex(x);
		int cellYIndex = getCellYIndex(y);

		// Get the corner position around the target point
		Tuple6b pA = offsets.get( vertices[cellXIndex  ][cellYIndex  ]);
		Tuple6b pB = offsets.get( vertices[cellXIndex  ][cellYIndex+1]);
		Tuple6b pC = offsets.get( vertices[cellXIndex+1][cellYIndex  ]);
		Tuple6b pD = offsets.get( vertices[cellXIndex+1][cellYIndex+1]);

		return findOffsetBilinear(clippedPosition, pA, pB, pC, pD);
	}

	/** (inheritDoc)
	 * @see org.goko.autoleveler.bean.IHeightMap#splitSegment(org.goko.core.math.Tuple6b, org.goko.core.math.Tuple6b)
	 */
	@Override
	public List<Tuple6b> splitSegment(final Tuple6b pStart, final Tuple6b pEnd) throws GkException {
		List<Tuple6b> lstSubSegment = new ArrayList<Tuple6b>();
		List<Tuple6b> lstSubSegmentX = new ArrayList<Tuple6b>();
		// The given segment is a purely vertical segment, we won't split it
		if( pEnd.getX().equals(pStart.getX())
		&&  pEnd.getY().equals(pStart.getY())){
			lstSubSegment.add(pStart);
			lstSubSegment.add(pEnd);
			return lstSubSegment;
		}

		BigDecimal slope = BigDecimal.ZERO;
		if( !pEnd.getX().equals(pStart.getX()) ){
			slope = (pEnd.getY().subtract(pStart.getY())).divide(pEnd.getX().subtract(pStart.getX()));
		}

		Tuple6b start = new Tuple6b(pStart);
		Tuple6b end = new Tuple6b(pEnd);
		start.setZ(NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE));
		end.setZ(NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE));
		{
			// Let's divide the segment along the X axis (in this case, segmenting line are along Y axis
			List<BigDecimalQuantity<Length>> lstIntersectionXCoordinate = getIntersectingDivisionsX(start, end);

			lstSubSegmentX.add(start);
			if(CollectionUtils.isNotEmpty(lstIntersectionXCoordinate)){
				for (BigDecimalQuantity<Length> xCoord : lstIntersectionXCoordinate) {
					BigDecimalQuantity<Length> computedY = start.getY().add( xCoord.subtract(start.getX()).multiply(slope) );
					Tuple6b pt = new Tuple6b(Units.MILLIMETRE, xCoord, computedY, NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE));
					lstSubSegmentX.add(pt);
				}
			}

			lstSubSegmentX.add(end);
		}
		{
			// Now we can divise along the Y axis
			int iterationMax = lstSubSegmentX.size() - 1;
			for (int j = 0; j < iterationMax; j++) {
				Tuple6b tmpStart = lstSubSegmentX.get(j);
				Tuple6b tmpEnd = lstSubSegmentX.get(j+1);
				List<BigDecimalQuantity<Length>> lstIntersectionYCoordinate = getIntersectingDivisionsY(tmpStart, tmpEnd);
				lstSubSegment.add(tmpStart);
				if(CollectionUtils.isNotEmpty(lstIntersectionYCoordinate)){
					for (BigDecimalQuantity<Length> yCoord : lstIntersectionYCoordinate) {
						BigDecimalQuantity<Length> computedX = tmpStart.getX();
						if(!slope.equals(BigDecimal.ZERO)){
							computedX = yCoord.subtract(tmpStart.getY()).divide(slope).add( tmpStart.getX() );
						}
						Tuple6b pt = new Tuple6b(Units.MILLIMETRE, computedX, yCoord, NumberQuantity.of(BigDecimal.ZERO, Units.MILLIMETRE));
						lstSubSegment.add(pt);
					}
				}
			}
			lstSubSegment.add(end);
		}
		return lstSubSegment;
	}

	/**
	 * Returns the ordered list of position along the X axis which intersects the given segment. The points are ordered from start to end
	 * @param start the start point of the segment
	 * @param end the end point of the segment
	 * @return a list of Quantity
	 */
	private List<BigDecimalQuantity<Length>> getIntersectingDivisionsX(Tuple6b start, Tuple6b end){
		List<BigDecimalQuantity<Length>> lstXPosition = new ArrayList<>();
		// Start < End
		if(start.getX().lowerThanOrEqualTo(end.getX())){
			for(int i = 0; i < xDivisionCount ; i++){
				Tuple6b gridPoint = offsets.get(vertices[i][0]);
				if(gridPoint.getX().greaterThan(start.getX()) && gridPoint.getX().lowerThan(end.getX())){
					lstXPosition.add( gridPoint.getX() );
				}else if(gridPoint.getX().greaterThanOrEqualTo(end.getX())){
					break;
				}
			}
		}else{
			// Start > End
			for(int i = xDivisionCount - 1; i >= 0  ; i--){
				Tuple6b gridPoint = offsets.get(vertices[i][0]);
				if(gridPoint.getX().greaterThan(end.getX()) && gridPoint.getX().lowerThan(start.getX())){
					lstXPosition.add( gridPoint.getX() );
				}else if(gridPoint.getX().lowerThanOrEqualTo(start.getX())){
					break;
				}
			}
		}
		return lstXPosition;
	}

	/**
	 * Returns the ordered list of position along the Y axis which intersects the given segment. The points are ordered from start to end
	 * @param start the start point of the segment
	 * @param end the end point of the segment
	 * @return a list of Quantity
	 */
	private List<BigDecimalQuantity<Length>> getIntersectingDivisionsY(Tuple6b start, Tuple6b end){
		List<BigDecimalQuantity<Length>> lstYPosition = new ArrayList<>();
		// Start < End
		if(start.getY().lowerThanOrEqualTo(end.getY())){
			for(int i = 0; i < yDivisionCount ; i++){
				Tuple6b gridPoint = offsets.get(vertices[0][i]);
				if(gridPoint.getY().greaterThan(start.getY()) && gridPoint.getY().lowerThan(end.getY())){
					lstYPosition.add( gridPoint.getY() );
				}else if(gridPoint.getY().greaterThanOrEqualTo(end.getY())){
					break;
				}
			}
		}else{
			// End < Start
			for(int i = yDivisionCount - 1; i >= 0  ; i--){
				Tuple6b gridPoint = offsets.get(vertices[0][i]);
				if(gridPoint.getY().greaterThan(end.getY()) && gridPoint.getY().lowerThan(start.getY())){
					lstYPosition.add( gridPoint.getY() );
				}else if(gridPoint.getY().lowerThanOrEqualTo(start.getY())){
					break;
				}
			}
		}
		return lstYPosition;
	}

	/**
	 * Returns the index row containing the X value of the given position
	 * @param position the row position
	 * @return integer
	 */
	private int getCellXIndex(BigDecimalQuantity<Length> x){
		int cellIndex = 0;
		for(int i = 0; i < xDivisionCount - 1 ; i++){
			Tuple6b gridPoint = offsets.get(vertices[i][0]);
			if(gridPoint.getX().doubleValue(Units.MILLIMETRE) >= x.doubleValue(Units.MILLIMETRE) ){
				break;
			}
			cellIndex = i;
		}
		return cellIndex;
	}

	/**
	 * Returns the index column containing the Y value of the given position
	 * @param position the column position
	 * @return integer
	 */
	private int getCellYIndex(BigDecimalQuantity<Length> y){
		int cellIndex = 0;
		for(int i = 0; i < yDivisionCount - 1; i++){
			Tuple6b gridPoint = offsets.get(vertices[0][i]);
			if(gridPoint.getY().doubleValue(Units.MILLIMETRE) >= y.doubleValue(Units.MILLIMETRE) ){
				break;
			}
			cellIndex = i;
		}
		return cellIndex;
	}

	/**
	 * Find the interpolated height of the given position using a bilinear interpolation of the height probed at the 4 corners
	 * @param position the calculation position
	 * @param v1 probed position of corner 1
	 * @param v2 probed position of corner 2
	 * @param v3 probed position of corner 3
	 * @param v4 probed position of corner 4
	 * @return the probed height
	 */
	private BigDecimalQuantity<Length> findOffsetBilinear(Tuple6b position,Tuple6b v1,Tuple6b v2,Tuple6b v3,Tuple6b v4){
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
	}
	/**
	 * Test method
	 * @param args arguments
	 * @throws GkException GkException
	 */
	public static void main(String[] args) throws GkException {
		List<Tuple6b> lOffsets = new ArrayList<>();
		lOffsets.add( new Tuple6b(new BigDecimal("0"), new BigDecimal("0"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("10"), new BigDecimal("0"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("20"), new BigDecimal("0"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("0"), new BigDecimal("10"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("10"), new BigDecimal("10"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("20"), new BigDecimal("10"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("0"), new BigDecimal("20"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("10"), new BigDecimal("20"), new BigDecimal("0"), Units.MILLIMETRE));
		lOffsets.add( new Tuple6b(new BigDecimal("20"), new BigDecimal("20"), new BigDecimal("0"), Units.MILLIMETRE));

		int[][] lVertices = new int[][]{{0,3,6},{1,4,7},{2,5,8}};
		GridHeightMap map = new GridHeightMap(lVertices, lOffsets);

		Tuple6b pStart = new Tuple6b(new BigDecimal("5"), new BigDecimal("5"), new BigDecimal("0"), Units.MILLIMETRE);
		Tuple6b pEnd   = new Tuple6b(new BigDecimal("15"), new BigDecimal("5"), new BigDecimal("0"), Units.MILLIMETRE);
		List<Tuple6b> lst = map.splitSegment(pStart, pEnd);
		long t = System.currentTimeMillis();
//		for(int i = 0; i < 100000; i++){
//			map.splitSegment(pStart, pEnd);
//		}
		System.out.println( (System.currentTimeMillis() - t) +"ms");
	}
}
