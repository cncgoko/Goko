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
package org.goko.serial.jssc.console.internal;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.goko.common.bindings.AbstractController;
import org.goko.core.common.GkUtils;
import org.goko.core.common.exception.GkException;
import org.goko.core.connection.IConnectionDataListener;
import org.goko.core.connection.IConnectionService;
import org.goko.core.log.GkLog;

public class JsscSerialConsoleController extends AbstractController<JsscSerialConsoleModel> implements IConnectionDataListener {
	private static final GkLog LOG = GkLog.getLogger(JsscSerialConsoleController.class);
	@Inject
	IConnectionService connectionService;
	private StyledText textDisplay;

	public JsscSerialConsoleController() {
		super(new JsscSerialConsoleModel());

	}

	@Override
	public void initialize() throws GkException {
		// TODO Auto-generated method stub
		connectionService.addOutputDataListener(this);
		connectionService.addInputDataListener(this);

	}

	@Override
	public void onDataReceived(List<Byte> data) throws GkException {
//		if(getDataModel().isConsoleEnabled()){
//			final String text = GkUtils.toString(data);
//			Display.getDefault().asyncExec(new Runnable() {
//			    @Override
//				public void run() {
//			    	textDisplay.append(text);
//			    	if(!getDataModel().isScrollLock()){
//			    		textDisplay.setTopIndex(textDisplay.getLineCount() - 1);
//			    	}
//			    }
//			});
//		}
	}

	@Override
	public void onDataSent(List<Byte> data) throws GkException {
		if(getDataModel().isConsoleEnabled()){
			final String text = GkUtils.toString(data);
			Display.getDefault().asyncExec(new Runnable() {
			    @Override
				public void run() {
			    	textDisplay.append(text);
			    	if(!getDataModel().isScrollLock()){
			    		textDisplay.setTopIndex(textDisplay.getLineCount() - 1);
			    	}
			    }
			});
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
	public void sendCurrentCommand() {
		try {
			connectionService.send(getCompleteCommand());
			getDataModel().setCommand(StringUtils.EMPTY);
		} catch (GkException e) {
			LOG.error(e);
		}
	}

	public void clearConsole() {
		textDisplay.setText(StringUtils.EMPTY);
	}


}
