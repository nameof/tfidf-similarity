package com.nameof.tfidf.exception;

public class TFIDFException extends RuntimeException {
    public TFIDFException(String message) {
        super(message);
    }

    public TFIDFException(String message, Throwable cause) {
        super(message, cause);
    }

    public TFIDFException(Throwable cause) {
        super(cause);
    }
}
