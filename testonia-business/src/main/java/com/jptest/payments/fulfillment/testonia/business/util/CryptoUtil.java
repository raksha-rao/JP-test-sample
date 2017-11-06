package com.jptest.payments.fulfillment.testonia.business.util;

import java.math.BigInteger;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.jptest.foundation.security.EncryptorRegistry;
import com.jptest.foundation.security.FieldEncryptor;
import com.jptest.foundation.security.crypto.ObjectIdEncryptor;
import com.jptest.infra.protectedpkg.utils.StringUtil;
import com.jptest.payments.fulfillment.testonia.business.annotations.EffectivelyImmutable;
import com.jptest.platform.security.jptestSSLHelper;

import net.sourceforge.cobertura.CoverageIgnore;

@Singleton
@CoverageIgnore
@EffectivelyImmutable
public class CryptoUtil {
    private static final String TRANSACTION_IV_PASSPHRASE = "encrypted_transaction_id_pimp_mac_key";
    private static final String TRANSACTION_KEY_PASSPHRASE = "encrypted_transaction_id_pimp_crypt_key";
    private static final String AGREEMENT_IV_PASSPHRASE= "encrypted_order_id_mac_key";
    private static final String AGREEMENT_KEY_PASSPHRASE= "encrypted_order_id_crypt_key";
    private static final String OFFER_ACCOUNT_NUMBER_CODE_IV_PASSPHRASE = "encrypted_account_number_code_iv_passphrase";
    private static final String OFFER_ACCOUNT_NUMBER_CODE_KEY_PASSPHRASE = "encrypted_account_number_code_key_passphrase";

    private static final String ENCRYPTOR_API_TRANSACTION = "TRANSACTION";
    private static final String ENCRYPTOR_API_AGREEMENT = "ORDER";
    private static final String ENCRYPTOR_API_OFFER = "OFFER";

    private static final int ACCOUNT_LOCATOR_BITS = 12;
    private static final int MAX_ID_BITS = 48;
    private static final int ENCODING_TYPE_BITS = 4;
    private static final int MAX_ACTIVITY_ID_BITS = 56;

    private static final int ACCOUNT_LOCATOR_OFFSET = MAX_ID_BITS;
    private static final BigInteger ACCOUNT_LOCATOR_MASK = BigInteger.valueOf(1L).shiftLeft(ACCOUNT_LOCATOR_BITS)
            .subtract(BigInteger.ONE).shiftLeft(ACCOUNT_LOCATOR_OFFSET);
    private static final int ENCODING_TYPE_OFFSET = MAX_ID_BITS + ACCOUNT_LOCATOR_BITS;

    // masks for the fields
    private static final BigInteger ENCODE_ID_MASK = BigInteger.valueOf(1L).shiftLeft(MAX_ID_BITS)
            .subtract(BigInteger.ONE);
    private static final BigInteger ENCODING_TYPE_MASK = BigInteger.valueOf(1L).shiftLeft(ENCODING_TYPE_BITS)
            .subtract(BigInteger.ONE).shiftLeft(ENCODING_TYPE_OFFSET);

    // Other than legacy, we now have two other way of encoding
    // legacy encoding
    private static final BigInteger FLAG_ENCODING_TYPE_LEGACY = BigInteger.valueOf(0L).shiftLeft(ENCODING_TYPE_OFFSET);
    // Non-Standin encoding (Normal mode)
    private static final BigInteger FLAG_ENCODING_TYPE_ACCOUNT_ID = BigInteger.valueOf(1L)
            .shiftLeft(ENCODING_TYPE_OFFSET);
    // Standin encoding (Standin mode)
    private static final BigInteger FLAG_ENCODING_TYPE_STANDIN_ID = BigInteger.valueOf(2L)
            .shiftLeft(ENCODING_TYPE_OFFSET);
    // encoding for fake wtrans ids generated for Payments 2.0 transactions
    private static final BigInteger FLAG_ENCODING_TYPE_P20_WTXN_ID = BigInteger.valueOf(4L)
            .shiftLeft(ENCODING_TYPE_OFFSET);

