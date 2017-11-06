package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.jptest.common.ActorInfoVO;
import com.jptest.financialinstrument.AnalyzeAddFiResponse;
import com.jptest.financialinstrument.FIVO;
import com.jptest.financialinstrument.FinancialInstrumentBlockedStatus;
import com.jptest.financialinstrument.FinancialInstrumentTypeClass;
import com.jptest.financialinstrument.ProductInstrumentBeneficiaryDetails;
import com.jptest.financialinstrument.ProductInstrumentBeneficiaryNameFormat;
import com.jptest.financialinstrument.ProductInstrumentDenominationDetails;
import com.jptest.financialinstrument.ProductInstrumentDenominationType;
import com.jptest.financialinstrument.ProductInstrumentEntityType;
import com.jptest.financialinstrument.ProductInstrumentIdentifierAttribute;
import com.jptest.financialinstrument.ProductInstrumentIdentifierType;
import com.jptest.financialinstrument.ProductInstrumentType;
import com.jptest.financialinstrument.ProductInstrumentVO;
import com.jptest.financialinstrument.WalletInstrumentStatus;
import com.jptest.financialinstrument.WalletInstrumentStatusDetails;
import com.jptest.financialinstrument.WalletInstrumentVO;
import com.jptest.money.DisbursementPolicyType;
import com.jptest.money.FeePayerPolicyType;
import com.jptest.money.FeePolicyVO;
import com.jptest.money.FundingMethodType;
import com.jptest.money.IntegrationType;
import com.jptest.money.JournaledDataExtensionVO;
import com.jptest.money.ParticipantExtensionsVO;
import com.jptest.money.PayV2Request;
import com.jptest.money.PayV2Response;
import com.jptest.money.PaymentAliasType;
import com.jptest.money.PaymentFlagsVO;
import com.jptest.money.PaymentFlowContextVO;
import com.jptest.money.PaymentParametersVO;
import com.jptest.money.PaymentPartyVO;
import com.jptest.money.PreferredFundingVO;
import com.jptest.money.PurchaseContextVO;
import com.jptest.payments.fulfillment.testonia.bridge.PaymentServCABridge;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.qi.rest.domain.pojo.Creditcard;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.risk.DirectCardPreferencesVO;
import com.jptest.types.Currency;
import com.jptest.user.AccountStatus;
import com.jptest.user.AccountType;
import com.jptest.user.AccountUserGroup;
import com.jptest.user.AccountVO;
import com.jptest.user.AddressType;
import com.jptest.user.AddressVO;
import com.jptest.user.DOBConfirmationAuthority;
import com.jptest.user.DOBConfirmationStatus;
import com.jptest.user.DOBType;
import com.jptest.user.EmailVO;
import com.jptest.user.LegalEntity;
import com.jptest.user.ModifyDateOfBirthVO;
import com.jptest.user.ModifyUserDataResponse;
import com.jptest.user.NewAccountVO;
import com.jptest.user.NewAddressVO;
import com.jptest.user.NewEmailVO;
import com.jptest.user.NewNameVO;
import com.jptest.user.NewPartyVO;
import com.jptest.user.PartyType;
import com.jptest.user.TypeOfAccount;
import com.jptest.user.UserFlagsVO;
import com.jptest.user.UserNameVO;
import com.jptest.vo.ValueObject;

/**
 * This task will build the PayV2(DCC) request and make PayV2 call
 * 
 * @JP Inc.
 * 
 **/

public class PayV2ExecutionTask extends BaseTask<PayV2Response> {
	@Inject
	PaymentServCABridge paymentServCABridge;

	TransactionAmountInput input;

	private BigInteger accountNumber;
	private String idempotencyId;
	private static final String buyerIpAddress = "10" + "." + "57" + "." + "212" + "." + "92";

	public PayV2ExecutionTask(TransactionAmountInput amount) {
		super();
		this.input = amount;
	}

	public PayV2Response process(Context context) {

		try {
			final PayV2Request payV2Request = buildRequest(context);
			PayV2Response payV2Response = paymentServCABridge.payV2(payV2Request);
            context.addReportingAttribute(ReportingAttributes.PAY_V2_TRANSACTION_HANDLE,
                    payV2Response.getFulfillmentHandle());
			return payV2Response;
		} catch (final Exception e) {
			throw new TestExecutionException("Failed executing  PayV2 call", e);
		}

	}

