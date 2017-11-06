package com.jptest.payments.fulfillment.testonia.model.money;

/*
 * This enum to define all CC Processors
 */
public enum Processor {

    /**
     * FDMS - FDMSNORTH Processor for USD cross currency conversion
     */
    FDMS("FDMS", "FDMSNORTH"),

    /**
     * BNPP - BNPP Processor for EUR cross currency conversion
     */
    BNPP("BNPP", "BNPP"),

    /**
     * NTT - NTT Processor for JPY cross currency conversion
     */
    NTT("NTT", "NTT"),

    /**
     * NG - NG Processor for Latin America cross currency conversion
     */
    NG("NG", "NG"),

    /**
     * STAR - STAR Processor for USD cross currency conversion when it's sale Pinless
     */
    STAR("STAR", "STAR"),
	
    /**
     * OMNIPAY - for AU country
     */
	OMNIPAY("OMPY", "OMPY"),
	
	/**
     * DGRV - for FR country with carte_aurore CC TYPE
     */
	DGRV("DGRV","DGRV"),
    
    /**
     * NETGIRO - FR country with carte_aurore CC TYPE
     */
	NETGIRO("NETGIRO","NETGIRO");

    private final String name;
    private final String value;

    private Processor(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

}
