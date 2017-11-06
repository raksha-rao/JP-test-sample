package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.common.OpaqueDataElementVO;
import com.jptest.money.PaymentInternalOpaqueDataVO;
import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseAsserter;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.UniversalDeserializer;

/**
 * Validates payment extensions in PRS response
 */
public class PRSResponseInternalOpaqueDataElementAsserter extends BaseAsserter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PRSResponseInternalOpaqueDataElementAsserter.class);
	private static final String internalPaymentData = "Money::PaymentInternalOpaqueDataVO";
	private static final String riskDecisionData = "Money::RiskDecisionVO";
	
    public PRSResponseInternalOpaqueDataElementAsserter() {
    }

    @Override
    public void validate(Context context) {
        GetLegacyEquivalentByPaymentReferenceResponse prsResponse = (GetLegacyEquivalentByPaymentReferenceResponse) getDataFromContext(
                context, ContextKeys.PRS_RESPONSE_KEY.getName());
        	OpaqueDataElementVO internalPaymentDataElement = containsPaymentExtension(prsResponse, internalPaymentData);
        	PaymentInternalOpaqueDataVO piodVO = new PaymentInternalOpaqueDataVO();
        	
        	try {
        		Assert.assertTrue("Internal Opaque Data Element" + riskDecisionData + 
        				" expected, but not present in payment extensions", 
        				decodeOpaqueDataElement(internalPaymentDataElement, piodVO));
			} catch (IllegalArgumentException | IOException e) {
				LOGGER.warn("Exception in decodeOpaqueDataElement", e);
			}
    }
    
    private OpaqueDataElementVO containsPaymentExtension(GetLegacyEquivalentByPaymentReferenceResponse prsResponse,
            String voClassName) {
    	
        	return prsResponse.getLegacyEquivalent().getPaymentExtensionsList().stream()
                    .flatMap(paymentExtensionsVO -> paymentExtensionsVO.getPaymentExtensions().stream())
                    .filter(extension -> extension.getClassName().equals(voClassName))
                    .findAny().get(); 
    }
    
    private static Boolean decodeOpaqueDataElement(OpaqueDataElementVO opaqueData, PaymentInternalOpaqueDataVO targetVO)
            throws IllegalArgumentException, IOException {
        // Strict comparison - case sensitive
        if (!opaqueData.getClassName().equals(targetVO.voClassName())) {
            throw new IllegalArgumentException(
                    "targetVO type expected:" + opaqueData.getClassName() + "actual:" + targetVO.voClassName());
        }

        PRSResponseInternalOpaqueDataElementAsserter.deserializeBytes(opaqueData.getSerializedData(), targetVO);
        
        for (ValueObject internalVO : targetVO.getInternalOpaqueData()){
        	if(internalVO.voClassName().equals(riskDecisionData)){
        		return true;
        	}
        }
        LOGGER.warn("InternalOpaqueData Money::RiskDecisionVO expected, but not found");
        return false;
        
    }
    
    private static <VO extends ValueObject> void deserializeBytes (byte[] data, VO targetVO) throws IOException {
        new UniversalDeserializer().deserialize(new ByteArrayInputStream(data), targetVO);
    }

}
