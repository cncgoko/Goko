package org.goko.core.gcode.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;


public class GCodeCommand {
	private String lineNumber;
	private String comment;
	private List<GCodeWord> lstGCodeWords;
	private GCodeCommandState state;

	public GCodeCommand() {
		lstGCodeWords = new ArrayList<GCodeWord>();
	}

	public List<GCodeWord> getGCodeWords(){
		return lstGCodeWords;
	}

	public void setGCodeWords(Collection<GCodeWord> lstGcodeWord){
		lstGCodeWords = new ArrayList<GCodeWord>(lstGcodeWord);
	}

	public void addGCodeWord(GCodeWord word){
		lstGCodeWords.add(word);
	}
	/**
	 * @return the lineNumber
	 */
	public String getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber the lineNumber to set
	 */
	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void updateContext(GCodeContext context){

	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if(StringUtils.isNotBlank(getLineNumber())){
			buffer.append(getLineNumber());
		}

		if(CollectionUtils.isNotEmpty(getGCodeWords())){
			for(GCodeWord word : getGCodeWords()){
				buffer.append(word.toString());
			}
		}

		if(StringUtils.isNotBlank(getComment())){
			buffer.append(getComment());
		}
		return buffer.toString();
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result
				+ ((lineNumber == null) ? 0 : lineNumber.hashCode());
		result = prime * result
				+ ((lstGCodeWords == null) ? 0 : lstGCodeWords.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GCodeCommand other = (GCodeCommand) obj;
		if( CollectionUtils.size( other.getGCodeWords() ) != CollectionUtils.size( getGCodeWords() )){
			return false;
		}
		int iMax = CollectionUtils.size(getGCodeWords());
		for(int i = 0; i < iMax; i++){
			if(!ObjectUtils.equals(other.getGCodeWords().get(i), getGCodeWords().get(i))){
				return false;
			}
		}
		return true;
	}

	/**
	 * @return the state
	 */
	public GCodeCommandState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(GCodeCommandState state) {
		this.state = state;
	}

}