	private PayV2Request buildRequest(Context context) {
		User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
		ModifyUserDataResponse dccBuyer = (ModifyUserDataResponse) context.getData(ContextKeys.BUYER_VO_KEY.getName());
		PayV2Request payV2Request = new PayV2Request();

		payV2Request.setActorInfo(getActorInfo(context));
		idempotencyId = seller.getAccountNumber() + "_" + dccBuyer.getAccount().getAccountNumber() + "_"
				+ System.currentTimeMillis();
		payV2Request.setIdempotenceId(idempotencyId);
		payV2Request.setPaymentParameters(getPaymentParameters(context));
		payV2Request.setJournalData(getJournalData(context));

		return payV2Request;
	}

	private List<JournaledDataExtensionVO> getJournalData(Context context) {
		List<JournaledDataExtensionVO> journalDataList = new ArrayList<JournaledDataExtensionVO>();
		JournaledDataExtensionVO journaledDataExtensionVO = new JournaledDataExtensionVO();
		journaledDataExtensionVO.setName("User");
		journaledDataExtensionVO.setData(getNewAccount(context));
		journalDataList.add(journaledDataExtensionVO);

		JournaledDataExtensionVO journaledFIDataExtensionVO = new JournaledDataExtensionVO();
		journaledFIDataExtensionVO.setName("FI");
		journaledFIDataExtensionVO.setData(getInstrument(context));
		journalDataList.add(journaledFIDataExtensionVO);

		return journalDataList;
	}

	private NewAccountVO getNewAccount(Context context) {

		ModifyUserDataResponse dccBuyer = (ModifyUserDataResponse) context.getData(ContextKeys.BUYER_VO_KEY.getName());
		Creditcard credicard = (Creditcard) context.getData(ContextKeys.GENERATE_CREDIT_CARD.getName());

		NewAccountVO newAccountVO = new NewAccountVO();
		newAccountVO.setAccountNumber(dccBuyer.getAccount().getAccountNumber());
		newAccountVO.setType(TypeOfAccount.ANONYMOUS);
		newAccountVO.setAccountType(AccountType.ANONYMOUS);
		newAccountVO.setLegalCountry(dccBuyer.getAccount().getLegalCountry());
		newAccountVO.setNewOwner(getNewParty(dccBuyer));
		newAccountVO.setPrimaryCurrencyCode(credicard.getCurrency());
		newAccountVO.setCitizenship(dccBuyer.getParty().getCitizenship());

		return newAccountVO;
	}

	private List<NewPartyVO> getNewParty(ModifyUserDataResponse dccBuyer) {
		NewPartyVO newPartyVO = new NewPartyVO();
		newPartyVO.setType(PartyType.PERSON);
		newPartyVO.setCitizenship(dccBuyer.getParty().getCitizenship());

		// setting name
		NewNameVO newNameVO = new NewNameVO();
		newNameVO.setFirstName(dccBuyer.getName().get(0).getFirstName());
		newNameVO.setLastName(dccBuyer.getName().get(0).getLastName());
		List<NewNameVO> newNameList = new ArrayList<NewNameVO>();
		newNameList.add(newNameVO);
		newPartyVO.setNewName(newNameList);

		// setting Email
		NewEmailVO newEmailVO = new NewEmailVO();
		newEmailVO.setEmail(dccBuyer.getEmail().get(0).getEmail());
		newEmailVO.setMakePrimary(true);
		List<NewEmailVO> newEmailList = new ArrayList<NewEmailVO>();
		newEmailList.add(newEmailVO);
		newPartyVO.setNewEmail(newEmailList);

		// setting Address
		NewAddressVO newAddressVO = new NewAddressVO();
		newAddressVO.setType(AddressType.HOME_OR_WORK);
		newAddressVO.setAddress1(dccBuyer.getAddress().get(0).getAddress1());
		newAddressVO.setCity(dccBuyer.getAddress().get(0).getCity());
		newAddressVO.setState(dccBuyer.getAddress().get(0).getState());
		newAddressVO.setZip(dccBuyer.getAddress().get(0).getZip());
		newAddressVO.setIsoCountry(dccBuyer.getAddress().get(0).getIsoCountry());
		newAddressVO.setMakePrimary(true);
		List<NewAddressVO> newAddressList = new ArrayList<NewAddressVO>();
		newAddressList.add(newAddressVO);
		newPartyVO.setNewAddress(newAddressList);

		// setting Birth Date
		ModifyDateOfBirthVO modifyDateOfBirthVO = new ModifyDateOfBirthVO();
		modifyDateOfBirthVO.setBirthDate("19840927");
		modifyDateOfBirthVO.setType(DOBType.BIRTH_DATE);
		modifyDateOfBirthVO.setConfirmationStatus(DOBConfirmationStatus.VALID);
		modifyDateOfBirthVO.setConfirmationAuthority(DOBConfirmationAuthority.USER);
		newPartyVO.setNewDateOfBirth(modifyDateOfBirthVO);

		List<NewPartyVO> newPartyList = new ArrayList<NewPartyVO>();
		newPartyList.add(newPartyVO);
		return newPartyList;
	}

