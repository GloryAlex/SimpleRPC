package com.glory.coder;

public class RpcResponse {
    private String id;
    private Throwable error;
    private Object result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
