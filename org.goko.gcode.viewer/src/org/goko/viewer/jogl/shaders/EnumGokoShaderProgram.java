package org.goko.viewer.jogl.shaders;

public enum EnumGokoShaderProgram{
	GCODE_SHADER("/org/goko/viewer/jogl/shaders/gcode/gcodeVertexShader.glsl","/org/goko/viewer/jogl/shaders/gcode/gcodeFragmentShader.glsl"),
	LINE_SHADER("/org/goko/viewer/jogl/shaders/line/lineVertexShader.glsl","/org/goko/viewer/jogl/shaders/line/lineFragmentShader.glsl"),
	TEXT_SHADER( "/org/goko/viewer/jogl/shaders/text/textVertexShader.glsl",  "/org/goko/viewer/jogl/shaders/text/textFragmentShader.glsl"),
	SHADED_SHADER( "/org/goko/viewer/jogl/shaders/shaded/shadedVertexShader.glsl",  "/org/goko/viewer/jogl/shaders/shaded/shadedFragmentShader.glsl");

	private String vertexShaderPath;
	private String fragmentShaderPath;

	private EnumGokoShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		this.vertexShaderPath = vertexShaderPath;
		this.fragmentShaderPath = fragmentShaderPath;
	}

	/**
	 * @return the vertexShaderPath
	 */
	public String getVertexShaderPath() {
		return vertexShaderPath;
	}

	/**
	 * @param vertexShaderPath the vertexShaderPath to set
	 */
	public void setVertexShaderPath(String vertexShaderPath) {
		this.vertexShaderPath = vertexShaderPath;
	}

	/**
	 * @return the fragmentShaderPath
	 */
	public String getFragmentShaderPath() {
		return fragmentShaderPath;
	}

	/**
	 * @param fragmentShaderPath the fragmentShaderPath to set
	 */
	public void setFragmentShaderPath(String fragmentShaderPath) {
		this.fragmentShaderPath = fragmentShaderPath;
	}
}