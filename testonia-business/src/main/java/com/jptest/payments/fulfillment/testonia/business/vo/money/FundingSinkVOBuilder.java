package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.FundingSinkVO;
import com.jptest.money.InstrumentIdentifierVO;
import com.jptest.money.UserIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;


public class FundingSinkVOBuilder extends ListWrappedVOBuilder<FundingSinkVO> {

    private VOBuilder<UserIdentifierVO> userIdentifier = UserIndentifierVOBuilder.newBuilder();
    
    private VOBuilder<InstrumentIdentifierVO> instrumentIdentifer = InstrumentIdentifierVOBuilder.newBuilder();

    private Currency totalFundsOut;
        
    public static FundingSinkVOBuilder newBuilder() {
        return new FundingSinkVOBuilder();
    }

    public FundingSinkVOBuilder totalFundsOut(Currency totalFundsOut) {
        this.totalFundsOut = totalFundsOut;
        return this;
    }
    
    public FundingSinkVOBuilder userIdentifier(VOBuilder<UserIdentifierVO> builder) {
    	userIdentifier = builder;
        return this;
    }
    
    public FundingSinkVOBuilder instrumentIdentifer(VOBuilder<InstrumentIdentifierVO> builder) {
    	instrumentIdentifer = builder;
        return this;
    }
    

    public FundingSinkVO build() {
    	FundingSinkVO vo = new FundingSinkVO();

    	vo.setTotalFundsOut(totalFundsOut);
    	
    	vo.setUser(userIdentifier.build());
    	
    	vo.setInstrument(instrumentIdentifer.build());

        return vo;
    }

}
