package com.jptest.payments.fulfillment.testonia.model.util.jaxb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

/**
 * By default JAXB converts Map to XML format: &lt;entry&gt;&lt;key&gt;key1&lt;/key&gt;&lt;value&gt;value1&lt;/value&gt;&lt;/entry&gt;
 * <p>
 * This adapter allows to convert it to XML format: &lt;key1&gt;value1&lt;/key1&gt;
 */
public class JAXBMapToListAdapter extends XmlAdapter<JAXBMapToListAdapter.ListWrapper, Map<String, String>> {

    @Override
    public ListWrapper marshal(Map<String, String> map) throws Exception {
        ListWrapper wrapper = new ListWrapper();
        TreeMap<String, String> sortedMap = new TreeMap<>(map);
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            wrapper.add(new JAXBElement<String>(new QName(entry.getKey()), String.class, entry.getValue()));
        }
        return wrapper;
    }

    @Override
    public Map<String, String> unmarshal(ListWrapper elements) throws Exception {
        return new HashMap<>();
    }

    static class ListWrapper {
        @XmlAnyElement
        private List<JAXBElement<String>> elements = new ArrayList<>();

        public void add(JAXBElement<String> element) {
            elements.add(element);
        }
    }

}
