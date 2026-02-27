package com.ghosthack.jira.json.client.model.rpc;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A JSON-RPC parameter that serializes as a bare string value.
 */
public class RpcString extends RpcParam {

    private String value;

    public RpcString() {
    }

    public RpcString(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RpcString{value='" + value + "'}";
    }
}
