package com.ghosthack.jira.json.client;

import static org.junit.Assert.*;

import com.ghosthack.jira.json.client.model.rest.Issue;
import com.ghosthack.jira.json.client.model.rest.IssueFields;
import org.junit.Test;

/**
 * Unit tests for JiraJsonRestApiClient (non-network tests).
 */
public class JiraJsonRestApiClientTest {

    @Test
    public void testBuildIssueCreatesCorrectStructure() {
        JiraJsonRestApiClient client = new JiraJsonRestApiClient("http://localhost:8080");
        Issue issue = client.buildIssue("PROJ", "Bug", "Test Summary", "Test Description");

        assertNotNull(issue);
        IssueFields fields = issue.getFields();
        assertNotNull(fields);
        assertEquals("PROJ", fields.getProject().getKey());
        assertEquals("Bug", fields.getIssuetype().getName());
        assertEquals("Test Summary", fields.getSummary());
        assertEquals("Test Description", fields.getDescription());
    }

    @Test
    public void testDeprecatedCreateIssueStillWorks() {
        JiraJsonRestApiClient client = new JiraJsonRestApiClient("http://localhost:8080");
        @SuppressWarnings("deprecation")
        Issue issue = client.createIssue("PROJ", "Task", "Summary", "Description");

        assertNotNull(issue);
        assertEquals("PROJ", issue.getFields().getProject().getKey());
        assertEquals("Task", issue.getFields().getIssuetype().getName());
    }

    @Test
    public void testGetWebResource() {
        JiraJsonRestApiClient client = new JiraJsonRestApiClient("http://localhost:8080");
        assertNotNull(client.getWebResource());
    }

    @Test
    public void testGetBaseResource() {
        JiraJsonRestApiClient client = new JiraJsonRestApiClient("http://localhost:8080");
        assertNotNull(client.getBaseResource());
    }

    @Test
    public void testConstructorWithAuth() {
        JiraJsonRestApiClient client = new JiraJsonRestApiClient(
                "http://localhost:8080", "admin", "admin");
        assertNotNull(client.getWebResource());
    }
}