    private static final BigInteger ACTIVITY_ID_MASK = BigInteger.valueOf(1L).shiftLeft(MAX_ACTIVITY_ID_BITS)
            .subtract(BigInteger.ONE);
    private static final int ACTIVITY_ID_LEDGER_TYPE_OFFSET = MAX_ACTIVITY_ID_BITS;
    private static final BigInteger FLAG_ACTIVITY_ID_LEDGER_TYPE_CREDIT = BigInteger.valueOf(1L).shiftLeft(ACTIVITY_ID_LEDGER_TYPE_OFFSET);
    private static final BigInteger FLAG_ACTIVITY_ID_LEDGER_TYPE_DEBIT = BigInteger.valueOf(0L).shiftLeft(ACTIVITY_ID_LEDGER_TYPE_OFFSET);
    private static final BigInteger FLAG_ENCODING_TYPE_ACTIVITY_ID = BigInteger.valueOf(3L).shiftLeft(ENCODING_TYPE_OFFSET);
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

    @Inject
    protected void initialize() throws Exception {
        jptestSSLHelper.initializeSecurity();

        Properties txnEncryptProps = new Properties();
        txnEncryptProps.put("class", ObjectIdEncryptor.class.getName());
        txnEncryptProps.put(ObjectIdEncryptor.IV_NAME_PROP, TRANSACTION_IV_PASSPHRASE);
        txnEncryptProps.put(ObjectIdEncryptor.KEY_NAME_PROP, TRANSACTION_KEY_PASSPHRASE);
        txnEncryptProps.put(ObjectIdEncryptor.BASE32_TWISTED, "true");
        EncryptorRegistry.register(ENCRYPTOR_API_TRANSACTION, txnEncryptProps);

        Properties orderEncryptProps = new Properties();
        orderEncryptProps.put("class", ObjectIdEncryptor.class.getName());
        orderEncryptProps.put(ObjectIdEncryptor.IV_NAME_PROP, AGREEMENT_IV_PASSPHRASE);
        orderEncryptProps.put(ObjectIdEncryptor.KEY_NAME_PROP, AGREEMENT_KEY_PASSPHRASE);
        orderEncryptProps.put(ObjectIdEncryptor.BASE32_TWISTED, "true");
        orderEncryptProps.put(ObjectIdEncryptor.PREFIX_PROP, "O-");
        EncryptorRegistry.register(ENCRYPTOR_API_AGREEMENT, orderEncryptProps);

        Properties offerEncryptProps = new Properties();
        offerEncryptProps.put("class", ObjectIdEncryptor.class.getName());
        offerEncryptProps.put(ObjectIdEncryptor.IV_NAME_PROP, OFFER_ACCOUNT_NUMBER_CODE_IV_PASSPHRASE);
        offerEncryptProps.put(ObjectIdEncryptor.KEY_NAME_PROP, OFFER_ACCOUNT_NUMBER_CODE_KEY_PASSPHRASE);
        offerEncryptProps.put(ObjectIdEncryptor.BASE32_TWISTED, "false");
        EncryptorRegistry.register(ENCRYPTOR_API_OFFER, offerEncryptProps);
    }

    public long decryptTxnId(String encryptedTransactionId) throws Exception {
        long decryptedId = 0l;
        if (StringUtil.isEmpty(encryptedTransactionId)) {
            return decryptedId;
        }
        FieldEncryptor<Long> fieldEncryptor = EncryptorRegistry.getFieldEncryptor(Long.class,
                ENCRYPTOR_API_TRANSACTION);
        long crunchedId = fieldEncryptor.decrypt("", encryptedTransactionId);
        decryptedId = uncrunchValues(crunchedId);
        return decryptedId;
    }

    public String encryptTxnId(long rawTxnId, long accountNumber) throws Exception {
        FieldEncryptor<Long> fieldEncryptor = EncryptorRegistry.getFieldEncryptor(Long.class,
                ENCRYPTOR_API_TRANSACTION);
        return encrypt(fieldEncryptor, accountNumber, rawTxnId);
    }

    public String encryptActivityId(char ledgerType, BigInteger rawActivityId) throws Exception {
        if (rawActivityId == null || rawActivityId.equals(BigInteger.ZERO)) {
            return null;
        }

        BigInteger ledgerTypeFlag;
        switch (ledgerType) {
        case PActivityTransMapConstants.LEDGER_TYPE_CREDIT:
            ledgerTypeFlag = FLAG_ACTIVITY_ID_LEDGER_TYPE_CREDIT;
            break;

        case PActivityTransMapConstants.LEDGER_TYPE_DEBIT:
            ledgerTypeFlag = FLAG_ACTIVITY_ID_LEDGER_TYPE_DEBIT;
            break;
        default:
            throw new Exception("Invalid ledger type specified: " + ledgerType);
        }
        BigInteger val = FLAG_ENCODING_TYPE_ACTIVITY_ID.or(rawActivityId)
                .or(ledgerTypeFlag);
        FieldEncryptor<Long> fieldEncryptor = EncryptorRegistry.getFieldEncryptor(Long.class,
                ENCRYPTOR_API_TRANSACTION);
        return fieldEncryptor.encrypt(ENCRYPTOR_API_TRANSACTION, val.longValue());
    }

