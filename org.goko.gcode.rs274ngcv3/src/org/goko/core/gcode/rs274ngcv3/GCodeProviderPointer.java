/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;
import org.goko.core.gcode.element.validation.IValidationElement;
import org.goko.core.gcode.service.IGCodeProviderRepository;
import org.goko.core.log.GkLog;

/**
 * Delegates the retrieval of the GCode provider from a repo and an id
 * @author Psyko
 * @date 26 juin 2016
 */
public class GCodeProviderPointer implements IGCodeProvider{
	private static final GkLog LOG = GkLog.getLogger(GCodeProviderPointer.class);
	/** The repo where the provider can be found */
	private IGCodeProviderRepository gcodeProviderRepository;
	/** The id of the provider */
	private Integer idGCodeProvider;
	
	/**
	 * @param rs274Service
	 * @param idGCodeProvider
	 */
	public GCodeProviderPointer(IGCodeProviderRepository gcodeProviderRepository, Integer idGCodeProvider) {
		super();
		this.gcodeProviderRepository = gcodeProviderRepository;
		this.idGCodeProvider = idGCodeProvider;
	}
	
	private IGCodeProvider getProvider(){
		try {
			return gcodeProviderRepository.internalGetGCodeProvider(idGCodeProvider);
		} catch (GkException e) {
			LOG.error(e);
		}
		return null;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#getId()
	 */
	@Override
	public Integer getId() {		
		return getProvider().getId();
	}
	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		getProvider().setId(id);		
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return getProvider().hasErrors();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasWarnings()
	 */
	@Override
	public boolean hasWarnings() {
		return getProvider().hasWarnings();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#getValidationElements()
	 */
	@Override
	public List<IValidationElement> getValidationElements() {
		return getProvider().getValidationElements();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#clearValidationElements()
	 */
	@Override
	public void clearValidationElements() {
		getProvider().clearValidationElements();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#addValidationElement(org.goko.core.gcode.element.validation.IValidationElement)
	 */
	@Override
	public void addValidationElement(IValidationElement element) {
		getProvider().addValidationElement(element);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getCode()
	 */
	@Override
	public String getCode() {
		return getProvider().getCode();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		getProvider().getCode();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		return getProvider().getLines();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {		
		return getProvider().getLinesCount();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {		
		return getProvider().getLine(idLine);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		return getProvider().getLineAtIndex(indexLine);
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return getProvider().isLocked();
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#setLocked(boolean)
	 */
	@Override
	public void setLocked(boolean locked) {
		getProvider().setLocked(locked);	
	}
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getSource()
	 */
	@Override
	public IGCodeProviderSource getSource() {
		return getProvider().getSource();
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
		GCodeProviderPointer other = (GCodeProviderPointer) obj;
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
	
	
}
