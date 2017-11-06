package com.jptest.payments.fulfillment.testonia.business.service;

import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;

import com.jptest.payments.GetLegacyEquivalentByPaymentReferenceResponse;

@Singleton
public class PaymentExtensionHelper {

    /**
     * Checks all extensions in prsResponse and returns true if extension with voClazz
     * @param prsResponse
     * @param voClazz
     * @return
     */
    public boolean containsPaymentExtension(GetLegacyEquivalentByPaymentReferenceResponse prsResponse,
            String voClassName) {
        return CollectionUtils.isNotEmpty(prsResponse.getLegacyEquivalent().getPaymentExtensionsList())
                && prsResponse.getLegacyEquivalent().getPaymentExtensionsList().stream()
                        .flatMap(paymentExtensionsVO -> paymentExtensionsVO.getPaymentExtensions().stream())
                        .filter(extension -> extension.getClassName().equals(voClassName))
                        .findAny().isPresent();
    }
}
