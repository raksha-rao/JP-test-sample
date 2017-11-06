package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;
import com.jptest.finsys.OverageToleranceType;
import com.jptest.money.AuthCaptureAnalyzeVO;
import com.jptest.money.TransactionAuthVO;
import com.jptest.payments.fulfillment.testonia.business.vo.NullVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;


public class AuthCaptureAnalyzeVOBuilder implements VOBuilder<AuthCaptureAnalyzeVO> {

    private Boolean hasActiveGenericInstrumentAuth = false;
    private Currency allowedAdditionalBalanceAmount = null; // Zero amount?
    private Boolean isOrderRelated = false;
    private Currency amountOverAuth = null; // Zero amount?
    private Boolean hasActiveCcAuth = false;
    private BigInteger activeCcAuthCctransId = BigInteger.ZERO;
    private Currency minCcCanSettle = null; // Zero amount?
    private Currency maxCcCanSettle = null; // Zero amount?
    private Boolean hasActiveBcAuth = false;
    private BigInteger activeBcAuthBctransId = BigInteger.ZERO;
    private Currency minBcCanSettle = null; // Zero amount?
    private Currency maxBcCanSettle = null; // Zero amount?
    private Currency minGenericInstrumentCanSettle = null;
    private VOBuilder<TransactionAuthVO> origAuth = new NullVOBuilder<>();
    private VOBuilder<TransactionAuthVO> activeAuth = new NullVOBuilder<>();
    private VOBuilder<TransactionAuthVO> childAuth = new NullVOBuilder<>();
    private BigInteger orderId = BigInteger.ZERO;
    private Boolean hasOrderChild = false;
    private Boolean shouldHonor = false;
    private Boolean closeAuth = false;
    private Currency maxGenericInstrumentCanSettle = null;
    private BigInteger tempPHoldTxnId = BigInteger.ZERO;
    private BigInteger tempFHoldTxnId = BigInteger.ZERO;
    private Currency amountReservedFromPBalance = null;
    private Currency amountReservedFromFBalance = null;
    private Boolean fraudDecisionOverridden = false;
    private Boolean isNegBalanceCaptureAllowed = false;
    private Boolean generateAuthCode = false;
    private Boolean multiCaptureAuth = false;
    private Boolean useOriginalAuthForLateCapture = false;
    private Currency forceCaptureBalanceAmount = null; // Zero amount?
    private Boolean ccChargeInTxnCurrency;
    // private Long overageToleranceType;
    private OverageToleranceType overageToleranceType = OverageToleranceType.NONE;
    private BigInteger authActivityId = null;
    private BigInteger orderActivityId = null;
    private Boolean isDelayedCcCharge = false;

    public static AuthCaptureAnalyzeVOBuilder newBuilder() {
        return new AuthCaptureAnalyzeVOBuilder();
    }

