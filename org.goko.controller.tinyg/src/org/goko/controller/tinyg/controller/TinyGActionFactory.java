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
package org.goko.controller.tinyg.controller;

import org.goko.controller.tinyg.controller.actions.TinyGFeedHoldAction;
import org.goko.controller.tinyg.controller.actions.TinyGHomingAction;
import org.goko.controller.tinyg.controller.actions.TinyGKillAlarmAction;
import org.goko.controller.tinyg.controller.actions.TinyGResetAction;
import org.goko.controller.tinyg.controller.actions.TinyGResetZeroAction;
import org.goko.controller.tinyg.controller.actions.TinyGSpindleOffAction;
import org.goko.controller.tinyg.controller.actions.TinyGSpindleOnAction;
import org.goko.controller.tinyg.controller.actions.TinyGStartJogAction;
import org.goko.controller.tinyg.controller.actions.TinyGStopAction;
import org.goko.controller.tinyg.controller.actions.TinyGStopJogAction;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.action.ControllerActionFactory;

public class TinyGActionFactory extends ControllerActionFactory{	
	/** The target controller service */
	private TinyGControllerService controllerService;

	/**
	 * @param controllerService
	 */
	public TinyGActionFactory(TinyGControllerService controllerService) {
		super();
		this.controllerService = controllerService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.controller.action.ControllerActionFactory#createActions()
	 */
	@Override
	public void createActions() throws GkException {
		add(new TinyGFeedHoldAction(controllerService));
		add(new TinyGKillAlarmAction(controllerService));
		add(new TinyGResetAction(controllerService));
		add(new TinyGResetZeroAction(controllerService));
		add(new TinyGSpindleOffAction(controllerService));
		add(new TinyGSpindleOnAction(controllerService));
		add(new TinyGStartJogAction(controllerService));
		add(new TinyGStopAction(controllerService));
		add(new TinyGStopJogAction(controllerService));
		add(new TinyGHomingAction(controllerService));
	}

}
