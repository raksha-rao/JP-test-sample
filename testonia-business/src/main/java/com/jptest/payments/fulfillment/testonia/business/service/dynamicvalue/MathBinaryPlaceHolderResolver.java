package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.MathBinaryPlaceHolder;

/**
 * Resolves placeholder string starting with "${MATH-BINARY:"
 * 
 * @see MathBinaryPlaceHolder
 * @see CompositePlaceHolderResolver
 * @see CompositePlaceHolderResolverTest
 */
@Singleton
public class MathBinaryPlaceHolderResolver extends AbstractPlaceHolderResolver<MathBinaryPlaceHolder> {

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(MathBinaryPlaceHolder.MATH_BINARY_PLACEHOLDER_PREFIX);
	}

	@Override
	protected MathBinaryPlaceHolder getPlaceHolderInstance(String placeholder) {
		return new MathBinaryPlaceHolder(placeholder);
	}

	/**
	 * Performs math operations and stores result in MathPlaceHolder
	 * 
	 * @param context
	 * @param placeholder
	 */
    protected void setPlaceHolderValues(MathBinaryPlaceHolder placeholder) {
		placeholder.setValues();
	}

}
