package org.goko.core.gcode.rs274ngcv3.element;

import java.util.Date;
import java.util.List;

import org.goko.core.common.exception.GkException;
import org.goko.core.gcode.element.GCodeLine;
import org.goko.core.gcode.element.IGCodeProvider;
import org.goko.core.gcode.element.IGCodeProviderSource;

public class StackableGCodeProviderModifier extends GCodeProvider implements IGCodeProvider, IStackableGCodeProvider{
	private IStackableGCodeProvider parent;
	private IStackableGCodeProvider child;
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
		this.parent.setChild(this);
		this.modifier = modifier;
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLines()
	 */
	@Override
	public List<GCodeLine> getLines() throws GkException {
		if(!modifier.isEnabled() || !modifier.isConfigured()){
			return parent.getLines();
		}
		return super.getLines();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLinesCount()
	 */
	@Override
	public int getLinesCount() throws GkException {
		if(!modifier.isEnabled() || !modifier.isConfigured()){
			return parent.getLinesCount();
		}
		return super.getLinesCount();
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLine(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLine(Integer idLine) throws GkException {
		if(!modifier.isEnabled() || !modifier.isConfigured()){
			return parent.getLine(idLine);
		}
		return super.getLine(idLine);
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getLineAtIndex(java.lang.Integer)
	 */
	@Override
	public GCodeLine getLineAtIndex(Integer indexLine) throws GkException {
		if(!modifier.isEnabled() || !modifier.isConfigured()){
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
			if(modifier.isEnabled() && modifier.isConfigured()){
				clear();
				modifier.apply(parent, this);
			}
			lastUpdateDate = new Date();
			setModificationDate(lastUpdateDate);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getId()
	 */
	@Override
	public Integer getId() {
		if(parent!= null){
			return parent.getId();
		}
		return null;
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
	/** (inheritDoc)
	 * @see org.goko.core.gcode.rs274ngcv3.element.GCodeProvider#getSource()
	 */
	@Override
	public IGCodeProviderSource getSource() {		
		return parent.getSource();
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}
	
	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StackableGCodeProviderModifier other = (StackableGCodeProviderModifier) obj;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	/**
	 * @return the child
	 */
	public IStackableGCodeProvider getChild() {
		return child;
	}
	/**
	 * @param child the child to set
	 */
	public void setChild(IStackableGCodeProvider child) {
		this.child = child;
	}
	
}
