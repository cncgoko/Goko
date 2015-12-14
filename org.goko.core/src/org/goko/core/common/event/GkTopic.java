package org.goko.core.common.event;

public class GkTopic<T> {
	private String topic;
	private Class<T> dataClass;
	
	protected GkTopic(String topic, Class<T> dataClass) {
		super();
		this.topic = topic;
		this.dataClass = dataClass;
	}
	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
	/**
	 * @return the dataClass
	 */
	public Class<T> getDataClass() {
		return dataClass;
	}
	/**
	 * @param dataClass the dataClass to set
	 */
	public void setDataClass(Class<T> dataClass) {
		this.dataClass = dataClass;
	}
}
