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

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;

public class GrblCommunicator {
	private GrblControllerService grbl;

	protected void handleIncomingData(String data) throws GkException{
		String trimmedData = StringUtils.trim(data);
		if(StringUtils.isNotEmpty(trimmedData)){
			if(StringUtils.equals(trimmedData, OK_RESPONSE)){
				grbl.handleOkResponse();
			}else if(StringUtils.startsWith(trimmedData, "error:")){
				grbl.handleError(trimmedData);
			}else if(StringUtils.startsWith(trimmedData, "<") && StringUtils.endsWith(trimmedData, ">")){
				handleStatusReport(trimmedData);
			}else if(StringUtils.startsWith(trimmedData, "Grbl")){
				handleGrblHeader(trimmedData);
				refreshStatus();
			}else if(StringUtils.defaultString(trimmedData).matches("\\$[0-9]*=.*")){
				handleConfigurationReading(trimmedData);
			}else if(StringUtils.defaultString(trimmedData).matches("\\[G.*\\]")){
				handleViewCoordinates(trimmedData);
			}else{
				System.out.println("Ignoring received data "+ trimmedData);
				getApplicativeLogService().warning("Ignoring received data "+ trimmedData, SERVICE_ID);
			}
		}
	}
}
