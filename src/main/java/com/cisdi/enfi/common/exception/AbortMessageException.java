package com.cisdi.enfi.common.exception;

public class AbortMessageException extends PlatformException {
    public AbortMessageException() {
    }

    public AbortMessageException(String msg) {
        super(msg);
    }

    public AbortMessageException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
