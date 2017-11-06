package com.jptest.payments.fulfillment.testonia.bridge.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jptest.platform.security.securitycontext.Claims;
import com.jptest.platform.security.securitycontext.SecurityContext;
import com.jptest.platform.security.securitycontext.SecurityContext.AuthTokenType;
import com.jptest.platform.security.securitycontext.Subject;

public final class SecurityContextHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextHelper.class);

    private static final int REQ_ID_LENGTH = 8;

    public static String getSecurityContext(String accountNumber, List<String> scopes) {
        SecurityContext securityContext = new SecurityContext();
        String[] authClaims = { "USERNAME", "PASSWORD" };
        com.jptest.platform.security.securitycontext.User user = new com.jptest.platform.security.securitycontext.User();
        user.setAccountNumber(accountNumber);
        user.setAuthClaims(Arrays.asList(authClaims));
        user.setUserType("CONSUMER");
        securityContext.setActor(user);
        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject();
        user.setAuthState("LOGGEDIN");
        subject.setSubject(user);
        subjects.add(subject);
        securityContext.setSubjects(subjects);
        securityContext.setScopes(scopes);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(securityContext);
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error occurred converting to json => ", e);
            return null;
        }
    }

    public static String getRandomRequestId() {
        String alphanumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(REQ_ID_LENGTH);
        for (int i = 0; i < REQ_ID_LENGTH; i++) {
            sb.append(alphanumeric.charAt(secureRandom.nextInt(alphanumeric.length())));
        }
        return sb.toString();
    }

    public static String getDetailedSecurityContext(String accountNumber) {
        SecurityContext securityContext = new SecurityContext();
        String[] authClaims = { "CLIENT_ID_SECRET" };
        com.jptest.platform.security.securitycontext.User user = new com.jptest.platform.security.securitycontext.User();
        user.setAccountNumber(accountNumber);
        user.setAuthClaims(Arrays.asList(authClaims));
        user.setUserType("CONSUMER");
        user.setClientId("ARhjCYjsFoODly-CT0_2LDkaY-pL8JGB34HuPW8F8D6tj66w0NOqpejHhy0QujJMfWmQ3cALTskK0pTb");
        user.setId("245285");
        user.setPartyId(accountNumber);
        user.setAuthState("LOGGEDIN");
        securityContext.setActor(user);

        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject();
        subject.setSubject(user);
        subjects.add(subject);
        securityContext.setSubjects(subjects);

        String[] scopes = { "https://api.jptest.com/v1/payments/.*", "https://api.jptest.com/v1/vault/credit-card",
                "https://uri.jptest.com/services/applications/webhooks", "openid",
                "https://api.jptest.com/v1/vault/credit-card/.*", "https://uri.jptest.com/services/payments/basic" };
        securityContext.setScopes(Arrays.asList(scopes));

        securityContext.setVersion("1.2");
        securityContext.setAuthToken("A005E8r8u-dbCU9wJsbKRhr10SBn-7fuczIOtVLbCYe7hDI");
        securityContext.setAuthTokenType(AuthTokenType.ACCESS_TOKEN);
        securityContext.setGlobalSessionId("I69d3673d-b1d3-4db4-9f6a-f6f006e71c8e");
        securityContext.setAppId("APP-9UX08958JL001314M");

        Claims claims = new Claims();
        claims.setAdditionalProperties("actor_payer_id", "SZEKQNBKSSQ8L");
        securityContext.setClaims(claims);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(securityContext);
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Error occurred converting to json => ", e);
            return null;
        }
    }
}
