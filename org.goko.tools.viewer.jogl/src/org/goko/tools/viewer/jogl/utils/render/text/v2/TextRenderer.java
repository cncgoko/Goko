package org.goko.tools.viewer.jogl.utils.render.text.v2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.measure.quantity.Length;
import org.goko.tools.viewer.jogl.service.JoglUtils;
import org.goko.tools.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.tools.viewer.jogl.shaders.ShaderLoader;
import org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;

public class TextRenderer extends AbstractVboJoglRenderer {
	/** Channel for each char layout */
	private static final int CHAR_CHANNEL_LAYOUT = 5;	
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
	/** The image channel of each char buffer object*/
	private Integer charChannelBufferObject;
	/** Int buffer for char channel*/
	private IntBuffer charChannelBuffer;	
	private Length verticalPadding;
	private Length horizontalPadding;
	
	public TextRenderer(String text, double size, Point3d position) {
		this(text, size, position, new Vector3d(1,0,0), new Vector3d(0,1,0));
	}

	public TextRenderer(String text, double size, Point3d position, int alignement) {
		this(text, size, position, new Vector3d(1,0,0), new Vector3d(0,1,0), alignement);
	}
	public TextRenderer(String text, double size, Point3d position, Vector3d widthVector, Vector3d heightVector) {
		this(text, size, position, widthVector, heightVector, CENTER | MIDDLE);
	}
 //le text renderer a du mal sur les plans autres que XY
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
		this.verticalPadding = Length.ZERO;
		this.horizontalPadding = Length.ZERO;
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#buildGeometry()
	 */
	@Override
	protected void buildGeometry() throws GkException {		
		setVerticesCount(StringUtils.length(text)*6);
		charChannelBuffer = IntBuffer.allocate(3*6*getText().length());
		
		generateVertices();
	}

	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {		
		IntBuffer intBuffer = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, intBuffer);
		textureSize = Math.min(2048, intBuffer.get());
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
		
