/**
 * 
 */
package org.goko.core.execution;

import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.quantity.SpeedUnit;
import org.goko.core.common.measure.quantity.Time;
import org.goko.core.math.Arc3b;
import org.goko.core.math.Tuple6b;

/**
 * @author Psyko
 * @date 13 avr. 2017
 */
public class ExecutionConstraint {
	/** Maximum feed by axis */
	private Speed xAxisMaximumFeed;
	private Speed yAxisMaximumFeed;
	private Speed zAxisMaximumFeed;
	private Speed aAxisMaximumFeed;
	
	public ExecutionConstraint(){
		xAxisMaximumFeed = Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE);
		yAxisMaximumFeed = Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE);
		zAxisMaximumFeed = Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE);
		aAxisMaximumFeed = Speed.valueOf(1000, SpeedUnit.MILLIMETRE_PER_MINUTE);
	}
	public Speed getMaximumFeedrate(Speed feedrate){
		return getMaximumFeedrate(feedrate, null);
	}
	
	public Speed getMaximumFeedrate(Speed feedrate, Tuple6b vector){
		Speed result = feedrate;
		if(vector == null  
				|| (vector.getX() != null && vector.getX().abs().greaterThan(Length.ZERO)) 
					&& xAxisMaximumFeed != null){
			result = QuantityUtils.min(result, xAxisMaximumFeed);
		}
		if(vector == null  
				|| (vector.getY() != null && vector.getY().abs().greaterThan(Length.ZERO)) 
					&& yAxisMaximumFeed != null){
			result = QuantityUtils.min(result, yAxisMaximumFeed);
		}
		if(vector == null 
				|| (vector.getZ() != null && vector.getZ().abs().greaterThan(Length.ZERO)) 
					&& zAxisMaximumFeed != null){
			result = QuantityUtils.min(result, zAxisMaximumFeed);
		}
		if(vector == null 
				|| (vector.getA() != null && vector.getA().abs().greaterThan(Angle.ZERO)) 
					&& aAxisMaximumFeed != null){
			result = QuantityUtils.min(result, aAxisMaximumFeed);
		}
		return result;
	}

	public Time getTravelTime(Speed feedrate, Tuple6b vector){
		Time result = Time.ZERO;
		if(feedrate != null && feedrate.greaterThan(Speed.ZERO)){
			if(vector.getX() != null && xAxisMaximumFeed != null){
				result = vector.getX().abs().divide(QuantityUtils.min(feedrate, xAxisMaximumFeed));
			}
			if(vector.getY() != null && yAxisMaximumFeed != null){
				result = QuantityUtils.max(result, vector.getY().abs().divide(QuantityUtils.min(feedrate, yAxisMaximumFeed)));
			}
			if(vector.getZ() != null && zAxisMaximumFeed != null){
				result = QuantityUtils.max(result, vector.getZ().abs().divide(QuantityUtils.min(feedrate, zAxisMaximumFeed)));
			}
		}
		return result;
	}
	
	public Time getTravelTime(Speed feedrate, Arc3b arc){
		Time result = Time.ZERO;
		Speed average = null;
		if(Arc3b.X_AXIS.equals(arc.getAxis())){
			if(yAxisMaximumFeed != null && zAxisMaximumFeed != null){
				average = yAxisMaximumFeed.add(zAxisMaximumFeed).divide(2);
			}
		}else if(Arc3b.Y_AXIS.equals(arc.getAxis())){
			if(xAxisMaximumFeed != null && zAxisMaximumFeed != null){
				average = xAxisMaximumFeed.add(zAxisMaximumFeed).divide(2);
			}
		}else if(Arc3b.Z_AXIS.equals(arc.getAxis())){
			if(xAxisMaximumFeed != null && yAxisMaximumFeed != null){
				average = xAxisMaximumFeed.add(yAxisMaximumFeed).divide(2);
			}
		}
		if(average != null){
			average = QuantityUtils.min(average, feedrate);
			result = arc.getLength().divide(average);
		}
		return result;
	}
	/**
	 * @return the xAxisMaximumFeed
	 */
	public Speed getXAxisMaximumFeed() {
		return xAxisMaximumFeed;
	}

	/**
	 * @param xAxisMaximumFeed the xAxisMaximumFeed to set
	 */
	public void setXAxisMaximumFeed(Speed xAxisMaximumFeed) {
		this.xAxisMaximumFeed = xAxisMaximumFeed;
	}

	/**
	 * @return the yAxisMaximumFeed
	 */
	public Speed getYAxisMaximumFeed() {
		return yAxisMaximumFeed;
	}

	/**
	 * @param yAxisMaximumFeed the yAxisMaximumFeed to set
	 */
	public void setYAxisMaximumFeed(Speed yAxisMaximumFeed) {
		this.yAxisMaximumFeed = yAxisMaximumFeed;
	}

	/**
	 * @return the zAxisMaximumFeed
	 */
	public Speed getZAxisMaximumFeed() {
		return zAxisMaximumFeed;
	}

	/**
	 * @param zAxisMaximumFeed the zAxisMaximumFeed to set
	 */
	public void setZAxisMaximumFeed(Speed zAxisMaximumFeed) {
		this.zAxisMaximumFeed = zAxisMaximumFeed;
	}

	/**
	 * @return the aAxisMaximumFeed
	 */
	public Speed getAAxisMaximumFeed() {
		return aAxisMaximumFeed;
	}

	/**
	 * @param aAxisMaximumFeed the aAxisMaximumFeed to set
	 */
	public void setAAxisMaximumFeed(Speed aAxisMaximumFeed) {
		this.aAxisMaximumFeed = aAxisMaximumFeed;
	}	
}
