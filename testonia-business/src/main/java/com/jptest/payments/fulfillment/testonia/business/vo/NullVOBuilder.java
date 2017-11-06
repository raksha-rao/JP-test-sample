package com.jptest.payments.fulfillment.testonia.business.vo;

import java.util.List;
import com.jptest.vo.ValueObject;

/**
 * Null Builder to be used for null elements to avoid NPEs for elements which
 * are not required.
 * 
 * @JP Inc.
 *
 * @param <T>
 */
public class NullVOBuilder<T extends ValueObject> implements ListVOBuilder<T> {

    @Override
    public T build() {
        return null;
    }

    @Override
    public List<T> buildList() {
        return null;
    }
    
    public static <T extends ValueObject> ListVOBuilder<T> newBuilder() {
        return new NullVOBuilder<>();
    }

}
