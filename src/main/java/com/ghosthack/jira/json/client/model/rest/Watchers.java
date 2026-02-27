package com.ghosthack.jira.json.client.model.rest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the watchers response for a JIRA issue.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Watchers {

    private String self;
    private boolean isWatching;
    private int watchCount;
    private List<Watcher> watchers;

    public Watchers() {
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public boolean isWatching() {
        return isWatching;
    }

    public void setWatching(boolean isWatching) {
        this.isWatching = isWatching;
    }

    public int getWatchCount() {
        return watchCount;
    }

    public void setWatchCount(int watchCount) {
        this.watchCount = watchCount;
    }

    public List<Watcher> getWatchers() {
        return watchers;
    }

    public void setWatchers(List<Watcher> watchers) {
        this.watchers = watchers;
    }

    @Override
    public String toString() {
        return "Watchers{watchCount=" + watchCount
                + ", isWatching=" + isWatching + "}";
    }
}
