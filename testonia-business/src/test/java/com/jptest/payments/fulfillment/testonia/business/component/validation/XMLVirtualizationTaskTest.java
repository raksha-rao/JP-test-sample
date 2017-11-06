package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.model.money.VirtualizeSchemaDTO;

public class XMLVirtualizationTaskTest {

    @Test
    public void testTransferNodes() throws Exception {

        String jsonSchema = new StringBuilder()
                .append("{\"map\" : {\"virtual_account_number\" : 500},")
                .append("\"transform\" : {\"account_number\" : \"virtual_account_number\"},")
                .append("\"ignoreNodeList\" : [],\"transformXPath\" : {},\"setIfNonEmpty\" : {}}\"")
                .toString();
        String xmlContent = new StringBuilder()
                .append("<vo>")
                .append("<account_number>1234</account_number>")
                .append("<account_number>5678</account_number>")
                .append("</vo>")
                .toString();
        String expectedOutput = new StringBuilder()
                .append("<vo>")
                .append("<account_number>501</account_number>")
                .append("<account_number>502</account_number>")
                .append("</vo>")
                .toString();
        executeAndAssert(xmlContent, expectedOutput, jsonSchema);
    }

    @Test
    public void testTransferNodesForEmptyValues() throws Exception {

        String jsonSchema = new StringBuilder()
                .append("{\"map\" : {\"virtual_account_number\" : 500},")
                .append("\"transform\" : {\"account_number\" : \"virtual_account_number\"},")
                .append("\"ignoreNodeList\" : [],\"transformXPath\" : {},\"setIfNonEmpty\" : {}}\"")
                .toString();
        String xmlContent = new StringBuilder()
                .append("<vo>")
                .append("<account_number></account_number>")
                .append("<account_number>5678</account_number>")
                .append("</vo>")
                .toString();
        String expectedOutput = new StringBuilder()
                .append("<vo>")
                .append("<account_number/>")
                .append("<account_number>501</account_number>")
                .append("</vo>")
                .toString();
        executeAndAssert(xmlContent, expectedOutput, jsonSchema);
    }

    @Test
    public void testForTwoOccurancesOfSameAccountNumber() throws Exception {

        String jsonSchema = new StringBuilder()
                .append("{\"map\" : {\"virtual_account_number\" : 500},")
                .append("\"transform\" : {\"account_number\" : \"virtual_account_number\"},")
                .append("\"ignoreNodeList\" : [],\"transformXPath\" : {},\"setIfNonEmpty\" : {}}\"")
                .toString();
        String xmlContent = new StringBuilder()
                .append("<vo>")
                .append("<account_number>1234</account_number>")
                .append("<account_number>5678</account_number>")
                .append("<account_number>1234</account_number>")
                .append("<account_number>5678</account_number>")
                .append("</vo>")
                .toString();
        String expectedOutput = new StringBuilder()
                .append("<vo>")
                .append("<account_number>501</account_number>")
                .append("<account_number>502</account_number>")
                .append("<account_number>501</account_number>")
                .append("<account_number>502</account_number>")
                .append("</vo>")
                .toString();

        executeAndAssert(xmlContent, expectedOutput, jsonSchema);
    }

    @Test
    public void testForTwoElementTransform() throws Exception {

        String jsonSchema = new StringBuilder()
                .append("{\"map\" : {\"virtual_account_number\" : 500,")
                .append("\"virtual_payment_id\" : 600},")
                .append("\"transform\" : {\"account_number\" : \"virtual_account_number\",")
                .append("\"payment_id\" : \"virtual_payment_id\"},")
                .append("\"ignoreNodeList\" : [],\"transformXPath\" : {},\"setIfNonEmpty\" : {}}\"")
                .toString();
        String xmlContent = new StringBuilder()
                .append("<vo>")
                .append("<account_number>1234</account_number>")
                .append("<payment_id>1234</payment_id>")
                .append("<account_number>5678</account_number>")
                .append("<payment_id>5678</payment_id>")
                .append("</vo>")
                .toString();
        String expectedOutput = new StringBuilder()
                .append("<vo>")
                .append("<account_number>501</account_number>")
                .append("<payment_id>601</payment_id>")
                .append("<account_number>502</account_number>")
                .append("<payment_id>602</payment_id>")
                .append("</vo>")
                .toString();

        executeAndAssert(xmlContent, expectedOutput, jsonSchema);
    }

