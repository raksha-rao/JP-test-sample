package com.jptest.payments.fulfillment.testonia.bridge;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.jptest.cipro.Money;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.model.risk.fmf.BaseFMFData;
import com.jptest.payments.fulfillment.testonia.model.risk.fmf.CountryListFMFData;
import com.jptest.payments.fulfillment.testonia.model.risk.fmf.TransactionAmountFMFData;
import com.jptest.payments.fulfillment.testonia.resource.RiskAdminServClient;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.riskadminserv.RiskControlAccountFilterSettingsCIPRO;
import com.jptest.riskadminserv.RiskControlAccountInfoCIPRO;
import com.jptest.riskadminserv.RiskControlExceptionStatusCIPRO;
import com.jptest.riskadminserv.RiskControlFilterParamCollectionCIPRO;
import com.jptest.riskadminserv.RiskControlFilterParamCountryListCIPRO;
import com.jptest.riskadminserv.RiskControlFilterParamCountryListCIPROArray;
import com.jptest.riskadminserv.RiskControlFilterParamPaymentCIPRO;
import com.jptest.riskadminserv.RiskControlFilterParamPaymentCIPROArray;
import com.jptest.riskadminserv.RiskControlFilterSettingsCIPRO;
import com.jptest.riskadminserv.RiskControlFilterSettingsCIPROArray;
import com.jptest.riskadminserv.StringArray;

/**
 * Represents a bridge to interact with riskadminserv.
 */
@Singleton
public class RiskAdminServBridge {

    @Inject
    private RiskAdminServClient riskAdminServClient;

    public void enableFMFForUser(User user) {
        char productType = 'D'; // This is the product type for FMF
        int productCode = 1; // TODO: Find out what this means. This is coming from equivalent bluefin code.
        boolean isSandBox = true; // Since this is part of test case and not production.
        RiskControlAccountInfoCIPRO accountInfo = new RiskControlAccountInfoCIPRO(
                Long.parseLong(user.getAccountNumber()),
                user.getCountry(), productType, productCode, isSandBox);
        RiskControlExceptionStatusCIPRO exceptionStatus = new RiskControlExceptionStatusCIPRO();
        try {
            riskAdminServClient.risk_control_enroll(accountInfo, exceptionStatus);
        } catch (Exception e) {
            throw new TestExecutionException("Failed while enabling FMF for user " + user.getAccountNumber(), e);
        }
    }

    /**
     * Adds the FMF filters for the given user. FMF filters are represented by
     * {@link BaseFMFData} hierarchy of classes.
     * @param actualUser
     * @param fmfFilters
     */
    public void addFMFFilters(User user, List<? extends BaseFMFData> fmfFilters) {
        enableFMFForUser(user);
        RiskControlFilterSettingsCIPRO[] filterSettings = getFilterSettings(fmfFilters);
        setFMFFilterForUser(user.getAccountNumber(), filterSettings);
    }

    /**
     * Converts the {@link BaseFMFData} instances into {@link RiskControlFilterSettingsCIPRO} instances which
     * are consumed by the risk service to set the FMF filters.
     * @param fmfFilters
     * @return
     */
    private RiskControlFilterSettingsCIPRO[] getFilterSettings(List<? extends BaseFMFData> fmfFilters) {
        RiskControlFilterSettingsCIPRO[] filters = new RiskControlFilterSettingsCIPRO[fmfFilters.size()];
        for (int i = 0; i < fmfFilters.size(); i++) {
            BaseFMFData fmfData = fmfFilters.get(i);
            switch (fmfData.getFilterType()) {
            case Transaction_Amount:
                filters[i] = getFilterSettingForTransactionAmount((TransactionAmountFMFData) fmfData);
                break;
            case Country_List:
                filters[i] = getFilterSettingForCountry((CountryListFMFData) fmfData);
                break;
            default:
                throw new TestExecutionException("Incorrect FMF filter type");
            }
        }
        return filters;
    }

    /**
     * @param fmfData
     */
    private RiskControlFilterSettingsCIPRO getFilterSettingForTransactionAmount(TransactionAmountFMFData fmfData) {
        RiskControlFilterSettingsCIPRO filter = getBasicFilterSettingObject(fmfData);
        filter.param.max_payment = new RiskControlFilterParamPaymentCIPROArray();
        RiskControlFilterParamPaymentCIPRO paymentFilter = new RiskControlFilterParamPaymentCIPRO();
        paymentFilter.payment = new Money(); // TODO : Make this work.
        paymentFilter.sub_rule_id = 0;
        filter.param.max_payment.add(paymentFilter);
        return filter;

    }

    private RiskControlFilterSettingsCIPRO getBasicFilterSettingObject(BaseFMFData fmfData) {
        RiskControlFilterSettingsCIPRO filter = new RiskControlFilterSettingsCIPRO();
        filter.action = fmfData.getAction().getValue();
        filter.enabled = Boolean.TRUE;
        filter.filter_id = fmfData.getFilterType().getValue();
        filter.param = new RiskControlFilterParamCollectionCIPRO();
        return filter;
    }

    private RiskControlFilterSettingsCIPRO getFilterSettingForCountry(CountryListFMFData fmfData) {
        RiskControlFilterSettingsCIPRO filter = getBasicFilterSettingObject(fmfData);
        filter.param.country_list = new RiskControlFilterParamCountryListCIPROArray();
        RiskControlFilterParamCountryListCIPRO countryFilter = new RiskControlFilterParamCountryListCIPRO();
        countryFilter.country = new StringArray();
        for (String country : fmfData.getCountries()) {
            countryFilter.country.add(country);
        }
        countryFilter.sub_rule_id = 0;
        filter.param.country_list.add(countryFilter);
        return filter;

    }

    private void setFMFFilterForUser(String accountNumber, RiskControlFilterSettingsCIPRO[] filterArray) {
        RiskControlExceptionStatusCIPRO riskControlExceptionStatus = new RiskControlExceptionStatusCIPRO();
        RiskControlFilterSettingsCIPROArray ciproArray = new RiskControlFilterSettingsCIPROArray(filterArray);
        RiskControlAccountFilterSettingsCIPRO accountFilterSettings = new RiskControlAccountFilterSettingsCIPRO(
                Long.parseLong(accountNumber), ciproArray);
        riskAdminServClient.risk_control_update_filter_setting_for_account(accountFilterSettings,
                riskControlExceptionStatus);
    }

}
