/**
 * 
 */
package org.goko.core.gcode.rs274ngcv3.element.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.goko.core.gcode.element.IGCodeProviderSource;

/**
 * @author PsyKo
 * @date 13 déc. 2015
 */
public class FileGCodeSource implements IGCodeProviderSource{
	/** The target file */
	private File file;
		
	/**
	 * Constructor
	 * @param file the target file
	 */
	public FileGCodeSource(File file) {
		super();
		this.file = file;
	}


	/** (inheritDoc)
	 * @see org.goko.core.gcode.element.IGCodeProviderSource#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws GkException {		
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new GkTechnicalException(e);
		}
	}


	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

}
