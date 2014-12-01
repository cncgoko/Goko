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
package org.goko.core.gcode.bean.provider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkFunctionalException;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Basic command provider for a single command
 *
 * @author PsyKo
 *
 */
public abstract class AbstractGCodeProvider implements IGCodeProvider{
	/** The commands */
	private Map<Integer, GCodeCommand> mapCommandById;
	/** The id of this provider */
	private Integer id;
	/** The name */
	private String name;
	/** The bounds */
	private BoundingTuple6b bounds;

	/**
	 * @param lstCommands
	 */
	public AbstractGCodeProvider(List<GCodeCommand> lstCommands) {
		super();
		this.id = 0;
		this.mapCommandById = new LinkedHashMap<Integer, GCodeCommand>();
		this.bounds 		= new BoundingTuple6b(new Tuple6b().setNull(), new Tuple6b().setNull());
		if(CollectionUtils.isNotEmpty(lstCommands)){
			for (GCodeCommand gCodeCommand : lstCommands) {
				addGCodeCommand(gCodeCommand);
			}
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getGCodeCommands()
	 */
	@Override
	public List<GCodeCommand> getGCodeCommands() {
		return new ArrayList<GCodeCommand>( mapCommandById.values() );
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.IGCodeProvider#getCommandById(java.lang.Integer)
	 */
	@Override
	public GCodeCommand getCommandById(Integer id) throws GkException {
		if(!mapCommandById.containsKey(id)){
			throw new GkFunctionalException("GCodeCommand with internal id '"+id+"' does not exist.");
		}
		return mapCommandById.get(id);
	}

	/**
	 * Add the GCode command
	 * @param command the command to add
	 */
	private void addGCodeCommand(GCodeCommand command){
		if(command.getId() == null){
			id = id +1;
			command.setId(id);
		}
		if(command.getLineNumber() == null){
			generateLineNumber(command);
		}
		this.mapCommandById.put(command.getId(), command);
		this.bounds.add(command.getBounds());
	}


	/**
	 * Generate a line number for the given command
	 * @param command the GCodeLine
	 */
	private void generateLineNumber(GCodeCommand command) {
		int lineNumber = CollectionUtils.size(mapCommandById) + 1;
		command.setLineNumber(lineNumber);
	}
	/**
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the bounds
	 */
	@Override
	public BoundingTuple6b getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(BoundingTuple6b bounds) {
		this.bounds = bounds;
	}

}
