package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.FinancialInstrumentVO;
import com.jptest.financialinstrument.WalletInstrumentVO;
import com.jptest.money.InstrumentIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class InstrumentIdentifierVOBuilder extends ListWrappedVOBuilder<InstrumentIdentifierVO> {

    private VOBuilder<FinancialInstrumentVO> financialInstrument = FinancialInstrumentVOBuilder.newBuilder();

    private String instrumentId;
    
    private FinancialInstrumentTypeClass instrumentType;

    private WalletInstrumentVO walletInstrumentId;
        
    public static InstrumentIdentifierVOBuilder newBuilder() {
        return new InstrumentIdentifierVOBuilder();
    }

    public InstrumentIdentifierVOBuilder instrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
        return this;
    }
    
    public InstrumentIdentifierVOBuilder instrumentType(FinancialInstrumentTypeClass instrumentType) {
        this.instrumentType = instrumentType;
        return this;
    }
    
    public InstrumentIdentifierVOBuilder walletInstrumentId(WalletInstrumentVO walletInstrumentId) {
        this.walletInstrumentId = walletInstrumentId;
        return this;
    }
    
    public InstrumentIdentifierVOBuilder financialInstrument(VOBuilder<FinancialInstrumentVO> builder) {
    	financialInstrument = builder;
        return this;
    }
    

    public InstrumentIdentifierVO build() {
    	InstrumentIdentifierVO vo = new InstrumentIdentifierVO();
    	
    	vo.setInstrumentId(instrumentId);
    	
    	vo.setInstrumentType(instrumentType);
    	
    	vo.setWalletInstrumentId(walletInstrumentId);
    	
    	vo.setInstrumentDetails(financialInstrument.build());

        return vo;
    }

}
