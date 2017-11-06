package com.jptest.payments.fulfillment.testonia.business.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.google.protobuf.TextFormat;
import com.jptest.payments.common.CommonPartyPb;
import com.jptest.payments.common.Utils;
import com.jptest.payments.fulfillment.testonia.core.util.xml.XMLHelper;
import com.jptest.payments.funding_selection.InstructionTargetPb;
import com.jptest.payments.funding_selection.LedgerDetailsPb.LedgerDetails;
import com.jptest.payments.payment_message.PaymentInstructionPb;
import com.jptest.payments.payment_message.PaymentMessagePb;
import com.jptest.payments.payment_message.SimplePaymentMessagePb;
import com.jptest.payments.wallet.FundingInstrumentPb;

/**
 * This is an XML builder for SettlementRequestValidator
 * <p>
 * The logic to extract SettlementRequest is as below.
 * <li>Check in ActivityLog "async_messages_for_amq_daemon"</li>
 * <li>If not present, check in ActivityLog "async_messages"</li>
 * <li>If not present, check in ActivityLog "async_tasks"</li>
 * <li>Once we find the Settlement Task get the request from
 * <ul>
 * <li>serialized_payment_message</li>
 * <li>if not present b. json_request</li>
 * </ul>
 * </li>
 *
@JP Inc.
 */
