package org.goko.core.gcode.element;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.validation.IValidationElement;


/**
 * @author Psyko
 * @date 22 nov. 2016
 */
public abstract class GCodeProviderReference implements IGCodeProvider{

	/**
	 * Resolves the GCodeProvider referred by this object
	 * @return IGCodeProvider
	 * @throws GkException GkException
	 */
	protected abstract IGCodeProvider get();
	
	/**
	 * Make sure this object refers to a valid provider
	 * @return <code>true</code> if the referred provider is valid, <code>false</code> otherwise
	 * @throws GkException GkException
	 */
	public abstract boolean isValid();
	
	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasErrors()
	 */
	@Override
	public boolean hasErrors() {
		return get().hasErrors();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#hasWarnings()
	 */
	@Override
	public boolean hasWarnings() {
		return get().hasWarnings();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#getValidationElements()
	 */
	@Override
	public List<IValidationElement> getValidationElements() {
		return get().getValidationElements();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#clearValidationElements()
	 */
	@Override
	public void clearValidationElements() {
		get().clearValidationElements();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.validation.IValidationTarget#addValidationElement(org.goko.core.gcode.element.validation.IValidationElement)
	 */
	@Override
	public void addValidationElement(IValidationElement element) {
		get().addValidationElement(element);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#getCode()
	 */
	@Override
	public String getCode() {
		return get().getCode();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.ICodeBean#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		get().setCode(code);
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#getId()
	 */
	@Override
	public Integer getId() {
		return get().getId();
	}

	/** (inheritDoc)
	 * @see org.goko.core.common.utils.IIdBean#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		get().setId(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		return get().getLines();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {
		return get().getLinesCount();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		return get().getLine(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		return get().getLineAtIndex(indexLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#isLocked()
	 */
	@Override
	public boolean isLocked() {
		return get().isLocked();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#setLocked(boolean)
	 */
	@Override
	public void setLocked(boolean locked) {
		get().setLocked(locked);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#lock()
	 */
	@Override
	public void lock() {
		get().lock();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#unlock()
	 */
	@Override
	public void unlock() {
		get().unlock();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProvider#getSource()
	 */
	@Override
	public IGCodeProviderSource getSource() {
		return get().getSource();
	}
	
	

}
