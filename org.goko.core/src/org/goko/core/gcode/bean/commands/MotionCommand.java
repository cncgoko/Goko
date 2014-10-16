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
package org.goko.core.gcode.bean.commands;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.BoundingTuple6b;
import org.goko.core.gcode.bean.GCodeContext;
import org.goko.core.gcode.bean.IGCodeCommandVisitor;
import org.goko.core.gcode.bean.Tuple6b;

/**
 * Defines an abstract motion command
 *
 * @author PsyKo
 *
 */
public abstract class MotionCommand extends SettingCommand {
	/** The computed start coordinate in the absolute coordinate system in millimeters*/
	private Tuple6b absoluteStartCoordinate;
	/** The computed start coordinate in the absolute coordinate system in millimeters */
	private Tuple6b absoluteEndCoordinate;
	/** The raw coordinates as extracted from the raw expression */
	private Tuple6b coordinates;
	/** TODO : Working plane */

	/**
	 * Constructor
	 * @param motionType the type of motion
	 */
	public MotionCommand(EnumGCodeCommandMotionType motionType) {
		super();
		setType(EnumGCodeCommandType.MOTION);
		super.setMotionType(motionType);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.bean.GCodeCommand#accept(org.goko.core.gcode.bean.IGCodeCommandVisitor)
	 */
	@Override
	public void accept(IGCodeCommandVisitor visitor) throws GkException {
		visitor.visit(this);
	}

	/** {@inheritDoc}
	 * @see org.goko.core.gcode.bean.commands.SettingCommand#updateContext(org.goko.core.gcode.bean.GCodeContext)
	 */
	@Override
	public void updateContext(GCodeContext context) {
		super.updateContext(context);
		context.setPosition(absoluteEndCoordinate);
	}


	/**
	 * @return the absoluteStartCoordinate
	 */
	public Tuple6b getAbsoluteStartCoordinate() {
		if(absoluteStartCoordinate == null){
			return null;
		}
		return new Tuple6b(absoluteStartCoordinate);
	}

	/**
	 * @param absoluteStartCoordinate the absoluteStartCoordinate to set
	 */
	public void setAbsoluteStartCoordinate(Tuple6b absoluteStartCoordinate) {
		this.absoluteStartCoordinate = new Tuple6b(absoluteStartCoordinate);
		updateAbsoluteEndCoordinate();
	}

	/**
	 * @return the absoluteEndCoordinate
	 */
	public Tuple6b getAbsoluteEndCoordinate() {
		if(absoluteEndCoordinate == null){
			return null;
		}
		return new Tuple6b(absoluteEndCoordinate);
	}

	/**
	 * @param absoluteEndCoordinate the absoluteEndCoordinate to set
	 */
	public void setAbsoluteEndCoordinate(Tuple6b absoluteEndCoordinate) {
		this.absoluteEndCoordinate = new Tuple6b(absoluteEndCoordinate);
		updateBounds();
	}

	/**
	 * @return the coordinates
	 */
	public Tuple6b getCoordinates() {
		if(coordinates == null){
			return null;
		}
		return new Tuple6b(coordinates);
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Tuple6b coordinates) {
		this.coordinates = coordinates;
		updateAbsoluteEndCoordinate();
	}

	/**
	 * Update the end coordinates
	 */
	private void updateAbsoluteEndCoordinate(){
		if(absoluteStartCoordinate != null && coordinates != null){
			Tuple6b tmpTuple = null;
			if(absoluteEndCoordinate == null){
				tmpTuple = new Tuple6b(absoluteStartCoordinate);
			}else{
				tmpTuple = new Tuple6b(absoluteEndCoordinate);
			}
			if(getDistanceMode() == EnumGCodeCommandDistanceMode.ABSOLUTE){
				tmpTuple.updateAbsolute(coordinates);
			}else{
				tmpTuple.updateRelative(coordinates);
			}
			setAbsoluteEndCoordinate(tmpTuple);
		}
	}

	@Override
	public void setStringCommand(String stringCommand) {
		String str = stringCommand.replaceAll("(X|x)[^A-Z]+", "X{x}");
		str = str.replaceAll("(Y|y)[^A-Z]+", "Y{y}");
		str = str.replaceAll("(Z|z)[^A-Z]+", "Z{z}");
		str = str.replaceAll("(A|a)[^A-Z]+", "A{a}");
		str = str.replaceAll("(B|b)[^A-Z]+", "B{b}");
		str = str.replaceAll("(C|c)[^A-Z]+", "C{c}");
		super.setStringCommand(str);
	}

	@Override
	public String getStringCommand() {
		String str = super.getStringCommand().replaceAll("\\{x\\}", String.valueOf(getCoordinates().getX()));
		str = str.replaceAll("\\{y\\}", String.valueOf(getAbsoluteEndCoordinate().getY()));
		str = str.replaceAll("\\{z\\}", String.valueOf(getAbsoluteEndCoordinate().getZ()));
		str = str.replaceAll("\\{a\\}", String.valueOf(getAbsoluteEndCoordinate().getA()));
		str = str.replaceAll("\\{b\\}", String.valueOf(getAbsoluteEndCoordinate().getB()));
		str = str.replaceAll("\\{c\\}", String.valueOf(getAbsoluteEndCoordinate().getC()));
		return str;
	}


	private void updateBounds(){
		if(getAbsoluteEndCoordinate() != null && getAbsoluteStartCoordinate() != null){
			Tuple6b min = getAbsoluteStartCoordinate().min(getAbsoluteEndCoordinate());
			Tuple6b max = getAbsoluteStartCoordinate().max(getAbsoluteEndCoordinate());
			setBounds(new BoundingTuple6b(min,max));
		}
	}
}
