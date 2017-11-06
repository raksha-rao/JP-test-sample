package com.jptest.payments.fulfillment.testonia.bridge;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.CategoryCollection;
import com.jptest.payments.fulfillment.testonia.bridge.resource.MerchantPreferenceResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.UserServiceResource;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.CoreServiceUtil;
import com.jptest.platform.security.securitycontext.SecurityContext;
import com.jptest.platform.security.securitycontext.Subject;
import com.jptest.platform.test.rest.AutoCloseableResponseWrapper;
import com.jptest.qi.rest.domain.pojo.Apicredential;
import com.jptest.qi.rest.domain.pojo.Bank;
import com.jptest.qi.rest.domain.pojo.BuyerCredit;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.qi.rest.domain.pojo.Fund;
import com.jptest.qi.rest.domain.pojo.FundList;
import com.jptest.qi.rest.domain.pojo.RestError;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.qi.rest.domain.pojo.UserFlagList;
import com.jptest.qi.rest.domain.pojo.YouthAccount;
import com.jptest.qi.rest.service.BuyerCreditService;
import com.jptest.qi.rest.service.YouthAccountService;
import com.jptest.qi.rest.service.useroperations.APICredentialService;
import com.jptest.qi.rest.service.useroperations.FundService;
import com.jptest.qi.rest.service.useroperations.UserFlagService;
import com.jptest.sv.api.rest.util.SecurityContextUtil;

/**
 * Represents a single point for all external calls that relate to operations concerning user (buyer or seller)
 * from Testonia.
 * 
 */
