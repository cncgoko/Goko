/**
 * 
 */
package org.goko.tools.autoleveler.io.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.io.xml.XmlTuple6b;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.common.io.xml.quantity.XmlSpeed;
import org.goko.core.common.measure.quantity.Length;
import org.goko.core.math.Tuple6b;
import org.goko.core.workspace.service.ILoader;
import org.goko.core.workspace.service.IMapperService;
import org.goko.tools.autoleveler.bean.grid.GridHeightMap;

/**
 * @author PsyKo
 * @date 16 févr. 2016
 */
public class GridHeightMapLoader implements ILoader<XmlGridHeightMap, GridHeightMap> {
	/** (inheritDoc)
		 * @see org.goko.core.workspace.service.ILoader#load(java.lang.Object, org.goko.core.workspace.service.IMapperService)
		 */
		@Override
		public GridHeightMap load(XmlGridHeightMap input, IMapperService mapperService) throws GkException {
			int xDivision = input.getxDivisionCount();
			int yDivision = input.getyDivisionCount();
			int[][] vertices = new int[xDivision+1][yDivision+1];
			List<Tuple6b> offsets = new ArrayList<Tuple6b>();
			Tuple6b start = XmlTuple6b.valueOf(input.getStart());
			Tuple6b end   = XmlTuple6b.valueOf(input.getEnd());
			Length dx = end.getX().subtract(start.getX());
			Length dy = end.getY().subtract(start.getY());
			
			if(CollectionUtils.isNotEmpty(input.getOffsets())){
				for (XmlGridHeightMapOffset xmlOffset : input.getOffsets()) {
					Tuple6b tuple = new Tuple6b();
					tuple.setX( start.getX().add( dx.multiply( xmlOffset.getxCoord() )));
					tuple.setY( start.getY().add( dy.multiply( xmlOffset.getyCoord() )));
					tuple.setZ( XmlLength.valueOf(xmlOffset.getOffset()) );
					vertices[xmlOffset.getxCoord()][xmlOffset.getyCoord()] = offsets.size();
					offsets.add(tuple);
				}
			}
			GridHeightMap map = new GridHeightMap(vertices, offsets);
			map.setClearanceHeight( XmlLength.valueOf( input.getClearanceHeight() ));
			map.setEnd( end );
			map.setStart( start );
			map.setProbed( input.isProbed() );
			map.setProbeFeedrate( XmlSpeed.valueOf(input.getProbeFeedrate() ));
			map.setProbeLowerHeight( XmlLength.valueOf(input.getProbeLowerHeight()));
			map.setProbeStartHeight( XmlLength.valueOf(input.getProbeStartHeight()));			
			return map;
		}
//
//	/** (inheritDoc)
//	 * @see org.goko.core.workspace.service.IExporter#export(java.lang.Object, org.goko.core.workspace.service.IMapperService)
//	 */
//	@Override
//	public GridHeightMap load(XmlGridHeightMap input, IMapperService mapperService) throws GkException {
//		XmlGridHeightMap xmlMap = new XmlGridHeightMap();
//		xmlMap.setClearanceHeight( XmlLength.valueOf(input.getClearanceHeight()) );
//		xmlMap.setStart( XmlTuple6b.valueOf(input.getStart()) );
//		xmlMap.setEnd( XmlTuple6b.valueOf(input.getEnd()) );
//		xmlMap.setProbed(input.isProbed());
//		xmlMap.setProbeLowerHeight(XmlLength.valueOf(input.getProbeLowerHeight()));
//		xmlMap.setProbeStartHeight(XmlLength.valueOf(input.getProbeLowerHeight()));
//		xmlMap.setxDivisionCount(input.getxDivisionCount());
//		xmlMap.setyDivisionCount(input.getyDivisionCount());
//		xmlMap.setProbeFeedrate( XmlSpeed.valueOf(input.getProbeFeedrate()) );
//		ArrayList<XmlTuple6b> xmlOffssets = new ArrayList<XmlTuple6b>();
//		
//		List<Tuple6b> lstOffsets = input.getOffsets();
//		if(CollectionUtils.isNotEmpty(lstOffsets)){
//			for (Tuple6b tuple6b : lstOffsets) {
//				xmlOffssets.add( XmlTuple6b.valueOf(tuple6b));
//			}
//		}
//		xmlMap.setOffsets(xmlOffssets);
//		return xmlMap; 
//	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getInputClass()
	 */
	@Override
	public Class<XmlGridHeightMap> getInputClass() {
		return XmlGridHeightMap.class;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IExporter#getOutputClass()
	 */
	@Override
	public Class<GridHeightMap> getOutputClass() {
		return GridHeightMap.class;
	}

}
