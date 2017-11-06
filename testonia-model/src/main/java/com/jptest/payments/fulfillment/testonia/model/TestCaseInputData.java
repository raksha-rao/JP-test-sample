package com.jptest.payments.fulfillment.testonia.model;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class represents the input data which the test cases depend on for fulfillment use cases. Typically this class
 * is instantiated by deserializing the JSON data from src/test/resources/testdata/<testclass>.json files
 */
@JsonInclude(Include.NON_NULL)
public class TestCaseInputData {

    private String testCaseId;
    private UserCreationTaskInput buyerData;
    private UserCreationTaskInput sellerData;
    private UserCreationTaskInput funderData;
    private UserCreationTaskInput thirdPartyData;
    private FulfillPaymentPlanOptions fulfillPaymentPlanOptions;
    private FulfillmentHoldData holdData;
    private boolean disablePostOperationValidations;
    private PostOperationValidationsInput postOperationValidationsInput;
    private ComponentSpecificValidationInput componentSpecificValidationInput;
    private boolean checkIdempotency;
    private String useCaseType;
    private List<PostPaymentRequest> postPaymentRequests;
    private String prePaymentToggles;
    private String toggles;
    private boolean precreatedUser;
    private PostOperationValidationsInput paymentValidationsInput;
    private ValidationsInput validationsInput;
    private String paymentId;
    private boolean selectiveValidation;
    private String overrideIgnorableFlagsLocation;
    
    public String getUseCaseType() {
        return this.useCaseType;
    }

    public void setUseCaseType(String useCaseType) {
        this.useCaseType = useCaseType;
    }

    public boolean isDisablePostOperationValidations() {
        return this.disablePostOperationValidations;
    }

    public void setDisablePostOperationValidations(final boolean disablePostOperationValidations) {
        this.disablePostOperationValidations = disablePostOperationValidations;
    }

    public FulfillmentHoldData getHoldData() {
        return this.holdData;
    }

    public UserCreationTaskInput getBuyerData() {
        return this.buyerData;
    }

    public UserCreationTaskInput getSellerData() {
        return this.sellerData;
    }

    public UserCreationTaskInput getFunderData() {
        return this.funderData;
    }

    public void setFunderData(UserCreationTaskInput funderData) {
        this.funderData = funderData;
    }
    
    public void setThirdPartyData(UserCreationTaskInput thirdPartyData) {
        this.thirdPartyData = thirdPartyData;
    }

    public UserCreationTaskInput getThirdPartyData() {
        return thirdPartyData;
    }

    public FulfillPaymentPlanOptions getFulfillPaymentPlanOptions() {
        return this.fulfillPaymentPlanOptions;
    }

    public PostOperationValidationsInput getPostOperationValidationsInput() {
        return this.postOperationValidationsInput;
    }

    public ComponentSpecificValidationInput getComponentSpecificValidationInput() {
        return componentSpecificValidationInput;
    }

    public void setComponentSpecificValidationInput(ComponentSpecificValidationInput componentSpecificValidationInput) {
        this.componentSpecificValidationInput = componentSpecificValidationInput;
    }

    public String getTestCaseId() {
        return this.testCaseId;
    }
    public List<PostPaymentRequest> getPostPaymentRequests() {
        return postPaymentRequests;
    }

    public void setPostPaymentRequests(List<PostPaymentRequest> postPaymentRequests) {
        this.postPaymentRequests = postPaymentRequests;
    }

    public String getToggles() {
        return toggles;
    }

    public void setToggles(String toggles) {
        this.toggles = toggles;
    }

    public boolean isPrecreatedUser() {
        return precreatedUser;
    }

    public void setPrecreatedUser(boolean precreatedUser) {
        this.precreatedUser = precreatedUser;
    }
    public String getPrePaymentToggles() {
        return prePaymentToggles;
    }

    public void setPrePaymentToggles(String prePaymentToggles) {
        this.prePaymentToggles = prePaymentToggles;
    }
    @Override
    public String toString() {
        if (StringUtils.isEmpty(this.testCaseId)) {
            return "not_initialized";
        } else {
            return this.testCaseId;
        }

    }

    public boolean isCheckIdempotency() {
        return this.checkIdempotency;
    }

    public void setCheckIdempotency(final boolean checkIdempotency) {
        this.checkIdempotency = checkIdempotency;
    }

    public PostOperationValidationsInput getPaymentValidationsInput() {
        return paymentValidationsInput;
    }

    public void setPostPaymentValidationsInput(PostOperationValidationsInput paymentValidationsInput) {
        this.paymentValidationsInput = paymentValidationsInput;
    }

    public ValidationsInput getValidationsInput() {
        return validationsInput;
    }

    public void setValidationsInput(ValidationsInput validationsInput) {
        this.validationsInput = validationsInput;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isSelectiveValidation() {
        return selectiveValidation;
    }

    public void setSelectiveValidation(boolean selectiveValidation) {
        this.selectiveValidation = selectiveValidation;
    }

    public String getOverrideIgnorableFlagsLocation() {
        return overrideIgnorableFlagsLocation;
    }

    public void setOverrideIgnorableFlagsLocation(String overrideIgnorableFlagsLocation) {
        this.overrideIgnorableFlagsLocation = overrideIgnorableFlagsLocation;
    }
}
