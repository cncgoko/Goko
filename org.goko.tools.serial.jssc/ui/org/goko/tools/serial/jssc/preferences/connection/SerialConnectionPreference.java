package org.goko.tools.serial.jssc.preferences.connection;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.goko.core.config.GkPreference;
import org.goko.core.log.GkLog;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilter;
import org.goko.tools.serial.jssc.console.internal.JsscConsoleFilterType;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class SerialConnectionPreference extends GkPreference {
	private static final GkLog LOG = GkLog.getLogger(SerialConnectionPreference.class);
	/** Node name */
	public static final String PREFERENCE_NODE = "org.goko.tools.serial.jssc";
	/** Key for console preferences - Filters */
	public static final String KEY_FILTERS = "console.filters";

	private static SerialConnectionPreference _instance;

	public SerialConnectionPreference() {
		super(PREFERENCE_NODE);
	}

	/**
	 * Returns the Preference Store
	 * 
	 * @return {@link IPreferenceStore}
	 */
	public static SerialConnectionPreference getInstance() {
		if (_instance == null) {
			_instance = new SerialConnectionPreference();
		}
		return _instance;
	}

	public void setFilters(List<JsscConsoleFilter> lstFilters) {
		JsonArray filters = new JsonArray();
		
		for (JsscConsoleFilter filter : lstFilters) {
			filters.add(new JsonObject().add("description", filter.getDescription())
										.add("pattern", filter.getRegex())
										.add("enabled", filter.isEnabled())
										.add("type", filter.getType().getValue()));
		}	
		
		setValue(KEY_FILTERS, filters.toString());		
	}
	
	public List<JsscConsoleFilter> getFilters() {
		List<JsscConsoleFilter> result = new ArrayList<JsscConsoleFilter>();
		String filters = getString(KEY_FILTERS);
		if (StringUtils.isNotBlank(filters)) {
			JsonArray filtersArray = JsonArray.readFrom(filters);
			for(int i = 0; i < filtersArray.size(); i++){
				JsonObject jsonFilter = filtersArray.get(i).asObject();
				result.add(new JsscConsoleFilter(jsonFilter.get("enabled").asBoolean(),
												 jsonFilter.get("pattern").asString(),
												 jsonFilter.get("description").asString(),
												 JsscConsoleFilterType.get( jsonFilter.get("type").asInt()) ));
			}
		}
		return result;
	}
}
