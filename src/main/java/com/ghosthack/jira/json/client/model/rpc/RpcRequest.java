package com.ghosthack.jira.json.client.model.rpc;

import java.util.Arrays;

/**
 * Represents a JSON-RPC 2.0 request.
 */
public class RpcRequest {

    private String jsonrpc;
    private String method;
    private RpcParam[] params;
    private long id;

    public RpcRequest() {
    }

    public RpcRequest(String jsonrpc, String method, RpcParam[] params, long id) {
        this.jsonrpc = jsonrpc;
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public RpcParam[] getParams() {
        return params;
    }

    public void setParams(RpcParam[] params) {
        this.params = params;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RpcRequest{jsonrpc='" + jsonrpc + "'"
                + ", method='" + method + "'"
                + ", id=" + id
                + ", params=" + Arrays.toString(params) + "}";
    }
}
