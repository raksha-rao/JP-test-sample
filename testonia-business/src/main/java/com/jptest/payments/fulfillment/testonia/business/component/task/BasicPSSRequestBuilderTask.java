package com.jptest.payments.fulfillment.testonia.business.component.task;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.jptest.common.ActorInfoVO;
import com.jptest.common.DateRangeVO;
import com.jptest.money.AccountCriteriaVO;
import com.jptest.money.PagingControlVO;
import com.jptest.money.SearchCriteriaVO;
import com.jptest.money.SearchForCustomerTransactionLegacyEquivalentsRequest;
import com.jptest.money.SearchForCustomerTransactionLegacyEquivalentsResponse;
import com.jptest.money.SearchPeriodVO;
import com.jptest.money.SortOrder;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentSearchServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.vo.ValueObject;


/**
 * Basic PSS Request Builder Builds the request based on the buyer account number
 */

public class BasicPSSRequestBuilderTask extends BaseTask<ValueObject> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicPSSRequestBuilderTask.class);
    private BigInteger accountNumber = null;

    @Inject
    private PaymentSearchServBridge paymentSearchServBridge;

    @Override
    public ValueObject process(final Context context) {

        SearchForCustomerTransactionLegacyEquivalentsResponse pssResponse = null;
        final User buyer = (User) context.getData(ContextKeys.BUYER_VO_KEY.getName());
        Assert.assertNotNull(buyer.getAccountNumber());
        this.accountNumber = new BigInteger(buyer.getAccountNumber());
        pssResponse = this.searchForCustomerTransactionLegacyEquivalents(pssResponse);
        context.setData(ContextKeys.PSS_RESPONSE_KEY.getName(), pssResponse);
        return pssResponse;
    }

    /**
     * Returns transaction details for input accountNumber By calling PSS
     *
     * @param accountNumber
     * @return
     * @throws IOException
     */
    public SearchForCustomerTransactionLegacyEquivalentsResponse searchForCustomerTransactionLegacyEquivalents(
            SearchForCustomerTransactionLegacyEquivalentsResponse pssResponse)
                    throws TestExecutionException {

        final SearchForCustomerTransactionLegacyEquivalentsRequest request = this
                .buildSearchForCustomerTransactionLegacyEquivalentsRequest();

        try {
            LOGGER.info("PSS SearchForCustomerTransactionLegacyEquivalents request: {}", printValueObject(request));
            pssResponse = this.paymentSearchServBridge.searchForCustomerTransactionLegacyEquivalents(request);
            LOGGER.info("PSS SearchForCustomerTransactionLegacyEquivalents response: {}",
                    printValueObject(pssResponse));

        }
        catch (final Exception e) {
            LOGGER.error("Encountered exception while calling PaymentSearchService");
            throw new TestExecutionException("Encountered exception while calling PaymentSearchService");
        }

        if (pssResponse == null) {
            throw new TestExecutionException("Invalid/Empty Response from PSS");
        }
        return pssResponse;
    }

    private SearchForCustomerTransactionLegacyEquivalentsRequest buildSearchForCustomerTransactionLegacyEquivalentsRequest() {
        final SearchForCustomerTransactionLegacyEquivalentsRequest request = new SearchForCustomerTransactionLegacyEquivalentsRequest();
        request.setActor(new ActorInfoVO());
        request.setPagingControl(this.buildPagingControlVO());
        request.setCriteria(this.buildSearchCriteriaVO());
        request.setSortOrder(SortOrder.INITIATION_TIME_ASC);
        return request;
    }

    private PagingControlVO buildPagingControlVO() {
        final PagingControlVO pagingControlVO = new PagingControlVO();
        pagingControlVO.setPageSize(Integer.valueOf(10));
        pagingControlVO.setStartIndex(Integer.valueOf(0));
        return pagingControlVO;
    }

    private SearchCriteriaVO buildSearchCriteriaVO() {
        final SearchCriteriaVO searchCriteriaVO = new SearchCriteriaVO();
        final AccountCriteriaVO accountCriteraVO = new AccountCriteriaVO();
        final List<BigInteger> include = new ArrayList<BigInteger>();
        include.add(this.accountNumber);
        accountCriteraVO.setInclude(include);
        searchCriteriaVO.setAccountCriteria(accountCriteraVO);

        final SearchPeriodVO searchVO = new SearchPeriodVO();
        final DateRangeVO daterange = new DateRangeVO();
        daterange.setLowerBound(1317157336L);
        daterange.setLowerInclusive(true);
        daterange.setUpperInclusive(true);
        final long timestamp = System.currentTimeMillis() / 1000;
        daterange.setUpperBound(timestamp);
        searchVO.setPeriod(daterange);
        searchCriteriaVO.setPeriod(searchVO);
        return searchCriteriaVO;
    }
}
