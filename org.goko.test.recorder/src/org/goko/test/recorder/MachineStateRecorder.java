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
package org.goko.test.recorder;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.annotation.PostConstruct;
import javax.vecmath.Point3d;

import org.apache.commons.lang3.StringUtils;
import org.goko.core.common.event.EventListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.service.IGokoService;
import org.goko.core.controller.IControllerService;
import org.goko.core.controller.bean.DefaultControllerValues;
import org.goko.core.controller.event.MachineValueUpdateEvent;
import org.goko.core.log.GkLog;

public class MachineStateRecorder implements IGokoService{
	private static final GkLog LOG = GkLog.getLogger(MachineStateRecorder.class);
	private IControllerService controller;
	private Writer writer = null;
	private String filepath ="C:/Users/PsyKo/Documents/test.csv";
	/**
	 * @return the controller
	 */
	public IControllerService getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(IControllerService controller) {
		this.controller = controller;
		controller.addListener(this);
	}
	public void unsetController() {
		this.controller = null;
	}

	@Override
	public String getServiceId() throws GkException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PostConstruct
	public void start() throws GkException {

	}

	@Override
	public void stop() throws GkException {
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@EventListener(MachineValueUpdateEvent.class)
	public void onMachineValueUpdate(MachineValueUpdateEvent updateEvent){
		if(StringUtils.equals(updateEvent.getTarget().getIdDescriptor(), DefaultControllerValues.POSITION)){
			Point3d pos = (Point3d) updateEvent.getTarget().getValue();
			String str = System.currentTimeMillis()+";"+pos.x+";"+pos.y+";"+pos.z;
			appendToFile(str);
		}
	}

	private void appendToFile(String stringValue) {
		try {
			getWriter().write(stringValue);
			getWriter().write(System.lineSeparator());
			getWriter().flush();
		} catch (IOException | GkException e) {
			e.printStackTrace();
		}
	}

	protected Writer getWriter() throws GkException{
		if(writer == null){
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
				          new FileOutputStream(filepath), "utf-8"));
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return writer;
	}
}
