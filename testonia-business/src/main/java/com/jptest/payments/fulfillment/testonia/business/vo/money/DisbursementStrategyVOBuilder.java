package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.DisbursementStrategyVO;
import com.jptest.money.FeePolicyVO;
import com.jptest.money.FulfillmentType;
import com.jptest.money.PlanningContextVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.types.Currency;


public class DisbursementStrategyVOBuilder extends ListWrappedVOBuilder<DisbursementStrategyVO> {

    private VOBuilder<PlanningContextVO> planningContext = PlanningContextVOBuilder.newBuilder();

    private String transactionHandle;
    
    private FulfillmentType fulfillmentType;
    
    private Currency disbursementAmount;
    
    private FeePolicyVO feePolicy;
    
    private BigInteger settlementAccountNumber;
        
    public static DisbursementStrategyVOBuilder newBuilder() {
        return new DisbursementStrategyVOBuilder();
    }

    public DisbursementStrategyVOBuilder transactionHandle(String transactionHandle) {
        this.transactionHandle = transactionHandle;
        return this;
    }
    
    public DisbursementStrategyVOBuilder fulfillmentType(FulfillmentType fulfillmentType) {
        this.fulfillmentType = fulfillmentType;
        return this;
    }
    
    public DisbursementStrategyVOBuilder disbursementAmount(Currency disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
        return this;
    }
    
    public DisbursementStrategyVOBuilder feePolicy(FeePolicyVO feePolicy) {
        this.feePolicy = feePolicy;
        return this;
    }
    
    public DisbursementStrategyVOBuilder settlementAccountNumber(BigInteger settlementAccountNumber) {
        this.settlementAccountNumber = settlementAccountNumber;
        return this;
    }
    
    public DisbursementStrategyVOBuilder planningContext(VOBuilder<PlanningContextVO> builder) {
    	planningContext = builder;
        return this;
    }

    public DisbursementStrategyVO build() {
    	DisbursementStrategyVO vo = new DisbursementStrategyVO();

    	vo.setTransactionHandle(transactionHandle);
    	
    	vo.setFulfillmentType(fulfillmentType);
    	
    	vo.setDisbursementAmount(disbursementAmount);
    	
    	vo.setFeePolicy(feePolicy);
    	
    	vo.setSettlementAccountNumber(settlementAccountNumber);
    	
    	vo.setPlanningContext(planningContext.build());

        return vo;
    }

}
