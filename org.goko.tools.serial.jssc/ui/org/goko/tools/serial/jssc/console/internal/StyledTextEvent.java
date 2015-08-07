package org.goko.tools.serial.jssc.console.internal;

import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Event;

/**
 *
 */
class StyledTextEvent extends Event {
	// used by LineStyleEvent
	int[] ranges;
	StyleRange[] styles;
	int alignment;
	int indent;
	int wrapIndent;
	boolean justify;
	Bullet bullet;
	int bulletIndex;
	int[] tabStops;
	// used by LineBackgroundEvent
	Color lineBackground;
	// used by TextChangedEvent
	int replaceCharCount;
	int newCharCount;
	int replaceLineCount;
	int newLineCount;
	// used by PaintObjectEvent
	int x;
	int y;
	int ascent;
	int descent;
	StyleRange style;

StyledTextEvent (StyledTextContent content) {
	super();
	data = content;
}
}