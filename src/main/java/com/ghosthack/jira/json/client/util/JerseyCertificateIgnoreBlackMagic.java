package com.ghosthack.jira.json.client.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * Constructs a Jersey ClientConfig with a no-op SSLContext / TrustManager.
 *
 * <p><strong>WARNING: This disables all SSL certificate validation and should
 * only be used in development/testing environments. Never use in production.</strong></p>
 *
 * @see CertificateIgnoreBlackMagic
 */
public class JerseyCertificateIgnoreBlackMagic {

    /**
     * Creates a Jersey ClientConfig with SSL certificate validation disabled.
     *
     * @return a ClientConfig that trusts all certificates
     * @throws RuntimeException if SSLContext cannot be created
     */
    public static ClientConfig ignoreCertConfig() {
        ClientConfig config = new DefaultClientConfig();
        try {
            config.getProperties().put(
                    HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                    ignoreCertHttpsProperties());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create certificate-ignoring config", e);
        }
        return config;
    }

    /**
     * Creates HTTPSProperties that disable SSL certificate validation.
     *
     * @return HTTPSProperties with a trust-all SSLContext
     * @throws NoSuchAlgorithmException if TLS is not available
     * @throws KeyManagementException if SSLContext initialization fails
     */
    public static HTTPSProperties ignoreCertHttpsProperties()
            throws NoSuchAlgorithmException, KeyManagementException {
        return new HTTPSProperties(
                CertificateIgnoreBlackMagic.getHostnameVerifier(),
                CertificateIgnoreBlackMagic.ignoreCertSslContext());
    }

    private JerseyCertificateIgnoreBlackMagic() {
        // Prevent instantiation
    }
}
