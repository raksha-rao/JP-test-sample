package com.jptest.payments.fulfillment.testonia.model.user;

/**
 * Represents a user preference name and value as specified by user.
 */
public class UserPreferenceData {
    private UserPreferences userPreference;
    private UserPreferenceValues value;

    public UserPreferences getUserPreference() {
        return userPreference;
    }

    public UserPreferenceValues getValue() {
        return value;
    }

    public UserPreferenceData(UserPreferences userPreference, UserPreferenceValues value) {
        super();
        this.userPreference = userPreference;
        this.value = value;
    }

}
