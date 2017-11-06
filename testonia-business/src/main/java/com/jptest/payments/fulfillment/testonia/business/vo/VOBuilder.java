package com.jptest.payments.fulfillment.testonia.business.vo;

import com.jptest.vo.ValueObject;

/**
 * VOBuilder is a generic interface for vo builders which allows swapping builders
 * without affecting parent builder which uses nested builders.
 * 
 * example:
 * 
 * If parent uses VOBuilder<ActorInfoVO> actorInfoVOBuilder it doesn't care which
 * builder is provided for building actorInfoVO as long as it implements VOBuilder
 * interface.
 * 
 * @JP Inc.
 *
 * @param <T>
 */
public interface VOBuilder<T extends ValueObject> {

    /**
     * builds single value object
     * 
     * @return ValueObject
     */
    T build();
    
}
