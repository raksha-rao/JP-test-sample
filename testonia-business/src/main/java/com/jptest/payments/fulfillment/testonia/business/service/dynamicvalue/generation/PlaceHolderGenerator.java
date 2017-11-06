package com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.generation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.GoldenFilePlaceHolderDTO;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.MathUnaryPlaceHolder;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * Parent class for different PlaceHolderGenerator implementations e.g.
 * {@link FeePlaceHolderGenerator}, {@link FXPlaceHolderGenerator}
 * <p>
 * It exposes common methods used by child classes.
 */
public abstract class PlaceHolderGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaceHolderGenerator.class);

	private static final String GOLDEN_FILE_PLACEHOLDER_GENERATION_CONFIRM_REPLACEMENT_KEY = "goldenfile.placeholder.generation.confirm.replacement";

	@Inject
	private MathUnaryPlaceHolderGenerator mathUnaryPlaceHolderGenerator;

	@Inject
	private Configuration config;

	private boolean confirmReplacement;

	@Inject
	public void init() {
		this.confirmReplacement = config.getBoolean(GOLDEN_FILE_PLACEHOLDER_GENERATION_CONFIRM_REPLACEMENT_KEY);
	}

	protected void replaceValue(GoldenFilePlaceHolderDTO dto, Node node, String replaceValue, boolean forceReplace) {
		if (forceReplace || !confirmReplacement) {
			doReplaceValue(dto, node, replaceValue);
		} else {
			doReplaceValueAfterConfirmation(dto, node, replaceValue);
		}
	}

	private void doReplaceValueAfterConfirmation(GoldenFilePlaceHolderDTO dto, Node node, String replaceValue) {
		try {
            String input = getUserResponse("Ok to replace? (y/n)");
            if ("y".equalsIgnoreCase(input)) {
                doReplaceValue(dto, node, replaceValue);
            }
		} catch (IOException e) {
			LOGGER.error("Error reading user's response", e);
		}
	}

	private void doReplaceValue(GoldenFilePlaceHolderDTO dto, Node node, String replaceValue) {
		node.setTextContent(replaceValue);
		dto.setChanged(true);
	}

	protected static String getNodeValue(Node itemNode, String fieldName) {
		return getChildNode(itemNode, fieldName).getTextContent();
	}

	protected static Node getChildNode(Node itemNode, String fieldName) {
		return ((Element) itemNode).getElementsByTagName(fieldName).item(0);
	}

	public String[] getNegatedFieldAndValue(GoldenFilePlaceHolderDTO dto, PlaceHolder placeHolder, String fieldKey) {

		String fieldValue = placeHolder.getValue(fieldKey);
		MathUnaryPlaceHolder mathUnaryPlaceHolder = mathUnaryPlaceHolderGenerator.getMathUnaryPlaceHolder(dto,
				fieldValue);
		String negatedFieldKey = mathUnaryPlaceHolder.buildKey(MathUnaryPlaceHolder.NEGATE_INT_OPERATION);
		String nagatedFieldValue = mathUnaryPlaceHolder.getValue(negatedFieldKey);

		return new String[] { negatedFieldKey.replace(fieldValue, fieldKey), nagatedFieldValue };
	}

    protected String getUserResponse(String messageToUser) throws IOException {
        BufferedReader inputRead = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        System.out.println(messageToUser);
        return inputRead.readLine();
    }
}
