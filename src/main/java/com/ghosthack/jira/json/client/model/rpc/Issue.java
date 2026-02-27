package com.ghosthack.jira.json.client.model.rpc;

/**
 * Represents a JIRA issue for the JSON-RPC API.
 */
public class Issue extends RpcParam {

    private String description;
    private String project;
    private String summary;
    private int type;
    private String duedate;

    public Issue() {
    }

    public Issue(String project, int type, String summary, String description) {
        this.project = project;
        this.type = type;
        this.summary = summary;
        this.description = description;
    }

    public Issue(String project, int type, String summary, String description, String duedate) {
        this(project, type, summary, description);
        this.duedate = duedate;
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

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    @Override
    public String toString() {
        return "Issue{project='" + project + "'"
                + ", type=" + type
                + ", summary='" + summary + "'"
                + ", duedate='" + duedate + "'}";
    }
}
