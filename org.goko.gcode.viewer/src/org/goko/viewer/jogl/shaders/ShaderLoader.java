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

package org.goko.viewer.jogl.shaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Color4f;
import javax.vecmath.Point3f;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.goko.core.log.GkLog;
import org.goko.viewer.jogl.utils.light.Light;

import com.jogamp.opengl.util.PMVMatrix;

public class ShaderLoader {
	private static final GkLog LOG = GkLog.getLogger(ShaderLoader.class);
	private static ShaderLoader instance;
	private Map<EnumGokoShaderProgram, Integer> mapShaderByType;

	private ShaderLoader(){
		this.mapShaderByType = new HashMap<EnumGokoShaderProgram, Integer>();
	}

	public static ShaderLoader getInstance(){
		if(instance == null){
			instance = new ShaderLoader();
		}
		return instance;
	}
	public static int loadShader(GL3 gl, EnumGokoShaderProgram enumGokoShaderProgram){
		return getInstance().loadShaderIntern(gl, enumGokoShaderProgram);
	}

	private int loadShaderIntern(GL3 gl, EnumGokoShaderProgram enumGokoShaderProgram){
		int shaderProgram = 0;
		if(mapShaderByType.containsKey(enumGokoShaderProgram)){
			shaderProgram = mapShaderByType.get(enumGokoShaderProgram);
		}else{
			shaderProgram = loadShader(gl, getClass().getResourceAsStream(enumGokoShaderProgram.getVertexShaderPath()), getClass().getResourceAsStream(enumGokoShaderProgram.getFragmentShaderPath()));
			mapShaderByType.put(enumGokoShaderProgram, shaderProgram);
		}
		return shaderProgram;
	}
	/**
	 * Update the projection matrix in every loaded shader
	 * @param gl gl context
	 * @param matrix the camera matrix
	 */
	public void updateProjectionMatrix(GL3 gl, PMVMatrix matrix){
		matrix.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		if(MapUtils.isNotEmpty(mapShaderByType)){
			for (EnumGokoShaderProgram enumShaderProgram : mapShaderByType.keySet()) {
				Integer shaderProgram = mapShaderByType.get(enumShaderProgram);
				gl.glUseProgram(shaderProgram);
				int shaderProjectionMatrixId = gl.glGetUniformLocation(shaderProgram, "projectionMatrix");
				
				if(shaderProjectionMatrixId >= 0){
					gl.glUniformMatrix4fv(shaderProjectionMatrixId, 1, false, matrix.glGetPMatrixf());
				}
			}
		}
		gl.glUseProgram(0);
	}
	
	public void updateLightData(GL3 gl, Light light0, Light light1){
		if(MapUtils.isNotEmpty(mapShaderByType)){
			for (EnumGokoShaderProgram enumShaderProgram : mapShaderByType.keySet()) {
				Integer shaderProgram = mapShaderByType.get(enumShaderProgram);
				gl.glUseProgram(shaderProgram);
//				int hack					= gl.glGetUniformLocation(shaderProgram, "material.ambient");
//				if(hack >= 0){
//					int ambient = gl.glGetUniformLocation(shaderProgram, "material.ambient");
//					gl.glUniform3fv(ambient, 1, new float[]{0.2f,0.4f,0.2f},0);
//					int diffuse = gl.glGetUniformLocation(shaderProgram, "material.diffuse");
//					gl.glUniform3fv(diffuse, 1, new float[]{0.0f,1f,0},0);
//					int specular = gl.glGetUniformLocation(shaderProgram, "material.specular");
//					gl.glUniform3fv(specular, 1, new float[]{0.0f,0,0.5f},0);
//				}
				// First light
				if(light0 != null){
					int light0ShaderId					= gl.glGetUniformLocation(shaderProgram, "iLight0Position");
					int diffuse0ShaderId			 	= gl.glGetUniformLocation(shaderProgram, "iLight0Diffuse");
					int ambientShaderId			 	= gl.glGetUniformLocation(shaderProgram, "iLight0Ambient");
					if(light0ShaderId >= 0){
						Point3f position = light0.getPosition();
						gl.glUniform4fv(light0ShaderId, 1, new float[]{position.x,position.y,position.z,1},0);
					}
					if(diffuse0ShaderId >= 0){
						Color4f diffuse = light0.getDiffuse();
						gl.glUniform4fv(diffuse0ShaderId, 1, new float[]{diffuse.x,diffuse.y,diffuse.z,diffuse.w},0);
					}
					if(ambientShaderId >= 0){
						Color4f ambient = light0.getAmbient();
						gl.glUniform4fv(ambientShaderId, 1, new float[]{ambient.x,ambient.y,ambient.z,ambient.w},0);
					}
				}
				// Second light
				if(light1 != null){
					int light1ShaderId 					= gl.glGetUniformLocation(shaderProgram, "iLight1Position"); // Renvoi -1 si le champs n'est pas utilisé dans le shader
					int diffuse1ShaderId			 	= gl.glGetUniformLocation(shaderProgram, "iLight1Diffuse");
					
					if(light1ShaderId >= 0){
						Point3f p = light1.getPosition();
						gl.glUniform4fv(light1ShaderId, 1, new float[]{p.x,p.y,p.z,1},0);
					}
					if(diffuse1ShaderId >= 0){
						Color4f c = light1.getDiffuse();
						gl.glUniform4fv(diffuse1ShaderId, 1, new float[]{c.x,c.y,c.z,c.w},0);
					}
				}
			}
		}	
	}
	
//	a faire :	
//		- tester la possibilité de changer les features sans les installer/desinstaller
	
