package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.convertJsonStringToObject;
import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.interfaces.rs.exception.OfferException;
import com.jptest.interfaces.rs.resources.OfferProgramVO;
import com.jptest.interfaces.rs.resources.OfferVO;
import com.jptest.payments.fulfillment.testonia.bridge.resource.OfferServResource;
import com.jptest.payments.fulfillment.testonia.bridge.util.SecurityContextHelper;


/**
 * Represents bridge for Offer Serv API calls
 */
@Singleton
public class OfferServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(OfferServBridge.class);

    private static final String ACCOUNT_NUMBER = "0";
    private static final List<String> SECURITY_CONTEXT_SCOPES = Collections.singletonList("*");

    private String securityContext;

    @Inject
    @Named("offerservresource")
    private OfferServResource offerServResource;

    public OfferServBridge() {
        securityContext = SecurityContextHelper
                .getSecurityContext(ACCOUNT_NUMBER, SECURITY_CONTEXT_SCOPES);
    }

    /**
     * Create Offer Program
     *
     * @param offerProgramVO
     * @return
     * @throws OfferException
     */
    public OfferProgramVO createOfferProgram(OfferProgramVO offerProgramVO) throws OfferException {
        LOGGER.info("Create Offer Program Request: {}", printJsonObject(offerProgramVO));
        Response response = offerServResource.createOfferProgram(securityContext, offerProgramVO);

        String jsonResponse = response.readEntity(String.class);
        OfferProgramVO offerProgramResponse = convertJsonStringToObject(jsonResponse, OfferProgramVO.class);

        LOGGER.info("Create Offer Program Response: {}", printJsonObject(offerProgramResponse));
        logError(response, jsonResponse);
        return offerProgramResponse;
    }

    /**
     * Create Offer
     *
     * @param offerVO
     * @return
     * @throws OfferException
     */
    public OfferVO createOffer(OfferVO offerVO) throws OfferException {
        LOGGER.info("Create Offer Request: {}", printJsonObject(offerVO));
        Response response = offerServResource.createOffer(securityContext, offerVO);

        String jsonResponse = response.readEntity(String.class);
        OfferVO offerResponse = convertJsonStringToObject(jsonResponse, OfferVO.class);

        LOGGER.info("Create Offer Response: {}", printJsonObject(offerResponse));
        logError(response, jsonResponse);
        return offerResponse;
    }

    /**
     * Get Offer details using offer_id
     *
     * @param offerId
     * @return
     * @throws OfferException
     */
    public OfferVO getOfferById(String offerId) throws OfferException {
        LOGGER.info("Get Offer using Offer Id - ", offerId);
        Response response = offerServResource.getOfferById(securityContext, offerId);

        String jsonResponse = response.readEntity(String.class);
        OfferVO offerResponse = convertJsonStringToObject(jsonResponse, OfferVO.class);

        LOGGER.info("Offer Response: {}", printJsonObject(offerResponse));
        logError(response, jsonResponse);
        return offerResponse;
    }

    /**
     * If the response status is NOT successful, log the error message.
     *
     * @param response
     * @param jsonResponse
     */
    private void logError(Response response, String jsonResponse) {
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            LOGGER.error(jsonResponse.isEmpty() ? response.getStatusInfo().getFamily().name() : jsonResponse);
        }
    }
}