@Singleton
public class UserBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBridge.class);

    @Inject
    @Named("userservice")
    private UserServiceResource userService;

    @Inject
    @Named("youthaccountservice")
    private YouthAccountService youthAccountService;

    @Inject
    @Named("userflagservice")
    private UserFlagService userFlagService;

    @Inject
    @Named("userfundservice")
    private FundService fundService;

    @Inject
    @Named("buyercreditservice")
    private BuyerCreditService buyerCreditService;

    @Inject
    @Named("merchantpreferenceresource")
    private MerchantPreferenceResource merchantPreferences;

    @Inject
    @Named("restjaws")
    private APICredentialService apiCredentialService;

    @Inject
    private CoreServiceUtil coreServiceUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    public UserBridge() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public User createUser(User userInputData) {
        logRequest(userInputData);

        Response response = userService.createUser(coreServiceUtil.getTopCorrelationId(),
                coreServiceUtil.getTargetStage(), userInputData);
        User actualUser = getEntity(response, User.class);
        // lets fail user creation if adding bank fails
        validateBank(actualUser);
        // lets fail user creation if adding creditcard fails
        validateCreditCard(actualUser);
        // lets fail user creation if adding funds fails
        validateFund(actualUser);
        return actualUser;
    }

    private void validateBank(User actualUser) {
        if (CollectionUtils.isNotEmpty(actualUser.getBank())) {
            for (Bank bank : actualUser.getBank()) {
                if (bank.getRestError() != null) {
                    LOGGER.error("User creation failed while adding bank '{}' with error {}", bank.getBankName(),
                            bank.getRestError());
                    throw new TestExecutionException("User creation failed while adding bank");
                }
            }
        }
    }

    private void validateCreditCard(User actualUser) {
        if (CollectionUtils.isNotEmpty(actualUser.getCreditcard())) {
            for (Creditcard creditcard : actualUser.getCreditcard()) {
                if (creditcard.getRestError() != null) {
                    LOGGER.error("User creation failed while adding creditcard '{}' with error {}",
                            creditcard.getCardType(),
                            creditcard.getRestError());
                    throw new TestExecutionException("User creation failed while adding creditCard");
                }
            }
        }
    }

    private void validateFund(User actualUser) {
        if (CollectionUtils.isNotEmpty(actualUser.getFund())) {
            for (Fund fund : actualUser.getFund()) {
                if (fund.getRestError() != null) {
                    LOGGER.error("User creation failed while adding fund with error {}",
                            fund.getRestError());
                    throw new TestExecutionException("User creation failed while adding fund.");
                }
            }
        }
    }

    public YouthAccount createYouthAccount(YouthAccount youthAccount) {
        Response response = youthAccountService.createYouthAccount(coreServiceUtil.getTargetStage(), youthAccount);
        YouthAccount actualAccount = getEntity(response, YouthAccount.class);
        return actualAccount;
    }

    public void setUserFlags(String userAccountNumber, UserFlagList flagList) {
        Response response = userFlagService.setFlags(coreServiceUtil.getTargetStage(), userAccountNumber, flagList);
        assertResponseForSuccess(userAccountNumber, response);
    }

    public void clearFlags(String userAccountNumber, UserFlagList flagList) {
        Response response = userFlagService.clearFlags(coreServiceUtil.getTargetStage(), userAccountNumber,
                flagList);
        assertResponseForSuccess(userAccountNumber, response);
    }

    public void addFundToUser(String userAccountNumber, Fund... fundToAdd) {
        String stageName = coreServiceUtil.getTargetStage();
        Response response = fundService.addFund(stageName, userAccountNumber,
                new FundList(Arrays.asList(fundToAdd), null));
        assertResponseForSuccess(userAccountNumber, response);
    }

    public void enableBuyerCredit(String accountNumber, BuyerCredit buyerCredit) {
        Response response = buyerCreditService.enableBuyerCredit(coreServiceUtil.getTargetStage(), accountNumber,
                buyerCredit);
        assertResponseForSuccess(accountNumber, response);
    }

    /**
     * Returns user object from input accountNumber. If the fullProfile is set to true, 
     * this API will load the bank and credit cards associated with the user.
     * 
     * @param accountNumber
     * @param fullProfile - Enabling this parameter allows to load the bank accounts and credit card for the user
     * @return
     */
    public User getUser(String accountNumber, boolean fullProfile) {
        Response response = userService.getUser(coreServiceUtil.getTopCorrelationId(), coreServiceUtil.getTargetStage(),
                accountNumber, fullProfile);
        return getEntity(response, User.class);
    }

    /**
     * Returns user object from input emailAddress
     * @param emailAddress
     * @return
     */
    public User getUserByEmailAddress(String emailAddress) {
        Response response = userService.getUserByEmailAddress(coreServiceUtil.getTopCorrelationId(),
                coreServiceUtil.getTargetStage(), emailAddress);
        return getEntity(response, User.class);
    }

    public void addPreferenceToUser(CategoryCollection categoryCollection, String accountNumber) {

        try {
            SecurityContext securityContext = getSecurityContext(accountNumber);
            Response res = merchantPreferences.addMerchantPreferences(
                    SecurityContextUtil.serializeSecurityContext(securityContext), categoryCollection);
            assertResponseForSuccess(accountNumber, res);
        } catch (Exception e) {
            throw new TestExecutionException("Failed adding a preference to the user ", e);

        }
    }

    /**
     * Generates and adds the API credentials for the user represented by the account number in the argument.
     * Returns {@link Apicredential} instance with the generated info.
     * @param accountNumber
     */
    public Apicredential generateAPICredentials(String accountNumber) {
        Response response = this.apiCredentialService.addAPICredentials(coreServiceUtil.getTargetStage(),
                accountNumber);
        Apicredential creds = getEntity(response, Apicredential.class);
        return creds;
    }

    /**
     * @param acctNumber
     * @return
     */
    private SecurityContext getSecurityContext(String acctNumber) {
        SecurityContext context = new SecurityContext();
        com.jptest.platform.security.securitycontext.User actor = getSecurityContextUser(acctNumber);
        context.setActor(actor);
        Subject subject = new Subject();
        subject.setSubject(actor);
        context.setSubjects(Lists.newArrayList(subject));
        return context;
    }

    private com.jptest.platform.security.securitycontext.User getSecurityContextUser(String acctNumber) {
        com.jptest.platform.security.securitycontext.User actor = new com.jptest.platform.security.securitycontext.User();
        actor.setAccountNumber(acctNumber);
        actor.setPartyId(acctNumber);
        actor.setAuthClaims(Lists.newArrayList("USERNAME", "PASSWORD"));
        actor.setUserType("MERCHANT");
        actor.setAuthState("LOGGEDIN");
        return actor;
    }

    private void assertResponseForSuccess(String userAccountNumber, Response response) {
        try (AutoCloseableResponseWrapper closeable = new AutoCloseableResponseWrapper(response)) {
            if (Response.Status.Family.SUCCESSFUL == Response.Status.Family.familyOf(response.getStatus())) {
                LOGGER.info("Successfully finished the operation for account number {} ",
                        userAccountNumber);
            } else {
                handleErrorResponse(response);
            }
        } catch (Exception e) {
            throw new TestExecutionException(e);
        }
    }

    private <T> T getEntity(Response response, Class<T> t) {
        T entity = null;
        try (AutoCloseableResponseWrapper closeable = new AutoCloseableResponseWrapper(response)) {
            if (Response.Status.Family.SUCCESSFUL == Response.Status.Family.familyOf(response.getStatus())) {
                if (!response.hasEntity())
                    throw new TestExecutionException("getEntity failed for class: " + t.getSimpleName());
                String output = response.readEntity(String.class);
                LOGGER.info("User creation response: {}", output);
                entity = objectMapper.readValue(output, t);
            } else {
                handleErrorResponse(response);
            }
        } catch (TestExecutionException e) {
            throw e;
        } catch (Exception e) {
            throw new TestExecutionException(e);
        }
        return entity;
    }

    private void handleErrorResponse(Response response) {
        RestError error = response.readEntity(RestError.class);
        String message = "Error performing user operation; errorCode: " + error.getErrorCode()
                + " message: " + error.getErrorMessage();
        throw new TestExecutionException(message);
    }

    private void logRequest(Object obj) {
        try {
            String request = objectMapper.writeValueAsString(obj);
            LOGGER.info("User creation request: {}", request);
        } catch (Exception e) {
            LOGGER.error("Unable to log user creation request", e);
        }
    }

}
