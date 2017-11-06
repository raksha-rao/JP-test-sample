package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.name.Named;
import com.jptest.common.ActorInfoVO;
import com.jptest.financialinstrument.FIWalletRead;
import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.GetFisByAccountRequest;
import com.jptest.financialinstrument.GetFisByAccountResponse;
import com.jptest.financialinstrument.PagingVO;
import com.jptest.financialinstrument.WalletInstrumentSearchCriteriaVO;

/**
 * Represents bridge for fininstreadserv API calls
 */
@Singleton
public class FinInstReadServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(FinInstReadServBridge.class);

    @Inject
    @Named("fininstreadserv")
    private FIWalletRead fiWalletRead;

    public GetFisByAccountResponse getFisByAccount(BigInteger accountNumber) {
        GetFisByAccountRequest request = buildRequest(accountNumber);
        LOGGER.info("get_fis_by_account request: {}", printValueObject(request));
        GetFisByAccountResponse response = fiWalletRead.get_fis_by_account(request);
        LOGGER.info("get_fis_by_account response: {}", printValueObject(response));
        if (!response.getSuccess()) {
            Assert.fail("Account not found for accountNumber " + accountNumber);
        }

        return response;
    }

    private GetFisByAccountRequest buildRequest(BigInteger accountNumber) {
        GetFisByAccountRequest request = new GetFisByAccountRequest();
        // 1. set actor
        request.setClientActor(createActorInfo(accountNumber));
        request.setAccountNumber(accountNumber);
        request.setPaging(createPaging());
        request.setInstrumentSearchCriteria(createInstrumentSearchCriterias());
        request.setIncludeAccount(true);
        return request;
    }

    private ActorInfoVO createActorInfo(BigInteger accountNumber) {
        ActorInfoVO actorInfoVO = new ActorInfoVO();
        actorInfoVO.setActorAccountNumber(accountNumber);
        actorInfoVO.setActorType((byte) 10);
        actorInfoVO.setActorAuthType((byte) 0);
        actorInfoVO.setActorAuthCredential((byte) 0);
        actorInfoVO.setActorId(accountNumber);
        actorInfoVO.setActorSessionId(null);
        actorInfoVO.setActorIpAddr("10.57.212.92");
        actorInfoVO.setEntryPoint(null);
        actorInfoVO.setGuid(BigInteger.ZERO);
        actorInfoVO.setToken(null);
        actorInfoVO.setTokenType(Byte.valueOf("0"));
        return actorInfoVO;
    }

    private PagingVO createPaging() {
        PagingVO pagingVO = new PagingVO();
        pagingVO.setMaxRows((short) 15);
        pagingVO.setStartRow((short) 0);
        return pagingVO;
    }

    private List<WalletInstrumentSearchCriteriaVO> createInstrumentSearchCriterias() {
        List<WalletInstrumentSearchCriteriaVO> instrumentSearchCriteriaVOs = new ArrayList<WalletInstrumentSearchCriteriaVO>();
        WalletInstrumentSearchCriteriaVO instrumentSearchCriteria = null;
        for (FinancialInstrumentTypeClass fiType : FinancialInstrumentTypeClass
                .values()) {
            switch (fiType.getName()) {
            // List of un-handled funding sources in the utility
            case "FI_UNKNOWN":
            case "FI_INCENTIVE":
            case "FI_TREASURY":
            case "FI_RESTRICTED_HOLDING":
            case "FI_CHINESE_BANK_ACCOUNT":
            case "FI_BANK_CARD":
            case "FI_CHINA_UNIONPAY_CARD":
            case "FI_GENERIC_INSTRUMENT":
                break;
            // Adding the supported funding sources to the search criteria
            default:
                instrumentSearchCriteria = new WalletInstrumentSearchCriteriaVO();
                instrumentSearchCriteria.setFiType(fiType);
                instrumentSearchCriteria.setIncludeIssuerDetails(true);
                instrumentSearchCriteriaVOs.add(instrumentSearchCriteria);
            }
        }
        return instrumentSearchCriteriaVOs;
    }
    
    public GetFisByAccountResponse getFisByAccount(BigInteger accountNumber, String walletId) {
        GetFisByAccountRequest request = buildRequest(accountNumber);
        request.getInstrumentSearchCriteria().get(0).setFiId(walletId);
        LOGGER.info("get_fis_by_account request: {}", printValueObject(request));
        GetFisByAccountResponse response = fiWalletRead.get_fis_by_account(request);
        LOGGER.info("get_fis_by_account response: {}", printValueObject(response));

        return response;
    }

    

}
