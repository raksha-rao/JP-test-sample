package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBHelper;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBMapToListAdapter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@XmlRootElement(name = "IPNFixture_ResultVO")
public class IPNFixtureResultVO {

    @XmlElement(name = "http_response")
    private String httpResponse;

    @XmlJavaTypeAdapter(JAXBMapToListAdapter.class)
    @XmlElement(name = "ipn_values")
    private Map<String, String> ipnValues;

    @XmlElement(name = "error_message")
    private String errorMessage;

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public void setHttpResponse(String httpResponse) {
        this.httpResponse = httpResponse;
    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public void setIpnValues(Map<String, String> ipnValues) {
        this.ipnValues = ipnValues;
    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Document getDocument() throws JAXBException, ParserConfigurationException {
        return JAXBHelper.convertJaxbToDocument(this);
    }
}
