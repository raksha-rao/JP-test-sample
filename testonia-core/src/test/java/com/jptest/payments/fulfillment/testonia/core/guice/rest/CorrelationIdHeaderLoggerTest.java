package com.jptest.payments.fulfillment.testonia.core.guice.rest;

import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Unit test for {@link CorrelationIdHeaderLogger}
 */
public class CorrelationIdHeaderLoggerTest {

    @Mock
    private ClientRequestContext mockedRequestContext;

    @Mock
    private ClientResponseContext mockedResponseContext;

    @InjectMocks
    private CorrelationIdHeaderLogger correlationIdHeaderLogger;

    @BeforeTest
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilter() throws IOException {
        MultivaluedMap<String, String> headers = new MultivaluedHashMap<>();
        headers.add("corr_id", "bdjhbds729bc");
        when(mockedResponseContext.getHeaders()).thenReturn(headers);

        correlationIdHeaderLogger.filter(mockedRequestContext, mockedResponseContext);
    }
}
