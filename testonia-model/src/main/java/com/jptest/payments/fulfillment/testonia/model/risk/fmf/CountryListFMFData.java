package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

import java.util.Set;

/**
 * Represents the input for RiskFMFActionType.Country_list FMF filter.
 */
public class CountryListFMFData extends BaseFMFData {

    private Set<String> countries;

    public Set<String> getCountries() {
        return countries;
    }

    public void setCountries(Set<String> countries) {
        this.countries = countries;
    }

    @Override
    public RiskFMFFilterType getFilterType() {
        return RiskFMFFilterType.Country_List;
    }

}
