package org.goko.tools.serial.jssc.console.internal;

import org.eclipse.swt.events.TypedEvent;

/**
 * This event is sent after a text change occurs.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class GkExtendedModifyEvent extends TypedEvent {
	/** start offset of the new text */
	public int start;
	/** length of the new text */
	public int length;
	/** replaced text or empty string if no text was replaced */
	public String replacedText;

	static final long serialVersionUID = 3258696507027830832L;

/**
 * Constructs a new instance of this class based on the
 * information in the given event.
 *
 * @param e the event containing the information
 */
public GkExtendedModifyEvent(StyledTextEvent e) {
	super(e);
	start = e.start;
	length = e.end - e.start;
	replacedText = e.text;
}
}
