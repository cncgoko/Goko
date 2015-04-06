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

package org.goko.viewer.jogl.utils.render.text;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4f;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.shaders.EnumGokoShaderProgram;
import org.goko.viewer.jogl.shaders.ShaderLoader;
import org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;

public class TextRenderer extends DeprecatedAbstractVboJoglRenderer {
	private static GkLog LOG = GkLog.getLogger(TextRenderer.class);
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
	private int textureUniformLocation;
	private EnumBitmapFont enumBitmapFont;
	private BitmapFontFile bff;
	private FloatBuffer vertices;
	private FloatBuffer uvs;
	private FloatBuffer colors;
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
		super();
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
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#generateVbo(javax.media.opengl.GL3)
	 */
	@Override
	protected void generateVbo(GL3 gl) throws GkException{
		IntBuffer intBuffer = IntBuffer.allocate(1);
		gl.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, intBuffer);
		textureSize = Math.min(2048, intBuffer.get());
		this.bff = BitmapFontFileManager.getBitmapFontFile(enumBitmapFont, textureSize);


		texture = BitmapFontFileManager.getTextureFont(gl, enumBitmapFont, textureSize);
		texture.setTexParameteri(gl,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);
		texture.setTexParameteri(gl,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);

		setVertices(generateVertices());
		getVertices().rewind();

		setColors(generateColors());
		getColors().rewind();

		// JOGL Part now
		vao = IntBuffer.allocate(1);
		gl.glGenVertexArrays(1, vao);
		gl.glBindVertexArray(vao.get(0));
		vbo = new int[3];
		gl.glGenBuffers(3, vbo, 0);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, getInternVerticesCount()*4*Buffers.SIZEOF_FLOAT, getVertices(), GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(0);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,  getInternVerticesCount()*4*Buffers.SIZEOF_FLOAT, getColors(), GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(1);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[2]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER,  getInternVerticesCount()*2*Buffers.SIZEOF_FLOAT, getUvs(), GL.GL_STATIC_DRAW);
		gl.glEnableVertexAttribArray(2);
	}

	private FloatBuffer getUvs() throws GkException {
		return uvs;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#performInitialize(javax.media.opengl.GL3)
	 */
	@Override
	protected void performInitialize(GL3 gl) throws GkException {
		super.performInitialize(gl);
		setShaderProgram(ShaderLoader.loadShader(gl, EnumGokoShaderProgram.TEXT_SHADER));
		textureUniformLocation = gl.glGetUniformLocation(getShaderProgram(),"fontTextureSampler");
	}


	@Override
	protected void performRender(GL3 gl) throws GkException {
		if(!isInitialized()){
			initialize(gl);
		}

		gl.glUseProgram(getShaderProgram());
		gl.glEnableVertexAttribArray(0);
		gl.glEnableVertexAttribArray(1);
		gl.glEnableVertexAttribArray(2);

		gl.glActiveTexture(GL3.GL_TEXTURE0);
		texture.enable(gl);
		texture.bind(gl);
		gl.glUniform1i(textureUniformLocation, 0);


	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
	    gl.glVertexAttribPointer(0,4, GL.GL_FLOAT, false, 0, 0);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[1]);
	    gl.glVertexAttribPointer(1,4, GL.GL_FLOAT, false, 0, 0);
	    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[2]);
	    gl.glVertexAttribPointer(2,2, GL.GL_FLOAT, false, 0, 0);
	    // Actual draw
	    gl.glDrawArrays(getRenderType(), 0, getVerticesCount());
	    texture.disable(gl);
	    gl.glDisableVertexAttribArray(0);
	    gl.glDisableVertexAttribArray(1);
	    gl.glDisableVertexAttribArray(2);
	    gl.glUseProgram(0);

	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#generateColors()
	 */
	@Override
	protected FloatBuffer generateColors() throws GkException {
		return colors;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#generateVertices()
	 */
	@Override
	protected FloatBuffer generateVertices() throws GkException {
		vertices = FloatBuffer.allocate(4*getVerticesCount());
		uvs 	 = FloatBuffer.allocate(2*getVerticesCount());
		colors   = FloatBuffer.allocate(4*getVerticesCount());

		Point3d lowerLeft = computeLowerLeftCorner();
		Point3d p1 = new Point3d(lowerLeft);

		int length = StringUtils.length(text);
		for (int i = 0; i < length; i++) {
			generateBuffers(text.charAt(i), p1);
		}
		vertices.rewind();
		colors.rewind();
		uvs.rewind();
		return vertices;
	}

	private Point3d computeLowerLeftCorner() {
		Point3d lowerLeft = new Point3d(position);
		int length = StringUtils.length(text);
		double textWidth = 0;
		double textHeight = size;
		// Compute width
		for (int i = 0; i < length; i++) {
			char letter = text.charAt(i);
			int glyph = letter  - bff.getFirstCharOffset();
			float cw = bff.getCharWidth((char) glyph);
			float ch = bff.getCellHeight();
			float ratio = (float) (cw*size / ch); // The actual width of the letter

			textWidth += ratio;
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

	private void generateBuffers(char letter, Point3d position){
		
		int glyph = letter - bff.getFirstCharOffset();
		int col = glyph % bff.getColumnCount();
		int row = (int) ((Math.floor(glyph / bff.getColumnCount())) + 1);
		float w = bff.getTextureWidth();
		float h = bff.getTextureHeight();
		Vector3d wVector = new Vector3d(widthVector);
		float cw = bff.getCharWidth((char) glyph);
		float ch = bff.getCellHeight();
		float ratio = (float) (cw*size / ch);
		wVector.scale(ratio);
		Vector3d hVector = new Vector3d(heightVector);
		hVector.scale((float) size);
		//   3----4
		//   |    |
		//   |    |
		//   1----2
		float u1 = (col * bff.getCellWidth())/w;
		float v1 = (bff.getTextureHeight() - (row * bff.getCellHeight()))/h;

		float u2 = (col * bff.getCellWidth() + cw)/w;
		float v2 = (bff.getTextureHeight() - (row * bff.getCellHeight()))/h;

		float u3 = (col * bff.getCellWidth())/w;
		float v3 = (bff.getTextureHeight() - ((row-1) * bff.getCellHeight()))/h;

		float u4 = (col * bff.getCellWidth() + cw)/w;
		float v4 = (bff.getTextureHeight() - ((row-1) * bff.getCellHeight()))/h;

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

		position.add(wVector);
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#getVerticesCount()
	 */
	@Override
	protected int getVerticesCount() throws GkException {
		return StringUtils.length(text) * 6;
	}

	/** (inheritDoc)
	 * @see org.goko.viewer.jogl.utils.render.internal.DeprecatedAbstractVboJoglRenderer#getRenderType()
	 */
	@Override
	protected int getRenderType() {
		return GL3.GL_TRIANGLES;
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
