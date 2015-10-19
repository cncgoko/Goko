package org.goko.core.gcode.rs274ngcv3.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;


public class GCodeProvider implements IGCodeProvider {
	/**  Internal identifier */
	private Integer id;
	/** The lines in this provider */
	private Map<Integer, GCodeLine> lines;
	
	/** Constructor */
	public GCodeProvider() {
		this.lines = new HashMap<Integer, GCodeLine>();
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() {		
		return new ArrayList<GCodeLine>(lines.values());
	}

	/**
	 * Add the given line at the end of this provider
	 * @param line the line to add
	 */
	public void addLine(GCodeLine line){
		lines.put(line.getId(), line);		
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
	public GCodeLine getLine(Integer idLine) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
