package com.jptest.payments.fulfillment.testonia.dao;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.vo.ValueObject;


/**
 * This class all utility methods used across all DAOs
 */
public abstract class BaseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    @Inject
    protected DBHelper dbHelper;

    protected String getString(Object stringObj) {
        return (String) stringObj;
    }

    protected Long getLong(Object longObj) {
        return longObj == null ? null : Long.parseLong(longObj.toString());
    }

    protected Integer getInteger(Object intObj) {
        return intObj == null ? null : Integer.parseInt(intObj.toString());
    }

    protected BigInteger getBigInteger(Object bigIntObj) {
        return bigIntObj == null ? null : new BigInteger(bigIntObj.toString());
    }

    protected BigDecimal getBigDecimal(Object bigDecObj) {
        return bigDecObj == null ? null : new BigDecimal(bigDecObj.toString());
    }

    protected Byte getByte(Object byteObj) {
        return byteObj == null ? null : (byte) (byteObj.toString()).charAt(0);
    }

    protected String quotedString(String str) {
        return str == null ? "null" : "'" + str + "'";
    }

    protected char getChar(Object stringObj) {
        return stringObj.toString().charAt(0);
    }

    protected String blobToString(Object blobObj) throws IOException {
        String result = null;
        if (blobObj == null) {
            return result;
        }
        if (blobObj instanceof InputStream) {
            result = IOUtils.toString(((InputStream) blobObj), StandardCharsets.UTF_8);
        }
        else {
            result = blobObj.toString();
        }
        return result;
    }

    protected <T extends ValueObject> T blobToValueObject(Object blobObj, Class<T> clazz) throws IOException {
        if (blobObj == null) {
            return null;
        }

        final byte[] bytes = blobObj instanceof InputStream ? IOUtils.toByteArray((InputStream) blobObj)
                : ((String) blobObj).getBytes();
        try {
            return VoHelper.deserializeByteArray2VO(clazz, bytes);
        }
        catch (final IOException e) {
            LOGGER.error("Error occurred converting BLOB to VO.", e);
            throw e;
        }
    }
    
	protected String convertNullToEmptyString(String value) {
		return value == null || value.isEmpty() ? "" : value;
	}

	protected String convertNullToEmptyString(BigInteger value) {
		return (value == null) ? "" : value.toString();
	}

	protected String convertNullToEmptyString(Long value) {
		return (value == null) ? "" : value.toString();
	}
	
	protected String convertByteToChar(Byte value) {
		return (value == null) ? "" : String.valueOf((char)value.byteValue());
	}

    protected abstract String getDatabaseName();
}
