package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpinc.inc.platform.mayfly.MayflyOperationStatus;
import com.jpinc.inc.platform.mayfly.MayflyResponse;
import com.jptest.fulfillmentengine.PYMTMapEntryVO;
import com.jptest.fulfillmentengine.PYMTMapEntryValueVO;
import com.jptest.payments.PaymentReferenceTypeCode;
import com.jptest.payments.ProcessDataRequest;
import com.jptest.payments.WritePaymentDataVO;
import com.jptest.payments.WritePaymentSideReferenceVO;
import com.jptest.payments.fulfillment.testonia.bridge.MayflyBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.vo.ValueObject;
import com.jptest.vo.serialization.Formats;

/**
 * @JP Inc.
 *
 */
public class WritePaymentEventsToMayFlyTask extends BaseTask<Map<String, Set<String>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WritePaymentEventsToMayFlyTask.class);

    @Inject
    private MayflyBridge bridge;

    private ProcessDataRequest processDataRequest;

    private Map<String, Set<String>> mayflyKeyMap;

    private String rootPaymentId;

    private static final String MAYFLY_PYMT_EVENTS_KEY_PREFIX = "PaymentData.PPID_";
    private static final String MAYFLY_PYMT_REFERENCE_KEY_PREFIX = "PaymentReference.";

    public WritePaymentEventsToMayFlyTask(ProcessDataRequest request) {
        this.processDataRequest = request;
    }

    @Override
    public Map<String, Set<String>> process(Context context) {
        mayflyKeyMap = new HashMap<>();
        rootPaymentId = getRootTransactionPaymentID();
        Set<String> mayflyPymtEventKeys = new HashSet<>();
        Set<String> mayflyPymtMapKeys = new HashSet<>();
        writePaymentEventsToMayfly(mayflyPymtEventKeys);
        writePaymentMapToMayfly(mayflyPymtMapKeys);
        mayflyKeyMap.put("Money", mayflyPymtEventKeys);
        mayflyKeyMap.put("Money_IdMap", mayflyPymtMapKeys);
        return mayflyKeyMap;
    }

    private void writePaymentEventsToMayfly(Set<String> mayflyPymtEventKeys) {
        LOGGER.info("Root Transaction Payment ID", rootPaymentId);
        String mayFlyKey = MAYFLY_PYMT_EVENTS_KEY_PREFIX + rootPaymentId;
        try {
            writeDataIntoMayfly(mayFlyKey, processDataRequest, true, mayflyPymtEventKeys);
        } catch (IOException e) {
            LOGGER.error("Error occured while writing into Mayfly", e.getMessage());
        }
    }

    private void writePaymentMapToMayfly(Set<String> mayflyPymtMapKeys) {
        for (WritePaymentDataVO writePaymentDataVO : processDataRequest.getPaymentData()) {
            String paymentSideId = writePaymentDataVO.getPaymentSideId().toString();
            PYMTMapEntryValueVO pymtMapEntryValueVO = new PYMTMapEntryValueVO();
            pymtMapEntryValueVO.setPaymentPaymentId(rootPaymentId);
            pymtMapEntryValueVO.setPaymentsideId(paymentSideId);
            LOGGER.info("Payment ID:{},Payment Side Id:{}", rootPaymentId, paymentSideId);
            List<PYMTMapEntryValueVO> pymtMapEntryValueVOList = new ArrayList<>();
            pymtMapEntryValueVOList.add(pymtMapEntryValueVO);
            for (ValueObject vo : writePaymentDataVO.getPaymentsData()) {
                if (vo instanceof WritePaymentSideReferenceVO) {
                    WritePaymentSideReferenceVO writePaymentSideReferenceVO = (WritePaymentSideReferenceVO) vo;
                    String refType = writePaymentSideReferenceVO.getReferenceTypeAsEnum().getValue();
                    String refValue = writePaymentSideReferenceVO.getReferenceValue();
                    String mayflyKey = MAYFLY_PYMT_REFERENCE_KEY_PREFIX + refType + "_" + refValue;

                    PYMTMapEntryVO pymtMapEntryVO = new PYMTMapEntryVO();
                    pymtMapEntryVO.setEntryValue(pymtMapEntryValueVOList);
                    pymtMapEntryVO.setEntryKey(mayflyKey);
                    try {
                        writeDataIntoMayfly(mayflyKey, pymtMapEntryVO, false, mayflyPymtMapKeys);
                    } catch (IOException e) {
                        LOGGER.error("Error occured while writing into Mayfly", e.getMessage());
                    }
                }
            }
        }
    }

    private void writeDataIntoMayfly(String key, ValueObject vo, boolean useMayflyMoneyServ, Set<String> mayFlyKeys)
            throws IOException {
        LOGGER.info("writeDataIntoMayfly key", key);
        byte[] serializedData = VoHelper.serializeValueObject(vo, Formats.BINARY, false);
        MayflyResponse response = null;
        if (useMayflyMoneyServ) {
            response = bridge.getMayflyMoneyClient().set(key, serializedData);
        } else {
            response = bridge.getMayflyClient().set(key, serializedData);
        }
        if (response.getMayflyOperationStatus() == MayflyOperationStatus.MAYFLY_SUCCESS) {
            mayFlyKeys.add(key);
        }
    }
    
    /**
     * 
     * @return
     */
    private String getRootTransactionPaymentID() {
        /**
         * While building the mayfly Payment data, we append the previous activity's payment data at the end of list, In
         * this way root payment ID will be available at the tail of the list. In this method we are iterating through
         * backwards and getting the first paymentData object's payment side id as root payment id.
         */
        for (int i = processDataRequest.getPaymentData().size() - 1; i >= 0; i--) {
            
            WritePaymentDataVO writePaymentDataVO = processDataRequest.getPaymentData().get(i);
            for (ValueObject vo : writePaymentDataVO.getPaymentsData()) {
                if (vo instanceof WritePaymentSideReferenceVO) {
                    WritePaymentSideReferenceVO paymentSideRefVO = (WritePaymentSideReferenceVO) vo;
                    if (paymentSideRefVO.getReferenceTypeAsEnum() == PaymentReferenceTypeCode.LEGACY_PAYMENT_ID) {
                        return paymentSideRefVO.getReferenceValue();
                    }
                }
            }
        }
        return null;
    }
}
