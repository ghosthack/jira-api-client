package com.ghosthack.jira.json.client.model.rpc;

public class RpcRequest {

    protected String jsonrpc;
    protected String method;
    protected RpcParam[] params;
    protected long id;

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

}
