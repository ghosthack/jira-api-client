package com.ghosthack.jira.json.client;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ghosthack.jira.json.client.model.rest.Issue;
import com.ghosthack.jira.json.client.model.rest.IssueFields;
import com.ghosthack.jira.json.client.model.rest.IssueTypeName;
import com.ghosthack.jira.json.client.model.rest.Project;
import com.ghosthack.jira.json.client.model.rest.ProjectKey;
import com.ghosthack.jira.json.client.model.rest.Watcher;
import com.ghosthack.jira.json.client.model.rest.Watchers;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * JIRA JSON REST API Client.
 *
 * <p>This client communicates with JIRA's REST API to provide programmatic
 * access to projects, issues, watchers, and other resources.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 * JiraJsonRestApiClient client = new JiraJsonRestApiClient("http://localhost:8080", "admin", "admin");
 * List&lt;Project&gt; projects = client.getProjects();
 * Issue issue = client.buildIssue("PROJ", "Bug", "Summary", "Description");
 * client.postIssue(issue);
 * </pre>
 */
public class JiraJsonRestApiClient extends AbstractJiraApiClient {

    protected static final String REST_API_LATEST = "/rest/api/latest";
    protected static final String PROJECT_RESOURCE = "project";
    protected static final String ISSUE_RESOURCE = "issue";

    /** Base resource (without REST API path) for non-API endpoints. */
    protected final WebResource baseResource;

    /**
     * Creates a client with a default Jersey configuration.
     *
     * @param base the JIRA base URL (e.g., "http://localhost:8080")
     */
    public JiraJsonRestApiClient(String base) {
        this(Client.create(), base);
    }

    /**
     * Creates a client with a custom Jersey configuration.
     *
     * @param base the JIRA base URL
     * @param config the Jersey client configuration
     */
    public JiraJsonRestApiClient(String base, ClientConfig config) {
        this(Client.create(config), base);
    }

    /**
     * Creates a client with basic authentication.
     *
     * @param base the JIRA base URL
     * @param user the JIRA username
     * @param pass the JIRA password
     */
    public JiraJsonRestApiClient(String base, String user, String pass) {
        this(base);
        webResource.addFilter(new HTTPBasicAuthFilter(user, pass));
        baseResource.addFilter(new HTTPBasicAuthFilter(user, pass));
    }

    /**
     * Creates a client with custom configuration and basic authentication.
     *
     * @param base the JIRA base URL
     * @param config the Jersey client configuration
     * @param user the JIRA username
     * @param pass the JIRA password
     */
    public JiraJsonRestApiClient(String base, ClientConfig config, String user, String pass) {
        this(base, config);
        webResource.addFilter(new HTTPBasicAuthFilter(user, pass));
        baseResource.addFilter(new HTTPBasicAuthFilter(user, pass));
    }

    /**
     * Creates a client with an existing Jersey Client instance.
     *
     * @param client the Jersey client
     * @param base the JIRA base URL
     */
    protected JiraJsonRestApiClient(Client client, String base) {
        super(client, base, REST_API_LATEST);
        this.baseResource = client.resource(base);
    }

    /**
     * Retrieves the list of watcher names for an issue.
     *
     * @param issueKey the issue key (e.g., "PROJ-123")
     * @return list of watcher usernames
     * @throws JiraJsonClientException on communication or parse failure
     */
    public List<String> getWatchers(String issueKey) throws JiraJsonClientException {
        ClientResponse response = get(ISSUE_RESOURCE + "/" + issueKey + "/watchers");
        checkStatus(response, 200);

        InputStreamReader reader = null;
        try {
            reader = getResponseReader(response);
            Watchers watchers = MAPPER.readValue(reader, Watchers.class);
            List<String> names = new ArrayList<String>();
            if (watchers.getWatchers() != null) {
                for (Watcher w : watchers.getWatchers()) {
                    names.add(w.getName());
                }
            }
            return names;
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to parse watchers response", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * Adds a watcher to an issue.
     *
     * @param issueKey the issue key (e.g., "PROJ-123")
     * @param watcher the username of the watcher to add
     * @throws JiraJsonClientException on communication failure
     */
    public void postWatchers(String issueKey, String watcher) throws JiraJsonClientException {
        String body;
        try {
            body = MAPPER.writeValueAsString(watcher);
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to serialize watcher", e);
        }
        ClientResponse response = post(ISSUE_RESOURCE + "/" + issueKey + "/watchers", body);
        checkStatus(response, 204);
    }

    /**
     * Retrieves all projects visible to the authenticated user.
     *
     * @return list of projects
     * @throws JiraJsonClientException on communication or parse failure
     */
    public List<Project> getProjects() throws JiraJsonClientException {
        ClientResponse response = get(PROJECT_RESOURCE);
        checkStatus(response, 200);

        InputStreamReader reader = null;
        try {
            reader = getResponseReader(response);
            return MAPPER.readValue(reader, new TypeReference<List<Project>>() {});
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to parse projects response", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * Builds an Issue object in memory (does not send to JIRA).
     * Use {@link #postIssue(Issue)} to actually create it.
     *
     * @param project the project key
     * @param type the issue type name (e.g., "Bug", "Task")
     * @param summary the issue summary
     * @param description the issue description
     * @return the constructed Issue
     */
    public Issue buildIssue(String project, String type, String summary, String description) {
        IssueFields fields = new IssueFields(
                new ProjectKey(project),
                summary,
                description,
                new IssueTypeName(type));
        return new Issue(fields);
    }

    /**
     * Creates an issue in JIRA.
     *
     * @param issue the issue to create (use {@link #buildIssue} to construct)
     * @return the created issue response as a Map
     * @throws JiraJsonClientException on communication or serialization failure
     */
    @SuppressWarnings("unchecked")
    public java.util.Map<String, Object> postIssue(Issue issue) throws JiraJsonClientException {
        String request;
        try {
            request = MAPPER.writeValueAsString(issue);
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to serialize issue", e);
        }

        ClientResponse response = post(ISSUE_RESOURCE, request);
        checkStatus(response, 201);

        InputStreamReader reader = null;
        try {
            reader = getResponseReader(response);
            return MAPPER.readValue(reader, java.util.Map.class);
        } catch (Exception e) {
            throw new JiraJsonClientException("Failed to parse issue creation response", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * @deprecated Use {@link #buildIssue(String, String, String, String)} instead.
     */
    @Deprecated
    public Issue createIssue(String project, String type, String summary, String description) {
        return buildIssue(project, type, summary, description);
    }

    /**
     * Returns the base WebResource (without REST API path).
     */
    public WebResource getBaseResource() {
        return baseResource;
    }

    // --- HTTP helper methods ---

    protected ClientResponse get(String resource) {
        return webResource.path(resource).get(ClientResponse.class);
    }

    protected ClientResponse baseGet(String resource) {
        return baseResource.path(resource).get(ClientResponse.class);
    }

    protected ClientResponse post(String resource, Object requestEntity) {
        return webResource.path(resource).type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, requestEntity);
    }

    protected ClientResponse basePost(String resource, Object requestEntity) {
        return baseResource.path(resource).post(ClientResponse.class, requestEntity);
    }
}
