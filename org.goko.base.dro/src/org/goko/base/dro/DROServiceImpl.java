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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.goko.common.preferences.ScopedPreferenceStore;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.MachineValueDefinition;
import org.goko.core.log.GkLog;

public class DROServiceImpl implements IDROService, IPropertyChangeListener{
	private static final GkLog LOG = GkLog.getLogger(DROServiceImpl.class);
	public static final String SERVICE_ID = "org.goko.base.droservice";
	private IControllerService controllerService;
	private ScopedPreferenceStore prefs;
	private List<MachineValueDefinition> lstDefinition;
	
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
		lstDefinition = new ArrayList<MachineValueDefinition>();
		prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE,"org.goko.base.droservice");		
		prefs.addPropertyChangeListener(this);
		updateValues();
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
		return lstDefinition;
	}

	/** (inheritDoc)
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse.jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {		
		updateValues();
	}

	private void updateValues() {
		String[] token = StringUtils.split(prefs.getString("dro.displayedValues.list"), ";");
		lstDefinition.clear();
		if(token != null && token.length > 0){
			for (String string : token) {
				try {
					MachineValueDefinition val = controllerService.getMachineValueDefinition(string);
					lstDefinition.add(val);
				} catch (GkException e) {
					LOG.error(e);					
				}
			}
		}
	}

	/**
	 * @return the controllerService
	 */
	public IControllerService getControllerService() {
		return controllerService;
	}

	/**
	 * @param controllerService the controllerService to set
	 */
	public void setControllerService(IControllerService controllerService) {
		this.controllerService = controllerService;
	}
	
	

}
