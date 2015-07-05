package org.goko.featuremanager.installer;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.ICopyright;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnitFragment;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.ITouchpointType;
import org.eclipse.equinox.p2.metadata.IUpdateDescriptor;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;

public class GkInstallableUnit implements IInstallableUnit {
	private IInstallableUnit baseUnit;
	private GkInstallableUnitCategory category;	
	private boolean installed;
	
	public GkInstallableUnit(IInstallableUnit wrappedUnit) {
		super();
		this.baseUnit = wrappedUnit;
	}
	
	public String getName(){
		return StringUtils.defaultString(baseUnit.getProperty("org.eclipse.equinox.p2.name"));
	}
	
	public String getCategoryName(){
		return StringUtils.defaultString(baseUnit.getProperty("org.eclipse.equinox.p2.type.category"));
	}
	

	public String getDescription() {
		return StringUtils.defaultString(baseUnit.getProperty(PROP_DESCRIPTION));
	}
	
	/** (inheritDoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IInstallableUnit o) {
		return baseUnit.compareTo(o);
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IVersionedId#getId()
	 */
	public String getId() {
		return baseUnit.getId();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IVersionedId#getVersion()
	 */
	public Version getVersion() {
		return baseUnit.getVersion();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getArtifacts()
	 */
	public Collection<IArtifactKey> getArtifacts() {
		return baseUnit.getArtifacts();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getFilter()
	 */
	public IMatchExpression<IInstallableUnit> getFilter() {
		return baseUnit.getFilter();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getFragments()
	 */
	public Collection<IInstallableUnitFragment> getFragments() {
		return baseUnit.getFragments();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getProperties()
	 */
	public Map<String, String> getProperties() {
		return baseUnit.getProperties();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return baseUnit.getProperty(key);
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(String key, String locale) {
		return baseUnit.getProperty(key, locale);
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getProvidedCapabilities()
	 */
	public Collection<IProvidedCapability> getProvidedCapabilities() {
		return baseUnit.getProvidedCapabilities();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getRequirements()
	 */
	public Collection<IRequirement> getRequirements() {
		return baseUnit.getRequirements();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getMetaRequirements()
	 */
	public Collection<IRequirement> getMetaRequirements() {
		return baseUnit.getMetaRequirements();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getTouchpointData()
	 */
	public Collection<ITouchpointData> getTouchpointData() {
		return baseUnit.getTouchpointData();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getTouchpointType()
	 */
	public ITouchpointType getTouchpointType() {
		return baseUnit.getTouchpointType();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#isResolved()
	 */
	public boolean isResolved() {
		return baseUnit.isResolved();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#isSingleton()
	 */
	public boolean isSingleton() {
		return baseUnit.isSingleton();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#satisfies(org.eclipse.equinox.p2.metadata.IRequirement)
	 */
	public boolean satisfies(IRequirement candidate) {
		return baseUnit.satisfies(candidate);
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#unresolved()
	 */
	public IInstallableUnit unresolved() {
		return baseUnit.unresolved();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getUpdateDescriptor()
	 */
	public IUpdateDescriptor getUpdateDescriptor() {
		return baseUnit.getUpdateDescriptor();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getLicenses()
	 */
	public Collection<ILicense> getLicenses() {
		return baseUnit.getLicenses();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getLicenses(java.lang.String)
	 */
	public Collection<ILicense> getLicenses(String locale) {
		return baseUnit.getLicenses(locale);
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getCopyright()
	 */
	public ICopyright getCopyright() {
		return baseUnit.getCopyright();
	}

	/** (inheritDoc)
	 * @see org.eclipse.equinox.p2.metadata.IInstallableUnit#getCopyright(java.lang.String)
	 */
	public ICopyright getCopyright(String locale) {
		return baseUnit.getCopyright(locale);
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return baseUnit.equals(obj);
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(GkInstallableUnitCategory category) {
		this.category = category;
	}

	/**
	 * @return the category
	 */
	public GkInstallableUnitCategory getCategory() {
		return category;
	}

	/**
	 * @return the baseUnit
	 */
	public IInstallableUnit getBaseUnit() {
		return baseUnit;
	}

	/**
	 * @return the installed
	 */
	public boolean isInstalled() {
		return installed;
	}

	/**
	 * @param installed the installed to set
	 */
	public void setInstalled(boolean installed) {
		this.installed = installed;
	}


	
}
