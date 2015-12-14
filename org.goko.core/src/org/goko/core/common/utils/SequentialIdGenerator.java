package org.goko.core.common.utils;

public class SequentialIdGenerator implements IIdGenerator{
	private int currentValue = 0;

	public void reset(){
		currentValue = 0;
	}
	
	public Integer getNextValue(){
		return ++currentValue;
	}
}
