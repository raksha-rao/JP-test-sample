package com.jptest.payments.fulfillment.testonia.bridge;

import static com.jptest.payments.fulfillment.testonia.core.util.JsonHelper.printJsonObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpinc.kernel.util.URLDecoder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionBusinessException;
import com.jptest.payments.fulfillment.testonia.model.UserCreationTaskInput;
import com.jptest.qi.rest.domain.pojo.User;

/**
 * Represents bridge for MsMonitor API calls
 */
@Singleton
public class MsMonitorBridge {

    private static final String VALUE_PARAM = "value";

    private static final String KEY_PARAM = "key";

    private static final String DOMAIN_PARAM = "domain";

    private static final String MS_ENVIRONMENT_PARAM = "msEnvironment";

    private static final String MSMASTER_PARAM_VALUE = "msmaster";

    private static final String DOMAIN_PARAM_VALUE = "payments";

    private static final String CORR_ID_PARAM = "corr_id";

    private static final String FAULT_DATA_PARAM = "fault_data";

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(MsMonitorBridge.class);

    @Inject
    private Configuration config;

    //    @Inject
    //    @Named("msmonitorresource")
    //    private MsMonitorResource msMontiorResource;

    public MsMonitorBridge() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public User getInstantUser(UserCreationTaskInput testInput) {

        try {
            String shaKey = testInput.generateKey();
            LOGGER.info("getInstantUser for key: {}", shaKey);
            //TODO: Use resource instead of http client.
            // String response = msMontiorResource.getInstantUser(input.getMsEnvironment(), input.getDomain(),
            // input.getKey());
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(
                    config.getString(BridgeConfigKeys.MSMONITOR_GET_URL.getName()));

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair(MS_ENVIRONMENT_PARAM, MSMASTER_PARAM_VALUE));
            urlParameters.add(new BasicNameValuePair(DOMAIN_PARAM, DOMAIN_PARAM_VALUE));
            urlParameters.add(new BasicNameValuePair(KEY_PARAM, shaKey));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            String userInput = URLDecoder.decode(IOUtils.toString(response.getEntity().getContent()));
            User user = objectMapper.readValue(userInput,
                    User.class);
            LOGGER.info("getInstantUser response: {}", printJsonObject(user));
            return user;
        } catch (RuntimeException | IOException e) {
            // Since this is best effort, no need to further process the exception.
            LOGGER.warn("Failed while retrieving user from msMonitor cache");
            return null;
        }
    }

    public boolean addUser(UserCreationTaskInput testInput, User actualUser) {
        try {
            String shaKey = testInput.generateKey();
            String serializedUser = objectMapper.writeValueAsString(actualUser);
            LOGGER.info("add User content: {}", serializedUser);
            String value = URLEncoder.encode(serializedUser, "UTF-8");

            LOGGER.info("Adding User for key: {} with data {}", shaKey, value);
            //TODO: Use resource instead of http client.
            // Response response = msMontiorResource.addUser(input.getMsEnvironment(), input.getDomain(),
            // input.getKey(),
            // input.getValue());

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(
                    config.getString(BridgeConfigKeys.MSMONITOR_ADD_URL.getName()));

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair(MS_ENVIRONMENT_PARAM, MSMASTER_PARAM_VALUE));
            urlParameters.add(new BasicNameValuePair(DOMAIN_PARAM, DOMAIN_PARAM_VALUE));
            urlParameters.add(new BasicNameValuePair(KEY_PARAM, shaKey));
            urlParameters.add(new BasicNameValuePair(VALUE_PARAM, value));

            post.setEntity(new UrlEncodedFormEntity(urlParameters));

            HttpResponse response = client.execute(post);

            LOGGER.info("Add User response: {}", printJsonObject(response));
            if (Response.Status.Family.SUCCESSFUL == Response.Status.Family
                    .familyOf(response.getStatusLine().getStatusCode())) {
                LOGGER.info("Successfully finished caching the user for key {} ",
                        shaKey);
                return true;
            } else {
                LOGGER.warn("failed while adding user to msMonitor cache");
            }
        } catch (IOException e) {
            // Since this is best effort, no need to further process the exception
            LOGGER.warn("Got exception trying to add user to msMonitor cache", e);
        }
        return false;
    }

    public void serviceTimeout(String correlationId, String port, int duration) {
        try {
            LOGGER.info("CorrelationId for testonia: {}", correlationId);
            String fault_data = "{\"delay\":{\"" + port + "\":\"" + duration + "\"}}";
            HttpClient client = HttpClientBuilder.create().build();
            LOGGER.info("MSmonitor URL to hit: {}",
                    config.getString(BridgeConfigKeys.MSMONITOR_FAULTINJ_ADD_URL.getName()));
            HttpPost post = new HttpPost(config.getString(BridgeConfigKeys.MSMONITOR_FAULTINJ_ADD_URL.getName()));
            LOGGER.info("Fault Data is: {}", fault_data);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(CORR_ID_PARAM, correlationId));
            urlParameters.add(new BasicNameValuePair(FAULT_DATA_PARAM, fault_data));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse httpResponse = client.execute(post);
            LOGGER.info("Add new fault Response Code: {}", httpResponse.getStatusLine().getStatusCode());
        }

        catch (Exception e) {
            throw new SomeBusinessException("Couldn't mock service timeout");
        }
    }

    public void serviceMarkDown(String correlationId, String port) {
        try {
            LOGGER.info("CorrelationId for testonia: {}", correlationId);
            String fault_data = "{\"markdown\":{\"ports\":[\"" + port + "\"]}}";
            HttpClient client = HttpClientBuilder.create().build();
            LOGGER.info("MSmonitor URL to hit: {}",
                    config.getString(BridgeConfigKeys.MSMONITOR_FAULTINJ_ADD_URL.getName()));
            HttpPost post = new HttpPost(config.getString(BridgeConfigKeys.MSMONITOR_FAULTINJ_ADD_URL.getName()));
            LOGGER.info("Fault Data is: {}", fault_data);
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair(CORR_ID_PARAM, correlationId));
            urlParameters.add(new BasicNameValuePair(FAULT_DATA_PARAM, fault_data));
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse httpResponse = client.execute(post);
            LOGGER.info("Add new fault Response Code : {}", httpResponse.getStatusLine().getStatusCode());
        }

        catch (Exception e) {
            throw new SomeBusinessException("Couldn't mock service timeout");
        }
    }

    private static class SomeBusinessException extends TestExecutionBusinessException {

        @Override
        public TestoniaExceptionReasonCode getReasonCode() {
            return TestoniaExceptionReasonCode.FAILURE_GENERIC_ERROR;
        }

        public SomeBusinessException(String message) {
            super(message);
        }

    }

}
