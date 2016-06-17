package org.goko.tools.editor;

import org.eclipse.jface.text.source.Annotation;

public class BulletAnnotation extends Annotation {

	public static final String TYPE = "org.eclipse.mylyn.internal.wikitext.ui.viewer.annotation.bullet";

	private final int indentLevel;

	public BulletAnnotation(int indentLevel) {
		super(TYPE, false, Integer.toString(indentLevel));
		this.indentLevel = indentLevel;
	}

	public int getIndentLevel() {
		return indentLevel;
	}

}