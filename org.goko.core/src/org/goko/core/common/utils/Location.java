/**
 * 
 */
package org.goko.core.common.utils;

/**
 * Location inside a GCode document 
 * @author Psyko
 * @date 12 juin 2016
 */
public class Location {
	/** The line number */
	private int line;
	/** The column number */	
	private int column;
		
	/**
	 * Constructor
	 * @param line the line number
	 * @param column the column number
	 */
	public Location(int line, int column) {
		super();
		this.line = line;
		this.column = column;
	}
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	
	
}
