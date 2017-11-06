package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.VoHelper.printValueObject;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jptest.money.AsyncFulfillment;
import com.jptest.money.FulfillAsyncTasksRequest;
import com.jptest.money.FulfillAsyncTasksResponse;

@Singleton
public class AsyncFulfillmentServBridge {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFulfillmentServBridge.class);
    @Inject
    @Named("asyncfulfillmentserv")
    private AsyncFulfillment asyncFulfillmentServ;
    
    public FulfillAsyncTasksResponse fulfillAsyncTask(FulfillAsyncTasksRequest fulfillAsyncTasksRequest){
        LOGGER.info("fulfill_async_request request: {}", printValueObject(fulfillAsyncTasksRequest));
        final FulfillAsyncTasksResponse response = asyncFulfillmentServ.fulfill_async_tasks(fulfillAsyncTasksRequest);
        LOGGER.info("fulfill_async_response: {}", printValueObject(response));
        return response;
    }
}
