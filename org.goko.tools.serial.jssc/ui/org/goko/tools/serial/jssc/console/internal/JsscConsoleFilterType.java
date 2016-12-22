/**
 * 
 */
package org.goko.tools.serial.jssc.console.internal;

/**
 * @author Psyko
 * @date 21 déc. 2016
 */
public enum JsscConsoleFilterType {
	INPUT(1, "Input"),
	OUTPUT(2, "Output"),
	BOTH(3, "Input and output");
	
	int value;
	String label;
	
	/**
	 * @param value
	 */
	private JsscConsoleFilterType(int value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public static JsscConsoleFilterType get(int value){
		for (JsscConsoleFilterType enumValue : JsscConsoleFilterType.values()) {
			if(value == enumValue.getValue()){
				return enumValue;
			}
		}
		return null;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

}
