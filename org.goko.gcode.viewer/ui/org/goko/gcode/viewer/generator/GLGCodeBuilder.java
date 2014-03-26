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
package org.goko.gcode.viewer.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeWord;
import org.goko.core.gcode.bean.IGCodeCommandProvider;

public class GLGCodeBuilder {
	private static final String FAST_MOTION_MODE = "G0";
	private static final String COORDINATED_MOTION_MODE = "G1";
	private static final String ARC_CW_MODE = "G2";
	private static final String ARC_CCW_MODE = "G3";


	private static Point3d DEFAULT_COLOR = new Point3d(1,1,1);
	private static Point3d G00_COLOR = new Point3d(0,1,0);
	private static Point3d G01_COLOR = new Point3d(0,0,1);
	private static Point3d G02_COLOR = new Point3d(1,0,0);
	private List<Point3d> vertices;
	private List<Point3d> colors;
	private Map<String, Point3d> colorsByMode;
	private Point3d lastPosition;
	private Point3d lastColor = DEFAULT_COLOR;
	private String motionMode;


	public GLGCodeBuilder() {
		vertices = new ArrayList<Point3d>();
		colors = new ArrayList<Point3d>();

		colorsByMode = new HashMap<String, Point3d>();
		colorsByMode.put("G0", G00_COLOR);
		colorsByMode.put("G1", G01_COLOR);
		colorsByMode.put("G2", G02_COLOR);
		colorsByMode.put("G3", G02_COLOR);
	}

	public void generateGCode(IGCodeCommandProvider provider){
		for(GCodeCommand gCodeCommand : provider.getGCodeCommands()){
			parseGCodeCommand(gCodeCommand);
		}

	}


	public void parseGCodeCommand(GCodeCommand gCodeCommand){
		if(containsWord(gCodeCommand, FAST_MOTION_MODE)){
			setMotionMode(FAST_MOTION_MODE);
		}else if(containsWord(gCodeCommand, COORDINATED_MOTION_MODE)){
			setMotionMode(COORDINATED_MOTION_MODE);
		}else if(containsWord(gCodeCommand, ARC_CW_MODE)){
			setMotionMode(ARC_CW_MODE);
		}else if(containsWord(gCodeCommand, ARC_CCW_MODE)){
			setMotionMode(ARC_CCW_MODE);
		}

		if(StringUtils.equalsIgnoreCase(motionMode, FAST_MOTION_MODE)){
			interpreteLinearInterpolation(gCodeCommand);
		}else if(StringUtils.equalsIgnoreCase(motionMode, COORDINATED_MOTION_MODE)){
			interpreteCoordinatedMotion(gCodeCommand);
		}else if(StringUtils.equalsIgnoreCase(motionMode, ARC_CCW_MODE) || StringUtils.equalsIgnoreCase(motionMode, ARC_CW_MODE) ){
			interpreteArcMotion(gCodeCommand);
		}

	}

	protected void setMotionMode(String mode){
		motionMode = mode;
		// If there is a last position, create the last vertex for the previous mode and let's reopen one
		if(lastPosition != null){
			vertices.add(new Point3d(lastPosition));
			colors.add(new Point3d(lastColor));
			vertices.add(new Point3d(lastPosition));
			lastColor = colorsByMode.get(mode);
			colors.add(new Point3d(colorsByMode.get(mode)));
		}
	}

	protected boolean containsWord(GCodeCommand command, String word){
		for(GCodeWord gCodeWord : command.getGCodeWords()){
			if(StringUtils.equalsIgnoreCase(word, gCodeWord.getStringValue())){
				return true;
			}
		}
		return false;
	}

	protected GCodeWord findWordByLetter(GCodeCommand command, String wordLetter){
		for(GCodeWord gCodeWord : command.getGCodeWords()){
			if(StringUtils.equalsIgnoreCase(gCodeWord.getLetter(), wordLetter)){
				return gCodeWord;
			}
		}
		return null;
	}
	protected void interpreteArcMotion(GCodeCommand command){
		Point3d endPoint = getLastPosition();

		GCodeWord xWord = findWordByLetter(command, "X");
		GCodeWord yWord = findWordByLetter(command, "Y");
		GCodeWord zWord = findWordByLetter(command, "Z");

		if(xWord != null){
			endPoint.x = xWord.getValue().doubleValue();
		}
		if(yWord != null){
			endPoint.y = yWord.getValue().doubleValue();
		}
		if(zWord != null){
			endPoint.z = zWord.getValue().doubleValue();
		}
		vertices.add(endPoint);
		colors.add(G02_COLOR);
		lastPosition = endPoint;
	}
	protected void interpreteLinearInterpolation(GCodeCommand command){
		Point3d endPoint = getLastPosition();

		GCodeWord xWord = findWordByLetter(command, "X");
		GCodeWord yWord = findWordByLetter(command, "Y");
		GCodeWord zWord = findWordByLetter(command, "Z");

		if(xWord != null){
			endPoint.x = xWord.getValue().doubleValue();
		}
		if(yWord != null){
			endPoint.y = yWord.getValue().doubleValue();
		}
		if(zWord != null){
			endPoint.z = zWord.getValue().doubleValue();
		}
		vertices.add(endPoint);
		colors.add(G00_COLOR);
		lastPosition = endPoint;
	}

	protected void interpreteCoordinatedMotion(GCodeCommand command){
		Point3d endPoint = getLastPosition();

		GCodeWord xWord = findWordByLetter(command, "X");
		GCodeWord yWord = findWordByLetter(command, "Y");
		GCodeWord zWord = findWordByLetter(command, "Z");

		if(xWord != null){
			endPoint.x = xWord.getValue().doubleValue();
		}
		if(yWord != null){
			endPoint.y = yWord.getValue().doubleValue();
		}
		if(zWord != null){
			endPoint.z = zWord.getValue().doubleValue();
		}
		vertices.add(endPoint);
		colors.add(G01_COLOR);
		lastPosition = endPoint;
	}

	protected Point3d getLastPosition(){
		if(lastPosition != null){
			return new Point3d(lastPosition);
		}else{
			return new Point3d();
		}
	}
	/**
	 * @return the vertices
	 */
	public List<Point3d> getVertices() {
		return vertices;
	}

	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(List<Point3d> vertices) {
		this.vertices = vertices;
	}

	/**
	 * @return the colors
	 */
	public List<Point3d> getColors() {
		return colors;
	}

	/**
	 * @param colors the colors to set
	 */
	public void setColors(List<Point3d> colors) {
		this.colors = colors;
	}

}
