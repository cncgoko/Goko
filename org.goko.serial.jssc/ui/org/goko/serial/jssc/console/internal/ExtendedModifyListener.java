package org.goko.serial.jssc.console.internal;

import org.eclipse.swt.internal.SWTEventListener;

/**
 * Classes which implement this interface provide a method
 * that deals with the event that is generated when text
 * is modified.
 *
 * @see GkExtendedModifyEvent
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public interface ExtendedModifyListener extends SWTEventListener {

/**
 * This method is called after a text change occurs.
 * <p>
 * The following event fields are used:<ul>
 * <li>event.start the start offset of the new text (input)</li>
 * <li>event.length the length of the new text (input)</li>
 * <li>event.replacedText the replaced text (input)</li>
 * </ul>
 *
 * @param event the given event
 * @see GkExtendedModifyEvent
 */
public void modifyText(GkExtendedModifyEvent event);
}
