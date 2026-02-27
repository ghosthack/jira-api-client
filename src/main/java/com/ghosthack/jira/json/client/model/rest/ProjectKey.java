package com.ghosthack.jira.json.client.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a JIRA project key reference.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectKey {

    private String key;

    public ProjectKey() {
    }

    public ProjectKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "ProjectKey{key='" + key + "'}";
    }
}
