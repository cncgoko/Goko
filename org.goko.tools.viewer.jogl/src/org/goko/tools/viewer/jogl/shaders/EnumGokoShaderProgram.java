package org.goko.tools.viewer.jogl.shaders;

public enum EnumGokoShaderProgram{
	GCODE_SHADER("/org/goko/tools/viewer/jogl/shaders/gcode/gcodeVertexShader.glsl","/org/goko/tools/viewer/jogl/shaders/gcode/gcodeFragmentShader.glsl"),
	LINE_SHADER("/org/goko/tools/viewer/jogl/shaders/line/lineVertexShader.glsl","/org/goko/tools/viewer/jogl/shaders/line/lineFragmentShader.glsl"),
	WIDE_LINE_SHADER("/org/goko/tools/viewer/jogl/shaders/line/wide/wideLineVertexShader.glsl", "/org/goko/tools/viewer/jogl/shaders/line/wide/wideLineGeometryShader.glsl","/org/goko/tools/viewer/jogl/shaders/line/wide/wideLineFragmentShader.glsl"),
	TEXT_SHADER( "/org/goko/tools/viewer/jogl/shaders/text/textVertexShader.glsl",  "/org/goko/tools/viewer/jogl/shaders/text/textFragmentShader.glsl"),
	SHADED_FLAT_SHADER( "/org/goko/tools/viewer/jogl/shaders/shaded/shadedFlatVertexShader.glsl",  "/org/goko/tools/viewer/jogl/shaders/shaded/shadedFlatFragmentShader.glsl"),
	SHADED_SHADER( "/org/goko/tools/viewer/jogl/shaders/shaded/shadedLitVertexShader.glsl",  "/org/goko/tools/viewer/jogl/shaders/shaded/shadedLitFragmentShader.glsl"),
	Z_LEVEL_SHADER( "/org/goko/tools/viewer/jogl/shaders/zlevel/zLevelVertexShader.glsl",  "/org/goko/tools/viewer/jogl/shaders/zlevel/zLevelFragmentShader.glsl");

	private String vertexShaderPath;
	private String geometryShaderPath;
	private String fragmentShaderPath;

	private EnumGokoShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
		this(vertexShaderPath, null, fragmentShaderPath);
	}
	
	private EnumGokoShaderProgram(String vertexShaderPath, String geometryShaderPath, String fragmentShaderPath) {
		this.vertexShaderPath = vertexShaderPath;
		this.geometryShaderPath = geometryShaderPath;
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

	/**
	 * @return the geometryShaderPath
	 */
	public String getGeometryShaderPath() {
		return geometryShaderPath;
	}

	/**
	 * @param geometryShaderPath the geometryShaderPath to set
	 */
	public void setGeometryShaderPath(String geometryShaderPath) {
		this.geometryShaderPath = geometryShaderPath;
	}
}