		// Initialize the channelbuffer object
		if(this.charChannelBufferObject == null){
			int[] vbo = new int[1];
			gl.glGenBuffers(1, vbo, 0);
			this.charChannelBufferObject = vbo[0];
		}
		// Make sure we take everything		
		charChannelBuffer.rewind();
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, charChannelBufferObject);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getText().length()*6*3*Buffers.SIZEOF_INT, charChannelBuffer, GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(CHAR_CHANNEL_LAYOUT);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#enableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void enableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		texture.enable(gl);
		gl.glEnableVertexAttribArray(CHAR_CHANNEL_LAYOUT);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, charChannelBufferObject);
		gl.glVertexAttribPointer(CHAR_CHANNEL_LAYOUT, 3, GL3.GL_INT, false, 0, 0);
	}
	
	/** (inheritDoc)
	 * @see org.goko.tools.viewer.jogl.utils.render.internal.AbstractVboJoglRenderer#disableAdditionalVertexAttribArray(javax.media.opengl.GL3)
	 */
	@Override
	protected void disableAdditionalVertexAttribArray(GL3 gl) throws GkException {
		super.disableAdditionalVertexAttribArray(gl);
		texture.disable(gl);

		gl.glDisableVertexAttribArray(CHAR_CHANNEL_LAYOUT);
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

		Point3d lowerLeft = computeTopLeftCorner();
		Point3d p1 = new Point3d(lowerLeft);

		int length = StringUtils.length(text);
		for (int i = 0; i < length; i++) {
			generateBuffers(text.charAt(i), p1, verticesBuffer, colorsBuffer, uvsBuffer);
		}
		setVerticesBuffer(verticesBuffer);
		setColorsBuffer(colorsBuffer);
		setUvsBuffer(uvsBuffer);
	}
	
	private Point3d computeTopLeftCorner() {
		Point3d topLeft = new Point3d(position);
		
		int length = StringUtils.length(text);
		double textWidth 	 = 0;	
		
		double ratio			= size / bff.getLineHeight();
		double minYOffset = Double.MAX_VALUE; 
		// Compute width by summing the width of each char
		for (int i = 0; i < length; i++) {
			char letter = text.charAt(i);
			CharBlock info = bff.getCharacterInfo(letter);			
			textWidth += info.getWidth();
			minYOffset = Math.min(minYOffset, info.getYoffset());
		}
		textWidth = ratio * textWidth;
		Vector3d wVector = new Vector3d(widthVector);		
		Vector3d hVector = new Vector3d(heightVector);

		Vector3d hPadding = new Vector3d(widthVector);
		hPadding.scale(horizontalPadding.doubleValue(JoglUtils.JOGL_UNIT));
		
		Vector3d vPadding = new Vector3d(heightVector);
		vPadding.scale(verticalPadding.doubleValue(JoglUtils.JOGL_UNIT));
		
		switch(this.alignement & HORIZONTAL_MASK ){
		case RIGHT:  
			wVector.scale((float) textWidth);
			wVector.add(hPadding);
			topLeft.sub(wVector);
		break;
		case CENTER: 
			wVector.scale(0.5 * textWidth);
			topLeft.sub(wVector);
		break;
		default: 
			topLeft.add(hPadding);
		break;
		}

		switch(this.alignement & VERTICAL_MASK ){
		case BOTTOM:
			hVector.scale(ratio * bff.getBase());//ratio*bff.getLineHeight());// * (1 - (lineHeight - base) / lineHeight));
			hVector.add(vPadding);
			topLeft.add(hVector);
		break;
		case TOP:
			hVector.scale(ratio*minYOffset);//* ( (base - maxCharHeight) / lineHeight));
			hVector.sub(vPadding);
			topLeft.add(hVector);
		break;
		case MIDDLE: hVector.scale(0.5f);
					topLeft.add(hVector);
			break;
		default: // Nothing here
		break;
		}

		return topLeft;
	}

	private void generateBuffers(char letter, Point3d position, FloatBuffer vertices, FloatBuffer colors, FloatBuffer uvs){		
		CharBlock charInfo 	= bff.getCharacterInfo(letter);		
		float textureWidth 	= bff.getTextureWidth();
		float textureHeight = bff.getTextureHeight();
		
		float fontBase 		= bff.getBase();
		float fontLineHeight= bff.getLineHeight();
		double ratio			= size / bff.getLineHeight();
				
		float charTextureWidth 	= charInfo.getWidth();
		float charTextureHeight = charInfo.getHeight();
		float charYOffset 		= charInfo.getYoffset();
		float charXOffset 		= charInfo.getXoffset();
		
		 
		Vector3d wVector 	= new Vector3d(widthVector);
		Vector3d hVector 	= new Vector3d(heightVector);
				
		// Let's calculate the vector from the top to the base line of the font
		Vector3d baselineOffset = new Vector3d(heightVector);
		baselineOffset.scale( charYOffset * ratio);
		 
		wVector.scale(charTextureWidth * ratio);
		
		hVector.scale(charTextureHeight * ratio);
		//   +y   
		//   |   1----2      1 = top left of image (0,0)     2 = top right (1,0) 
		//   |   |    |      3 = bottom left (0, 1)          4 = bottom right (1,1)
		//   |   |    |
		//   |   3----4
		//   +----------> +x
		//
		float u1 = (charInfo.getX())/textureWidth;
		float v1 = charInfo.getY()/textureHeight;

		float u2 = (charInfo.getX() + charTextureWidth)/textureWidth;
		float v2 = charInfo.getY()/textureHeight;

		float u3 = (charInfo.getX())/textureWidth;
		float v3 = (charInfo.getY() + charTextureHeight)/textureHeight;

		float u4 = (charInfo.getX() + charTextureWidth)/textureWidth;
		float v4 = (charInfo.getY() + charTextureHeight )/textureHeight;
		
		//adapter la taille des polygones générés
		Point3d p1 = new Point3d(position.x-baselineOffset.x                    ,position.y-baselineOffset.y                     ,position.z-baselineOffset.z);
		Point3d p2 = new Point3d(position.x-baselineOffset.x+wVector.x          ,position.y-baselineOffset.y+wVector.y           ,position.z-baselineOffset.z+wVector.z);
		Point3d p3 = new Point3d(position.x-baselineOffset.x+hVector.x          ,position.y-baselineOffset.y-hVector.y           ,position.z-baselineOffset.z+hVector.z);
		Point3d p4 = new Point3d(position.x-baselineOffset.x+hVector.x+wVector.x,position.y-baselineOffset.y-hVector.y+wVector.y ,position.z-baselineOffset.z+hVector.z+wVector.z);

		int[] channel = {0,0,0};
		channel[charInfo.getChannel()] = 1;
		// One for each vertice
		charChannelBuffer.put(channel);
		charChannelBuffer.put(channel);
		charChannelBuffer.put(channel);
		charChannelBuffer.put(channel);
		charChannelBuffer.put(channel);
		charChannelBuffer.put(channel);
		
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

		// We do not use XAdvance on purpose
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

	/**
	 * @return the verticalPadding
	 */
	public Length getVerticalPadding() {
		return verticalPadding;
	}

	/**
	 * @param verticalPadding the verticalPadding to set
	 */
	public void setVerticalPadding(Length verticalPadding) {
		this.verticalPadding = verticalPadding;
	}

	/**
	 * @return the horizontalPadding
	 */
	public Length getHorizontalPadding() {
		return horizontalPadding;
	}

	/**
	 * @param horizontalPadding the horizontalPadding to set
	 */
	public void setHorizontalPadding(Length horizontalPadding) {
		this.horizontalPadding = horizontalPadding;
	}
}