    public long decryptOfferId(String offerId) throws Exception {
        long decryptedId = 0l;
        if (StringUtil.isEmpty(offerId)) {
            return decryptedId;
        }
        FieldEncryptor<Long> fieldEncryptor = EncryptorRegistry.getFieldEncryptor(
                Long.class, ENCRYPTOR_API_OFFER);
        long crunchedId = fieldEncryptor.decrypt("", offerId);
        decryptedId = uncrunchValues(crunchedId);
        return decryptedId;
    }

    private long uncrunchValues(long crunchedId) {
        BigInteger id = null;
        BigInteger crunchedValue = BigInteger.valueOf(crunchedId);
        BigInteger encodingType = ENCODING_TYPE_MASK.and(crunchedValue);

        if (encodingType.equals(FLAG_ENCODING_TYPE_ACCOUNT_ID)) {
            id = crunchedValue.and(ENCODE_ID_MASK);
        } else if (encodingType.equals(FLAG_ENCODING_TYPE_STANDIN_ID)) {
            id = crunchedValue.and(ENCODE_ID_MASK);
        } else if (encodingType.equals(FLAG_ENCODING_TYPE_LEGACY)) {
            id = crunchedValue;
        } else if (encodingType.equals(FLAG_ENCODING_TYPE_P20_WTXN_ID)) {
            id = crunchedValue.and(ACTIVITY_ID_MASK);
        } else {
            id = BigInteger.valueOf(-1);
        }
        return id.longValue();
    }

    private String encrypt(FieldEncryptor<Long> fieldEncryptor, long accountNumber, long rawTxnId) {
        if (fieldEncryptor == null) {
            throw new IllegalArgumentException("Field Encryptor is null");
        }
        long crunchedValue = crunchValues(BigInteger.valueOf(accountNumber), BigInteger.valueOf(rawTxnId), false);
        return fieldEncryptor.encrypt("?", crunchedValue);
    }

    private long crunchValues(BigInteger accountNumber, BigInteger id, boolean inStandin) {
        BigInteger crunchedVal;
        if (inStandin) {
            crunchedVal = FLAG_ENCODING_TYPE_STANDIN_ID.or(id);
        } else if (!id.and(ENCODE_ID_MASK).equals(id)) {
            crunchedVal = FLAG_ENCODING_TYPE_P20_WTXN_ID.or(id.and(ACTIVITY_ID_MASK));
        } else if (accountNumber.equals(BigInteger.ZERO)) {
            crunchedVal = FLAG_ENCODING_TYPE_LEGACY.or(id);
        } else {
            crunchedVal = FLAG_ENCODING_TYPE_ACCOUNT_ID.or(
                    getRawAccountLocator(accountNumber).shiftRight(ACCOUNT_LOCATOR_OFFSET - MAX_ID_BITS)).or(id);
        }
        return crunchedVal.longValue();
    }

    private BigInteger getRawAccountLocator(BigInteger accountNumber) {
        return accountNumber.and(ACCOUNT_LOCATOR_MASK);
    }

    public String encryptOrderId(BigInteger orderId) {
        try {
            Assert.notNull(orderId, "encryptOrderId: orderId is null");

            FieldEncryptor<Long> orderIdEncryptor = EncryptorRegistry.getFieldEncryptor(Long.class,
                    ENCRYPTOR_API_AGREEMENT);

            return orderIdEncryptor.encrypt(ENCRYPTOR_API_AGREEMENT, crunchOrderId(orderId));
        } catch (Exception ex) {
            LOGGER.error("Unable to encrypt order Id:", ex);
            return null;
        }
    }

    private long crunchOrderId(BigInteger orderId) {
        Assert.isTrue(!orderId.equals(orderId.and(ENCODE_ID_MASK)), "crunchOrderId: invalid raw order id");
        return orderId.and(ACTIVITY_ID_MASK).or(FLAG_ENCODING_TYPE_P20_WTXN_ID).longValue();
    }
}
