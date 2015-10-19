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

package org.goko.tools.viewer.jogl.utils.render.gcode;

import javax.media.opengl.GL3;
import javax.vecmath.Vector3f;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IFourAxisControllerAdapter;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.execution.ExecutionState;
import org.goko.core.gcode.execution.ExecutionToken;
import org.goko.tools.viewer.jogl.preferences.JoglViewerPreference;

import com.jogamp.opengl.util.PMVMatrix;

public class RotaryAxisAdapter implements IGCodeProviderRenderer<ExecutionState, ExecutionToken<ExecutionState>>{
	private IFourAxisControllerAdapter fourAxisControllerAdapter;
	private IGCodeProviderRenderer<ExecutionState, ExecutionToken<ExecutionState>> backedRenderer;

	/**
	 * Constructor
	 * @param fourAxisControllerAdapter the controller adapter to read the rotary position from
	 * @param backedRenderer the gcode renderer
	 */
	public RotaryAxisAdapter(IFourAxisControllerAdapter fourAxisControllerAdapter, IGCodeProviderRenderer<ExecutionState, ExecutionToken<ExecutionState>> backedRenderer) {
		super();
		this.fourAxisControllerAdapter = fourAxisControllerAdapter;
		this.backedRenderer = backedRenderer;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#getId()
	 */
	@Override
	public String getId() {
		return backedRenderer.getId();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#render(javax.media.opengl.GL3, com.jogamp.opengl.util.PMVMatrix)
	 */
	@Override
	public void render(GL3 gl, PMVMatrix modelViewMatrix) throws GkException {
		float angle = 0;
		Double realAngle = fourAxisControllerAdapter.getA().doubleValue();
		if(realAngle != null){
			 angle = realAngle.floatValue();
		}
		Vector3f rotationAxis = JoglViewerPreference.getInstance().getRotaryAxisDirectionVector();
		modelViewMatrix.glRotatef(-angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
		backedRenderer.render(gl, modelViewMatrix);
		modelViewMatrix.glRotatef(angle, rotationAxis.x, rotationAxis.y, rotationAxis.z);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		backedRenderer.setEnabled(enabled);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#destroy()
	 */
	@Override
	public void destroy() throws GkException {
		backedRenderer.destroy();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#shouldDestroy()
	 */
	@Override
	public boolean shouldDestroy() throws GkException {
		return backedRenderer.shouldDestroy();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#performDestroy(javax.media.opengl.GL3)
	 */
	@Override
	public void performDestroy(GL3 gl) throws GkException {
		backedRenderer.destroy();
	}

	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionStart(ExecutionToken<ExecutionState> token) throws GkException {
		backedRenderer.onExecutionStart(token);
	}

	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionCanceled(ExecutionToken<ExecutionState> token) throws GkException {
		backedRenderer.onExecutionCanceled(token);
	}

	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionPause(ExecutionToken<ExecutionState> token) throws GkException {
		backedRenderer.onExecutionPause(token);
	}

	/**
	 * @param token
	 * @throws GkException
	 */
	@Override
	public void onExecutionComplete(ExecutionToken<ExecutionState> token) throws GkException {
		backedRenderer.onExecutionComplete(token);
	}

	/**
	 * @param token
	 * @param idCommand
	 * @throws GkException
	 */
	@Override
	public void onLineStateChanged(ExecutionToken<ExecutionState> token, Integer idCommand) throws GkException {
		backedRenderer.onLineStateChanged(token, idCommand);
	}

	@Override
	public void setGCodeProvider(IGCodeProvider provider) throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#getGCodeProvider()
	 */
	@Override
	public IGCodeProvider getGCodeProvider() throws GkException {
		return backedRenderer.getGCodeProvider();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#setColorizer(org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeColorizer)
	 */
	@Override
	public void setColorizer(IGCodeColorizer colorizer) throws GkException {
		backedRenderer.setColorizer(colorizer);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#getColorizer()
	 */
	@Override
	public IGCodeColorizer getColorizer() throws GkException {
		return backedRenderer.getColorizer();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.gcode.IGCodeProviderRenderer#update()
	 */
	@Override
	public void update() {
		backedRenderer.update();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#getLayerId()
	 */
	@Override
	public int getLayerId() {
		return backedRenderer.getLayerId();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.ICoreJoglRenderer#useAlpha()
	 */
	@Override
	public boolean useAlpha() {		
		return backedRenderer.useAlpha();
	}

}
