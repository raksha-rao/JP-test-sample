package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.math.BigInteger;

import com.jptest.money.UserDetailsVO;
import com.jptest.money.UserIdentifierVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;


public class UserIndentifierVOBuilder extends ListWrappedVOBuilder<UserIdentifierVO> {

    private VOBuilder<UserDetailsVO> userDetails = UserDetailsVOBuilder.newBuilder();

    private BigInteger accountNumber;
    
    private String emailAddress;
        
    public static UserIndentifierVOBuilder newBuilder() {
        return new UserIndentifierVOBuilder();
    }

    public UserIndentifierVOBuilder accountNumber(BigInteger accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }
    
    public UserIndentifierVOBuilder emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }
    
    public UserIndentifierVOBuilder userDetails(VOBuilder<UserDetailsVO> builder) {
    	userDetails = builder;
        return this;
    }

    public UserIdentifierVO build() {
    	UserIdentifierVO vo = new UserIdentifierVO();

    	vo.setAccountNumber(accountNumber);
    	
    	vo.setEmailAddress(emailAddress);
    	
    	vo.setUserDetails(userDetails.build());

        return vo;
    }

}
