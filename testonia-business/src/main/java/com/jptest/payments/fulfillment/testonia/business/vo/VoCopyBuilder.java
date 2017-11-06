package com.jptest.payments.fulfillment.testonia.business.vo;

import com.jptest.vo.ValueObject;

/**
 * VoCopyBuilder is a holder of already built ValueObject.   For cases where we 
 * already have a value object and need to add it to a parent builder, VoCopyBuilder
 * can be used.
 * 
 * @JP Inc.
 *
 * @param <T>
 */
public class VoCopyBuilder<T extends ValueObject> extends ListWrappedVOBuilder<T> {

    private T vo;
    
    private VoCopyBuilder(T vo) {
        this.vo = vo;
    }
    
    
    public static <T extends ValueObject> VOBuilder<T> newBuilder(T vo) {
        return new VoCopyBuilder<>(vo);
    }
    
    
    @Override
    public T build() {
        return vo;
    }
}
