package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.ListVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.money.TransactionAuthVO;

import com.jptest.money.AlternateIdentifierVO;
import com.jptest.money.AuthInfoVO;
import com.jptest.money.TransactionAuthFundingDetailsVO;
import com.jptest.types.Currency;
import java.math.BigInteger;


public class TransactionAuthVOBuilder implements VOBuilder<TransactionAuthVO> {

    private BigInteger id = BigInteger.ZERO;
    private BigInteger accountNumber = BigInteger.ZERO;
    private BigInteger counterparty = BigInteger.ZERO;
    private BigInteger transactionId = BigInteger.ZERO;
    private BigInteger cpartyTransId = BigInteger.ZERO;
    private BigInteger activeAuthId = BigInteger.ZERO;
    private Long flags = 0L;
    private Byte status = (byte) '\0';
    private Currency amountSettled;
    private Currency balanceAmount;
    private Byte fsType = (byte) '\0';
    private BigInteger fsId = BigInteger.ZERO;
    private Currency fsAmount;
    private Byte bufsType =  (byte) '\0';
    private BigInteger bufsId = BigInteger.ZERO;
    private Currency bufsAmount;
    private Long timeCreated = 0L;
    private Long timeUpdated = 0L;
    private Long reauthTime = 0L;
    private Currency fsAmountUsd;
    private Currency bufsAmountUsd;
    private Boolean isReauth = false;
    private VOBuilder<AuthInfoVO> authInfo = new NullVOBuilder<>();
    private BigInteger dbAuthVersion = BigInteger.ZERO;
    private ListVOBuilder<AlternateIdentifierVO> alternateIds = new NullVOBuilder<>();
    private BigInteger originalAuthId = BigInteger.ZERO;;
    private Currency amountAuthorized;
    private BigInteger numberOfSettlements = BigInteger.ZERO;;
    private Long honorExpirationTime = 0L;
    private BigInteger authExpirationTime = BigInteger.ZERO;;
    private Currency nontargetBalAmount;
    private Currency nontargetBalAmountUsd;
    private Currency amountAuthorizedUsd;
    private Currency amountSettledUsd;
    private Currency balanceAmountUsd;
    private Currency fsAmountSettled;
    private Integer memberOperation = 0;
    private VOBuilder<TransactionAuthFundingDetailsVO> transactionAuthFundingDetails = new NullVOBuilder<>();

    public static TransactionAuthVOBuilder newBuilder(String currencyCode) {
        if(currencyCode == null || currencyCode.isEmpty()) {
            currencyCode = "USD";
        }
        
        Currency emptyCurrency = new Currency(currencyCode, 0L);
        
        TransactionAuthVOBuilder transactionAuthVOBuilder =  new TransactionAuthVOBuilder()
                .amountAuthorized(emptyCurrency)
                .amountAuthorizedUsd(emptyCurrency)
                .amountSettled(emptyCurrency)
                .amountSettledUsd(emptyCurrency)
                .balanceAmount(emptyCurrency)
                .balanceAmountUsd(emptyCurrency)
                .bufsAmount(emptyCurrency)
                .bufsAmountUsd(emptyCurrency)
                .fsAmount(emptyCurrency)
                .fsAmountSettled(emptyCurrency)
                .fsAmountUsd(emptyCurrency)
                .nontargetBalAmount(emptyCurrency)
                .nontargetBalAmountUsd(emptyCurrency);
        
        return transactionAuthVOBuilder;
    }
    public TransactionAuthVOBuilder id(BigInteger id) {
        this.id = id;
        return this;

    }

