# JIRA JSON API Client

A lightweight Java client library for the JIRA JSON API, supporting both **REST** and **JSON-RPC 2.0** protocols.

The JSON-RPC client covers the transition period between JIRA 4.x and 5.x. The REST client targets the modern JIRA REST API (`/rest/api/latest`).

## Why?

Available Java options at the time didn't work out of the box, or their documentation was outdated and hard to find. The official `atlassian-jira-rest-java-client` required extra `keytool` work for SSL and didn't expose the underlying Jersey configuration.

This library gives you direct, simple access to JIRA's JSON APIs with full control over the Jersey client configuration.

## Requirements

- Java 8+
- Maven 3.x

## Installation

Add the dependency to your `pom.xml`:

```xml
<dependency>
  <groupId>com.ghosthack</groupId>
  <artifactId>jira-json-client</artifactId>
  <version>2.0.0</version>
</dependency>
```

Or build from source:

```bash
git clone https://github.com/ghosthack/jira-api-client.git
cd jira-api-client
mvn clean package
```

## Usage

### REST API Client

```java
// Basic authentication
JiraJsonRestApiClient client = new JiraJsonRestApiClient(
    "https://jira.example.com", "username", "password");

// List all projects
List<Project> projects = client.getProjects();

// Create and post an issue
Issue issue = client.buildIssue("PROJ", "Bug", "Login broken", "Cannot log in with SSO");
Map<String, Object> response = client.postIssue(issue);

// Get watchers for an issue
List<String> watchers = client.getWatchers("PROJ-123");

// Add a watcher
client.postWatchers("PROJ-123", "jdoe");
```

### JSON-RPC Client

```java
// Create client (optionally pass a Jersey ClientConfig)
JiraJsonRpcApiClient client = new JiraJsonRpcApiClient("https://jira.example.com");

// Authenticate
String token = client.login("admin", "password");

// Get an issue
Map<String, Object> issue = client.getIssue(token, "PROJ-123");

// Create an issue
Issue newIssue = new Issue("PROJ", 1, "Summary", "Description");
Map<String, Object> created = client.createIssue(token, newIssue);
```

### Custom Jersey Configuration

Both clients accept a Jersey `ClientConfig` for advanced configuration:

```java
ClientConfig config = new DefaultClientConfig();
// customize config...

JiraJsonRestApiClient client = new JiraJsonRestApiClient(
    "https://jira.example.com", config, "user", "pass");
```

### SSL Certificate Bypass (Development Only)

For development environments with self-signed certificates:

```java
import com.ghosthack.jira.json.client.util.JerseyCertificateIgnoreBlackMagic;

ClientConfig config = JerseyCertificateIgnoreBlackMagic.ignoreCertConfig();
JiraJsonRestApiClient client = new JiraJsonRestApiClient(
    "https://localhost:8443", config, "admin", "admin");
```

> **Warning:** This disables all SSL certificate validation. Never use in production.

## Project Structure

```
src/main/java/com/ghosthack/jira/json/client/
  AbstractJiraApiClient.java          -- Shared base class
  JiraJsonRestApiClient.java          -- REST API client
  JiraJsonRpcApiClient.java           -- JSON-RPC 2.0 client
  JiraJsonClientException.java        -- Client exception type
  model/
    rest/                             -- REST API models
      Issue.java, IssueFields.java, IssueTypeName.java,
      Login.java, Project.java, ProjectKey.java,
      Watcher.java, Watchers.java
    rpc/                              -- JSON-RPC models
      Issue.java, RpcParam.java, RpcRequest.java, RpcString.java
  util/
    CertificateIgnoreBlackMagic.java  -- SSL bypass utility
    JerseyCertificateIgnoreBlackMagic.java -- Jersey SSL config
```

## Building and Testing

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Package as JAR
mvn package

# Package as fat JAR with dependencies
mvn assembly:single
```

## Dependencies

| Dependency | Version | Purpose |
|---|---|---|
| Jersey Client | 1.19.4 | HTTP/REST transport |
| Jersey Apache Client | 1.19.4 | Apache HTTP connector |
| Jackson Databind | 2.15.3 | JSON serialization |
| Jackson Annotations | 2.15.3 | JSON annotations |

Test dependencies: JUnit 4.13.2, Mockito 4.11.0

## References

- [JIRA REST API Documentation](https://docs.atlassian.com/jira/REST/latest/)
- [JIRA JSON-RPC Overview](https://developer.atlassian.com/display/JIRADEV/JIRA+JSON-RPC+Overview)
- [Connecting to Services via SSL](https://confluence.atlassian.com/display/DOC/Connecting+to+LDAP+or+JIRA+or+Other+Services+via+SSL)
- [JIRA REST OAuth Configuration](https://confluence.atlassian.com/display/JIRA/Configuring+OAuth+Authentication+for+an+Application+Link)
- [Jersey OAuth](https://wikis.oracle.com/display/Jersey/OAuth)

## License

See repository for license details.
