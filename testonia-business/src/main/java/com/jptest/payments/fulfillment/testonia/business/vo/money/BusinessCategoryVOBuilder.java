package com.jptest.payments.fulfillment.testonia.business.vo.money;

import com.jptest.merchant.BusinessCategoryVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;


public class BusinessCategoryVOBuilder extends ListWrappedVOBuilder<BusinessCategoryVO> {

    private String businessCategory;

    private String businessSubCategory;

    private Integer mccCode;

    public static BusinessCategoryVOBuilder newBuilder() {
        return new BusinessCategoryVOBuilder();
    }

    public BusinessCategoryVOBuilder businessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
        return this;
    }
    
    public BusinessCategoryVOBuilder businessSubCategory(String businessSubCategory) {
        this.businessSubCategory = businessSubCategory;
        return this;
    }
    
    public BusinessCategoryVOBuilder mccCode(Integer mccCode) {
        this.mccCode = mccCode;
        return this;
    }

    public BusinessCategoryVO build() {
    	BusinessCategoryVO vo = new BusinessCategoryVO();

        vo.setBusinessCategory(businessCategory);

        vo.setBusinessSubcategory(businessSubCategory);

        vo.setMccCode(mccCode);
        
        return vo;
    }

}
