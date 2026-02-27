package com.ghosthack.jira.json.client.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a JIRA issue type identified by name.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueTypeName {

    private String name;

    public IssueTypeName() {
    }

    public IssueTypeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IssueTypeName{name='" + name + "'}";
    }
}
