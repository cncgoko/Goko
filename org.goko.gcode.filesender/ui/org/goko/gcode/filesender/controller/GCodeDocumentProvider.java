/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.gcode.filesender.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.Document;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.log.GkLog;

public class GCodeDocumentProvider extends Document {
	private static final GkLog LOG = GkLog.getLogger(GCodeDocumentProvider.class);

	/** EOL String token */
	private static String EOL = System.getProperty("line.separator");

	private IGCodeProvider gcodeProvider;
	private Map<Integer, Integer> gCodeCommandLine;

	private IRS274NGCService gcodeService;

	public GCodeDocumentProvider(IGCodeProvider gcodeProvider, IRS274NGCService gcodeService){
		this.gcodeProvider = gcodeProvider;
		this.gcodeService = gcodeService;
		this.gCodeCommandLine = new HashMap<Integer, Integer>();
		try {
			init();
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	private void init() throws GkException {
		List<GCodeLine> lstLines = gcodeProvider.getLines();
		int line = 0;
		StringBuffer buffer = new StringBuffer();
		for (GCodeLine gCodeLine : lstLines) {
			buffer.append(gcodeService.render(gCodeLine));
			buffer.append(EOL);
			gCodeCommandLine.put(gCodeLine.getId(), line++);
		}
		set(buffer.toString());
	}

	public int getLineForCommand(int idCommand){
		if(gCodeCommandLine.containsKey(idCommand)){
			return gCodeCommandLine.get(idCommand);
		}
		return -1;
	}
}
