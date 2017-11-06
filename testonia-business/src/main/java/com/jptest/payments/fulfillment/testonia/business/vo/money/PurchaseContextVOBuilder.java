package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.jptest.money.PurchaseContextVO;
import com.jptest.payments.fulfillment.testonia.business.vo.TemplateVOBuilder;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.types.Currency;
import com.jptest.user.LoadUserDataResponse;
import com.jptest.user.ModifyUserDataResponse;


public class PurchaseContextVOBuilder extends TemplateVOBuilder<PurchaseContextVO> {

    private final String resource;
    private Map<String, Object> valueMap = new HashMap<>();

    private PurchaseContextVOBuilder(String resource) {
        this.resource = resource;
    }

    public static PurchaseContextVOBuilder newBuilder(String template) {
        return new PurchaseContextVOBuilder(template);
    }

    public PurchaseContextVOBuilder sender(User sender) {
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

    public PurchaseContextVOBuilder recipient(User recipient) {
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

    public PurchaseContextVOBuilder sender(ModifyUserDataResponse dccBuyer) {
        valueMap.put("senderAccountNumber", dccBuyer.getAccount().getAccountNumber());
        valueMap.put("senderEmail", dccBuyer.getEmail().get(0).getEmail());
        valueMap.put("senderAddressOne", dccBuyer.getAddress().get(0).getAddress1());
        valueMap.put("senderAddressTwo", dccBuyer.getAddress().get(0).getAddress2());
        valueMap.put("senderCity", dccBuyer.getAddress().get(0).getCity());
        valueMap.put("senderZip", dccBuyer.getAddress().get(0).getZip());
        valueMap.put("senderIsoCountry", dccBuyer.getAddress().get(0).getCounty());
        return this;
    }

	public PurchaseContextVOBuilder sender(LoadUserDataResponse reponse) {
		valueMap.put("senderAccountNumber", reponse.getAccount().get(0).getAccountNumber());
		valueMap.put("senderAddressOne", reponse.getAddress().get(0).getAddress1());
		valueMap.put("senderAddressTwo", reponse.getAddress().get(0).getAddress2());
		valueMap.put("senderCity", reponse.getAddress().get(0).getCity());
		valueMap.put("senderZip", reponse.getAddress().get(0).getZip());
		valueMap.put("senderIsoCountry", reponse.getAddress().get(0).getCounty());
		return this;
	}

    public PurchaseContextVOBuilder senderAddressId(BigInteger senderAddressId) {
        valueMap.put("senderAddressId", String.valueOf(senderAddressId));
        return this;
    }

    public PurchaseContextVOBuilder recipientAddressId(BigInteger recipientAddressId) {
        valueMap.put("recipientAddressId", String.valueOf(recipientAddressId));
        return this;
    }

    public PurchaseContextVOBuilder invoice(String invoice) {
        valueMap.put("invoice", invoice);
        return this;
    }

    public PurchaseContextVOBuilder total(Currency totalAmount) {
        valueMap.put("totalAmount", totalAmount.toString());
        valueMap.put("currencyIsoCode",totalAmount.getCurrencyCode());
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
