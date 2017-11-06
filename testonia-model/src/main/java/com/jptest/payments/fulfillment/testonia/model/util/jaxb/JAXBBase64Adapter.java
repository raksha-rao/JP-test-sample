package com.jptest.payments.fulfillment.testonia.model.util.jaxb;

import java.nio.charset.StandardCharsets;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.codec.binary.Base64;

public class JAXBBase64Adapter extends XmlAdapter<String, String> {

    @Override
    public String marshal(String str) throws Exception {
        return str == null ? null
                : new String(Base64.encodeBase64(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @Override
    public String unmarshal(String arg0) throws Exception {
        return arg0;
    }

}
