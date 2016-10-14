package org.goko.tools.viewer.jogl.utils.render.text;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.opengl.util.texture.Texture;

/**
 * @deprecated use {@link org.goko.tools.viewer.jogl.utils.render.text.v2.TextRenderer} instead
 * @author Psyko
 * @date 11 oct. 2016
 */
@Deprecated
public class TextRenderer extends AbstractVboJoglRenderer {
	public static final int LEFT 	= 0;
	public static final int CENTER 	= 1 << 1;
	public static final int RIGHT 	= 1 << 2;
	public static final int TOP 	= 1 << 3;
	public static final int MIDDLE 	= 1 << 4;
	public static final int BOTTOM 	= 0;
	private static final int HORIZONTAL_MASK = LEFT | CENTER | RIGHT;
	private static final int VERTICAL_MASK = TOP | MIDDLE | BOTTOM;
	private Point3d position;
	private String text;
	private Vector3d widthVector;
	private Vector3d heightVector;
	private double size;
	private Vector4f color;
	private Texture texture;
	private EnumBitmapFont enumBitmapFont;
	private BitmapFontFile bff;
	private int alignement;
	private int textureSize;
	
	public TextRenderer(String text, double size, Point3d position) {
		this(text, size, position, new Vector3d(1,0,0), new Vector3d(0,1,0));
	}

	public TextRenderer(String text, double size, Point3d position, int alignement) {
		this(text, size, position, new Vector3d(1,0,0), new Vector3d(0,1,0), alignement);
	}
	public TextRenderer(String text, double size, Point3d position, Vector3d widthVector, Vector3d heightVector) {
		this(text, size, position, widthVector, heightVector, CENTER | MIDDLE);
	}

