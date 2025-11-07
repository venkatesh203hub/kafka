package com.example.kafka.exception;

public class KafkaExeception extends Exception {
    private final String errorCode;

    public KafkaExeception(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
