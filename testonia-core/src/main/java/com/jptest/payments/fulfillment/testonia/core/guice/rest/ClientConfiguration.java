package com.jptest.payments.fulfillment.testonia.core.guice.rest;

import java.util.concurrent.ExecutorService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Configuration object to control aspects of the client configuration
 *
 * @JP Inc.
 * @version $Id: $Id
 */
@CoverageIgnore
public class ClientConfiguration {
    protected String baseUrl = "";
    protected SSLContext sslContext = null;
    protected HostnameVerifier hostnameVerifier = null;
    protected ExecutorService executorService = null;
    protected int connections = 10;
    protected int responseBufferSize = 0;
    protected long socketConnectTimeout = -1;
    protected long socketTimeout = -1;

    private ClientConfiguration() {

    }

    /**
     * <p>Setter for the field <code>baseUrl</code>.</p>
     *
     * @param baseUrl a {@link java.lang.String} object.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * <p>Setter for the field <code>sslContext</code>.</p>
     *
     * @param sslContext a {@link javax.net.ssl.SSLContext} object.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return this;
    }

    /**
     * <p>Setter for the field <code>hostnameVerifier</code>.</p>
     *
     * @param hostnameVerifier a {@link javax.net.ssl.HostnameVerifier} object.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /**
     * <p>Setter for the field <code>executorService</code>.</p>
     *
     * @param executorService a {@link java.util.concurrent.ExecutorService} object.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    /**
     * <p>Setter for the field <code>connections</code>.</p>
     *
     * @param connections an int.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setConnections(int connections) {
        this.connections = connections;
        return this;
    }

    /**
     * The Response stream is wrapped in a BufferedInputStream. Value of 0 will not wrap it. Value of -1 will use a
     * SelfExpandingBufferedInputStream. Default is 0.
     *
     * @param responseBufferSize an int.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setResponseBufferSize(int responseBufferSize) {
        this.responseBufferSize = responseBufferSize;
        return this;
    }

    /**
     * Set the timeout for the initial socket connection. Timeout is in milliseconds.
     *
     * @param socketConnectTimeout
     *            Socket connection timeout in ms
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setSocketConnectTimeout(long socketConnectTimeout) {
        this.socketConnectTimeout = socketConnectTimeout;
        return this;
    }

    /**
     * Set the timeout for socket inactivity. Timeout is in milliseconds.
     *
     * @param socketTimeout
     *            Socket timeout in ms
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public ClientConfiguration setSocketTimeout(long socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    /**
     * <p>build.</p>
     *
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public static ClientConfiguration build() {
        return new ClientConfiguration();
    }

    /**
     * <p>build.</p>
     *
     * @param baseUrl a {@link java.lang.String} object.
     * @return a {@link com.jptest.platform.test.rest.ClientConfiguration} object.
     */
    public static ClientConfiguration build(String baseUrl) {
        return new ClientConfiguration().setBaseUrl(baseUrl);
    }
}
