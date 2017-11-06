package com.jptest.payments.fulfillment.testonia.model.dynamicvalue;

import java.util.Arrays;
import java.util.List;

/**
 * Placeholder for Math related dynamic values e.g. ${MATH-BINARY:SUBTRACT-INT(1000,
 * 2000)} - will perform 1000 - 2000
 * 
 * <p> 
 * The values map will contain entries such as:
 * <li>${MATH-BINARY:SUBTRACT-INT(2000, 1000)} : 1000</li>
 * <li>${MATH-BINARY:ADD-INT(2000, 1000)} : 3000</li>
 */
public class MathBinaryPlaceHolder extends PlaceHolder {

	// placeholder prefix is "${MATH-BINARY:"
	public static final String FUNCTION_NAME = "MATH-BINARY";
	public static final String MATH_BINARY_PLACEHOLDER_PREFIX = PLACEHOLDER_PREFIX + FUNCTION_NAME + FIELD_DELIMITER;

	public static final String SUBTRACT_INT_OPERATION = "SUBTRACT-INT";
	public static final String ADD_INT_OPERATION = "ADD-INT";

	private static final List<String> SUPPORTED_OPERATIONS = Arrays.asList(SUBTRACT_INT_OPERATION, ADD_INT_OPERATION);

	private String number1;
	private String number2;

	public MathBinaryPlaceHolder(String placeholder) {
		super(placeholder);
		String operation = placeholder
				.substring(placeholder.indexOf(FIELD_DELIMITER) + 1, placeholder.indexOf(FUNCTION_PREFIX)).trim();
		this.number1 = getParams().get(0).trim();
		this.number2 = getParams().get(1).trim();
		if (!SUPPORTED_OPERATIONS.contains(operation)) {
			throw new IllegalStateException(
					operation + " is not supported. " + "Supported operations:" + SUPPORTED_OPERATIONS);
		}
	}

	public String getNumber1() {
		return number1;
	}

	public String getNumber2() {
		return number2;
	}

	@Override
	protected String buildKey(String fieldName) {
		return MATH_BINARY_PLACEHOLDER_PREFIX + fieldName + FUNCTION_PREFIX + getParameters() + FUNCTION_SUFFIX
				+ PLACEHOLDER_SUFFIX;
	}
	
	private String getParameters() {
		return String.join(PARAMETER_DELIMITER, number1, number2);
	}

	/**
	 * Set output of Math binary operations (requiring 2 operands) in map
	 */
	public void setValues() {
		Integer number1 = Integer.parseInt(this.number1);
		Integer number2 = Integer.parseInt(this.number2);
		addKeyValue(SUBTRACT_INT_OPERATION, String.valueOf(number1 - number2));
		addKeyValue(ADD_INT_OPERATION, String.valueOf(number1 + number2));
	}
}
