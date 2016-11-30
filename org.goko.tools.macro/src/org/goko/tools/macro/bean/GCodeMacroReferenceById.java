/**
 * 
 */
package org.goko.tools.macro.bean;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeProviderReference;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.log.GkLog;
import org.goko.tools.macro.service.IGCodeMacroService;

/**
 * @author Psyko
 * @date 23 nov. 2016
 */
public class GCodeMacroReferenceById extends GCodeProviderReference {
	private static final GkLog LOG = GkLog.getLogger(GCodeMacroReferenceById.class);	
	/** The repo where the provider can be found */
	private IGCodeMacroService macroService;
	/** The id of the macro */
	private Integer idMacro;
	
	/**
	 * @param rs274Service
	 * @param idGCodeProvider
	 */
	public GCodeMacroReferenceById(IGCodeMacroService macroService, Integer idMacro) {
		super();
		this.macroService = macroService;
		this.idMacro = idMacro;
	}
	
	protected IGCodeProvider get(){
		try {			
			return macroService.internalGetGCodeProviderByMacro(idMacro);
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
			return macroService.findGCodeMacro(idMacro) != null;
		} catch (GkException e) {
			LOG.error(e);
		}
		return false;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#lock()
	 */
	@Override
	public void lock() {
		try {
			macroService.lockGCodeProvider(get().getId());
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
			macroService.unlockGCodeProvider(get().getId());
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMacro == null) ? 0 : idMacro.hashCode());
		result = prime * result + ((macroService == null) ? 0 : macroService.hashCode());
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
		GCodeMacroReferenceById other = (GCodeMacroReferenceById) obj;
		if (idMacro == null) {
			if (other.idMacro != null)
				return false;
		} else if (!idMacro.equals(other.idMacro))
			return false;
		if (macroService == null) {
			if (other.macroService != null)
				return false;
		} else if (!macroService.equals(other.macroService))
			return false;
		return true;
	}

		
}
