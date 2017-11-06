package com.jptest.payments.fulfillment.testonia.business.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.jptest.vo.ValueObject;

/**
 * ListVOBuilder wraps vo created in the build() method into a list.
 * There are quite a few VOs that are used as a single element list.  Rather 
 * than creating these type of lists over and over, ListVOBuilder implements
 * List wrapper for the vo.
 * 
 * If you are creating a vo that is used in the list you can extend this class
 * and call buildList to get List<YourVO> object.
 * 
 * @JP Inc.
 *
 * @param <T> type of the ValueObject being built.
 */
public abstract class ListWrappedVOBuilder<T extends ValueObject> implements ListVOBuilder<T> {
    
    @Override
    public List<T> buildList() {
        T vo = build();
        if (vo == null) {
            return new ArrayList<T>();
        } else {
            return Collections.singletonList(vo);
        }
    }

    
    
}
