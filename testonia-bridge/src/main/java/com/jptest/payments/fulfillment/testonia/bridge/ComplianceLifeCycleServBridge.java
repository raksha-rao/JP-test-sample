package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.name.Named;
import com.jptest.api.platform.compliance.levels.ComplianceLevel;
import com.jptest.api.platform.compliance.levels.ComplianceLevelDefinition;
import com.jptest.api.platform.compliance.levels.ComplianceStateTransition;
import com.jptest.payments.fulfillment.testonia.bridge.resource.ComplianceLifeCycleResource;


/**
 * Represents bridge for complifecycleserv API calls
 */

@Singleton
public class ComplianceLifeCycleServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComplianceLifeCycleServBridge.class);

    @Inject
    @Named("compliancelifecycleresource")
    private ComplianceLifeCycleResource complianceLifeCycleResource;

    public void updateCIPLevelsByCustomerID(String accountNumber) {
        List<ComplianceLevel> complianceLevelList = new ArrayList<ComplianceLevel>();
        ComplianceLevel complianceLevel = new ComplianceLevel();
        complianceLevel.setName(ComplianceLevelDefinition.CIP);
        complianceLevel.setState(ComplianceStateTransition.COMPLETED);
        complianceLevelList.add(complianceLevel);
        LOGGER.info("updateCIPLevelsByCustomerID request for accountNumber: {} ", accountNumber);
        Response response = complianceLifeCycleResource.updateLevelsByCustomerID(accountNumber, "ACCOUNT",
                complianceLevelList);
        LOGGER.info("updateCIPLevelsByCustomerID response: {} ", printJsonObject(response));

    }
}
