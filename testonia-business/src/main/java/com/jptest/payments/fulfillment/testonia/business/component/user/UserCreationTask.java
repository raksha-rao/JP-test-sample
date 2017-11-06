
package com.jptest.payments.fulfillment.testonia.business.component.user;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Lists;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.CategoryCollection;
import com.jptest.payments.fulfillment.testonia.bridge.ComplianceLifeCycleServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.FIBridge;
import com.jptest.payments.fulfillment.testonia.bridge.MsMonitorBridge;
import com.jptest.payments.fulfillment.testonia.bridge.RiskAdminServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.UserBridge;
import com.jptest.payments.fulfillment.testonia.business.component.RetriableBaseTask;
import com.jptest.payments.fulfillment.testonia.business.service.UserTaskHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.util.CoreServiceUtil;
import com.jptest.payments.fulfillment.testonia.model.UserCreationTaskInput;
import com.jptest.payments.fulfillment.testonia.model.UserCreationTaskInput.NameValue;
import com.jptest.payments.fulfillment.testonia.model.user.UserPreferenceData;
import com.jptest.payments.fulfillment.testonia.model.user.UserPreferenceValues;
import com.jptest.qi.rest.domain.pojo.Apicredential;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.qi.rest.domain.pojo.UserFlag;
import com.jptest.qi.rest.domain.pojo.UserFlagList;

