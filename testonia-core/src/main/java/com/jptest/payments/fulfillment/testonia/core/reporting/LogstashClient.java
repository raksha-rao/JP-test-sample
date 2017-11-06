package com.jptest.payments.fulfillment.testonia.core.reporting;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guice injected logstash client
 * 
 * @JP Inc.
 */
@Singleton
public class LogstashClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogstashClient.class);

    @Inject
    private Configuration configuration;

    private Socket socket;

    @Inject
    public void init() {
        String logstashHostPort = configuration.getString("logstash.host.port");

        if (logstashHostPort != null && logstashHostPort.contains(":")) {
            String[] hostAndPort = logstashHostPort.split(":");
            int port = Integer.parseInt(hostAndPort[1]);
            try {
                socket = new Socket(hostAndPort[0], port);
            } catch (Exception e) {
                LOGGER.error("unable to create socket to logstash at " + logstashHostPort, e);
            }
        } else {
            socket = new Socket();
            LOGGER.error(
                    "Cannot create logstash client with {} as host and port are not set in properties file. Please define property logstash.host.port",
                    logstashHostPort);
        }
    }

    public void write(String message) {
        if (socket != null && socket.isConnected()) {
            try {
                PrintStream out = new PrintStream(socket.getOutputStream());
                out.println(message);
            } catch (IOException e) {
                LOGGER.error("Error occurred sending message on logstash socket", e);
            }
        }
    }

}
