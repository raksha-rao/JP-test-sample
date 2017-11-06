package com.jptest.payments.fulfillment.testonia.core.guice.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClientRequest/Response Http header(s) logger
 * 
 * @JP Inc.
 * @see CorrelationIdHeaderLoggerTest
 */
public class CorrelationIdHeaderLogger implements ClientResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(CorrelationIdHeaderLogger.class);

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {

        MultivaluedMap<String, String> responseHeaders = responseContext.getHeaders();
        for (Entry<String, List<String>> entry : responseHeaders.entrySet()) {
            String key = entry.getKey();
            if ("corr_id".equals(key)) {
                List<String> value = entry.getValue();
                if (CollectionUtils.isNotEmpty(value)) {
                    String corrId = value.get(0);
                    logger.info(
                            "http://mscal.qa.jptest.com/cgi/idsearch_manager.py?id_type=corr_id&id_value={}&fetchlog=0&submit=Search",
                            corrId);
                }

            }
        }

    }

}
