package com.ghosthack.jira.json.client.model.rest;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a JIRA project for the REST API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    private String self;
    private String key;
    private String name;
    private Map<String, String> roles;

    public Project() {
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, String> roles) {
        this.roles = roles;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Project{key='" + key + "', name='" + name + "'}";
    }
}
