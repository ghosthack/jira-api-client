package com.ghosthack.jira.json.client;

import static org.junit.Assert.*;
import org.junit.Test;

public class JiraJsonClientExceptionTest {

    @Test
    public void testMessageConstructor() {
        JiraJsonClientException ex = new JiraJsonClientException("test error");
        assertEquals("test error", ex.getMessage());
        assertEquals(-1, ex.getStatusCode());
    }

    @Test
    public void testCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        JiraJsonClientException ex = new JiraJsonClientException(cause);
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("root cause");
        JiraJsonClientException ex = new JiraJsonClientException("wrapper", cause);
        assertEquals("wrapper", ex.getMessage());
        assertEquals(cause, ex.getCause());
        assertEquals(-1, ex.getStatusCode());
    }

    @Test
    public void testMessageAndStatusCodeConstructor() {
        JiraJsonClientException ex = new JiraJsonClientException("not found", 404);
        assertEquals("not found", ex.getMessage());
        assertEquals(404, ex.getStatusCode());
    }

    @Test
    public void testCreateResponseStatusException() {
        JiraJsonClientException ex = JiraJsonClientException.createResponseStatusException(401);
        assertTrue(ex.getMessage().contains("401"));
        assertEquals(401, ex.getStatusCode());
    }

    @Test
    public void testCreateResponseStatusExceptionWithExpected() {
        JiraJsonClientException ex = JiraJsonClientException.createResponseStatusException(200, 500);
        assertTrue(ex.getMessage().contains("200"));
        assertTrue(ex.getMessage().contains("500"));
        assertEquals(500, ex.getStatusCode());
    }

    @Test
    public void testToStringWithStatusCode() {
        JiraJsonClientException ex = new JiraJsonClientException("error", 503);
        String str = ex.toString();
        assertTrue(str.contains("503"));
        assertTrue(str.contains("error"));
    }

    @Test
    public void testToStringWithoutStatusCode() {
        JiraJsonClientException ex = new JiraJsonClientException("plain error");
        String str = ex.toString();
        assertTrue(str.contains("plain error"));
    }

    @Test
    public void testIsCheckedException() {
        assertTrue(Exception.class.isAssignableFrom(JiraJsonClientException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(JiraJsonClientException.class));
    }
}