public class UserCreationTask extends RetriableBaseTask<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCreationTask.class);

    @Inject
    private UserBridge userBridge;

    @Inject
    private UserTaskHelper userTaskHelper;

    @Inject
    private FIBridge fiBridge;

    @Inject
    private RiskAdminServBridge fmfBridge;

    @Inject
    private MsMonitorBridge msMonitorBridge;

    @Inject
    private ComplianceLifeCycleServBridge complianceLifeCycleServBridge;

    private UserCreationTaskInput input;

    @Inject
    private CoreServiceUtil coreServiceUtil;

    private static final String CACHE_LOOKUP_USER_RESULT = "CACHE_LOOKUP_USER_RESULT";

    private static final String ADD_USER_TO_CACHE_RESULT = "ADD_USER_TO_CACHE_RESULT";

    public UserCreationTask(UserCreationTaskInput input) {
        // To limit user creation attempts to 6 (every 20 seconds for 2 mins max)
        super(120 * 1000L, 20 * 1000L);
        this.input = input;
    }

    @Override
    protected boolean isDesiredOutput(User output) {
        // Fix to avoid IPNDataVO DB Diff caused when same virtual value is assigned to both first/last name when
        // identical.
        if (output != null && !output.getFirstName().equals(output.getLastName()))
            return true;
        else
            return false;
    }

    @Override
    protected User retriableExecute(Context context) {
        return processUserCreation(context);
    }

    @Override
    protected User onSuccess(Context context, User output) {
        return output;
    }

    @Override
    protected User onFailure(Context context, User output) {
        throw new UserCreationException("Failed during user creation");
    }

    private User processUserCreation(Context context) {
        try {
            User userInput = input.getUser();
            if (isEligibleForUsingCachedUser()) {
                User response = getUserFromCache(input);
                if (response != null) {
                    LOGGER.info("User cache lookup successful");
                    addUserCacheReportingData(context, CACHE_LOOKUP_USER_RESULT,
                            new UserCacheReportingData(input.getPlainTextHashKey(), input.generateKey(), true));
                    return response;
                }
                LOGGER.info("User cache lookup NOT successful, continuing with normal user creation");
            } else {
                LOGGER.warn("cached user mode is turned off or the target stage is not eligible for user caching.");
            }
            addUserCacheReportingData(context, CACHE_LOOKUP_USER_RESULT,
                    new UserCacheReportingData(input.getPlainTextHashKey(), input.generateKey(), false));
            preProcessInput(userInput);

            User actualUser = userBridge.createUser(userInput);
            LOGGER.info("Created User: {} {} with jptest account number: {}", actualUser.getFirstName(),
                    actualUser.getLastName(), actualUser.getAccountNumber());

            addOtherUserProperties(actualUser);

            // This step should be at the end no more if conditions after this
            if (config.getBoolean(CoreConfigKeys.CREATE_CACHED_USER.getName(), false)
                    && isStageEligibleForCachedUser()) {
                addUserToCache(actualUser, context);
            }

            return actualUser;
        } catch (RuntimeException e) {
            LOGGER.warn("User creation failed, it will be retried if possible");
        }

        return null;
    }

    private boolean isEligibleForUsingCachedUser() {
        return config.getBoolean(CoreConfigKeys.USE_CACHED_USER.getName(), false) && isStageEligibleForCachedUser();
    }

    private void addUserCacheReportingData(Context context, String key, UserCacheReportingData data) {
        List<UserCacheReportingData> dataList = (List<UserCacheReportingData>) context.getReportingAttributes()
                .putIfAbsent(key, new CopyOnWriteArrayList<UserCacheReportingData>());
        if (dataList == null) {
            dataList = (List<UserCacheReportingData>) context.getReportingAttributes().get(key);
        }
        dataList.add(data);
        context.addReportingAttribute(key, dataList);
    }

    /**
     * Currently only msmaster is eligible for caching users
     * since we can automatically flush the cache when DB is cleared.
     * @return
     */
    private boolean isStageEligibleForCachedUser() {
        String targetStage = coreServiceUtil.getTargetStage();
        return targetStage.startsWith("msmaster");
    }

    private User getUserFromCache(UserCreationTaskInput input) {
        LOGGER.info("Trying to use pre created users");
        User response = msMonitorBridge.getInstantUser(input);
        if (response != null && isCachedUserPresentInDB(response)) {
            return response;
        } else {
            return null;
        }
    }

    /**
     * @param response
     * @return
     */
    private boolean isCachedUserPresentInDB(User userFromCache) {
        try {
            User userFromDB = null;
            if (StringUtils.isNotEmpty(userFromCache.getAccountNumber())) {
                userFromDB = userBridge.getUser(userFromCache.getAccountNumber(), false);
            } else if (StringUtils.isNotEmpty(userFromCache.getEmailAddress())) {
                userFromDB = userBridge.getUserByEmailAddress(userFromCache.getEmailAddress());
            }
            if (userFromDB != null)
                return true;
            return false;
        } catch (Exception e) {
            LOGGER.warn("Cached user not found in the DB. Possibly need a cache clean up.", e);
            return false;
        }
    }

    private void addUserToCache(User actualUser, Context context) {
        LOGGER.info("In UserCreation Phase, so just persist the users in DB");
        Boolean isAdded = msMonitorBridge.addUser(input, actualUser);
        addUserCacheReportingData(context, ADD_USER_TO_CACHE_RESULT,
                new UserCacheReportingData(input.getPlainTextHashKey(), input.generateKey(), isAdded));
    }

    private void preProcessInput(User userInput) {
        processDebitCardInput(userInput);
        processRiskHoldInput(userInput);
    }

    private void processRiskHoldInput(User userInput) {
        if (input.getRiskHoldType() != null && input.getPartyType() != null) {
            String email = userTaskHelper.getEmailForRiskHold(input.getRiskHoldType(),
                    input.getPartyType().getName());
            userInput.setEmailAddress(email);
        }
    }

    private void processDebitCardInput(User userInput) {
        if (input.getDebitCardInfo() != null) {
            Creditcard cc = fiBridge.genrateCreditCard(input.getDebitCardInfo());
            List<Creditcard> listCC = userInput.getCreditcard();
            listCC.add(cc);
            userInput.setCreditcard(listCC);
        }
    }

    private void addOtherUserProperties(User actualUser) {
        if (CollectionUtils.isNotEmpty(input.getFlags())) {
            addFlagsToUser(actualUser, input.getFlags());
            LOGGER.info("successfully set flags for the user {} ", actualUser.getAccountNumber());
        }

        if (input.getBuyerCredit() != null) {
            userBridge.enableBuyerCredit(actualUser.getAccountNumber(), input.getBuyerCredit());
            LOGGER.info("successfully enabled buyer credit for the user {} ", actualUser.getAccountNumber());
        }

        if (CollectionUtils.isNotEmpty(input.getPreferences())) {
            addPreferencesToUser(input.getPreferences(), actualUser.getAccountNumber());
            LOGGER.info("successfully set preferences for the user {} ", actualUser.getAccountNumber());
        }

        if (input.isEnableIPN()) {
            userTaskHelper.addIPNtoUser(actualUser.getAccountNumber());
            LOGGER.info("successfully enabled IPN for seller {} ", actualUser.getAccountNumber());
            userTaskHelper.addIPNFlagtoUser(actualUser.getAccountNumber());
        }

        if (input.getFmfData() != null && CollectionUtils.isNotEmpty(input.getFmfData().getFmfFilters())) {
            fmfBridge.addFMFFilters(actualUser, input.getFmfData().getFmfFilters());
        }

        if (input.isGenerateAPICredentials()) {
            Apicredential apiCreds = userBridge.generateAPICredentials(actualUser.getAccountNumber());
            populateCredsInUser(actualUser, apiCreds);
        }

        if (input.isCipCompleted()) {
            complianceLifeCycleServBridge.updateCIPLevelsByCustomerID(actualUser.getAccountNumber());
            LOGGER.info("successfully completed CIP verification for user {} ", actualUser.getAccountNumber());
        }
    }

    /**
     * @param actualUser
     * @param apiCreds
     */
    private void populateCredsInUser(User actualUser, Apicredential apiCreds) {
        // We have no option but to use the depcrecated methods here till we figure out a way from rest
        // jaws team to have all user info in one user instance. We don't want to override, wrap, process user instance
        // at this point.
        actualUser.setApiUserName(apiCreds.getApiUserName());
        actualUser.setApiPassword(apiCreds.getApiPassword());
        actualUser.setApiSignature(apiCreds.getSignature());
    }

    /**
     * @param preferences
     * @param string
     */
    private void addPreferencesToUser(List<NameValue> preferences, String accountNumber) {
        CategoryCollection categoryCollection = userTaskHelper
                .getPreferenceCategoryCollection(getUserPreferences(preferences), accountNumber);
        userBridge.addPreferenceToUser(categoryCollection, accountNumber);
    }

    /**
     * @param preferences
     * @return
     */
    private List<UserPreferenceData> getUserPreferences(List<NameValue> preferences) {
        List<UserPreferenceData> prefDataList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(preferences)) {
            for (NameValue prefNameValue : preferences) {
                prefDataList.add(
                        new UserPreferenceData(userTaskHelper.getUserPreference(prefNameValue.getName()),
                                UserPreferenceValues.valueOf(prefNameValue.getValue())));
            }
        }
        return prefDataList;
    }

    /**
     * @param actualUser
     * @param flags
     */
    private void addFlagsToUser(User actualUser, List<String> flags) {
        UserFlagList flagList = getUserFlags(flags);
        userBridge.setUserFlags(actualUser.getAccountNumber(), flagList);
    }

    /**
     * @param flags
     * @return
     */
    private UserFlagList getUserFlags(List<String> flags) {
        List<UserFlag> userFlags = new ArrayList<>(flags.size());
        for (String flag : flags) {
            userFlags.add(userTaskHelper.getUserFlag(flag));
        }
        return new UserFlagList(userFlags);
    }

    @Override
    public boolean shouldParticipate(Context context) {
        // User creation should always take place unless and untill we find 
        // a scenario when it shouldn't 
        return true;
    }

    private static class UserCacheReportingData {
        private String plainTextCacheKey, hash;
        private Boolean cacheInteractionSuccessful;

        private UserCacheReportingData(String plainTextCacheKey, String hash, Boolean cacheInteractionSuccessful) {
            super();
            this.plainTextCacheKey = plainTextCacheKey;
            this.hash = hash;
            this.cacheInteractionSuccessful = cacheInteractionSuccessful;
        }

        private String getPlainTextCacheKey() {
            return plainTextCacheKey;
        }

        private String getHash() {
            return hash;
        }

        private Boolean getCacheInteractionSuccessful() {
            return cacheInteractionSuccessful;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("Cache key : ").append(plainTextCacheKey).append(" hash : ")
                    .append(hash).append(" cacheInteractionSuccessful : ").append(cacheInteractionSuccessful);
            return sb.toString();
        }

    }

}
