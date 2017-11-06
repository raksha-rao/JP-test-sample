package com.jptest.payments.fulfillment.testonia.model.pymt;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBHelper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


@XmlRootElement(name = "validation_fixture")
public class PaymentSideValidationFixtureDTO {

    @XmlElement(name = "MetaInfo")
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    private MetaInfo metaInfo = new MetaInfo();

    @XmlElement(name = "paymentside")
    public PymtTablesDTO pymtDTO;

    public void setPaymentSideDTO(PymtTablesDTO pymtDTO) {
        this.pymtDTO = pymtDTO;
    }
    
    public PymtTablesDTO getPaymentSideDTO(PymtTablesDTO pymtDTO) {
        return this.pymtDTO;
    }

    public Document getDocument() throws JAXBException, ParserConfigurationException {
        return JAXBHelper.convertJaxbToDocument(this);
    }

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public static class MetaInfo {
        @XmlElement
        private String name = "jptest Validation fixture";
    }
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }
  
}
