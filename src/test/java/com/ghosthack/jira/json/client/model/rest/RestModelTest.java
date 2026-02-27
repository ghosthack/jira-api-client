package com.ghosthack.jira.json.client.model.rest;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class RestModelTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // --- Issue ---

    @Test
    public void testIssueDefaultConstructor() {
        Issue issue = new Issue();
        assertNull(issue.getFields());
    }

    @Test
    public void testIssueFieldsConstructor() {
        IssueFields fields = new IssueFields();
        Issue issue = new Issue(fields);
        assertSame(fields, issue.getFields());
    }

    @Test
    public void testIssueSerialization() throws Exception {
        IssueFields fields = new IssueFields(
                new ProjectKey("TEST"),
                "Test Summary",
                "Test Description",
                new IssueTypeName("Bug"));
        Issue issue = new Issue(fields);

        String json = MAPPER.writeValueAsString(issue);
        assertTrue(json.contains("\"summary\":\"Test Summary\""));
        assertTrue(json.contains("\"key\":\"TEST\""));
        assertTrue(json.contains("\"name\":\"Bug\""));
    }

    @Test
    public void testIssueDeserialization() throws Exception {
        String json = "{\"fields\":{\"summary\":\"My Issue\",\"description\":\"Desc\"}}";
        Issue issue = MAPPER.readValue(json, Issue.class);
        assertNotNull(issue.getFields());
        assertEquals("My Issue", issue.getFields().getSummary());
        assertEquals("Desc", issue.getFields().getDescription());
    }

    @Test
    public void testIssueIgnoresUnknownFields() throws Exception {
        String json = "{\"fields\":{\"summary\":\"Test\"},\"unknown_field\":123}";
        Issue issue = MAPPER.readValue(json, Issue.class);
        assertEquals("Test", issue.getFields().getSummary());
    }

    @Test
    public void testIssueToString() {
        Issue issue = new Issue(new IssueFields());
        assertTrue(issue.toString().contains("fields="));
    }

    // --- IssueFields ---

    @Test
    public void testIssueFieldsDefaultConstructor() {
        IssueFields fields = new IssueFields();
        assertNull(fields.getProject());
        assertNull(fields.getSummary());
        assertNull(fields.getDescription());
        assertNull(fields.getIssuetype());
    }

    @Test
    public void testIssueFieldsFullConstructor() {
        ProjectKey pk = new ProjectKey("PROJ");
        IssueTypeName itn = new IssueTypeName("Task");
        IssueFields fields = new IssueFields(pk, "Sum", "Desc", itn);

        assertSame(pk, fields.getProject());
        assertEquals("Sum", fields.getSummary());
        assertEquals("Desc", fields.getDescription());
        assertSame(itn, fields.getIssuetype());
    }

    @Test
    public void testIssueFieldsSetters() {
        IssueFields fields = new IssueFields();
        ProjectKey pk = new ProjectKey("KEY");
        IssueTypeName itn = new IssueTypeName("Story");

        fields.setProject(pk);
        fields.setSummary("Summary");
        fields.setDescription("Description");
        fields.setIssuetype(itn);

        assertSame(pk, fields.getProject());
        assertEquals("Summary", fields.getSummary());
        assertEquals("Description", fields.getDescription());
        assertSame(itn, fields.getIssuetype());
    }

    // --- IssueTypeName ---

    @Test
    public void testIssueTypeNameConstructors() {
        IssueTypeName empty = new IssueTypeName();
        assertNull(empty.getName());

        IssueTypeName named = new IssueTypeName("Epic");
        assertEquals("Epic", named.getName());
    }

    @Test
    public void testIssueTypeNameSerialization() throws Exception {
        String json = MAPPER.writeValueAsString(new IssueTypeName("Bug"));
        assertEquals("{\"name\":\"Bug\"}", json);
    }

    // --- Login ---

    @Test
    public void testLoginDefaultConstructor() {
        Login login = new Login();
        assertNull(login.getUsername());
        assertNull(login.getPassword());
    }

    @Test
    public void testLoginFullConstructor() {
        Login login = new Login("admin", "secret");
        assertEquals("admin", login.getUsername());
        assertEquals("secret", login.getPassword());
    }

    @Test
    public void testLoginToStringDoesNotLeakPassword() {
        Login login = new Login("admin", "secret");
        String str = login.toString();
        assertTrue(str.contains("admin"));
        assertFalse(str.contains("secret"));
    }

    @Test
    public void testLoginSerialization() throws Exception {
        Login login = new Login("user", "pass");
        String json = MAPPER.writeValueAsString(login);
        assertTrue(json.contains("\"username\":\"user\""));
        assertTrue(json.contains("\"password\":\"pass\""));
    }

    // --- Project ---

    @Test
    public void testProjectGettersSetters() {
        Project project = new Project();
        project.setSelf("https://jira.example.com/rest/api/2/project/10000");
        project.setKey("PROJ");
        project.setName("My Project");
        Map<String, String> roles = new HashMap<String, String>();
        roles.put("admin", "https://jira.example.com/rest/api/2/project/10000/role/10001");
        project.setRoles(roles);

        assertEquals("https://jira.example.com/rest/api/2/project/10000", project.getSelf());
        assertEquals("PROJ", project.getKey());
        assertEquals("My Project", project.getName());
        assertEquals(roles, project.getRoles());
    }

    @Test
    public void testProjectDeserialization() throws Exception {
        String json = "{\"self\":\"https://jira.example.com\",\"key\":\"TEST\",\"name\":\"Test Project\"}";
        Project project = MAPPER.readValue(json, Project.class);
        assertEquals("TEST", project.getKey());
        assertEquals("Test Project", project.getName());
    }

    @Test
    public void testProjectIgnoresUnknownFields() throws Exception {
        String json = "{\"key\":\"TEST\",\"name\":\"Test\",\"avatarUrls\":{\"16x16\":\"url\"}}";
        Project project = MAPPER.readValue(json, Project.class);
        assertEquals("TEST", project.getKey());
    }

    @Test
    public void testProjectToString() {
        Project project = new Project();
        project.setKey("PROJ");
        project.setName("Name");
        String str = project.toString();
        assertTrue(str.contains("PROJ"));
        assertTrue(str.contains("Name"));
    }

    // --- ProjectKey ---

    @Test
    public void testProjectKeyConstructors() {
        ProjectKey empty = new ProjectKey();
        assertNull(empty.getKey());

        ProjectKey keyed = new ProjectKey("PROJ");
        assertEquals("PROJ", keyed.getKey());
    }

    @Test
    public void testProjectKeySerialization() throws Exception {
        String json = MAPPER.writeValueAsString(new ProjectKey("TEST"));
        assertEquals("{\"key\":\"TEST\"}", json);
    }

    // --- Watcher ---

    @Test
    public void testWatcherGettersSetters() {
        Watcher watcher = new Watcher();
        watcher.setSelf("https://jira.example.com/rest/api/2/user?username=admin");
        watcher.setName("admin");
        watcher.setDisplayName("Admin User");
        watcher.setActive(true);

        assertEquals("https://jira.example.com/rest/api/2/user?username=admin", watcher.getSelf());
        assertEquals("admin", watcher.getName());
        assertEquals("Admin User", watcher.getDisplayName());
        assertTrue(watcher.isActive());
    }

    @Test
    public void testWatcherDeserialization() throws Exception {
        String json = "{\"name\":\"jdoe\",\"displayName\":\"John Doe\",\"active\":true}";
        Watcher watcher = MAPPER.readValue(json, Watcher.class);
        assertEquals("jdoe", watcher.getName());
        assertEquals("John Doe", watcher.getDisplayName());
        assertTrue(watcher.isActive());
    }

    @Test
    public void testWatcherToString() {
        Watcher watcher = new Watcher();
        watcher.setName("admin");
        assertTrue(watcher.toString().contains("admin"));
    }

    // --- Watchers ---

    @Test
    public void testWatchersGettersSetters() {
        Watchers watchers = new Watchers();
        watchers.setSelf("https://jira.example.com/rest/api/2/issue/TEST-1/watchers");
        watchers.setWatching(true);
        watchers.setWatchCount(3);

        Watcher w = new Watcher();
        w.setName("user1");
        List<Watcher> list = Arrays.asList(w);
        watchers.setWatchers(list);

        assertTrue(watchers.isWatching());
        assertEquals(3, watchers.getWatchCount());
        assertEquals(1, watchers.getWatchers().size());
        assertEquals("user1", watchers.getWatchers().get(0).getName());
    }

    @Test
    public void testWatchersDeserialization() throws Exception {
        String json = "{\"self\":\"url\",\"isWatching\":false,\"watchCount\":2,"
                + "\"watchers\":[{\"name\":\"a\"},{\"name\":\"b\"}]}";
        Watchers watchers = MAPPER.readValue(json, Watchers.class);
        assertFalse(watchers.isWatching());
        assertEquals(2, watchers.getWatchCount());
        assertEquals(2, watchers.getWatchers().size());
    }

    @Test
    public void testWatchersToString() {
        Watchers watchers = new Watchers();
        watchers.setWatchCount(5);
        assertTrue(watchers.toString().contains("5"));
    }
}
