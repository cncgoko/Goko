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

package org.goko.core.gcode.rs274ngcv3.jogl.renderer;

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
import org.goko.core.common.utils.IIdBean;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.IInstructionColorizer;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.MotionModeColorizer;
import org.goko.tools.viewer.jogl.service.ICoreJoglRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

import com.jogamp.common.nio.Buffers;

/**
 * Default GCode provider renderer
 * @author PsyKo
 *
 */
public class RS274GCodeRenderer extends AbstractLineRenderer implements ICoreJoglRenderer, IIdBean {
	/** Internal ID */
	private Integer id;
	/** Id of the generating GCodeProvider*/
	private Integer idGCodeProvider;
	/** Command state layout */
	private static final int STATE_LAYOUT = 2;
	/** The GCodeProvider to render */
	private InstructionProvider instructionSet;	
	/** TEST : the map of vertices by ID */
	private Map<Integer, Integer[]> mapVerticesPositionByIdCommand;	
	/** Float buffer for command state */
	private IntBuffer stateBuffer;
	/** The id of the state buffer object*/
	private Integer stateBufferObject;
	
	/**
	 * Constructor
	 * @param gcodeProvider the GCodeProvider to render
	 */
	public RS274GCodeRenderer(InstructionProvider instructionSet) {
		super(GL.GL_LINE_STRIP, COLORS | VERTICES);
		this.instructionSet = instructionSet;
		setLineWidth(1f);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {		
		ArrayList<Point3d> lstVertices 	= new ArrayList<Point3d>();
		ArrayList<Color4f> lstColors 	= new ArrayList<Color4f>();
		mapVerticesPositionByIdCommand 	= new HashMap<Integer, Integer[]>();
			
		// FIXME : don't use new gcode context
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(instructionSet, new GCodeContext());
		IInstructionColorizer<GCodeContext, AbstractInstruction> colorizer = new MotionModeColorizer();
//		IInstructionColorizer<GCodeContext, AbstractInstruction> colorizer = new SelectedPlaneColorizer();
		//IInstructionColorizer<GCodeContext, AbstractInstruction> colorizer = new ArcAngleColorizer(); 
		
		while(iterator.hasNext()){
			GCodeContext preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instruction = iterator.next();
			List<Point3d> vertices = InstructionGeometryFactory.build(preContext, instruction);
			lstVertices.addAll(vertices);
			// Let's generate the colors
			Color4f color = colorizer.getColor(preContext, instruction);
			for ( int i = 0; i < vertices.size(); i++) {
				lstColors.add(color);
			}
			// FIXME : add instruction id to position mapping
		}
		
//		for (IInstruction instruction : lstInstructions) {
//			List<Point3d> lstCmdVertices = InstructionGeometryFactory.build(instruction);
//			if(CollectionUtils.isNotEmpty(lstCmdVertices)){
//				if(firstCommand){
//					firstCommand = false;
//				}else{
//					lstCmdVertices.remove(0);
//				}
//				// First index is the position of the first vertices that belong to the command
//				// Second index is the number of vertices that belongs to this command
//				if(CollectionUtils.isNotEmpty(lstCmdVertices)){
//					mapVerticesPositionByIdCommand.put( instruction.getId(), new Integer[]{lstVertices.size(), lstCmdVertices.size()});
//					lstVertices.addAll(lstCmdVertices);
//				}
//				// Let's generate the colors
//				Color4f color = colorizer.getColor(command);
//				for ( int i = 0; i < lstCmdVertices.size(); i++) {
//					lstColors.add(color);
//				}
//			}
//		}
		setVerticesCount(CollectionUtils.size(lstVertices));
		stateBuffer = IntBuffer.allocate(getVerticesCount());
		stateBuffer.rewind();
		
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer3d(lstVertices));
	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.GCODE_SHADER);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performUpdateBufferObjects(javax.media.opengl.GL3)
	 */
	@Override
	protected void performUpdateBufferObjects(GL3 gl) throws GkException {
		//super.performUpdateBufferObjects(gl);
		stateBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*Buffers.SIZEOF_FLOAT, stateBuffer, GL.GL_DYNAMIC_DRAW);
		setUpdateBuffer(false);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#initializeAdditionalBufferObjects(javax.media.opengl.GL3)
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
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#enableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void enableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		gl.glEnableVertexAttribArray(STATE_LAYOUT);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glVertexAttribPointer(STATE_LAYOUT, 1, GL3.GL_FLOAT, false, 0, 0);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#disableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		gl.glDisableVertexAttribArray(STATE_LAYOUT);
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the idGCodeProvider
	 */
	public Integer getIdGCodeProvider() {
		return idGCodeProvider;
	}

	/**
	 * @param idGCodeProvider the idGCodeProvider to set
	 */
	public void setIdGCodeProvider(Integer idGCodeProvider) {
		this.idGCodeProvider = idGCodeProvider;
	}
}
