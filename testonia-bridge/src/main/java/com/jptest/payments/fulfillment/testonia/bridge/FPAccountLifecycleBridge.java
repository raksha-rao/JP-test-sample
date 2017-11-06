package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.name.Named;
import com.jptest.common.ActorInfoVO;
import com.jptest.financialproduct.AddAccountOwnerOrEntityRequest;
import com.jptest.financialproduct.AddAccountOwnerOrEntityResponse;
import com.jptest.financialproduct.CreateAccountRequest;
import com.jptest.financialproduct.CreateAccountResponse;
import com.jptest.financialproduct.FPAccountLifecycle;
import com.jptest.financialproduct.FPAccountOwnerType;
import com.jptest.financialproduct.FPAccountOwnerVO;
import com.jptest.financialproduct.FPAccountServiceContextVO;
import com.jptest.financialproduct.FPProduct;

/**
 * Represents bridge for FPAccountLifecycle API calls
 */
@Singleton
public class FPAccountLifecycleBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(FIWalletLifecycleServBridge.class);

    private static final String COUNTRY_GERMANY = "DE";

    @Inject
    @Named("fpaccountlifecycle")
    private FPAccountLifecycle fpAccountLifecycle;

    public CreateAccountResponse createPUIAccount(String accountNumber) {
        CreateAccountRequest request = generateCreateAccountRequestForPUI(accountNumber);
        LOGGER.info("create_account request: {}", printValueObject(request));
        CreateAccountResponse response = fpAccountLifecycle.create_account(request);
        LOGGER.info("create_account response: {}", printValueObject(response));
        return response;
    }

    /**
     * Method to generate the create account request from account number.
     *  
     * @param accountNumber
     * @return
     */
    private CreateAccountRequest generateCreateAccountRequestForPUI(String accountNumber) {
        CreateAccountRequest fpCreateAcctreq = new CreateAccountRequest();
        FPAccountOwnerVO accountVO = new FPAccountOwnerVO();
        FPAccountServiceContextVO fpAccountContextVo = new FPAccountServiceContextVO();

        ActorInfoVO actorInfoVO = new ActorInfoVO();
        actorInfoVO.setActorType((byte) 8);
        fpAccountContextVo.setActor(actorInfoVO);
        fpCreateAcctreq.setContext(fpAccountContextVo);

        accountVO.setOwnerType(FPAccountOwnerType.FPACCOUNT_OWNER_TYPE_USER);
        accountVO.setOwnerId(accountNumber);
        fpCreateAcctreq.setOwner(accountVO);

        fpCreateAcctreq.setFinancialProductType(FPProduct.FP_PRODUCT_PUI);

        fpCreateAcctreq.setCountryCode(COUNTRY_GERMANY);

        return fpCreateAcctreq;
    }

    public AddAccountOwnerOrEntityResponse addAccountOwnerOrEntity(String fpAccountId) {
        AddAccountOwnerOrEntityRequest request = generateAddVIBANRequest(fpAccountId);
        LOGGER.info("add_account_owner_or_entity request: {}", printValueObject(request));
        AddAccountOwnerOrEntityResponse response = fpAccountLifecycle.add_account_owner_or_entity(request);
        LOGGER.info("add_account_owner_or_entity response: {}", printValueObject(response));
        return response;
    }

    /**
     * This method is used to generate the VIBAN request.
     * 
     * @param fpaccountId
     * @return
     */
    private AddAccountOwnerOrEntityRequest generateAddVIBANRequest(
            String fpAccountId) {
        AddAccountOwnerOrEntityRequest fpAddAcctOwnerreq = new AddAccountOwnerOrEntityRequest();

        FPAccountServiceContextVO fpAccountContextVo = new FPAccountServiceContextVO();

        ActorInfoVO actorInfoVO = new ActorInfoVO();
        actorInfoVO.setActorType((byte) 2);
        fpAccountContextVo.setActor(actorInfoVO);

        fpAddAcctOwnerreq.setContext(fpAccountContextVo);
        fpAddAcctOwnerreq.setFpAccountId(fpAccountId);
        fpAddAcctOwnerreq.setGenerateVirtualAccountNumber(true);
        return fpAddAcctOwnerreq;
    }
}
