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
package org.goko.serial.jssc.preferences.connection;

import java.util.ArrayList;
import java.util.List;

import jssc.SerialPort;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.common.elements.combo.LabeledValue;

/**
 * Model for the Serial connection preferences
 *
 * @author PsyKo
 *
 */
public class SerialConnectionPreferencesModel extends AbstractModelObject {
	/** The list of available data bits */
	private List<LabeledValue<Integer>> choiceDataBits;
	private List<LabeledValue<Integer>> choiceStopBits;
	private List<LabeledValue<Integer>> choiceParity;
	/** The selected data bits*/
	private LabeledValue<Integer> dataBits;
	private LabeledValue<Integer> stopBits;
	private LabeledValue<Integer> parity;
	private boolean rcsCts;
	private boolean xonXoff;

	public SerialConnectionPreferencesModel() {
		initDataBitsChoices();
		initParityChoices();
		initStopBitsChoices();
	}

	private void initDataBitsChoices(){
		choiceDataBits = new ArrayList<LabeledValue<Integer>>();
		choiceDataBits.add(new LabeledValue<Integer>(SerialPort.DATABITS_5	,"5"));
		choiceDataBits.add(new LabeledValue<Integer>(SerialPort.DATABITS_6	,"6"));
		choiceDataBits.add(new LabeledValue<Integer>(SerialPort.DATABITS_7	,"7"));
		choiceDataBits.add(new LabeledValue<Integer>(SerialPort.DATABITS_8	,"8"));
	}

	private void initParityChoices(){
		choiceParity = new ArrayList<LabeledValue<Integer>>();
		choiceParity.add(new LabeledValue<Integer>(SerialPort.PARITY_NONE	,"None"));
		choiceParity.add(new LabeledValue<Integer>(SerialPort.PARITY_EVEN	,"Even"));
		choiceParity.add(new LabeledValue<Integer>(SerialPort.PARITY_ODD	,"Odd"));
	}

	private void initStopBitsChoices(){
		choiceStopBits = new ArrayList<LabeledValue<Integer>>();
		choiceStopBits.add(new LabeledValue<Integer>(SerialPort.STOPBITS_1		,"1"));
		choiceStopBits.add(new LabeledValue<Integer>(SerialPort.STOPBITS_1_5	,"1.5"));
		choiceStopBits.add(new LabeledValue<Integer>(SerialPort.STOPBITS_2		,"2"));
	}

	/**
	 * @return the choiceDataBits
	 */
	public List<LabeledValue<Integer>> getChoiceDataBits() {
		return choiceDataBits;
	}

	/**
	 * @param choiceDataBits the choiceDataBits to set
	 */
	public void setChoiceDataBits(List<LabeledValue<Integer>> choiceDataBits) {
		firePropertyChange("choiceDataBits", this.choiceDataBits, this.choiceDataBits =choiceDataBits);
	}

	/**
	 * @return the choiceStopBits
	 */
	public List<LabeledValue<Integer>> getChoiceStopBits() {
		return choiceStopBits;
	}

	/**
	 * @param choiceStopBits the choiceStopBits to set
	 */
	public void setChoiceStopBits(List<LabeledValue<Integer>> choiceStopBits) {
		firePropertyChange("choiceStopBits", this.choiceStopBits, this.choiceStopBits =choiceStopBits);
	}

	/**
	 * @return the choiceParity
	 */
	public List<LabeledValue<Integer>> getChoiceParity() {
		return choiceParity;
	}

	/**
	 * @param choiceParity the choiceParity to set
	 */
	public void setChoiceParity(List<LabeledValue<Integer>> choiceParity) {
		firePropertyChange("choiceParity", this.choiceParity, this.choiceParity =choiceParity);
	}

	/**
	 * @return the dataBits
	 */
	public LabeledValue<Integer> getDataBits() {
		return dataBits;
	}

	/**
	 * @param dataBits the dataBits to set
	 */
	public void setDataBits(LabeledValue<Integer> dataBits) {
		firePropertyChange("dataBits", this.dataBits, this.dataBits =dataBits);
	}

	/**
	 * @return the stopBits
	 */
	public LabeledValue<Integer> getStopBits() {
		return stopBits;
	}

	/**
	 * @param stopBits the stopBits to set
	 */
	public void setStopBits(LabeledValue<Integer> stopBits) {
		firePropertyChange("stopBits", this.stopBits, this.stopBits =stopBits);
	}

	/**
	 * @return the parity
	 */
	public LabeledValue<Integer> getParity() {
		return parity;
	}

	/**
	 * @param parity the parity to set
	 */
	public void setParity(LabeledValue<Integer> parity) {
		firePropertyChange("parity", this.parity, this.parity =parity);
	}

	/**
	 * @return the rcsCts
	 */
	public boolean isRcsCts() {
		return rcsCts;
	}
	public boolean getRcsCts() {
		return rcsCts;
	}

	/**
	 * @param rcsCts the rcsCts to set
	 */
	public void setRcsCts(boolean rcsCts) {
		firePropertyChange("rcsCts", this.rcsCts, this.rcsCts = rcsCts);
	}

	/**
	 * @return the xonXoff
	 */
	public boolean isXonXoff() {
		return xonXoff;
	}
	/**
	 * @return the xonXoff
	 */
	public boolean getXonXoff() {
		return xonXoff;
	}

	/**
	 * @param xonXoff the xonXoff to set
	 */
	public void setXonXoff(boolean xonXoff) {
		firePropertyChange("xonXoff", this.xonXoff, this.xonXoff = xonXoff);
	}
}
