package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.money.IntegrationType;
import com.jptest.money.PlanningContextVO;
import com.jptest.money.ProductFamilyType;
import com.jptest.money.ProductType;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;


public class PlanningContextVOBuilder extends ListWrappedVOBuilder<PlanningContextVO> {


    private IntegrationType integrationType;
    
    private ProductFamilyType productFamily;
    
    private ProductType productType;

        
    public static PlanningContextVOBuilder newBuilder() {
        return new PlanningContextVOBuilder();
    }

    public PlanningContextVOBuilder integrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
        return this;
    }
    
    public PlanningContextVOBuilder productFamily(ProductFamilyType productFamily) {
        this.productFamily = productFamily;
        return this;
    }
    
    public PlanningContextVOBuilder productType(ProductType productType) {
    	this.productType = productType;
        return this;
    }

    public PlanningContextVO build() {
    	PlanningContextVO vo = new PlanningContextVO();

    	vo.setIntegrationType(integrationType);
    	
    	vo.setProductFamily(productFamily);
    	
    	vo.setProduct(productType);

        return vo;
    }

}
