package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.user.AccountCriteriaVO;
import com.jptest.user.AccountLoadVO;
import com.jptest.user.AccountPartyRelCriteriaVO;
import com.jptest.user.AddressCriteriaVO;
import com.jptest.user.EmailCriteriaVO;
import com.jptest.user.LoadUserDataRequest;
import com.jptest.user.LoadUserDataResponse;
import com.jptest.user.ModifyProductConfigVO;
import com.jptest.user.ModifyUserDataRequest;
import com.jptest.user.ModifyUserDataResponse;
import com.jptest.user.PartyCriteriaVO;
import com.jptest.user.ProductConfigCustomLayerType;
import com.jptest.user.ProductConfigValueVO;
import com.jptest.user.UserLifecycle;
import com.jptest.user.UserRead;


/**
 * Represents bridge for userlifecycleserv API calls
 */
@Singleton
public class UserLifecycleServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLifecycleServBridge.class);

    private static final Integer MAX_RESULTS = Integer.valueOf(50);

    @Inject
    @Named("userlifecycleserv")
    private UserRead userReadClient;

    @Inject
    @Named("userlifecycleserv")
    private UserLifecycle userLifeCycleClient;

    /**
     * Utility method that takes account number as input and calls userRead operation on it
     * <p>
     * This will work for many standard cases. For more specific use case, use
     * {@link #loadUserData(LoadUserDataRequest)}
     *
     * @param accountNumber
     * @return
     */
    public LoadUserDataResponse loadUserData(final BigInteger accountNumber) {
        return this.loadUserData(this.buildRequest(accountNumber));
    }

    private LoadUserDataRequest buildRequest(final BigInteger accountNumber) {
        final LoadUserDataRequest request = new LoadUserDataRequest();

        request.setMaxResults(MAX_RESULTS);

        // account_load
        final AccountLoadVO acctLoadVO = new AccountLoadVO();
        acctLoadVO.setAccountNumber(Collections.singletonList(accountNumber));

        // account_criteria
        final AccountCriteriaVO accountCriteria = new AccountCriteriaVO();
        accountCriteria.setLoadAccount(Boolean.TRUE);

        final AddressCriteriaVO addressCriteria = new AddressCriteriaVO();
        addressCriteria.setLoadAddresses(true);
        addressCriteria.setFilterOutHidden(true);
        final PartyCriteriaVO partyCriteriaVO = new PartyCriteriaVO();
        partyCriteriaVO.setLoadParty(Boolean.TRUE);
        partyCriteriaVO.setAddressCriteria(addressCriteria);

        final AccountPartyRelCriteriaVO accountPartyRelCriteriaVO = new AccountPartyRelCriteriaVO();
        accountPartyRelCriteriaVO.setLoadPartyAccountRel(Boolean.TRUE);
        accountPartyRelCriteriaVO.setPartyCriteria(partyCriteriaVO);
        accountCriteria.setAccountPartyRelationshipCriteria(accountPartyRelCriteriaVO);
        final EmailCriteriaVO emailCriteria = new EmailCriteriaVO();
        emailCriteria.setLoadEmail(Boolean.TRUE);
        partyCriteriaVO.setEmailCriteria(emailCriteria);

        acctLoadVO.setAccountCriteria(accountCriteria);

        request.setAccountLoad(acctLoadVO);

        return request;
    }

    /**
     * Expects client to build LoadUserDataRequest.
     * <p>
     * For generic usecase when you have account number, use {@link #loadUserData(BigInteger)}
     *
     * @param request
     * @return
     */
    public LoadUserDataResponse loadUserData(final LoadUserDataRequest request) {
        LOGGER.info("load_user_data request {}:", printValueObject(request));
        final LoadUserDataResponse response = this.userReadClient.load_user_data(request);
        LOGGER.info("load_user_data response {}:", printValueObject(response));
        return response;
    }

    /**
     * modify_user_data will create dccbuyer for the given request
     */
    public ModifyUserDataResponse createAnonymousDCCBuyer(final ModifyUserDataRequest request) {

        LOGGER.info("modify_user_data {}:", printValueObject(request));
        final ModifyUserDataResponse response = this.userLifeCycleClient.modify_user_data(request);
        LOGGER.info("modify_user_data {}:", printValueObject(response));
        LOGGER.info("DCC_Buyer_AccountNumber {}:", response.getAccount().getAccountNumber());
        return response;
    }

    /**
     * Utility method that takes account number, overageRefundPercentage and overageRefundAboveTransactionAmount as
     * input and calls modifyUserData on userLifecycleClient.
     * <p>
     * This will work for many standard cases. For more specific use case, use
     * {@link #modifyUserData(ModifyUserDataRequest)}
     *
     * @param accountNumber
     * @param overageRefundPercentage
     * @param overageRefundAboveTransactionAmount
     * @return ModifyUserDataResponse
     */
    public ModifyUserDataResponse enableOverageRefund(final BigInteger accountNumber, final String overageRefundPercentage,
            final String overageRefundAboveTransactionAmount) {
        return this.modifyUserData(
                this.buildRequest(accountNumber, overageRefundPercentage, overageRefundAboveTransactionAmount));
    }

    private ModifyUserDataRequest buildRequest(final BigInteger accountNumber, final String overageRefundPercentage,
            final String overageRefundAboveTransactionAmount) {
        final ModifyUserDataRequest modifyUserDataRequest = new ModifyUserDataRequest();

        final List<ModifyProductConfigVO> modifyProductConfigVOList = new ArrayList<ModifyProductConfigVO>();
        final ModifyProductConfigVO modifyProductConfigVO = new ModifyProductConfigVO();
        final List<ProductConfigValueVO> productConfigVOList = new ArrayList<ProductConfigValueVO>();
        modifyProductConfigVO.setTypeOfLayerToModify(ProductConfigCustomLayerType.ADMIN_OVERRIDE);
        final ProductConfigValueVO overageRefundPercentageVO = new ProductConfigValueVO();
        final ProductConfigValueVO overageRefundaboveTransactionAmountVO = new ProductConfigValueVO();
        overageRefundPercentageVO.setName("refund_above_txn_percentage");
        overageRefundaboveTransactionAmountVO.setName("refund_above_txn_amount");
        overageRefundPercentageVO.setValue(overageRefundPercentage);
        overageRefundaboveTransactionAmountVO.setValue(overageRefundAboveTransactionAmount);
        productConfigVOList.add(overageRefundPercentageVO);
        productConfigVOList.add(overageRefundaboveTransactionAmountVO);
        modifyProductConfigVO.setSetConfigs(productConfigVOList);
        modifyProductConfigVOList.add(modifyProductConfigVO);
        modifyUserDataRequest.setModifyProductConfig(modifyProductConfigVOList);
        modifyUserDataRequest.setAccountNumber(accountNumber);

        return modifyUserDataRequest;
    }

    /**
     * Expects client to build ModifyUserDataRequest.
     * <p>
     * For generic usecase when you have account number, use {@link #modifyUserData(BigInteger)}
     *
     * @param request
     * @return
     */
    public ModifyUserDataResponse modifyUserData(final ModifyUserDataRequest request) {
        LOGGER.info("modify_user_data request {}:", printValueObject(request));
        final ModifyUserDataResponse response = this.userLifeCycleClient.modify_user_data(request);
        LOGGER.info("modify_user_data response {}:", printValueObject(response));
        return response;
    }

}
