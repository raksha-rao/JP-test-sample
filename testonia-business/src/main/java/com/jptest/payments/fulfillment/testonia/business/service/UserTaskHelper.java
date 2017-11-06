package com.jptest.payments.fulfillment.testonia.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.configuration.Configuration;

import com.google.common.collect.Lists;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.Category;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.CategoryCollection;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.Group;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.Preference;
import com.jptest.business.merchantsettingserv.resource.merchantpreferences.Subgroup;
import com.jptest.payments.fulfillment.testonia.bridge.UserBridge;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WNotifyAddressDAO;
import com.jptest.payments.fulfillment.testonia.model.risk.RiskHoldType;
import com.jptest.payments.fulfillment.testonia.model.user.UserFlags;
import com.jptest.payments.fulfillment.testonia.model.user.UserPreferenceData;
import com.jptest.payments.fulfillment.testonia.model.user.UserPreferences;
import com.jptest.qi.rest.domain.pojo.UserFlag;
import com.jptest.qi.rest.domain.pojo.UserFlagList;

/**
 * Helper class that can be used for convenience methods related to user domain.
 */

@Singleton
public class UserTaskHelper {

    private static final String PREF_VALUE_KEY = "RECEIVING_PREF_VALUE";
    
    private static final String ALH_EMAIL_KEY = "alh-alfa0";
    
    private static final String IPR_EMAIL_KEY = "postauth-hold";

    @Inject
    private WNotifyAddressDAO notifyAddDAO;

    @Inject
    private UserBridge userBridge;

    @Inject
    private Configuration config;

    public UserFlag getUserFlag(String flagName) {
        try {
            UserFlags userFlagsEnum = UserFlags.valueOf(flagName);
            return new UserFlag(userFlagsEnum.name(), userFlagsEnum.getVal(), userFlagsEnum.getGroup());
        } catch (Exception e) {
            throw new TestExecutionException(
                    "This flag is either not valid or is not configured in testonia " + flagName, e);
        }
    }

    public UserFlagList getUserFlagList (String userFlagName) {
        List<String> flags = new ArrayList<>();
        flags.add(userFlagName);
        List<UserFlag> userFlags = new ArrayList<>(flags.size());
        for (String flag : flags) {
            userFlags.add(getUserFlag(flag));
        }
        return new UserFlagList(userFlags);
    }

    public void addIPNtoUser(String accountNumber) {
        String ipnURL = config.getString(ContextKeys.IPN_URL.getName());
        if (notifyAddDAO.selectIPNURL(accountNumber) != null)
            notifyAddDAO.updateIPNURL(ipnURL, accountNumber);
        else
            notifyAddDAO.insertIPNURL(ipnURL, accountNumber);
    }

    public void addIPNFlagtoUser(String accountNumber) {
        UserFlagList usrFlagList = getUserFlagList(UserFlags.WUSER_FLAG_IPN_ENABLED.name());
        userBridge.setUserFlags(accountNumber, usrFlagList);
    }

    public String getEmailForRiskHold(String riskHoldType, String partyType) {
        String optionalEmailString = null;
        if (RiskHoldType.ALH.getName().equals(riskHoldType)) {
            optionalEmailString = ALH_EMAIL_KEY;
        }
        else if (RiskHoldType.IPR.getName().equals(riskHoldType)) {
            optionalEmailString = IPR_EMAIL_KEY;
        }
        String email = optionalEmailString + "-" + System.nanoTime() + "-" + partyType + "-" + "@jptest.com";
        return email;
    }

    public UserPreferences getUserPreference(String prefName) {
        try {
            UserPreferences userPrefEnum = UserPreferences.valueOf(prefName);
            return userPrefEnum;
        } catch (Exception e) {
            throw new TestExecutionException(
                    "This preference is either not valid or is not configured in testonia " + prefName, e);
        }
    }

    /**
     * Builds the {@link CategoryCollection} instance representing the preference data that needs to be updated based on
     * the input.
     * 
     * @param userPreferenceData
     * @param accountNumber
     * @return
     */
    public CategoryCollection getPreferenceCategoryCollection(List<UserPreferenceData> userPreferenceData,
            String accountNumber) {
        if (CollectionUtils.isEmpty(userPreferenceData))
            return null;
        
        CategoryCollection categoryCollection = new CategoryCollection();
        List<Category> categories = Lists.newArrayList();
        for (UserPreferenceData prefData : userPreferenceData) {

            UserPreferences userPreference = prefData.getUserPreference();
            Category category = getCategoryByName(categories, userPreference);
            if (category == null) {
                category = getCategory(userPreference);
                categories.add(category);
            }

            Group group = getGroupByName(userPreference, category);
            if (group == null) {
                group = getGroup(userPreference.getGroup());
                category.getGroups().add(group);
            }
          if(prefData.getUserPreference().isSubGroup())
        	  group.getSubgroups().add(getSubGroup(userPreference.name(), PREF_VALUE_KEY, prefData.getValue().name()));
                
          else
        	  group.getPreferences().add(getPreference(userPreference.name(), prefData.getValue().name(), Preference.Status.A));
            
            
            
        }
        categoryCollection.setCategories(categories);
        return categoryCollection;
    }

    private Category getCategory(UserPreferences userPreference) {
        Category category = new Category();
        category.setName(userPreference.getCategory());
        category.setGroups(Lists.newArrayList());
        return category;
    }

    private Group getGroupByName(UserPreferences userPreference, Category category) {
        List<Group> groups = category.getGroups();
        if (CollectionUtils.isNotEmpty(groups)) {
            for (Group grp : groups) {
                if (grp.getName().equalsIgnoreCase(userPreference.getGroup()))
                    return grp;
            }
        }
        return null;
    }

    private Category getCategoryByName(List<Category> categories, UserPreferences userPreference) {
        for (Category cat : categories) {
            if (cat.getName().equalsIgnoreCase(userPreference.getCategory()))
                return cat;
        }
        return null;
    }

    private Group getGroup(String groupName) {
        Group group = new Group();
        group.setName(groupName);
        group.setSubgroups(Lists.newArrayList());
        return group;
    }

    private Subgroup getSubGroup(String prefName, String prefValueKey, String prefValue) {
        Subgroup subGroup = new Subgroup();
        subGroup.setName(prefName);
        Preference preference = getPreference(prefValueKey, prefValue);
        List<Preference> preferences = Lists.newArrayList(preference);
        subGroup.setPreferences(preferences);
        return subGroup;
    }

    private Preference getPreference(String prefValueKey, String prefValue) {
        Preference preference = new Preference();
        preference.setName(prefValueKey);
        preference.setValue(prefValue);
        return preference;
    }
    private Preference getPreference(String prefValueKey, String prefValue, Preference.Status status) {
        Preference preference = new Preference();
        preference.setName(prefValueKey);
        preference.setValue(prefValue);
        if (Objects.nonNull(status))
            preference.setStatus(status);
        return preference;
    }
}
