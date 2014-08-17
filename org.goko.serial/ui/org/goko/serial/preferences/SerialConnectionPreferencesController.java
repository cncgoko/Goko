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
package org.goko.serial.preferences;

import org.apache.commons.lang3.BooleanUtils;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.internal.SerialActivator;
import org.goko.serial.bean.SerialConnectionParameter;

/**
 * Controller for the Serial Connection preferences page
 * @author PsyKo
 *
 */
public class SerialConnectionPreferencesController extends AbstractController<SerialConnectionPreferencesModel> {
	/**
	 * Constructor
	 */
	public SerialConnectionPreferencesController() {
		super(new SerialConnectionPreferencesModel());
	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		String dataBits = SerialActivator.getPreferenceStore().getString(SerialConnectionParameter.DATABITS.toString());
		String stopBits = SerialActivator.getPreferenceStore().getString(SerialConnectionParameter.STOPBITS.toString());
		String parity   = SerialActivator.getPreferenceStore().getString(SerialConnectionParameter.PARITY.toString());
		String rcscts	= SerialActivator.getPreferenceStore().getString(SerialConnectionParameter.RCSCTS.toString());
		String xonxoff	= SerialActivator.getPreferenceStore().getString(SerialConnectionParameter.XONXOFF.toString());

		LabeledValue<Integer> selectedDataBits = GkUiUtils.getLabelledValueByKey(Integer.valueOf(dataBits), getDataModel().getChoiceDataBits());
		getDataModel().setDataBits(selectedDataBits);

		LabeledValue<Integer> selectedStopBits = GkUiUtils.getLabelledValueByKey(Integer.valueOf(stopBits), getDataModel().getChoiceStopBits());
		getDataModel().setStopBits(selectedStopBits);

		LabeledValue<Integer> selectedParity = GkUiUtils.getLabelledValueByKey(Integer.valueOf(parity), getDataModel().getChoiceParity());
		getDataModel().setParity(selectedParity);

		getDataModel().setRcsCts(BooleanUtils.toBoolean(rcscts));
		getDataModel().setXonXoff(BooleanUtils.toBoolean(xonxoff));

	}

	public void savePreferences() {
		SerialActivator.getPreferenceStore().putValue(SerialConnectionParameter.DATABITS.toString(), 	String.valueOf(getDataModel().getDataBits().getValue()));
		SerialActivator.getPreferenceStore().putValue(SerialConnectionParameter.STOPBITS.toString(), 	String.valueOf(getDataModel().getStopBits().getValue()));
		SerialActivator.getPreferenceStore().putValue(SerialConnectionParameter.PARITY.toString(), 		String.valueOf(getDataModel().getParity().getValue()));
		SerialActivator.getPreferenceStore().putValue(SerialConnectionParameter.RCSCTS.toString(), 		String.valueOf(getDataModel().isRcsCts()));
		SerialActivator.getPreferenceStore().putValue(SerialConnectionParameter.XONXOFF.toString(), 		String.valueOf(getDataModel().isXonXoff()));

	}

}
