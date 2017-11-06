package com.jptest.payments.fulfillment.testonia.model.finsys;

import java.math.BigInteger;

/**
 * Represents SET_TXN_REC_CLEARING table row of FINSYS database.
 * It is used to build input for TxnPostFulfillmentServ daemon for bank-completion use case
 */
public class SetTxnRecClearingDTO {

    private BigInteger tid;

    private String bankCountry;

    private String bankName;

    private String batchRefId;

    private Long clearingDate;

    private String intmAgtRef;

    private BigInteger moneyMvmntId;

    private String moneyMvmntType;

    private String procId;

    private String remarks;

    private BigInteger setTxnReqTid;

    private BigInteger setTxnTid;

    private String status;

    private BigInteger txnAmt;

    private String txnCurCd;

    private String txnRef;

    private BigInteger txnTm;

    public BigInteger getTid() {
        return this.tid;
    }

    public void setTid(BigInteger tid) {
        this.tid = tid;
    }

    public String getBankCountry() {
        return this.bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getBankName() {
        return this.bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBatchRefId() {
        return this.batchRefId;
    }

    public void setBatchRefId(String batchRefId) {
        this.batchRefId = batchRefId;
    }

    public Long getClearingDate() {
        return this.clearingDate;
    }

    public void setClearingDate(Long clearingDate) {
        this.clearingDate = clearingDate;
    }

    public String getIntmAgtRef() {
        return this.intmAgtRef;
    }

    public void setIntmAgtRef(String intmAgtRef) {
        this.intmAgtRef = intmAgtRef;
    }

    public BigInteger getMoneyMvmntId() {
        return this.moneyMvmntId;
    }

    public void setMoneyMvmntId(BigInteger moneyMvmntId) {
        this.moneyMvmntId = moneyMvmntId;
    }

    public String getMoneyMvmntType() {
        return this.moneyMvmntType;
    }

    public void setMoneyMvmntType(String moneyMvmntType) {
        this.moneyMvmntType = moneyMvmntType;
    }

    public String getProcId() {
        return this.procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigInteger getSetTxnReqTid() {
        return this.setTxnReqTid;
    }

    public void setSetTxnReqTid(BigInteger setTxnReqTid) {
        this.setTxnReqTid = setTxnReqTid;
    }

    public BigInteger getSetTxnTid() {
        return this.setTxnTid;
    }

    public void setSetTxnTid(BigInteger setTxnTid) {
        this.setTxnTid = setTxnTid;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigInteger getTxnAmt() {
        return this.txnAmt;
    }

    public void setTxnAmt(BigInteger txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnCurCd() {
        return this.txnCurCd;
    }

    public void setTxnCurCd(String txnCurCd) {
        this.txnCurCd = txnCurCd;
    }

    public String getTxnRef() {
        return this.txnRef;
    }

    public void setTxnRef(String txnRef) {
        this.txnRef = txnRef;
    }

    public BigInteger getTxnTm() {
        return this.txnTm;
    }

    public void setTxnTm(BigInteger txnTm) {
        this.txnTm = txnTm;
    }
}
