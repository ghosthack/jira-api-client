package com.ghosthack.jira.json.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * Abstract base class for JIRA API clients providing common functionality
 * for JSON serialization, HTTP operations, and resource management.
 */
public abstract class AbstractJiraApiClient {

    protected static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    protected static final ObjectMapper MAPPER = new ObjectMapper();

    protected final WebResource webResource;

    /**
     * Creates a client with a default Jersey configuration.
     *
     * @param base the JIRA base URL (e.g., "https://jira.example.com")
     * @param path the API path to append to the base URL
     */
    protected AbstractJiraApiClient(String base, String path) {
        this(Client.create(), base, path);
    }

    /**
     * Creates a client with a custom Jersey configuration.
     *
     * @param config the Jersey client configuration
     * @param base the JIRA base URL
     * @param path the API path to append to the base URL
     */
    protected AbstractJiraApiClient(ClientConfig config, String base, String path) {
        this(Client.create(config), base, path);
    }

    /**
     * Creates a client with an existing Jersey Client instance.
     *
     * @param client the Jersey client
     * @param base the JIRA base URL
     * @param path the API path to append to the base URL
     */
    protected AbstractJiraApiClient(Client client, String base, String path) {
        this.webResource = client.resource(base).path(path);
        this.webResource.addFilter(new LoggingFilter());
    }

    /**
     * Returns the underlying Jersey WebResource for advanced usage.
     */
    public WebResource getWebResource() {
        return webResource;
    }

    /**
     * Validates a ClientResponse against the expected HTTP status code.
     *
     * @param response the response to check
     * @param expectedStatus the expected HTTP status
     * @throws JiraJsonClientException if the status does not match
     */
    protected void checkStatus(ClientResponse response, int expectedStatus)
            throws JiraJsonClientException {
        int actual = response.getStatus();
        if (actual != expectedStatus) {
            throw JiraJsonClientException.createResponseStatusException(expectedStatus, actual);
        }
    }

    /**
     * Creates an InputStreamReader from a response, using UTF-8 encoding.
     *
     * @param response the client response
     * @return an InputStreamReader wrapping the response entity
     */
    protected InputStreamReader getResponseReader(ClientResponse response) {
        InputStream is = response.getEntityInputStream();
        return new InputStreamReader(is, UTF8_CHARSET);
    }

    /**
     * Quietly closes a Closeable resource, ignoring any IOException.
     */
    protected static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
                // intentionally ignored
            }
        }
    }
}
