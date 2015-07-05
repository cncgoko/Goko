package org.goko.core.common.event;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.goko.core.common.exception.GkException;
import org.goko.core.common.exception.GkTechnicalException;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;

public class EventBrokerUtils {

	public static <T> void subscribe(IEventBroker broker, EventHandler eventHandler) throws GkException{
		broker.unsubscribe(eventHandler);
	}
	
	public static <T> void subscribe(IEventBroker broker, GkTopic<T> topic, EventHandler eventHandler) throws GkException{
		broker.subscribe(topic.getTopic(), eventHandler);
	}
	
	public static <T> void subscribe(IEventBroker broker, GkTopic<T> topic, String filter, EventHandler eventHandler, boolean headless) throws GkException{
		broker.subscribe(topic.getTopic(), filter, eventHandler, headless);
	}
	
	public static <T> void post(IEventBroker broker, GkTopic<T> topic, T data) throws GkException{
		broker.post(topic.getTopic(), data);
	}
	
	public static <T> void post(EventAdmin broker, GkTopic<T> topic, T data) throws GkException{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(IEventBroker.DATA, data);
		org.osgi.service.event.Event e = new org.osgi.service.event.Event(topic.getTopic(), properties);
		broker.postEvent(e);
	}
	
	public static <T> void send(IEventBroker broker, GkTopic<T> topic, T data) throws GkException{
		broker.send(topic.getTopic(), data);
	}
	
	public static <T> void send(EventAdmin broker, GkTopic<T> topic, T data) throws GkException{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(IEventBroker.DATA, data);
		org.osgi.service.event.Event e = new org.osgi.service.event.Event(topic.getTopic(), properties);
		broker.sendEvent(e);
	}
	
	public static <T> T getData(org.osgi.service.event.Event event, GkTopic<T> topic) throws GkException{		
		if( ! topic.getTopic().equals(event.getTopic())){
			throw new GkTechnicalException("Topic doesn't match");
		}
		Object obj = event.getProperty(IEventBroker.DATA);
		if(obj != null){
			if(topic.getDataClass().isAssignableFrom(obj.getClass())){
				return (T) obj; 
			}else{
				throw new GkTechnicalException("Object is not expected type. Expected '"+topic.getDataClass()+"', got '"+obj.getClass()+"'");
			}
		}
		return null;
	}
}
