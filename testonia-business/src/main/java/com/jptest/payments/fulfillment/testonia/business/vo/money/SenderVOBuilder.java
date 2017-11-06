package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.SenderVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

public class SenderVOBuilder implements VOBuilder<SenderVO> {

	private BigInteger senderAccountNumber;
	private BigInteger addressId;
	private BigInteger addressConfirmedFlags;
	private BigInteger dccBuyerNonPrimaryAliasId;
	
	public static SenderVOBuilder newBuilder() {
		return new SenderVOBuilder();
	}
	
	public SenderVOBuilder senderAccountNumber(BigInteger senderAccountNumber) {
		this.senderAccountNumber = senderAccountNumber;
		return this;
	}
	
	public SenderVOBuilder addressId(BigInteger addressId) {
		this.addressId = addressId;
		return this;
	}
	
	public SenderVOBuilder addressConfirmedFlags(BigInteger addressConfirmedFlags) {
		this.addressConfirmedFlags = addressConfirmedFlags;
		return this;
	}
	
	public SenderVOBuilder dccBuyerNonPrimaryAliasId(BigInteger dccBuyerNonPrimaryAliasId) {
		this.dccBuyerNonPrimaryAliasId = dccBuyerNonPrimaryAliasId;
		return this;
	}

	@Override
	public SenderVO build() {
		SenderVO vo = new SenderVO();
		vo.setSenderAccountNumber(senderAccountNumber);
		vo.setAddressId(addressId);
		vo.setAddressConfirmedFlags(addressConfirmedFlags);
		vo.setDccBuyerNonPrimaryAliasId(dccBuyerNonPrimaryAliasId);
		return vo;
	}

}
