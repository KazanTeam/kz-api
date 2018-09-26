package com.kazan.exception;

public class ValidationCustomException extends Exception {

    private static final long serialVersionUID = -5744854964658537470L;

    private String errorCode;

    public ValidationCustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
