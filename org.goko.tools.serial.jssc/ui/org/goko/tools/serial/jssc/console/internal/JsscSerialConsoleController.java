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
package org.goko.tools.serial.jssc.console.internal;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.GkUtils;
import org.goko.core.common.buffer.ByteCommandBuffer;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;

public class JsscSerialConsoleController extends AbstractController<JsscSerialConsoleModel> implements IConnectionDataListener {
	private static final GkLog LOG = GkLog.getLogger(JsscSerialConsoleController.class);
	@Inject
	IConnectionService connectionService;
	private StyledText textDisplay;
	private ByteCommandBuffer inputBuffer;
	private ByteCommandBuffer outputBuffer;

	public JsscSerialConsoleController() {
		super(new JsscSerialConsoleModel());
		inputBuffer  = new ByteCommandBuffer((byte)('\n'));
		outputBuffer = new ByteCommandBuffer((byte)('\n'));

	}

	@Override
	public void initialize() throws GkException {
		// TODO Auto-generated method stub
		connectionService.addOutputDataListener(this);
		connectionService.addInputDataListener(this);

	}

	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
		if(getDataModel().isConsoleEnabled()){
			inputBuffer.addAll(data);
			while(inputBuffer.hasNext()){
				final String text = GkUtils.toString(inputBuffer.unstackNextCommand());

				Display.getDefault().asyncExec(new Runnable() {
				    @Override
					public void run() {
				    	if(!textDisplay.isDisposed()){
					    	textDisplay.append(text);
					    	if(!getDataModel().isScrollLock()){
					    		textDisplay.setTopIndex(textDisplay.getLineCount() - 1);
					    	}
				    	}
				    }
				});
			}
		}
	}

	@Override
	public void onDataSent(List<Byte> data) throws GkException {
		if(getDataModel().isConsoleEnabled()){
			outputBuffer.addAll(data);
			if(outputBuffer.hasNext()){
				final String text = GkUtils.toString(outputBuffer.unstackNextCommand());
				Display.getDefault().asyncExec(new Runnable() {
				    @Override
					public void run() {
				    	if(!textDisplay.isDisposed()){
					    	textDisplay.append(text);
					    	if(!getDataModel().isScrollLock()){
					    		textDisplay.setTopIndex(textDisplay.getLineCount() - 1);
					    	}
				    	}
				    }
				});
			}
		}

	}

	public void setTextDisplay(StyledText text_1) {
		this.textDisplay = text_1;
	}

	private List<Byte> getCompleteCommand(){
		List<Byte> baseList = GkUtils.toBytesList(getDataModel().getCommand());
		baseList.addAll(GkUtils.toBytesList( getDataModel().getEndLineToken().getValue()));
		return baseList;
	}

	public void sendCurrentCommand() throws GkException {
		if(StringUtils.isNotBlank(getDataModel().getCommand())){
			connectionService.send(getCompleteCommand());
			historizeCommand(getDataModel().getCommand());
			resetHistoryIndex();
			getDataModel().setCommand(StringUtils.EMPTY);
		}
	}

	public void climbHistoryUp(){
		incrementHistoryIndex(1);
	}
	public void climbHistoryDown(){
		incrementHistoryIndex(-1);
	}
	protected void incrementHistoryIndex(int value){
		int maxValue = getDataModel().getCommandHistory().size() - 1;
		int newValue = getDataModel().getCurrentHistoryIndex() + value;
		getDataModel().setCurrentHistoryIndex(Math.min( Math.max(0, newValue ), maxValue));
		setCurrentCommand();
	}
	protected void setCurrentCommand(){
		if(CollectionUtils.isNotEmpty(getDataModel().getCommandHistory())){
			getDataModel().setCommand(getDataModel().getCommandHistory().get(getDataModel().getCurrentHistoryIndex()));
		}
	}

	protected void historizeCommand(String command){
		getDataModel().getCommandHistory().addFirst(command);
	}
	protected void resetHistoryIndex(){
		getDataModel().setCurrentHistoryIndex(-1);
	}
	public void clearConsole() {
		textDisplay.setText(StringUtils.EMPTY);
	}


	public void destroy() {
		if(connectionService != null){
			try {
				connectionService.removeInputDataListener(this);
				connectionService.removeOutputDataListener(this);
			} catch (GkException e) {
				LOG.error(e);
			}
		}
	}


}
