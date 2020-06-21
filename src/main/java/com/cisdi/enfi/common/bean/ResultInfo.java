package com.cisdi.enfi.common.bean;

public class ResultInfo {
    private Object object;
    private boolean result;
    private String message;

    public ResultInfo() {
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

