package org.goko.tools.viewer.jogl.utils.render.tool;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Point4f;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.common.measure.units.Unit;
import org.goko.core.config.GokoPreference;
import org.goko.core.controller.IThreeAxisControllerAdapter;
import org.goko.core.log.GkLog;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

import com.jogamp.opengl.util.PMVMatrix;

public class ToolRenderer extends AbstractLineRenderer {
	protected static final String ID = "org.goko.viewer.jogl.utils.render.ToolRenderer";
	/** LOG */
	private static final GkLog LOG = GkLog.getLogger(ToolRenderer.class);
	/** the controller service used to retrieve the tool position */
	private IThreeAxisControllerAdapter controllerAdapter;
	/** */
	private int segmentCount = 24;
	
	/**
	 * Constructor
	 * @param controllerService the controller service used to retrieve the tool position
	 */
	public ToolRenderer(IThreeAxisControllerAdapter controllerService) {
		super(GL.GL_LINES, VERTICES | COLORS);
		this.controllerAdapter = controllerService;
	}

	/** (inheritDoc)
	 * @see org.goko.core.viewer.renderer.IViewer3DRenderer#getId()
	 */
	@Override
	public String getId() {
		return ID;
	}


	private Point3d updateRenderMatrix(PMVMatrix modelMatrix){
		Point3d p = new Point3d();
		try{
			if(getControllerAdapter() != null){
				modelMatrix.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
				modelMatrix.glLoadIdentity();
				Unit<Length> targetLengthUnit = GokoPreference.getInstance().getLengthUnit();
				p.x = getControllerAdapter().getX().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT); 
				p.y = getControllerAdapter().getY().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT);
				p.z = getControllerAdapter().getZ().to(targetLengthUnit).doubleValue(JoglUtils.JOGL_UNIT);				
				modelMatrix.glTranslatef((float)p.x, (float)p.y, (float)p.z);
				modelMatrix.update();
			}
		}catch(GkException e){
			LOG.error(e);
		}
		return p;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.service.AbstractCoreJoglRenderer#getModelMatrix()
	 */
	@Override
	public PMVMatrix getModelMatrix() {
		PMVMatrix modelMatrix = super.getModelMatrix();
		updateRenderMatrix(modelMatrix);
		return modelMatrix;
	}

	/**
	 * @return the controllerService
	 */
	public IThreeAxisControllerAdapter getControllerAdapter() {
		return controllerAdapter;
	}


	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		List<Point4f> lstPoint = new ArrayList<Point4f>();
		List<Color4f> lstColor = new ArrayList<Color4f>();
		int radius = 3 / 2;
		int toolHeight = 6;
		lstPoint.add( new Point4f(-radius,0, toolHeight,1));
		lstPoint.add( new Point4f(0,0, 0,1));
		
		lstPoint.add( new Point4f(0,0, 0,1));
		lstPoint.add( new Point4f( radius,0, toolHeight,1));
		
		lstPoint.add( new Point4f(0,-radius, toolHeight,1));
		lstPoint.add( new Point4f(0,0, 0,1));
				
		lstPoint.add( new Point4f(0,0, 0,1));
		lstPoint.add( new Point4f(0,radius, toolHeight,1));
		
		float angle = (float) ((Math.PI * 2) / (segmentCount));
		
		for (int i = 0; i <= segmentCount; i++) {
			float x = (float) (radius * Math.cos(i * angle));
			float y = (float) (radius * Math.sin(i * angle));
			lstPoint.add( new Point4f(x, y, toolHeight,1));			
			float x1 = (float) (radius * Math.cos((i+1) * angle));
			float y1 = (float) (radius * Math.sin((i+1) * angle));
			lstPoint.add( new Point4f(x1, y1, toolHeight,1));			
		}
		
		for (Point4f point4f : lstPoint) {
			lstColor.add(new Color4f(0.87f, 0.31f, 0.43f, 1f));
		}
		setVerticesCount(lstPoint.size());
		setVerticesBuffer( JoglUtils.buildFloatBuffer4f(lstPoint) );
		setColorsBuffer(   JoglUtils.buildFloatBuffer4f(lstColor) );
	}

}
