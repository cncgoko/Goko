/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.tool;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Color4f;
import javax.vecmath.Point4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

/**
 * @author PsyKo
 *
 */
public class ToolLinePrintRenderer extends AbstractLineRenderer{
	/** Adapter giving the 3 axis positions */
	private IThreeAxisControllerAdapter controllerAdapter;

	/**
	 * @param renderPrimitive
	 * @param usedBuffers
	 */
	public ToolLinePrintRenderer(IThreeAxisControllerAdapter controllerService) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.controllerAdapter = controllerService;
		this.setLineWidth(2f);
	}	

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performRender(javax.media.opengl.GL3)
	 */
	@Override
	protected void performRender(GL3 gl) throws GkException {		
		update();
		super.performRender(gl);
		
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		List<Point4f> lstPoint = new ArrayList<Point4f>();
		List<Color4f> lstColor = new ArrayList<Color4f>();
		
		float x = controllerAdapter.getX().to(JoglUtils.JOGL_UNIT).getValue().floatValue();
		float y = controllerAdapter.getY().to(JoglUtils.JOGL_UNIT).getValue().floatValue();
		float z = controllerAdapter.getZ().to(JoglUtils.JOGL_UNIT).getValue().floatValue();
		
		lstPoint.add( new Point4f(0,0,0,1));
		lstPoint.add( new Point4f(x,0,0,1));		
		lstColor.add(new Color4f(1f, 0.0f, 0.0f, 1f));
		lstColor.add(new Color4f(1f, 0.0f, 0.0f, 1f));
		
		lstPoint.add( new Point4f(x,0, 0,1));
		lstPoint.add( new Point4f(x,y, 0,1));
		lstColor.add(new Color4f(0.0f, 1.0f, 0.0f, 1f));
		lstColor.add(new Color4f(0.0f, 1.0f, 0.0f, 1f));
		
		lstPoint.add( new Point4f(x,y, 0,1));
		lstPoint.add( new Point4f(x,y, z,1));
		lstColor.add(new Color4f(0.0f, 0.0f, 1.0f, 1f));
		lstColor.add(new Color4f(0.0f, 0.0f, 1.0f, 1f));
		
		setVerticesCount(lstPoint.size());
		setVerticesBuffer( JoglUtils.buildFloatBuffer4f(lstPoint) );
		setColorsBuffer(   JoglUtils.buildFloatBuffer4f(lstColor) );
	}

}
