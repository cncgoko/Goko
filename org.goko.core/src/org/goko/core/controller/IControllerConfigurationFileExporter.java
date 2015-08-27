/**
 * 
 */
package org.goko.core.controller;

import java.io.OutputStream;

import org.goko.core.common.exception.GkException;

/**
 * Generic interface allowing to export a controller configuration 
 * 
 * @author PsyKo
 *
 */
public interface IControllerConfigurationFileExporter {

	String getFileExtension();
	
	boolean canExport() throws GkException;
	
	void exportTo(OutputStream stream) throws GkException;
}
