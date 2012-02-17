package com.ghosthack.jira.json.client.model.rest;

import java.util.List;

public class Watchers {

    public String self;
    public boolean isWatching;
    public int watchCount;
    public List<Watcher> watchers;

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

}
