package org.goko.common.preferences.fieldeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public abstract class LabeledFieldEditor<C extends Control> extends FieldEditor<C> {
    /**
     * Text limit constant (value <code>-1</code>) indicating unlimited
     * text limit and width.
     */
    public static int UNLIMITED = -1;
	/** Control for label display */
	protected Label labelControl;
	/** Label width in chars */
	private int labelWidthInChar = UNLIMITED;
	
	/**
	 * Constructor
	 * @param parent parent
	 * @param style style
	 */
	public LabeledFieldEditor(Composite parent, int style) {
		super(parent, style);		
	}

	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#createControls(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected void createControls(Composite parent, int style) {		
		super.createControls(parent, style);
		labelControl = new Label(this, style);    	
    	labelControl.setText(getLabel());
    	labelControl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));		
		labelControl.pack();
	}
	
	
	/** (inheritDoc)
	 * @see org.goko.common.preferences.fieldeditor.FieldEditor#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {		
		super.setLabel(label);
		labelControl.setText(label);
		labelControl.pack();
		pack();
	}
	
	private void updateLabelLayout(){
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);		
		if (labelWidthInChar != UNLIMITED) {
            GC gc = new GC(control);
            try {
                Point extent = gc.textExtent("X");//$NON-NLS-1$
                gd.widthHint = labelWidthInChar * extent.x;
            } finally {
                gc.dispose();
            }
        } 
		labelControl.setLayoutData(gd);
		pack();
	}

	/**
	 * @return the labelWidthInChar
	 */
	public int getLabelWidthInChar() {
		return labelWidthInChar;
	}

	/**
	 * @param labelWidthInChar the labelWidthInChar to set
	 */
	public void setLabelWidthInChar(int labelWidthInChar) {
		this.labelWidthInChar = labelWidthInChar;
		updateLabelLayout();
	}
}
