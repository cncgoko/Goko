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
public class GCodeMacroReference extends GCodeProviderReference {
	private static final GkLog LOG = GkLog.getLogger(GCodeMacroReference.class);	
	/** The repo where the provider can be found */
	private IGCodeMacroService macroService;
	/** The code of the macro */
	private String codeMacro;
	
	/**
	 * @param rs274Service
	 * @param idGCodeProvider
	 */
	public GCodeMacroReference(IGCodeMacroService macroService, String codeMacro) {
		super();
		this.macroService = macroService;
		this.codeMacro = codeMacro;
	}
	
	protected IGCodeProvider get(){
		try {
			GCodeMacro macro = macroService.getGCodeMacro(codeMacro);
			return macroService.internalGetGCodeProviderByMacro(macro.getId());
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
			return macroService.findGCodeMacro(codeMacro) != null;
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
		result = prime * result + ((codeMacro == null) ? 0 : codeMacro.hashCode());
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
		GCodeMacroReference other = (GCodeMacroReference) obj;
		if (codeMacro == null) {
			if (other.codeMacro != null)
				return false;
		} else if (!codeMacro.equals(other.codeMacro))
			return false;
		if (macroService == null) {
			if (other.macroService != null)
				return false;
		} else if (!macroService.equals(other.macroService))
			return false;
		return true;
	}
	
	
}
