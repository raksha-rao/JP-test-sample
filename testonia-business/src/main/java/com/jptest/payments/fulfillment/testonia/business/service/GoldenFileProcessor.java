package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jptest.payments.fulfillment.testonia.business.service.dynamicvalue.CompositePlaceHolderResolver;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.model.dynamicvalue.PlaceHolder;

/**
 * Processes golden file document e.g. replaces placeholders in golden file
 */
@Singleton
public class GoldenFileProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GoldenFileProcessor.class);

	private static final String XPATH_EXPRESSION_TO_FIND_PLACEHOLDERS_IN_GOLDEN_FILE = "//*[starts-with(text(), '"
			+ PlaceHolder.PLACEHOLDER_PREFIX + "') and not(text()[2])]";

	@Inject
	private CompositePlaceHolderResolver compositePlaceHolderResolver;

	public void process(Context context, Document inputDocument) {
		// lets update golden file placeholders with dynamic values
		replacePlaceholdersInGoldenFile(context, inputDocument);
	}

	/**
	 * Replaces all placeholders (values starting with "${") with dynamic values
	 * from context in golden file
	 * 
	 * @param context
	 * @param inputDocument
	 */
	private void replacePlaceholdersInGoldenFile(Context context, Document inputDocument) {
		try {
			XPath xp = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xp.evaluate(XPATH_EXPRESSION_TO_FIND_PLACEHOLDERS_IN_GOLDEN_FILE, inputDocument,
					XPathConstants.NODESET);

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				String oldValue = node.getTextContent();
				String newValue = compositePlaceHolderResolver.resolvePlaceholder(context, oldValue);
				if (newValue == null) {
					// Don't replace the placeholder(s) if value not found in
					// map
					LOGGER.warn("Dynamic value {} not found in map {}", oldValue, context.getDynamicValues());
				} else {
					LOGGER.debug("Setting dynamic value for node {} {} {}", node.getNodeName(), oldValue, newValue);
					node.setTextContent(newValue);
				}
			}
		} catch (XPathExpressionException e) {
			LOGGER.error("Exception getting XPATH:", e);
		}
	}
}
