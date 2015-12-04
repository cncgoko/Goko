package org.goko.core.gcode.rs274ngcv3.element;

import java.util.Date;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;

public class StackableGCodeProviderModifier extends GCodeProvider implements IGCodeProvider, IStackableGCodeProvider{
	private IStackableGCodeProvider parent;
	private IModifier<GCodeProvider> modifier;
	private Date lastUpdateDate;

	/**
	 * Constructor
	 * @param parent the parent IGCodeProvider
	 */
	public StackableGCodeProviderModifier(IStackableGCodeProvider parent) {
		this(parent, null);
	}
	/**
	 * Constructor
	 * @param parent the parent IGCodeProvider
	 * @param modifier the modifier
	 */
	public StackableGCodeProviderModifier(IStackableGCodeProvider parent, IModifier<GCodeProvider> modifier) {
		super();
		this.parent = parent;
		this.modifier = modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		if(!modifier.isEnabled()){
			return parent.getLines();
		}
		return super.getLines();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {
		if(!modifier.isEnabled()){
			return parent.getLinesCount();
		}
		return super.getLinesCount();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		if(!modifier.isEnabled()){
			return parent.getLine(idLine);
		}
		return super.getLine(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		if(!modifier.isEnabled()){
			return parent.getLineAtIndex(indexLine);
		}
		return super.getLineAtIndex(indexLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getModificationDate()
	 */
	@Override
	public Date getModificationDate() {
		return super.getModificationDate();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#update()
	 */
	@Override
	public void update() throws GkException{
		parent.update();
		// We also have to check if the parent was updated before us
		if(lastUpdateDate == null
		|| lastUpdateDate.getTime() < modifier.getModificationDate().getTime()
		|| lastUpdateDate.getTime() < parent.getModificationDate().getTime()){
			if(modifier.isEnabled()){
				clear();
				modifier.apply(parent, this);
			}
			lastUpdateDate = new Date();
			setModificationDate(lastUpdateDate);
		}
	}

	/**
	 * Remove data in this provider
	 */
	private void clear(){
		this.cacheLines.removeAll();
		this.linesSequence.clear();
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
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getCode()
	 */
	@Override
	public String getCode() {
		return parent.getCode();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider#getIdModifier()
	 */
	@Override
	public Integer getIdModifier() {
		return modifier.getId();
	}
	/**
	 * @return the parent
	 */
	@Override
	public IStackableGCodeProvider getParent() {
		return parent;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider#setParent(org.goko.core.gcode.rs274ngcv3.element.IStackableGCodeProvider)
	 */
	@Override
	public void setParent(IStackableGCodeProvider parent) {
		this.parent = parent;
		this.lastUpdateDate = null;
	}
}
