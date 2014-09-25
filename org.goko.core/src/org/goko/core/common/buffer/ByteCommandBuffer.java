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
package org.goko.core.common.buffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.log.GkLog;

public class ByteCommandBuffer {
	private static final GkLog LOG = GkLog.getLogger(ByteCommandBuffer.class);
	private List<List<Byte>> stackedCommands;
	private List<Byte> currentCommand;
	private Byte commandDelimiter;

	public ByteCommandBuffer(Byte commandDelimiter) {
		this.stackedCommands = Collections.synchronizedList(new ArrayList<List<Byte>>());
		this.currentCommand  = Collections.synchronizedList(new ArrayList<Byte>());
		this.commandDelimiter = commandDelimiter;
	}

	public void add(Byte b){
		this.currentCommand.add(b);
		if(ObjectUtils.equals(b, commandDelimiter)){
			this.stackCurrentCommand();
		}
	}

	public void addAll(List<Byte> byteList){
		for (Byte b : byteList) {
			this.currentCommand.add(b);
			if(ObjectUtils.equals(b, commandDelimiter)){
				this.stackCurrentCommand();
			}
		}
	}

	public boolean hasNext(){
		synchronized (stackedCommands) {
			return !stackedCommands.isEmpty();
		}
	}
	public synchronized void clear(){
		stackedCommands.clear();
		currentCommand.clear();
	}
	/**
	 * Add the current command to the stack of commands
	 */
	private void stackCurrentCommand(){
		synchronized (stackedCommands) {
			stackedCommands.add(Collections.synchronizedList(new ArrayList<Byte>(currentCommand)));
			//LOG.info("Stacking command in ByteBuffer "+GkUtils.toStringReplaceCRLF(currentCommand));
			this.currentCommand.clear();
		}
	}

	public List<Byte> unstackNextCommand(){
		synchronized (stackedCommands) {
			return stackedCommands.remove(0);
		}
	}
	public List<Byte> getNextCommand(){
		synchronized (stackedCommands) {
			return stackedCommands.get(0);
		}
	}
}