    public AuthCaptureAnalyzeVOBuilder hasActiveGenericInstrumentAuth(final Boolean hasActiveGenericInstrumentAuth) {
        this.hasActiveGenericInstrumentAuth = hasActiveGenericInstrumentAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder allowedAdditionalBalanceAmount(final Currency allowedAdditionalBalanceAmount) {
        this.allowedAdditionalBalanceAmount = allowedAdditionalBalanceAmount;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder isOrderRelated(final Boolean isOrderRelated) {
        this.isOrderRelated = isOrderRelated;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder amountOverAuth(final Currency amountOverAuth) {
        this.amountOverAuth = amountOverAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder hasActiveCcAuth(final Boolean hasActiveCcAuth) {
        this.hasActiveCcAuth = hasActiveCcAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder activeCcAuthCctransId(final BigInteger activeCcAuthCctransId) {
        this.activeCcAuthCctransId = activeCcAuthCctransId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder minCcCanSettle(final Currency minCcCanSettle) {
        this.minCcCanSettle = minCcCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder maxCcCanSettle(final Currency maxCcCanSettle) {
        this.maxCcCanSettle = maxCcCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder hasActiveBcAuth(final Boolean hasActiveBcAuth) {
        this.hasActiveBcAuth = hasActiveBcAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder activeBcAuthBctransId(final BigInteger activeBcAuthBctransId) {
        this.activeBcAuthBctransId = activeBcAuthBctransId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder minBcCanSettle(final Currency minBcCanSettle) {
        this.minBcCanSettle = minBcCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder maxBcCanSettle(final Currency maxBcCanSettle) {
        this.maxBcCanSettle = maxBcCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder minGenericInstrumentCanSettle(final Currency minGenericInstrumentCanSettle) {
        this.minGenericInstrumentCanSettle = minGenericInstrumentCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder origAuth(final VOBuilder<TransactionAuthVO> origAuth) {
        this.origAuth = origAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder activeAuth(final VOBuilder<TransactionAuthVO> activeAuth) {
        this.activeAuth = activeAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder childAuth(final VOBuilder<TransactionAuthVO> childAuth) {
        this.childAuth = childAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder orderId(final BigInteger orderId) {
        this.orderId = orderId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder hasOrderChild(final Boolean hasOrderChild) {
        this.hasOrderChild = hasOrderChild;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder shouldHonor(final Boolean shouldHonor) {
        this.shouldHonor = shouldHonor;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder closeAuth(final Boolean closeAuth) {
        this.closeAuth = closeAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder maxGenericInstrumentCanSettle(final Currency maxGenericInstrumentCanSettle) {
        this.maxGenericInstrumentCanSettle = maxGenericInstrumentCanSettle;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder tempPHoldTxnId(final BigInteger tempPHoldTxnId) {
        this.tempPHoldTxnId = tempPHoldTxnId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder tempFHoldTxnId(final BigInteger tempFHoldTxnId) {
        this.tempFHoldTxnId = tempFHoldTxnId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder amountReservedFromPBalance(final Currency amountReservedFromPBalance) {
        this.amountReservedFromPBalance = amountReservedFromPBalance;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder amountReservedFromFBalance(final Currency amountReservedFromFBalance) {
        this.amountReservedFromFBalance = amountReservedFromFBalance;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder fraudDecisionOverridden(final Boolean fraudDecisionOverridden) {
        this.fraudDecisionOverridden = fraudDecisionOverridden;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder isNegBalanceCaptureAllowed(final Boolean isNegBalanceCaptureAllowed) {
        this.isNegBalanceCaptureAllowed = isNegBalanceCaptureAllowed;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder generateAuthCode(final Boolean generateAuthCode) {
        this.generateAuthCode = generateAuthCode;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder multiCaptureAuth(final Boolean multiCaptureAuth) {
        this.multiCaptureAuth = multiCaptureAuth;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder useOriginalAuthForLateCapture(final Boolean useOriginalAuthForLateCapture) {
        this.useOriginalAuthForLateCapture = useOriginalAuthForLateCapture;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder forceCaptureBalanceAmount(final Currency forceCaptureBalanceAmount) {
        this.forceCaptureBalanceAmount = forceCaptureBalanceAmount;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder ccChargeInTxnCurrency(final Boolean ccChargeInTxnCurrency) {
        this.ccChargeInTxnCurrency = ccChargeInTxnCurrency;
        return this;

    }

    // public AuthCaptureAnalyzeVOBuilder overageToleranceType(Long overageToleranceType) {
    // this.overageToleranceType = overageToleranceType;
    // return this;
    //
    // }

    public AuthCaptureAnalyzeVOBuilder overageToleranceType(final OverageToleranceType overageToleranceType) {
        this.overageToleranceType = overageToleranceType;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder authActivityId(final BigInteger authActivityId) {
        this.authActivityId = authActivityId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder orderActivityId(final BigInteger orderActivityId) {
        this.orderActivityId = orderActivityId;
        return this;

    }

    public AuthCaptureAnalyzeVOBuilder isDelayedCcCharge(final Boolean isDelayedCcCharge) {
        this.isDelayedCcCharge = isDelayedCcCharge;
        return this;

    }

    @Override
    public AuthCaptureAnalyzeVO build() {
        final AuthCaptureAnalyzeVO vo = new AuthCaptureAnalyzeVO();
        vo.setHasActiveGenericInstrumentAuth(this.hasActiveGenericInstrumentAuth);
        vo.setAllowedAdditionalBalanceAmount(this.allowedAdditionalBalanceAmount);
        vo.setIsOrderRelated(this.isOrderRelated);
        vo.setAmountOverAuth(this.amountOverAuth);
        vo.setHasActiveCcAuth(this.hasActiveCcAuth);
        vo.setActiveCcAuthCctransId(this.activeCcAuthCctransId);
        vo.setMinCcCanSettle(this.minCcCanSettle);
        vo.setMaxCcCanSettle(this.maxCcCanSettle);
        vo.setHasActiveBcAuth(this.hasActiveBcAuth);
        vo.setActiveBcAuthBctransId(this.activeBcAuthBctransId);
        vo.setMinBcCanSettle(this.minBcCanSettle);
        vo.setMaxBcCanSettle(this.maxBcCanSettle);
        vo.setMinGenericInstrumentCanSettle(this.minGenericInstrumentCanSettle);
        vo.setOrigAuth(this.origAuth.build());
        vo.setActiveAuth(this.activeAuth.build());
        vo.setChildAuth(this.childAuth.build());
        vo.setOrderId(this.orderId);
        vo.setHasOrderChild(this.hasOrderChild);
        vo.setShouldHonor(this.shouldHonor);
        vo.setCloseAuth(this.closeAuth);
        vo.setMaxGenericInstrumentCanSettle(this.maxGenericInstrumentCanSettle);
        vo.setTempPHoldTxnId(this.tempPHoldTxnId);
        vo.setTempFHoldTxnId(this.tempFHoldTxnId);
        vo.setAmountReservedFromPBalance(this.amountReservedFromPBalance);
        vo.setAmountReservedFromFBalance(this.amountReservedFromFBalance);
        vo.setFraudDecisionOverridden(this.fraudDecisionOverridden);
        vo.setIsNegBalanceCaptureAllowed(this.isNegBalanceCaptureAllowed);
        vo.setGenerateAuthCode(this.generateAuthCode);
        vo.setMultiCaptureAuth(this.multiCaptureAuth);
        vo.setUseOriginalAuthForLateCapture(this.useOriginalAuthForLateCapture);
        vo.setForceCaptureBalanceAmount(this.forceCaptureBalanceAmount);
        vo.setCcChargeInTxnCurrency(this.ccChargeInTxnCurrency);
        vo.setOverageToleranceType(this.overageToleranceType);
        vo.setOverageToleranceType(this.overageToleranceType);
        vo.setAuthActivityId(this.authActivityId);
        vo.setOrderActivityId(this.orderActivityId);
        vo.setIsDelayedCcCharge(this.isDelayedCcCharge);
        return vo;
    }
}
