/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import java.util.ArrayList;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.XmlTuple6b;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.common.io.xml.quantity.XmlSpeed;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class GridHeightMapExporter implements IExporter<GridHeightMap, XmlGridHeightMap> {

	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
	 */
	@Override
	public XmlGridHeightMap export(GridHeightMap input, IMapperService mapperService) throws GkException {
		XmlGridHeightMap xmlMap = new XmlGridHeightMap();
		xmlMap.setClearanceHeight( XmlLength.valueOf(input.getClearanceHeight()) );
		xmlMap.setStart( XmlTuple6b.valueOf(input.getStart()) );
		xmlMap.setEnd( XmlTuple6b.valueOf(input.getEnd()) );
		xmlMap.setProbed(input.isProbed());
		xmlMap.setProbeLowerHeight(XmlLength.valueOf(input.getProbeLowerHeight()));
		xmlMap.setProbeStartHeight(XmlLength.valueOf(input.getProbeLowerHeight()));
		xmlMap.setxDivisionCount(input.getxDivisionCount());
		xmlMap.setyDivisionCount(input.getyDivisionCount());
		xmlMap.setProbeFeedrate( XmlSpeed.valueOf(input.getProbeFeedrate()) );
		
		ArrayList<XmlGridHeightMapOffset> xmlOffssets = new ArrayList<XmlGridHeightMapOffset>();
		
		for(int x = 0; x <= input.getxDivisionCount(); x++){
			for(int y = 0; y <= input.getyDivisionCount(); y++){
				XmlGridHeightMapOffset offset = new XmlGridHeightMapOffset();
				offset.setxCoord(x);
				offset.setyCoord(y);
				offset.setOffset(XmlLength.valueOf(input.getPoint(x, y).getZ()));
				xmlOffssets.add( offset );
			}	
		}
		
		xmlMap.setOffsets(xmlOffssets);
		return xmlMap; 
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<XmlGridHeightMap> getOutputClass() {
		return XmlGridHeightMap.class;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<GridHeightMap> getInputClass() {
		return GridHeightMap.class;
	}

}
