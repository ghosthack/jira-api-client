JIRA JSON API Client (ReST and RPC)
-----------------------------------

* JIRA API JSON-RPC Client for the transition between 4.x to 5.x.
* JIRA API JSON REST Client.

Why?

Available options in Java didn't work out of the box or documentation was outdated / too hard to find.

Some extra work with keytool [1] was required with atlassian-jira-rest-java-client. Also Jersey Config wasn't exposed on the Jersey implementation.


Using
-----

JSON-RPC:

    // instantiate (optionally pass a Jersey Config)
    client = new JiraJsonRpcApiClient("http://localhost:8080");
    // login
    token = client.login("admin", "admin");
    // create an issue
    client.createIssue(token, new Issue());

JSON ReST:

    // instantiate (optionals Jersey Config, user/pass, etc.)
    client = new JiraJsonRestApiClient("http://localhost:8080");
    List<Project> projects = client.getProjects();
    

Documentation was scattered all over the internets
--------------------------------------------------

[1] SSL JIRA:

http://confluence.atlassian.com/display/DOC/Connecting+to+LDAP+or+JIRA+or+Other+Services+via+SSL

JIRA REST:

http://docs.atlassian.com/jira/REST/latest/#id2474343

JIRA RPC:

https://developer.atlassian.com/display/JIRADEV/JIRA+JSON-RPC+Overview
http://confluence.atlassian.com/display/JIRA043/Creating+a+XML-RPC+Client

JIRA Issues

http://confluence.atlassian.com/display/JIRACOM/Creating+Jira+Issues+Remotely

JRJC jira-rest-java-client:

https://plugins.atlassian.com/plugin/details/39474
https://studio.atlassian.com/svn/JRJC/trunk/atlassian-jira-rest-java-client/

JIRA REST OAuth (consumers setup)

http://confluence.atlassian.com/display/JIRA042/Configuring+OAuth+Consumers
http://confluence.atlassian.com/display/JIRA042/Configuring+OAuth
http://confluence.atlassian.com/display/JIRA/Configuring+OAuth+Authentication+for+an+Application+Link

Jersey OAuth:

https://wikis.oracle.com/display/Jersey/OAuth

JIRA SOAP

http://confluence.atlassian.com/display/JIRA043/Creating+a+SOAP+Client


SOAP client generation plugin for Maven
---------------------------------------

If JSON clients fail, fall back to SOAP:

Extra dependencies:

    <dependency>
      <groupId>axis</groupId>
      <artifactId>axis-jaxrpc</artifactId>
      <version>1.3</version>
    </dependency>
    <dependency>
      <groupId>axis</groupId>
      <artifactId>axis-saaj</artifactId>
      <version>1.3</version>
    </dependency>
    <dependency>
      <groupId>axis</groupId>
      <artifactId>axis</artifactId>
      <version>1.3</version>
    </dependency>

Plugin block:

      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>axistools-maven-plugin</artifactId>
        <version>1.3</version>
        <configuration>
          <urls>
            <url>http://localhost:8080/rpc/soap/jirasoapservice-v2?wsdl</url>
          </urls>
          <packageSpace>com.ghosthack.jira.soap.client</packageSpace>
        </configuration>
        <executions>
          <execution>
            <goals>
              <goal>wsdl2java</goal>
            </goals>
          </execution>
        </executions>
      </plugin>

