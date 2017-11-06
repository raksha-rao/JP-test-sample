package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.CounterPartyVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

public class CounterPartyVOBuilder implements VOBuilder<CounterPartyVO> {

	private BigInteger recipientAccountNumber;
	private String counterpartyAlias;
	private Byte counterpartyAliasType;
	private BigInteger transactionTargetAliasId;
	private Boolean isCounterpartyConfirmed = Boolean.TRUE;
	private BigInteger sppReasons = BigInteger.ZERO;
	private Long fmfPreference = Long.valueOf(0L);
	private Boolean useReceivingPreferencesMask = Boolean.FALSE;

	
	public static CounterPartyVOBuilder newBuilder() {
		return new CounterPartyVOBuilder();
	}
	
	public CounterPartyVOBuilder recipientAccountNumber(BigInteger recipientAccountNumber) {
		this.recipientAccountNumber = recipientAccountNumber;
		return this;
	}

	public CounterPartyVOBuilder counterpartyAlias(String counterpartyAlias) {
		this.counterpartyAlias = counterpartyAlias;
		return this;
	}
	
	public CounterPartyVOBuilder counterpartyAliasType(Byte counterpartyAliasType) {
		this.counterpartyAliasType = counterpartyAliasType;
		return this;
	}
	
	public CounterPartyVOBuilder transactionTargetAliasId(BigInteger transactionTargetAliasId) {
		this.transactionTargetAliasId = transactionTargetAliasId;
		return this;
	}
	
	public CounterPartyVOBuilder isCounterpartyConfirmed(Boolean isCounterpartyConfirmed) {
		this.isCounterpartyConfirmed = isCounterpartyConfirmed;
		return this;
	}
	
	public CounterPartyVOBuilder sppReasons(BigInteger sppReasons) {
		this.sppReasons = sppReasons;
		return this;
	}
	
	public CounterPartyVOBuilder fmfPreference(Long fmfPreference) {
		this.fmfPreference = fmfPreference;
		return this;
	}
	
	public CounterPartyVOBuilder useReceivingPreferencesMask(Boolean useReceivingPreferencesMask) {
		this.useReceivingPreferencesMask = useReceivingPreferencesMask;
		return this;
	}
	
	
	@Override
	public CounterPartyVO build() {
		CounterPartyVO vo = new CounterPartyVO();
		vo.setRecipientAccountNumber(recipientAccountNumber);
		vo.setCounterpartyAlias(counterpartyAlias);
		vo.setCounterpartyAliasType(counterpartyAliasType);
		vo.setTransactionTargetAliasId(transactionTargetAliasId);
		vo.setIsCounterpartyConfirmed(isCounterpartyConfirmed);
		vo.setSppReasons(sppReasons);
		vo.setFmfPreference(fmfPreference);
		vo.setUseReceivingPreferencesMask(useReceivingPreferencesMask);
		return vo;
	}

}
