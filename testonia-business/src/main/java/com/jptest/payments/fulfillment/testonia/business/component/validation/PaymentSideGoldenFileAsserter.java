package com.jptest.payments.fulfillment.testonia.business.component.validation;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import com.jptest.money.WTransactionVO;
import com.jptest.payments.fulfillment.testonia.business.ignorable.flags.UnsetIgnorableFlagsHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException.TestoniaExceptionReasonCode;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.fulfillment.testonia.dao.pymt.CurrencyExchangeDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.FeeCompositionDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.MoneyMovementDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.PaymentSideDao;
import com.jptest.payments.fulfillment.testonia.dao.pymt.WithHeldBalancechangeDao;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.fulfillment.testonia.model.GoldenFileComparisonTaskInput;
import com.jptest.payments.fulfillment.testonia.model.pymt.CurrencyExchangeDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.FeeCompositionDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.MoneyMovementDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PaymentSideValidationFixtureDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.PymtTablesDTO;
import com.jptest.payments.fulfillment.testonia.model.pymt.WithHeldBalanceChangeDTO;


/*** Makes VOX call by passing payment_side schema and then compares output with golden payment_side XML file */

public class PaymentSideGoldenFileAsserter extends GoldenFileComparisonAsserter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentSideGoldenFileAsserter.class);

    private static final String OPERATION = "PaymentSide-Validation";

    @Inject
    private PaymentSideDao paymentSideDao;

    @Inject
    private MoneyMovementDao moneyMovementDao;

    @Inject
    private FeeCompositionDao feeCompositionDao;

    @Inject
    private CurrencyExchangeDao currencyExchangeDao;

    @Inject
    private WithHeldBalancechangeDao withHeldBalanceChangeDao;

    @Inject
    private XMLHelper xmlHelper;

    @Inject
    private PaymentSideXmlVirtualizationTask xmlVirtualizationTask;

    @Inject
    private WTransactionP20DaoImpl wTxnP20DaoImpl;

    @Inject
    private UnsetIgnorableFlagsHelper unsetIgnorableFlagsHelper;

    public PaymentSideGoldenFileAsserter(GoldenFileComparisonTaskInput input) {
        super(input);
    }

    @Override
    public String getValidationType() {
        return OPERATION;
    }

    @Override
    public TestoniaExceptionReasonCode getDiffReasonCode() {
        return TestoniaExceptionReasonCode.FAILURE_PYMT_DBDIFF;
    }

    @Override
    protected Document getActualResponseXml(Context context) {
        List<WTransactionVO> wTransactionList = (List<WTransactionVO>) getDataFromContext(context,
                ContextKeys.WTRANSACTION_LIST_KEY.getName());
        String ignorableFlagsLocation = (String) getDataFromContext(context,
                ContextKeys.IGNORABLE_FLAGS_LOCATION.getName());
        Set<String> paymentIds = getPaymentIdsByTxnIds(wTransactionList);
        List<PaymentSideDTO> paymentSideDTO = paymentSideDao.findByPaymentId(paymentIds);
        return getActualResponse(paymentSideDTO, ignorableFlagsLocation);
    }

    /**
     * Populate response object from Database
     *
     * @param senderTransaction
     * @return
     */
    private Document getActualResponse(List<PaymentSideDTO> paymentSideDTO, String ignorableFlagsLocation) {
        PaymentSideValidationFixtureDTO resultDTO = new PaymentSideValidationFixtureDTO();
        PymtTablesDTO pymtDTO = new PymtTablesDTO();
        pymtDTO.addPaymentSideDTO(paymentSideDTO);
        Set<String> paymentSideIds = getPaymentSideIds(paymentSideDTO);
        pymtDTO.addMoneyMovementDTO(addMoneyMovementDTO(paymentSideIds));
        pymtDTO.addfeeCompositionDTO(getFeeCompositionDTO(paymentSideIds));
        pymtDTO.addCurrencyExchangeDTO(getCurrencyExchangeDTO(paymentSideIds));
        pymtDTO.addWithHeldBalChangeDTO(getWithHeldBalanceDTO(paymentSideIds));
        resultDTO.setPaymentSideDTO(pymtDTO);
        try {
            Document doc = resultDTO.getDocument();
            unsetIgnorableFlagsHelper.unsetFlags(ignorableFlagsLocation, doc);
            doc = processActualDocument(doc);
            doc = xmlVirtualizationTask.execute(doc);
            LOGGER.info("PYMT Actual XML: \n{}", xmlHelper.getPrettyXml(doc));
            return doc;
        }
        catch (ParserConfigurationException | JAXBException e) {
            throw new TestExecutionException(e);
        }
    }

    protected Document processActualDocument(Document doc) {
        return doc;
    }

    private List<MoneyMovementDTO> addMoneyMovementDTO(Set<String> paymentSideIds) {

        List<MoneyMovementDTO> moneyMovementDTOs = moneyMovementDao.findByPaymentSideIds(paymentSideIds);
        return moneyMovementDTOs;
    }

    private Set<String> getPaymentSideIds(List<PaymentSideDTO> paymentSideDTO) {
        //Using LinkedHashSet to maintain the order in which the paymentside ids are added
        Set<String> paymentSideIds = new LinkedHashSet<String>();
        for (int i = 0; i < paymentSideDTO.size(); i++) {
            paymentSideIds.add(paymentSideDTO.get(i).getId().toString());
        }
        return paymentSideIds;
    }

    private List<FeeCompositionDTO> getFeeCompositionDTO(Set<String> paymentSideIds) {
        List<FeeCompositionDTO> feeCompositionDTOs = feeCompositionDao.findByPaymentSideIds(paymentSideIds);
        return feeCompositionDTOs;
    }

    private List<CurrencyExchangeDTO> getCurrencyExchangeDTO(Set<String> paymentSideIds) {
        List<CurrencyExchangeDTO> currencyExchangeDTO = currencyExchangeDao.findByPaymentSideIds(paymentSideIds);
        return currencyExchangeDTO;
    }

    private List<WithHeldBalanceChangeDTO> getWithHeldBalanceDTO(Set<String> paymentSideIds) {
        List<WithHeldBalanceChangeDTO> withHeldBalanceChangeDTO = withHeldBalanceChangeDao
                .findByPaymentSideIds(paymentSideIds);
        return withHeldBalanceChangeDTO;
    }
    
    public Set<String> getPaymentIdsByTxnIds(List<WTransactionVO> txnIds) {
        Set<String> paymentIds = new HashSet<String>();
        for (int i = 0; i < txnIds.size(); i++) {
            String paymentId = wTxnP20DaoImpl.getPaymentIdByTxnId(txnIds.get(i).getId().toString());
            if (!paymentIds.contains(paymentId))
                paymentIds.add(paymentId);
        }
        return paymentIds;
    }

}
