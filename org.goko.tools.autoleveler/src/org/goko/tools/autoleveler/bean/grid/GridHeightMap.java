package org.goko.tools.autoleveler.bean.grid;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.measure.Units;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.utils.AbstractIdBean;
import org.goko.core.math.Tuple6b;
import org.goko.tools.autoleveler.bean.IHeightMap;

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
	/** The clearance height */
	private Length clearanceHeight;
	/** The probe start height */
	private Length probeStartHeight;
	/** The probe lower height */
	private Length probeLowerHeight;
	/** The probe feed rate */
	private Speed probeFeedrate;
	/** Boolean indicating that the map has been probed */
	private boolean isProbed;
		
	/**
	 * Constructor
	 * @param vertices the grid of offsets indexes
	 * @param offsets the list of indexes
	 */
	public GridHeightMap(int[][] vertices, List<Tuple6b> offsets) {
		super();
		this.vertices = vertices;
		this.offsets = offsets;
		this.xDivisionCount = vertices.length - 1;
		this.yDivisionCount = vertices[0].length - 1;
		this.start = offsets.get( vertices[0][0]);
		this.end   = offsets.get( vertices[xDivisionCount - 1][yDivisionCount - 1]);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.bean.IHeightMap#getHeight(org.goko.core.common.measure.quantity.type.BigDecimalQuantity, org.goko.core.common.measure.quantity.type.BigDecimalQuantity)
	 */
	@Override
	public Length getHeight(Length x, Length y) throws GkException {
		Tuple6b clippedPosition = new Tuple6b(x, y, Length.ZERO);
		if(x.greaterThan(start.getX()) && x.lowerThan(end.getX())
			&& y.greaterThan(start.getY()) && y.lowerThan(end.getY())){
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
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.bean.IHeightMap#splitSegment(org.goko.core.math.Tuple6b, org.goko.core.math.Tuple6b)
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
		start.setZ(Length.ZERO);
		end.setZ(Length.ZERO);
		{
			// Let's divide the segment along the X axis (in this case, segmenting line are along Y axis
			List<Length> lstIntersectionXCoordinate = getIntersectingDivisionsX(start, end);

			lstSubSegmentX.add(start);
			if(CollectionUtils.isNotEmpty(lstIntersectionXCoordinate)){
				for (Length xCoord : lstIntersectionXCoordinate) {
					Length computedY = start.getY().add( xCoord.subtract(start.getX()).multiply(slope) );
					Tuple6b pt = new Tuple6b(Units.MILLIMETRE, xCoord, computedY, Length.ZERO);
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
				List<Length> lstIntersectionYCoordinate = getIntersectingDivisionsY(tmpStart, tmpEnd);
				lstSubSegment.add(tmpStart);
				if(CollectionUtils.isNotEmpty(lstIntersectionYCoordinate)){
					for (Length yCoord : lstIntersectionYCoordinate) {
						Length computedX = tmpStart.getX();
						if(!slope.equals(BigDecimal.ZERO)){
							computedX = yCoord.subtract(tmpStart.getY()).divide(slope).add( tmpStart.getX() );
						}
						Tuple6b pt = new Tuple6b(Units.MILLIMETRE, computedX, yCoord, Length.ZERO);
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
	private List<Length> getIntersectingDivisionsX(Tuple6b start, Tuple6b end){
		List<Length> lstXPosition = new ArrayList<>();
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
	private List<Length> getIntersectingDivisionsY(Tuple6b start, Tuple6b end){
		List<Length> lstYPosition = new ArrayList<>();
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
	private int getCellXIndex(Length x){
		int cellIndex = 0;
		for(int i = 0; i <= xDivisionCount ; i++){
			Tuple6b gridPoint = offsets.get(vertices[i][0]);
			if(gridPoint.getX().greaterThanOrEqualTo(x) ){
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
	private int getCellYIndex(Length y){
		int cellIndex = 0;
		for(int i = 0; i <= yDivisionCount; i++){
			Tuple6b gridPoint = offsets.get(vertices[0][i]);
			if(gridPoint.getY().greaterThanOrEqualTo(y) ){
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
	private Length findOffsetBilinear(Tuple6b position,Tuple6b v1,Tuple6b v2,Tuple6b v3,Tuple6b v4){
		Length x1 =  position.getY().subtract(v1.getY()).abs();
		Length dx1 = v2.getY().subtract(v1.getY()).abs();
		Length a1z = v2.getZ().multiply(x1.divide(dx1)).add(v1.getZ().multiply( BigDecimal.ONE.subtract(x1.divide(dx1)) ));

		Length x2 =  position.getY().subtract(v3.getY()).abs();
		Length dx2 = v4.getY().subtract(v3.getY()).abs();
		Length a2z = v4.getZ().multiply(x2.divide(dx2)).add(v3.getZ().multiply( BigDecimal.ONE.subtract(x2.divide(dx2) )));

		Length y1  = position.getX().subtract(v1.getX()).abs();
		Length dy1 = v3.getX().subtract(v1.getX()).abs();
		Length zFinal = a2z.multiply(y1.divide(dy1)).add(a1z.multiply( BigDecimal.ONE.subtract(y1.divide(dy1) )));

		return zFinal;
	}
	
	/**
	 * @return the offsets
	 */
	public List<Tuple6b> getOffsets() {
		return offsets;
	}

	public Tuple6b getPoint(int x, int y){
		if(vertices != null && vertices.length > x){
			if(vertices[x] != null && vertices[x].length > y){
				int index = vertices[x][y];
				return offsets.get(index);
			}	
		}
		return null;
	}
	
	public void build() throws GkException{
		if(xDivisionCount <= 0 || yDivisionCount <= 0){
			throw new GkTechnicalException("X/Y division count should be positive");
		}
		Length dx = end.getX().subtract(start.getX()).divide(xDivisionCount);
		Length dy = end.getY().subtract(start.getY()).divide(yDivisionCount);
		
		offsets = new ArrayList<Tuple6b>();
		vertices = new int[xDivisionCount + 1][yDivisionCount + 1];
		for(int x = 0 ; x <= xDivisionCount; x++){			
			Length xCoord = start.getX().add(dx.multiply(x));
			for(int y = 0 ; y <= yDivisionCount; y++){
				vertices[x][y] = offsets.size();
				offsets.add(new Tuple6b(xCoord, start.getY().add(dy.multiply(y)), Length.ZERO));
			}
		}			
	}
	/**
	 * @param offsets the offsets to set
	 */
	public void setOffsets(List<Tuple6b> offsets) {
		this.offsets = offsets;
	}

	/**
	 * @return the start
	 */
	public Tuple6b getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Tuple6b start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Tuple6b getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Tuple6b end) {
		this.end = end;
	}

	/**
	 * @return the xDivisionCount
	 */
	public int getxDivisionCount() {
		return xDivisionCount;
	}

	/**
	 * @param xDivisionCount the xDivisionCount to set
	 */
	public void setxDivisionCount(int xDivisionCount) {
		this.xDivisionCount = xDivisionCount;
	}

	/**
	 * @return the yDivisionCount
	 */
	public int getyDivisionCount() {
		return yDivisionCount;
	}

	/**
	 * @param yDivisionCount the yDivisionCount to set
	 */
	public void setyDivisionCount(int yDivisionCount) {
		this.yDivisionCount = yDivisionCount;
	}

	/**
	 * @return
	 */
	public Length getStepSizeX() {		
		return end.getX().subtract(start.getX()).divide(xDivisionCount);
	}
	
	/**
	 * @return
	 */
	public Length getStepSizeY() {		
		return end.getY().subtract(start.getY()).divide(yDivisionCount);
	}

	/**
	 * @return the clearanceHeight
	 */
	public Length getClearanceHeight() {
		return clearanceHeight;
	}

	/**
	 * @param clearanceHeight the clearanceHeight to set
	 */
	public void setClearanceHeight(Length clearanceHeight) {
		this.clearanceHeight = clearanceHeight;
	}

	/**
	 * @return the probeStartHeight
	 */
	public Length getProbeStartHeight() {
		return probeStartHeight;
	}

	/**
	 * @param probeStartHeight the probeStartHeight to set
	 */
	public void setProbeStartHeight(Length probeStartHeight) {
		this.probeStartHeight = probeStartHeight;
	}

	/**
	 * @return the probeLowerHeight
	 */
	public Length getProbeLowerHeight() {
		return probeLowerHeight;
	}

	/**
	 * @param probeLowerHeight the probeLowerHeight to set
	 */
	public void setProbeLowerHeight(Length probeLowerHeight) {
		this.probeLowerHeight = probeLowerHeight;
	}

	/**
	 * @return the probeFeedrate
	 */
	public Speed getProbeFeedrate() {
		return probeFeedrate;
	}

	/**
	 * @param probeFeedrate the probeFeedrate to set
	 */
	public void setProbeFeedrate(Speed probeFeedrate) {
		this.probeFeedrate = probeFeedrate;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.autoleveler.bean.IHeightMap#isProbed()
	 */
	@Override
	public boolean isProbed() {
		return isProbed;
	}

	/**
	 * @param isProbed the isProbed to set
	 */
	public void setProbed(boolean isProbed) {
		this.isProbed = isProbed;
	}
	
	


}
