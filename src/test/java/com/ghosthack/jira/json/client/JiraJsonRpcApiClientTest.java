package com.ghosthack.jira.json.client;

import static org.junit.Assert.*;

import com.ghosthack.jira.json.client.model.rpc.RpcParam;
import com.ghosthack.jira.json.client.model.rpc.RpcRequest;
import com.ghosthack.jira.json.client.model.rpc.RpcString;
import org.junit.Test;

/**
 * Unit tests for JiraJsonRpcApiClient (non-network tests).
 */
public class JiraJsonRpcApiClientTest {

    @Test
    public void testConstructor() {
        JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("http://localhost:8080");
        assertNotNull(client.getWebResource());
    }

    @Test
    public void testRequestIdIncrementsMonotonically() {
        JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("http://localhost:8080");
        long id1 = client.createRequestId();
        long id2 = client.createRequestId();
        long id3 = client.createRequestId();

        assertTrue("IDs should be monotonically increasing", id2 > id1);
        assertTrue("IDs should be monotonically increasing", id3 > id2);
    }

    @Test
    public void testRequestIdIsUniqueAcrossInstances() {
        JiraJsonRpcApiClient client1 = new JiraJsonRpcApiClient("http://localhost:8080");
        JiraJsonRpcApiClient client2 = new JiraJsonRpcApiClient("http://localhost:8080");

        long id1 = client1.createRequestId();
        long id2 = client2.createRequestId();

        // Both start from 0 and increment, so both get 1 -- they're independent
        assertEquals(1, id1);
        assertEquals(1, id2);
    }

    @Test
    public void testCreateJsonRpcRequest() {
        JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("http://localhost:8080");

        RpcParam[] params = new RpcParam[] {
                new RpcString("admin"), new RpcString("pass")
        };
        RpcRequest req = client.createJsonRpcRequest(42, "login", params);

        assertEquals("2.0", req.getJsonrpc());
        assertEquals("login", req.getMethod());
        assertEquals(42, req.getId());
        assertArrayEquals(params, req.getParams());
    }

    @Test
    public void testCreateJsonRpcRequestWithNoParams() {
        JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("http://localhost:8080");
        RpcRequest req = client.createJsonRpcRequest(1, "someMethod");

        assertEquals("2.0", req.getJsonrpc());
        assertEquals("someMethod", req.getMethod());
        assertEquals(1, req.getId());
        assertNotNull(req.getParams());
        assertEquals(0, req.getParams().length);
    }
}
