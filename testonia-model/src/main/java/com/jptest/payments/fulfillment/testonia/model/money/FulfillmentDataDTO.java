package com.jptest.payments.fulfillment.testonia.model.money;

import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBHelper;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FulfillmentDataDTO {

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fulfillment_activity")
    private List<FulfillmentActivityDTO> fulfillmentActivityList;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fulfillment_activity_id_map")
    private List<FulfillmentActivityIdMapDTO> FulfillmentActivityIdMapList;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "fulfillment_rollback_data")
    private List<FulfillmentRollbackDataDTO> fulfillmentRollbackDataList;

    @XmlElement(name = "item")
    @XmlElementWrapper(name = "payment_checkpoint")
    private List<PaymentCheckpointDTO> paymentCheckpointList;

    public List<FulfillmentActivityDTO> getFulfillmentActivityList() {
        return fulfillmentActivityList;
    }

    public void setFulfillmentActivityList(List<FulfillmentActivityDTO> fulfillmentActivityList) {
        this.fulfillmentActivityList = fulfillmentActivityList;
    }

    public List<FulfillmentActivityIdMapDTO> getFulfillmentActivityIdMapList() {
        return FulfillmentActivityIdMapList;
    }

    public void setFulfillmentActivityIdMapList(List<FulfillmentActivityIdMapDTO> fulfillmentActivityIdMapList) {
        FulfillmentActivityIdMapList = fulfillmentActivityIdMapList;
    }

    public List<FulfillmentRollbackDataDTO> getFulfillmentRollbackDataList() {
        return fulfillmentRollbackDataList;
    }

    public void setFulfillmentRollbackDataList(List<FulfillmentRollbackDataDTO> fulfillmentRollbackDataList) {
        this.fulfillmentRollbackDataList = fulfillmentRollbackDataList;
    }

    public List<PaymentCheckpointDTO> getPaymentCheckpointList() {
        return paymentCheckpointList;
    }

    public void setPaymentCheckpointList(List<PaymentCheckpointDTO> paymentCheckpointList) {
        this.paymentCheckpointList = paymentCheckpointList;
    }

    public Document getDocument() throws JAXBException, ParserConfigurationException {
        return JAXBHelper.convertJaxbToDocument(this);
    }

}
