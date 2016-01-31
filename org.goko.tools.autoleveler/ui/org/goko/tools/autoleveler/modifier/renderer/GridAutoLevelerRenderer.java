/**
 * 
 */
package org.goko.tools.autoleveler.modifier.renderer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point4d;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.core.math.Tuple6b;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;
import org.goko.tools.autoleveler.modifier.AutoLevelerModifier;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer;

/**
 * @author PsyKo
 * @date 18 janv. 2016
 */
public class GridAutoLevelerRenderer extends AbstractLineRenderer {
	/** LOG */
	private static GkLog LOG = GkLog.getLogger(GridAutoLevelerRenderer.class);
	/** The rendererd map */
	private WeakReference<AutoLevelerModifier<GridHeightMap>> modifierReference;
	
	/**
	 * Constructor
	 */
	public GridAutoLevelerRenderer(AutoLevelerModifier<GridHeightMap> modifier) {
		super(GL.GL_LINES,  VERTICES);
		this.modifierReference = new WeakReference<AutoLevelerModifier<GridHeightMap>>(modifier);
		this.setUseAlpha(true);
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractLineRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {		
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.Z_LEVEL_SHADER);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#updateShaderData(javax.media.opengl.GL3)
	 */
	@Override
	protected void updateShaderData(GL3 gl) throws GkException {		
		super.updateShaderData(gl);
		if(modifierReference.get() == null){
			return;
		}
		AutoLevelerModifier<GridHeightMap> modifier = modifierReference.get();
		GridHeightMap map = modifier.getHeightMap();
		
		int shaderProgram = getShaderProgram();
				
		int zTop    = gl.glGetUniformLocation(shaderProgram, "zTop");
		int zCenter = gl.glGetUniformLocation(shaderProgram, "zCenter");
		int zBottom = gl.glGetUniformLocation(shaderProgram, "zBottom");
		if(zTop >= 0){			
			gl.glUniform1f(zTop, map.getProbeStartHeight().value(JoglUtils.JOGL_UNIT).floatValue() );			
		}
		if(zCenter >= 0){			
			gl.glUniform1f(zCenter, modifier.getTheoricHeight().value(JoglUtils.JOGL_UNIT).floatValue() );			
		}		
		if(zBottom >= 0){			
			gl.glUniform1f(zBottom, map.getProbeLowerHeight().value(JoglUtils.JOGL_UNIT).floatValue() );			
		}		
		
		int colorTop    = gl.glGetUniformLocation(shaderProgram, "colorTop");
		int colorCenter = gl.glGetUniformLocation(shaderProgram, "colorCenter");
		int colorBottom = gl.glGetUniformLocation(shaderProgram, "colorBottom");
		if(colorTop >= 0){			
			gl.glUniform4fv(colorTop, 1, new float[]{1f, 0f, 0f, 0.6f},0);			
		}
		if(colorCenter >= 0){			
			gl.glUniform4fv(colorCenter, 1, new float[]{0f, 1f, 0f, 0.6f},0);
		}		
		if(colorBottom >= 0){			
			gl.glUniform4fv(colorBottom, 1, new float[]{0f, 0f, 1f, 0.6f},0);			
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		List<Point4d> lstVertices = new ArrayList<Point4d>();
		
		GridHeightMap map = modifierReference.get().getHeightMap();
		for (int x = 0; x <= map.getxDivisionCount(); x++) {
			for (int y = 0; y < map.getyDivisionCount(); y++) {
				Tuple6b p1 = map.getPoint(x, y);
				Tuple6b p2 = map.getPoint(x, y+1);
				lstVertices.add(new Point4d(p1.toPoint3d(JoglUtils.JOGL_UNIT)));				 
				lstVertices.add(new Point4d(p2.toPoint3d(JoglUtils.JOGL_UNIT)));		
			}
		}
		
		for (int y = 0; y <= map.getyDivisionCount(); y++) {
			for (int x = 0; x < map.getxDivisionCount(); x++) {			
				Tuple6b p1 = map.getPoint(x, y);
				Tuple6b p2 = map.getPoint(x+1, y);
				lstVertices.add(new Point4d(p1.toPoint3d(JoglUtils.JOGL_UNIT)));				 
				lstVertices.add(new Point4d(p2.toPoint3d(JoglUtils.JOGL_UNIT)));
			}
		}
		
		setVerticesCount(CollectionUtils.size(lstVertices));
		setVerticesBuffer(JoglUtils.buildFloatBuffer4d(lstVertices));		
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#update()
	 */
	@Override
	public void update() {		
		super.update();
		try {
			updateGeometry();
		} catch (GkException e) {
			LOG.error(e);
		}
		updateShaderData();
	}
}