    public TransactionAuthVOBuilder accountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
        return this;

    }

    public TransactionAuthVOBuilder counterparty(BigInteger counterparty) {
        this.counterparty = counterparty;
        return this;

    }

    public TransactionAuthVOBuilder transactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
        return this;

    }

    public TransactionAuthVOBuilder cpartyTransId(BigInteger cpartyTransId) {
        this.cpartyTransId = cpartyTransId;
        return this;

    }

    public TransactionAuthVOBuilder activeAuthId(BigInteger activeAuthId) {
        this.activeAuthId = activeAuthId;
        return this;

    }

    public TransactionAuthVOBuilder flags(Long flags) {
        this.flags = flags;
        return this;

    }

    public TransactionAuthVOBuilder status(Byte status) {
        this.status = status;
        return this;

    }

    public TransactionAuthVOBuilder amountSettled(Currency amountSettled) {
        this.amountSettled = amountSettled;
        return this;

    }

    public TransactionAuthVOBuilder balanceAmount(Currency balanceAmount) {
        this.balanceAmount = balanceAmount;
        return this;

    }

    public TransactionAuthVOBuilder fsType(Byte fsType) {
        this.fsType = fsType;
        return this;

    }

    public TransactionAuthVOBuilder fsId(BigInteger fsId) {
        this.fsId = fsId;
        return this;

    }

    public TransactionAuthVOBuilder fsAmount(Currency fsAmount) {
        this.fsAmount = fsAmount;
        return this;

    }

    public TransactionAuthVOBuilder bufsType(Byte bufsType) {
        this.bufsType = bufsType;
        return this;

    }

    public TransactionAuthVOBuilder bufsId(BigInteger bufsId) {
        this.bufsId = bufsId;
        return this;

    }

    public TransactionAuthVOBuilder bufsAmount(Currency bufsAmount) {
        this.bufsAmount = bufsAmount;
        return this;

    }

    public TransactionAuthVOBuilder timeCreated(Long timeCreated) {
        this.timeCreated = timeCreated;
        return this;

    }

    public TransactionAuthVOBuilder timeUpdated(Long timeUpdated) {
        this.timeUpdated = timeUpdated;
        return this;

    }

    public TransactionAuthVOBuilder reauthTime(Long reauthTime) {
        this.reauthTime = reauthTime;
        return this;

    }

    public TransactionAuthVOBuilder fsAmountUsd(Currency fsAmountUsd) {
        this.fsAmountUsd = fsAmountUsd;
        return this;

    }

    public TransactionAuthVOBuilder bufsAmountUsd(Currency bufsAmountUsd) {
        this.bufsAmountUsd = bufsAmountUsd;
        return this;

    }

    public TransactionAuthVOBuilder isReauth(Boolean isReauth) {
        this.isReauth = isReauth;
        return this;

    }

    public TransactionAuthVOBuilder authInfo(VOBuilder<AuthInfoVO> authInfo) {
        this.authInfo = authInfo;
        return this;

    }

    public TransactionAuthVOBuilder dbAuthVersion(BigInteger dbAuthVersion) {
        this.dbAuthVersion = dbAuthVersion;
        return this;

    }

    public TransactionAuthVOBuilder alternateIds(ListVOBuilder<AlternateIdentifierVO> alternateIds) {
        this.alternateIds = alternateIds;
        return this;

    }

    public TransactionAuthVOBuilder originalAuthId(BigInteger originalAuthId) {
        this.originalAuthId = originalAuthId;
        return this;

    }

    public TransactionAuthVOBuilder amountAuthorized(Currency amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
        return this;

    }

    public TransactionAuthVOBuilder numberOfSettlements(BigInteger numberOfSettlements) {
        this.numberOfSettlements = numberOfSettlements;
        return this;

    }

    public TransactionAuthVOBuilder honorExpirationTime(Long honorExpirationTime) {
        this.honorExpirationTime = honorExpirationTime;
        return this;

    }

    public TransactionAuthVOBuilder authExpirationTime(BigInteger authExpirationTime) {
        this.authExpirationTime = authExpirationTime;
        return this;

    }

    public TransactionAuthVOBuilder nontargetBalAmount(Currency nontargetBalAmount) {
        this.nontargetBalAmount = nontargetBalAmount;
        return this;

    }

    public TransactionAuthVOBuilder nontargetBalAmountUsd(Currency nontargetBalAmountUsd) {
        this.nontargetBalAmountUsd = nontargetBalAmountUsd;
        return this;

    }

    public TransactionAuthVOBuilder amountAuthorizedUsd(Currency amountAuthorizedUsd) {
        this.amountAuthorizedUsd = amountAuthorizedUsd;
        return this;

    }

    public TransactionAuthVOBuilder amountSettledUsd(Currency amountSettledUsd) {
        this.amountSettledUsd = amountSettledUsd;
        return this;

    }

    public TransactionAuthVOBuilder balanceAmountUsd(Currency balanceAmountUsd) {
        this.balanceAmountUsd = balanceAmountUsd;
        return this;

    }

    public TransactionAuthVOBuilder fsAmountSettled(Currency fsAmountSettled) {
        this.fsAmountSettled = fsAmountSettled;
        return this;

    }

    public TransactionAuthVOBuilder memberOperation(Integer memberOperation) {
        this.memberOperation = memberOperation;
        return this;

    }

    public TransactionAuthVOBuilder transactionAuthFundingDetails(VOBuilder<TransactionAuthFundingDetailsVO> transactionAuthFundingDetails) {
        this.transactionAuthFundingDetails = transactionAuthFundingDetails;
        return this;

    }

    @Override
    public TransactionAuthVO build() {
          TransactionAuthVO vo = new TransactionAuthVO();
          vo.setId(id);
          vo.setAccountNumber(accountNumber);
          vo.setCounterparty(counterparty);
          vo.setTransactionId(transactionId);
          vo.setCpartyTransId(cpartyTransId);
          vo.setActiveAuthId(activeAuthId);
          vo.setFlags(flags);
          vo.setStatus(status);
          vo.setAmountSettled(amountSettled);
          vo.setBalanceAmount(balanceAmount);
          vo.setFsType(fsType);
          vo.setFsId(fsId);
          vo.setFsAmount(fsAmount);
          vo.setBufsType(bufsType);
          vo.setBufsId(bufsId);
          vo.setBufsAmount(bufsAmount);
          vo.setTimeCreated(timeCreated);
          vo.setTimeUpdated(timeUpdated);
          vo.setReauthTime(reauthTime);
          vo.setFsAmountUsd(fsAmountUsd);
          vo.setBufsAmountUsd(bufsAmountUsd);
          vo.setIsReauth(isReauth);
          vo.setAuthInfo(authInfo.build());
          vo.setDbAuthVersion(dbAuthVersion);
          vo.setAlternateIds(alternateIds.buildList());
          vo.setOriginalAuthId(originalAuthId);
          vo.setAmountAuthorized(amountAuthorized);
          vo.setNumberOfSettlements(numberOfSettlements);
          vo.setHonorExpirationTime(honorExpirationTime);
          vo.setAuthExpirationTime(authExpirationTime);
          vo.setNontargetBalAmount(nontargetBalAmount);
          vo.setNontargetBalAmountUsd(nontargetBalAmountUsd);
          vo.setAmountAuthorizedUsd(amountAuthorizedUsd);
          vo.setAmountSettledUsd(amountSettledUsd);
          vo.setBalanceAmountUsd(balanceAmountUsd);
          vo.setFsAmountSettled(fsAmountSettled);
          vo.setMemberOperation(memberOperation);
          vo.setTransactionAuthFundingDetails(transactionAuthFundingDetails.build());
          return vo;
    }
}
