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
package org.goko.base.dro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.osgi.service.prefs.BackingStoreException;

public class DROServiceImpl implements IDROService{
	public static final String SERVICE_ID = "org.goko.base.droservice";

	private IEclipsePreferences preferences;

	private IControllerService controllerService;

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		// TODO Auto-generated method stub
		preferences = InstanceScope.INSTANCE.getNode(SERVICE_ID);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.base.dro.IDROService#getDisplayedMachineValueDefinition()
	 */
	@Override
	public List<MachineValueDefinition> getDisplayedMachineValueDefinition() throws GkException{
		return getDisplayedValuesFromPreferences();
	}


	@Override
	public void saveDisplayedMachineValueDefinition( List<MachineValueDefinition> lstMachineValueDefinition) throws GkException {
		List<String> lstId = new ArrayList<String>();
		for (MachineValueDefinition definition : lstMachineValueDefinition) {
			lstId.add( definition.getId() );
		}

		String serializedList = serialize( lstId );

		preferences.put(IDROPreferencesConstants.KEY_VALUES_ID_LIST, serializedList);

		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	private List<MachineValueDefinition> getDisplayedValuesFromPreferences() throws GkException {
		List<MachineValueDefinition> lstMachineValues = new ArrayList<MachineValueDefinition>();
		String serializedList = preferences.get(IDROPreferencesConstants.KEY_VALUES_ID_LIST, StringUtils.EMPTY);
		if(StringUtils.isNotEmpty(serializedList)){
			List<String> lstId = deserialize(serializedList);
			for (String id : lstId) {
				MachineValueDefinition definition = controllerService.findMachineValueDefinition(id);
				if(definition != null){
					lstMachineValues.add( definition );
				}
			}
		}
		return lstMachineValues;
	}


	private String serialize(List<String> lst){
		String result = StringUtils.EMPTY;
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			String[] array = lst.toArray(new String[]{});
			oos.writeObject(array);
			oos.flush();
			oos.close();
			result =  baos.toString();
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

	private List<String> deserialize(String str){
		List<String> result = new ArrayList<String>();
		try{
			ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
			ObjectInputStream ois = new ObjectInputStream(bais);

			String[] arrayResult = (String[]) ois.readObject();
			for (String string : arrayResult) {
				result.add(string);
			}
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @param preferences the preferences to set
	 */
	public void setPreferences(IEclipsePreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(IControllerService controllerService) {
		this.controllerService = controllerService;
	}


}
