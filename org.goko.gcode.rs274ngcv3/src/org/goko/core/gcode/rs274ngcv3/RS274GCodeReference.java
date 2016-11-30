/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeProviderReference;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;

/**
 * Delegates the retrieval of the GCode provider from a repo and an id
 * @author Psyko
 * @date 26 juin 2016
 */
public class RS274GCodeReference extends GCodeProviderReference{
	private static final GkLog LOG = GkLog.getLogger(RS274GCodeReference.class);
	/** The repo where the provider can be found */
	private IGCodeProviderRepository gcodeProviderRepository;
	/** The id of the provider */
	private Integer idGCodeProvider;
	
	/**
	 * @param rs274Service
	 * @param idGCodeProvider
	 */
	public RS274GCodeReference(IGCodeProviderRepository gcodeProviderRepository, Integer idGCodeProvider) {
		super();
		this.gcodeProviderRepository = gcodeProviderRepository;
		this.idGCodeProvider = idGCodeProvider;
	}
	
	protected IGCodeProvider get(){
		try {
			return gcodeProviderRepository.internalGetGCodeProvider(idGCodeProvider);
		} catch (GkException e) {
			LOG.error(e);
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.GCodeProviderReference#isValid()
	 */
	@Override
	public boolean isValid() {
		try {
			return gcodeProviderRepository.findGCodeProvider(idGCodeProvider) != null;
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gcodeProviderRepository == null) ? 0 : gcodeProviderRepository.hashCode());
		result = prime * result + ((idGCodeProvider == null) ? 0 : idGCodeProvider.hashCode());
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
		RS274GCodeReference other = (RS274GCodeReference) obj;
		if (gcodeProviderRepository == null) {
			if (other.gcodeProviderRepository != null)
				return false;
		} else if (!gcodeProviderRepository.equals(other.gcodeProviderRepository))
			return false;
		if (idGCodeProvider == null) {
			if (other.idGCodeProvider != null)
				return false;
		} else if (!idGCodeProvider.equals(other.idGCodeProvider))
			return false;
		return true;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#lock()
	 */
	@Override
	public void lock() {
		try {
			gcodeProviderRepository.lockGCodeProvider(idGCodeProvider);
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#unlock()
	 */
	@Override
	public void unlock() {
		try {
			gcodeProviderRepository.unlockGCodeProvider(idGCodeProvider);
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/**
	 * @return the idGCodeProvider
	 */
	public Integer getIdGCodeProvider() {
		return idGCodeProvider;
	}

	/**
	 * @param idGCodeProvider the idGCodeProvider to set
	 */
	public void setIdGCodeProvider(Integer idGCodeProvider) {
		this.idGCodeProvider = idGCodeProvider;
	}	
	
}
