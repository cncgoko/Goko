package org.goko.core.gcode.element;

import java.util.ArrayList;
import java.util.List;

public class GCodeLine {
	/** Internal identifier of this line */
	private Integer id;
	private Integer lineNumber;
	private List<GCodeWord> words;
	private List<GCodeParameter> parameters;
	
	public GCodeLine() {
		this.words 		= new ArrayList<GCodeWord>();
		this.parameters = new ArrayList<GCodeParameter>();
	}
	/**
	 * @return the lineNumber
	 */
	public Integer getLineNumber() {
		return lineNumber;
	}
	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	/**
	 * @return the words
	 */
	public List<GCodeWord> getWords() {
		return words;
	}
	/**
	 * @param words the words to set
	 */
	public void setWords(List<GCodeWord> words) {
		this.words = words;
	}
	
	public void addWord(GCodeWord word){
		words.add(word);
	}
	/**
	 * @return the parameters
	 */
	public List<GCodeParameter> getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<GCodeParameter> parameters) {
		this.parameters = parameters;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
}
