/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.viewer.jogl.utils.render.gcode;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.bean.GCodeCommand;
import org.goko.core.gcode.bean.GCodeCommandState;
import org.goko.core.gcode.bean.IGCodeProvider;
import org.goko.core.gcode.bean.execution.IGCodeExecutionToken;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.viewer.jogl.service.JoglUtils;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.gcode.colorizer.MotionModeGCodeColorizer;
import org.goko.viewer.jogl.utils.render.gcode.geometry.GCodePartitionCodeGenerator;
import org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.common.nio.Buffers;

/**
 * Default GCode provider renderer
 * @author PsyKo
 *
 */
public class DefaultGCodeProviderRenderer extends AbstractVboJoglRenderer implements IGCodeProviderRenderer, IGCodeExecutionListener {
	/** Command state layout */
	private static final int STATE_LAYOUT = 2;
	/** The GCodeProvider to render */
	private IGCodeProvider gcodeProvider;
	/** The list of vertices */
	private List<Point3d> lstVertices;
	/** The vertices generator */
	GCodePartitionCodeGenerator generator;
	/** TEST : the map of vertices by ID */
	private Map<Integer, Integer[]> mapVerticesPositionByIdCommand;
	/** GCode colorizer */
	private IGCodeColorizer colorizer;
	/** Float buffer for command state */
	private IntBuffer stateBuffer;
	/** The id of the state buffer object*/
	private Integer stateBufferObject;

	/**
	 * Constructor
	 * @param gcodeProvider the GCodeProvider to render
	 */
	public DefaultGCodeProviderRenderer(IGCodeProvider gcodeProvider) {
		super(GL.GL_LINE_STRIP, COLORS | VERTICES);
		this.gcodeProvider = gcodeProvider;
		this.generator = new GCodePartitionCodeGenerator();
		this.colorizer = new MotionModeGCodeColorizer();
	}



	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		List<GCodeCommand> commands = gcodeProvider.getGCodeCommands();
		ArrayList<Point3d> lstVertices = new ArrayList<Point3d>();
		ArrayList<Color4f> lstColors = new ArrayList<Color4f>();
		mapVerticesPositionByIdCommand = new HashMap<Integer, Integer[]>();
		boolean firstCommand = true;
		for (GCodeCommand command : commands) {
			List<Point3d> lstCmdVertices = generator.generateVertices(command);
			if(CollectionUtils.isNotEmpty(lstCmdVertices)){
				if(firstCommand){
					firstCommand = false;
				}else{
					lstCmdVertices.remove(0);
				}
				// First index is the position of the first vertices that belong to the command
				// Second index is the number of vertices that belongs to this command
				if(CollectionUtils.isNotEmpty(lstCmdVertices)){
					mapVerticesPositionByIdCommand.put( command.getId(), new Integer[]{lstVertices.size(), lstCmdVertices.size()});
					lstVertices.addAll(lstCmdVertices);
				}
				// Let's generate the colors
				Color4f color = colorizer.getColor(command);
				for (Point3d point3d : lstCmdVertices) {
					lstColors.add(color);
				}
			}
		}
		setVerticesCount(CollectionUtils.size(lstVertices));
		stateBuffer = IntBuffer.allocate(getVerticesCount());
		stateBuffer.rewind();
		//generateCommandStateBuffer();
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer3d(lstVertices));
	}


	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.GCODE_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performUpdateBufferObjects(javax.media.opengl.GL3)
	 */
	@Override
	protected void performUpdateBufferObjects(GL3 gl) throws GkException {
		stateBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*Buffers.SIZEOF_FLOAT, stateBuffer, GL.GL_DYNAMIC_DRAW);
		setUpdateBuffer(false);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#initializeAdditionalBufferObjects(javax.media.opengl.GL3)
	 */
	@Override
	protected void initializeAdditionalBufferObjects(GL3 gl) throws GkException {
		// Initialize the status buffer object
		if(this.stateBufferObject == null){
			int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.stateBufferObject = vbo[0];
		}
		// Make sure we take everything
		stateBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*Buffers.SIZEOF_FLOAT, stateBuffer, GL.GL_DYNAMIC_DRAW);
		gl.glEnableVertexAttribArray(STATE_LAYOUT);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#enableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void enableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		gl.glEnableVertexAttribArray(STATE_LAYOUT);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glVertexAttribPointer(STATE_LAYOUT, 1, GL3.GL_FLOAT, false, 0, 0);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#disableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		gl.glDisableVertexAttribArray(STATE_LAYOUT);
	}
	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#getGCodeProvider()
	 */
	@Override
	public IGCodeProvider getGCodeProvider() {
		return gcodeProvider;
	}


	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#setGCodeProvider(org.goko.core.gcode.bean.IGCodeProvider)
	 */
	@Override
	public void setGCodeProvider(IGCodeProvider gcodeProvider) {
		this.gcodeProvider = gcodeProvider;
	}



	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#setColorizer(org.goko.viewer.jogl.utils.render.gcode.IGCodeColorizer)
	 */
	@Override
	public void setColorizer(IGCodeColorizer colorizer) throws GkException {
		this.colorizer = colorizer;
	}


	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#getColorizer()
	 */
	@Override
	public IGCodeColorizer getColorizer() throws GkException {
		return colorizer;
	}


	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionStart(IGCodeExecutionToken token) throws GkException {
		if(stateBuffer != null){
			int capacity = stateBuffer.capacity();
			for (int i = 0; i < capacity; i++){
				stateBuffer.put(i, 0);
			}
			update();
		}
	}


	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionCanceled(IGCodeExecutionToken token) throws GkException {
		// TODO Auto-generated method stub

	}


	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionPause(IGCodeExecutionToken token) throws GkException {
		// TODO Auto-generated method stub

	}


	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionComplete(IGCodeExecutionToken token) throws GkException {
		// TODO Auto-generated method stub

	}


	/**
	 * @param token
	 * @param idCommand
	 * @throws GkException
	 */
	@Override
	public void onCommandStateChanged(IGCodeExecutionToken token, Integer idCommand) throws GkException {

		Integer[] verticesIndex = mapVerticesPositionByIdCommand.get(idCommand);
		if(verticesIndex != null){
			GCodeCommandState state = token.getCommandState(idCommand);
			for (int i = verticesIndex[0]; i < verticesIndex[0] + verticesIndex[1]; i++) {
				stateBuffer.put(i, state.state);
			}
			update();
		}
	}
}
