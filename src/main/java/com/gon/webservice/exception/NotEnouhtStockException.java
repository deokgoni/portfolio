package com.gon.webservice.exception;

public class NotEnouhtStockException extends RuntimeException {

    public NotEnouhtStockException() {
        super();
    }

    public NotEnouhtStockException(String message) {
        super(message);
    }

    public NotEnouhtStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnouhtStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnouhtStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
