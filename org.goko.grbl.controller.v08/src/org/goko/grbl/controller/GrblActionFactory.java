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

import java.util.HashMap;
import java.util.Map;

import org.goko.core.controller.action.IGkControllerAction;
import org.goko.grbl.controller.action.GrblFeedHoldAction;
import org.goko.grbl.controller.action.GrblHomeAction;
import org.goko.grbl.controller.action.GrblJogStartAction;
import org.goko.grbl.controller.action.GrblJogStopAction;
import org.goko.grbl.controller.action.GrblResetZeroAction;
import org.goko.grbl.controller.action.GrblStartAction;
import org.goko.grbl.controller.action.GrblStopAction;

/**
 * Grbl action factory
 *
 * @author PsyKo
 *
 */
public class GrblActionFactory {
	private Map<String, IGkControllerAction> mapAction;
	private GrblControllerService grblService;

	/**
	 * Constructor
	 * @param grblService the running {@link GrblControllerService}
	 */
	protected GrblActionFactory(GrblControllerService grblService){
		super();
		this.grblService = grblService;
		this.mapAction = new HashMap<String, IGkControllerAction>();

		createHomingAction();
		createFeedholdAction();
		createStopAction();
		createStartAction();
		createJogStopAction();
		createJogStartAction();
		createResetZeroAction();
	}
	/**
	 * Find the action with the designated Id
	 * @param id the Id of the action to find
	 * @return the corresponding {@link IGkControllerAction} if it exists, <code>null</code> otherwise
	 */
	protected IGkControllerAction findAction(String id){
		IGkControllerAction action = null;
		if(mapAction.containsKey(id)){
			action = mapAction.get(id);
		}
		return action;
	}

	private void createHomingAction(){
		GrblHomeAction action = new GrblHomeAction(grblService);
		mapAction.put(action.getId(), action);
	}

	private void createFeedholdAction(){
		GrblFeedHoldAction action = new GrblFeedHoldAction(grblService);
		mapAction.put(action.getId(), action);
	}

	private void createStopAction() {
		GrblStopAction action = new GrblStopAction(grblService);
		mapAction.put(action.getId(), action);
	}

	private void createStartAction() {
		GrblStartAction action = new GrblStartAction(grblService);
		mapAction.put(action.getId(), action);
	}


	private void createJogStopAction() {
		GrblJogStopAction action = new GrblJogStopAction(grblService);
		mapAction.put(action.getId(), action);
	}

	private void createJogStartAction() {
		GrblJogStartAction action = new GrblJogStartAction(grblService);
		mapAction.put(action.getId(), action);
	}
	private void createResetZeroAction() {
		GrblResetZeroAction action = new GrblResetZeroAction(grblService);
		mapAction.put(action.getId(), action);
	}
}
