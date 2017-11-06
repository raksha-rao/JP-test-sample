package com.jptest.payments.fulfillment.testonia.business.service;

import com.jptest.payments.fulfillment.testonia.business.component.XMLDiffTask;
import com.jptest.payments.fulfillment.testonia.business.component.validation.FileRetrieverTask;
import com.jptest.payments.fulfillment.testonia.business.util.DiffHtmlGenerator;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XmlDiff;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.sampletest.SampleDiffHelper;
import com.jptest.sampletest.SampleGoldenFileComparisonAsserter;
import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class SampleGoldenFileComparisonAsserterTest {

    private SampleGoldenFileComparisonAsserter sampleGoldenFileComparisonAsserter;

    @Mock
    public Configuration configuration;

    @Mock
    public SampleDiffHelper diffHelper;

    @Mock
    public FileRetrieverTask fileRetriever;

    @Mock
    public XMLDiffTask xmlDiffTask;

    @Mock
    private DiffHtmlGenerator diffHtmlGenerator;

    @Before
    public void setup() {
        Mockito.reset(fileRetriever);
        MockitoAnnotations.initMocks(SampleGoldenFileComparisonAsserter.class);
    }


    @Test
    public void generateGoldenFile() throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        fileRetriever = Mockito.mock(FileRetrieverTask.class);
        GoldenFileComparisonTaskInput input = new GoldenFileComparisonTaskInput();

        input.setGoldenFileLocation("sample-golden.xml");
        sampleGoldenFileComparisonAsserter = new SampleGoldenFileComparisonAsserter(input);
        sampleGoldenFileComparisonAsserter.setFileRetriever(fileRetriever);
        sampleGoldenFileComparisonAsserter.setGoldenfileGenerationMode(true);

        diffHelper = Mockito.mock(SampleDiffHelper.class);
        sampleGoldenFileComparisonAsserter.setDiffHelper(diffHelper);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("sample-golden.xml"));

        Mockito.when(fileRetriever.execute("sample-golden.xml")).thenReturn(document);

        sampleGoldenFileComparisonAsserter.validate();

    }

    @Test(expectedExceptions = AssertionError.class)
    public void assertionFailure() throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        try {
            fileRetriever = Mockito.mock(FileRetrieverTask.class);
            diffHelper = Mockito.mock(SampleDiffHelper.class);
            xmlDiffTask = Mockito.mock(XMLDiffTask.class);
            diffHtmlGenerator = Mockito.mock(DiffHtmlGenerator.class);

            GoldenFileComparisonTaskInput input = new GoldenFileComparisonTaskInput();
            input.setGoldenFileLocation("sample-golden.xml");
            sampleGoldenFileComparisonAsserter = new SampleGoldenFileComparisonAsserter(input);
            sampleGoldenFileComparisonAsserter.setFileRetriever(fileRetriever);

            sampleGoldenFileComparisonAsserter.setDiffHelper(diffHelper);
            sampleGoldenFileComparisonAsserter.setGoldenfileGenerationMode(false);
            sampleGoldenFileComparisonAsserter.setXmlDiffTask(xmlDiffTask);
            sampleGoldenFileComparisonAsserter.setDiffHtmlGenerator(diffHtmlGenerator);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document actualDoc = builder.parse(new File("sample-actual.xml"));

            DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder1 = factory.newDocumentBuilder();
            Document goldenDoc = builder.parse(new File("sample-golden.xml"));

            Mockito.when(fileRetriever.execute("sample-actual.xml")).thenReturn(actualDoc);
            Mockito.when(fileRetriever.execute("sample-golden.xml")).thenReturn(goldenDoc);
            XmlDiff xmlDiff = new XmlDiff(actualDoc,goldenDoc,"","");

            Mockito.when(xmlDiffTask.performDiff(actualDoc, goldenDoc, "")).thenReturn(xmlDiff);

            sampleGoldenFileComparisonAsserter.validate();
            Assert.fail("Assertion failure - no differences found!");
        } catch (AssertionError e) {
            Assert.assertEquals(e.getMessage(), "");
        }
    }

    @Test
    public void assertionPass() throws NoSuchFieldException, ParserConfigurationException, IOException, SAXException {
        fileRetriever = Mockito.mock(FileRetrieverTask.class);
        xmlDiffTask = Mockito.mock(XMLDiffTask.class);
        diffHelper = Mockito.mock(SampleDiffHelper.class);
        GoldenFileComparisonTaskInput input = new GoldenFileComparisonTaskInput();

        input.setGoldenFileLocation("sample-golden.xml");
        sampleGoldenFileComparisonAsserter = new SampleGoldenFileComparisonAsserter(input);
        sampleGoldenFileComparisonAsserter.setFileRetriever(fileRetriever);
        sampleGoldenFileComparisonAsserter.setGoldenfileGenerationMode(false);


        sampleGoldenFileComparisonAsserter.setDiffHelper(diffHelper);
        sampleGoldenFileComparisonAsserter.setXmlDiffTask(xmlDiffTask);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document actualDoc = builder.parse(new File("sample-actual.xml"));

        DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1 = factory.newDocumentBuilder();
        Document goldenDoc = builder.parse(new File("sample-golden.xml"));

        Mockito.when(fileRetriever.execute("sample-actual.xml")).thenReturn(actualDoc);
        Mockito.when(fileRetriever.execute("sample-golden.xml")).thenReturn(goldenDoc);

        sampleGoldenFileComparisonAsserter.validate();

    }

}
