package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import com.jptest.common.ActorInfoVO;
import com.jptest.financialinstrument.AnalyzeAddFiRequest;
import com.jptest.financialinstrument.AnalyzeAddFiResponse;
import com.jptest.financialinstrument.FIVO;
import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.ProductInstrumentBeneficiaryDetails;
import com.jptest.financialinstrument.ProductInstrumentDenominationDetails;
import com.jptest.financialinstrument.ProductInstrumentDenominationType;
import com.jptest.financialinstrument.ProductInstrumentIssuerDetails;
import com.jptest.financialinstrument.ProductInstrumentVO;
import com.jptest.payments.fulfillment.testonia.bridge.FIWalletLifecycleServBridge;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.user.AccountType;
import com.jptest.user.AccountVO;
import com.jptest.user.AddressVO;
import com.jptest.user.ModifyUserDataResponse;
import com.jptest.user.TypeOfAccount;
import com.jptest.user.UserNameVO;

/**
 * This task is to call analyzeAaddFI with dccBuyer and creditCard Info
 * 
 * @JP Inc.
 *
 */

public class AnalyzeAddFiTask extends BaseTask<AnalyzeAddFiResponse> {
	@Inject
	private FIWalletLifecycleServBridge fiWalletLifecycleServBridge;

	public final AnalyzeAddFiResponse process(Context context) {
		try {
			AnalyzeAddFiRequest request = buildRequest(context);
			AnalyzeAddFiResponse response = fiWalletLifecycleServBridge.analyzeAddFI(request);
			return response;
		} catch (final Exception e) {
			throw new TestExecutionException("Failed executing  AnalyzeAddFi call", e);
		}
	}

	private AnalyzeAddFiRequest buildRequest(Context context) {

		AnalyzeAddFiRequest request = new AnalyzeAddFiRequest();
		request.setAccountDetails(getAccountDeatails());
		request.setGenerateId(true);
		request.setFiDetails(getFIDetails(context));
		request.setClientActor(getClientActor());

		return request;
	}

	private ActorInfoVO getClientActor() {
		ActorInfoVO actorInfoVO = new ActorInfoVO();
		actorInfoVO.setActorType((byte) (4));

		return actorInfoVO;
	}

	private FIVO getFIDetails(Context context) {
		FIVO fiVo = new FIVO();
		fiVo.setType(FinancialInstrumentTypeClass.FI_CARD);
		fiVo.setPiDetails(getsPiDetails(context));

		return fiVo;
	}

	private ProductInstrumentVO getsPiDetails(Context context) {
		Creditcard credicard = (Creditcard) context.getData(ContextKeys.GENERATE_CREDIT_CARD.getName());

		ProductInstrumentVO productInstrumentvo = new ProductInstrumentVO();

		productInstrumentvo.setPiAccountNumber(credicard.getCardNumber());
		if (credicard.getCardType().equalsIgnoreCase("VISA"))
			productInstrumentvo.setCheckDigits("111");
		else
			productInstrumentvo.setCheckDigits("1111");

		Calendar expDate = Calendar.getInstance();
		TimeZone currentTimeZone = expDate.getTimeZone();
		expDate.set(credicard.getExpirationYear(), credicard.getExpirationMonth(),
				expDate.getActualMaximum(Calendar.DATE), 23, 59, 59);

		//// to make GMT from PST
		int offset = currentTimeZone.getOffset(expDate.getTimeInMillis());
		Long lastTimeofMonthOffset = (expDate.getTimeInMillis() + offset) / 1000L;
		BigInteger unixTime = new BigInteger(lastTimeofMonthOffset.toString());
		productInstrumentvo.setExpirationTime(unixTime);

		productInstrumentvo.setBeneficiaryDetails(getPiBeneficiaryListAddFi(context));

		productInstrumentvo.setIssuerDetails(getproductInstrumentIssuerDetails(credicard));

		productInstrumentvo.setDenominationDetails(getDenominationDetails());
		productInstrumentvo.setBillingAddress(getBillinAddress(context));

		return productInstrumentvo;
	}

	private AddressVO getBillinAddress(Context context) {
		ModifyUserDataResponse dccBuyer = (ModifyUserDataResponse) context.getData(ContextKeys.BUYER_VO_KEY.getName());

		AddressVO addressVO = new AddressVO();
		addressVO.setAddress1(dccBuyer.getAddress().get(0).getAddress1());
		addressVO.setAddress2(dccBuyer.getAddress().get(0).getAddress2());
		addressVO.setCity(dccBuyer.getAddress().get(0).getCity());
		addressVO.setState(dccBuyer.getAddress().get(0).getState());
		return addressVO;
	}

	private ProductInstrumentDenominationDetails getDenominationDetails() {
		ProductInstrumentDenominationDetails productInstrumentDenominationDetails = new ProductInstrumentDenominationDetails();

		productInstrumentDenominationDetails.setType(ProductInstrumentDenominationType.PI_DENOMINATION_TYPE_CURRENCY);
		return productInstrumentDenominationDetails;
	}

	private ProductInstrumentIssuerDetails getproductInstrumentIssuerDetails(Creditcard creditcard) {
		ProductInstrumentIssuerDetails productInstrumentIssuerDetails = new ProductInstrumentIssuerDetails();
		productInstrumentIssuerDetails.setCountryCode(creditcard.getCountry());
		return productInstrumentIssuerDetails;
	}

	private List<ProductInstrumentBeneficiaryDetails> getPiBeneficiaryListAddFi(Context context) {
		ModifyUserDataResponse dccBuyer = (ModifyUserDataResponse) context.getData(ContextKeys.BUYER_VO_KEY.getName());

		UserNameVO userNameVo = new UserNameVO();

		userNameVo.setAccountFullNameExtended(
				dccBuyer.getName().get(0).getFirstName() + " " + dccBuyer.getName().get(0).getLastName());

		userNameVo.setFirstName(dccBuyer.getName().get(0).getFirstName());
		userNameVo.setLastName(dccBuyer.getName().get(0).getLastName());

		ProductInstrumentBeneficiaryDetails piBeneficiaryDetails = new ProductInstrumentBeneficiaryDetails();
		piBeneficiaryDetails.setName(userNameVo);
		piBeneficiaryDetails.setNameAsInIssuer(dccBuyer.getName().get(0).getFirstName());
		piBeneficiaryDetails.setDOB("1984-09-27");

		List<ProductInstrumentBeneficiaryDetails> piBeneficiaryListaddFI = new ArrayList<ProductInstrumentBeneficiaryDetails>();
		piBeneficiaryListaddFI.add(piBeneficiaryDetails);
		return piBeneficiaryListaddFI;
	}

	private AccountVO getAccountDeatails() {
		AccountVO accountVO = new AccountVO();
		accountVO.setType(TypeOfAccount.ANONYMOUS);
		accountVO.setAccountType(AccountType.ANONYMOUS);
		accountVO.setLegalCountry("US");
		return accountVO;
	}
}