	private PaymentParametersVO getPaymentParameters(Context context) {
		User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
		ModifyUserDataResponse dccBuyer = (ModifyUserDataResponse) context.getData(ContextKeys.BUYER_VO_KEY.getName());
		PurchaseContextVO PurchaseContext = (PurchaseContextVO) context.getData(ContextKeys.PURCHASE_CONTEXT.getName());

		PaymentParametersVO paymentParametersVO = new PaymentParametersVO();
		paymentParametersVO.setSender(getSender(dccBuyer));
		paymentParametersVO.setReceiver(gerReceiver(seller));
		paymentParametersVO.setPaymentAmount(input.getTxnAmount());

		paymentParametersVO.setPaymentFlowContext(getPaymentFlow());

		paymentParametersVO.setPreferredFunding(getPreferredFunding(context));
		paymentParametersVO.setFeePolicy(getFee());

		paymentParametersVO.setPurchaseContext(PurchaseContext);

		paymentParametersVO.setDisbursementPolicy(getDisbursementPolicyType());

		paymentParametersVO.setPassThroughData(getPassThroughData());
		paymentParametersVO.setMessage("TestDCC");

		return paymentParametersVO;
	}

	protected List<ParticipantExtensionsVO> getPassThroughData() {
		ParticipantExtensionsVO participantExtensionsVO = new ParticipantExtensionsVO();
		participantExtensionsVO.setName("Risk::DirectCardPreferencesVO");
		participantExtensionsVO.setData(getDirectCardPreferences());

		List<ParticipantExtensionsVO> participantExtensList = new ArrayList<ParticipantExtensionsVO>();
		participantExtensList.add(participantExtensionsVO);
		return participantExtensList;
	}

	private ValueObject getDirectCardPreferences() {
		DirectCardPreferencesVO directCardPreferencesVO = new DirectCardPreferencesVO();
		directCardPreferencesVO.setBillingAddressIsPartial(false);
		directCardPreferencesVO.setCvvIsOptional(true);
		directCardPreferencesVO.setCvvPresent(true);
		directCardPreferencesVO.setBuyerIpAddress(buyerIpAddress);

		return directCardPreferencesVO;
	}

	private FeePolicyVO getFee() {
		FeePolicyVO feePolicyVO = new FeePolicyVO();
		feePolicyVO.setPayerPolicy(FeePayerPolicyType.RECIPIENTS);
		return feePolicyVO;
	}

	private PreferredFundingVO getPreferredFunding(Context context) {
		PreferredFundingVO preferredFundingVO = new PreferredFundingVO();
		preferredFundingVO.setForceInstrument(getInstrument(context));
		preferredFundingVO.setForceFundingMethod(FundingMethodType.CHARGE);
		return preferredFundingVO;
	}

	private WalletInstrumentVO getInstrument(Context context) {
		AnalyzeAddFiResponse analyzeFiResponse = (AnalyzeAddFiResponse) context
				.getData(ContextKeys.ANALYZE_ADD_FI_RESPONSE_KEY.getName());

		WalletInstrumentVO walletInstrumentVO = new WalletInstrumentVO();
		walletInstrumentVO.setId(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getId());
		walletInstrumentVO.setFiDetails(getFiDetails(context, analyzeFiResponse));
		walletInstrumentVO.setStatusDetails(getStatusDetails(analyzeFiResponse));
		return walletInstrumentVO;
	}

