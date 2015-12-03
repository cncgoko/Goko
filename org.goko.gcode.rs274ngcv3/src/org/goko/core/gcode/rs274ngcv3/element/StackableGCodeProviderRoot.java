package org.goko.core.gcode.rs274ngcv3.element;

import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;

public class StackableGCodeProviderRoot extends GCodeProvider implements IGCodeProvider, IStackableGCodeProvider{
	private IGCodeProvider parent;

	/**
	 * Constructor
	 * @param parent the parent IGCodeProvider
	 */
	public StackableGCodeProviderRoot(IGCodeProvider parent) {
		super();
		this.parent = parent;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		return parent.getLines();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {
		return parent.getLinesCount();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#addLine(org.goko.core.gcode.element.GCodeLine)
	 */
	@Override
	public void addLine(GCodeLine line) throws GkException {
		throw new GkTechnicalException("Cannot add line to existing GCodeProvider");
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getId()
	 */
	@Override
	public Integer getId() {
		return parent.getId();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#setId(java.lang.Integer)
	 */
	@Override
	public void setId(Integer id) {
		parent.setId(id);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		return parent.getLine(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		return parent.getLineAtIndex(indexLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getCode()
	 */
	@Override
	public String getCode() {
		return parent.getCode();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#setCode(java.lang.String)
	 */
	@Override
	public void setCode(String code) {
		parent.setCode(code);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#update()
	 */
	@Override
	public void update() throws GkException {
		// Nothing
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider#getIdModifier()
	 */
	@Override
	public Integer getIdModifier() {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider#getParent()
	 */
	@Override
	public IStackableGCodeProvider getParent() {
		return null;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider#setParent(org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider)
	 */
	@Override
	public void setParent(IStackableGCodeProvider paarent) {}
}
