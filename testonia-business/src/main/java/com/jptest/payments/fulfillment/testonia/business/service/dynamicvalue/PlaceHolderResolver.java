package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.core.Context;

/**
 * Golden file contains "dynamic values"/"placeholders" for expected data that
 * changes frequently. This resolver interface helps in resolving such
 * placeholders.
 * <p>
 * Placeholder is of format ${PLACEHOLDER-NAME} in golden file
 * 
 * @see com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder
 * 
 */
public interface PlaceHolderResolver {

	boolean shouldResolve(String placeholder);

	String resolvePlaceholder(Context context, String placeholder);
}
