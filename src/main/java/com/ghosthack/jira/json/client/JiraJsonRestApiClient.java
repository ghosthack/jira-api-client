package com.ghosthack.jira.json.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.ghosthack.jira.json.client.model.rest.Issue;
import com.ghosthack.jira.json.client.model.rest.IssueFields;
import com.ghosthack.jira.json.client.model.rest.IssueTypeName;
import com.ghosthack.jira.json.client.model.rest.Project;
import com.ghosthack.jira.json.client.model.rest.ProjectKey;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;

/**
 * JIRA JSON REST API Client.
 */
public class JiraJsonRestApiClient {

    protected static final String REST_API_LATEST = "/rest/api/latest";
    protected static final String PROJECT_RESOURCE = "project";
    protected static final String ISSUE_RESOURCE = "issue";
    protected WebResource br;

    public List<String> getWatchers(String issueKey)
            throws JiraJsonClientException {
        ClientResponse response = get("/issue/" + issueKey + "/watchers");
        checkStatus(response, 200);
        InputStream is = response.getEntityInputStream();
        // InputStreamReader isr =
        new InputStreamReader(is, utf8Charset);
        try {
            // return readWatchers(isr);
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
        return null;
    }

    public void postWatchers(String issueKey, String watcher)
            throws JiraJsonClientException {
        ClientResponse response;
        try {
            response = post("/issue/" + issueKey + "/watchers",
                    mapper.writeValueAsString(watcher));
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
        checkStatus(response, 204);
    }

    public List<Project> getProjects() throws JiraJsonClientException {
        ClientResponse response = get(PROJECT_RESOURCE);
        checkStatus(response, 200);
        InputStream is = response.getEntityInputStream();
        InputStreamReader isr = new InputStreamReader(is, utf8Charset);
        try {
            return readProject(isr);
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
    }

    public Issue createIssue(String project, String type, String summary,
            String description) {
        IssueTypeName issuetype = new IssueTypeName();
        issuetype.setName(type);
        ProjectKey projectKey = new ProjectKey();
        projectKey.setKey(project);
        Issue issue = new Issue();
        IssueFields fields = new IssueFields();
        fields.setDescription(description);
        fields.setIssuetype(issuetype);
        fields.setProject(projectKey);
        fields.setSummary(summary);
        issue.setFields(fields);
        return issue;
    }

    public void postIssue(Issue issue) throws JiraJsonClientException {
        String request;
        try {
            request = mapper.writeValueAsString(issue);
        } catch (Exception e) {
            throw new JiraJsonClientException(e);
        }
        // ClientResponse response =
        post(ISSUE_RESOURCE, request);
    }

    protected JiraJsonRestApiClient(Client client, String base) {
        br = client.resource(base);
        br.addFilter(new LoggingFilter());
        wr = br.path(REST_API_LATEST);
    }

    public JiraJsonRestApiClient(String base) {
        this(Client.create(), base);
    }

    public JiraJsonRestApiClient(String base, ClientConfig config) {
        this(Client.create(config), base);
    }

    public JiraJsonRestApiClient(String base, String user, String pass) {
        this(base);
        wr.addFilter(new HTTPBasicAuthFilter(user, pass));
    }

    public JiraJsonRestApiClient(String base, ClientConfig config, String user,
            String pass) {
        this(base, config);
        wr.addFilter(new HTTPBasicAuthFilter(user, pass));
    }

    protected ClientResponse get(String resource) {
        return wr.path(resource).get(ClientResponse.class);
    }

    protected ClientResponse baseGet(String resource) {
        return br.path(resource).get(ClientResponse.class);
    }

    protected ClientResponse post(String resource, Object requestEntity) {
        return wr.path(resource).type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, requestEntity);
    }

    protected ClientResponse basePost(String resource, Object requestEntity) {
        return br.path(resource).post(ClientResponse.class, requestEntity);
    }

    protected WebResource wr;

    protected void checkStatus(ClientResponse response, int checkStatus)
            throws JiraJsonClientException {
        int status = response.getStatus();
        if (status != checkStatus)
            throw JiraJsonClientException.createResponseStatusException(status);
    }

    protected List<Project> readProject(InputStreamReader isr)
            throws IOException, JsonParseException, JsonMappingException {
        return mapper.readValue(isr, new TypeReference<List<Project>>() {
        });
    }

    public WebResource getWebResource() {
        return wr;
    }

    protected static final Charset utf8Charset = Charset.forName("UTF-8");

    // Reusable Jackson Mapper
    protected static final ObjectMapper mapper = new ObjectMapper();

}