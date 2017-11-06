package com.jptest.payments.fulfillment.testonia.model.util.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JAXBByteToStringAdapter extends XmlAdapter<String, Byte> {

    @Override
    public Byte unmarshal(String strObj) throws Exception {
        
        return strObj != null ? (byte)strObj.charAt(0) : null;
    }

    @Override
    public String marshal(Byte byteObj) throws Exception {
        return byteObj != null ? String.valueOf((char) (byteObj.byteValue() & 0xFF)) : null;
    }

}
