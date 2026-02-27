package com.ghosthack.jira.json.client.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a JIRA issue for the REST API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private IssueFields fields;

    public Issue() {
    }

    public Issue(IssueFields fields) {
        this.fields = fields;
    }

    public IssueFields getFields() {
        return fields;
    }

    public void setFields(IssueFields fields) {
        this.fields = fields;
    }

    @Override
    public String toString() {
        return "Issue{fields=" + fields + "}";
    }
}
