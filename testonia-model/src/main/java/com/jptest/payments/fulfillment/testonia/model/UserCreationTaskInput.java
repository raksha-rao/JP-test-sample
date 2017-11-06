package com.jptest.payments.fulfillment.testonia.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.jptest.payments.fulfillment.testonia.model.risk.fmf.FMFInputData;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/*import com.jptest.qi.rest.domain.pojo.Bank;
import com.jptest.qi.rest.domain.pojo.BuyerCredit;
import com.jptest.qi.rest.domain.pojo.CreditCardMill;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.qi.rest.domain.pojo.Fund;
import com.jptest.qi.rest.domain.pojo.User;*/

@JsonInclude(Include.NON_NULL)
public class UserCreationTaskInput {

  //  private User user;
    private List<String> flags;
    //private BuyerCredit buyerCredit;
    private List<NameValue> preferences;
    private boolean ipn;
    //private CreditCardMill debitCardInfo;
    private FMFInputData fmfData;
    private boolean generateAPICredentials = false;
    private String riskHoldType;
    private PaymentPartyType partyType;
    private boolean cipCompleted = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreationTaskInput.class);

    public boolean isGenerateAPICredentials() {
        return generateAPICredentials;
    }

    public void setGenerateAPICredentials(boolean generateAPICredentials) {
        this.generateAPICredentials = generateAPICredentials;
    }

    public FMFInputData getFmfData() {
        return fmfData;
    }

    public void setFmfData(FMFInputData fmfData) {
        this.fmfData = fmfData;
    }

    public List<NameValue> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<NameValue> preferences) {
        this.preferences = preferences;
    }

   /* public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

    public List<String> getFlags() {
        return flags;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

  /*  public BuyerCredit getBuyerCredit() {
        return buyerCredit;
    }

    public void setBuyerCredit(BuyerCredit buyerCredit) {
        this.buyerCredit = buyerCredit;
    }*/

    public void setEnableIPN(boolean value) {
        this.ipn = value;
    }

    public boolean isEnableIPN() {
        return ipn;
    }

 /*   public CreditCardMill getDebitCardInfo() {
        return debitCardInfo;
    }

    public void setDebitCardInfo(CreditCardMill debitCardInfo) {
        this.debitCardInfo = debitCardInfo;
    }*/

    public String getRiskHoldType() {
        return riskHoldType;
    }

    public void setRiskHoldType(String riskHoldType) {
        this.riskHoldType = riskHoldType;
    }

    public PaymentPartyType getPartyType() {
        return partyType;
    }

    public void setPartyType(PaymentPartyType partyType) {
        this.partyType = partyType;
    }

    public boolean isCipCompleted() {
        return cipCompleted;
    }

    public void setCipCompleted(boolean cipCompleted) {
        this.cipCompleted = cipCompleted;
    }

    public static class NameValue {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    /**
     * This method goes through each and every property from {@link UserCreationTaskInput} and
     * based on the data creates a unique hash so that when we want the user with same exact properties
     * the hash should be same and we should be able to look up in the cache if we have such a user.
     * It is very important to maintain this logic inline with the changes in user creation input, otherwise
     * the cache look up can potentially bring a user which has some different properties which we failed
     * to consider in this logic.
     * @param //input
     * @return
     */
    public String generateKey() {
        String hash = getPlainTextHashKey();
        String key = new String(Hex.encodeHex(DigestUtils.md5(hash)));
        LOGGER.info("User caching Hash: {}, Key: {}", hash, key);
        return key;

    }

    public String getPlainTextHashKey() {
        StringBuffer hash = new StringBuffer();

      /*  BIConversion.User user = this.getUser();
        hash.append(user.getAccountType()).append(this.getUser().getCountry())
                .append(this.getUser().getCurrency()).append(user.isConfirmEmail())
                .append(this.isEnableIPN());

        if (CollectionUtils.isNotEmpty(this.getFlags())) {
            for (String flag : this.getFlags()) {
                hash.append(flag);
            }
        }

        if (CollectionUtils.isNotEmpty(user.getCreditcard())) {
            for (Creditcard card : user.getCreditcard()) {
                hash.append(card.getCardType()).append(card.getCurrency()).append(card.getCountry())
                        .append(card.isNeedConfirmed());
            }
        }

        if (CollectionUtils.isNotEmpty(user.getBank())) {
            for (Bank bank : user.getBank()) {
                hash.append(bank.getBankAccountType()).append(bank.getCurrency()).append(bank.getCountry())
                        .append(bank.isConfirmed());
            }
        }

        if (CollectionUtils.isNotEmpty(user.getFund())) {
            for (Fund fund : user.getFund()) {
                hash.append(fund.getFundsInCents()).append(fund.getCurrency());
            }
        }

        if (CollectionUtils.isNotEmpty(this.getPreferences())) {
            for (NameValue nv : this.getPreferences()) {
                hash.append(nv.getName()).append(nv.getValue());
            }
        }

        if (this.getFmfData() != null) {
            for (BaseFMFData baseFMF : this.getFmfData().getFmfFilters()) {
                if (baseFMF instanceof CountryListFMFData) {
                    CountryListFMFData fmfData = (CountryListFMFData) baseFMF;
                    hash.append(fmfData.getAction()).append(fmfData.getCountries());
                }
            }
        }

        if (this.getBuyerCredit() != null) {
            hash.append(StringUtils.defaultIfEmpty(this.getBuyerCredit().getApplicationId(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getBuyerCredit().getBuyerCreditNumber(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getBuyerCredit().getType(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getBuyerCredit().getVendorRecordReference(), ""));
        }

        if (this.getDebitCardInfo() != null) {
            hash.append(StringUtils.defaultIfEmpty(this.getDebitCardInfo().getBin(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getDebitCardInfo().getCardType(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getDebitCardInfo().getCountry(), ""))
                    .append(StringUtils.defaultIfEmpty(this.getDebitCardInfo().getCurrency(), ""));
        }

        if (this.isGenerateAPICredentials()) {
            hash.append("generatAPICredentials");
        }

        if (StringUtils.isNotBlank(this.getRiskHoldType())) {
            hash.append(this.getRiskHoldType());
        }

        if (this.getPartyType() != null) {
            hash.append(this.getPartyType().getName());
        }

        if (this.isCipCompleted()) {
            hash.append(this.isCipCompleted());
        }*/
        return hash.toString();
    }

}
