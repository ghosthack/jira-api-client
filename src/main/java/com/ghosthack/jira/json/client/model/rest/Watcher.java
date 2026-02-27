package com.ghosthack.jira.json.client.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents a JIRA issue watcher.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Watcher {

    private String self;
    private String name;
    private String displayName;
    private boolean active;

    public Watcher() {
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Watcher{name='" + name + "', displayName='" + displayName
                + "', active=" + active + "}";
    }
}
