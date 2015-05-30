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
package org.goko.core.gcode.bean.commands;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.type.BigDecimalQuantity;
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

	/**
	 * Constructor
	 * @param motionType the type of motion
	 */
	public MotionCommand(EnumGCodeCommandMotionType motionType) {
		super();
		coordinates = new Tuple6b().setNull();
		absoluteStartCoordinate = new Tuple6b().setZero();
		absoluteEndCoordinate = new Tuple6b().setZero();
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

	@Override
	public void initFromContext(GCodeContext context) {
		super.initFromContext(context);
		setAbsoluteStartCoordinate(context.getPosition());
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

	@Override
	public void setDistanceMode(EnumGCodeCommandDistanceMode distanceMode) {
		super.setDistanceMode(distanceMode);
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
			tmpTuple = new Tuple6b(absoluteStartCoordinate);
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
		String str = stringCommand.replaceAll("(X|x)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "X{x}");		
		str = str.replaceAll("(Y|y)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "Y{y}");
		str = str.replaceAll("(Z|z)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "Z{z}");
		str = str.replaceAll("(A|a)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "A{a}");
		str = str.replaceAll("(B|b)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "B{b}");
		str = str.replaceAll("(C|c)((\\+|\\-)?[0-9]?(\\.|0-9]+)?)+", "C{c}");
		super.setStringCommand(str);
	}

	@Override
	public String getStringCommand() {
		String str = replace(super.getStringCommand(),"{x}", getCoordinates().getX());
		str        = replace(str,"{y}", getAbsoluteEndCoordinate().getY());
		str        = replace(str,"{z}", getAbsoluteEndCoordinate().getZ());
		str        = replace(str,"{a}", getAbsoluteEndCoordinate().getA());
		str        = replace(str,"{b}", getAbsoluteEndCoordinate().getB());
		str        = replace(str,"{c}", getAbsoluteEndCoordinate().getC());
		return str;
	}

	private String replace(String target, String pattern, BigDecimalQuantity<?> coord){
		if(StringUtils.defaultIfBlank(target, StringUtils.EMPTY).contains(pattern)){
			return target.replace(pattern, String.valueOf(coord.value()));
		}
		return target;
	}
	private void updateBounds(){
		if(getAbsoluteEndCoordinate() != null && getAbsoluteStartCoordinate() != null){
			Tuple6b min = getAbsoluteStartCoordinate().min(getAbsoluteEndCoordinate());
			Tuple6b max = getAbsoluteStartCoordinate().max(getAbsoluteEndCoordinate());
			setBounds(new BoundingTuple6b(min,max));
		}
	}

}
