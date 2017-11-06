package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PaymentSideDTO {

    @XmlElement(name = "payment_side_id")
	private BigInteger id;

    @XmlElement(name = "payment_id")
    private BigInteger paymentId;

    @XmlElement(name = "legacy_payment_id")
    private BigInteger legacyPaymentId;

    @XmlElement(name = "debit_credit_code")
    private String debitCreditCode;

    @XmlElement(name = "account_number")
    private BigInteger accountNumber;

    @XmlElement(name = "counter_party_account_number")
    private BigInteger counterpartyAccountNumber;

    @XmlElement(name = "party_id")
	private Long partyId;

    @XmlElement(name = "primary_party_name_id")
    private Long primaryPartyNameId;

    @XmlElement(name = "primary_party_phone_id")
    private BigInteger primaryPartyPhoneId;

    @XmlElement(name = "primary_party_postal_addr_id")
    private BigInteger primaryPartyPostalAddrId;

    @XmlElement(name = "type_code")
    private String typeCode;

    @XmlElement(name = "subtype_code")
    private String subtypeCode;

    @XmlElement(name = "reason_code")
	private String reasonCode;

    @XmlElement(name = "status_code")
    private String statusCode;

    @XmlElement(name = "money_amount")
    private Long moneyAmount;

    @XmlElement(name = "currency_code")
    private String currencyCode;

    @XmlElement(name = "usd_money_amount")
    private Long usdMoneyAmount;

    @XmlElement(name = "legacy_batc")
    private Long legacyBatc;

    @XmlElement(name = "legacy_shipping_addr_id")
    private Long legacyShippingAddrId;

    @XmlElement(name = "initiation_time")
    private Long initiationTime;

    @XmlElement(name = "last_updated_time")
    private Long lastUpdatedTime;

    @XmlElement(name = "parent_id")
    private BigInteger parentId;

    @XmlElement(name = "user_memo")
    private String userMemo;

    @XmlElement(name = "message_id")
    private BigInteger messageId;    

    @XmlElement(name = "item_detail_indicators")
	private BigInteger itemDetailIndicators;

    @XmlElement(name = "item_detail_flags")
    private BigInteger itemDetailFlags;

    @XmlElement(name = "flags01")
    private BigInteger flags01;

    @XmlElement(name = "flags02")
    private BigInteger flags02;

    @XmlElement(name = "flags03")
    private BigInteger flags03;

    @XmlElement(name = "flags04")
    private BigInteger flags04;

    @XmlElement(name = "flags05")
    private BigInteger flags05;

    @XmlElement(name = "flags06")
    private BigInteger flags06;

    @XmlElement(name = "flags07")
    private BigInteger flags07;

    @XmlElement(name = "flags08")
    private BigInteger flags08;

    @XmlElement(name = "flags09")
    private BigInteger flags09;

    @XmlElement(name = "flags10")
    private BigInteger flags10;

    @XmlElement(name = "row_created_time")
    private Long rowCreatedTime;

    @XmlElement(name = "row_updated_time")
    private Long rowUpdatedTime;

    @XmlElement(name = "counterparty_credential_code")
    private String counterpartyCredentialCode;

    @XmlElement(name = "counterparty_credential_id")
	private BigInteger counterpartyCredentialId;

    @XmlElement(name = "reason_detail_code")
	private String reasonDetailCode;

    @XmlElement(name = "last_login_identifier")
	private String lastLoginIdentifier;

    @XmlElement(name = "login_type_code")
    private String loginTypeCode;

    @XmlElement(name = "legacy_shared_id")
	private BigInteger legacySharedId;

    @XmlElement(name = "financial_journal_id")
	private BigInteger financialJournalId;  

	public BigInteger getFinancialJournalId() {
        return financialJournalId;
    }
    
	public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(BigInteger paymentId) {
        this.paymentId = paymentId;
    }

    public BigInteger getLegacyPaymentId() {
        return legacyPaymentId;
    }

    public void setLegacyPaymentId(BigInteger legacyPaymentId) {
        this.legacyPaymentId = legacyPaymentId;
    }

    public String getDebitCreditCode() {
        return debitCreditCode;
    }

    public void setDebitCreditCode(String debitCreditCode) {
        this.debitCreditCode = debitCreditCode;
    }

    public BigInteger getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigInteger getItemDetailIndicators() {
        return itemDetailIndicators;
    }

    public void setItemDetailIndicators(BigInteger itemDetailIndicators) {
        this.itemDetailIndicators = itemDetailIndicators;
    }

    public BigInteger getItemDetailFlags() {
        return itemDetailFlags;
    }

    public void setItemDetailFlags(BigInteger itemDetailFlags) {
        this.itemDetailFlags = itemDetailFlags;
    }

    public BigInteger getCounterpartyAccountNumber() {
        return counterpartyAccountNumber;
    }

    public void setCounterpartyAccountNumber(BigInteger counterpartyAccountNumber) {
        this.counterpartyAccountNumber = counterpartyAccountNumber;
    }

    public Long getLegacyShippingAddrId() {
        return legacyShippingAddrId;
    }

    public void setLegacyShippingAddrId(Long legacyShippingAddrId) {
        this.legacyShippingAddrId = legacyShippingAddrId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getPrimaryPartyNameId() {
        return primaryPartyNameId;
    }

    public void setPrimaryPartyNameId(Long primaryPartyNameId) {
        this.primaryPartyNameId = primaryPartyNameId;
    }

    public BigInteger getPrimaryPartyPhoneId() {
        return primaryPartyPhoneId;
    }

    public void setPrimaryPartyPhoneId(BigInteger primaryPartyPhoneId) {
        this.primaryPartyPhoneId = primaryPartyPhoneId;
    }

    public BigInteger getPrimaryPartyPostalAddrId() {
        return primaryPartyPostalAddrId;
    }

    public void setPrimaryPartyPostalAddrId(BigInteger primaryPartyPostalAddrId) {
        this.primaryPartyPostalAddrId = primaryPartyPostalAddrId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Long getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(Long moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getUsdMoneyAmount() {
        return usdMoneyAmount;
    }

    public void setUsdMoneyAmount(Long usdMoneyAmount) {
        this.usdMoneyAmount = usdMoneyAmount;
    }

    public Long getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(Long initiationTime) {
        this.initiationTime = initiationTime;
    }

    public Long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Long lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public BigInteger getLegacySharedId() {
        return legacySharedId;
    }

    public void setLegacySharedId(BigInteger legacySharedId) {
        this.legacySharedId = legacySharedId;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }

    public BigInteger getMessageId() {
        return messageId;
    }

    public void setMessageId(BigInteger messageId) {
        this.messageId = messageId;
    }

    public Long getRowCreatedTime() {
        return rowCreatedTime;
    }

    public void setRowCreatedTime(Long rowCreatedTime) {
        this.rowCreatedTime = rowCreatedTime;
    }

    public Long getRowUpdatedTime() {
        return rowUpdatedTime;
    }

    public void setRowUpdatedTime(Long rowUpdatedTime) {
        this.rowUpdatedTime = rowUpdatedTime;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getSubtypeCode() {
        return subtypeCode;
    }

    public void setSubtypeCode(String subtypeCode) {
        this.subtypeCode = subtypeCode;
    }

    public Long getLegacyBatc() {
        return legacyBatc;
    }

    public void setLegacyBatc(Long legacyBatc) {
        this.legacyBatc = legacyBatc;
    }

    public BigInteger getFlags01() {
        return flags01;
    }

    public void setFlags01(BigInteger flags01) {
        this.flags01 = flags01;
    }

    public BigInteger getFlags02() {
        return flags02;
    }

    public void setFlags02(BigInteger flags02) {
        this.flags02 = flags02;
    }

    public BigInteger getFlags03() {
        return flags03;
    }

    public void setFlags03(BigInteger flags03) {
        this.flags03 = flags03;
    }

    public BigInteger getFlags04() {
        return flags04;
    }

    public void setFlags04(BigInteger flags04) {
        this.flags04 = flags04;
    }

    public BigInteger getFlags05() {
        return flags05;
    }

    public void setFlags05(BigInteger flags05) {
        this.flags05 = flags05;
    }

    public BigInteger getFlags06() {
        return flags06;
    }

    public void setFlags06(BigInteger flags06) {
        this.flags06 = flags06;
    }

    public BigInteger getFlags07() {
        return flags07;
    }

    public void setFlags07(BigInteger flags07) {
        this.flags07 = flags07;
    }

    public BigInteger getFlags08() {
        return flags08;
    }

    public void setFlags08(BigInteger flags08) {
        this.flags08 = flags08;
    }

    public BigInteger getFlags09() {
        return flags09;
    }

    public void setFlags09(BigInteger flags09) {
        this.flags09 = flags09;
    }

    public BigInteger getFlags10() {
        return flags10;
    }

    public void setFlags10(BigInteger flags10) {
        this.flags10 = flags10;
    }

    public BigInteger getCounterpartyCredentialId() {
        return counterpartyCredentialId;
    }

    public void setCounterpartyCredentialId(BigInteger counterpartyCredentialId) {
        this.counterpartyCredentialId = counterpartyCredentialId;
    }

    public String getCounterpartyCredentialCode() {
        return counterpartyCredentialCode;
    }

    public void setCounterpartyCredentialCode(String counterpartyCredentialCode) {
        this.counterpartyCredentialCode = counterpartyCredentialCode;
    }

    public String getReasonDetailCode() {
        return reasonDetailCode;
    }

    public void setReasonDetailCode(String reasonDetailCode) {
        this.reasonDetailCode = reasonDetailCode;
    }

    public String getLastLoginIdentifier() {
        return lastLoginIdentifier;
    }

    public void setLastLoginIdentifier(String lastLoginIdentifier) {
        this.lastLoginIdentifier = lastLoginIdentifier;
    }

    public String getLoginTypeCode() {
        return loginTypeCode;
    }

    public void setLoginTypeCode(String loginTypeCode) {
        this.loginTypeCode = loginTypeCode;
    }
    
	public void setFinancialJournalId(BigInteger financialJournalId) {
        this.financialJournalId=financialJournalId;
    }    
}
