package com.jptest.payments.fulfillment.testonia.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Singleton;

@Singleton
public class StringHelper {

    /**
     * converts query string to key-value pair Map
     *  
     * @param content
     * @return
     */
    public Map<String, String> convertStringToMap(String content) {
        Map<String, String> ipnValues = new HashMap<>();
        String[] elements = content.split("&");
        for (String element : elements) {
            String elementKeyValue[] = element.split("=");
            if (elementKeyValue.length <= 1) {
                continue;
            }
            ipnValues.put(elementKeyValue[0], elementKeyValue[1]);
        }
        return ipnValues;
    }

    /**
     * Maps ID list to comma separated String
     *
     * @param wTransactionList
     * @return
     */
    public static <T> String mapIdToString(Collection<T> collection, Function<T, String> mapExpression) {
        return collection
                .stream()
                .map(mapExpression)
                .distinct()
                .collect(Collectors.joining(","));
    }

}
