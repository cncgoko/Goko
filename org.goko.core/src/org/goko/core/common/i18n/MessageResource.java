package org.goko.core.common.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;

public class MessageResource {
	private static MessageResource _instance;	
	private List<ResourceBundle> lstResourceBundle;
	
	private MessageResource(){
		
		lstResourceBundle = new ArrayList<ResourceBundle>();		
	}
	
	public static MessageResource getInstance(){
		if(_instance == null){
			_instance = new MessageResource();
		}
		return _instance;
	}
	
	public static String getMessage(String key){
		return getInstance().getMessageIntern(key);
	}
	
	public String getMessageIntern(String key){
		if(CollectionUtils.isNotEmpty(lstResourceBundle)){
			for (ResourceBundle resourceBundle : lstResourceBundle) {
				if(resourceBundle.containsKey(key)){
					return resourceBundle.getString(key);
				}
			}
		}
		return key;
	}
	
	public static void registerResourceBundle(ResourceBundle resourceBundle){
		getInstance().registerResourceBundleIntern(resourceBundle);
	}
	
	public void registerResourceBundleIntern(ResourceBundle resourceBundle){
		lstResourceBundle.add(resourceBundle);
	}
}
