/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import java.util.ArrayList;

import org.goko.core.common.io.xml.XmlTuple6b;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.common.io.xml.quantity.XmlSpeed;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.DerivedType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

/**
 * @author PsyKo
 * @date 19 févr. 2016
 */
@DerivedType(parent=AbstractXmlHeightMap.class, name="heightmap:grid")
public class XmlGridHeightMap extends AbstractXmlHeightMap{
	/** The indexed list of position */
	@ElementList
	private ArrayList<XmlGridHeightMapOffset> offsets;
	/** The start point of this map */
	@Element
	private XmlTuple6b start;
	/** The end point of this map */
	@Element
	private XmlTuple6b end;
	/** Number of divisions on the X axis*/
	@Attribute
	private int xDivisionCount;
	/** Number of divisions on the Y axis*/
	@Attribute
	private int yDivisionCount;
	/** The clearance height */
	@Attribute
	private XmlLength clearanceHeight;
	/** The probe start height */
	@Attribute
	private XmlLength probeStartHeight;
	/** The probe lower height */
	@Attribute
	private XmlLength probeLowerHeight;
	/** The probe feed rate */
	@Attribute
	private XmlSpeed probeFeedrate;
	/** Boolean indicating that the map has been probed */
	@Attribute
	private boolean probed;
	/**
	 * @return the offsets
	 */
	public ArrayList<XmlGridHeightMapOffset> getOffsets() {
		return offsets;
	}
	/**
	 * @param offsets the offsets to set
	 */
	public void setOffsets(ArrayList<XmlGridHeightMapOffset> offsets) {
		this.offsets = offsets;
	}
	/**
	 * @return the start
	 */
	public XmlTuple6b getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(XmlTuple6b start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public XmlTuple6b getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(XmlTuple6b end) {
		this.end = end;
	}
	/**
	 * @return the xDivisionCount
	 */
	public int getxDivisionCount() {
		return xDivisionCount;
	}
	/**
	 * @param xDivisionCount the xDivisionCount to set
	 */
	public void setxDivisionCount(int xDivisionCount) {
		this.xDivisionCount = xDivisionCount;
	}
	/**
	 * @return the yDivisionCount
	 */
	public int getyDivisionCount() {
		return yDivisionCount;
	}
	/**
	 * @param yDivisionCount the yDivisionCount to set
	 */
	public void setyDivisionCount(int yDivisionCount) {
		this.yDivisionCount = yDivisionCount;
	}
	/**
	 * @return the clearanceHeight
	 */
	public XmlLength getClearanceHeight() {
		return clearanceHeight;
	}
	/**
	 * @param clearanceHeight the clearanceHeight to set
	 */
	public void setClearanceHeight(XmlLength clearanceHeight) {
		this.clearanceHeight = clearanceHeight;
	}
	/**
	 * @return the probeStartHeight
	 */
	public XmlLength getProbeStartHeight() {
		return probeStartHeight;
	}
	/**
	 * @param probeStartHeight the probeStartHeight to set
	 */
	public void setProbeStartHeight(XmlLength probeStartHeight) {
		this.probeStartHeight = probeStartHeight;
	}
	/**
	 * @return the probeLowerHeight
	 */
	public XmlLength getProbeLowerHeight() {
		return probeLowerHeight;
	}
	/**
	 * @param probeLowerHeight the probeLowerHeight to set
	 */
	public void setProbeLowerHeight(XmlLength probeLowerHeight) {
		this.probeLowerHeight = probeLowerHeight;
	}
	/**
	 * @return the probeFeedrate
	 */
	public XmlSpeed getProbeFeedrate() {
		return probeFeedrate;
	}
	/**
	 * @param probeFeedrate the probeFeedrate to set
	 */
	public void setProbeFeedrate(XmlSpeed probeFeedrate) {
		this.probeFeedrate = probeFeedrate;
	}
	/**
	 * @return the probed
	 */
	public boolean isProbed() {
		return probed;
	}
	/**
	 * @param probed the probed to set
	 */
	public void setProbed(boolean probed) {
		this.probed = probed;
	}
	
	
}
