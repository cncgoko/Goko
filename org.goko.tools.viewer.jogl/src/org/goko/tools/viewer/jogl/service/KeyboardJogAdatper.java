/*
 *	This file is part of Goko.
 *
 *  Goko is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Goko is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Goko.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.goko.tools.viewer.jogl.service;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.goko.core.common.exception.GkException;
import org.goko.core.controller.IContinuousJogService;
import org.goko.core.controller.bean.EnumControllerAxis;
import org.goko.tools.viewer.jogl.GokoJoglCanvas;

public class KeyboardJogAdatper extends KeyAdapter implements KeyListener{
	private IContinuousJogService jogService;
	private GokoJoglCanvas canvas;
	private Map<Integer, EnumControllerAxis> mapAxisByKey;

	public KeyboardJogAdatper(GokoJoglCanvas canvas, IContinuousJogService jogService) {
		super();
		this.jogService = jogService;
		this.canvas = canvas;
		mapAxisByKey = new HashMap<Integer, EnumControllerAxis>();
		mapAxisByKey.put(SWT.ARROW_LEFT, 	EnumControllerAxis.X_NEGATIVE);
		mapAxisByKey.put(SWT.ARROW_RIGHT, 	EnumControllerAxis.X_POSITIVE);
		mapAxisByKey.put(SWT.ARROW_DOWN, 	EnumControllerAxis.Y_NEGATIVE);
		mapAxisByKey.put(SWT.ARROW_UP, 		EnumControllerAxis.Y_POSITIVE);
		mapAxisByKey.put(SWT.PAGE_DOWN, 	EnumControllerAxis.Z_NEGATIVE);
		mapAxisByKey.put(SWT.PAGE_UP, 	EnumControllerAxis.Z_POSITIVE);
	}


	@Override
	public void keyPressed(KeyEvent event) {
		if(!canvas.isKeyboardJogEnabled()){
			return;
		}
		if(mapAxisByKey.containsKey(event.keyCode)){
			try {
				triggerJog(mapAxisByKey.get(event.keyCode));
			} catch (GkException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event){
		if(!canvas.isKeyboardJogEnabled()){
			return;
		}
		if(mapAxisByKey.containsKey(event.keyCode)){
			try {
				stopJog();
			} catch (GkException e) {
				e.printStackTrace();
			}
		}
	}

	public void triggerJog(EnumControllerAxis axis) throws GkException{
		jogService.startJog(axis, null);
	}

	public void stopJog() throws GkException{
		jogService.stopJog();
	}
}
