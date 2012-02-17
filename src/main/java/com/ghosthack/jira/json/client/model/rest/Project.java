package com.ghosthack.jira.json.client.model.rest;

public class Project {

    protected String self;
    protected String key;
    protected String name;
    protected Object roles;

    public Object getRoles() {
        return roles;
    }
    public void setRoles(Object roles) {
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

}
