package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.MathUnaryPlaceHolder;

import javax.inject.Singleton;

/**
 * Resolves placeholder string starting with "${MATH-UNARY:"
 * 
 * @see MathUnaryPlaceHolder
 * @see CompositePlaceHolderResolver
 * @see //CompositePlaceHolderResolverTest
 */
@Singleton
public class MathUnaryPlaceHolderResolver extends AbstractPlaceHolderResolver<MathUnaryPlaceHolder> {

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(MathUnaryPlaceHolder.MATH_UNARY_PLACEHOLDER_PREFIX);
	}

	@Override
	protected MathUnaryPlaceHolder getPlaceHolderInstance(String placeholder) {
		return new MathUnaryPlaceHolder(placeholder);
	}

	/**
	 * Performs math operations and stores result in MathPlaceHolder
	 * 
	 * @param //context
	 * @param placeholder
	 */
    public void setPlaceHolderValues(MathUnaryPlaceHolder placeholder) {
		placeholder.setValues();
	}

}
