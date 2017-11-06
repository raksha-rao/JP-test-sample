package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents COUNTERPARTY_ALIAS table record
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CounterPartyAliasDTO {
	
	private BigInteger paymentSideId;
	private String aliasValue;
	private String aliasType;
	
	@XmlElement(name = "payment_side_id")
	public BigInteger getPaymentSideId() {
		return paymentSideId;
	}
	
	public void setPaymentSideId(BigInteger paymentSideId) {
		this.paymentSideId = paymentSideId;
	}

	@XmlElement(name = "alias_value")
	public String getAliasValue() {
		return aliasValue;
	}

	public void setAliasValue(String aliasValue) {
		this.aliasValue = aliasValue;
	}

	@XmlElement(name = "alias_type")
	public String getAliasType() {
		return aliasType;
	}

	public void setAliasType(String aliasType) {
		this.aliasType = aliasType;
	}

}
