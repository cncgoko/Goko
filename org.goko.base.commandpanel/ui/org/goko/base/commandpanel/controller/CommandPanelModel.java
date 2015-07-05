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
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;

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
	private boolean stepModeChoiceEnabled;
	private Unit<Length> lengthUnit;
	private String lengthUnitSymbol;

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
	 * @return the stepModeChoiceEnabled
	 */
	public boolean isStepModeChoiceEnabled() {
		return stepModeChoiceEnabled;
	}
	/**
	 * @param stepModeChoiceEnabled the stepModeChoiceEnabled to set
	 */
	public void setStepModeChoiceEnabled(boolean stepModeChoiceEnabled) {
		firePropertyChange("stepModeChoiceEnabled", this.stepModeChoiceEnabled, this.stepModeChoiceEnabled = stepModeChoiceEnabled);
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

}
