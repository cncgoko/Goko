/**
 * 
 */
package org.goko.tools.viewer.jogl.utils.render.tool;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Point4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IGCodeContextProvider;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.gcode.rs274ngcv3.context.GCodeContext;
import org.goko.core.math.Tuple6b;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

/**
 * @author PsyKo
 *
 */
public class ToolLinePrintRenderer extends AbstractLineRenderer{
	/** Adapter giving the 3 axis positions */
	private IThreeAxisControllerAdapter controllerAdapter;
	private IGCodeContextProvider<GCodeContext> gcodeContextProvider;
	/**
	 * @param renderPrimitive
	 * @param usedBuffers
	 */
	public ToolLinePrintRenderer(IThreeAxisControllerAdapter controllerService, IGCodeContextProvider<GCodeContext> gcodeContextProvider) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.controllerAdapter = controllerService;
		this.gcodeContextProvider = gcodeContextProvider;
		this.setLineWidth(2f);
	}	

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performRender(javax.media.opengl.GL3)
	 */
	@Override
	protected void performRender(GL3 gl) throws GkException {		
		updateGeometry();
		super.performRender(gl);
		
	}
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		List<Point4f> lstPoint = new ArrayList<Point4f>();
		List<Color4f> lstColor = new ArrayList<Color4f>();
		
		float x = controllerAdapter.getX().value(JoglUtils.JOGL_UNIT).floatValue();
		float y = controllerAdapter.getY().value(JoglUtils.JOGL_UNIT).floatValue();
		float z = controllerAdapter.getZ().value(JoglUtils.JOGL_UNIT).floatValue();
		Tuple6b offset = new Tuple6b().setZero();
		float sx = 0;
		float sy = 0;
		float sz = 0;
		if(gcodeContextProvider != null){
			offset = gcodeContextProvider.getGCodeContext().getActiveCoordinateSystemData();
			sx = offset.getX().value(JoglUtils.JOGL_UNIT).floatValue();
			sy = offset.getY().value(JoglUtils.JOGL_UNIT).floatValue();
			sz = offset.getZ().value(JoglUtils.JOGL_UNIT).floatValue();
		}
		x += sx;
		y += sy;
		z += sz;
		lstPoint.add( new Point4f(sx,sy,sz,1));
		lstPoint.add( new Point4f(x,sy,sz,1));		
		lstColor.add(new Color4f(1f, 0.0f, 0.0f, 1f));
		lstColor.add(new Color4f(1f, 0.0f, 0.0f, 1f));
		
		lstPoint.add( new Point4f(x,sy, sz,1));
		lstPoint.add( new Point4f(x,y, sz,1));
		lstColor.add(new Color4f(0.0f, 1.0f, 0.0f, 1f));
		lstColor.add(new Color4f(0.0f, 1.0f, 0.0f, 1f));
		
		lstPoint.add( new Point4f(x,y, sz,1));
		lstPoint.add( new Point4f(x,y, z,1));
		lstColor.add(new Color4f(0.0f, 0.0f, 1.0f, 1f));
		lstColor.add(new Color4f(0.0f, 0.0f, 1.0f, 1f));
		
		setVerticesCount(lstPoint.size());
		setVerticesBuffer( JoglUtils.buildFloatBuffer4f(lstPoint) );
		setColorsBuffer(   JoglUtils.buildFloatBuffer4f(lstColor) );
	}

}
