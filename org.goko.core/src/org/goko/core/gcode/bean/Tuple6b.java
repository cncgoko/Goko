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

public class Tuple6b {
	private BigDecimal x;
	private BigDecimal y;
	private BigDecimal z;
	private BigDecimal a;
	private BigDecimal b;
	private BigDecimal c;

	public Tuple6b(Tuple6b tuple){
		this.x = new BigDecimal(String.valueOf(tuple.x));
		this.y = new BigDecimal(String.valueOf(tuple.y));
		this.z = new BigDecimal(String.valueOf(tuple.z));
		this.a = new BigDecimal(String.valueOf(tuple.a));
		this.b = new BigDecimal(String.valueOf(tuple.b));
		this.c = new BigDecimal(String.valueOf(tuple.c));
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

	public void updateAbsolute(Tuple6b position){
		this.x = atomUpdateAbsolute(x, position.x);
		this.y = atomUpdateAbsolute(y, position.y);
		this.z = atomUpdateAbsolute(z, position.z);
		this.a = atomUpdateAbsolute(a, position.a);
		this.b = atomUpdateAbsolute(b, position.b);
		this.c = atomUpdateAbsolute(c, position.c);
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


}
