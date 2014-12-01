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
package org.goko.base.commandpanel.controller;

import java.math.BigDecimal;

import org.eclipse.core.databinding.observable.map.WritableMap;
import org.goko.common.bindings.AbstractModelObject;

/**
 * Command panel data model
 *
 * @author PsyKo
 *
 */
public class CommandPanelModel extends AbstractModelObject{
	private WritableMap actionState;
	private BigDecimal jogSpeed;
	private BigDecimal jogIncrement;
	private boolean incrementalJog;
//	private boolean incrementalJogSupported;
//	private boolean continuousJogSupported;

	public CommandPanelModel(){
		actionState = new WritableMap(String.class, Boolean.class);
	}
	/**
	 * @return the actionState
	 */
	public WritableMap getActionState() {
		return actionState;
	}

	public void setActionEnabled(String action, boolean canExecute){
		actionState.put(action, canExecute);
	}
	/**
	 * @return the jogSpeed
	 */
	public BigDecimal getJogSpeed() {
		return jogSpeed;
	}
	/**
	 * @param jogSpeed the jogSpeed to set
	 */
	public void setJogSpeed(BigDecimal jogSpeed) {
		firePropertyChange("jogSpeed", this.jogSpeed, this.jogSpeed = jogSpeed);
	}
	/**
	 * @return the jogIncrement
	 */
	public BigDecimal getJogIncrement() {
		return jogIncrement;
	}
	/**
	 * @param jogIncrement the jogIncrement to set
	 */
	public void setJogIncrement(BigDecimal jogIncrement) {
		firePropertyChange("jogIncrement", this.jogIncrement, this.jogIncrement = jogIncrement);
	}
	/**
	 * @return the incrementalJog
	 */
	public boolean isIncrementalJog() {
		return incrementalJog;
	}
	/**
	 * @param incrementalJog the incrementalJog to set
	 */
	public void setIncrementalJog(boolean incrementalJog) {
		firePropertyChange("incrementalJog", this.incrementalJog, this.incrementalJog = incrementalJog);
	}
	/**
//	 * @return the incrementalJogSupported
//	 */
//	public boolean isIncrementalJogSupported() {
//		return incrementalJogSupported;
//	}
//	/**
//	 * @param incrementalJogSupported the incrementalJogSupported to set
//	 */
//	public void setIncrementalJogSupported(boolean incrementalJogSupported) {
//		firePropertyChange("incrementalJogSupported", this.incrementalJogSupported, this.incrementalJogSupported = incrementalJogSupported);
//	}
//	/**
//	 * @return the continuousJogSupported
//	 */
//	public boolean isContinuousJogSupported() {
//		return continuousJogSupported;
//	}
//	/**
//	 * @param continuousJogSupported the continuousJogSupported to set
//	 */
//	public void setContinuousJogSupported(boolean continuousJogSupported) {
//		firePropertyChange("continuousJogSupported", this.continuousJogSupported, this.continuousJogSupported = continuousJogSupported);
//	}



}
