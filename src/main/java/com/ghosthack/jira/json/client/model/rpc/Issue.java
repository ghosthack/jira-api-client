package com.ghosthack.jira.json.client.model.rpc;

public class Issue extends RpcParam {

    protected String description;
    protected String project;
    protected String summary;
    protected int type;
    protected String duedate;

    public Issue() {
        // TODO Auto-generated constructor stub
    }

    public Issue(String project, int type, String summary, String description) {
        super();
        this.project = project;
        this.type = type;
        this.summary = summary;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
