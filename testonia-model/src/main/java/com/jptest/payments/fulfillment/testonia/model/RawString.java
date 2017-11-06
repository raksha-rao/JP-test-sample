package com.jptest.payments.fulfillment.testonia.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.jpinc.kernel.logger.LogLevel;
import com.jpinc.kernel.logger.Logger;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Copy of {@link com.jpinc.af.common.types.RawString}
 * <p>
 * This class is copied so that we don't have to add uKernel dependency to testonia-model 
 */
@SuppressWarnings("PMD")
public class RawString implements Serializable, Cloneable {

    private static final String DEFAULT_ENCODING = "ISO8859_1";

    private byte UNKNOWN_ASCII_COMPLIANCE = 0;
    private byte ASCII_COMPLIANT = 1;
    private byte ASCII_NOT_COMPLIANT = 2;

    private byte[] m_rawData = null;
    private byte m_ascii = UNKNOWN_ASCII_COMPLIANCE;
    private String m_encoding = null;
    private String m_str = null;
    private static Logger s_logger = null;

    //
    // Constructor(s)
    //  
    public RawString() {
        this(new byte[0], DEFAULT_ENCODING);
    }

    public RawString(String str) {
        this(str, DEFAULT_ENCODING);
    }

    public RawString(String str, String encoding) {
        m_str = str;
        m_encoding = (encoding != null) ? encoding : DEFAULT_ENCODING;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public RawString(byte[] rawData, String encoding) {
        m_rawData = rawData;
        m_encoding = (encoding != null) ? encoding : DEFAULT_ENCODING;
    }

    /**
     * Return a copy of this object.
     */
    public Object clone() {
        RawString to = null;
        try {
            // GWS - 01/13/03
            // we need to make a copy of the byte array
            // since its contents could be changed by someone who
            // requests it through getBytes
            // technically if someone did actually change the 
            // contents of the array returned, then the getString
            // method could return an incorrect string
            //
            // if we are willing to assume no change to the 
            // array, then this copy is not neccesary
            //
            to = (RawString) super.clone();
            if (m_rawData != null) {
                to.m_rawData = new byte[m_rawData.length];
                System.arraycopy(m_rawData, 0, to.m_rawData, 0, m_rawData.length);
            } else {
                to.m_rawData = null;
            }
            // other fields are picked up by clone
        } catch (CloneNotSupportedException e) {
        } // Won't happen since we are clonable

        return to;
    }

    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getBytes() {
        if (m_rawData == null) {
            try {
                return m_str.getBytes(m_encoding);
            } catch (UnsupportedEncodingException e) {
                getLogger().log(LogLevel.ERROR, m_encoding + " is not supported:" + e.getMessage());
            }
        }
        return m_rawData;
    }

    public byte[] getBytes(String encoding) {
        if (m_encoding.equals(encoding)) {
            return getBytes();
        } else if (isRawDataAsciiCompliantFor(encoding)) {
            return getBytes();
        }

        byte[] data = null;
        try {
            data = getString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            getLogger().log(LogLevel.ERROR, m_encoding + " is not supported:" + e.getMessage());
        }
        return data;
    }

    /**
     * Method "isRawDataAsciiCompliantFor" checks,
     * if RawString.m_rawData can be used directly,
     * as a byte-presenation of this RawString in the requested encoding.
     * 
     * Method assumes, that nobody will try to change "m_rawData" array, 
     * because it's strange to change bynary-encoded data,
     * when it's not possible to change the lenght of that data (m_rawData.length).   
     * 
     * @param encoding  
     * @return
     */
    public boolean isRawDataAsciiCompliantFor(String encoding) {
        // Null byte-array "m_rawData" - shouldn't be considered as "Ascii Compliant For"
        if (m_rawData == null)
            return false;

        // If we already know, that "m_rawData" is ASCII_COMPLIANT    
        if (m_ascii == ASCII_COMPLIANT)
            // we will use that array
            return true;

        // If we already know, that "m_rawData" is ASCII_NOT_COMPLIANT    
        if (m_ascii == ASCII_NOT_COMPLIANT)
            // we cannot use that array
            return false;

        //    If we didn't check "Ascii Compliance" yet,
        // then we should go through array
        boolean hasSignBit = false;
        for (int i = 0; i < m_rawData.length; i++) {
            // till we will find non-ascii symbol
            if (m_rawData[i] < 0) {
                hasSignBit = true;
                break;
            }
        }

        // If non-ascii symbol existed
        if (hasSignBit) {
            // then "m_rawData" is ASCII_NOT_COMPLIANT ,
            // RawString should remember it in "m_ascii" 
            m_ascii = ASCII_NOT_COMPLIANT;
            return false;
        } else {
            // Otherwise, "m_rawData" is ASCII_COMPLIANT ,
            // which RawString should also remember in "m_ascii" 
            m_ascii = ASCII_COMPLIANT;
            return true;
        }
    }

    public String getString() {
        if (m_str == null) {
            try {
                m_str = new String(m_rawData, m_encoding);
            } catch (UnsupportedEncodingException e) {
                getLogger().log(LogLevel.ERROR, m_encoding + " is not supported:" + e.getMessage());
            }
        }
        return m_str;
    }

    public int getStrSize() {
        if (m_str != null) {
            return m_str.length();
        }
        if (m_rawData == null) {
            return 0;
        }
        return 2 * m_rawData.length;
    }

    public String toString() {
        return getString();
    }

    public int indexOf(String s) {
        if (m_str != null) {
            return m_str.indexOf(s);
        }
        if (m_rawData == null || m_rawData.length == 0 ||
                s == null || s.length() == 0) {
            return -1;
        }

        byte[] token = null;
        try {
            token = s.getBytes(m_encoding);
        } catch (UnsupportedEncodingException e) {
            getLogger().log(LogLevel.ERROR, m_encoding + " is not supported:" + e.getMessage());
            return -1;
        }

        return indexOf(token);
    }

    public RawString getRawStringAfter(String s) {
        if (m_str != null) {
            int index = m_str.indexOf(s);
            if (index != -1) {
                return new RawString(m_str.substring(index), m_encoding);
            }
            return this;
        }
        if (m_rawData == null || m_rawData.length == 0 ||
                s == null || s.length() == 0) {
            return this;
        }

        byte[] token = null;
        try {
            token = s.getBytes(m_encoding);
        } catch (UnsupportedEncodingException e) {
            getLogger().log(LogLevel.ERROR, m_encoding + " is not supported:" + e.getMessage());
            return this;
        }

        if (token.length == 0) {
            return this;
        }

        int index = indexOf(token);
        if (index == -1) {
            return this;
        }

        byte[] subBytes = new byte[m_rawData.length - (index + s.length())];
        System.arraycopy(m_rawData, index + s.length(), subBytes, 0, subBytes.length);
        return new RawString(subBytes, m_encoding);
    }

    public String getEncoding() {
        return m_encoding;
    }

    public boolean hasRawData() {
        return (m_rawData != null);
    }

    //
    // Private
    //
    private int indexOf(byte[] token) {
        int ending = m_rawData.length - token.length;
        if (ending <= 0) {
            return -1;
        }

        int index = 0;
        label: do {
            while (index <= ending && m_rawData[index] != token[0]) {
                index++;
            }
            if (index > ending) {
                return -1;
            }

            int i = index + 1;
            int j = (i + token.length) - 1;
            int k = 1;
            while (i < j) {
                if (m_rawData[i++] != token[k++]) {
                    index++;
                    continue label;
                }
            }
            return index;
        } while (true);
    }

    private static Logger getLogger() {
        if (s_logger == null) {
            s_logger = Logger.getInstance(RawString.class);
        }

        return s_logger;
    }

    private static final long serialVersionUID = -1672725935888381611L;
}