	protected int loadShader(GL3 gl, InputStream vertexShaderInputStream, InputStream fragmentShaderInputStream){
		int vertexShader   = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		int fragmentShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);

		String vertexShaderSource = getStringFromInputStream(vertexShaderInputStream);

		gl.glShaderSource(vertexShader, 1, new String[]{vertexShaderSource}, (int[])null, 0);
		gl.glCompileShader(vertexShader);

		String fragmentShaderSource = getStringFromInputStream(fragmentShaderInputStream);

		gl.glShaderSource(fragmentShader, 1, new String[]{fragmentShaderSource}, (int[])null, 0);
		gl.glCompileShader(fragmentShader);

		int shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);
		gl.glLinkProgram(shaderProgram);
		checkGlError("Link :", gl);
		checkLinkProgramm(gl, shaderProgram);
		checkCompiling(gl, vertexShader);
		checkCompiling(gl, fragmentShader);
		gl.glValidateProgram(shaderProgram);
		checkValidateProgramm(gl, shaderProgram);
		checkGlError("Validate :", gl);
		return shaderProgram;
	}

	private static void checkGlError(String str, GL3 gl){
		int errorCheckValue = gl.glGetError();
		if (errorCheckValue != GL.GL_NO_ERROR) {
			GLU glu = new GLU();
			LOG.error(str+glu.gluErrorString(errorCheckValue));
		}
	}

	private static void checkLinkProgramm(GL3 gl, int program){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetProgramiv(program, GL3.GL_LINK_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);
		gl.glGetProgramInfoLog(program, 10000, null, logBuffer);
		try {
			String logBufferStr = new String(logBuffer.array(), "UTF-8");
			if(StringUtils.isNotBlank(StringUtils.defaultString(logBufferStr).trim())){
				LOG.error("CheckProgramm :"+logBufferStr);
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
		}
	}

	private static void checkValidateProgramm(GL3 gl, int program){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetProgramiv(program, GL3.GL_VALIDATE_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);

		gl.glGetProgramInfoLog(program, 10000, null, logBuffer);
		try {
			String logBufferStr = new String(logBuffer.array(), "UTF-8");
			if(StringUtils.isNotBlank(StringUtils.defaultString(logBufferStr).trim())){
				LOG.error("Check validate programm :"+logBufferStr);
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
		}
	}
	private static void checkCompiling(GL3 gl, int shader){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);

		gl.glGetShaderInfoLog(shader, 10000, null, logBuffer);
		try {
			String logBufferStr = new String(logBuffer.array(), "UTF-8");
			if(StringUtils.isNotBlank(StringUtils.defaultString(logBufferStr).trim())){
				LOG.error("CheckCompiling :"+logBufferStr);
			}
		} catch (UnsupportedEncodingException e) {
			LOG.error(e);
		}
	}
	private static String getStringFromInputStream(InputStream is) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(is, writer);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return writer.toString();
	}
}
