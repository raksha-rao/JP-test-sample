package com.jptest.payments.fulfillment.testonia.business.component.task;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jptest.payments.fulfillment.testonia.bridge.FpOnboardingServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.StoredValueServBridge;
import com.jptest.payments.fulfillment.testonia.bridge.UserBridge;
import com.jptest.payments.fulfillment.testonia.bridge.UserLifecycleServBridge;
import com.jptest.payments.fulfillment.testonia.business.service.UserTaskHelper;
import com.jptest.payments.fulfillment.testonia.business.service.WUserAchHelper;
import com.jptest.payments.fulfillment.testonia.business.service.WUserHelper;
import com.jptest.payments.fulfillment.testonia.core.Context;
import com.jptest.payments.fulfillment.testonia.core.impl.BaseTask;
import com.jptest.payments.fulfillment.testonia.core.impl.ContextKeys;
import com.jptest.payments.fulfillment.testonia.dao.cloc.FICacheObjVerDAO;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WMerchantPullBaDao;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WMerchantSettlementDAO;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WUserCCDao;
import com.jptest.payments.fulfillment.testonia.dao.cloc.WUserFinancialInstrumentMapDAO;
import com.jptest.payments.fulfillment.testonia.dao.money.WUserHoldingDao;
import com.jptest.payments.fulfillment.testonia.model.PostPaymentTogglesConstants;
import com.jptest.payments.fulfillment.testonia.model.money.WUserHoldingDTO;
import com.jptest.payments.fulfillment.testonia.model.user.UserFlags;
import com.jptest.qi.rest.domain.pojo.User;
import com.jptest.sv.api.rest.resources.CurrentAccountClose;
import com.jptest.sv.api.rest.resources.CurrentAccountCreate;


