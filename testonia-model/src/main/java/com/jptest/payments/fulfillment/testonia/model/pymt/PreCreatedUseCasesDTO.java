package com.jptest.payments.fulfillment.testonia.model.pymt;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PreCreatedUseCasesDTO {

    @XmlElement(name = "id")
    private BigInteger id;

    @XmlElement(name = "usecase_id")
    private String usecaseId;

    @XmlElement(name = "transaction_id")
    private BigInteger transactionId;

    @XmlElement(name = "buyer_account_number")
    private BigInteger buyerAccountNumber;

    @XmlElement(name = "seller_account_number")
    private BigInteger sellerAccountNumber;

    @XmlElement(name = "funder_account_number")
    private BigInteger funderAccountNumber;

    @XmlElement(name = "usecase_type")
    private String usecaseType;

    @XmlElement(name = "is_used")
    private char isUsed;

    public BigInteger getId() {
        return this.id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUsecaseId() {
        return this.usecaseId;
    }

    public void setUsecaseId(String usecaseId) {
        this.usecaseId = usecaseId;
    }

    public BigInteger getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(BigInteger transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getBuyerAccountNumber() {
        return this.buyerAccountNumber;
    }

    public void setBuyerAccountNumber(BigInteger buyerAccountNumber) {
        this.buyerAccountNumber = buyerAccountNumber;
    }

    public BigInteger getSellerAccountNumber() {
        return this.sellerAccountNumber;
    }

    public void setSellerAccountNumber(BigInteger sellerAccountNumber) {
        this.sellerAccountNumber = sellerAccountNumber;
    }

    public BigInteger getFunderAccountNumber() {
        return this.funderAccountNumber;
    }

    public void setFunderAccountNumber(BigInteger funderAccountNumber) {
        this.funderAccountNumber = funderAccountNumber;
    }

    public String getUsecaseType() {
        return this.usecaseType;
    }

    public void setUsecaseType(String usecaseType) {
        this.usecaseType = usecaseType;
    }

    public char getIsUsed() {
        return this.isUsed;
    }

    public void setIsUsed(char isUsed) {
        this.isUsed = isUsed;
    }

    public enum UseCaseTypeEnum {

        SINGLE("funder"),
        MULTIPLE("buyer");

        String useCaseType;

        UseCaseTypeEnum(String useCaseType) {
            this.useCaseType = useCaseType;
        }

        public String getUseCaseType() {
            return this.useCaseType;
        }

    }

}