	private WalletInstrumentStatusDetails getStatusDetails(AnalyzeAddFiResponse analyzeFiResponse) {
		WalletInstrumentStatusDetails walletInstrumentStatusDetails = new WalletInstrumentStatusDetails();

		walletInstrumentStatusDetails.setStatus(WalletInstrumentStatus.WALLET_INSTRUMENT_STATUS_ACTIVE);
		walletInstrumentStatusDetails.setTimeAdded(
				analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getStatusDetails().getTimeAdded());
		return walletInstrumentStatusDetails;
	}

	private FIVO getFiDetails(Context context, AnalyzeAddFiResponse analyzeFiResponse) {
		FIVO fivo = new FIVO();
		fivo.setId(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getFiDetails().getId());
		fivo.setType(FinancialInstrumentTypeClass.FI_CARD);
		fivo.setBlockedStatus(FinancialInstrumentBlockedStatus.FI_STATUS_UNBLOCKED);
		fivo.setPiDetails(getPiDetails(context, analyzeFiResponse));
		return fivo;
	}

	private ProductInstrumentVO getPiDetails(Context context,
			AnalyzeAddFiResponse analyzeFiResponse) {
		ProductInstrumentVO productInstrumentVO = new ProductInstrumentVO();
		productInstrumentVO.setType(ProductInstrumentType.PI_TYPE_CARD_VISA);
		productInstrumentVO.setProductType("CREDIT_CARD");
		productInstrumentVO.setEntity(ProductInstrumentEntityType.PI_ENTITY_TYPE_jptest);
		productInstrumentVO.setIdentifiers(getProductInstrumentIdentifierAttribute());

		productInstrumentVO.setPiAccountNumberHmac(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getPiAccountNumberHmac());
		productInstrumentVO.setPiAccountNumberEncrypted(analyzeFiResponse.getCandidateInstrument()
				.getInstrumentDetails().getFiDetails().getPiDetails().getPiAccountNumberEncrypted());
		productInstrumentVO.setAccountNumberLastNChars(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getAccountNumberLastNChars());

		productInstrumentVO.setCheckDigits(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getCheckDigits());
		productInstrumentVO.setUsageTypeOverridden(false);
		productInstrumentVO.setDenominationDetails(getDenominationDetails());

		// productInstrumentVO.setIssuerDetails(getIssuerDetails());

		productInstrumentVO.setExpirationTime(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getExpirationTime());

		productInstrumentVO.setBillingAddress(getBillinAddress(context));
		productInstrumentVO.setBeneficiaryDetails(getPiBeneficiaryList(analyzeFiResponse));

		productInstrumentVO.setCapabilityDetails(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getCapabilityDetails());

		productInstrumentVO.setInstrumentChecksum(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails()
				.getFiDetails().getPiDetails().getInstrumentChecksum());

		return productInstrumentVO;
	}

