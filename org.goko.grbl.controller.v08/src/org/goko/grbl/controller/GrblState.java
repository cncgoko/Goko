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
package org.goko.grbl.controller;

import java.math.BigDecimal;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.controller.bean.MachineValue;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.controller.bean.MachineValueStore;
import org.goko.core.gcode.bean.Tuple6b;

public class GrblState {
	/** Constant for Grbl used buffer value in value store */
	private static final String GRBL_USED_BUFFER = "GrblControllerUsedRxBuffer";
	private static final String G54_OFFSET = "G54Offset";
	private static final String G55_OFFSET = "G55Offset";
	private static final String G56_OFFSET = "G56Offset";
	private static final String G57_OFFSET = "G57Offset";

	/** Storage object for machine values (speed, position, etc...) */
	private MachineValueStore valueStore;

	public GrblState() {
		try {
			initValues();
		} catch (GkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initValues() throws GkException{
		valueStore = new MachineValueStore();
		valueStore.storeValue(new MachineValueDefinition(DefaultControllerValues.STATE, "State", "The state of Grbl controller board", MachineState.class),
								new MachineValue<MachineState>(DefaultControllerValues.STATE, MachineState.UNDEFINED));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_X, "X", "The X position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_X, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_Y, "Y", "The Y position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_Y, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.MACHINE_POSITION_Z, "Z", "The Z position of the machine", BigDecimal.class),
								new MachineValue<BigDecimal>(GrblControllerValues.MACHINE_POSITION_Z, new BigDecimal("0.000")));

		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_X, "Wor. X", "The X work position", BigDecimal.class),
				new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_X, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_Y, "Wor. Y", "The Y work position", BigDecimal.class),
						new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_Y, new BigDecimal("0.000")));
		valueStore.storeValue(new MachineValueDefinition(GrblControllerValues.WORK_POSITION_Z, "Wor. Z", "The Z work position", BigDecimal.class),
				new MachineValue<BigDecimal>(GrblControllerValues.WORK_POSITION_Z, new BigDecimal("0.000")));

		valueStore.storeValue(new MachineValueDefinition(GRBL_USED_BUFFER, "Grbl Buffer", "The space used in Grbl buffer", Integer.class), new MachineValue<Integer>(GRBL_USED_BUFFER, 0));

		valueStore.storeValue(new MachineValueDefinition(G54_OFFSET, "G54", "The G54 Offset", Tuple6b.class), new MachineValue<Tuple6b>(G54_OFFSET, new Tuple6b()));

		valueStore.addListener(this);
	}

	/**
	 * Return the machine position
	 * @return the position of the tool in the machine space coordinate
	 * @throws GkException GkException
	 */
	public Tuple6b getMachinePosition() throws GkException{
		MachineValue<BigDecimal> x = valueStore.getBigDecimalValue(GrblControllerValues.MACHINE_POSITION_X);
		MachineValue<BigDecimal> y = valueStore.getBigDecimalValue(GrblControllerValues.MACHINE_POSITION_Y);
		MachineValue<BigDecimal> z = valueStore.getBigDecimalValue(GrblControllerValues.MACHINE_POSITION_Z);
		return new Tuple6b(x.getValue(), y.getValue(), z.getValue());
	}


	/**
	 * Return the machine position
	 * @return the position of the tool in the machine space coordinate
	 * @throws GkException GkException
	 */
	public Tuple6b getWorkPosition() throws GkException{
		MachineValue<BigDecimal> x = valueStore.getBigDecimalValue(GrblControllerValues.WORK_POSITION_X);
		MachineValue<BigDecimal> y = valueStore.getBigDecimalValue(GrblControllerValues.WORK_POSITION_Y);
		MachineValue<BigDecimal> z = valueStore.getBigDecimalValue(GrblControllerValues.WORK_POSITION_Z);
		return new Tuple6b(x.getValue(), y.getValue(), z.getValue());
	}

	public int getUsedBuffer() throws GkException{
		MachineValue<Integer> buffer = valueStore.getIntegerValue(GRBL_USED_BUFFER);
		return buffer.getValue();
	}
	/**
	 * Méthode générique de set pour un offset
	 * @param id identifiant
	 * @param value valeur
	 */
	private void setOffset(String id, Tuple6b value){
		try {
			valueStore.updateValue(G54_OFFSET, value);
		} catch (GkException e) {
			e.printStackTrace();
		}
	}
	private Tuple6b getOffset(String id){
		try {
			valueStore.getValue(id, Tuple6b.class);
		} catch (GkException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Tuple6b getG54Offset(){
		return getOffset(G54_OFFSET);
	}
	public void setG54Offset(Tuple6b offset){
		setOffset(G54_OFFSET, offset);
	}
	public Tuple6b getG55Offset(){
		return getOffset(G55_OFFSET);
	}
	public void setG55Offset(Tuple6b offset){
		setOffset(G55_OFFSET, offset);
	}
	public Tuple6b getG56Offset(){
		return getOffset(G56_OFFSET);
	}
	public void setG56Offset(Tuple6b offset){
		setOffset(G56_OFFSET, offset);
	}
	public Tuple6b getG57Offset(){
		return getOffset(G57_OFFSET);
	}
	public void setG57Offset(Tuple6b offset){
		setOffset(G57_OFFSET, offset);
	}
}
