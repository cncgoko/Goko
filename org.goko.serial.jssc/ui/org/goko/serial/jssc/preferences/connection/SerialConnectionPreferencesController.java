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

import org.apache.commons.lang3.BooleanUtils;
import org.goko.common.GkUiUtils;
import org.goko.common.bindings.AbstractController;
import org.goko.common.elements.combo.LabeledValue;
import org.goko.core.common.exception.GkException;
import org.goko.serial.jssc.internal.JsscSerialActivator;
import org.goko.serial.jssc.service.JsscParameter;


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
		String dataBits = JsscSerialActivator.getPreferenceStore().getString(JsscParameter.DATABITS.toString());
		String stopBits = JsscSerialActivator.getPreferenceStore().getString(JsscParameter.STOPBITS.toString());
		String parity   = JsscSerialActivator.getPreferenceStore().getString(JsscParameter.PARITY.toString());
		String rcscts	= JsscSerialActivator.getPreferenceStore().getString(JsscParameter.RCSCTS.toString());
		String xonxoff	= JsscSerialActivator.getPreferenceStore().getString(JsscParameter.XONXOFF.toString());

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
		JsscSerialActivator.getPreferenceStore().putValue(JsscParameter.DATABITS.toString(), 	String.valueOf(getDataModel().getDataBits().getValue()));
		JsscSerialActivator.getPreferenceStore().putValue(JsscParameter.STOPBITS.toString(), 	String.valueOf(getDataModel().getStopBits().getValue()));
		JsscSerialActivator.getPreferenceStore().putValue(JsscParameter.PARITY.toString(), 		String.valueOf(getDataModel().getParity().getValue()));
		JsscSerialActivator.getPreferenceStore().putValue(JsscParameter.RCSCTS.toString(), 		String.valueOf(getDataModel().isRcsCts()));
		JsscSerialActivator.getPreferenceStore().putValue(JsscParameter.XONXOFF.toString(), 		String.valueOf(getDataModel().isXonXoff()));

	}

}
