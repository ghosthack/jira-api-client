package com.ghosthack.jira.json.client;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghosthack.jira.json.client.model.rpc.Issue;
import com.ghosthack.jira.json.client.model.rpc.RpcParam;
import com.ghosthack.jira.json.client.model.rpc.RpcRequest;
import com.ghosthack.jira.json.client.model.rpc.RpcString;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;

/**
 * JIRA API JSON-RPC 2.0 Client for the transition between JIRA 4.x to 5.x.
 *
 * <p>This client communicates with JIRA's JSON-RPC bridge endpoint to provide
 * programmatic access to JIRA operations such as login, issue creation, and
 * issue retrieval.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("http://localhost:8080");
 * String token = client.login("admin", "admin");
 * Map&lt;String, Object&gt; issue = client.getIssue(token, "PROJ-123");
 * </pre>
 */
public class JiraJsonRpcApiClient extends AbstractJiraApiClient {

    protected static final String RPC_JSON_RPC_JIRASOAPSERVICE_V2 = "/rpc/json-rpc/jirasoapservice-v2";
    protected static final String JSON_RPC_2_0 = "2.0";

    protected static final String LOGIN = "login";
    protected static final String GET_ISSUE = "getIssue";
    protected static final String CREATE_ISSUE = "createIssue";

    protected static final String RESULT = "result";
    protected static final String ERROR = "error";

    private final AtomicLong requestIdCounter = new AtomicLong(0);

    /**
     * Creates a client with a default Jersey configuration.
     *
     * @param base the JIRA base URL (e.g., "http://localhost:8080")
     */
    public JiraJsonRpcApiClient(String base) {
        super(base, RPC_JSON_RPC_JIRASOAPSERVICE_V2);
    }

    /**
     * Creates a client with a custom Jersey configuration.
     *
     * @param base the JIRA base URL
     * @param config the Jersey client configuration
     */
    public JiraJsonRpcApiClient(String base, ClientConfig config) {
        super(config, base, RPC_JSON_RPC_JIRASOAPSERVICE_V2);
    }

    /**
     * Creates a client with a pre-built Jersey Client.
     *
     * @param client the Jersey client
     * @param base the JIRA base URL
     */
    protected JiraJsonRpcApiClient(Client client, String base) {
        super(client, base, RPC_JSON_RPC_JIRASOAPSERVICE_V2);
    }

    /**
     * Authenticates and obtains a security token.
     *
     * @param user the JIRA username
     * @param pass the JIRA password
     * @return the security token string
     * @throws JiraJsonClientException on authentication or communication failure
     */
    public String login(String user, String pass) throws JiraJsonClientException {
        return post(String.class, LOGIN, new RpcString(user), new RpcString(pass));
    }

    /**
     * Retrieves an issue by key.
     *
     * @param token the security token from {@link #login}
     * @param issueKey the issue key (e.g., "PROJ-123")
     * @return the issue data as a Map
     * @throws JiraJsonClientException on communication or parse failure
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getIssue(String token, String issueKey)
            throws JiraJsonClientException {
        return post(Map.class, GET_ISSUE, new RpcString(token), new RpcString(issueKey));
    }

    /**
     * Creates an issue in JIRA.
     *
     * @param token the security token from {@link #login}
     * @param issue the issue to create
     * @return the created issue data as a Map
     * @throws JiraJsonClientException on communication or parse failure
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createIssue(String token, Issue issue)
            throws JiraJsonClientException {
        return post(Map.class, CREATE_ISSUE, new RpcString(token), issue);
    }

    /**
     * Sends a JSON-RPC request and returns the typed result.
     */
    protected <T> T post(Class<T> clazz, String method, RpcParam... params)
            throws JiraJsonClientException {
        return post(clazz, createRequestId(), 200, method, params);
    }

    /**
     * Sends a JSON-RPC request with explicit ID and expected status, returning the typed result.
     */
    @SuppressWarnings("unchecked")
    protected <T> T post(Class<T> clazz, long id, int expectedStatus,
            String method, RpcParam... params) throws JiraJsonClientException {
        RpcRequest req = createJsonRpcRequest(id, method, params);
        ClientResponse res = postRequest(req);
        Map<String, Object> json = responseAsJsonWithCode(res, expectedStatus);
        Object result = json.get(RESULT);
        if (result == null) {
            return null;
        }
        try {
            return clazz.cast(result);
        } catch (ClassCastException e) {
            throw new JiraJsonClientException("Cannot cast result to " + clazz.getName(), e);
        }
    }

    /**
     * Generates a unique request ID for JSON-RPC correlation.
     */
    protected long createRequestId() {
        return requestIdCounter.incrementAndGet();
    }

    /**
     * Parses the response as JSON and validates the HTTP status code.
     * Checks for JSON-RPC error responses.
     *
     * @param response the HTTP response
     * @param expectedStatus the expected HTTP status code
     * @return the parsed JSON response as a Map
     * @throws JiraJsonClientException on status mismatch, JSON-RPC error, or parse failure
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> responseAsJsonWithCode(ClientResponse response,
            int expectedStatus) throws JiraJsonClientException {
        checkStatus(response, expectedStatus);

        InputStreamReader reader = null;
        try {
            reader = getResponseReader(response);
            Map<String, Object> parsed = MAPPER.readValue(reader, Map.class);

            Object error = parsed.get(ERROR);
            if (error != null) {
                throw new JiraJsonClientException("JSON-RPC error: "
                        + MAPPER.writeValueAsString(error));
            }

            return parsed;
        } catch (JiraJsonClientException e) {
            throw e;
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to parse JSON-RPC response", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * Builds a JSON-RPC 2.0 request object.
     */
    protected RpcRequest createJsonRpcRequest(long id, String method, RpcParam... params) {
        return new RpcRequest(JSON_RPC_2_0, method, params, id);
    }

    /**
     * Serializes and sends a JSON-RPC request.
     */
    protected ClientResponse postRequest(RpcRequest request) throws JiraJsonClientException {
        String requestBody;
        try {
            requestBody = MAPPER.writeValueAsString(request);
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to serialize JSON-RPC request", e);
        }
        return webResource.type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, requestBody);
    }
}
