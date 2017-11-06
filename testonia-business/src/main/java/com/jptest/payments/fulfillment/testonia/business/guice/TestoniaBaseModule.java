package com.jptest.payments.fulfillment.testonia.business.guice;

import java.util.Iterator;
import java.util.Properties;

import com.jptest.payments.te.admin.api.TxnEngineAdminServResource;
import org.apache.commons.configuration.Configuration;
import com.jpinc.inc.platform.mayfly.MayflyClient;
import com.jpinc.inc.platform.mayfly.MayflyClientFactory;
import com.jpinc.inc.platform.mayfly.conf.MayflyPropertiesKeyDefinition;
import com.jpinc.inc.platform.mayfly.conf.MayflyPropertiesProvider;
import com.jpinc.kernel.bean.configuration.BeanConfigCategoryInfo;
import com.jpinc.kernel.bean.configuration.ConfigCategoryCreateException;
import com.jpinc.kernel.executor.ExecutorPropertyBean;
import com.jpinc.kernel.executor.ExecutorType;
import com.jpinc.kernel.executor.TaskExecutor;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.jptest.ffxfee.ffxFee;
import com.jptest.ffxfx.ffxFX;
import com.jptest.financialinstrument.FIWalletLifecycle;
import com.jptest.financialinstrument.FIWalletRead;
import com.jptest.financialinstrument.TabLifecycle;
import com.jptest.financialproduct.FPAccountLifecycle;
import com.jptest.incentive.IncentiveLifecycle;
import com.jptest.money.AgreementPlanning;
import com.jptest.money.AsyncFulfillment;
import com.jptest.money.DisbursementPlanning;
import com.jptest.money.FulfillmentServ;
import com.jptest.money.FulfillmentUniqueIdParticipant;
import com.jptest.money.HoldingRead;
import com.jptest.money.HoldsLifeCycle;
import com.jptest.money.IdempotencyParticipant;
import com.jptest.money.MoneyPlanning;
import com.jptest.money.MustangEngineAdmin;
import com.jptest.money.PaymentRead;
import com.jptest.money.PaymentSearch;
import com.jptest.money.PaymentServ;
import com.jptest.money.Posting;
import com.jptest.money.SinglePartyFulfillment;
import com.jptest.payments.PaymentDataProcessor;
import com.jptest.payments.PaymentLookup;
import com.jptest.payments.fulfillment.testonia.bridge.resource.OfferServResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.PricingPlatformServResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.ComplianceLifeCycleResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.FpOnboardingServResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.MerchantPreferenceResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.UserServiceResource;
import com.jptest.payments.fulfillment.testonia.bridge.resource.WalletResource;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.guice.ClientBootStrapper;
import com.jptest.payments.fulfillment.testonia.core.guice.ConfigurationHelper;
import com.jptest.payments.fulfillment.testonia.core.guice.rest.RestClientManager;
import com.jptest.payments.fulfillment.testonia.dao.IwTransactionDao;
import com.jptest.payments.fulfillment.testonia.dao.WTransactionDao;
import com.jptest.payments.fulfillment.testonia.dao.money.WTransactionP10DaoImpl;
import com.jptest.payments.fulfillment.testonia.dao.txn.WTransactionP20DaoImpl;
import com.jptest.payments.paymentcompletion.api.PaymentCompletionService;
import com.jptest.payments.txnfulfillment.api.v1.PaymentMessageResource;
import com.jptest.qi.rest.service.BuyerCreditService;
import com.jptest.qi.rest.service.CreditCardMillService;
import com.jptest.qi.rest.service.OCCQueryService;
import com.jptest.qi.rest.service.YouthAccountService;
import com.jptest.qi.rest.service.useroperations.APICredentialService;
import com.jptest.qi.rest.service.useroperations.FundService;
import com.jptest.qi.rest.service.useroperations.UserFlagService;
import com.jptest.sv.api.rest.CurrentAccountResource;
import com.jptest.transfer.transfercompletion.api.TransferCompletionService;
import com.jptest.user.UserLifecycle;
import com.jptest.user.UserRead;


/**
 * Testonia Guice Module containing common dependencies
 */
public abstract class TestoniaBaseModule extends AbstractModule {

    private static final String TASK_EXECUTOR_CORE_POOLSIZE_KEY = "taskexecutor.corepoolsize";
    private static final String TASK_EXECUTOR_MAX_POOLSIZE_KEY = "taskexecutor.maxpoolsize";
    private static final String TASK_EXECUTOR_MAX_QUEUESIZE_KEY = "taskexecutor.queuesize";