	private List<ProductInstrumentBeneficiaryDetails> getPiBeneficiaryList(AnalyzeAddFiResponse analyzeFiResponse) {
		UserNameVO userNameVo = new UserNameVO();

		userNameVo.setFirstName(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getFiDetails()
				.getPiDetails().getBeneficiaryDetails().get(0).getName().getFirstName());
		userNameVo.setLastName(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getFiDetails()
				.getPiDetails().getBeneficiaryDetails().get(0).getName().getLastName());

		ProductInstrumentBeneficiaryDetails piBeneficiaryDetails = new ProductInstrumentBeneficiaryDetails();
		piBeneficiaryDetails.setNameFormat(ProductInstrumentBeneficiaryNameFormat.PI_BENEFICIARY_NAME_FORMAT_STANDARD);
		piBeneficiaryDetails.setName(userNameVo);
		piBeneficiaryDetails.setDOB(analyzeFiResponse.getCandidateInstrument().getInstrumentDetails().getFiDetails()
				.getPiDetails().getBeneficiaryDetails().get(0).getDOB());

		List<ProductInstrumentBeneficiaryDetails> piBeneficiaryList = new ArrayList<ProductInstrumentBeneficiaryDetails>();
		piBeneficiaryList.add(piBeneficiaryDetails);
		return piBeneficiaryList;
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

	private List<ProductInstrumentIdentifierAttribute> getProductInstrumentIdentifierAttribute() {
		ProductInstrumentIdentifierAttribute productInstrumentIdentifierAttribute = new ProductInstrumentIdentifierAttribute();
		productInstrumentIdentifierAttribute
				.setName(ProductInstrumentIdentifierType.PI_IDENTIFIER_TYPE_ISSUER_IDENTIFICATION_NUMBER_HMAC);

		List<ProductInstrumentIdentifierAttribute> productIdentifierList = new ArrayList<ProductInstrumentIdentifierAttribute>();
		productIdentifierList.add(productInstrumentIdentifierAttribute);

		return productIdentifierList;
	}

	private PaymentFlowContextVO getPaymentFlow() {
		PaymentFlowContextVO paymentFlowContextVO = new PaymentFlowContextVO();
		paymentFlowContextVO.setPaymentFlags(getPaymentFlags());
		paymentFlowContextVO.setPaymentSubtype((byte) ('G'));
		paymentFlowContextVO.setIntegrationType(IntegrationType.MOBILE_POS);
		return paymentFlowContextVO;
	}

	protected PaymentFlagsVO getPaymentFlags() {
		PaymentFlagsVO paymentFlagsVO = new PaymentFlagsVO();
		paymentFlagsVO.setFlags(new BigInteger(Integer.toString(4194304), 10));
		paymentFlagsVO.setFlags2(new BigInteger(Integer.toString(0), 10));
		paymentFlagsVO.setFlags3(new BigInteger(Integer.toString(65536), 10));
		paymentFlagsVO.setFlags4(new BigInteger(Integer.toString(32), 10));
		paymentFlagsVO.setFlags5(new BigInteger(Integer.toString(0), 10));
		paymentFlagsVO.setFlags6(new BigInteger(Integer.toString(8), 10));
		return paymentFlagsVO;
	}

	private PaymentPartyVO gerReceiver(User seller) {
		this.accountNumber = new BigInteger(seller.getAccountNumber());
		PaymentPartyVO paymentPartyVO = new PaymentPartyVO();
		paymentPartyVO.setAccountNumber(accountNumber);
		paymentPartyVO.setAliasType(PaymentAliasType.ALIAS_TYPE_NONE);
		paymentPartyVO.setOptionalAccountData(getAccountVoReceiver(accountNumber));
		paymentPartyVO.setOptionalEmailDetails(getSellerEmailAddress(seller));
		return paymentPartyVO;
	}

	private List<EmailVO> getSellerEmailAddress(User seller) {
		EmailVO emailVOSeller = new EmailVO();
		emailVOSeller.setEmail(seller.getEmailAddress());
		emailVOSeller.setIsActive(true);
		emailVOSeller.setIsPrimary(true);
		emailVOSeller.setIsInactive(false);
		emailVOSeller.setIsConfirmed(true);
		emailVOSeller.setIsInvalid(false);
		emailVOSeller.setIsAdmin(false);
		List<EmailVO> emailList = new ArrayList<EmailVO>();
		emailList.add(emailVOSeller);

		return emailList;
	}

	private AccountVO getAccountVoReceiver(BigInteger accountNumber) {
		AccountVO receiverAccount = new AccountVO();
		receiverAccount.setAccountNumber(accountNumber);
		receiverAccount.setUserFlags(getFlags(accountNumber));
		return receiverAccount;
	}

	private List<UserFlagsVO> getFlags(BigInteger accountNumber) {
		List<UserFlagsVO> UserFlagsList = new ArrayList<UserFlagsVO>();

		UserFlagsVO uerFlagsItem = new UserFlagsVO();
		uerFlagsItem.setAccountNumber(accountNumber);
		uerFlagsItem.setFlagGroupId(1001L);
		uerFlagsItem.setFlagGroupValue(6343L);
		UserFlagsList.add(uerFlagsItem);

		UserFlagsVO uerFlagsItemOne = new UserFlagsVO();
		uerFlagsItemOne.setFlagGroupId(1002L);
		uerFlagsItemOne.setFlagGroupValue(6344L);
		UserFlagsList.add(uerFlagsItemOne);

		UserFlagsVO uerFlagsItemTwo = new UserFlagsVO();
		uerFlagsItemTwo.setFlagGroupId(1003L);
		uerFlagsItemTwo.setFlagGroupValue(6345L);
		UserFlagsList.add(uerFlagsItemTwo);

		return UserFlagsList;
	}

	private PaymentPartyVO getSender(ModifyUserDataResponse dccBuyer) {
		PaymentPartyVO paymentPartyVO = new PaymentPartyVO();
		paymentPartyVO.setAccountNumber(dccBuyer.getAccount().getAccountNumber());
		paymentPartyVO.setOptionalAccountData(getOptionalAccountDataSender(dccBuyer));
		paymentPartyVO.setOptionalEmailDetails(getEmaildetails(dccBuyer));
		paymentPartyVO.setOptionalAddresses(getAddress(dccBuyer));
		paymentPartyVO.setUserIp(buyerIpAddress);
		return paymentPartyVO;
	}

	private List<AddressVO> getAddress(ModifyUserDataResponse dccBuyer) {
		AddressVO addressVO = new AddressVO();
		addressVO.setAddress1(dccBuyer.getAddress().get(0).getAddress1());
		addressVO.setAddress2(dccBuyer.getAddress().get(0).getAddress2());
		addressVO.setCity(dccBuyer.getAddress().get(0).getCity());
		addressVO.setState(dccBuyer.getAddress().get(0).getState());
		List<AddressVO> addressVOList = new ArrayList<AddressVO>();
		addressVOList.add(addressVO);

		return addressVOList;
	}

	private List<EmailVO> getEmaildetails(ModifyUserDataResponse dccBuyer) {
		EmailVO emailVO = new EmailVO();
		emailVO.setId(dccBuyer.getEmail().get(0).getId());
		emailVO.setEmail(dccBuyer.getEmail().get(0).getEmail());
		emailVO.setIsActive(false);
		emailVO.setIsPrimary(false);

		List<EmailVO> emailVOList = new ArrayList<EmailVO>();
		emailVOList.add(emailVO);

		return emailVOList;
	}

	private AccountVO getOptionalAccountDataSender(ModifyUserDataResponse dccBuyer) {
		AccountVO buyerAccountVO = new AccountVO();
		buyerAccountVO.setAccountNumber(dccBuyer.getAccount().getAccountNumber());
		buyerAccountVO.setEncryptedAccountNumber(dccBuyer.getAccount().getEncryptedAccountNumber());
		buyerAccountVO.setType(TypeOfAccount.ANONYMOUS);
		buyerAccountVO.setTimeCreated(dccBuyer.getAccount().getTimeCreated());
		buyerAccountVO.setTimeClosed(dccBuyer.getAccount().getTimeClosed());
		buyerAccountVO.setStatus(AccountStatus.OPEN);
		String AccountName = dccBuyer.getName().get(0).getFirstName() + " " + dccBuyer.getName().get(0).getLastName();
		buyerAccountVO.setAccountName(AccountName);
		buyerAccountVO.setLegalCountry(dccBuyer.getAccount().getLegalCountry());
		buyerAccountVO.setLegalEntity(LegalEntity.INC);
		buyerAccountVO.setAccountType(AccountType.ANONYMOUS);
		buyerAccountVO.setUserGroup(AccountUserGroup.PERSONAL);
		buyerAccountVO.setFirstName(dccBuyer.getName().get(0).getFirstName());
		buyerAccountVO.setLastName(dccBuyer.getName().get(0).getLastName());
		buyerAccountVO.setRequiredSecurityLevel(0L);
		buyerAccountVO.setDisposition("OPEN");
		buyerAccountVO.setLastTimeOpened(dccBuyer.getAccount().getLastTimeOpened());
		return buyerAccountVO;
	}

	private ActorInfoVO getActorInfo(Context context) {
		User seller = (User) context.getData(ContextKeys.SELLER_VO_KEY.getName());
		ActorInfoVO actorInfoVO = new ActorInfoVO();

		actorInfoVO.setActorType((byte) (10));
		actorInfoVO.setActorAuthType((byte) (0));
		actorInfoVO.setActorAuthCredential((byte) (0));
		this.accountNumber = new BigInteger(seller.getAccountNumber());
		actorInfoVO.setActorAccountNumber(accountNumber);
		actorInfoVO.setActorId(accountNumber);
		actorInfoVO.setActorIpAddr(buyerIpAddress);
		actorInfoVO.setGuid(new BigInteger(Integer.toString(0), 10));
		actorInfoVO.setTokenType((byte) (0));
		return actorInfoVO;
	}

	protected  DisbursementPolicyType getDisbursementPolicyType() {
	    return DisbursementPolicyType.UNRESTRICTED;
	}

	public static class TransactionAmountInput {
		private Currency txnAMount;

		public TransactionAmountInput(Currency txnAmount) {
			super();
			this.txnAMount = txnAmount;
		}

		public Currency getTxnAmount() {
			return txnAMount;
		}

	}

}