    private void executeAndAssert(String inputDocument, String expectedOutput, String jsonSchema) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        VirtualizeSchemaDTO virtualizeSchemaDTO = mapper.readValue(jsonSchema,
                new TypeReference<VirtualizeSchemaDTO>() {
                });
        XMLHelper xmlHelper = new XMLHelper();
        Document document = xmlHelper.convertToXmlDocument(inputDocument);
        TestXmlVirtualizationTask task = new TestXmlVirtualizationTask(virtualizeSchemaDTO);
        Document newDocument = task.execute(document);
        Assert.assertNotNull(newDocument);
        Assert.assertTrue(expectedOutput.equals(getXml(newDocument)));
    }

    public String getXml(Document doc) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        return writer.toString();
    }

    @Test(dataProvider = "testUnsetFlagsDP")
    public void testUnsetFlags(String xmlContent, String jsonSchema, String expectedOutput) throws Exception {
        XMLHelper xmlHelper = new XMLHelper();
        Document document = xmlHelper.convertToXmlDocument(xmlContent);

        ObjectMapper mapper = new ObjectMapper();
        VirtualizeSchemaDTO virtualizeSchemaDTO = mapper.readValue(jsonSchema,
                new TypeReference<VirtualizeSchemaDTO>() {
                });

        TestUnsetFlagXmlVirtualizationTask task = new TestUnsetFlagXmlVirtualizationTask(virtualizeSchemaDTO);
        Document newDocument = task.execute(document);
        Assert.assertNotNull(newDocument);
        String actualOutput = getXml(newDocument);
        Assert.assertTrue(expectedOutput.equals(actualOutput));
    }

    @DataProvider
    private static Object[][] testUnsetFlagsDP() {
        String jsonSchema = new StringBuilder()
                .append("{\"unsetFlags\" : {\"flags\" : 1}}")
                .toString();

        return new Object[][] {
                { new StringBuilder()
                        .append("<validation_fixture><recipient>")
                        .append("<wtransaction><item>")
                        .append("<flags>")
                        .append("<flags1_NO_DISPLAY>1</flags1_NO_DISPLAY>")
                        .append("</flags>")
                        .append("<flags1>1</flags1>")
                        .append("</item></wtransaction>")
                        .append("<wuser_holding><item>")
                        .append("<flags>5</flags>")
                        .append("</item></wuser_holding>")
                        .append("")
                        .append("</recipient></validation_fixture>")
                        .toString(), jsonSchema,
                        new StringBuilder()
                                .append("<validation_fixture><recipient>")
                                .append("<wtransaction><item>")
                                .append("<flags>")
                                .append("<flags1_NO_DISPLAY>1</flags1_NO_DISPLAY>")
                                .append("</flags>")
                                .append("<flags1>1</flags1>")
                                .append("</item></wtransaction>")
                                .append("<wuser_holding><item>")
                                .append("<flags>4</flags>")
                                .append("</item></wuser_holding>")
                                .append("")
                                .append("</recipient></validation_fixture>")
                                .toString() },
                { new StringBuilder()
                        .append("<validation_fixture><recipient>")
                        .append("<wtransaction><item>")
                        .append("<flags>")
                        .append("</flags>")
                        .append("<flags1>1</flags1>")
                        .append("</item></wtransaction>")
                        .append("<wuser_holding><item>")
                        .append("<flags>5</flags>")
                        .append("</item></wuser_holding>")
                        .append("")
                        .append("</recipient></validation_fixture>")
                        .toString(), jsonSchema,
                        new StringBuilder()
                                .append("<validation_fixture><recipient>")
                                .append("<wtransaction><item>")
                                .append("<flags/>")
                                .append("<flags1>1</flags1>")
                                .append("</item></wtransaction>")
                                .append("<wuser_holding><item>")
                                .append("<flags>4</flags>")
                                .append("</item></wuser_holding>")
                                .append("")
                                .append("</recipient></validation_fixture>")
                                .toString() }

        };
    }

    private static class TestXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

        private TestXmlVirtualizationTask(VirtualizeSchemaDTO dto) {
            super();
            setVirtualizeSchemaDTO(dto);
        }

        @Override
        public Document execute(Document document) {
            transformNodes(document);
            return document;
        }

    }

    private static class TestUnsetFlagXmlVirtualizationTask extends XmlVirtualizationTask<Document> {

        private TestUnsetFlagXmlVirtualizationTask(VirtualizeSchemaDTO dto) {
            super();
            setVirtualizeSchemaDTO(dto);
        }

        @Override
        public Document execute(Document document) {
            unsetFlags(document);
            return document;
        }

    }

}
