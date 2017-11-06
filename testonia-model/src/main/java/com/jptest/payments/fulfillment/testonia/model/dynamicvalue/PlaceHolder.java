package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Placeholder string should be of below format: ${FUNCTION:FIELD(PARAMETER1,
 * PARAMETER2...)}
 * <p>
 * Placeholder holds "parameters" and "values map".
 * 
 * @see FXPlaceHolder
 * @see FeePlaceHolder
 */
public abstract class PlaceHolder {

	public static final String PLACEHOLDER_PREFIX = "${";
	public static final String PLACEHOLDER_SUFFIX = "}";

	public static final String FUNCTION_PREFIX = "(";
	public static final String FUNCTION_SUFFIX = ")";

	public static final String FIELD_DELIMITER = ":";
	public static final String PARAMETER_DELIMITER = ",";

	private List<String> params;
	private Map<String, String> values;

	public PlaceHolder(String originalPlaceholder) {
		// parse the string and get parameters
		this.params = Arrays.asList(originalPlaceholder
				.substring(originalPlaceholder.indexOf(FUNCTION_PREFIX) + 1, originalPlaceholder.length() - 2)
				.split(PARAMETER_DELIMITER));
		this.values = new ConcurrentHashMap<>();
	}

	public List<String> getParams() {
		return params;
	}

	public String getValue(String placeholderString) {
		return values.get(placeholderString);
	}
	
	@Override
	public String toString() {
		return "PlaceHolder:[Params:" + params + ", Values:" + values.toString() + "]";
	}

	protected void addKeyValue(String fieldName, String value) {
		values.put(buildKey(fieldName), value);
	}
	
	protected abstract String buildKey(String fieldName);

	public Map<String, PlaceHolder> getAllValues() {
		Map<String, PlaceHolder> map = new HashMap<>();
		for (String key : values.keySet()) {
			map.put(key, this);
		}
		return map;
	}

	

}