@Singleton
public class SettlementValidationXMLBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettlementValidationXMLBuilder.class);

    private static final String ASYNC_MESSAGES_FOR_AMQ_DAEMON = "//async_messages_for_amq_daemon/item[type='SETTLEMENT']";

    private static final String ASYNC_REQUESTS_FROM_ASYNC_MESSAGES = "//async_messages/item[type='SETTLEMENT']";

    private static final String ASYNC_REQUESTS_FROM_ASYNC_TASKS = "//async_tasks/item[type='SETTLEMENT']";

    private static List<String> xPaths = new ArrayList<>(Arrays.asList(ASYNC_MESSAGES_FOR_AMQ_DAEMON,
            ASYNC_REQUESTS_FROM_ASYNC_MESSAGES, ASYNC_REQUESTS_FROM_ASYNC_TASKS));

    private XMLHelper xmlHelper;

    @Inject
    public SettlementValidationXMLBuilder(XMLHelper xmlHelper) {
        this.xmlHelper = xmlHelper;
    }

    public Document getXmlResponse(Document doc) throws IOException, ParserConfigurationException, TransformerException {
        for(String xPathCriteria : xPaths) {
            Document asyncMessagesRequest = getSettlementRequest(doc, xPathCriteria);
            Document jsonOrSpmRequest = getJsonOrSpmRequest(asyncMessagesRequest);
            if(Objects.nonNull(jsonOrSpmRequest))
                return jsonOrSpmRequest;
        }

        return null;
    }

    private Document getXmlFromJson(String request) throws ParserConfigurationException, IOException, TransformerException {
        JSONObject jsonRequest = new JSONObject(request);
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDocument = builder.newDocument();
        Element settlementRequest = xmlDocument.createElement("SettlementRequest");
        xmlDocument.appendChild(settlementRequest);
        mmToXmlConverter(jsonRequest, xmlDocument, settlementRequest);
        return xmlDocument;
    }

    private Document getXmlFromSpm(String request) throws IOException, ParserConfigurationException, TransformerException {
        PaymentMessagePb.PaymentMessage paymentMessage = null;
        try {
            PaymentMessagePb.PaymentMessage.Builder pmBuilder = PaymentMessagePb.PaymentMessage.newBuilder();
            TextFormat.merge(request, pmBuilder);
            paymentMessage= pmBuilder.build();
        } catch (Exception e) {
            LOGGER.error("Failed while converting to Simple Payment Message", e);
            return null;
        }
        SimplePaymentMessagePb.SimplePaymentMessage simplePaymentMessage = paymentMessage.getSimplePaymentMessage();
        if(simplePaymentMessage.getPaymentInstructionList().isEmpty())
            return null;

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xmlDocument = builder.newDocument();
        Element settlementRequest = xmlDocument.createElement("SettlementRequest");
        xmlDocument.appendChild(settlementRequest);
        ignoringException(() -> appendElement(xmlDocument, settlementRequest, "PaymentTrackingId",
                simplePaymentMessage.getContext().getPaymentContext().getPaymentTrackingId()));
        ignoringException(() -> appendElement(xmlDocument, settlementRequest, "CreationTime",
                String.valueOf(simplePaymentMessage.getContext().getTransactionContext().getCreationTime()
                        .getTimeInMillis())));
        ignoringException(() -> appendElement(xmlDocument, settlementRequest, "TransactionActivityId",
                String.valueOf(simplePaymentMessage.getContext().getTransactionContext().getTransactionActivityId())));
        piToXmlConverter(simplePaymentMessage.getPaymentInstructionList(), settlementRequest, xmlDocument);

        return xmlDocument;
    }

    private void piToXmlConverter(List<PaymentInstructionPb.PaymentInstruction> paymentInstructions, Element settlementRequest, Document document) throws ParserConfigurationException, IOException, TransformerException {

        for(PaymentInstructionPb.PaymentInstruction paymentInstruction : paymentInstructions) {
            Element paymentInstructionDom = document.createElement("PaymentInstruction");
            appendElement(document, paymentInstructionDom, "ParticipantTxnId", paymentInstruction.getInstructionContext().getParticipantTransactionId());
            appendElement(document, paymentInstructionDom, "Amount", Utils.getCurrency(paymentInstruction.getAmount()));
            appendElement(document, paymentInstructionDom, "ReferenceTxnId", String.valueOf(paymentInstruction.getReferenceTransactionId()));
            appendElement(document, paymentInstructionDom, "MoneyMovementType", String.valueOf(paymentInstruction.getType().getNumber()));
            appendElement(document, paymentInstructionDom, "AllowClosedInstrument", String.valueOf(paymentInstruction.getAllowClosedInstrument()));
            ignoringException(() -> appendElement(document, paymentInstructionDom, "ProcessorId", paymentInstruction.getProcessorContext().getProcessorId()));
            ignoringException(() -> appendElement(document, paymentInstructionDom, "RoutingEntity", paymentInstruction.getProcessorContext().getRoutingEntity().toString()));
            ignoringException(() -> appendElement(document, paymentInstructionDom, "RoutingEntity", paymentInstruction.getProcessorContext().getRoutingEntity().toString()));
            if (paymentInstruction.getTarget().equals(InstructionTargetPb.InstructionTarget.LEDGER)) {
                ignoringException(() -> appendElement(document, paymentInstructionDom, "Target", paymentInstruction.getTarget().toString()));
                paymentInstructionDom.appendChild(buildLedgerDetails(paymentInstruction.getLedgerDetails(), document));
            } else {
                paymentInstructionDom.appendChild(buildBackingInstrument(paymentInstruction.getBackingInstrument(), document));
            }
            paymentInstructionDom.appendChild(buildInstrumentHolder(paymentInstruction.getInstrumentHolder(), document));
            settlementRequest.appendChild(paymentInstructionDom);
        }
    }

    private Element buildLedgerDetails (LedgerDetails ledgerDetails, Document document) {
        Element ledgerDetailsDom = document.createElement("LedgerDetails");
        appendElement(document, ledgerDetailsDom, "ledger", ledgerDetails.getLedger().toString());
        appendElement(document, ledgerDetailsDom, "ledger_type", ledgerDetails.getLedger().toString());
        appendElement(document, ledgerDetailsDom, "ledger_processing_mode", ledgerDetails.getLedger().toString());

        return ledgerDetailsDom;
    }

    private Element buildBackingInstrument(FundingInstrumentPb.BackingInstrument backingInstrument, Document document) {
        Element backingInstrumentDom = document.createElement("BackingInstruction");
        appendElement(document, backingInstrumentDom, "Type", backingInstrument.getType().toString());
        switch(backingInstrument.getType()) {
            case STORED_VALUE:
                appendElement(document, backingInstrumentDom, "CurrencyCode", backingInstrument.getStoredValue().getCurrencyCode());
                appendElement(document, backingInstrumentDom, "StoredValueId", backingInstrument.getStoredValue().getStoredValueId());
                appendElement(document, backingInstrumentDom, "AccountNumber", backingInstrument.getStoredValue().getBalanceAccountId());
                appendElement(document, backingInstrumentDom, "BalanceType", backingInstrument.getStoredValue().getSubBalanceType().toString());
                appendElement(document, backingInstrumentDom, "LegalEntity", backingInstrument.getStoredValue().getLegacyBalanceConstructs().getLegalEntity());
                appendElement(document, backingInstrumentDom, "AffectsAggregate", String.valueOf(backingInstrument.getStoredValue().getLegacyBalanceConstructs().getAffectsAggregate()));
                appendElement(document, backingInstrumentDom, "AffectsNegativeBalance", String.valueOf(backingInstrument.getStoredValue().getLegacyBalanceConstructs().getAllowNegativeBalance()));
                break;
            case BANK:
                appendElement(document, backingInstrumentDom, "BankId", backingInstrument.getBankAccount().getBankId());
                appendElement(document, backingInstrumentDom, "BankAccountType", backingInstrument.getBankAccount().getBankAccountType().toString());
                appendElement(document, backingInstrumentDom, "LastNChars", backingInstrument.getBankAccount().getLastNChars());
                appendElement(document, backingInstrumentDom, "AccountName", backingInstrument.getBankAccount().getAccountName());
                appendElement(document, backingInstrumentDom, "CurrencyCode", backingInstrument.getBankAccount().getCurrencyCode());
                appendElement(document, backingInstrumentDom, "FirstName", backingInstrument.getBankAccount().getFirstName());
                appendElement(document, backingInstrumentDom, "LastName", backingInstrument.getBankAccount().getLastName());
                appendElement(document, backingInstrumentDom, "BillingAddressId", backingInstrument.getBankAccount().getBillingAddress().getAddressId());
                appendElement(document, backingInstrumentDom, "BillingAddressLine1", backingInstrument.getBankAccount().getBillingAddress().getLine2());
                appendElement(document, backingInstrumentDom, "BillingAddressLine2", backingInstrument.getBankAccount().getBillingAddress().getLine1());
                appendElement(document, backingInstrumentDom, "BillingAddressCity", backingInstrument.getBankAccount().getBillingAddress().getCity());
                appendElement(document, backingInstrumentDom, "BillingAddressPostalCode", backingInstrument.getBankAccount().getBillingAddress().getPostalCode());
                appendElement(document, backingInstrumentDom, "BillingAddressState", backingInstrument.getBankAccount().getBillingAddress().getStateProvince());
                appendElement(document, backingInstrumentDom, "BillingAddressCountry", backingInstrument.getBankAccount().getBillingAddress().getCountryCode());
                appendElement(document, backingInstrumentDom, "BankIssuerCountry", backingInstrument.getBankAccount().getBankIssuer().getCountryCode());
                appendElement(document, backingInstrumentDom, "BankIssuerName", backingInstrument.getBankAccount().getBankIssuer().getName());
                backingInstrumentDom.appendChild(buildBankAccountIdentifiers(backingInstrument.getBankAccount().getBankAccountIdentifiersList(), document));
                break;
            case CARD:
                appendElement(document, backingInstrumentDom, "CardBrand", backingInstrument.getCardAccount().getCardBrand().toString());
                appendElement(document, backingInstrumentDom, "CardIssuerCountryCode", backingInstrument.getCardAccount().getCardIssuer().getCountryCode());
                appendElement(document, backingInstrumentDom, "CardId", backingInstrument.getCardAccount().getCardId());
                appendElement(document, backingInstrumentDom, "CardNumberHmac", backingInstrument.getCardAccount().getCardNumberHmac());
                appendElement(document, backingInstrumentDom, "CardNumberEncrypted", backingInstrument.getCardAccount().getCardNumberEncrypted());
                appendElement(document, backingInstrumentDom, "FirstName", backingInstrument.getCardAccount().getFirstName());
                appendElement(document, backingInstrumentDom, "LastName", backingInstrument.getCardAccount().getLastName());
                appendElement(document, backingInstrumentDom, "ExpireMonth", String.valueOf(backingInstrument.getCardAccount().getExpireMonth()));
                appendElement(document, backingInstrumentDom, "BillingAddressId", backingInstrument.getCardAccount().getBillingAddress().getAddressId());
                appendElement(document, backingInstrumentDom, "BillingcountryCode", backingInstrument.getCardAccount().getBillingAddress().getCountryCode());
                appendElement(document, backingInstrumentDom, "Primary", String.valueOf(backingInstrument.getCardAccount().getBillingAddress().getPrimary()));
                appendElement(document, backingInstrumentDom, "Active", String.valueOf(backingInstrument.getCardAccount().getBillingAddress().getActive()));
                appendElement(document, backingInstrumentDom, "UnencryptedCardId", String.valueOf(backingInstrument.getCardAccount().getBillingAddress().getActive()));
                appendElement(document, backingInstrumentDom, "CardNumberDecrypted", backingInstrument.getCardAccount().getCardNumberDecrypted());
                appendElement(document, backingInstrumentDom, "CardConfirmationAvsResult", backingInstrument.getCardAccount().getCardConfirmationAvsResult().toString());
                break;
            default:
                return backingInstrumentDom;
        }

        return backingInstrumentDom;
    }

    private Element buildInstrumentHolder(CommonPartyPb.Party instrumentHolder, Document document) {
        Element instrumentHolderDom = document.createElement("InstrumentHolder");
        appendElement(document, instrumentHolderDom, "Fname", instrumentHolder.getPersonalDetails().getName().getFirstName());
        appendElement(document, instrumentHolderDom, "Lname", instrumentHolder.getPersonalDetails().getName().getLastName());
        appendElement(document, instrumentHolderDom, "FullName", instrumentHolder.getPersonalDetails().getFullName());
        appendElement(document, instrumentHolderDom, "AddressLine1", instrumentHolder.getPersonalDetails().getAddress().getLine1());
        appendElement(document, instrumentHolderDom, "AddressLine2", instrumentHolder.getPersonalDetails().getAddress().getLine2());
        appendElement(document, instrumentHolderDom, "AddressId", instrumentHolder.getPersonalDetails().getAddress().getAddressId());
        appendElement(document, instrumentHolderDom, "AddressCity", instrumentHolder.getPersonalDetails().getAddress().getCity());
        appendElement(document, instrumentHolderDom, "AddressState", instrumentHolder.getPersonalDetails().getAddress().getStateProvince());
        appendElement(document, instrumentHolderDom, "AddressCountry", instrumentHolder.getPersonalDetails().getAddress().getCountryCode());
        appendElement(document, instrumentHolderDom, "AddressPostalCode", instrumentHolder.getPersonalDetails().getAddress().getPostalCode());
        appendElement(document, instrumentHolderDom, "AccountType", instrumentHolder.getjptestAccountDetails().getAccountType().toString());

        return instrumentHolderDom;
    }

    private void appendElement(Document document, Element parentElement, String tag, String value) {
        Element element = document.createElement(tag);
        Text elementtext = document.createTextNode(value);
        element.appendChild(elementtext);
        parentElement.appendChild(element);
    }

    private Element buildBankAccountIdentifiers(List<FundingInstrumentPb.BankAccountIdentifier> bankAccountIdentifiers, Document document) {
        Element bankAccountIdentifiersDom = document.createElement("BankAccountIdentifiers");
        for(FundingInstrumentPb.BankAccountIdentifier bankAccountIdentifier : bankAccountIdentifiers) {
            Element element = document.createElement(bankAccountIdentifier.getName().name());
            Text elementtext = document.createTextNode(bankAccountIdentifier.getValue());
            element.appendChild(elementtext);
            bankAccountIdentifiersDom.appendChild(element);
        }

        return bankAccountIdentifiersDom;
    }

    private void mmToXmlConverter(JSONObject jsonRequest, Document document, Element settlementRequest) throws IOException, TransformerException {
        JSONArray moneyMovementInstructions = jsonRequest.getJSONArray("money_movement_instruction");
        JSONObject transaction = (JSONObject) jsonRequest.get("transaction");
        for(int i=0; i<moneyMovementInstructions.length(); i++) {
            Element paymentInstructionDom = document.createElement("PaymentInstruction");
            JSONObject moneyMovementInstruction = (JSONObject) moneyMovementInstructions.get(i);
            appendElement(document, paymentInstructionDom, "ParticipantTxnId", (String) moneyMovementInstruction.get("participant_transaction_id"));
            JSONObject instructedAmount = (JSONObject) moneyMovementInstruction.get("instructed_amount");
            String amount = ((Integer) instructedAmount.get("amount")).toString() + (String) instructedAmount.get("currency_code");
            appendElement(document, paymentInstructionDom, "Amount", amount);
            appendElement(document, paymentInstructionDom, "ReferenceTxnId", ((Long)transaction.get("reference_id")).toString());
            appendElement(document, paymentInstructionDom, "MoneyMovementType", (String) moneyMovementInstruction.get("money_movement_type"));
            if(moneyMovementInstruction.get("funding_type").equals("FUNDING_TYPE_BANK")) {
                appendElement(document, paymentInstructionDom, "ProcessorId", (String) ((JSONObject) moneyMovementInstruction.get("bank")).get("processor_id"));
                appendElement(document, paymentInstructionDom, "RoutingEntity", (String) ((JSONObject) moneyMovementInstruction.get("bank")).get("routing_entity"));
            } else if(moneyMovementInstruction.get("funding_type").equals("FUNDING_TYPE_BALANCE")) {
                appendElement(document, paymentInstructionDom, "AllowClosedInstrument", String.valueOf(((JSONObject) moneyMovementInstruction.get("store_value")).get("allow_closed_holding")));
            }
            paymentInstructionDom.appendChild(buildBackingInstrument(moneyMovementInstruction, document));
            paymentInstructionDom.appendChild(buildInstrumentHolder(moneyMovementInstruction, document));
            settlementRequest.appendChild(paymentInstructionDom);
        }
    }

    private Element buildBackingInstrument(JSONObject moneyMovementInstruction, Document document) {
        Element backingInstrumentDom = document.createElement("BackingInstruction");
        if(moneyMovementInstruction.get("funding_type").equals("FUNDING_TYPE_BALANCE")) {
            JSONObject storedValue = (JSONObject) moneyMovementInstruction.get("store_value");
            JSONObject balance = (JSONObject) storedValue.get("balance");
            JSONObject balanceAmount = (JSONObject) balance.get("balance_amount");
            appendElement(document, backingInstrumentDom, "CurrencyCode", (String) balanceAmount.get("currency_code"));
            appendElement(document, backingInstrumentDom, "StoredValueId", (String) storedValue.get("sv_account_identifier"));
            appendElement(document, backingInstrumentDom, "AccountNumber", (String) storedValue.get("tenant_account_reference"));
            appendElement(document, backingInstrumentDom, "BalanceType", (String) balance.get("balance_type"));
            appendElement(document, backingInstrumentDom, "LegalEntity", (String) moneyMovementInstruction.get("legal_entity"));
        } else if (moneyMovementInstruction.get("funding_type").equals("FUNDING_TYPE_BANK")) {
            JSONObject bank = (JSONObject) moneyMovementInstruction.get("bank");
            appendElement(document, backingInstrumentDom, "BankId", (String) bank.get("id"));
            appendElement(document, backingInstrumentDom, "BankAccountType", (String) bank.get("bank_account_type"));
            appendElement(document, backingInstrumentDom, "AccountName", (String) bank.get("account_name"));
            appendElement(document, backingInstrumentDom, "CurrencyCode", (String) bank.get("bank_account_currency_code"));
            JSONObject instrumentHolder = (JSONObject) bank.get("instrument_holder");
            appendElement(document, backingInstrumentDom, "FirstName", (String) instrumentHolder.get("first_name"));
            appendElement(document, backingInstrumentDom, "LastName", (String) instrumentHolder.get("last_name"));
            JSONObject address = (JSONObject) instrumentHolder.get("address");
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressLine1", (String) address.get("line1")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressLine2", (String) address.get("line2")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressCity", (String) address.get("city")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressPostalCode", (String) address.get("postal_code")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressState", (String) address.get("state_province")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BillingAddressCountry", (String) address.get("country_code")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BankIssuerCountry", (String) bank.get("bank_country_code")));
            ignoringException(() -> appendElement(document, backingInstrumentDom, "BankIsserName", (String) bank.get("bank_name")));
            backingInstrumentDom.appendChild(buildBankAccountIdentifiers(bank, document));
        }

        return backingInstrumentDom;
    }

    private Element buildBankAccountIdentifiers(JSONObject instrument, Document document) {
        Element bankAccountIdentifiersDom = document.createElement("BankAccountIdentifiers");
        ignoringException(() -> appendElement(document, bankAccountIdentifiersDom, "bank_account_number_enc", (String) instrument.get("bank_account_number_enc")));
        ignoringException(() -> appendElement(document, bankAccountIdentifiersDom, "iban_enc", (String) instrument.get("iban_enc")));
        ignoringException(() -> appendElement(document, bankAccountIdentifiersDom, "iban_hmac", (String) instrument.get("iban_hmac")));
        return bankAccountIdentifiersDom;
    }

    private Element buildInstrumentHolder(JSONObject instrument, Document document) {
        Element instrumentHolderDom = document.createElement("InstrumentHolder");
        if(!instrument.has("instrument_holder")) {
            return instrumentHolderDom;
        }
        JSONObject instrumentHolder = (JSONObject) instrument.get("instrument_holder");
        appendElement(document, instrumentHolderDom, "Fname", (String) instrumentHolder.get("first_name"));
        appendElement(document, instrumentHolderDom, "Lname", (String) instrumentHolder.get("last_name"));
        JSONObject address = (JSONObject) instrumentHolder.get("address");
        appendElement(document, instrumentHolderDom, "AddressLine1", (String) address.get("line1"));
        appendElement(document, instrumentHolderDom, "AddressLine2", (String) address.get("line2"));
        appendElement(document, instrumentHolderDom, "AddressCity", (String) address.get("city"));
        appendElement(document, instrumentHolderDom, "AddressState", (String) address.get("state_province"));
        appendElement(document, instrumentHolderDom, "AddressCountry", (String) address.get("country_code"));
        appendElement(document, instrumentHolderDom, "AddressPostalCode", (String) address.get("postal_code"));

        return instrumentHolderDom;
    }

    private Document getSettlementRequest(Document doc, String xPathCriteria) throws IOException, TransformerException {
        return xmlHelper.getSubsetDocument(doc, xPathCriteria);
    }

    public void ignoringException(IgnoreException ignoreException) {
        try {
            ignoreException.work();
        } catch (Exception e) {
            LOGGER.info("Exception when mapping Settlement Request", e);
        }
    }

    public Document getJsonOrSpmRequest(Document doc) throws IOException, TransformerException, ParserConfigurationException {
        if(Objects.nonNull(doc) && Objects.nonNull(getSpmRequest(doc))) {
            return getXmlFromSpm(getSpmRequest(doc));
        } else if(Objects.nonNull(doc) && Objects.nonNull(getJsonRequest(doc))) {
            return getXmlFromJson(getJsonRequest(doc));
        }
        return null;
    }

    public String getJsonRequest(Document doc) {
        try {
            return xmlHelper.getNodesByXpath(doc, "//json_request").item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }

    public String getSpmRequest(Document doc) {
        try {
            return xmlHelper.getNodesByXpath(doc, "//serialized_payment_message").item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }

}
