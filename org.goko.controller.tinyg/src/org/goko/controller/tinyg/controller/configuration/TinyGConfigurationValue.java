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
package org.goko.controller.tinyg.controller.configuration;

import java.math.BigDecimal;

public class TinyGConfigurationValue {
	public static final BigDecimal QUEUE_REPORT_OFF			= BigDecimal.ZERO;
	public static final BigDecimal QUEUE_REPORT_FILTERED	= new BigDecimal("1");
	public static final BigDecimal QUEUE_REPORT_VERBOSE		= new BigDecimal("2");

	public static final BigDecimal JSON_VERBOSITY_VERBOSE			= new BigDecimal("5");
	
	
	public static final BigDecimal FLOW_CONTROL_OFF			= BigDecimal.ZERO;
	public static final BigDecimal FLOW_CONTROL_XON_XOFF	= new BigDecimal("1");
	public static final BigDecimal FLOW_CONTROL_RTS_CTS		= new BigDecimal("2");


}
