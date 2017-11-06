package com.jptest.payments.fulfillment.testonia.business.vo.money;

import java.util.List;

import com.jptest.merchant.BusinessCategoryVO;
import com.jptest.merchant.UserIndustryVO;
import com.jptest.money.UserDetailsVO;
import com.jptest.payments.fulfillment.testonia.business.vo.ListWrappedVOBuilder;
import com.jptest.payments.fulfillment.testonia.business.vo.VOBuilder;
import com.jptest.user.AccountVO;
import com.jptest.user.AddressVO;
import com.jptest.user.AliasVO;
import com.jptest.user.EmailVO;
import com.jptest.user.PartyVO;
import com.jptest.user.PhoneVO;
import com.jptest.user.PublicCredentialVO;


public class UserDetailsVOBuilder extends ListWrappedVOBuilder<UserDetailsVO> {

    private VOBuilder<BusinessCategoryVO> businessCategory = BusinessCategoryVOBuilder.newBuilder();
    
    private VOBuilder<UserIndustryVO> userIndustry = UserIndustryVOBuilder.newBuilder();

    private AccountVO coreUserDetails;
    
    private List<PartyVO> partyDetails;
    
    private List<AddressVO> addresses;

    private List<AliasVO> aliasDetails;

    private List<EmailVO> emailDetails;
    
    private List<PublicCredentialVO> publicCredentials;
    
    private List<PhoneVO> phoneDetails;
        
    public static UserDetailsVOBuilder newBuilder() {
        return new UserDetailsVOBuilder();
    }

    public UserDetailsVOBuilder userAccountVO(AccountVO coreUserDetails) {
        this.coreUserDetails = coreUserDetails;
        return this;
    }
    
    public UserDetailsVOBuilder partyDetails(List<PartyVO> partyDetails) {
        this.partyDetails = partyDetails;
        return this;
    }
    
    public UserDetailsVOBuilder addresses(List<AddressVO> addresses) {
        this.addresses = addresses;
        return this;
    }
    
    public UserDetailsVOBuilder aliasDetails(List<AliasVO> aliasDetails) {
        this.aliasDetails = aliasDetails;
        return this;
    }
    
    public UserDetailsVOBuilder emailDetails(List<EmailVO> emailDetails) {
        this.emailDetails = emailDetails;
        return this;
    }
    
    public UserDetailsVOBuilder publicCredentials(List<PublicCredentialVO> publicCredentials) {
        this.publicCredentials = publicCredentials;
        return this;
    }
    
    public UserDetailsVOBuilder phoneDetails(List<PhoneVO> phoneDetails) {
        this.phoneDetails = phoneDetails;
        return this;
    }
    
    public UserDetailsVOBuilder businessCategory(VOBuilder<BusinessCategoryVO> builder) {
    	businessCategory = builder;
        return this;
    }
    
    public UserDetailsVOBuilder userIndustry(VOBuilder<UserIndustryVO> builder) {
    	userIndustry = builder;
        return this;
    }
    

    public UserDetailsVO build() {
    	UserDetailsVO vo = new UserDetailsVO();

    	vo.setCoreUserDetails(coreUserDetails);
    	
    	vo.setPartyDetails(partyDetails);
    	
    	vo.setAddresses(addresses);
    	
    	vo.setAliasDetails(aliasDetails);
    	
    	vo.setEmailDetails(emailDetails);
    	
    	vo.setPublicCredentials(publicCredentials);
    	
    	vo.setPhoneDetails(phoneDetails);
    	
    	vo.setBusinessCategory(businessCategory.build());
    	 
    	vo.setUserIndustry(userIndustry.build());

        return vo;
    }

}
