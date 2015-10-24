/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.core.math;

import java.math.BigDecimal;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import org.goko.core.common.measure.SI;
import org.goko.core.common.measure.SIPrefix;
import org.goko.core.common.measure.quantity.Angle;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Quantity;
import org.goko.core.common.measure.quantity.QuantityUtils;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
import org.goko.core.common.measure.quantity.type.NumberQuantity;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.common.utils.BigDecimalUtils;

public class Tuple6b {
	private BigDecimalQuantity<Length> x;
	private BigDecimalQuantity<Length> y;
	private BigDecimalQuantity<Length> z;
	private BigDecimalQuantity<Angle> a;
	private BigDecimalQuantity<Angle> b;
	private BigDecimalQuantity<Angle> c;

	public Tuple6b(){
		this(SIPrefix.MILLI(SI.METRE), SI.DEGREE_ANGLE);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param a
	 * @param b
	 * @param c
	 */
	public Tuple6b(BigDecimalQuantity<Length> x, BigDecimalQuantity<Length> y, BigDecimalQuantity<Length> z,
			BigDecimalQuantity<Angle> a, BigDecimalQuantity<Angle> b, BigDecimalQuantity<Angle> c) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public Tuple6b(Tuple6b tuple){
		this.x = tuple.x;
		this.y = tuple.y;
		this.z = tuple.z;
		this.a = tuple.a;
		this.b = tuple.b;
		this.c = tuple.c;
	}

	public Tuple6b(Unit<Length> unit, Unit<Angle> angleUnit) {
		super();
		this.x = NumberQuantity.of(BigDecimal.ZERO, unit);
		this.y = NumberQuantity.of(BigDecimal.ZERO, unit);
		this.z = NumberQuantity.of(BigDecimal.ZERO, unit);
		this.a = NumberQuantity.of(BigDecimal.ZERO, angleUnit);
		this.b = NumberQuantity.of(BigDecimal.ZERO, angleUnit);
		this.c = NumberQuantity.of(BigDecimal.ZERO, angleUnit);
	}
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple6b(double x, double y, double z, Unit<Length> unit) {
		super();
		this.x = NumberQuantity.of(new BigDecimal(String.valueOf(x)), unit);
		this.y = NumberQuantity.of(new BigDecimal(String.valueOf(y)), unit);
		this.z = NumberQuantity.of(new BigDecimal(String.valueOf(z)), unit);
	}
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple6b(BigDecimal x, BigDecimal y, BigDecimal z, Unit<Length> unit) {
		super();
		this.x = NumberQuantity.of(x, unit);
		this.y = NumberQuantity.of(y, unit);
		this.z = NumberQuantity.of(z, unit);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param a
	 * @param b
	 * @param c
	 */
	public Tuple6b(BigDecimal x, BigDecimal y, BigDecimal z, BigDecimal a, BigDecimal b, BigDecimal c, Unit<Length> unit, Unit<Angle> angleUnit) {
		super();
		this.x = NumberQuantity.of(x, unit);
		this.y = NumberQuantity.of(y, unit);
		this.z = NumberQuantity.of(z, unit);
		this.a = NumberQuantity.of(a, angleUnit);
		this.b = NumberQuantity.of(b, angleUnit);
		this.c = NumberQuantity.of(c, angleUnit);
	}

	public Tuple6b(Unit<Length> unit, BigDecimalQuantity<Length> qx, BigDecimalQuantity<Length> qy, BigDecimalQuantity<Length> qz) {
		super();
		this.x = qx.to(unit);
		this.y = qy.to(unit);
		this.z = qz.to(unit);
	}

	public void updateRelative(Tuple6b position){
		this.x = atomUpdateRelative(x, position.x);
		this.y = atomUpdateRelative(y, position.y);
		this.z = atomUpdateRelative(z, position.z);
		this.a = atomUpdateRelative(a, position.a);
		this.b = atomUpdateRelative(b, position.b);
		this.c = atomUpdateRelative(c, position.c);
	}

	protected <Q extends Quantity<Q>> BigDecimalQuantity<Q> atomUpdateRelative(BigDecimalQuantity<Q> a, BigDecimalQuantity<Q> b) {
		if( a != null && b!=null){
			return (BigDecimalQuantity<Q>) a.add(b);
		}else if( a != null){
			return a;
		}else if(b != null){
			return b;
		}
		return null;
	}

	public Tuple6b min(Tuple6b t){
		Tuple6b result = new Tuple6b(this);
		result.x = QuantityUtils.min(x, t.x);
		result.y = QuantityUtils.min(y, t.y);
		result.z = QuantityUtils.min(z, t.z);
		result.a = QuantityUtils.min(a, t.a);
		result.b = QuantityUtils.min(b, t.b);
		result.c = QuantityUtils.min(c, t.c);
		return result;
	}
	
	public void min(Tuple6b a, Tuple6b b){		
		this.x = QuantityUtils.min(a.x, b.x);
		this.y = QuantityUtils.min(a.y, b.y);
		this.z = QuantityUtils.min(a.z, b.z);
		this.a = QuantityUtils.min(a.a, b.a);
		this.b = QuantityUtils.min(a.b, b.b);
		this.c = QuantityUtils.min(a.c, b.c);		
	}
	
	public Tuple6b max(Tuple6b t){
		Tuple6b result = new Tuple6b(this);
		result.x = QuantityUtils.max(x, t.x);
		result.y = QuantityUtils.max(y, t.y);
		result.z = QuantityUtils.max(z, t.z);
		result.a = QuantityUtils.max(a, t.a);
		result.b = QuantityUtils.max(b, t.b);
		result.c = QuantityUtils.max(c, t.c);
		return result;
	}
	
	public void max(Tuple6b a, Tuple6b b){		
		this.x = QuantityUtils.max(a.x, b.x);
		this.y = QuantityUtils.max(a.y, b.y);
		this.z = QuantityUtils.max(a.z, b.z);
		this.a = QuantityUtils.max(a.a, b.a);
		this.b = QuantityUtils.max(a.b, b.b);
		this.c = QuantityUtils.max(a.c, b.c);		
	}
	
	public Tuple6b subtract(Tuple6b sub){
		Tuple6b result = new Tuple6b(this);
		result.x = QuantityUtils.subtract(x, sub.x);
		result.y = QuantityUtils.subtract(y, sub.y);
		result.z = QuantityUtils.subtract(z, sub.z);
		result.a = QuantityUtils.subtract(a, sub.a);
		result.b = QuantityUtils.subtract(b, sub.b);
		result.c = QuantityUtils.subtract(c, sub.c);
		return result;
	}

	public Tuple6b add(Tuple6b sub){
		Tuple6b result = new Tuple6b(this);
		result.x = QuantityUtils.add(x, sub.x);
		result.y = QuantityUtils.add(y, sub.y);
		result.z = QuantityUtils.add(z, sub.z);
		result.a = QuantityUtils.add(a, sub.a);
		result.b = QuantityUtils.add(b, sub.b);
		result.c = QuantityUtils.add(c, sub.c);
		return result;
	}

	public void updateAbsolute(Tuple6b position){
		this.x = atomUpdateAbsolute(x, position.x);
		this.y = atomUpdateAbsolute(y, position.y);
		this.z = atomUpdateAbsolute(z, position.z);
		this.a = atomUpdateAbsolute(a, position.a);
		this.b = atomUpdateAbsolute(b, position.b);
		this.c = atomUpdateAbsolute(c, position.c);
	}

	protected <Q extends Quantity<Q>> BigDecimalQuantity<Q> atomUpdateAbsolute(BigDecimalQuantity<Q> a, BigDecimalQuantity<Q> b) {
		if( a != null && b!=null){
			return b;
		}else if( a != null){
			return a;
		}else if(b != null){
			return b;
		}
		return null;
	}
	/**
	 * @return the x
	 */
	public BigDecimalQuantity<Length> getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX( BigDecimalQuantity<Length> x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public  BigDecimalQuantity<Length> getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY( BigDecimalQuantity<Length> y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public  BigDecimalQuantity<Length> getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ( BigDecimalQuantity<Length> z) {
		this.z = z;
	}
	/**
	 * @return the a
	 */
	public  BigDecimalQuantity<Angle> getA() {
		return a;
	}
	/**
	 * @param a the a to set
	 */
	public void setA( BigDecimalQuantity<Angle> a) {
		this.a = a;
	}
	/**
	 * @return the b
	 */
	public BigDecimalQuantity<Angle> getB() {
		return b;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(BigDecimalQuantity<Angle> b) {
		this.b = b;
	}
	/**
	 * @return the c
	 */
	public BigDecimalQuantity<Angle> getC() {
		return c;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(BigDecimalQuantity<Angle> c) {
		this.c = c;
	}

	public Point3d toPoint3d(Unit<Length> unit){
		return new Point3d(getX().to(unit).doubleValue(), getY().to(unit).doubleValue(), getZ().to(unit).doubleValue());
	}

	public Point3d angleToPoint3d(Unit<Angle> unit){
		Point3d angle = new Point3d();
		if(a != null){
			angle.x = a.to(unit).doubleValue();
		}
		if(b != null){
			angle.y = b.to(unit).doubleValue();
		}
		if(c != null){
			angle.z = c.to(unit).doubleValue();
		}
		return angle;
	}
	
	public Point3f toPoint3f(Unit<Length> unit){
		return new Point3f((float)getX().to(unit).doubleValue(), (float)getY().to(unit).doubleValue(), (float)getZ().to(unit).doubleValue());
	}
	
	public Tuple6b setNull() {
		this.x = null;
		this.y = null;
		this.z = null;
		this.a = null;
		this.b = null;
		this.c = null;
		return this;
	}

	public Tuple6b to(Unit<Length> unit){
		Tuple6b result = new Tuple6b(this);
		result.setX( getX().to(unit) );
		result.setY( getY().to(unit) );
		result.setZ( getZ().to(unit) );
		return result;
	}
	
	public Tuple6b toAngle(Unit<Angle> unit){
		Tuple6b result = new Tuple6b(this);
		result.setA( getA().to(unit) );
		result.setB( getB().to(unit) );
		result.setC( getC().to(unit) );
		return result;
	}
	
	public Tuple6b setZero() {
		this.x = NumberQuantity.of(BigDecimal.ZERO, SI.METRE);
		this.y = NumberQuantity.of(BigDecimal.ZERO, SI.METRE);
		this.z = NumberQuantity.of(BigDecimal.ZERO, SI.METRE);
		this.a = NumberQuantity.of(BigDecimal.ZERO, SI.DEGREE_ANGLE);
		this.b = NumberQuantity.of(BigDecimal.ZERO, SI.DEGREE_ANGLE);
		this.c = NumberQuantity.of(BigDecimal.ZERO, SI.DEGREE_ANGLE);
		return this;
	}

	public Tuple6b normalize(){
		Unit<Length> unit = x.getUnit();
		BigDecimalQuantity<Length> pX = this.x.to(unit);
		BigDecimalQuantity<Length> pY = this.y.to(unit);
		BigDecimalQuantity<Length> pZ = this.z.to(unit);		
		BigDecimal length = length().value(unit);
		pX.divide(length);
		pY.divide(length);
		pZ.divide(length);
		return new Tuple6b(pX, pY, pZ, a, b, c);
	}
	
	public BigDecimalQuantity<Length> length(){
		BigDecimal bdX = this.x.value(this.x.getUnit());		
		BigDecimal bdY = this.y.value(this.x.getUnit());		
		BigDecimal bdZ = this.z.value(this.x.getUnit());
		
		BigDecimal bdx2 = bdX.multiply(bdX);
		BigDecimal bdy2 = bdX.multiply(bdY);
		BigDecimal bdz2 = bdX.multiply(bdZ);
		return NumberQuantity.of(BigDecimalUtils.sqrt(bdx2.add(bdy2).add(bdz2),5), this.x.getUnit());
	}
	
	public BigDecimalQuantity<Length> distance(Tuple6b target){		
		BigDecimalQuantity<Length> dx = target.getX().subtract(x); 
		BigDecimalQuantity<Length> dy = target.getY().subtract(y); 
		BigDecimalQuantity<Length> dz = target.getZ().subtract(z);
		BigDecimal dx2 = dx.getValue().pow(2);
		BigDecimal dy2 = dy.getValue().pow(2);
		BigDecimal dz2 = dz.getValue().pow(2);
		BigDecimal result = BigDecimalUtils.sqrt(dx2.add(dy2).add(dz2), dx.getValue().scale());
		
		return NumberQuantity.of(result, dx.getUnit());
	}
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
		result = prime * result + ((z == null) ? 0 : z.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Tuple6b other = (Tuple6b) obj;
		if (a == null) {
			if (other.a != null) {
				return false;
			}
		} else if (!a.equals(other.a)) {
			return false;
		}
		if (b == null) {
			if (other.b != null) {
				return false;
			}
		} else if (!b.equals(other.b)) {
			return false;
		}
		if (c == null) {
			if (other.c != null) {
				return false;
			}
		} else if (!c.equals(other.c)) {
			return false;
		}
		if (x == null) {
			if (other.x != null) {
				return false;
			}
		} else if (!x.equals(other.x)) {
			return false;
		}
		if (y == null) {
			if (other.y != null) {
				return false;
			}
		} else if (!y.equals(other.y)) {
			return false;
		}
		if (z == null) {
			if (other.z != null) {
				return false;
			}
		} else if (!z.equals(other.z)) {
			return false;
		}
		return true;
	}

}
