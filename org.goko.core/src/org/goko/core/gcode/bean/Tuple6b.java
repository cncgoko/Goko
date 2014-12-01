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
package org.goko.core.gcode.bean;

import java.math.BigDecimal;

import javax.vecmath.Point3d;

public class Tuple6b {
	private BigDecimal x;
	private BigDecimal y;
	private BigDecimal z;
	private BigDecimal a;
	private BigDecimal b;
	private BigDecimal c;

	public Tuple6b(Tuple6b tuple){
		this.x = tuple.x;
		this.y = tuple.y;
		this.z = tuple.z;
		this.a = tuple.a;
		this.b = tuple.b;
		this.c = tuple.c;
	}

	public Tuple6b() {
		super();
		this.x = new BigDecimal("0");
		this.y = new BigDecimal("0");
		this.z = new BigDecimal("0");
		this.a = new BigDecimal("0");
		this.b = new BigDecimal("0");
		this.c = new BigDecimal("0");
	}
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple6b(double x, double y, double z) {
		super();
		this.x = new BigDecimal(String.valueOf(x));
		this.y = new BigDecimal(String.valueOf(y));
		this.z = new BigDecimal(String.valueOf(z));
	}
	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple6b(BigDecimal x, BigDecimal y, BigDecimal z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param a
	 * @param b
	 * @param c
	 */
	public Tuple6b(BigDecimal x, BigDecimal y, BigDecimal z, BigDecimal a, BigDecimal b, BigDecimal c) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
		this.b = b;
		this.c = c;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param a
	 */
	public Tuple6b(BigDecimal x, BigDecimal y, BigDecimal z, BigDecimal a) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.a = a;
	}

	public void updateRelative(Tuple6b position){
		this.x = atomUpdateRelative(x, position.x);
		this.y = atomUpdateRelative(y, position.y);
		this.z = atomUpdateRelative(z, position.z);
		this.a = atomUpdateRelative(a, position.a);
		this.b = atomUpdateRelative(b, position.b);
		this.c = atomUpdateRelative(c, position.c);
	}

	protected BigDecimal atomUpdateRelative(BigDecimal a, BigDecimal b) {
		if( a != null && b!=null){
			return a.add(b);
		}else if( a != null){
			return a;
		}else if(b != null){
			return b;
		}
		return null;
	}

	public Tuple6b min(Tuple6b t){
		Tuple6b result = new Tuple6b(this);
		result.x = atomMin(x, t.x);
		result.y = atomMin(y, t.y);
		result.z = atomMin(z, t.z);
		result.a = atomMin(a, t.a);
		result.b = atomMin(b, t.b);
		result.c = atomMin(c, t.c);
		return result;
	}
	public Tuple6b max(Tuple6b t){
		Tuple6b result = new Tuple6b(this);
		result.x = atomMax(x, t.x);
		result.y = atomMax(y, t.y);
		result.z = atomMax(z, t.z);
		result.a = atomMax(a, t.a);
		result.b = atomMax(b, t.b);
		result.c = atomMax(c, t.c);
		return result;
	}
	private BigDecimal atomMin(BigDecimal a, BigDecimal b) {
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		return a.min(b);
	}
	private BigDecimal atomMax(BigDecimal a, BigDecimal b) {
		if(a == null){
			return b;
		}else if(b == null){
			return a;
		}
		return a.max(b);
	}

	public Tuple6b subtract(Tuple6b sub){
		Tuple6b result = new Tuple6b(this);
		result.x = atomSubtract(x, sub.x);
		result.y = atomSubtract(y, sub.y);
		result.z = atomSubtract(z, sub.z);
		result.a = atomSubtract(a, sub.a);
		result.b = atomSubtract(b, sub.b);
		result.c = atomSubtract(c, sub.c);
		return result;
	}

	public Tuple6b add(Tuple6b sub){
		Tuple6b result = new Tuple6b(this);
		result.x = atomAdd(x, sub.x);
		result.y = atomAdd(y, sub.y);
		result.z = atomAdd(z, sub.z);
		result.a = atomAdd(a, sub.a);
		result.b = atomAdd(b, sub.b);
		result.c = atomAdd(c, sub.c);
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

	protected BigDecimal atomSubtract(BigDecimal a, BigDecimal b) {
		if( a != null && b!=null){
			return a.subtract(b);
		}else if( a != null){
			return a;
		}else if(b != null){
			return b.negate();
		}
		return null;
	}

	protected BigDecimal atomAdd(BigDecimal a, BigDecimal b) {
		if( a != null && b!=null){
			return a.add(b);
		}else if( a != null){
			return a;
		}else if(b != null){
			return b;
		}
		return null;
	}

	protected BigDecimal atomUpdateAbsolute(BigDecimal a, BigDecimal b) {
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
	public BigDecimal getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(BigDecimal x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public BigDecimal getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(BigDecimal y) {
		this.y = y;
	}
	/**
	 * @return the z
	 */
	public BigDecimal getZ() {
		return z;
	}
	/**
	 * @param z the z to set
	 */
	public void setZ(BigDecimal z) {
		this.z = z;
	}
	/**
	 * @return the a
	 */
	public BigDecimal getA() {
		return a;
	}
	/**
	 * @param a the a to set
	 */
	public void setA(BigDecimal a) {
		this.a = a;
	}
	/**
	 * @return the b
	 */
	public BigDecimal getB() {
		return b;
	}
	/**
	 * @param b the b to set
	 */
	public void setB(BigDecimal b) {
		this.b = b;
	}
	/**
	 * @return the c
	 */
	public BigDecimal getC() {
		return c;
	}
	/**
	 * @param c the c to set
	 */
	public void setC(BigDecimal c) {
		this.c = c;
	}

	public Point3d toPoint3d(){
		return new Point3d(getX().doubleValue(), getY().doubleValue(), getZ().doubleValue());
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

	public Tuple6b setZero() {
		this.x = new BigDecimal("0");
		this.y = new BigDecimal("0");
		this.z = new BigDecimal("0");
		this.a = new BigDecimal("0");
		this.b = new BigDecimal("0");
		this.c = new BigDecimal("0");
		return this;
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
