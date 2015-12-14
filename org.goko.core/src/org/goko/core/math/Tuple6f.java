/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.core.math;

import javax.vecmath.Tuple3f;

public class Tuple6f extends Tuple3f {
	public float a;
	public float b;
	public float c;

	public Tuple6f() {
		super();
		this.a = 0;
		this.b = 0;
		this.c = 0;
	}

	public Tuple6f(float x, float y, float z, float a, float b, float c) {
		super(x,y,z);
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public Tuple6f(Tuple6f t) {
		super(t);
		this.a = t.a;
		this.b = t.b;
		this.c = t.c;
	}

	public void add(Tuple6f t){
		super.add(t);
		this.a += t.a;
		this.b += t.b;
		this.c += t.c;
	}

	public void sub(Tuple6f t){
		super.sub(t);
		this.a -= t.a;
		this.b -= t.b;
		this.c -= t.c;
	}

}
