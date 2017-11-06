package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import java.util.Arrays;
import java.util.List;

/**
 * Placeholder for Math related dynamic values e.g.
 * ${MATH-UNARY:NEGATE-INT(1000)} - will return -1000
 * 
 * <p>
 * The values map will contain entries such as:
 * <li>${MATH-UNARY:NEGATE-INT(1000)} : -1000</li>
 */
public class MathUnaryPlaceHolder extends PlaceHolder {

	// placeholder prefix is "${MATH-UNARY:"
	public static final String FUNCTION_NAME = "MATH-UNARY";
	public static final String MATH_UNARY_PLACEHOLDER_PREFIX = PLACEHOLDER_PREFIX + FUNCTION_NAME + FIELD_DELIMITER;

	public static final String NEGATE_INT_OPERATION = "NEGATE-INT";

	private static final List<String> SUPPORTED_OPERATIONS = Arrays.asList(NEGATE_INT_OPERATION);

	private String number;

	public MathUnaryPlaceHolder(String placeholder) {
		super(placeholder);
		String operation = placeholder
				.substring(placeholder.indexOf(FIELD_DELIMITER) + 1, placeholder.indexOf(FUNCTION_PREFIX)).trim();
		this.number = getParams().get(0).trim();
		if (!SUPPORTED_OPERATIONS.contains(operation)) {
			throw new IllegalStateException(
					operation + " is not supported. " + "Supported operations:" + SUPPORTED_OPERATIONS);
		}
	}

	public String getNumber() {
		return number;
	}

	@Override
	public String buildKey(String fieldName) {
		return MATH_UNARY_PLACEHOLDER_PREFIX + fieldName + FUNCTION_PREFIX + getParameters() + FUNCTION_SUFFIX
				+ PLACEHOLDER_SUFFIX;
	}

	private String getParameters() {
		return number;
	}

	/**
	 * Set output of Math unary operations (requiring 1 operand) in map
	 */
	public void setValues() {
		Integer number = Integer.parseInt(this.number);
		addKeyValue(NEGATE_INT_OPERATION, String.valueOf(-number));
	}
}
