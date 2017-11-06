package com.jptest.payments.fulfillment.testonia.core.guice.rest;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.jpincinc.platform.services.ASF;
import com.jptest.jaxrs.protobuf.ProtoBufMessageHandler;
import com.jptest.platform.security.jptestSSLHelper;
import com.jptest.platform.security.ProtectedPkgSSLConfigProvider;
import com.jptest.platform.test.rest.utils.ProxyResourceFactory;
import com.jptest.platform.test.ssl.UberTrustyTrustManager;
import com.jptest.test.asf.AsfFeature;

import net.sourceforge.cobertura.CoverageIgnore;

/**
 * Main client manager that keeps track of a configured REST Client
 * and can produce things like WebTargets and other goodies
 *
 * @JP Inc.
 */
@CoverageIgnore
public class RestClientManager {

    private HashMap<String, Client> clients;
    private HashMap<String, URI> baseAddresses;

    private RestClientManager() {
        clients = new HashMap<>();
        baseAddresses = new HashMap<>();
    }

    private static class SingletonHolder {
        private static final RestClientManager INSTANCE = new RestClientManager();
    }

    public static RestClientManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void registerClient(String id, ClientConfiguration config) {
        Client client = clients.get(id);
        if (client == null) {
            String baseurl = config.baseUrl;
            if (!baseurl.endsWith("/")) {
                baseurl += "/";
            }
            URI uri = URI.create(baseurl);
            baseAddresses.put(id, uri);
            client = buildClient(config);
            clients.put(id, client);
        }
    }

    public synchronized void removeClient(String id) {
        baseAddresses.remove(id);
        Client client = clients.get(id);
        if (client != null) {
            client.close();
        }
        clients.remove(id);
    }

    public Client getRawClient(String id) {
        Client client = clients.get(id);
        if (client == null) {
            throw new IllegalStateException("Unable to load client: " + id + " it has not been registered");
        }
        return client;
    }

    public WebTarget getBaseTarget(String id) {
        Client client = getRawClient(id);
        URI uri = getURI(id);
        WebTarget ret = client.target(uri);
        return ret;
    }

    public WebTarget getAndAppendTarget(String id, String path) {
        return getBaseTarget(id).path(path);
    }

    public <C> C getProxy(String id, Class<C> clazz) {
        WebTarget target = getBaseTarget(id);
        if (clazz.isAnnotationPresent(ASF.class)) {
            target.register(AsfFeature.class);
        } else {
            target.register(ProtoBufMessageHandler.class);
        }
        
        target.register(new CorrelationIdHeaderLogger());
        
        return ProxyResourceFactory.newResource(clazz, target);
    }

    public URI getURI(String id) {
        URI ret = baseAddresses.get(id);
        if (ret == null) {
            throw new IllegalStateException("Unable to load client: " + id + " it has not been registered");
        }
        return ret;
    }

    private Client buildClient(ClientConfiguration config) {
        //ugh need to bind this to RestEasy but there is not other easy way at the JAX-RS level... it would
        //make things very complicated
        ResteasyClientBuilder builder = (ResteasyClientBuilder) ResteasyClientBuilder
                .newBuilder();
        if (config.sslContext != null) {
            builder.sslContext(config.sslContext);
        } else {
            builder.sslContext(getSSLContext());
        }
        ///executor service
        if (config.executorService != null) {
            builder.asyncExecutor(config.executorService);
        }

        if (config.hostnameVerifier != null) {
            builder.hostnameVerifier(config.hostnameVerifier);
        } else {
            //force a hostname verifier that is super friendly
            builder.hostnameVerifier(new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslSession) {
                    return true; // To change body of implemented methods use
                    // File | Settings | File Templates.
                }
            });
        }
        builder.connectionPoolSize(config.connections);
        builder.maxPooledPerRoute(config.connections);
        builder.responseBufferSize(config.responseBufferSize);
        builder.socketTimeout(config.socketTimeout, TimeUnit.MILLISECONDS);
        builder.establishConnectionTimeout(config.socketConnectTimeout, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    private SSLContext getSSLContext() {
        URL protectedUrl = RestClientManager.class.getResource("/protected/protected.jks");
        if (protectedUrl == null) {
            return vanillaSSLContext();
        } else {
            return protectedSSLContext();
        }
    }

    private SSLContext vanillaSSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1");
            context.init(null,
                    new TrustManager[] { new UberTrustyTrustManager() },
                    new java.security.SecureRandom());
            return context;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create SSL context", e);
        }
    }

    private SSLContext protectedSSLContext() {
        try {
            //initialize the protected package without the need of a password
            jptestSSLHelper.initializeSecurity();
            ProtectedPkgSSLConfigProvider sslProvider = new ProtectedPkgSSLConfigProvider();
            return sslProvider.getInternalSSLContext();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create SSL context", e);
        }
    }

}
