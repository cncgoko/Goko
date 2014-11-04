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
package org.goko.grbl.controller.configuration.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.inject.Inject;

import org.goko.common.bindings.AbstractController;
import org.goko.core.common.exception.GkException;
import org.goko.grbl.controller.GrblControllerService;
import org.goko.grbl.controller.IGrblControllerService;
import org.goko.grbl.controller.configuration.GrblConfiguration;

/**
 * Grbl configuration controller
 *
 * @author PsyKo
 *
 */
public class GrblConfigurationController extends AbstractController<GrblConfigurationModel> implements PropertyChangeListener {
	@Inject
	private IGrblControllerService grblService;
	/**
	 * Constructor
	 * @param binding
	 */
	public GrblConfigurationController() {
		super(new GrblConfigurationModel());
		getDataModel().addPropertyChangeListener(this);

	}

	/** (inheritDoc)
	 * @see org.goko.common.bindings.AbstractController#initialize()
	 */
	@Override
	public void initialize() throws GkException {
		GrblConfiguration cfg = ((GrblControllerService)grblService).getConfiguration();
		getDataModel().setValueParam0( cfg.getStepsMmX().getValue() );
		getDataModel().setValueParam1( cfg.getStepsMmY().getValue() );
		getDataModel().setValueParam2( cfg.getStepsMmZ().getValue() );
		getDataModel().setValueParam3( cfg.getStepPulse().getValue() );
		getDataModel().setValueParam4( cfg.getDefaultFeed().getValue() );
		getDataModel().setValueParam5( cfg.getDefaultSeek().getValue() );
		getDataModel().setValueParam6( cfg.getStepPortInvertMask().getValue() );
		getDataModel().setValueParam7( cfg.getStepIdleDelay().getValue() );
		getDataModel().setValueParam8( cfg.getAcceleration().getValue() );
		getDataModel().setValueParam9( cfg.getJunctionDeviation().getValue() );
		getDataModel().setValueParam10( cfg.getArc().getValue() );
		getDataModel().setValueParam11( cfg.getArcCorrection().getValue() );
		getDataModel().setValueParam12( cfg.getDecimalCount().getValue() );
		getDataModel().setValueParam13( cfg.getReportInches().getValue() );
		getDataModel().setValueParam14( cfg.getAutoStart().getValue() );
		getDataModel().setValueParam15( cfg.getInvertStepEnable().getValue() );
		getDataModel().setValueParam16( cfg.getHardLimits().getValue() );
		getDataModel().setValueParam17( cfg.getHomingCycle().getValue() );
		getDataModel().setValueParam18( cfg.getHomingInvertMask().getValue() );
		getDataModel().setValueParam19( cfg.getHomingFeed().getValue() );
		getDataModel().setValueParam20( cfg.getHomingSeek().getValue() );
		getDataModel().setValueParam21( cfg.getHomingDebounce().getValue() );
		getDataModel().setValueParam22( cfg.getHomingPullOff().getValue() );

	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void applyChange() {
		GrblConfiguration cfg = ((GrblControllerService)grblService).getConfiguration();

		cfg.getStepsMmX().setValue(getDataModel().getValueParam0());
		cfg.getStepsMmY().setValue(getDataModel().getValueParam1());
		cfg.getStepsMmZ().setValue(getDataModel().getValueParam2());
		cfg.getStepPulse().setValue(getDataModel().getValueParam3());
		cfg.getDefaultFeed().setValue(getDataModel().getValueParam4());
		cfg.getDefaultSeek().setValue(getDataModel().getValueParam5());
		cfg.getStepPortInvertMask().setValue(getDataModel().getValueParam6());
		cfg.getStepIdleDelay().setValue(getDataModel().getValueParam7());
		cfg.getAcceleration().setValue(getDataModel().getValueParam8());
		cfg.getJunctionDeviation().setValue(getDataModel().getValueParam9());
		cfg.getArc().setValue(getDataModel().getValueParam10());
		cfg.getArcCorrection().setValue(getDataModel().getValueParam11());
		cfg.getDecimalCount().setValue(getDataModel().getValueParam12());
		cfg.getReportInches().setValue(getDataModel().getValueParam13());
		cfg.getAutoStart().setValue(getDataModel().getValueParam14());
		cfg.getInvertStepEnable().setValue(getDataModel().getValueParam15());
		cfg.getHardLimits().setValue(getDataModel().getValueParam16());
		cfg.getHomingCycle().setValue(getDataModel().getValueParam17());
		cfg.getHomingInvertMask().setValue(getDataModel().getValueParam18());
		cfg.getHomingFeed().setValue(getDataModel().getValueParam19());
		cfg.getHomingSeek().setValue(getDataModel().getValueParam20());
		cfg.getHomingDebounce().setValue(getDataModel().getValueParam21());
		cfg.getHomingPullOff().setValue(getDataModel().getValueParam22());

		try {
			grblService.setConfiguration(cfg);
		} catch (GkException e) {
			e.printStackTrace();
		}

	}
}
