package com.jptest.payments.fulfillment.testonia.business.amqdaemon;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;

/**
 * This class is an enhancement over QueueCDBImpl. 
 * 
 * It allows reading AMQ properties from Configuration instead of CDB 
 */
public class QueueConfigImpl extends QueueCDBImpl {

    public QueueConfigImpl(Configuration config, List<String> message_types) {
        super(convertToProperties(config), message_types);
    }

    private static Properties convertToProperties(Configuration config) {
        Iterator<String> keys = config.getKeys();
        Properties properties = new Properties();
        while (keys.hasNext()) {
            String key = keys.next();
            properties.setProperty(key, config.getString(key));
        }
        return properties;
    }

    /**
     * Lets not read cdb file
     */
    @Override
    protected void set_config(String message_type) {
        // Its same as doing m_local_config = m_config.
        setLocalConfig(getConfig());
    }

}
