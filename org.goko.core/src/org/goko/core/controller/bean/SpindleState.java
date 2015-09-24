/**
 * 
 */
package org.goko.core.controller.bean;

/**
 * @author PsyKo
 *
 */
public class SpindleState {
	public static final SpindleState UNDEFINED 		= new SpindleState(0,"Undefined");
	public static final SpindleState OFF			= new SpindleState(1,"OFF");
	public static final SpindleState ON_CW 			= new SpindleState(2,"ON (CW)");
	public static final SpindleState ON_CCW 		= new SpindleState(4,"ON (CCW)");
	
	/** The code of the state */
	private int code;
	/** Label of the state */ // TODO : make it work for I18N
	private String label;

	public SpindleState(int code, String label){
		this.code = code;
		this.label = label;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		return result;
	}

	/** (inheritDoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SpindleState other = (SpindleState) obj;
		if (code != other.code) {
			return false;
		}
		return true;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
