package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.Date;
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
	protected CacheById<GCodeLine> cacheLines;
	/** The id of the line in the order of the file */
	protected List<Integer> linesSequence;
	/** The latest modification date */
	private Date modificationDate;
	/** Locked state */
	private boolean locked;
	
	/** Constructor */
	public GCodeProvider() {
		this.cacheLines 	= new CacheById<GCodeLine>(new SequentialIdGenerator());
		this.linesSequence 	= new ArrayList<Integer>();
		this.modificationDate = new Date();
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
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
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
	private List<Integer> getLinesSequence() {
		return linesSequence;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getCode()
	 */
	@Override
	public String getCode() {
		return code;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the modificationDate
	 */
	public Date getModificationDate() {
		return modificationDate;
	}

	/**
	 * @param modificationDate the modificationDate to set
	 */
	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public void update() throws GkException{
		// Nothing to do here
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
