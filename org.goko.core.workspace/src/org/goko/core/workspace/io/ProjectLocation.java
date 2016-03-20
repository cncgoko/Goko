/**
 * 
 */
package org.goko.core.workspace.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.URIUtil;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;

/**
 * @author PsyKo
 * @date 14 mars 2016
 */
public class ProjectLocation implements IProjectLocation {
	/** The name of the project */
	private String name;
	/** The project files by name */
	private Map<String, URI> mapFile;
	/** The project resource location by name */
	private Map<String, URIResourceLocation> mapUriLocation;
	/** The project uri */
	private URI projectUri;
	/** The project file URI*/
	private URI projectDescriptorUri;
	/** The data for the project descriptor */
	private ByteBuffer projectDescriptorData;
	
	/**
	 * Default constructor
	 */
	public ProjectLocation() {			
		this.name = "Untitled";
		this.mapFile = new HashMap<>();
		this.mapUriLocation = new HashMap<>();
	}
	
	public ProjectLocation(URI projectFileUri) {
		File projectFile = new File(projectFileUri);			
		this.projectUri = projectFile.getParentFile().toURI();
		this.name = FilenameUtils.getName(projectFile.getName());
		this.projectDescriptorUri = projectFileUri;
		this.mapFile = new HashMap<>();
		this.mapUriLocation = new HashMap<>();
	}
	/**
	 * Constructor 
	 */
	public ProjectLocation(URI projectUri, String name) {
		this.mapFile = new HashMap<String, URI>();
		this.projectUri = projectUri;
		this.name = name;
		this.projectDescriptorUri = new File(projectUri.toString(), name+".goko").toURI();
		this.mapFile = new HashMap<>();
		this.mapUriLocation = new HashMap<>();
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#getName()
	 */
	@Override
	public String getName() {		
		return name;
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#setProjectDescriptor(java.net.URI)
	 */
	@Override
	public void setProjectDescriptor(InputStream stream) throws GkException {		
		try {
			projectDescriptorData = ByteBuffer.wrap(IOUtils.toByteArray(stream));								
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#getProjectDescriptor()
	 */
	@Override
	public InputStream getProjectDescriptor() throws GkException {
		try {
			return projectDescriptorUri.toURL().openStream();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#addResource(java.lang.String, java.io.InputStream)
	 */
	@Override
	public IResourceLocation addResource(String name, URI uri) throws GkException {
		if(!mapFile.containsKey(name)){
			URI absoluteUri = uri;
			if(!uri.isAbsolute()){
				absoluteUri = URIUtil.makeAbsolute(uri, projectUri);
			}
			mapFile.put(name, absoluteUri);
			mapUriLocation.put(name, new URIResourceLocation(uri, absoluteUri));
		}
		return mapUriLocation.get(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#removeResource(java.lang.String)
	 */
	@Override
	public void removeResource(String name) throws GkException {
		if(!mapFile.containsKey(name)){
			throw new GkTechnicalException("File '"+name+"' does not exist in project");
		}
		if(mapUriLocation.containsKey(name)){
			mapUriLocation.remove(name);
		}
		mapFile.remove(name);
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#readResource(java.lang.String)
	 */
	@Override
	public IResourceLocation readResource(String name) throws GkException {
		if(!mapFile.containsKey(name)){
			throw new GkTechnicalException("File '"+name+"' does not exist in project");
		}
		return mapUriLocation.get(name);		
	}

	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#write()
	 */
	@Override
	public void write() throws GkException {		
		try {
			createProjectFolder();
			File projectFolder = new File(this.projectUri);
			
			if(!projectFolder.exists()){
				// Create the project folder
				projectFolder.mkdirs();
			}	
			
			// Write project descriptor
			File projectDescriptor = new File(projectDescriptorUri);
			if(!projectDescriptor.exists()){
				projectDescriptor.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(projectDescriptor);
			InputStream input = new ByteArrayInputStream(projectDescriptorData.array());
			IOUtils.copy(input, fileOutputStream);
			input.close();
			fileOutputStream.close();
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}

	}
	
	protected void createProjectFolder(){
		File projectFolder = new File(this.projectUri);
		
		if(!projectFolder.exists()){
			// Create the project folder
			projectFolder.mkdirs();
		}
		
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#importProjectDependencies()
	 */
	@Override
	public void importProjectDependencies() throws GkException {
		createProjectFolder();
		// Export each URI to local folder
		Set<Entry<String, URI>> entrySet = mapFile.entrySet();
		
		
		for (Entry<String, URI> entry : entrySet) {
			URI resourceUri = entry.getValue();
			URIResourceLocation resource = mapUriLocation.get(entry.getKey());		
			
			if(resource.getReferenceCount() > 0){				
				if(resource.getUri().isAbsolute()){					
					importResource(name, resource);
				}
			}else{
				if(!resource.getUri().isAbsolute()){					
					deleteResource(name, resource);
				}
			}
		}

	}
	
	protected void importResource(String name, URIResourceLocation resource) throws GkException{		
		String resourceName = URIUtil.toFile(resource.getAbsoluteUri()).getName();
		try {
			// Copy the resource in the project folder using the resource name
			if(StringUtils.isNotBlank(resourceName)){							
				URI projectResource = URIUtil.append(projectUri, resourceName);
				File targetFile = new File(projectResource);
				if(!targetFile.exists()){
					targetFile.createNewFile();
				}					
				InputStream sourceInputStream = resource.openInputStream();
				FileOutputStream fileOutputStream = new FileOutputStream(targetFile);				
				IOUtils.copy(sourceInputStream, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				sourceInputStream.close();
				URI relativeUri = URIUtil.makeRelative(projectResource, projectUri);
				resource.setUri(relativeUri);
				mapUriLocation.remove(name);				
				mapUriLocation.put(resourceName, resource);
				
			}			
		} catch (IOException e) {
			throw new GkTechnicalException(e);
		}
	}
	
	protected void deleteResource(String name, URIResourceLocation resource){
		URI projectResource = URIUtil.makeAbsolute(resource.getUri(), projectUri);
		File targetFile = new File(projectResource);
		if(targetFile.exists() && targetFile.canWrite()){
			targetFile.delete();
		}
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#read()
	 */
	@Override
	public void read() throws GkException {
		readFolder(new File(projectUri));
	}
	
	protected void readFolder(File folder) throws GkException{
		File[] children = folder.listFiles();
		for (File file : children) {			
			if(file.isDirectory()){
				readFolder(file);
			}else{	
				// Make sure we don't read project file 
				if(!URIUtil.sameURI(projectDescriptorUri, file.toURI())){
					URI relativePath = URIUtil.makeRelative(file.toURI(), projectUri);
					addResource(relativePath.toString(), relativePath);
				}
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#isLocationDefined()
	 */
	@Override
	public boolean isLocationDefined() {		
		return projectUri != null;
	}
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#setLocation(java.net.URI)
	 */
	@Override
	public void setLocation(URI target) {
		projectUri = target;			
		projectDescriptorUri = URIUtil.append(projectUri, name+".goko");		
	}
	
	/** (inheritDoc)
	 * @see org.goko.core.workspace.io.IProjectLocation#getLocation()
	 */
	@Override
	public URI getLocation() {		
		return projectUri;
	}
	
	protected URI getURI(String name){
		return mapFile.get(name);
	}
}