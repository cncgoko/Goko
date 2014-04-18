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
package org.goko.core.controller.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;

/**
 * A class designed to follow the status of command sending
 *
 * @author PsyKo
 *
 */
public class StreamStatus implements IGCodeProvider{
	private List<GCodeCommand> lstGCodeCommand;
	private List<GCodeCommand> lstGCodeCommandToSend;
	private List<GCodeCommand> lstSentGCodeCommand;
	private List<GCodeCommand> lstPendingGCodeCommand;
	private List<GCodeCommand> lstAknowledgedGCodeCommand;

	/**
	 * Constructor
	 */
	public StreamStatus(List<GCodeCommand> lstCommandToSend) {
		lstGCodeCommand 			= Collections.synchronizedList(new ArrayList<GCodeCommand>());
		lstGCodeCommand.addAll(lstCommandToSend);

		lstGCodeCommandToSend 		= Collections.synchronizedList(new ArrayList<GCodeCommand>());
		lstGCodeCommandToSend.addAll(lstCommandToSend);

		lstSentGCodeCommand 		= Collections.synchronizedList(new ArrayList<GCodeCommand>());
		lstPendingGCodeCommand 		= Collections.synchronizedList(new ArrayList<GCodeCommand>());
		lstAknowledgedGCodeCommand 	= Collections.synchronizedList(new ArrayList<GCodeCommand>());
	}

	/**
	 * @return the lstUnsentGCodeCommandToSend
	 */
	public synchronized List<GCodeCommand> getGCodeCommandToSend() {
		return lstGCodeCommandToSend;
	}

	/**
	 * @return the lstSentGCodeCommand
	 */
	public List<GCodeCommand> getSentGCodeCommands() {
		return lstSentGCodeCommand;
	}

	/**
	 * @return the lstAknowledgedGCodeCommand
	 */
	public List<GCodeCommand> getAknowledgedGCodeCommand() {
		return lstAknowledgedGCodeCommand;
	}

	/**
	 * Remove the next commande from the list of command to send
	 * @return {@link GCodeCommand} or <code>null</code> if there is no command to send
	 */
	public GCodeCommand consumeNextCommand(){
		if(getGCodeCommandToSend().size() > 0){
			return getGCodeCommandToSend().remove(0);
		}
		return null;
	}

	public boolean hasMoreCommands(){
		return getGCodeCommandToSend().size() > 0;
	}
	/**
	 * Add the given GCodeCommand to the list of sent commands
	 * @param command the command to mark as sent
	 */
	public void addSentCommand(GCodeCommand command){
		lstSentGCodeCommand.add(command);
		lstPendingGCodeCommand.add(command);
	}

	/**
	 * Add the given GCodeCommand to the list of acknowledged commands
	 * @param command the command to mark as acknowledged
	 */
	public void addAcknowledgedCommand(GCodeCommand command){
		lstAknowledgedGCodeCommand.add(command);
	}

	public GCodeCommand unstackNextPendingCommand(){
		return lstPendingGCodeCommand.remove(0);
	}

	public GCodeCommand getNextPendingCommand(){
		return lstPendingGCodeCommand.get(0);
	}


	/**
	 * @return the lstGCodeCommand
	 */
	public synchronized List<GCodeCommand> getAllGCodeCommand() {
		return lstGCodeCommand;
	}

	/**
	 * @return the complete
	 */
	public boolean hasPendingCommand() {
		return !lstPendingGCodeCommand.isEmpty();
	}

	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return getAllGCodeCommand();
	}

}
