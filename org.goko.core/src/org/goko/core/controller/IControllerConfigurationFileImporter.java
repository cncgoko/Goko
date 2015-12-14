/**
 * 
 */
package org.goko.core.controller;

import java.io.InputStream;

import org.goko.core.common.exception.GkException;

/**
 * Generic interface allowing to import a controller configuration 
 * 
 * @author PsyKo
 *
 */
public interface IControllerConfigurationFileImporter {

	String getFileExtension();
	
	boolean canImport() throws GkException;
	
	void importFrom(InputStream inputStream) throws GkException;
}