	public TextRenderer(String text, double size, Point3d position, Vector3d widthVector, Vector3d heightVector, int alignement) {
		super(GL.GL_TRIANGLES, VERTICES | COLORS | UVS);
		this.text = text;		
		this.widthVector = new Vector3d(widthVector);
		this.heightVector = new Vector3d(heightVector);
		this.size = size;
		this.position = new Point3d(position);
		this.enumBitmapFont = EnumBitmapFont.CONSOLAS;
		this.color = new Vector4f(1,1,1,1);
		this.alignement = alignement;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {
		setVerticesCount(StringUtils.length(text)*6);
		
		
		generateVertices();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, intBuffer);
		textureSize = Math.min(1024, intBuffer.get());
		this.bff = BitmapFontFileManager.getBitmapFontFile(enumBitmapFont, textureSize);


		texture = BitmapFontFileManager.getTextureFont(gl, enumBitmapFont, textureSize);
		texture.setTexParameteri(gl,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		texture.setTexParameteri(gl,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);		
		setShaderProgram(loadShaderProgram(gl));				
		super.performInitialize(gl);
		
		
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#initializeAdditionalBufferObjects(javax.media.opengl.GL3)
	 */
	@Override
	protected void initializeAdditionalBufferObjects(GL3 gl) throws GkException {		
		super.initializeAdditionalBufferObjects(gl);		
		gl.glActiveTexture(GL3.GL_TEXTURE0);
		texture.enable(gl);
		texture.bind(gl);	
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#disableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		super.disableAdditionalVertexAttribArray(gl);
		texture.disable(gl);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#loadShaderProgram(javax.media.opengl.GL3)
	 */
	@Override
	protected int loadShaderProgram(GL3 gl) throws GkException {		
		return ShaderLoader.loadShader(gl, EnumGokoShaderProgram.TEXT_SHADER);
	}

	
	protected void generateVertices() throws GkException {
		FloatBuffer verticesBuffer  = FloatBuffer.allocate(4*getVerticesCount());
		FloatBuffer uvsBuffer 	 	= FloatBuffer.allocate(2*getVerticesCount());
		FloatBuffer colorsBuffer    = FloatBuffer.allocate(4*getVerticesCount());

		Point3d lowerLeft = computeLowerLeftCorner();
		Point3d p1 = new Point3d(lowerLeft);

		int length = StringUtils.length(text);
		for (int i = 0; i < length; i++) {
			generateBuffers(text.charAt(i), p1, verticesBuffer, colorsBuffer, uvsBuffer);
		}
		setVerticesBuffer(verticesBuffer);
		setColorsBuffer(colorsBuffer);
		setUvsBuffer(uvsBuffer);
	}
	
	private Point3d computeLowerLeftCorner() {
		Point3d lowerLeft = new Point3d(position);
		int length = StringUtils.length(text);
		double textWidth = 0;		
		// Compute width
		for (int i = 0; i < length; i++) {
			char letter = text.charAt(i);
			CharBlock info = bff.getCharacterInfo(letter);
			float sizeRatio = (float) (size / bff.getLineHeight());
			
			textWidth += sizeRatio * (info.getXadvance() - info.getXoffset());
		}
		Vector3d wVector = new Vector3d(widthVector);
		wVector.scale((float) textWidth);
		Vector3d hVector = new Vector3d(heightVector);
		hVector.scale((float) size);

		switch(this.alignement & HORIZONTAL_MASK ){
		case RIGHT: lowerLeft.sub(wVector);
		break;
		case CENTER: wVector.scale(0.5f);
					lowerLeft.sub(wVector);
			break;
		default: // Nothing here
		break;
		}

		switch(this.alignement & VERTICAL_MASK ){
		case TOP: lowerLeft.sub(hVector);
		break;
		case MIDDLE: hVector.scale(0.5f);
					lowerLeft.sub(hVector);
			break;
		default: // Nothing here
		break;
		}

		return lowerLeft;
	}

	private void generateBuffers(char letter, Point3d position, FloatBuffer vertices, FloatBuffer colors, FloatBuffer uvs){
		CharBlock info = bff.getCharacterInfo(letter);
		float sizeRatio = (float) (size / bff.getLineHeight());
		float w = bff.getTextureWidth();
		float h = bff.getTextureHeight();
		Vector3d wVector = new Vector3d(widthVector);
		wVector.scale(sizeRatio * info.getWidth());		
		Vector3d hVector = new Vector3d(heightVector);
		hVector.scale((float) sizeRatio * (bff.getBase()-info.getYoffset()));
		//   3----4
		//   |    |
		//   |    |
		//   1----2
		float u1 = (info.getX())/w;
		float v1 = (/*bff.getTextureHeight() - */(info.getY() + info.getHeight()))/h;
		
		float u2 = (info.getX() + info.getWidth())/w;
		float v2 = (/*bff.getTextureHeight() - */(info.getY() + info.getHeight()))/h;

		float u3 = (info.getX())/w;
		float v3 = (/*bff.getTextureHeight() - */(info.getY() ))/h;

		float u4 = (info.getX() + info.getWidth())/w;
		float v4 = (/*bff.getTextureHeight() - */(info.getY()))/h;

		Point3d p1 = new Point3d(position);
		Point3d p2 = new Point3d(position.x+wVector.x,position.y+wVector.y,position.z+wVector.z);
		Point3d p3 = new Point3d(position.x+hVector.x,position.y+hVector.y,position.z+hVector.z);
		Point3d p4 = new Point3d(position.x+hVector.x+wVector.x,position.y+hVector.y+wVector.y,position.z+hVector.z+wVector.z);


		vertices.put(new float[]{(float)p1.x, (float)p1.y, (float)p1.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u1, v1});

		vertices.put(new float[]{(float)p2.x, (float)p2.y, (float)p2.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u2, v2});

		vertices.put(new float[]{(float)p3.x, (float)p3.y, (float)p3.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u3, v3});

		vertices.put(new float[]{(float)p2.x, (float)p2.y, (float)p2.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u2, v2});

		vertices.put(new float[]{(float)p3.x, (float)p3.y, (float)p3.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u3, v3});

		vertices.put(new float[]{(float)p4.x, (float)p4.y, (float)p4.z, 1});
		colors.put(new float[]{color.x,color.y,color.z,color.w});
		uvs.put(new float[]{u4, v4});
		wVector = new Vector3d(widthVector);
		wVector.scale(sizeRatio * (info.getXadvance() - info.getXoffset()));		
		position.add(wVector);
	}
	


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the widthVector
	 */
	public Vector3d getWidthVector() {
		return widthVector;
	}

	/**
	 * @param widthVector the widthVector to set
	 */
	public void setWidthVector(Vector3d widthVector) {
		this.widthVector = widthVector;
	}

	/**
	 * @return the heightVector
	 */
	public Vector3d getHeightVector() {
		return heightVector;
	}

	/**
	 * @param heightVector the heightVector to set
	 */
	public void setHeightVector(Vector3d heightVector) {
		this.heightVector = heightVector;
	}

	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * @return the color
	 */
	public Vector4f getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(double r, double g, double b, double a) {
		this.color = new Vector4f((float)r,(float)g,(float)b,(float)a);
	}

	/**
	 * @param color the color to set
	 */
	protected void setColor(Vector4f color) {
		this.color = color;
	}
}
