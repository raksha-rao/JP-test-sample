package com.jptest.payments.fulfillment.testonia.business.component.validation;

import com.jptest.common.ActorInfoVO;
import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.FulfillPaymentRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceListRequest;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceListResponse;
import com.jptest.payments.LegacyEquivalentInputDetailsVO;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.PaymentSideReferenceVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentReadServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
 * Tasks gets a list of LegacyEquivalentByPaymentReferences. This one is very usefull if we have multipple activities
 * with the same PayId.
 * This task is mainly used to test mayFly TFS MSP functionality.
 */
public class PaymentReadServListRetrieverTask extends BaseTask<GetLegacyEquivalentByPaymentReferenceListResponse> {

    @Inject
    private PaymentReadServBridge plsBridge;

    private final int numberOfActivities;

    public PaymentReadServListRetrieverTask(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    @Override
    public GetLegacyEquivalentByPaymentReferenceListResponse process(Context context) {

        GetLegacyEquivalentByPaymentReferenceListRequest request = getLegacyEquivalentByPaymentReferenceRequest(
                context);
        GetLegacyEquivalentByPaymentReferenceListResponse response = plsBridge
                .getLegacyEquivalentByPaymentReferenceListRequest(request);

        Assert.assertTrue(CollectionUtils.isNotEmpty(response.getLegacyEquivalentResponses()));
        Assert.assertEquals(numberOfActivities, response.getLegacyEquivalentResponses().size(), 
        		this.getClass().getSimpleName() + ".process() failed for activityCount");
        return response;
    }

    protected GetLegacyEquivalentByPaymentReferenceListRequest getLegacyEquivalentByPaymentReferenceRequest(
            final Context context) {

        final FulfillPaymentRequest fpRequest = (FulfillPaymentRequest) this.getDataFromContext(context,
                ContextKeys.FULFILL_PAYMENT_REQUEST_KEY.getName());

        List<AlternateIdentifierVO> alternateIds = fpRequest.getFulfillmentContext().getAlternateIds();
        String refId = null;
        for (AlternateIdentifierVO alternateId : alternateIds) {
            if(PaymentReferenceTypeCode.PAY_ID.equals(alternateId.getReferenceTypeAsEnum())){
                refId = alternateId.getIdentifier();
            }
        }

        if(refId == null){
            throw new IllegalStateException("Cannot find PayId");
        }

        final GetLegacyEquivalentByPaymentReferenceListRequest request = new GetLegacyEquivalentByPaymentReferenceListRequest();
        final List<LegacyEquivalentInputDetailsVO> inputList = new ArrayList<LegacyEquivalentInputDetailsVO>();
        final LegacyEquivalentInputDetailsVO input = new LegacyEquivalentInputDetailsVO();
        final ActorInfoVO actor = new ActorInfoVO();
        request.setActor(actor);
        final PaymentSideReferenceVO paymentSideReferenceVO = new PaymentSideReferenceVO();
        paymentSideReferenceVO.setReferenceType(PaymentReferenceTypeCode.PAY_ID);
        paymentSideReferenceVO.setReferenceValue(refId);
        input.setPaymentReference(paymentSideReferenceVO);
        input.setRequireRiskDecision(false);
        inputList.add(input);
        request.setInputDetails(inputList);
        return request;

    }
}
