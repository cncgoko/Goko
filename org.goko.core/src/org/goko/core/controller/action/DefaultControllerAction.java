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
package org.goko.core.controller.action;

public interface DefaultControllerAction {

	static final String HOME 		= "GkDefaultControllerAction.Home";
	static final String START 		= "GkDefaultControllerAction.Start";
	static final String PAUSE 		= "GkDefaultControllerAction.Pause";
	static final String STOP		= "GkDefaultControllerAction.Stop";
	static final String JOG_START	= "GkDefaultControllerAction.JogStart";
	static final String JOG_STOP	= "GkDefaultControllerAction.JogStop";
	static final String INCREMENTAL_JOG_START	= "GkDefaultControllerAction.IncrementalJogStart";
	static final String INCREMENTAL_JOG_STOP	= "GkDefaultControllerAction.IncrementalJogStop";
	static final String RESET_ZERO	= "GkDefaultControllerAction.ResetZero";
	static final String SPINDLE_ON	= "GkDefaultControllerAction.SpindleOn";
	static final String SPINDLE_OFF	= "GkDefaultControllerAction.SpindleOff";
	static final String KILL_ALARM	= "GkDefaultControllerAction.KillAlarm";
}
