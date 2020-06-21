package com.cisdi.enfi.common.exception;

public class PlatformException extends RuntimeException {
    public PlatformException() {
    }

    public PlatformException(String msg) {
        super(msg);
    }

    public PlatformException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PlatformException(Throwable cause) {
        super(cause);
    }
}
