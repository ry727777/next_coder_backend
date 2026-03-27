package com.nextcoder.backend.service;

public class ExecutionResult {
    private String output;
    private String error;

    public ExecutionResult(String output, String error) {
        this.output = output;
        this.error = error;
    }

    public String getOutput() { return output; }
    public String getError() { return error; }
}