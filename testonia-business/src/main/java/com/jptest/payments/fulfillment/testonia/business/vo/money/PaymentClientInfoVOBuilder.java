package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.PaymentClientInfoVO;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;

public class PaymentClientInfoVOBuilder implements
		VOBuilder<PaymentClientInfoVO> {

	private Boolean isDcc;
	private Boolean isWax;
	private Boolean isMobile;
	private Boolean isSkype;
	private Boolean isAutopay;
	private Boolean isUome;
	private Boolean isMobileTerminatedTxn;
	private Boolean isMobileInitiatedTxn;
	private Boolean isSkypeInitiatedTxn;
	private BigInteger addressReasons;
	private Boolean isSymphonyControlGroup;

	public static PaymentClientInfoVOBuilder newBuilder() {
		return new PaymentClientInfoVOBuilder();
	}

	public PaymentClientInfoVOBuilder isDcc(Boolean isDcc) {
		this.isDcc = isDcc;
		return this;

	}

	public PaymentClientInfoVOBuilder isWax(Boolean isWax) {
		this.isWax = isWax;
		return this;

	}

	public PaymentClientInfoVOBuilder isMobile(Boolean isMobile) {
		this.isMobile = isMobile;
		return this;

	}

	public PaymentClientInfoVOBuilder isSkype(Boolean isSkype) {
		this.isSkype = isSkype;
		return this;

	}

	public PaymentClientInfoVOBuilder isAutopay(Boolean isAutopay) {
		this.isAutopay = isAutopay;
		return this;

	}

	public PaymentClientInfoVOBuilder isUome(Boolean isUome) {
		this.isUome = isUome;
		return this;

	}

	public PaymentClientInfoVOBuilder isMobileTerminatedTxn(
			Boolean isMobileTerminatedTxn) {
		this.isMobileTerminatedTxn = isMobileTerminatedTxn;
		return this;

	}

	public PaymentClientInfoVOBuilder isMobileInitiatedTxn(
			Boolean isMobileInitiatedTxn) {
		this.isMobileInitiatedTxn = isMobileInitiatedTxn;
		return this;

	}

	public PaymentClientInfoVOBuilder isSkypeInitiatedTxn(
			Boolean isSkypeInitiatedTxn) {
		this.isSkypeInitiatedTxn = isSkypeInitiatedTxn;
		return this;

	}

	public PaymentClientInfoVOBuilder addressReasons(BigInteger addressReasons) {
		this.addressReasons = addressReasons;
		return this;

	}

	public PaymentClientInfoVOBuilder isSymphonyControlGroup(
			Boolean isSymphonyControlGroup) {
		this.isSymphonyControlGroup = isSymphonyControlGroup;
		return this;

	}

	@Override
	public PaymentClientInfoVO build() {
		PaymentClientInfoVO vo = new PaymentClientInfoVO();
		vo.setIsDcc(isDcc);
		vo.setIsWax(isWax);
		vo.setIsMobile(isMobile);
		vo.setIsSkype(isSkype);
		vo.setIsAutopay(isAutopay);
		vo.setIsUome(isUome);
		vo.setIsMobileTerminatedTxn(isMobileTerminatedTxn);
		vo.setIsMobileInitiatedTxn(isMobileInitiatedTxn);
		vo.setIsSkypeInitiatedTxn(isSkypeInitiatedTxn);
		vo.setAddressReasons(addressReasons);
		vo.setIsSymphonyControlGroup(isSymphonyControlGroup);
		return vo;
	}
}
