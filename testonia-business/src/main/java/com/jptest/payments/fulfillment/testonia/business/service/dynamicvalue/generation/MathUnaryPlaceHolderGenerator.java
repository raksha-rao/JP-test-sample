package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.MathUnaryPlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.MathUnaryPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * This class helps in generating {@link MathUnaryPlaceHolder} from input JSON
 * file and sender/recipient golden file. It returns MathUnaryPlaceHolder
 * object.
 * <p>
 * <p>
 * It replaces below "fee_history" rows of type 'X' in sender and recipient
 * golden files with placeholder strings:
 * <li>${MATH-UNARY:NEGATE-INT(1000)}</li>
 */
@Singleton
public class MathUnaryPlaceHolderGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(MathUnaryPlaceHolderGenerator.class);

	@Inject
	private MathUnaryPlaceHolderResolver mathUnaryPlaceHolderResolver;

	public MathUnaryPlaceHolder getMathUnaryPlaceHolder(GoldenFilePlaceHolderDTO dto, String number) {
		// 1. create MathUnary placeholder from input
		MathUnaryPlaceHolder mathUnaryPlaceHolder = createMathUnaryPlaceHolder(number);
		if (mathUnaryPlaceHolder != null) {
			try {
				// 2. resolve MathUnary placeholder
                mathUnaryPlaceHolderResolver.setPlaceHolderValues(mathUnaryPlaceHolder);
			} catch (Exception e) {
				LOGGER.error("Error occurred getting value for placeholder {} in {}", mathUnaryPlaceHolder.buildKey(""),
						dto.getPath());
				throw e;
			}
		}
		return mathUnaryPlaceHolder;
	}

	/**
	 * Create placeholder string from input and return MathUnaryPlaceHolder
	 * object.
	 */
	private MathUnaryPlaceHolder createMathUnaryPlaceHolder(String number) {
		StringBuilder sb = new StringBuilder(MathUnaryPlaceHolder.MATH_UNARY_PLACEHOLDER_PREFIX
				+ MathUnaryPlaceHolder.NEGATE_INT_OPERATION + PlaceHolder.FUNCTION_PREFIX).append(number)
						.append(PlaceHolder.FUNCTION_SUFFIX).append(PlaceHolder.PLACEHOLDER_SUFFIX);
		return new MathUnaryPlaceHolder(sb.toString());
	}
}
