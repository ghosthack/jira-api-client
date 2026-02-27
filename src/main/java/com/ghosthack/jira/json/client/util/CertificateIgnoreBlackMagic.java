package com.ghosthack.jira.json.client.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Constructs a no-op SSLContext / TrustManager that trusts all certificates.
 *
 * <p><strong>WARNING: This disables all SSL certificate validation and should
 * only be used in development/testing environments. Never use in production.</strong></p>
 *
 * <p>This avoids having to add a specific cert to JDK cacerts:</p>
 * <pre>
 * JAVA_HOME=/Library/Java/Home
 * sudo keytool -import -alias my-alias -file my-downloaded-cert-filename \
 *   -keystore $JAVA_HOME/lib/security/cacerts
 * # Default password is: changeit
 *
 * # Export and verify:
 * sudo keytool -list -v -keystore $JAVA_HOME/lib/security/cacerts
 * </pre>
 */
public class CertificateIgnoreBlackMagic {

    private static final String TLS = "TLS";

    /**
     * Creates an SSLContext that trusts all certificates without validation.
     *
     * @return a trust-all SSLContext
     * @throws NoSuchAlgorithmException if TLS is not available
     * @throws KeyManagementException if SSLContext initialization fails
     */
    public static SSLContext ignoreCertSslContext()
            throws NoSuchAlgorithmException, KeyManagementException {
        final X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Trust all clients
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Trust all servers
            }
        };
        final TrustManager[] tm = new TrustManager[] { x509TrustManager };
        final KeyManager[] km = null;
        final SSLContext sc = SSLContext.getInstance(TLS);
        sc.init(km, tm, null);
        return sc;
    }

    /**
     * Returns a HostnameVerifier that accepts all hostnames.
     *
     * @return a no-op HostnameVerifier
     */
    public static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private CertificateIgnoreBlackMagic() {
        // Prevent instantiation
    }
}
