package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentActivityIdMapDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.FulfillmentRollbackDataDao;
import com.jptest.payments.fulfillment.testonia.dao.eng.PaymentCheckpointDao;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentActivityIdMapDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentDataDTO;
import com.jptest.payments.fulfillment.testonia.model.money.FulfillmentRollbackDataDTO;
import com.jptest.payments.fulfillment.testonia.model.money.PaymentCheckpointDTO;

/**
 * Performs DB diff using golden file for FulfillmentActivity related tables:
 * <li>FULFILLMENT_ACTIVITY</li>
 * <li>FULFILLMENT_ACTIVITY_ID_MAP</li>
 * <li>FULFILLMENT_ROLLBACK_DATA</li>
 * <li>PAYMENT_CHECKPOINT</li>
 */
public class FulfillmentActivityGoldenFileAsserter extends ComponentLevelGoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FulfillmentActivityGoldenFileAsserter.class);

    private static final String OPERATION = "FulfillmentActivity";

    @Inject
    private FulfillmentActivityDao fulfillmentActivityDao;

    @Inject
    private FulfillmentActivityIdMapDao fulfillmentActivityIdMapDao;

    @Inject
    private FulfillmentRollbackDataDao fulfillmentRollbackDataDao;

    @Inject
    private PaymentCheckpointDao paymentCheckpointDao;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private FulfillmentActivityXmlVirtualizationTask xmlVirtualizationTask;

    public FulfillmentActivityGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }
    
    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_FULFILL_ACTIVITY_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        BigInteger fulfillmentActivityId = (BigInteger) context
                .getData(ContextKeys.ENGINE_ACTIVITY_ID_KEY.getName());

        FulfillmentDataDTO fulfillmentData = prepareFulfillmentData(fulfillmentActivityId);

        try {
            Document doc = fulfillmentData.getDocument();
            doc = xmlVirtualizationTask.execute(doc);
            LOGGER.info("FulfillmentActivity Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }

    private FulfillmentDataDTO prepareFulfillmentData(final BigInteger fulfillmentActivityId) {
        FulfillmentDataDTO fulfillmentData = new FulfillmentDataDTO();

        addFulfillmentActivityList(fulfillmentActivityId, fulfillmentData);
        addFulfillmentActivityIdMapList(fulfillmentActivityId, fulfillmentData);
        addFulfillmentActivityList(fulfillmentActivityId, fulfillmentData);
        addFulfillmentRollbackDataList(fulfillmentActivityId, fulfillmentData);
        addPaymentCheckpointList(fulfillmentActivityId, fulfillmentData);

        return fulfillmentData;
    }

    private void addFulfillmentActivityList(BigInteger fulfillmentActivityId, FulfillmentDataDTO fulfillmentData) {
        List<FulfillmentActivityDTO> fulfillmentActivityList = fulfillmentActivityDao
                .getRecordsByActivityId(fulfillmentActivityId);
        if (CollectionUtils.isNotEmpty(fulfillmentActivityList)) {
            fulfillmentData.setFulfillmentActivityList(fulfillmentActivityList);
        }
    }

    private void addFulfillmentActivityIdMapList(BigInteger fulfillmentActivityId, FulfillmentDataDTO fulfillmentData) {
        List<FulfillmentActivityIdMapDTO> fulfillmentActivityIdMapList = fulfillmentActivityIdMapDao
                .getRecordsByActivityId(fulfillmentActivityId);
        if (CollectionUtils.isNotEmpty(fulfillmentActivityIdMapList)) {
            fulfillmentData.setFulfillmentActivityIdMapList(fulfillmentActivityIdMapList);
        }
    }

    private void addFulfillmentRollbackDataList(BigInteger fulfillmentActivityId, FulfillmentDataDTO fulfillmentData) {
        List<FulfillmentRollbackDataDTO> fulfillmentRollbackDataList = fulfillmentRollbackDataDao
                .getRecordsByActivityId(fulfillmentActivityId);
        if (CollectionUtils.isNotEmpty(fulfillmentRollbackDataList)) {
            fulfillmentData.setFulfillmentRollbackDataList(fulfillmentRollbackDataList);
        }
    }

    private void addPaymentCheckpointList(BigInteger fulfillmentActivityId, FulfillmentDataDTO fulfillmentData) {
        List<PaymentCheckpointDTO> paymentCheckpointList = paymentCheckpointDao
                .getRecordsByActivityId(fulfillmentActivityId);
        if (CollectionUtils.isNotEmpty(paymentCheckpointList)) {
            fulfillmentData.setPaymentCheckpointList(paymentCheckpointList);
        }
    }

}
