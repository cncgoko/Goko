package org.goko.tools.serial.jssc.console.internal;


import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TypedListener;

class StyledTextListener extends TypedListener {
	/**
 */
	StyledTextListener(SWTEventListener listener) {
		super(listener);
	}

	/**
	 * Process StyledText events by invoking the event's handler.
	 *
	 * @param e
	 *            the event to handle
	 */
	@Override
	public void handleEvent(Event e) {

		switch (e.type) {
		case 0xbb8:
			GkExtendedModifyEvent extendedModifyEvent = new GkExtendedModifyEvent((StyledTextEvent) e);
			((ExtendedModifyListener) eventListener) .modifyText(extendedModifyEvent);
			break;
		}
	}
}
