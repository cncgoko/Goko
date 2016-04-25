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
package org.goko.tools.commandpanel.controller;

import java.math.BigDecimal;

import org.eclipse.core.databinding.observable.map.WritableMap;
import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.quantity.Speed;
import org.goko.core.common.measure.units.Unit;

/**
 * Command panel data model
 *
 * @author PsyKo
 *
 */
public class CommandPanelModel extends AbstractModelObject{
	/** Precise jog enabled property name*/
	public static final String PRECISE_JOG_ENABLED = "preciseJog";
	private WritableMap actionState;
	private Speed jogSpeed;
	private BigDecimal jogIncrement;
	private boolean preciseJog;	
	private Unit<Length> lengthUnit;
	private String lengthUnitSymbol;
	private boolean preciseJogForced;

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
	public Speed getJogSpeed() {
		return jogSpeed;
	}
	/**
	 * @param jogSpeed the jogSpeed to set
	 */
	public void setJogSpeed(Speed jogSpeed) {
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
	public boolean isPreciseJog() {
		return preciseJog;
	}
	/**
	 * @param incrementalJog the incrementalJog to set
	 */
	public void setPreciseJog(boolean preciseJog) {
		firePropertyChange(PRECISE_JOG_ENABLED, this.preciseJog, this.preciseJog = preciseJog);
	}

	public void setLengthUnitSymbol(String symbol) {
		firePropertyChange("lengthUnitSymbol", this.lengthUnitSymbol, this.lengthUnitSymbol = symbol);
	}
	
	public String getLengthUnitSymbol() {
		return lengthUnitSymbol;
	}
	
	public void setLengthUnit(Unit<Length> unit) {
		firePropertyChange("lengthUnit", this.lengthUnit, this.lengthUnit = unit);
		setLengthUnitSymbol(unit.getSymbol());
	}

	public Unit<Length> getLengthUnit() {
		return lengthUnit;
	}
	/**
	 * @return the preciseJogForced
	 */
	public boolean isPreciseJogForced() {
		return preciseJogForced;
	}
	/**
	 * @param preciseJogForced the preciseJogForced to set
	 */
	public void setPreciseJogForced(boolean preciseJogForced) {
		firePropertyChange("preciseJogForced", this.preciseJogForced, this.preciseJogForced = preciseJogForced);
	}
}
