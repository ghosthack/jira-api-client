package com.ghosthack.jira.json.client.model.rest;

public class IssueFields {

    protected ProjectKey project;
    // TODO: limit size
    protected String summary;
    // TODO: limit size
    protected String description;
    protected IssueTypeName issuetype;

    public ProjectKey getProject() {
        return project;
    }

    public void setProject(ProjectKey project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueTypeName getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(IssueTypeName issuetype) {
        this.issuetype = issuetype;
    }
}
