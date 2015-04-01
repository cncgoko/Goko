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

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;

import com.jogamp.opengl.util.PMVMatrix;

public class ShaderLoader {
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
			System.err.println(str+glu.gluErrorString(errorCheckValue));
		}
	}

	private static void checkLinkProgramm(GL3 gl, int program){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetProgramiv(program, GL3.GL_LINK_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);

		gl.glGetProgramInfoLog(program, 10000, null, logBuffer);
		try {
			System.err.println("checkProgramm :"+new String(logBuffer.array(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void checkValidateProgramm(GL3 gl, int program){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetProgramiv(program, GL3.GL_VALIDATE_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);

		gl.glGetProgramInfoLog(program, 10000, null, logBuffer);
		try {
			System.err.println("check validate programm :"+new String(logBuffer.array(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void checkCompiling(GL3 gl, int shader){
		IntBuffer b = IntBuffer.allocate(10);
		gl.glGetShaderiv(shader, GL3.GL_COMPILE_STATUS, b);
		ByteBuffer logBuffer = ByteBuffer.allocate(10000);

		gl.glGetShaderInfoLog(shader, 10000, null, logBuffer);
		try {
			System.err.println("checkCompiling : "+new String(logBuffer.array(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
