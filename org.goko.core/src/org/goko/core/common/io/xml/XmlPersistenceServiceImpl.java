package org.goko.core.common.io.xml;

import java.io.InputStream;
import java.io.OutputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.common.io.xml.bean.XmlColor;
import org.goko.core.common.io.xml.bean.XmlColorTransform;
import org.goko.core.common.io.xml.quantity.XmlAngle;
import org.goko.core.common.io.xml.quantity.XmlAngleTransform;
import org.goko.core.common.io.xml.quantity.XmlLength;
import org.goko.core.common.io.xml.quantity.XmlLengthTransform;
import org.goko.core.common.io.xml.quantity.XmlSpeed;
import org.goko.core.common.io.xml.quantity.XmlSpeedTransform;
import org.goko.core.log.GkLog;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.TypedRegistry;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.TypedTreeStrategy;
import org.simpleframework.xml.transform.RegistryMatcher;

public class XmlPersistenceServiceImpl implements IXmlPersistenceService {
	/** Log */
	private static final GkLog LOG = GkLog.getLogger(XmlPersistenceServiceImpl.class);
	/** Service id */
	private static final String SERVICE_ID = "org.goko.core.common.io.xml.XmlPersistenceServiceImpl";
	/** The class mapping */
	private TypedRegistry registry;
	/** The used serializer */
	private Serializer persister;
	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#getServiceId()
	 */
	@Override
	public String getServiceId() throws GkException {
		return SERVICE_ID;
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#start()
	 */
	@Override
	public void start() throws GkException {
		LOG.info("Starting "+getServiceId());
		RegistryMatcher matcher = new RegistryMatcher();
		matcher.bind(XmlLength.class, new XmlLengthTransform());
		matcher.bind(XmlSpeed.class, new XmlSpeedTransform());
		matcher.bind(XmlAngle.class, new XmlAngleTransform());
		matcher.bind(XmlColor.class, new XmlColorTransform());
		this.registry = new TypedRegistry();		
		this.persister = new Persister(new TypedTreeStrategy(registry), matcher);
		
		LOG.info("Successfully started "+getServiceId());
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.service.IGokoService#stop()
	 */
	@Override
	public void stop() throws GkException {
		// TODO Auto-generated method stub

	}

	/** (inheritDoc)
	 * @see org.goko.core.common.io.xml.IXmlPersistenceService#register(java.lang.Class)
	 */
	@Override
	public void register(Class<?> clazz) throws GkException {
		try {
			registry.register(clazz);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.io.xml.IXmlPersistenceService#read(java.lang.Class, java.io.File)
	 */
	@Override
	public <T> T read(Class<T> type, InputStream input) throws GkException {
		try {
			return persister.read(type, input);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.io.xml.IXmlPersistenceService#write(java.lang.Object, java.io.File)
	 */
	@Override
	public <T> void write(T type, OutputStream output) throws GkException {
		try {
			persister.write(type, output);
		} catch (Exception e) {
			throw new GkTechnicalException(e);
		}
	}

}
