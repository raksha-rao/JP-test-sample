package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityLogDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityLogDTO;

/**
 * This is a base class for all activity log based validations. This class performs the 
 * core logic and provides hooks where the individual validation classes can implement their
 * logic. This class extends from {@link GoldenFileComparisonAsserter} so all we need to do is provide
 * the impl for  getActualResponseXml()
 * The steps for this logic are:
 * 1. Get the activity id (default impl is to get it from context but it can be overriden in case of 
 * custom logic.
 * 2. Get the {@link FulfillmentActivityLogDTO} based on context and activity id. Default impl is to check
 * if it is present in the context and use it. If not, then get it from the DAO based on the activity id.
 * 3. Since the activity log is stored as a blob, we get the whole log everytime so we need to get the subset
 * of the document for which we are validating. 
 * 3A. Individual validations need to provide the XPATH that they are interested in. Based on that, we get the
 * subset of the document.
 * 4. Virtualization
 * 5. After this, the base class {@link GoldenFileComparisonAsserter} kicks in and performs the comparison.
 * Retrieves the FULFILLMENT_ACTIVITY_ID (FFAID) from context using
 * {@link ContextKeys.ENGINE_ACTIVITY_ID_KEY}.
 */
public abstract class FulfillmentActivityLogBaseAsserter extends ComponentLevelGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentActivityLogBaseAsserter.class);

    private static final String XML_ROOT_ELEMENT_NAME = "ActivityLog";

    @Inject
    private FulfillmentActivityLogDao dao;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    public FulfillmentActivityLogBaseAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        BigInteger activityId = getActivityId(context);
        FulfillmentActivityLogDTO activityLogDTO = getActivityLogDTO(context, activityId);
        Document doc = xmlHelper.getXMLDocumentForDeserializedVOs(activityLogDTO.getActivityLogs(),
                XML_ROOT_ELEMENT_NAME);
        doc = xmlHelper.getSubsetDocument(doc, getXPathCriteria());
        unsetFlags(context, doc);
        XmlVirtualizationTask<Document> virtualizationTask = getVirtualizationTask();
        if (virtualizationTask != null)
            doc = virtualizationTask.execute(doc);
        LOGGER.info("The {} XML {}", getValidationType(), xmlHelper.getPrettyXml(doc));
        return doc;

    }

    private void unsetFlags(Context context, Document doc) {
        String ignorableFlagsLocation = (String) getDataFromContext(context,
                ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());
        unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
    }

    protected FulfillmentActivityLogDTO getActivityLogDTO(Context context, BigInteger activityId) {
        return dao.getActivityLog(activityId);
    }

    protected BigInteger getActivityId(Context context) {
        return (BigInteger) getDataFromContext(context, ContextKeys.ENGINE_ACTIVITY_ID_KEY.getName());
    }

    protected abstract String getXPathCriteria();

    protected abstract XmlVirtualizationTask<Document> getVirtualizationTask();

}
