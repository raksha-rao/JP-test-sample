package com.jptest.payments.fulfillment.testonia.core;

/**
 * Represents a test case component that executes some
 * business logic and usually produces some output.
 */
public interface TestComponent<V> extends Component {

    V execute(Context context);

    default Object getDataFromContext(Context context, String key) {
        return context.getData(key);
    }
    
    default boolean shouldParticipate(Context context) {
    	return true;
    }

}
