package com.jptest.payments.fulfillment.testonia.core.util.xml.filter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.FileHelper;

/**
 * Filters XML.
 * Algorithm:
 * 1. Extract tag names from goldeFileXML
 * 2. Using tags above create XSLT to extract the required subset of goldenFileXML
 * 3. Apply the XSLT generated above to inputXML
 * 
 * @JP Inc.
 *
 */
@Singleton
public class XSLTHelper {
    
    @Inject
    FileHelper fileHelper;
    
    public XSLTHelper() {
        super();
    }
    
    protected XSLTHelper(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }
    
    public String readXSLT(String location) {
        try {
            return fileHelper.readContent(location);
        }
        catch (IOException e) {
            throw new TestExecutionException("Error reading Base XSLT File", e);
        }
    }
    
    public String applyXSLT(Document inputXML, String xslt) {
        
        Source xsltSource = new StreamSource(new StringReader(xslt));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer(xsltSource);
            Source inputSource = new DOMSource(inputXML);

            StreamResult result = new StreamResult(new StringWriter());
            
            transformer.transform(inputSource, result);
            return result.getWriter().toString();
            
        } catch (TransformerException e) {
            throw new TestExecutionException("Failed to apply XSLT on input", e);
        }
    }
     
}
