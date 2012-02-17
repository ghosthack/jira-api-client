package com.ghosthack.jira.json.client;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ghosthack.jira.json.client.model.rpc.Issue;
import com.ghosthack.jira.json.client.model.rpc.RpcParam;
import com.ghosthack.jira.json.client.model.rpc.RpcRequest;
import com.ghosthack.jira.json.client.model.rpc.RpcString;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * JIRA API JSON-RPC Client for the transition between 4.x to 5.x.
 */
public class JiraJsonRpcApiClient {

    private static final String GET_ISSUE = "getIssue";
    private static final String CREATE_ISSUE = "createIssue";
    protected static final String ERROR = "error";
    protected static final String RPC_JSON_RPC_JIRASOAPSERVICE_V2 = "/rpc/json-rpc/jirasoapservice-v2";
    protected static final String JSON_RPC_2_0 = "2.0";
    protected static final String RESULT = "result";
    protected static final String LOGIN = "login";

    /**
     * Queries an issue.
     * 
     * @param token
     * @param issueKey
     *            ex.: CM-123
     * @return Enforces a JSONObject type
     * @throws JiraJsonClientException
     *             Catches/wraps CCE, parse exceptions, IO and error codes.
     */
    public JSONObject getIssue(String token, String issueKey)
            throws JiraJsonClientException {
        return post(JSONObject.class, GET_ISSUE, new RpcString(token),
                new RpcString(issueKey));
    }

    /**
     * Obtains a security token.
     * 
     * @param user
     * @param pass
     * @return Enforces a String type
     * @throws JiraJsonClientException
     *             Catches/wraps CCE, parse exceptions, IO and error codes.
     */
    public String login(String user, String pass) throws JiraJsonClientException {
        return post(String.class, LOGIN, new RpcString(user), new RpcString(
                pass));
    }

    /**
     * Creates an issue.
     * 
     * @param token
     *            security token from login
     * @param issue
     * @return Enforces a JSONObject type
     * @throws JiraJsonClientException
     *             Catches/wraps CCE, parse exceptions, IO and error codes.
     */
    public JSONObject createIssue(String token, Issue issue)
            throws JiraJsonClientException {
        return post(JSONObject.class, CREATE_ISSUE, new RpcString(token), issue);
    }

    protected <T> T post(Class<T> clazz, String method, RpcParam... params)
            throws JiraJsonClientException {
        return post(clazz, createRequestId(), 200, method, params);
    }

    protected <T> T post(Class<T> clazz, long id, int expectedStatus,
            String method, RpcParam... params) throws JiraJsonClientException {
        RpcRequest req = createJsonRpcRequest(id, method, params);
        ClientResponse res = postRequest(req);
        JSONObject json = reponseAsJsonWithCode(res, expectedStatus);
        try {
            return clazz.cast(json.get(RESULT));
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
    }

    protected long createRequestId() {
        return 0;
    }

    protected JSONObject reponseAsJsonWithCode(ClientResponse response,
            int expectedStatus) throws JiraJsonClientException {
        InputStream is = response.getEntityInputStream();
        InputStreamReader isr = new InputStreamReader(is, utf8Charset);
        JSONObject parse;
        try {
            parse = (JSONObject) JSONValue.parseWithException(isr);
            JSONObject json = (JSONObject) parse.get(ERROR);
            if (json != null) {
                throw new JiraJsonClientException(json.toJSONString());
            }
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
        if (response.getStatus() == expectedStatus) {
            return parse;
        }
        throw new JiraJsonClientException("response.status != " + expectedStatus
                + ", " + response);
    }

    protected RpcRequest createJsonRpcRequest(long id, String method,
            RpcParam... params) {
        RpcRequest req = new RpcRequest();
        req.setJsonrpc(JSON_RPC_2_0);
        req.setId(id);
        req.setMethod(method);
        req.setParams(params);
        return req;
    }

    protected ClientResponse postRequest(RpcRequest jiraJsonRpcRequest)
            throws JiraJsonClientException {
        Object requestEntity;
        try {
            requestEntity = mapper.writeValueAsString(jiraJsonRpcRequest);
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
        return wr.type(MediaType.APPLICATION_JSON_TYPE).post(
                ClientResponse.class, requestEntity);
    }

    protected JiraJsonRpcApiClient(Client client, String base) {
        wr = client.resource(base).path(RPC_JSON_RPC_JIRASOAPSERVICE_V2);
        wr.addFilter(new LoggingFilter());
    }

    public JiraJsonRpcApiClient(String base) {
        this(Client.create(), base);
    }

    public JiraJsonRpcApiClient(String base, ClientConfig config) {
        this(Client.create(config), base);
    }

    protected WebResource wr;

    // Reusable Jackson Mapper
    protected final ObjectMapper mapper = new ObjectMapper();

    // Always assume UTF8
    protected static final Charset utf8Charset = Charset.forName("UTF-8");

}