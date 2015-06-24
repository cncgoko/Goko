/*******************************************************************************
 * 	This file is part of Goko.
 *
 *   Goko is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Goko is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package org.goko.gcode.filesender.controller;

import java.util.Date;

import org.goko.common.bindings.AbstractModelObject;
import org.goko.core.controller.bean.MachineState;
import org.goko.core.gcode.bean.IGCodeProvider;

public class GCodeFileSenderBindings extends AbstractModelObject {
	/** The absolute file to the GCode file */
	private String filePath;
	private String fileName;
	private String fileSize;
	private String fileLastUpdate;
	private GCodeDocumentProvider gCodeDocument;
	private int sentCommandCount;
	private int totalCommandCount;
	private Integer completedCommandCount;
	private MachineState controllerState;
	private boolean streamingInProgress;
	private boolean streamingAllowed;
	private String elapsedTime;
	private String remainingTime;
	private Date startDate;
	private Date endDate;
	private int selectedCommand;
	private IGCodeProvider gcodeProvider;
	/**
	 * @return the filepath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filepath the filepath to set
	 */
	public void setFilePath(String filePath) {
		firePropertyChange("filePath", this.filePath, this.filePath = filePath);
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		firePropertyChange("fileName", this.fileName, this.fileName = fileName);
	}

	/**
	 * @return the fileSize
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(String fileSize) {
		firePropertyChange("fileSize", this.fileSize, this.fileSize = fileSize);
	}

	/**
	 * @return the fileLastUpdate
	 */
	public String getFileLastUpdate() {
		return fileLastUpdate;
	}

	/**
	 * @param fileLastUpdate the fileLastUpdate to set
	 */
	public void setFileLastUpdate(String fileLastUpdate) {
		firePropertyChange("fileLastUpdate", this.fileLastUpdate, this.fileLastUpdate = fileLastUpdate);
	}

	/**
	 * @return the gCodeDocument
	 */
	public GCodeDocumentProvider getgCodeDocument() {
		return gCodeDocument;
	}

	/**
	 * @param gCodeDocument the gCodeDocument to set
	 */
	public void setgCodeDocument(GCodeDocumentProvider gCodeDocument) {
		firePropertyChange("gCodeDocument", this.gCodeDocument, this.gCodeDocument = gCodeDocument);
	}

	/**
	 * @return the sentCommandCount
	 */
	public int getSentCommandCount() {
		return sentCommandCount;
	}

	/**
	 * @param sentCommandCount the sentCommandCount to set
	 */
	public void setSentCommandCount(int sentCommandCount) {
		firePropertyChange("sentCommandCount", this.sentCommandCount, this.sentCommandCount = sentCommandCount);
	}

	/**
	 * @return the totalCommandCount
	 */
	public int getTotalCommandCount() {
		return totalCommandCount;
	}

	/**
	 * @param totalCommandCount the totalCommandCount to set
	 */
	public void setTotalCommandCount(int totalCommandCount) {
		firePropertyChange("totalCommandCount", this.totalCommandCount, this.totalCommandCount = totalCommandCount);
	}

	/**
	 * @return the completedCommandCount
	 */
	public Integer getCompletedCommandCount() {
		return completedCommandCount;
	}

	/**
	 * @param completedCommandCount the completedCommandCount to set
	 */
	public void setCompletedCommandCount(Integer completedCommandCount) {
		firePropertyChange("completedCommandCount", this.completedCommandCount, this.completedCommandCount = completedCommandCount);
	}

	/**
	 * @return the controllerState
	 */
	public MachineState getControllerState() {
		return controllerState;
	}

	/**
	 * @param controllerState the controllerState to set
	 */
	public void setControllerState(MachineState controllerState) {
		firePropertyChange("controllerState", this.controllerState, this.controllerState = controllerState);
	}

	/**
	 * @return the streamingInProgress
	 */
	public boolean isStreamingInProgress() {
		return streamingInProgress;
	}

	/**
	 * @return the streamingInProgress
	 */
	public boolean getStreamingInProgress() {
		return isStreamingInProgress();
	}

	/**
	 * @param streamingInProgress the streamingInProgress to set
	 */
	public void setStreamingInProgress(boolean streamingInProgress) {
		firePropertyChange("streamingInProgress", this.streamingInProgress, this.streamingInProgress = streamingInProgress);
	}

	/**
	 * @return the streamingAllowed
	 */
	public boolean isStreamingAllowed() {
		return streamingAllowed;
	}
	/**
	 * @return the streamingAllowed
	 */
	public boolean getStreamingAllowed() {
		return isStreamingAllowed();
	}

	/**
	 * @param streamingAllowed the streamingAllowed to set
	 */
	public void setStreamingAllowed(boolean streamingAllowed) {
		firePropertyChange("streamingAllowed", this.streamingAllowed, this.streamingAllowed = streamingAllowed);
	}

	/**
	 * @return the elapsedTime
	 */
	public String getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * @param elapsedTime the elapsedTime to set
	 */
	public void setElapsedTime(String elapsedTime) {
		firePropertyChange("elapsedTime", this.elapsedTime, this.elapsedTime = elapsedTime);
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the remainingTime
	 */
	public String getRemainingTime() {
		return remainingTime;
	}

	/**
	 * @param remainingTime the remainingTime to set
	 */
	public void setRemainingTime(String remainingTime) {
		firePropertyChange("remainingTime", this.remainingTime, this.remainingTime = remainingTime);
	}

	/**
	 * @return the selectedCommand
	 */
	public int getSelectedCommand() {
		return selectedCommand;
	}

	/**
	 * @param selectedCommand the selectedCommand to set
	 */
	public void setSelectedCommand(int selectedCommand) {
		firePropertyChange("selectedCommand", this.selectedCommand, this.selectedCommand = selectedCommand);
	}

	/**
	 * @return the gcodeProvider
	 */
	public IGCodeProvider getGcodeProvider() {
		return gcodeProvider;
	}

	/**
	 * @param gcodeProvider the gcodeProvider to set
	 */
	public void setGcodeProvider(IGCodeProvider gcodeProvider) {
		firePropertyChange("gcodeProvider", this.gcodeProvider, this.gcodeProvider = gcodeProvider);
	}




}
