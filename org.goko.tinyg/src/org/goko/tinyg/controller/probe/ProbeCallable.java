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
package org.goko.tinyg.controller.probe;

import java.util.concurrent.Callable;

import org.goko.core.gcode.bean.Tuple6b;

public class ProbeCallable implements Callable<Tuple6b> {
	private Tuple6b probeResult;
	private Object probeResultLock;

	public ProbeCallable() {
		probeResultLock = new Object();
	}

	@Override
	public Tuple6b call() throws Exception {
		synchronized (probeResultLock) {
			probeResultLock.wait();
		}
		System.err.println("Received probe result "+probeResult.getZ());
		return probeResult;
	}

	/**
	 * @return the probeResult
	 */
	public Tuple6b getProbeResult() {
		return probeResult;
	}

	/**
	 * @param probeResult the probeResult to set
	 */
	public void setProbeResult(Tuple6b probeResult) {
		this.probeResult = probeResult;
		synchronized (probeResultLock) {
			probeResultLock.notify();
		}
	}

}
