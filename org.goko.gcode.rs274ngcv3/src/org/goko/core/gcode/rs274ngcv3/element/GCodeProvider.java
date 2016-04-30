package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.utils.CacheById;
import org.goko.core.common.utils.SequentialIdGenerator;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.rs274ngcv3.element.source.DefaultGCodeProviderSource;


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
	/** This provider source */
	private IGCodeProviderSource source;
	
	/** Constructor */
	public GCodeProvider() {
		this.cacheLines 	= new CacheById<GCodeLine>(new SequentialIdGenerator());
		this.linesSequence 	= new ArrayList<Integer>();
		this.modificationDate = new Date();
		this.source = new DefaultGCodeProviderSource();		
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
		line.setId(null); // Let's force the generation of a new id 
		cacheLines.add(line);
		linesSequence.add(line.getId());
	}

	/**
	 * Clear all the data in this provider
	 * @throws GkException GkException
	 */
	public void clear() throws GkException{		
		cacheLines.removeAll();
		linesSequence.clear();
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

	/**
	 * @return the source
	 */
	public IGCodeProviderSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(IGCodeProviderSource source) {
		this.source = source;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GCodeProvider other = (GCodeProvider) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
	
}
