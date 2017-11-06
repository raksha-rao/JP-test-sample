package com.jptest.payments.fulfillment.testonia.model.money;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@XmlRootElement(name = "validation_fixture")
public class SenderValidationFixtureDTO {

    @XmlElement(name = "MetaInfo")
    private MetaInfo metaInfo = new MetaInfo();

    @XmlElement(name = "sender")
    private PaymentSenderDTO senderDTO;

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public void setSenderDTO(PaymentSenderDTO senderDTO) {
        this.senderDTO = senderDTO;
    }

    public Document getDocument() throws JAXBException, ParserConfigurationException {
        return JAXBHelper.convertJaxbToDocument(this);
    }

    private static class MetaInfo {
        @XmlElement
        private String name = "jptest Validation fixture";
    }
}
