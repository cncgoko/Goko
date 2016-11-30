/**
 * 
 */
package org.goko.gcode.rs274ngcv3.xml.service;

import java.util.ArrayList;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.AbstractGokoService;
import org.goko.core.execution.monitor.io.bean.XmlExecutionToken;
import org.goko.core.gcode.rs274ngcv3.IRS274NGCService;
import org.goko.core.workspace.service.IExporter;
import org.goko.core.workspace.service.ILoader;
import org.goko.gcode.rs274ngcv3.xml.bean.XmlRS274GCodeReference;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlRotateModifier;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlScaleModifier;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlSegmentizeModifier;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlTranslateModifier;
import org.goko.gcode.rs274ngcv3.xml.bean.modifier.XmlWrapModifier;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlExternalFileGCodeSource;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlResourceLocationGCodeSource;
import org.goko.gcode.rs274ngcv3.xml.bean.source.XmlStringGCodeSource;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.ArrayModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.RotateModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.ScaleModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.SegmentizeModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.TranslateModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.modifier.WrapModifierExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.reference.RS274GCodeReferenceExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.source.ResourceLocationGCodeSourceExporter;
import org.goko.gcode.rs274ngcv3.xml.exporter.source.StringGCodeSourceExporter;
import org.goko.gcode.rs274ngcv3.xml.loader.executiontoken.XmlExecutionTokenLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.ArrayModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.RotateModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.ScaleModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.SegmentizeModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.TranslateModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.modifier.WrapModifierLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.reference.RS274GCodeReferenceLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.source.ResourceLocationGCodeSourceLoader;
import org.goko.gcode.rs274ngcv3.xml.loader.source.StringGCodeSourceLoader;

/**
 * @author Psyko
 * @date 25 nov. 2016
 */
public class GCodeXmlPersistenceService extends AbstractGokoService implements IGCodeXmlPersistenceService {
	/** SERVICE ID */
	private static final String SERVICE_ID = "org.goko.gcode.rs274ngcv3.xml.service.GCodeXmlPersistenceService";
	/** Underlying IRS274NGCService  */
	private IRS274NGCService rs274service;
	
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.AbstractGokoService#startService()
	 */
	@Override
	protected void startService() throws GkException { }
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperProvider#getLoader()
	 */
	@Override
	public List<ILoader<?, ?>> getLoader() throws GkException {
		List<ILoader<?, ?>> loaders = new ArrayList<>();
		loaders.add(new ResourceLocationGCodeSourceLoader());
		loaders.add(new StringGCodeSourceLoader());		
		loaders.add(new SegmentizeModifierLoader());
		loaders.add(new TranslateModifierLoader());		
		loaders.add(new ScaleModifierLoader());		
		loaders.add(new WrapModifierLoader());		
		loaders.add(new RotateModifierLoader());
		loaders.add(new ArrayModifierLoader());
		// Reference
		loaders.add(new RS274GCodeReferenceLoader(rs274service));
		// Backward compatibility only
		loaders.add(new XmlExecutionTokenLoader(rs274service));
		
		return loaders;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.service.IMapperProvider#getExporter()
	 */
	@Override
	public List<IExporter<?, ?>> getExporter() throws GkException {
		List<IExporter<?, ?>> exporters = new ArrayList<>();
		exporters.add(new ResourceLocationGCodeSourceExporter());
		exporters.add(new StringGCodeSourceExporter());
		exporters.add(new TranslateModifierExporter());		
		exporters.add(new SegmentizeModifierExporter());		
		exporters.add(new ScaleModifierExporter());
		exporters.add(new WrapModifierExporter());
		exporters.add(new RotateModifierExporter());
		exporters.add(new ArrayModifierExporter());
		// Reference
		exporters.add(new RS274GCodeReferenceExporter());
		return exporters;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.io.xml.IXmlPersistenceProvider#getSupportedClass()
	 */
	@Override
	public List<Class<?>> getSupportedClass() throws GkException {
		List<Class<?>> lstClazz = new ArrayList<>();
				
		lstClazz.add(XmlExternalFileGCodeSource.class);		
		lstClazz.add(XmlResourceLocationGCodeSource.class);
		lstClazz.add(XmlStringGCodeSource.class);
		lstClazz.add(XmlSegmentizeModifier.class);		
		lstClazz.add(XmlTranslateModifier.class);		
		lstClazz.add(XmlWrapModifier.class);		
		lstClazz.add(XmlScaleModifier.class);		
		lstClazz.add(XmlRotateModifier.class);		
		lstClazz.add(XmlRS274GCodeReference.class);
		lstClazz.add(XmlExecutionToken.class);
		
		return lstClazz;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.AbstractGokoService#stopService()
	 */
	@Override
	protected void stopService() throws GkException {
		
	}

	/**
	 * @return the rs274service
	 */
	public IRS274NGCService getRs274service() {
		return rs274service;
	}

	/**
	 * @param rs274service the rs274service to set
	 */
	public void setRs274service(IRS274NGCService rs274service) {
		this.rs274service = rs274service;
	}



}