public class PostPaymentPreTogglesTask extends BaseTask<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostPaymentPreTogglesTask.class);
    private static final String accountType = "STANDARD_BALANCE_ACCOUNT";
    @Inject
    WUserCCDao wUserCCDao;
    @Inject
    WUserHoldingDao wuserHoldingDAO;
    @Inject
    private WMerchantSettlementDAO wmerchantSettlementDAO;
    @Inject
    StoredValueServBridge storedValueBridge;
    @Inject
    private UserBridge userBridge;
    @Inject
    private UserTaskHelper userTaskHelper;
    @Inject
    private WUserAchHelper wUserAch;
    @Inject
    private WUserHelper wUserHelper;
    @Inject
    private WMerchantPullBaDao wMerchantPullBaDAO;
    @Inject
    private FpOnboardingServBridge padServBridge;
    @Inject
    private WUserFinancialInstrumentMapDAO wUserFinancialInstrumentMapDAO;
    @Inject
    private FICacheObjVerDAO fiCacheObjVerDAO;
    @Inject
    private UserLifecycleServBridge userLifecycleServBridge;
    
    private final String toggles;

    public PostPaymentPreTogglesTask(final String toggles) {
        this.toggles = toggles;
    }

    @Override
    public Object process(final Context context) {
        final User seller = (User) this.getDataFromContext(context, ContextKeys.SELLER_VO_KEY.getName());
        final User buyer = (User) this.getDataFromContext(context, ContextKeys.BUYER_VO_KEY.getName());
        final String[] togglesData = this.toggles.split(";");
        String[] toggleData;
        for (int i = 0; i < togglesData.length; i++) {
            toggleData = togglesData[i].split(":");
            switch (toggleData[0]) {

                case PostPaymentTogglesConstants.REJECTCC:
                    this.wUserCCDao.rejectCC(buyer.getAccountNumber());
                    break;
                case PostPaymentTogglesConstants.CLEARSELLERBALANCE:
                    this.wuserHoldingDAO.updateBalance(seller.getAccountNumber(), "0");
                    break;
                case PostPaymentTogglesConstants.CLEARBUYERBALANCE:
                    this.wuserHoldingDAO.updateBalance(buyer.getAccountNumber(), "0");
                    break;
                case PostPaymentTogglesConstants.OPENHOLDINGBUYER:
                    final String buyerHoldingCurrencyCode = toggleData[1];
                    final CurrentAccountCreate buyerHoldingCreateRequest = new CurrentAccountCreate();
                    buyerHoldingCreateRequest.setCurrencyCode(buyerHoldingCurrencyCode);
                    buyerHoldingCreateRequest.setAccountType(accountType);
                    this.storedValueBridge.createAccount(buyer.getAccountNumber(), buyerHoldingCreateRequest);
                    break;
                case PostPaymentTogglesConstants.OPENHOLDINGSELLER:
                    final String sellerHoldingCurrencyCode = toggleData[1];
                    final CurrentAccountCreate sellerHoldingCreateRequest = new CurrentAccountCreate();
                    sellerHoldingCreateRequest.setCurrencyCode(sellerHoldingCurrencyCode);
                    sellerHoldingCreateRequest.setAccountType(accountType);
                    this.storedValueBridge.createAccount(seller.getAccountNumber(), sellerHoldingCreateRequest);
                    break;
                case PostPaymentTogglesConstants.CLOSEHOLDINGBUYER:
                    final String buyerCloseHoldingCurrencyCode = toggleData[1];
                    final List<WUserHoldingDTO> buyerHoldingDTO = this.wuserHoldingDAO
                            .getWUserHoldingDetails(buyer.getAccountNumber(), buyerCloseHoldingCurrencyCode);
                    final CurrentAccountClose buyerCloseHoldingRequest = new CurrentAccountClose();
                    this.storedValueBridge.closeAccount(buyer.getAccountNumber(),
                            buyerHoldingDTO.get(0).getId().toString(),
                            buyerCloseHoldingRequest);
                    break;
                case PostPaymentTogglesConstants.CLOSEHOLDINGSELLER:
                    final String sellerCloseHoldingCurrencyCode = toggleData[1];
                    final List<WUserHoldingDTO> sellerHoldingDTO = this.wuserHoldingDAO
                            .getWUserHoldingDetails(seller.getAccountNumber(), sellerCloseHoldingCurrencyCode);
                    final CurrentAccountClose sellerCloseHoldingRequest = new CurrentAccountClose();
                    this.storedValueBridge.closeAccount(seller.getAccountNumber(),
                            sellerHoldingDTO.get(0).getId().toString(), sellerCloseHoldingRequest);
                    break;
                case PostPaymentTogglesConstants.UPDATEHOLDINGFLAGSBUYER:
                    final String buyerUpdateHoldingCurrencyCode = toggleData[1];
                    final String buyerUpdateHoldingFlags = toggleData[2];
                    this.wuserHoldingDAO.updateFlags(buyer.getAccountNumber(), buyerUpdateHoldingCurrencyCode,
                            buyerUpdateHoldingFlags);
                    break;
                case PostPaymentTogglesConstants.UPDATEHOLDINGFLAGSSELLER:
                    final String sellerUpdateHoldingCurrencyCode = toggleData[1];
                    final String sellerUpdateHoldingFlags = toggleData[2];
                    this.wuserHoldingDAO.updateFlags(seller.getAccountNumber(), sellerUpdateHoldingCurrencyCode,
                            sellerUpdateHoldingFlags);
                    break;
                case PostPaymentTogglesConstants.UPDATEBUYERHOLDINGBALANCE:
                    final String buyerUpdateHoldingBalance = toggleData[1];
                    final String buyerUpdateHoldingBalanceCurrencyCode = toggleData[2];
                    this.wuserHoldingDAO.updateBalance(buyer.getAccountNumber(), buyerUpdateHoldingBalanceCurrencyCode,
                            buyerUpdateHoldingBalance);
                    break;
                case PostPaymentTogglesConstants.UPDATESELLERHOLDINGBALANCE:
                    final String sellerUpdateHoldingBalance = toggleData[1];
                    final String sellerUpdateHoldingBalanceCurrencyCode = toggleData[2];
                    this.wuserHoldingDAO.updateBalance(seller.getAccountNumber(),
                            sellerUpdateHoldingBalanceCurrencyCode, sellerUpdateHoldingBalance);
                    break;
                case PostPaymentTogglesConstants.ALLOWNEGATIVEBALANCE:
                    this.wmerchantSettlementDAO.insertWMerchantSettlement(seller.getAccountNumber());
                    break;
                case PostPaymentTogglesConstants.ENABLEBUYERBALANCECONVERSION:
                    this.userBridge.setUserFlags(buyer.getAccountNumber(),
                            this.userTaskHelper.getUserFlagList(
                                    UserFlags.WUSER_FLAGS_GROUP_LARGE_MERCHANT_MAY_REFUND_USING_BALANCE_CONVERSION
                                            .name()));
                    break;
                case PostPaymentTogglesConstants.ENABLESELLERBALANCECONVERSION:
                    this.userBridge.setUserFlags(seller.getAccountNumber(),
                            this.userTaskHelper.getUserFlagList(
                                    UserFlags.WUSER_FLAGS_GROUP_LARGE_MERCHANT_MAY_REFUND_USING_BALANCE_CONVERSION
                                            .name()));
                    break;
                case PostPaymentTogglesConstants.MAKEBANKBAD:
                    this.wUserAch.markBankAsBad(buyer.getAccountNumber());
                    break;
                case PostPaymentTogglesConstants.UPDATEBUYERFIRSTNAME:
                	this.wUserHelper.updateFirstName(buyer.getAccountNumber(), toggleData[1]);
                	break;
                case PostPaymentTogglesConstants.UPDATEMERCHANTPULLBA:
                	this.wMerchantPullBaDAO.updateMerchantPullBa(seller.getAccountNumber(), buyer.getAccountNumber());
                	break;
                case PostPaymentTogglesConstants.ENABLEIPN:
                    this.userTaskHelper.addIPNtoUser(seller.getAccountNumber());
                    LOGGER.info("successfully enabled IPN for seller {} ", seller.getAccountNumber());
                    userTaskHelper.addIPNFlagtoUser(seller.getAccountNumber());
                    break;
                case PostPaymentTogglesConstants.PAD_OPTIN:
                    // optin for PAD and make it the default instrument
                    this.padServBridge.enrollPAD(buyer);
                    this.wUserFinancialInstrumentMapDAO.updateDefaultInstrument(buyer.getAccountNumber(), buyer.getBank().iterator().next().getBankId());
                    this.fiCacheObjVerDAO.updateFiCache(buyer.getAccountNumber());
                    break;
                case PostPaymentTogglesConstants.ENABLEMORETHAN100PERCENTREFUND:
                    this.userLifecycleServBridge.enableOverageRefund(new BigInteger(seller.getAccountNumber()),toggleData[1],toggleData[2]);
                    break;                    
                default:
                    return togglesData;
            }
        }
        return togglesData;
    }
}
