package com.jptest.payments.fulfillment.testonia.model.risk.fmf;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *  Represents the input model to accept FMF filter related input.
 */
public class FMFInputData {

    private List<? extends BaseFMFData> fmfFilters;

    public List<? extends BaseFMFData> getFmfFilters() {
        return fmfFilters;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "filter_id")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TransactionAmountFMFData.class, name = "Transaction_Amount"),
            @JsonSubTypes.Type(value = CountryListFMFData.class, name = "Country_List")
    })
    public void setFmfFilters(List<? extends BaseFMFData> fmfFilters) {
        this.fmfFilters = fmfFilters;
    }

}
