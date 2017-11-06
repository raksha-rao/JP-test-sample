package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.merchant.UserIndustryVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;


public class UserIndustryVOBuilder extends ListWrappedVOBuilder<UserIndustryVO> {

    private BigInteger id;

    private BigInteger flags;

    private BigInteger accountNumber;
    
    private BigInteger industry;

    private BigInteger subIndustry;

    private Long dateReviewed;
    
    private String memo;
    
    private BigInteger userIndustry;

    private BigInteger userSubIndustry;


    public static UserIndustryVOBuilder newBuilder() {
        return new UserIndustryVOBuilder();
    }

    public UserIndustryVOBuilder id(BigInteger id) {
        this.id = id;
        return this;
    }
    
    public UserIndustryVOBuilder flags(BigInteger flags) {
        this.flags = flags;
        return this;
    }
    
    public UserIndustryVOBuilder accountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
    
    public UserIndustryVOBuilder industry(BigInteger industry) {
        this.industry = industry;
        return this;
    }
    
    public UserIndustryVOBuilder subIndustry(BigInteger subIndustry) {
        this.subIndustry = subIndustry;
        return this;
    }
    
    public UserIndustryVOBuilder dateReviewed(Long dateReviewed) {
        this.dateReviewed = dateReviewed;
        return this;
    }
    
    public UserIndustryVOBuilder memo(String memo) {
        this.memo = memo;
        return this;
    }
    
    public UserIndustryVOBuilder userIndustry(BigInteger userIndustry) {
        this.userIndustry = userIndustry;
        return this;
    }
    
    public UserIndustryVOBuilder userSubIndustry(BigInteger userSubIndustry) {
        this.userSubIndustry = userSubIndustry;
        return this;
    }

    public UserIndustryVO build() {
    	UserIndustryVO vo = new UserIndustryVO();

    	vo.setId(id);
    	
    	vo.setFlags(flags);
    	
    	vo.setAccountNumber(accountNumber);
    	
    	vo.setIndustry(industry);
    	
    	vo.setSubindustry(subIndustry);
    	
    	vo.setDateReviewed(dateReviewed);
    	
    	vo.setMemo(memo);
    	
    	vo.setUserIndustry(userIndustry);
    	
    	vo.setUserSubindustry(userSubIndustry);
    	
        return vo;
    }

}
