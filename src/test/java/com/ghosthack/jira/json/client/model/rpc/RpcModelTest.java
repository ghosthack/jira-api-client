package com.ghosthack.jira.json.client.model.rpc;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class RpcModelTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // --- RpcString ---

    @Test
    public void testRpcStringDefaultConstructor() {
        RpcString rpcString = new RpcString();
        assertNull(rpcString.getValue());
    }

    @Test
    public void testRpcStringValueConstructor() {
        RpcString rpcString = new RpcString("test-value");
        assertEquals("test-value", rpcString.getValue());
    }

    @Test
    public void testRpcStringSetValue() {
        RpcString rpcString = new RpcString();
        rpcString.setValue("new-value");
        assertEquals("new-value", rpcString.getValue());
    }

    @Test
    public void testRpcStringSerializesAsBareValue() throws Exception {
        RpcString rpcString = new RpcString("token123");
        String json = MAPPER.writeValueAsString(rpcString);
        assertEquals("\"token123\"", json);
    }

    @Test
    public void testRpcStringToString() {
        RpcString rpcString = new RpcString("test");
        assertTrue(rpcString.toString().contains("test"));
    }

    @Test
    public void testRpcStringExtendsRpcParam() {
        assertTrue(RpcParam.class.isAssignableFrom(RpcString.class));
    }

    // --- RpcRequest ---

    @Test
    public void testRpcRequestDefaultConstructor() {
        RpcRequest req = new RpcRequest();
        assertNull(req.getJsonrpc());
        assertNull(req.getMethod());
        assertNull(req.getParams());
        assertEquals(0, req.getId());
    }

    @Test
    public void testRpcRequestFullConstructor() {
        RpcParam[] params = new RpcParam[] { new RpcString("p1") };
        RpcRequest req = new RpcRequest("2.0", "login", params, 42);
        assertEquals("2.0", req.getJsonrpc());
        assertEquals("login", req.getMethod());
        assertArrayEquals(params, req.getParams());
        assertEquals(42, req.getId());
    }

    @Test
    public void testRpcRequestSetters() {
        RpcRequest req = new RpcRequest();
        req.setJsonrpc("2.0");
        req.setMethod("getIssue");
        req.setId(7);
        RpcParam[] params = new RpcParam[] {};
        req.setParams(params);

        assertEquals("2.0", req.getJsonrpc());
        assertEquals("getIssue", req.getMethod());
        assertEquals(7, req.getId());
        assertArrayEquals(params, req.getParams());
    }

    @Test
    public void testRpcRequestSerialization() throws Exception {
        RpcRequest req = new RpcRequest("2.0", "login",
                new RpcParam[] { new RpcString("admin"), new RpcString("pass") }, 1);
        String json = MAPPER.writeValueAsString(req);
        assertTrue(json.contains("\"jsonrpc\":\"2.0\""));
        assertTrue(json.contains("\"method\":\"login\""));
        assertTrue(json.contains("\"id\":1"));
        assertTrue(json.contains("\"admin\""));
        assertTrue(json.contains("\"pass\""));
    }

    @Test
    public void testRpcRequestToString() {
        RpcRequest req = new RpcRequest("2.0", "login", null, 5);
        String str = req.toString();
        assertTrue(str.contains("2.0"));
        assertTrue(str.contains("login"));
        assertTrue(str.contains("5"));
    }

    // --- Issue ---

    @Test
    public void testIssueDefaultConstructor() {
        Issue issue = new Issue();
        assertNull(issue.getDescription());
        assertNull(issue.getProject());
        assertNull(issue.getSummary());
        assertEquals(0, issue.getType());
        assertNull(issue.getDuedate());
    }

    @Test
    public void testIssueBasicConstructor() {
        Issue issue = new Issue("PROJ", 1, "Summary", "Description");
        assertEquals("PROJ", issue.getProject());
        assertEquals(1, issue.getType());
        assertEquals("Summary", issue.getSummary());
        assertEquals("Description", issue.getDescription());
        assertNull(issue.getDuedate());
    }

    @Test
    public void testIssueFullConstructor() {
        Issue issue = new Issue("PROJ", 2, "Summary", "Desc", "2026-03-01");
        assertEquals("PROJ", issue.getProject());
        assertEquals(2, issue.getType());
        assertEquals("Summary", issue.getSummary());
        assertEquals("Desc", issue.getDescription());
        assertEquals("2026-03-01", issue.getDuedate());
    }

    @Test
    public void testIssueSetters() {
        Issue issue = new Issue();
        issue.setProject("TEST");
        issue.setType(3);
        issue.setSummary("Test summary");
        issue.setDescription("Test desc");
        issue.setDuedate("2026-12-31");

        assertEquals("TEST", issue.getProject());
        assertEquals(3, issue.getType());
        assertEquals("Test summary", issue.getSummary());
        assertEquals("Test desc", issue.getDescription());
        assertEquals("2026-12-31", issue.getDuedate());
    }

    @Test
    public void testIssueDuedateSerialization() throws Exception {
        Issue issue = new Issue("PROJ", 1, "Sum", "Desc", "2026-06-15");
        String json = MAPPER.writeValueAsString(issue);
        assertTrue(json.contains("\"duedate\":\"2026-06-15\""));
    }

    @Test
    public void testIssueExtendsRpcParam() {
        assertTrue(RpcParam.class.isAssignableFrom(Issue.class));
    }

    @Test
    public void testIssueToString() {
        Issue issue = new Issue("PROJ", 1, "Summary", "Desc");
        String str = issue.toString();
        assertTrue(str.contains("PROJ"));
        assertTrue(str.contains("Summary"));
    }
}
