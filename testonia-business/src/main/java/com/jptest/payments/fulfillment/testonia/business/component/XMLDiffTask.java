package com.jptest.payments.fulfillment.testonia.business.component;

import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XMLDiffHelper;
import com.jptest.payments.fulfillment.testonia.core.util.xml.diff.XmlDiff;
import org.w3c.dom.Document;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Generic XML diff task that compares input XMLs and returns diff object.
 */
public class XMLDiffTask {

    //    private static final Logger LOGGER = LoggerFactory.getLogger(XMLDiffTask.class);

    // TEP has the logic that builds he audit trail string instead of getting it from pricing jar.
    // This logic is not upgraded with latest pricing changes due to which placeholder resolution fails resulting in DB
    // diff
    // ignoring the impacted field
    // This list needs to be empties out once the TEP fix (currently in development) gets fixed
    private static final Set<String> DEFAULT_EXCLUDE_SET = new HashSet<>(
            Arrays.asList("//validation_fixture/recipient/fee_history/item[transaction_type='X']/data",
                    "//validation_fixture/sender/fee_history/item[transaction_type='X']/data"));

    @Inject
    private XMLDiffHelper xmlDiffHelper;

    /**
     * Returns xml diff of xmlContent1 and xmlContent2
     * @param //xmlContent1
     * @param //xmlContent2
     * @param //excludeList
     * @return
     */
    public XmlDiff performDiff(Document actualXML, Document goldenXML, String outputLocation) {
        return performDiff(actualXML, goldenXML, DEFAULT_EXCLUDE_SET, outputLocation);
    }

    /**
     * Returns xml diff of xmlContent1 and xmlContent2 - ignoring exclude XPATH list
     * @param //xmlContent1
     * @param //xmlContent2
     * @param excludeList
     * @return
     */
    public XmlDiff performDiff(Document actualXML, Document goldenXML, Set<String> excludeList, String outputLocation) {
        XmlDiff diff = xmlDiffHelper.performDiff(actualXML, goldenXML, excludeList, outputLocation);
        //        if (!diff.similar()) {
        //            LOGGER.info(
        //                    "***********************************************************************************************");
        //            xmlDiffHelper.displayDiffs(diff);
        //            LOGGER.info(
        //                    "***********************************************************************************************");
        //        }
        return diff;
    }

}
