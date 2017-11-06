package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.NotSupportedException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.configuration.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;
import com.jptest.payments.fulfillment.testonia.core.util.HttpClientUtil;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;

/**
 * Reads from location, converts it to XML format and returns the xmlString
 */
public class FileRetrieverTask {

    private static final String GOLDEN_FILE_LOCATION_TYPE = "golden.file.location.type";
    @Inject
    private FileHelper fileHelper;

    @Inject
    private HttpClientUtil httpClientUtil;

    @Inject
    private XMLHelper xmlGenerator;

    @Inject
    private Configuration config;

    private LocationType locationType;

    @Inject
    public void init() {
        this.locationType = LocationType.getEnum(config.getString(GOLDEN_FILE_LOCATION_TYPE));
    }

    public Document execute(String location) {
        Document resultDoc = null;
        String xmlContent = getXMLContent(location);

        if (xmlContent != null) {
            try {
                resultDoc = xmlGenerator.convertToXmlDocument(xmlContent);
            } catch (ParserConfigurationException | SAXException | IOException e) {
                throw new TestExecutionException(e);
            }
        }

        return resultDoc;
    }

    private String getXMLContent(String location) {
        String xmlContent = null;
        try {
            switch (this.locationType) {
            case FILE:
                    xmlContent = fileHelper.readContentFromTestResource(location);
                break;
            case HTTP:
                xmlContent = httpClientUtil.readContent(location);
                break;
            default:
                throw new IllegalStateException("Unsupported locationType " + this.locationType);
            }
        } catch (IOException e) {
            throw new TestExecutionException(e);
        }
        return xmlContent;
    }

    public enum LocationType {
        FILE("FILE"),
        HTTP("HTTP");
        //DB("DB");

        private String value;

        private LocationType(String value) {
            this.value = value;
        }

        public static LocationType getEnum(String strValue) {
            for (LocationType type : values()) {
                if (type.value.equals(strValue)) {
                    return type;
                }
            }
            throw new NotSupportedException("FileLocationType " + strValue + " is not supported.");
        }
    }
}
