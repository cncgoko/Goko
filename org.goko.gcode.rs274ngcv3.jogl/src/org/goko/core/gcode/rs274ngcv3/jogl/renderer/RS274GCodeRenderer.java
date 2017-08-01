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
import javax.vecmath.Vector3f;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.AngleUnit;
import org.goko.core.common.utils.IIdBean;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IInstructionSetIterator;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.core.gcode.execution.ExecutionTokenState;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.gcode.rs274ngcv3.element.InstructionProvider;
import org.goko.core.gcode.rs274ngcv3.instruction.AbstractInstruction;
import org.goko.core.gcode.rs274ngcv3.jogl.internal.Activator;
import org.goko.core.gcode.rs274ngcv3.jogl.renderer.colorizer.AbstractInstructionColorizer;
import org.goko.core.gcode.service.IGCodeExecutionListener;
import org.goko.core.log.GkLog;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;
import org.goko.tools.viewer.jogl.service.ICoreJoglRenderer;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.PMVMatrix;

/**
 * Default GCode provider renderer
 * @author PsyKo
 *
 */
public class RS274GCodeRenderer extends AbstractLineRenderer implements ICoreJoglRenderer, IIdBean,
																		IGCodeExecutionListener<ExecutionTokenState,
																		ExecutionToken<ExecutionTokenState>>{
	private static final GkLog LOG = GkLog.getLogger(RS274GCodeRenderer.class);
	/** Internal ID */
	private Integer id;
	/** The  GCodeProvider*/
	private IGCodeProvider gcodeProvider;
	/** Command state layout */
	private static final int STATE_LAYOUT = 2;
	/** TEST : the map of vertices by ID */
	private Map<Integer, VerticesGroupByLine> mapVerticesGroupByIdLine;	
	/** Float buffer for command state */
	private IntBuffer stateBuffer;
	/** The id of the state buffer object*/
	private Integer stateBufferObject;
	/** The 4 axis controller adapter that provides angle of the current stock*/
	private IFourAxisControllerAdapter fourAxisControllerAdapter;
	/** The GCode context supplier */
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	/** The map of stored states (in case line get executed before renderer is initialized) */
	private Map<Integer, ExecutionTokenState> storedStates;
	/** Active command colorizer */
	private AbstractInstructionColorizer colorizer;
	/**
	 * Constructor
	 * @param gcodeProvider the GCodeProvider to render
	 */
	public RS274GCodeRenderer(IGCodeProvider gcodeProvider, IGCodeContextProvider<GCodeContext> gcodeContextProvider, IFourAxisControllerAdapter fourAxisControllerAdapter) {
		super(GL.GL_LINE_STRIP, COLORS | VERTICES);
		this.gcodeProvider = gcodeProvider;
		this.gcodeContextProvider = gcodeContextProvider;
		this.fourAxisControllerAdapter = fourAxisControllerAdapter;
		setLineWidth(1f);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#render(javax.media.opengl.GL3, com.jogamp.opengl.util.PMVMatrix)
	 */
	@Override
	public void render(GL3 gl, PMVMatrix modelViewMatrix) throws GkException {
		if(fourAxisControllerAdapter == null){
			super.render(gl, modelViewMatrix);
		}else{
			// We have to render using the 4th axis
			float angle = 0;
			Double realAngle = fourAxisControllerAdapter.getA().doubleValue(AngleUnit.DEGREE_ANGLE);
			if(realAngle != null){
				 angle = realAngle.floatValue();
			}
			Vector3f rotationAxis = JoglViewerPreference.getInstance().getRotaryAxisDirectionVector();
			modelViewMatrix.glRotatef(-angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
			super.render(gl, modelViewMatrix);
			modelViewMatrix.glRotatef(angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {		
		ArrayList<Point3d> lstVertices 	= new ArrayList<Point3d>();
		ArrayList<Color4f> lstColors 	= new ArrayList<Color4f>();
		
		mapVerticesGroupByIdLine 	= new HashMap<Integer, VerticesGroupByLine>();
		
		GCodeContext context = new GCodeContext(gcodeContextProvider.getGCodeContext());
		
		InstructionProvider instructionSet = Activator.getRS274NGCService().getInstructions(context, gcodeProvider);
		
		IInstructionSetIterator<GCodeContext, AbstractInstruction> iterator = Activator.getRS274NGCService().getIterator(instructionSet, context);		
		colorizer.initialize(context, instructionSet);
		while(iterator.hasNext()){
			GCodeContext preContext = new GCodeContext(iterator.getContext());
			AbstractInstruction instruction = iterator.next();			
			// TEST : Make sure we have a complete start position for rendering. 
			if(preContext.getX() != null && preContext.getY() != null && preContext.getZ() != null){
				List<Point3d> 		vertices 	= InstructionGeometryFactory.build(preContext, instruction);
				
				addVerticesGroup(instruction.getIdGCodeLine(), lstVertices.size(), vertices);
				lstVertices.addAll(vertices);
				
				// Let's generate the colors and update the bounds as well
				Color4f color = colorizer.getColor(preContext, instruction);
				for ( int i = 0; i < vertices.size(); i++) {
					lstColors.add(color);				
				}	
			}			
		}
		colorizer.conclude();
		setVerticesCount(CollectionUtils.size(lstVertices));
		
		stateBuffer = IntBuffer.allocate(getVerticesCount());
		stateBuffer.rewind();
		
		setColorsBuffer(JoglUtils.buildFloatBuffer4f(lstColors));
		setVerticesBuffer(JoglUtils.buildFloatBuffer3d(lstVertices));		
	}

	@Override
	protected void initializeBufferObjects(GL3 gl) throws GkException {		
		super.initializeBufferObjects(gl);
	}
	/**
	 * Add the given vertices to the group of vertices for this command 
	 * @param idGCodeLine the id of the generating GCodeLine
	 * @param startIndex the start index 
	 * @param vertices the vertices array
	 */
	private void addVerticesGroup(Integer idGCodeLine, int startIndex, List<Point3d> vertices) {
		if(!mapVerticesGroupByIdLine.containsKey(idGCodeLine)){
			mapVerticesGroupByIdLine.put(idGCodeLine, new VerticesGroupByLine(startIndex));
		}
		VerticesGroupByLine group = mapVerticesGroupByIdLine.get(idGCodeLine);
		group.setLength( group.getLength() + vertices.size());		
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
		super.performUpdateBufferObjects(gl);
		stateBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, stateBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getVerticesCount()*Buffers.SIZEOF_FLOAT, stateBuffer, GL.GL_DYNAMIC_DRAW);
		setUpdateBuffer(false);
	}
	
	/**
	 * (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#updateGeometry()
	 */
	public void updateGeometry() throws GkException {
		super.updateGeometry();		
	};

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

	void reinitializeStateBuffer(){
		if(stateBuffer != null){
			int capacity = stateBuffer.capacity();
			for (int i = 0; i < capacity; i++){
				stateBuffer.put(i, ExecutionTokenState.NONE_STATE);
			}
			update();
		}	
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionStart(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionTokenState> token) throws GkException {
		if(ObjectUtils.equals(token.getGCodeProvider(), gcodeProvider)){
			reinitializeStateBuffer();
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionComplete()
	 */
	@Override
	public void onQueueExecutionComplete() throws GkException {}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionStart()
	 */
	@Override
	public void onQueueExecutionStart() throws GkException {
		reinitializeStateBuffer();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionCanceled(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onQueueExecutionCanceled()
	 */
	@Override
	public void onQueueExecutionCanceled() throws GkException {}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionPause(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionResume(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionResume(ExecutionToken<ExecutionTokenState> token) throws GkException {}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeTokenExecutionListener#onExecutionComplete(org.goko.core.gcode.execution.IExecutionToken)
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionTokenState> token) throws GkException {}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.service.IGCodeLineExecutionListener#onLineStateChanged(org.goko.core.gcode.execution.IExecutionToken, java.lang.Integer)
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionTokenState> token, Integer idLine) throws GkException {
		if(ObjectUtils.equals(token.getGCodeProvider(), gcodeProvider)){
			synchronized(this){
				if(mapVerticesGroupByIdLine != null && stateBuffer != null){		
					if(mapVerticesGroupByIdLine.containsKey(idLine)){
						// Process stored states
						if(storedStates != null){
							for (Integer storedLineId : storedStates.keySet()) {
								updateStateBuffer(storedLineId, storedStates.get(storedLineId));
							}
							storedStates.clear();
							storedStates = null;
						}
						
						// Process last received state						
						ExecutionTokenState state = token.getLineState(idLine);
						updateStateBuffer(idLine, state);
					}	
				}else{
					// Renderer not initialized yet, we have to store received line
					if(storedStates == null){
						storedStates = new HashMap<>();
					}					
					storedStates.put(idLine, token.getLineState(idLine));
					LOG.info("Storing state line change for line ["+idLine+"] before init");
				}
			}
		}
	}

	private void updateStateBuffer(Integer idLine, ExecutionTokenState state){
		// Process last received state
		VerticesGroupByLine group = mapVerticesGroupByIdLine.get(idLine);		
		if(stateBuffer != null){
			// Make sure the line created renderable items (an empty line, not creating instruction, will be skipped)			
			if(group != null){
				for (int i = group.getStartIndex(); i < group.getStartIndex() + group.getLength(); i++) {
					stateBuffer.put(i, state.getState());
				}
			}
			update();
		}
	}
	/**
	 * @return the gcodeContextProvider
	 */
	public IGCodeContextProvider<GCodeContext> getGCodeContextProvider() {
		return gcodeContextProvider;
	}

	/**
	 * @param gcodeContextProvider the gcodeContextProvider to set
	 */
	public void setGCodeContextProvider(IGCodeContextProvider<GCodeContext> gcodeContextProvider) {
		this.gcodeContextProvider = gcodeContextProvider;
	}

	/**
	 * @return the gcodeProvider
	 */
	public IGCodeProvider getGCodeProvider() {
		return gcodeProvider;
	}

	/**
	 * @return the colorizer
	 */
	public AbstractInstructionColorizer getColorizer() {
		return colorizer;
	}

	/**
	 * @param colorizer the colorizer to set
	 */
	public void setColorizer(AbstractInstructionColorizer colorizer) {
		this.colorizer = colorizer;
	}


}

/**
 * Inner class describing a position and a number of vertices referring to the samed GCodeLine
 * 
 * @author Psyko
 */
class VerticesGroupByLine{
	private int startIndex;
	private int length;
	
	public VerticesGroupByLine(int startIndex) {
		super();
		this.startIndex = startIndex;
	}
	/**
	 * @return the startIndex
	 */
	public int getStartIndex() {
		return startIndex;
	}
	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	
	
}
