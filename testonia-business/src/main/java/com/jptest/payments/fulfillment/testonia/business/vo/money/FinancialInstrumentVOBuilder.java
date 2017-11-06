package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.FinancialInstrumentVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;


public class FinancialInstrumentVOBuilder extends ListWrappedVOBuilder<FinancialInstrumentVO> {
    
    private BigInteger id;

    private Integer instrumentType;

    private FinancialInstrumentTypeClass instrumentTypeEnum;
    
    private String currencyCode;
    
    private String countryCode;
        
    public static FinancialInstrumentVOBuilder newBuilder() {
        return new FinancialInstrumentVOBuilder();
    }

    public FinancialInstrumentVOBuilder id(BigInteger id) {
        this.id = id;
        return this;
    }
    
    public FinancialInstrumentVOBuilder instrumentType(Integer instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }
    
    public FinancialInstrumentVOBuilder instrumentTypeEnum(FinancialInstrumentTypeClass instrumentTypeEnum) {
        this.instrumentTypeEnum = instrumentTypeEnum;
        return this;
    }
    
    public FinancialInstrumentVOBuilder currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }
    
    public FinancialInstrumentVOBuilder countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }
    

    public FinancialInstrumentVO build() {
    	FinancialInstrumentVO vo = new FinancialInstrumentVO();

    	vo.setId(id);
    	
    	vo.setInstrumentType(instrumentType);
    	
    	vo.setInstrumentTypeEnum(instrumentTypeEnum);
    	
    	vo.setCurrencyCode(currencyCode);
    	
    	vo.setCountryCode(countryCode);
  
        return vo;
    }

}
