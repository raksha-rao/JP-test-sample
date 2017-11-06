package com.jptest.payments.fulfillment.testonia.core.util.xml.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.configuration.Configuration;
import org.jvnet.jaxb2_commons.lang.StringUtils;
import org.w3c.dom.Document;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.CoreConfigKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;


/**
 * This class filters inputXML on the basis of Golden File.
 * Logic is to remove the XPaths which are present in Actual but not in Golden.
 * 
 * But we shouldn't remove Xpaths of Expanded flags because if a developer misses to put
 * any expanded flag then it should be treated as a diff. To achieve this a xpath is checked
 * to not have been ended with expanded flag tag.
 * 
 * But there is a catch! What if a developer wants to test, let's say only flags2?
 * In the above scenario the developer can skip all other expanded flags except that of flags2,
 * and the test case should still pass.
 * 
 * Above case is handled in {@link ExpandedFlagsHelper}
 * The idea is to check for presence of tags which counts for the expanded flags,
 * and restrict the pool of available tags on the basis of it.
 * 
 * As a final thought, If a developer wants to remove the entire expanded flags section,
 * then that is possible with the current method.
 * 
 * @see XMLFilterHelperTest
 * 
 * @JP Inc.
 */
@Singleton
public class XMLFilterHelper {

    @Inject
    private XSLTHelper xsltHelper;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private Configuration configuration;

    @Inject
    private ExpandedFlagsHelper expandedFlagsHelper;

    public XMLFilterHelper() {
        super();
    }

    /*
     * For Testing purpose
     */
    protected XMLFilterHelper(XSLTHelper xsltHelper, XMLHelper xmlHelper,
            Configuration configuration, ExpandedFlagsHelper expandedFlagsHelper) {
        this.xsltHelper = xsltHelper;
        this.xmlHelper = xmlHelper;
        this.configuration = configuration;
        this.expandedFlagsHelper = expandedFlagsHelper;
    }

    public Document getFilteredXML(Document inputXML, Document goldenFileXML) {
        Set<String> goldenFileXPaths = getXPath(goldenFileXML);
        Set<String> inputXMLXPaths = getXPath(inputXML);
        Set<String> expandedFlags = expandedFlagsHelper.getExpandedFlags(goldenFileXML);
        /*
         * inputXPath should be removed if: 1. It's not an expanded Flag 2. Golden File doesn't contain this xPath
         */
        for (String inputXPath : inputXMLXPaths) {
            boolean isFlag = false;
            for (String flag : expandedFlags) {
                if (inputXPath.endsWith(flag)) {
                    isFlag = true;
                    break;
                }
            }

            if (!isFlag && !goldenFileXPaths.contains(inputXPath)) {
                try {
                    xmlHelper.removeNodesByXPath(inputXML, inputXPath);
                }
                catch (XPathExpressionException e) {
                    throw new TestExecutionException("Failed to remove XPath " + inputXPath, e);
                }
            }
        }
        return inputXML;
    }
    
    /*
     * For an XPath /root/x/y, we should keep all possible xpaths, i.e
     * [/root/x/y, /root/x, /root]
     * 
     * It's required because Golden file may not have a tag from any level.
     * We don't want to remove only terminal tags.
     * 
     */
    protected Set<String> getXPath(Document document) {
        final String NEWLINE_SPLIT_EXPRESSION = "\\r?\\n";
        String xPathExtractorFileLocation = configuration
                .getString(CoreConfigKeys.XML_XPATH_EXTRACTOR_XSLT_LOCATION.getName());
        String xPathExtractorXSLT = xsltHelper.readXSLT(xPathExtractorFileLocation);
        String termnialXPathString = xsltHelper.applyXSLT(document, xPathExtractorXSLT);
        Set<String> terminalXPaths = new HashSet<>(Arrays.asList(termnialXPathString.split(NEWLINE_SPLIT_EXPRESSION)));
        Set<String> allXPaths = new TreeSet<>();
        
        for(String xpath : terminalXPaths) {
            allXPaths.addAll(getAllXPathPatterns(xpath));
        }
        
        return allXPaths;
    }

    protected Set<String> getAllXPathPatterns(String xpath) {
        Set<String> allXPathPatterns = new HashSet<>();
        while(!StringUtils.isEmpty(xpath)) {
            allXPathPatterns.add(xpath);
            xpath = xpath.substring(0, xpath.lastIndexOf('/'));
        }
        return allXPathPatterns;
    }
    
}
