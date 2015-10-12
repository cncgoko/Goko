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

import org.goko.core.controller.bean.DefaultControllerValues;

/**
 * Constants definition
 *
 * @author PsyKo
 *
 */
public class TinyG implements DefaultControllerValues{
	public static final String FEED_HOLD = "!";
	public static final String CYCLE_START = "~";
	public static final String QUEUE_FLUSH = "%";
	protected static final String TINYG_BUFFER_COUNT = "tinyg.buffer.count";
	public static final String ON = "On";
	public static final String OFF = "Off";

	public class Topic{
		public class TinyGExecutionError{
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String TOPIC		= "topic/tinyg/execution/error";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String TITLE		= "prop/tinyg/execution/error/title";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String MESSAGE		= "prop/tinyg/execution/error/message";
			/** TinyG Topic : TinyG execution was paused due to an error during execution */ 
			public static final String ERROR		= "prop/tinyg/execution/error/error";
		}
	}
}
