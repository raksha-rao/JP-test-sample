package com.jptest.payments.fulfillment.testonia.model.money;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.model.util.jaxb.JAXBHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@XmlRootElement(name = "validation_fixture")
public class FinancialJournalValidationFixtureDTO {

    @XmlElement(name = "MetaInfo")
    private MetaInfo metaInfo = new MetaInfo();

    @XmlElement(name = "engine")
    private FinancialJournalDTO financialJournalDTO;

    //    <money>
    //    <holding_balance>
    //      <item>
    //        <account_number>7001</account_number>
    //        <amount>0</amount>
    //        <currency_code>USD</currency_code>
    //        <holding_balance_type>P</holding_balance_type>
    //        <holding_id>2001</holding_id>
    //      </item>
    //    </holding_balance>
    //  </money>
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    public void setFinancialJournalDTO(FinancialJournalDTO financialJournalDTO) {
        this.financialJournalDTO = financialJournalDTO;
    }

    public Document getDocument() throws JAXBException, ParserConfigurationException {
        return JAXBHelper.convertJaxbToDocument(this);
    }

    private static class MetaInfo {
        @XmlElement
        private String name = "jptest Validation fixture";
    }
}
