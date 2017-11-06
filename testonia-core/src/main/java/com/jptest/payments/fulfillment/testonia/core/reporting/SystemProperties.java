package com.jptest.payments.fulfillment.testonia.core.reporting;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;

import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;

/**
 * This class is used for displaying system properties in kibana. It gets list of all system properties and excludes the
 * common ones - which aren't very interesting.
 * <p>
 * This way when default endpoints are overridden, they will show up on kibana dashboard
 */
@Singleton
public class SystemProperties {

    private List<Object> ignorableProperties;

    private List<Object> reportingAttributes;

    @Inject
    private Configuration config;

    @Inject
    public void init() {
        ignorableProperties = config.getList(CoreConfigKeys.IGNORABLE_SYSTEM_PROPERTIES.getName());
        reportingAttributes = config.getList(CoreConfigKeys.DASHBOARD_REPORTING_ATTRIBUTES.getName());
    }

    /**
     * Returns all system properties key=value pair in comma separated manner (excluding common properties which are not
     * helpful during debugging)
     * 
     * @return
     */
    public String getCommaSeparatedProperties() {
        return ManagementFactory.getRuntimeMXBean().getSystemProperties().entrySet().stream()
                .filter(entry -> !ignorableProperties.contains(entry.getKey())).map(entry -> entry.toString())
                .sorted().collect(Collectors.joining(", "));
    }

    /**
     * This method returns the VM params that we want to show on kibana dashboard out of all the vm params
     * that are available for the test case.
     * It reads the "dashboard.reporting.attributes" property which holds the vm params we 
     * want to see as separate fields on kibana so we can filter based on their values.
     */
    public Map<String, String> getReportingVMParameters() {
        Map<String, String> reportingVMParams = new HashMap<String, String>();
        ManagementFactory.getRuntimeMXBean().getSystemProperties().entrySet().stream()
                .filter(entry -> reportingAttributes.contains(entry.getKey()))
                .forEach(entry -> reportingVMParams.put(transformKey(entry.getKey()), entry.getValue()));
        return reportingVMParams;
    }

    /**
     * Without transformation, kibana does not display the result (due to .)
     */
    public static String transformKey(String key) {
        return key.replaceAll("\\.", "-");
    }

}
