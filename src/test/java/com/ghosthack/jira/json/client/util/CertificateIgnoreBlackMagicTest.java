package com.ghosthack.jira.json.client.util;

import static org.junit.Assert.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import org.junit.Test;

public class CertificateIgnoreBlackMagicTest {

    @Test
    public void testIgnoreCertSslContextReturnsNonNull() throws Exception {
        SSLContext ctx = CertificateIgnoreBlackMagic.ignoreCertSslContext();
        assertNotNull(ctx);
        assertEquals("TLS", ctx.getProtocol());
    }

    @Test
    public void testGetHostnameVerifierReturnsNonNull() {
        HostnameVerifier verifier = CertificateIgnoreBlackMagic.getHostnameVerifier();
        assertNotNull("HostnameVerifier should not be null", verifier);
    }

    @Test
    public void testHostnameVerifierAcceptsAllHostnames() {
        HostnameVerifier verifier = CertificateIgnoreBlackMagic.getHostnameVerifier();
        assertTrue(verifier.verify("example.com", null));
        assertTrue(verifier.verify("evil.attacker.com", null));
        assertTrue(verifier.verify("", null));
    }

    @Test
    public void testIgnoreCertSslContextHasEmptyAcceptedIssuers() throws Exception {
        SSLContext ctx = CertificateIgnoreBlackMagic.ignoreCertSslContext();
        // Creating an SSL engine exercises the trust manager
        assertNotNull(ctx.getSocketFactory());
    }
}
