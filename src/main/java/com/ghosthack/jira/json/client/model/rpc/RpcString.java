package com.ghosthack.jira.json.client.model.rpc;

import org.codehaus.jackson.annotate.JsonValue;

public class RpcString extends RpcParam {

    protected String value;

    public RpcString() {
        // TODO Auto-generated constructor stub
    }

    public RpcString(String user) {
        setValue(user);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
