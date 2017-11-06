package com.jptest.payments.fulfillment.testonia.business.vo;

import java.util.List;
import com.jptest.vo.ValueObject;

public interface ListVOBuilder<T extends ValueObject> extends VOBuilder<T> {

    
    /**
     * Builds a list of ValueObject. for cases where single object is wrapped 
     * in a list, buildList() can call build and wrap it in a list.
     * @return
     */
    List<T> buildList();
    
}
