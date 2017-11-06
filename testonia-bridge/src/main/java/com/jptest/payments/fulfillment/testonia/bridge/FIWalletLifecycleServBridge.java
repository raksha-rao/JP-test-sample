package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import java.math.BigInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.inject.name.Named;
import com.jptest.common.ActorInfoVO;
import com.jptest.financialinstrument.AddFiRequest;
import com.jptest.financialinstrument.AddFiResponse;
import com.jptest.financialinstrument.AnalyzeAddFiRequest;
import com.jptest.financialinstrument.AnalyzeAddFiResponse;
import com.jptest.financialinstrument.FIVO;
import com.jptest.financialinstrument.FIWalletLifecycle;
import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.ProductInstrumentEntityType;
import com.jptest.financialinstrument.ProductInstrumentType;
import com.jptest.financialinstrument.ProductInstrumentVO;
import com.jptest.user.AccountVO;

/**
 * Represents bridge for filifecycleserv API calls
 */
@Singleton
public class FIWalletLifecycleServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(FIWalletLifecycleServBridge.class);

    @Inject
    @Named("fiwalletlifecycle")
    private FIWalletLifecycle fiWalletLifecycle;

    /**
     * Add MSB (merchant specific balance) as FI to funder's account  
     * @param accountNumber
     * @param tabId
     * @return
     */
    public AddFiResponse addMSBAsFI(BigInteger accountNumber, BigInteger tabId) {
        AddFiRequest request = buildRequestForAddMSBAsFI(accountNumber, tabId);
        return addFI(request);
    }

    private AddFiRequest buildRequestForAddMSBAsFI(BigInteger accountNumber, BigInteger tabId) {
        AddFiRequest request = new AddFiRequest();
        // set actor
        ActorInfoVO actorInfo = new ActorInfoVO();
        actorInfo.setActorType((byte) 3);
        request.setClientActor(actorInfo);

        // set account
        AccountVO accountVO = new AccountVO();
        accountVO.setAccountNumber(accountNumber);
        request.setAccountDetails(accountVO);

        // set FIVO
        FIVO fiVO = new FIVO();
        fiVO.setType(FinancialInstrumentTypeClass.FI_TAB);
        ProductInstrumentVO productInstrumentVO = new ProductInstrumentVO();
        productInstrumentVO.setType(ProductInstrumentType.PI_TYPE_TAB_MSB);
        productInstrumentVO.setEntity(ProductInstrumentEntityType.PI_ENTITY_TYPE_jptest);
        productInstrumentVO.setPiAccountNumber(String.valueOf(tabId));
        fiVO.setPiDetails(productInstrumentVO);
        request.setFiDetails(fiVO);
        return request;
    }

    /**
     * Generic AddFI() call
     *  
     * @param request
     * @return
     */
    public AddFiResponse addFI(AddFiRequest request) {
        LOGGER.info("add_fi request: {}", printValueObject(request));
        AddFiResponse response = fiWalletLifecycle.add_fi(request);
        LOGGER.info("add_fi response: {}", printValueObject(response));
        // validate
        if (!response.getSuccess()) {
            Assert.fail("Adding MSB as FI failed");
        }

        return response;
    }

	/**
	 * Generic AnalyzeAaddFI() call
	 * 
	 * @param request
	 * @return
	 */
	public AnalyzeAddFiResponse analyzeAddFI(AnalyzeAddFiRequest request) {
		LOGGER.info("analyze_add_fi request: {}", printValueObject(request));

		AnalyzeAddFiResponse response = fiWalletLifecycle.analyze_add_fi(request);
		LOGGER.info("analyze_add_fi response: {}", printValueObject(response));

		return response;

	}
}
