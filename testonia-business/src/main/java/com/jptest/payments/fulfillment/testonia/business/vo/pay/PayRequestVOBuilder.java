package com.jptest.payments.fulfillment.testonia.business.vo.pay;

import java.util.HashMap;
import java.util.Map;

import com.jptest.money.PayRequest;
import com.jptest.payments.fulfillment.testonia.business.vo.TemplateVOBuilder;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Request builder for {@link PayRequest} based on the serialized templated 
 * file that represents a request.
 */
public class PayRequestVOBuilder extends TemplateVOBuilder<PayRequest> {

    private final String resource;
    private Map<String, Object> valueMap = new HashMap<>();

    private PayRequestVOBuilder(String resource) {
        this.resource = resource;
    }

    public static PayRequestVOBuilder newBuilder(String template) {
        return new PayRequestVOBuilder(template);
    }

    public PayRequestVOBuilder sender(User sender) {
        valueMap.put("senderAccountNumber", sender.getAccountNumber());
        valueMap.put("senderPhone", sender.getHomePhoneNumber());
        valueMap.put("senderEmail", sender.getEmailAddress());
        valueMap.put("senderAddressOne", sender.getHomeAddress1());
        valueMap.put("senderAddressTwo", sender.getHomeAddress2());
        valueMap.put("senderCity", sender.getHomeCity());
        valueMap.put("senderZip", sender.getHomeZip());
        valueMap.put("senderIsoCountry", sender.getHomeCountry());
        return this;
    }

    public PayRequestVOBuilder recipient(User recipient) {
        valueMap.put("recipientAccountNumber", recipient.getAccountNumber());
        valueMap.put("recipientEmail", recipient.getEmailAddress());
        valueMap.put("recipientPhone", recipient.getWorkPhone());
        valueMap.put("recipientAddressOne", recipient.getHomeAddress1());
        valueMap.put("recipientAddressTwo", recipient.getHomeAddress2());
        valueMap.put("recipientCity", recipient.getHomeCity());
        valueMap.put("recipientZip", recipient.getHomeZip());
        valueMap.put("recipientIsoCountry", recipient.getHomeCountry());
        return this;
    }

    public PayRequestVOBuilder senderAddressId(String senderAddressId) {
        valueMap.put("sender_address_id", senderAddressId);
        return this;
    }

    /**
     * Credit card id for the transaction.
     * @param ccId
     * @return
     */
    public PayRequestVOBuilder ccId(String ccId) {
        valueMap.put("cc_id", ccId);
        return this;
    }

    /**
     * Credit card id for the transaction.
     * @param ccId
     * @return
     */
    public PayRequestVOBuilder holdingId(String holdingId) {
        valueMap.put("holding_id", holdingId);
        return this;
    }

    /**
     * IACH ACH Id
     *  
     * @param iAchAchId
     * @return
     */
    public PayRequestVOBuilder iAchAchId(String iAchAchId) {
        valueMap.put("instant_ach_ach_id", iAchAchId);
        return this;
    }

    /**
     * Echeck ACH Id
     *  
     * @param echeckAchId
     * @return
     */
    public PayRequestVOBuilder echeckAchId(String echeckAchId) {
        valueMap.put("echeck_ach_id", echeckAchId);
        return this;
    }

    /**
     * Activity id for the given transaction.
     * @param id
     * @return
     */
    public PayRequestVOBuilder activityId(String id) {
        valueMap.put("pay_activity_id", id);
        return this;
    }

    /**
     * Backup funding source Id for the given transaction.
     * @param id
     * @return
     */
    public PayRequestVOBuilder backupFundingSourceId(String backupFundingSourceId) {
        valueMap.put("backup_funding_source_id", backupFundingSourceId);
        return this;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public Map<String, Object> getValueMap() {
        return valueMap;
    }

}
