package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.jptest.payments.fulfillment.testonia.bridge.UserLifecycleServBridge;
import com.jptest.payments.fulfillment.testonia.business.util.ReportingAttributes;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.user.AccountType;
import com.jptest.user.AccountUserGroup;
import com.jptest.user.ModifyUserDataRequest;
import com.jptest.user.ModifyUserDataResponse;
import com.jptest.user.NewAccountVO;
import com.jptest.user.NewAddressVO;
import com.jptest.user.NewEmailVO;
import com.jptest.user.NewNameVO;
import com.jptest.user.NewPartyVO;
import com.jptest.user.PartyNameConfirmationAuthority;
import com.jptest.user.PartyNameConfirmationStatus;
import com.jptest.user.PartyType;
import com.jptest.user.TypeOfAccount;

/**
 * This task is to create dccbuyer by calling modify_user_data
 * 
 * @JP Inc.
 *
 */
public class DCCUserCreationTask extends BaseTask<ModifyUserDataResponse> {

	@Inject
	private UserLifecycleServBridge userLifecycleServBridge;

	@Override
	public final ModifyUserDataResponse process(Context context) {
		try {
			final ModifyUserDataRequest request = buildRequest();
			ModifyUserDataResponse response = userLifecycleServBridge.createAnonymousDCCBuyer(request);
            context.addReportingAttribute(ReportingAttributes.DCC_ACCOUNT_NUMBER,
                    response.getAccount().getAccountNumber());
			return response;
		} catch (final Exception e) {
			throw new TestExecutionException("Failed executing  ModifyUserDataTask", e);
		}
	}

	private ModifyUserDataRequest buildRequest() {

		ModifyUserDataRequest request = new ModifyUserDataRequest();

		NewAccountVO newAccount = getNewAccountVO();

		request.setNewAccount(newAccount);

		return request;
	}

	private NewAccountVO getNewAccountVO() {
		NewAccountVO newAccount = new NewAccountVO();

		newAccount.setType(TypeOfAccount.ANONYMOUS);
		newAccount.setAccountType(AccountType.ANONYMOUS);
		newAccount.setUserGroup(AccountUserGroup.PERSONAL);
		newAccount.setLegalCountry("US");
		newAccount.setAccountName("DCC Test");

		NewPartyVO newParty = createNewParty();
		List<NewPartyVO> partyList = new ArrayList<NewPartyVO>();
		partyList.add(newParty);
		newAccount.setNewOwner(partyList);

		return newAccount;
	}

	private NewPartyVO createNewParty() {
		NewPartyVO newParty = new NewPartyVO();

		newParty.setType(PartyType.PERSON);
		newParty.setPreferredLanguage("en_US");
		newParty.setCitizenship("US");
		newParty.setTimezone("America/Los_Angeles");

		NewNameVO name = createNewNameVO();
		List<NewNameVO> nameList = new ArrayList<NewNameVO>();
		nameList.add(name);
		newParty.setNewName(nameList);

		NewEmailVO email = createnewEmailVO();
		List<NewEmailVO> emailList = new ArrayList<NewEmailVO>();
		emailList.add(email);
		newParty.setNewEmail(emailList);

		NewAddressVO address = createNewAddress();
		List<NewAddressVO> addressList = new ArrayList<NewAddressVO>();
		addressList.add(address);
		newParty.setNewAddress(addressList);

		return newParty;
	}

	private NewEmailVO createnewEmailVO() {
		NewEmailVO newEmailVO = new NewEmailVO();
		newEmailVO.setEmail("TestDCC-" + System.currentTimeMillis() + "-sndr@jptest.com");
		newEmailVO.setMakePrimary(true);
		return newEmailVO;
	}

	private NewAddressVO createNewAddress() {
		NewAddressVO address = new NewAddressVO();
		address.setAddress1("2211 North 1st St");
		address.setCity("San Jose");
		address.setState("CA");
		address.setZip("95129");
		address.setIsoCountry("US");
		address.setMakePrimary(true);

		return address;
	}

	private NewNameVO createNewNameVO() {
		NewNameVO name = new NewNameVO();

		name.setFirstName("Amit");
		name.setLastName("Bhati");
		name.setMakePrimary(true);
		name.setConfirmationStatus(PartyNameConfirmationStatus.CONFIRMED);
		name.setConfirmationAuthority(PartyNameConfirmationAuthority.STANDARD);

		return name;
	}

}
