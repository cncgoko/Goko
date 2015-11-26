package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;


public class GCodeProvider implements IGCodeProvider {
	/**  Internal identifier */
	private Integer id;
	/** The code of this provider*/
	private String code;
	/** The lines in this provider */
	private CacheById<GCodeLine> cacheLines;
	/** The id of the line in the order of the file */
	private List<Integer> linesSequence;
	
	/** Constructor */
	public GCodeProvider() {
		this.cacheLines 	= new CacheById<GCodeLine>(new SequentialIdGenerator());
		this.linesSequence 	= new ArrayList<Integer>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {		
		return cacheLines.get(linesSequence);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {		
		return cacheLines.size();
	}
	
	/**
	 * Add the given line at the end of this provider
	 * @param line the line to add
	 * @throws GkException GkException 
	 */
	public void addLine(GCodeLine line) throws GkException{		
		line.setLineNumber(cacheLines.size());
		cacheLines.add(line);		
		linesSequence.add(line.getId());
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

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		return cacheLines.get(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {		
		return cacheLines.get(getLinesSequence().get(indexLine));
	}
	/**
	 * @return the linesSequence
	 */
	public List<Integer> getLinesSequence() {
		return linesSequence;
	}

	/**
	 * @param linesSequence the linesSequence to set
	 */
	public void setLinesSequence(List<Integer> linesSequence) {
		this.linesSequence = linesSequence;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
