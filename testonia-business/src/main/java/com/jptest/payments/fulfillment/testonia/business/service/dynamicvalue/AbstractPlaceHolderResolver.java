package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * Parent class of different PlaceHolderResolver implementations.
 * <p>
 * It expects all implementations to:
 * <li>return an instance of PlaceHolder</li>
 * <li>make external calls to retrieve necessary data and set these data in PlaceHolder instance</li>
 * <p>
 * @param <T>
 */
public abstract class AbstractPlaceHolderResolver<T extends PlaceHolder> implements PlaceHolderResolver {

	@Override
	public String resolvePlaceholder(Context context, String placeholderString) {
		T placeHolder = getPlaceHolderInstance(placeholderString);
		setDynamicValues(context, placeHolder);
		return placeHolder.getValue(placeholderString);
	}

	protected abstract T getPlaceHolderInstance(String placeholderString);

	protected void setDynamicValues(Context context, T placeHolder) {
        setPlaceHolderValues(placeHolder);
		// Put result in context's dynamic value map so that the map can later
		// be used.
		context.getDynamicValues().putAll(placeHolder.getAllValues());
	}

    protected abstract void setPlaceHolderValues(T placeHolder);
}
