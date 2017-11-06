package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.List;

import javax.inject.Inject;
import org.testng.Assert;

import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.business.service.PaymentExtensionHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;

/**
 * Validates payment extensions in PRS response
 */
public class PaymentExtensionAsserter extends BaseAsserter {

    @Inject
    private PaymentExtensionHelper paymentExtensionHelper;

    private List<String> voClassNameList;
    private boolean assertValue = true;

    public PaymentExtensionAsserter(List<String> voClassNameList) {
        this.voClassNameList = voClassNameList;
    }
    
    public PaymentExtensionAsserter(List<String> voClassNameList, Boolean assertValue) {
        this.voClassNameList = voClassNameList;
        this.assertValue = assertValue;
    }

    @Override
    public void validate(Context context) {
        GetLegacyEquivalentByPaymentReferenceResponse prsResponse = (GetLegacyEquivalentByPaymentReferenceResponse) getDataFromContext(
                context, ContextKeys.PRS_RESPONSE_KEY.getName());
        
        for (String voClassName : voClassNameList) {
        	Assert.assertEquals(assertValue, paymentExtensionHelper.containsPaymentExtension(prsResponse, voClassName)
        			, this.getClass().getSimpleName() + ".validate() failed " + voClassName + " should be present in payment extensions");
        }
    }


}
