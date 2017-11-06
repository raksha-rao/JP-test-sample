package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.name.Named;
import com.jptest.payments.common.CommonPartyPb.Party;
import com.jptest.payments.fulfillment.testonia.bridge.resource.PricingPlatformServResource;
import com.jptest.payments.fulfillment.testonia.bridge.util.SecurityContextHelper;
import com.jptest.pricing.interfaces.rest.common.CurrencyProperty;
import com.jptest.pricing.interfaces.rest.common.Money;
import com.jptest.pricing.interfaces.rest.fx.AccountIdentification;
import com.jptest.pricing.interfaces.rest.fx.CustomerIdentification;
import com.jptest.pricing.interfaces.rest.fx.ForeignExchange;
import com.jptest.pricing.interfaces.rest.fx.ForeignExchangeContext;
import com.jptest.pricing.interfaces.rest.fx.ForeignExchangeContext.ConversionReason;
import com.jptest.types.Currency;


/*
 * Represents bridge for pricingplatformserv API calls
 * author - sanray
 */

public class PricingPlatformServBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingPlatformServBridge.class);

    @Inject
    @Named("pricingplatformserv")
    private PricingPlatformServResource pricingplatformserv;

    private static final String SECURITY_CONTEXT_SCOPE_FOR_PPS = "https://uri.jptest.com/services/pricing/calculate-foreign-exchange";

    public ForeignExchange calculateForeignExchange(Party transferParty, Currency currFrom, Currency currTo) {

        String accountNumber = transferParty.getUserId().getAccountNumber();

        ForeignExchangeContext foreignExchangeContext = new ForeignExchangeContext();
        foreignExchangeContext.setConversionReason(ConversionReason.SEND.toString());
        foreignExchangeContext.setCustomerIdentification(getCustomerIdentification(transferParty));
        foreignExchangeContext.setSource(getSourceCurrencyProperty(currFrom));
        foreignExchangeContext.setTarget(getTargetCurrencyProperty(currTo));

        LOGGER.info("Pricing request for accountNumber: {}", foreignExchangeContext);
        List<String> scopes = Arrays.asList(SECURITY_CONTEXT_SCOPE_FOR_PPS);
        final String securityContextString = SecurityContextHelper.getSecurityContext(accountNumber, scopes);

        Response response = pricingplatformserv.calculateForeignExchange(foreignExchangeContext, securityContextString);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            ForeignExchange result = response.readEntity(ForeignExchange.class);
            LOGGER.info("Pricing response: {}", printJsonObject(result));
            return result;
        }
        return null;
    }

    private CustomerIdentification getCustomerIdentification(Party transferParty) {
        CustomerIdentification customerIdentification = new CustomerIdentification();
        AccountIdentification accountIdentification = new AccountIdentification();
        accountIdentification.setAccountId(transferParty.getUserId().getAccountNumber());
        accountIdentification.setCountryCode(transferParty.getPersonalDetails().getAddress().getCountryCode());
        customerIdentification.setAccount(accountIdentification);
        return customerIdentification;
    }

    private CurrencyProperty getSourceCurrencyProperty(Currency currency) {
        CurrencyProperty currencyProperty = new CurrencyProperty();
        Money money = new Money();
        money.setCurrencyCode(currency.getCurrencyCode());
        money.setValue(Double.toString(currency.getUnitAmount()));
        currencyProperty.setAmount(new Money(currency.getCurrencyCode(), Double.toString(currency.getUnitAmount())));
        return currencyProperty;
    }

    private CurrencyProperty getTargetCurrencyProperty(Currency currency) {
        CurrencyProperty currencyProperty = new CurrencyProperty();
        currencyProperty.setCurrencyCode(currency.getCurrencyCode());
        return currencyProperty;
    }
}
