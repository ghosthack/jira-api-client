package com.ghosthack.jira.json.client.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the fields of a JIRA issue for the REST API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueFields {

    private ProjectKey project;
    private String summary;
    private String description;
    private IssueTypeName issuetype;

    public IssueFields() {
    }

    public IssueFields(ProjectKey project, String summary, String description, IssueTypeName issuetype) {
        this.project = project;
        this.summary = summary;
        this.description = description;
        this.issuetype = issuetype;
    }

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

    @Override
    public String toString() {
        return "IssueFields{project=" + project
                + ", summary='" + summary + "'"
                + ", description='" + description + "'"
                + ", issuetype=" + issuetype + "}";
    }
}