    @Override
    protected void configure() {

        ClientBootStrapper.init(null, null);

        // Create binding for Configuration
        final Configuration configuration = ConfigurationHelper.getTestConfiguration();
        this.bind(Configuration.class).toInstance(configuration);
        // create task executor
        this.bind(TaskExecutor.class).toInstance(this.createTaskExecutor(configuration));

        // Create bindings for DAO interfaces
        this.bind(IwTransactionDao.class).annotatedWith(Names.named("WTransactionDao")).to(WTransactionDao.class);
        this.bind(IwTransactionDao.class).annotatedWith(Names.named("WTransactionDaoP20"))
                .to(WTransactionP20DaoImpl.class);
        this.bind(IwTransactionDao.class).annotatedWith(Names.named("WTransactionDaoP10"))
                .to(WTransactionP10DaoImpl.class);

        // Create bindings for service interfaces
        this.bind(FulfillmentServ.class).annotatedWith(Names.named("txnfulfillmentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("txnfulfillmentserv", FulfillmentServ.class));
        this.bind(PaymentMessageResource.class).annotatedWith(Names.named("paymentmessageresource")).toInstance(
                RestClientManager.getInstance().getProxy("paymentmessageresource", PaymentMessageResource.class));
        this.bind(PaymentServ.class).annotatedWith(Names.named("postpaymenttxnserv"))
                .toInstance(RestClientManager.getInstance().getProxy("postpaymenttxnserv", PaymentServ.class));

        this.bind(PaymentServ.class).annotatedWith(Names.named("postpaymentexecserv"))
                .toInstance(RestClientManager.getInstance().getProxy("postpaymentexecserv", PaymentServ.class));

        this.bind(UserRead.class).annotatedWith(Names.named("userlifecycleserv"))
                .toInstance(RestClientManager.getInstance().getProxy("userlifecycleserv", UserRead.class));
        this.bind(UserLifecycle.class).annotatedWith(Names.named("userlifecycleserv"))
				.toInstance(RestClientManager.getInstance().getProxy("userlifecycleserv", UserLifecycle.class));
        this.bind(UserServiceResource.class).annotatedWith(Names.named("userservice"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", UserServiceResource.class));
        this.bind(YouthAccountService.class).annotatedWith(Names.named("youthaccountservice"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", YouthAccountService.class));
        this.bind(UserFlagService.class).annotatedWith(Names.named("userflagservice"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", UserFlagService.class));
        this.bind(FundService.class).annotatedWith(Names.named("userfundservice"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", FundService.class));
        this.bind(BuyerCreditService.class).annotatedWith(Names.named("buyercreditservice"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", BuyerCreditService.class));
        this.bind(APICredentialService.class).annotatedWith(Names.named("restjaws"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", APICredentialService.class));
        this.bind(CreditCardMillService.class).annotatedWith(Names.named("ccMillService"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", CreditCardMillService.class));
        this.bind(PricingPlatformServResource.class).annotatedWith(Names.named("pricingplatformserv"))
                .toInstance(RestClientManager.getInstance().getProxy("pricingplatformserv", PricingPlatformServResource.class));

        this.bind(IncentiveLifecycle.class).annotatedWith(Names.named("incentivelifecycle")).toInstance(
                RestClientManager.getInstance().getProxy("incentivelifecycleserv", IncentiveLifecycle.class));

        this.bind(FIWalletLifecycle.class).annotatedWith(Names.named("fiwalletlifecycle"))
                .toInstance(RestClientManager.getInstance().getProxy("filifecycleserv", FIWalletLifecycle.class));

        this.bind(PaymentServ.class).annotatedWith(Names.named("paymentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentserv", PaymentServ.class));
        this.bind(FulfillmentServ.class).annotatedWith(Names.named("paymentserv_ca"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentserv_ca", FulfillmentServ.class));
        this.bind(PaymentServ.class).annotatedWith(Names.named("paymentservca"))
        		.toInstance(RestClientManager.getInstance().getProxy("paymentserv_ca", PaymentServ.class));

        this.bind(MoneyPlanning.class).annotatedWith(Names.named("moneyplanningserv_ca"))
                .toInstance(RestClientManager.getInstance().getProxy("moneyplanningserv_ca", MoneyPlanning.class));
        this.bind(AgreementPlanning.class).annotatedWith(Names.named("moneyplanningserv_ca"))
                .toInstance(RestClientManager.getInstance().getProxy("moneyplanningserv_ca", AgreementPlanning.class));
        this.bind(DisbursementPlanning.class).annotatedWith(Names.named("moneyplanningserv_ca"))
                .toInstance(
                        RestClientManager.getInstance().getProxy("moneyplanningserv_ca", DisbursementPlanning.class));
        this.bind(HoldingRead.class).annotatedWith(Names.named("holdingserv"))
                .toInstance(RestClientManager.getInstance().getProxy("holdingserv", HoldingRead.class));
        this.bind(TabLifecycle.class).annotatedWith(Names.named("tablifecycle"))
                .toInstance(RestClientManager.getInstance().getProxy("holdingserv", TabLifecycle.class));

        this.bind(FIWalletRead.class).annotatedWith(Names.named("fininstreadserv"))
                .toInstance(RestClientManager.getInstance().getProxy("fininstreadserv", FIWalletRead.class));
        this.bind(PaymentLookup.class).annotatedWith(Names.named("paymentreadserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentreadserv", PaymentLookup.class));
        this.bind(PaymentRead.class).annotatedWith(Names.named("paymentreadserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentreadserv", PaymentRead.class));
        this.bind(OCCQueryService.class).annotatedWith(Names.named("restjaws"))
                .toInstance(RestClientManager.getInstance().getProxy("restjaws", OCCQueryService.class));
        this.bind(MerchantPreferenceResource.class).annotatedWith(Names.named("merchantpreferenceresource")).toInstance(
                RestClientManager.getInstance().getProxy("merchantsettingserv", MerchantPreferenceResource.class));
        this.bind(WalletResource.class).annotatedWith(Names.named("walletresource"))
                .toInstance(RestClientManager.getInstance().getProxy("fimanagementserv_ca", WalletResource.class));
        this.bind(FPAccountLifecycle.class).annotatedWith(Names.named("fpaccountlifecycle"))
                .toInstance(RestClientManager.getInstance().getProxy("fpaccountserv", FPAccountLifecycle.class));
        // Create bindings multiple mustang engines
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyengineadminserv1")).toInstance(
                RestClientManager.getInstance().getProxy("moneyengineadminserv1", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyengineadminserv2")).toInstance(
                RestClientManager.getInstance().getProxy("moneyengineadminserv2", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyengineadminserv3")).toInstance(
                RestClientManager.getInstance().getProxy("moneyengineadminserv3", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyengineadminserv4")).toInstance(
                RestClientManager.getInstance().getProxy("moneyengineadminserv4", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyengineadminserv6")).toInstance(
                RestClientManager.getInstance().getProxy("moneyengineadminserv6", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("moneyasyncengineadminserv1")).toInstance(
                RestClientManager.getInstance().getProxy("moneyasyncengineadminserv1", MustangEngineAdmin.class));
        this.bind(MustangEngineAdmin.class).annotatedWith(Names.named("txnengineadminserv")).toInstance(
                RestClientManager.getInstance().getProxy("txnengineadminserv", MustangEngineAdmin.class));

        this.bind(CurrentAccountResource.class).annotatedWith(Names.named("currentaccountresource"))
                .toInstance(RestClientManager.getInstance().getProxy("storedvalueserv", CurrentAccountResource.class));

        this.bind(PaymentCompletionService.class).annotatedWith(Names.named("txncompletionserv")).toInstance(
                RestClientManager.getInstance().getProxy("txncompletionserv", PaymentCompletionService.class));

        this.bind(TransferCompletionService.class).annotatedWith(Names.named("txncompletionserv")).toInstance(
                RestClientManager.getInstance().getProxy("txncompletionserv", TransferCompletionService.class));

        this.bind(HoldsLifeCycle.class).annotatedWith(Names.named("paymentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentserv", HoldsLifeCycle.class));

        this.bind(Posting.class).annotatedWith(Names.named("paymentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentserv", Posting.class));

        this.bind(SinglePartyFulfillment.class).annotatedWith(Names.named("paymentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentserv", SinglePartyFulfillment.class));

        this.bind(PaymentSearch.class).annotatedWith(Names.named("paymentsearchserv"))
                .toInstance(RestClientManager.getInstance().getProxy("paymentsearchserv", PaymentSearch.class));

        this.bind(AsyncFulfillment.class).annotatedWith(Names.named("asyncfulfillmentserv"))
                .toInstance(RestClientManager.getInstance().getProxy("asyncfulfillmentserv", AsyncFulfillment.class));

        this.bind(TxnEngineAdminServResource.class).annotatedWith(Names.named("txnengineadminserv_rest"))
                .toInstance(RestClientManager.getInstance().getProxy("txnengineadminserv_rest", TxnEngineAdminServResource.class));

        this.bind(ComplianceLifeCycleResource.class).annotatedWith(Names.named("compliancelifecycleresource"))
                .toInstance(RestClientManager.getInstance().getProxy("complifecycleserv",
                        ComplianceLifeCycleResource.class));

        this.bind(IdempotencyParticipant.class).annotatedWith(Names.named("moneyidempserv"))
                .toInstance(RestClientManager.getInstance().getProxy("moneyidempserv", IdempotencyParticipant.class));

        this.bind(FulfillmentUniqueIdParticipant.class).annotatedWith(Names.named("moneyidempserv"))
                .toInstance(RestClientManager.getInstance().getProxy("moneyidempserv",
                        FulfillmentUniqueIdParticipant.class));

        this.bind(MayflyClient.class).annotatedWith(Names.named("mayflyserv"))
        .toInstance(MayflyClientFactory
                .newMayflyClient(new MayflyPropertiesProvider(extractMayflyProp(configuration))));

        this.bind(MayflyClient.class).annotatedWith(Names.named("mayflymoneyserv"))
        .toInstance(MayflyClientFactory
                .newMayflyClient(new MayflyPropertiesProvider(extractMayflyMoneyProp(configuration))));

        //        this.bind(MsMonitorResource.class).annotatedWith(Names.named("msmonitorresource"))
        //                .toInstance(RestClientManager.getInstance().getProxy("msmonitor", MsMonitorResource.class));
        
        this.bind(ffxFX.class).annotatedWith(Names.named("ffxserv_ca"))
                		.toInstance(RestClientManager.getInstance().getProxy("ffxserv_ca", ffxFX.class));
        
        this.bind(ffxFee.class).annotatedWith(Names.named("ffxserv_ca"))
		.toInstance(RestClientManager.getInstance().getProxy("ffxserv_ca", ffxFee.class));

		this.bind(FpOnboardingServResource.class).annotatedWith(Names.named("fponboardingserv_r"))
		.toInstance(RestClientManager.getInstance().getProxy("fponboardingserv_r", FpOnboardingServResource.class));

        this.bind(PaymentDataProcessor.class).annotatedWith(Names.named("transactioneventprocessorserv"))
                .toInstance(RestClientManager.getInstance().getProxy("transactioneventprocessorserv", PaymentDataProcessor.class));

        this.bind(OfferServResource.class).annotatedWith(Names.named("offerservresource"))
                .toInstance(RestClientManager.getInstance().getProxy("offerserv", OfferServResource.class));
        // Configure project-specific dependencies
        this.configureProjectDependencies();
    }

    private Properties extractMayflyProp(Configuration configuration) {
        Iterator<String> iterator = configuration.getKeys("mayfly");
        Properties properties = new Properties();
        while (iterator.hasNext()) {
            String key = iterator.next();
            properties.put(key, configuration.getString(key));
        }
        return properties;
    }

    private Properties extractMayflyMoneyProp(Configuration configuration) {
        Iterator<String> iterator = configuration.getKeys("mayfly");
        Properties properties = new Properties();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if(key.equals(MayflyPropertiesKeyDefinition.recordNamespace))
                properties.put(key, "Money");
            else 
                properties.put(key, configuration.getString(key));
        }
        return properties;
    }

    protected TaskExecutor createTaskExecutor(final Configuration config) {
        BeanConfigCategoryInfo categoryInfo = null;
        try {
            final String uniqueName = "TestExecutor";
            categoryInfo = BeanConfigCategoryInfo.createBeanConfigCategoryInfo(uniqueName, uniqueName,
                    "stageExecutorGroup", false, false, null, "executor for test stageExecutors");
        } catch (final ConfigCategoryCreateException e) {
            throw new TestExecutionException("Couldn't create the executor", e);
        }
        return TaskExecutor.newExecutor(new ExecutorPropertyBean(this.getClass().getCanonicalName(),
                ExecutorType.BOUNDED, categoryInfo, config.getInt(TASK_EXECUTOR_CORE_POOLSIZE_KEY),
                config.getInt(TASK_EXECUTOR_MAX_POOLSIZE_KEY), config.getInt(TASK_EXECUTOR_MAX_QUEUESIZE_KEY)));
    }

    /**
     * Configure project-specific dependencies
     */
    protected abstract void configureProjectDependencies();
}
