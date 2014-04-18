/*
 *
 *   Goko
 *   Copyright (C) 2013  PsyKo
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.goko.serial.bindings;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;
import org.goko.internal.SerialActivator;
import org.goko.serial.preferences.SerialConsolePreferences;

public class SerialConsoleController extends AbstractController<SerialConsoleBindings> implements IConnectionDataListener {
	private static GkLog LOG = GkLog.getLogger(SerialConsoleController.class);
	private StyledText consoleWidget;

	@Inject
	IConnectionService connectionService;


	public SerialConsoleController(SerialConsoleBindings binding) {
		super(binding);
	}

	@Override
	public void initialize() throws GkException {
		connectionService.addInputDataListener(this);
		connectionService.addOutputDataListener(this);
	}

	public void clearConsole() {
		getDataModel().setConsole("");
	}

	public void sendCurrentCommand() {
		try {
			if(connectionService.isConnected()){
				String cmdText = getDataModel().getCurrentCommand() + getDataModel().getEndLineToken().getValue();
				addCommandHistory(cmdText);
				resetCommandHistoryIndex();
				List<Byte> data = GkUtils.toBytesList(cmdText);
				connectionService.send(data);
			}
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	public void resetCommandHistoryIndex() {
		getDataModel().setCommandHistoryIndex(-1);
		getDataModel().setCurrentCommand( StringUtils.EMPTY );
	}

	private void addCommandHistory(String command) {
		getDataModel().getCommandHistory().add(command);
	}

	public void selectPreviousCommandInHistory() {
		int index = getDataModel().getCommandHistoryIndex();
		if(index + 1 < CollectionUtils.size( getDataModel().getCommandHistory() ) ){
			index = index + 1;
			getDataModel().setCommandHistoryIndex(index);
		}
		//int reversedIndex = getDataModel().getCommandHistory().size() - index;
		index = Math.max(0, Math.min(index, getDataModel().getCommandHistory().size() - 1));
		String cmd = getDataModel().getCommandHistory().get(index);
		getDataModel().setCurrentCommand( cmd );
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataReceived(java.util.List)
	 */
	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
		if(getDataModel().isEnabled()){
			appendTextToConsoleWidget(GkUtils.toString(data),SWT.COLOR_BLACK);
			updateCarretPosition();
		}
	}

	protected void appendTextToConsoleWidget(final String text, final int color){
		if(consoleWidget != null){
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					int initialCharCount = consoleWidget.getCharCount();

					consoleWidget.append(text);

					StyleRange[] ranges = new StyleRange[1];


					ranges[0] = new StyleRange();
					ranges[0].start = initialCharCount;
					ranges[0].length = StringUtils.length(text);
					ranges[0].foreground = SWTResourceManager.getColor(color);
					consoleWidget.replaceStyleRanges(ranges[0].start, ranges[0].length, ranges);

					if(SerialActivator.getPreferenceStore() .getBoolean(SerialConsolePreferences.SERIAL_CONSOLE_LIMIT_CHARACTER)){
						int maxChar = SerialActivator.getPreferenceStore() .getInt(SerialConsolePreferences.SERIAL_CONSOLE_MAX_CHARACTER);

						if(consoleWidget.getCharCount() > maxChar){
							int delta = consoleWidget.getCharCount() - maxChar;
							consoleWidget.setText( StringUtils.substring(consoleWidget.getText(), delta));
						}
					}
				}
			});

		}
	}

	protected void updateCarretPosition(){
		if(consoleWidget != null){
			if( !getDataModel().isLockScroll() ){
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						consoleWidget.setTopIndex(consoleWidget.getLineCount() - 1);
					}
				});
			}
		}
	}
	/** (inheritDoc)
	 * @see org.goko.core.connection.IConnectionDataListener#onDataSent(java.util.List)
	 */
	@Override
	public void onDataSent(List<Byte> data) throws GkException {
		if(getDataModel().isEnabled()){
			appendTextToConsoleWidget(GkUtils.toString(data),SWT.COLOR_BLUE);
			updateCarretPosition();

		}
	}


	/**
	 * @return the consoleWidget
	 */
	public StyledText getConsoleWidget() {
		return consoleWidget;
	}

	/**
	 * @param consoleWidget the consoleWidget to set
	 */
	public void setConsoleWidget(StyledText consoleWidget) {
		this.consoleWidget = consoleWidget;
	}

}
