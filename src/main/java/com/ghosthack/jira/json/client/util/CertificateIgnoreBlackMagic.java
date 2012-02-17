package com.ghosthack.jira.json.client.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Helps constructing a no-op SSLContext / TrustManager
 * <p>
 * Avoids adding a specific cert to JDK cacerts:
 * <p>
 * JAVA_HOME=/Library/Java/Home
 * <p>
 * sudo keytool -import -alias my-alias -file my-downloaded-cert-filename
 * -keystore $JAVA_HOME/lib/security/cacerts
 * <p>
 * Default password is: changeit
 * <p>
 * Export and verify:
 * <p>
 * sudo keytool -list -v -keystore $JAVA_HOME/lib/security/cacerts
 */
public class CertificateIgnoreBlackMagic {

    public static SSLContext ignoreCertSslContext()
            throws NoSuchAlgorithmException, KeyManagementException {
        final X509TrustManager x509TrustManager = new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                    String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                    String authType) {
            }
        };
        final TrustManager[] tm = new TrustManager[] { x509TrustManager };
        final SecureRandom sr = null;
        final KeyManager[] km = null;
        final SSLContext sc = SSLContext.getInstance(TLS);
        sc.init(km, tm, sr);
        return sc;
    }

    public static HostnameVerifier getHostnameVerifier() {
        return null;
    }

    private static final String TLS = "TLS";

}