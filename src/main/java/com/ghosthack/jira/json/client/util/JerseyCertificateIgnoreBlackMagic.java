package com.ghosthack.jira.json.client.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * Helps constructing a Jersey Config with a no-op SSLContext / TrustManager
 * <p>
 * Wraps all exceptions with a RuntimeException
 * <p>
 * 
 * @see JerseyCertificateIgnoreBlackMagic
 */
public class JerseyCertificateIgnoreBlackMagic {

    public static ClientConfig ignoreCertConfig() throws RuntimeException {
        ClientConfig config = new DefaultClientConfig();
        try {
            config.getProperties().put(
                    HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    ignoreCertHttpsProperties());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return config;
    }

    public static HTTPSProperties ignoreCertHttpsProperties()
            throws NoSuchAlgorithmException, KeyManagementException {
        return new HTTPSProperties(
                CertificateIgnoreBlackMagic.getHostnameVerifier(),
                CertificateIgnoreBlackMagic.ignoreCertSslContext());
    }

}