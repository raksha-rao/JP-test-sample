package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * Default implementation of PlaceHolderResolver. It iteratively resolves all
 * the placeholders.
 * <p>
 * It calls respective implementation (e.g. FXPlaceHolderResolver) - to resolve
 * the individual placeholder.
 * 
 * @see CompositePlaceHolderResolverTest
 */
@Singleton
public class CompositePlaceHolderResolver implements PlaceHolderResolver {

	@Inject
	private FXPlaceHolderResolver fxResolver;
	
	@Inject
	private FXRefundPlaceHolderResolver fxRefundResolver;

    @Inject
    private FXInterbankPlaceHolderResolver fxInterbankResolver;

	@Inject
	private FeePlaceHolderResolver feeResolver;
	
	@Inject
	private MathUnaryPlaceHolderResolver mathUnaryResolver;

	@Inject
	private MathBinaryPlaceHolderResolver mathBinaryResolver;

	@Override
	public boolean shouldResolve(String placeholder) {
		return placeholder.startsWith(PlaceHolder.PLACEHOLDER_PREFIX);
	}

	/**
	 * Resolves placeholders recursively e.g.
	 * 
	 * If map contains below value map.put("${12}", "123"); map.put("${a}",
	 * "2");
	 * 
	 * It will resolve "${1${a}}" to "123"
	 * 
	 * @param map
	 * @param inputKey
	 * @return
	 */
	public String resolvePlaceholder(Context context, String placeholder) {
		// replace spaces from placeholder string
		placeholder = placeholder.replaceAll("\\s", "");
		// If map contains the placeholder, lets get it from it
		if (context.getDynamicValues().containsKey(placeholder)) {
			return context.getDynamicValues().get(placeholder).getValue(placeholder);
		}

		// Lets resolve placeholder(s) recursively to handle use case such as
		// ${FX:AMOUNT-TO(USD,${FEE(USD,1000)},GBP,UK,)}
		// It will resolve ${FEE(USD,1000)} to (say) 926 and then
		// will resolve ${FX:AMOUNT-TO(USD,926,GBP)}
		while (placeholder.lastIndexOf(PlaceHolder.PLACEHOLDER_PREFIX) != 0) {
			int lastIndexStart = placeholder.lastIndexOf(PlaceHolder.PLACEHOLDER_PREFIX);
			int lastIndexEnd = placeholder.indexOf(PlaceHolder.PLACEHOLDER_SUFFIX, lastIndexStart);
			if (lastIndexStart >= lastIndexEnd) {
				throw new IllegalStateException("Invalid dynamic value found:" + placeholder);
			}

			String substring = placeholder.substring(lastIndexStart, lastIndexEnd + 1);
			String value = resolvePlaceholder(context, substring);

			placeholder = placeholder.replace(substring, value);
		}
		// Not found in map and input does not have any token
		// So lets use resolver to resolve the placeholder
		return findResolver(placeholder).resolvePlaceholder(context, placeholder);
	}

	protected PlaceHolderResolver findResolver(String placeholder) {
		if (fxResolver.shouldResolve(placeholder)) {
			return fxResolver;
		} else if (fxRefundResolver.shouldResolve(placeholder)) {
			return fxRefundResolver;
        }
        else if (fxInterbankResolver.shouldResolve(placeholder)) {
            return fxInterbankResolver;
        }
        else if (feeResolver.shouldResolve(placeholder)) {
			return feeResolver;
		} else if (mathUnaryResolver.shouldResolve(placeholder)) {
			return mathUnaryResolver;
		} else if (mathBinaryResolver.shouldResolve(placeholder)) {
			return mathBinaryResolver;
		}
		throw new UnsupportedOperationException("No Resolver is supported for input " + placeholder);
	}

}
